package com.parkit.parkingsystem.integration.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.Ticket;

/**
 * 
 * Service of preparation of database for ParkingDAO. Instancing a
 * dataBaseTestConfig
 * 
 **/
public class DatabasePrepareServiceParkingDao {

	private static final Logger logger = LogManager.getLogger("DatabasePrepareServiceParkingDao");

	private DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	/**
	 * Get availability of a parking slot
	 * 
	 * @param Ticket ticket
	 * 
	 * @return boolean isUnavailable
	 */
	public boolean getAvailabilityOfAParkingSlot(Ticket ticket) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean isUnavailable = false;

		try {
			con = dataBaseTestConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_AVAILABILITY_TEST);
			ps.setString(1, ticket.getVehicleRegNumber());
			rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getBoolean(1) == false) {
					isUnavailable = true;
				}
			}
		} catch (SQLException sqlEx) {
			logger.error("Error SQL ", sqlEx.getMessage());

		} catch (NullPointerException npEx) {
			logger.error("Error Null pointer ", npEx);

		} catch (Exception ex) {
			logger.error("Error fetching ", ex);

		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeResultSet(rs);
			dataBaseTestConfig.closeConnection(con);
		}

		return isUnavailable;
	}

}
