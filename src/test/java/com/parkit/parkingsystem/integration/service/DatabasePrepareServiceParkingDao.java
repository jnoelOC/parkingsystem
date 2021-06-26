package com.parkit.parkingsystem.integration.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

public class DatabasePrepareServiceParkingDao {

	public static final String UPDATE_PARKING_SPOT_TEST = "update parking set AVAILABLE = ? where PARKING_NUMBER = ?";

	private static final Logger logger = LogManager.getLogger("DatabasePrepareServiceParkingDao");

	private DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	public boolean updateAParkingSlot(boolean available, int parkNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		Integer rs = 0;
		boolean isAvailable = false;

		try {
			con = dataBaseTestConfig.getConnection();
			ps = con.prepareStatement(UPDATE_PARKING_SPOT_TEST);
			ps.setBoolean(1, available);
			ps.setInt(2, parkNumber);
			rs = ps.executeUpdate();
			if (rs > 0) {
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
