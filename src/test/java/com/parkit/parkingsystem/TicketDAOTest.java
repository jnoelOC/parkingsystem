package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAOTest {
	private static TicketDAO ticketDAO;
	private Ticket ticket;
	private ParkingSpot parkingSpot;
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	@BeforeAll
	private static void setUp() {
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	public void saveTicketTest() {
		// ARRANGE
		boolean ret = false;
		parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		ticket.setId(1);
		ticket.setInTime(LocalDateTime.now().minusHours(1));
		ticket.setOutTime(LocalDateTime.now());
		ticket.setPrice(1);
		ticket.setVehicleRegNumber("AZERTY");
		ticket.setParkingSpot(parkingSpot);
		// ACT
		ret = ticketDAO.saveTicket(ticket);
		// ASSERT
		assertTrue(ret);
	}

	@Test
	public void getTicketTest() {
		// ARRANGE
		String vehicleRegNumber = "ABCDEF";
		// ACT
		ticket = ticketDAO.getTicket(vehicleRegNumber);
		// ASSERT
		assertNotNull(ticket);
	}

	@Test
	public void updateTicketTest() {
		// ARRANGE
		boolean ret = false;
		ticket.setPrice(123);
		ticket.setOutTime(LocalDateTime.now());
		ticket.setId(2);
		// ACT
		ret = ticketDAO.updateTicket(ticket);
		// ASSERT
		assertTrue(ret);
	}

}
