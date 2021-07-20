package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;

/**
 * This class tests the methods of Parking (getNextAvailableSlotTest and
 * updateParkingTest).
 * 
 * @author jean-noel.chambe
 *
 */

public class ParkingSpotDAOTest {
	private static ParkingSpotDAO parkingSpotDAO;
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	@BeforeAll
	private static void setUp() {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
	}

	@BeforeEach
	private void setUpPerTest() {
	}

	/**
	 * This method tests the next available slot
	 */
	@Test
	public void getNextAvailableSlotTest() {
		// ARRANGE
		Integer nb = 0;
		ParkingType parkingType = ParkingType.BIKE;
		// ACT
		nb = parkingSpotDAO.getNextAvailableSlot(parkingType);
		// ASSERT
		assertNotNull(nb);
	}

	/**
	 * This method tests updating of parking spot.
	 */
	@Test
	public void updateParkingTest() {
		// ARRANGE
		boolean ret = false;
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		// ACT
		ret = parkingSpotDAO.updateParking(parkingSpot);
		// ASSERT
		assertTrue(ret);
	}

}
