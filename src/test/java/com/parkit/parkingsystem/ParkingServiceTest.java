package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	// to be tested
	private static ParkingService parkingService;

	// to be mocked
	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeEach
	private void setUpPerTest() {
		try {
//			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

//			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
//			Ticket ticket = new Ticket();
//
//			LocalDateTime inTime = LocalDateTime.now().minusHours(1);
//			ticket.setInTime(inTime);
//
//			ticket.setParkingSpot(parkingSpot);
//			ticket.setVehicleRegNumber("ABCDEF");
//			when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
//			when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
//
//			when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
//
//			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@Test
	public void processExitingVehicleTest() throws Exception {
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		ticket.setInTime(inTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		parkingService.processExitingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
	}

	@Test
	public void processExitingBikeTest() throws Exception {
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		Ticket ticket = new Ticket();
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(40);
		ticket.setInTime(inTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processExitingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
	}

	@Test
	public void getVehichleTypeTest_ReturnsBIKE() {
		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(2);
		parkingService = new ParkingService(inputReaderUtil, null, null);
		// ACT
		ParkingType ps = parkingService.getVehichleType();
		// ASSERT
		assertEquals("BIKE", ps.toString());
	}

	@Test
	public void getVehichleTypeTest_ReturnsCAR() {
		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);
		parkingService = new ParkingService(inputReaderUtil, null, null);
		// ACT
		ParkingType ps = parkingService.getVehichleType();
		// ASSERT
		assertEquals("CAR", ps.toString());
	}

	@Test
	public void getNextParkingNumberIfAvailable_ReturnsAvailable() {
		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(3);

		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		// ACT
		ParkingSpot ps = parkingService.getNextParkingNumberIfAvailable();
		// ASSERT
		assertTrue(ps.isAvailable());
	}

}
