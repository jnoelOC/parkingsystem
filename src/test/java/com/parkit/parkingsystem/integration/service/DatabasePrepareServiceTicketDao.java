package com.parkit.parkingsystem.integration.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class DatabasePrepareServiceTicketDao {

	private static final Logger logger = LogManager.getLogger("DatabasePrepareServiceTicketDao");

	public static final String GET_TICKET_TEST = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.PARKING_NUMBER = t.PARKING_NUMBER and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME DESC limit 1";
	public static final String UPDATE_PARKING_SPOT_TEST = "update parking set AVAILABLE = ? where PARKING_NUMBER = ?";

	private DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	public Ticket getATicketFromDBTest(String vehicleRegNumber) {

		Connection con = null;
		Ticket ticket = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = dataBaseTestConfig.getConnection();
			ps = con.prepareStatement(GET_TICKET_TEST);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(LocalDateTime.of(rs.getDate(4).toLocalDate(), rs.getTime(4).toLocalTime()));
			}
		} catch (SQLException sqlEx) {
			logger.error("Error SQL ", sqlEx);

		} catch (NullPointerException npEx) {
			logger.error("Error Null pointer ", npEx);

		} catch (Exception ex) {
			logger.error("Error fetching ", ex);

		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeResultSet(rs);
			dataBaseTestConfig.closeConnection(con);
		}

		return ticket;
	}

	public boolean getUpdatingFareAndOutTimeFromDBTest(boolean available, int parkNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer result = 0;
		boolean areTheySaved = true;

		try {
			con = dataBaseTestConfig.getConnection();
			ps = con.prepareStatement(UPDATE_PARKING_SPOT_TEST);
			ps.setBoolean(1, available);
			ps.setInt(2, parkNumber);

			// result = ps.executeUpdate();
			ps.execute();
//			if (result > 0) {
//
//				System.out.println("the fare generated and out time are populated correctly in the database");
//				areTheySaved = true;
//			} else {
//				System.out.println("the fare generated and out time aren't populated correctly in the database");
//			}
		} catch (SQLException sqlEx) {
			logger.error("Error SQL ", sqlEx);

		} catch (NullPointerException npEx) {
			logger.error("Error Null pointer ", npEx);

		} catch (Exception ex) {
			logger.error("Error fetching ", ex);

		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeResultSet(rs);
			dataBaseTestConfig.closeConnection(con);
		}

		return areTheySaved;
	}

}
