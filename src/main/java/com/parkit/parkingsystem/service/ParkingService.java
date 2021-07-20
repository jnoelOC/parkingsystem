package com.parkit.parkingsystem.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

/**
 * This class processes the incoming and exiting vehicle
 * 
 * @author jean-noel.chambe
 *
 */
public class ParkingService {

	private static final Logger logger = LogManager.getLogger("ParkingService");

	private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

	private InputReaderUtil inputReaderUtil;
	private ParkingSpotDAO parkingSpotDAO;
	private TicketDAO ticketDAO;

	// check if customer is recurring in order to calculate 5% discount
	private boolean isRecurringCustomer = false;

	public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
		this.inputReaderUtil = inputReaderUtil;
		this.parkingSpotDAO = parkingSpotDAO;
		this.ticketDAO = ticketDAO;
	}

	/**
	 * This method processes incoming vehicle.
	 *
	 */
	public void processIncomingVehicle() {
		try {
			ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
			if (parkingSpot != null && parkingSpot.getId() > 0) {
				String vehicleRegNumber = getVehichleRegNumber();

				// if true then calculate 5% discount
				isRecurringCustomer = verifyExistenceOfVehicleRegNumber(vehicleRegNumber);

				parkingSpot.setAvailable(false);
				parkingSpotDAO.updateParking(parkingSpot);// allot this parking space and mark it's availability as
															// false

				LocalDateTime inTime = LocalDateTime.now();
				Ticket ticket = new Ticket();
				// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
				// ticket.setId(ticketID);
				// Fetch the last id into database
				// ticket.setId(parkingSpotDAO.getTheLastIdFromDB() + 1);
				ticket.setParkingSpot(parkingSpot);
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(0);
				ticket.setInTime(inTime);
				ticket.setOutTime(null);

				ticketDAO.saveTicket(ticket);
				System.out.println("Generated Ticket and saved in DB");
				System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
				System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);
			}
		} catch (Exception e) {
			logger.error("Unable to process incoming vehicle", e);
		}
	}

	/**
	 * 
	 * This method read to keyboard the vehicle registration number.
	 * 
	 * @return string VehicleRegistrationNumber
	 * @throws Exception
	 */
	private String getVehichleRegNumber() throws Exception {
		System.out.println("Please type the vehicle registration number and press enter key");
		return inputReaderUtil.readVehicleRegistrationNumber();
	}

	/**
	 * This method checks the existence of vehicle registration number in database.
	 * It displays too a message for recurring customers if it needs
	 * 
	 * @param String vehicleRegNumber
	 * @return boolean vehicleRegNumberExists
	 */
	private boolean verifyExistenceOfVehicleRegNumber(String vehicleRegNumber) {
		boolean vehicleRegNumberExists = false;
		DataBaseConfig dataBaseConfig = new DataBaseConfig();
		Connection con = null;

		// verify if vehicleRegNumber exists in database
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.VERIFY);
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Integer val = rs.getInt(1);
				if (val > 0) {
					vehicleRegNumberExists = true;
				}
			}
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
		} catch (Exception ex) {
			logger.error("Error fetching next available slot : ", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}

		if (vehicleRegNumberExists == true) {
			System.out.println(
					"Welcome back ! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
		}

		return vehicleRegNumberExists;
	}

	/**
	 * 
	 * This method gets next parking number if available
	 * 
	 * @return ParkingSpot parkingSpot
	 */
	public ParkingSpot getNextParkingNumberIfAvailable() {
		int parkingNumber = 0;
		ParkingSpot parkingSpot = null;
		try {
			ParkingType parkingType = getVehichleType();
			parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
			if (parkingNumber > 0) {
				parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
			} else {
				throw new Exception("Error fetching parking number from DB. Parking slots might be full");
			}
		} catch (IllegalArgumentException iae) {
			logger.error("Error parsing user input for type of vehicle", iae);
		} catch (Exception e) {
			logger.error("Error fetching next available parking slot", e);
		}
		return parkingSpot;
	}

	/**
	 * 
	 * This method gets vehicle type (CAR or BIKE).
	 * 
	 * @return ParkingType
	 */
	public ParkingType getVehichleType() {
		System.out.println("Please select vehicle type from menu");
		System.out.println("1 CAR");
		System.out.println("2 BIKE");
		int input = inputReaderUtil.readSelection();
		switch (input) {
		case 1: {
			return ParkingType.CAR;
		}
		case 2: {
			return ParkingType.BIKE;
		}
		default: {
			System.out.println("Incorrect input provided");
			throw new IllegalArgumentException("Entered input is invalid");
		}
		}
	}

	/**
	 * 
	 * This method processes exiting vehicle.
	 * 
	 * @return Ticket ticket
	 */
	public Ticket processExitingVehicle() {
		try {
			String vehicleRegNumber = getVehichleRegNumber();
			Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
			LocalDateTime outTime = LocalDateTime.now();
			ticket.setOutTime(outTime);
			fareCalculatorService.calculateFare(ticket, isRecurringCustomer);

			if (ticketDAO.updateTicket(ticket)) {
				ParkingSpot parkingSpot = ticket.getParkingSpot();
				parkingSpot.setAvailable(true);
				parkingSpotDAO.updateParking(parkingSpot);

				System.out.println("Please pay the parking fare:" + ticket.getPrice());
				System.out.println(
						"Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is: " + outTime);

			} else {
				System.out.println("Unable to update ticket information. Error occurred");
			}
			return ticket;
		} catch (NullPointerException npe) {
			logger.error("Unable to process exiting vehicle : null pointer", npe);
		} catch (Exception e) {
			logger.error("Unable to process exiting vehicle", e);
		}
		return null;

	}
}
