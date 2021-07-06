package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.integration.service.DatabasePrepareServiceParkingDao;
import com.parkit.parkingsystem.integration.service.DatabasePrepareServiceTicketDao;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;
	private static DatabasePrepareServiceTicketDao databasePrepareServiceTicketDao;
	private static DatabasePrepareServiceParkingDao databasePrepareServiceParkingDao;

	// To be tested
	private ParkingService parkingService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
		databasePrepareServiceTicketDao = new DatabasePrepareServiceTicketDao();
		databasePrepareServiceParkingDao = new DatabasePrepareServiceParkingDao();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() {
		// ARRANGE

		// ACT
		parkingService.processIncomingVehicle();
		// DONE: check that a ticket is actually saved in DB and Parking table is
		// updated with availability

		Ticket ticket = databasePrepareServiceTicketDao.getATicketFromDBTest("ABCDEF");
		boolean isAvailabilitySavedInDB = databasePrepareServiceParkingDao.saveAParkingSlot(ticket);

		// ASSERT
		assertEquals(1, ticket.getId());
		assertTrue(isAvailabilitySavedInDB);
	}

	@Test
	public void testParkingLotExit() {
		// ARRANGE
		// ACT
		testParkingACar();
		Ticket ticket1 = parkingService.processExitingVehicle();
		// TODO: check that the fare generated and out time are populated correctly in
		// the database

//		System.out.println("le in et le out time: " + ticket1.getInTime() + " " + ticket1.getOutTime()
//				+ " et le vehicle Reg nb : " + ticket1.getVehicleRegNumber());
		boolean isFareAndOutTimeUpdatedInDB = databasePrepareServiceTicketDao.getUpdatingFareAndOutTimeFromDBTest(false,
				1);
		// ASSERT
		assertTrue(isFareAndOutTimeUpdatedInDB);

	}

}
