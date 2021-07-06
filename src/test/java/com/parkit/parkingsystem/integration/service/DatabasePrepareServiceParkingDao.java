package com.parkit.parkingsystem.integration.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.Ticket;

public class DatabasePrepareServiceParkingDao {

	public static final String SAVE_TICKET_TEST = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";

	private static final Logger logger = LogManager.getLogger("DatabasePrepareServiceParkingDao");

	private DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	public boolean saveAParkingSlot(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		Integer rs = 0;
		boolean isAvailable = false;

		try {
			con = dataBaseTestConfig.getConnection();
			ps = con.prepareStatement(SAVE_TICKET_TEST);
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, Timestamp.valueOf(ticket.getInTime()));
			ps.setTimestamp(5, Timestamp.valueOf(ticket.getOutTime()));

			rs = ps.executeUpdate();
			if (rs > 0) {
				System.out.println("the fare generated and out time are populated correctly in the database");
//				areTheySaved = true;
				isAvailable = true;
			}
		} catch (SQLException sqlEx) {
			logger.error("Error SQL ", sqlEx.getMessage());

		} catch (NullPointerException npEx) {
			logger.error("Error Null pointer ", npEx);

		} catch (Exception ex) {
			logger.error("Error fetching ", ex);

		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeConnection(con);
		}

		return isAvailable;
	}

}
