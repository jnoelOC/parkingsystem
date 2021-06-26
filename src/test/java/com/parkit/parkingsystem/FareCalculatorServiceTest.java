package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	public void calculateFareCar() {

		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();

		// Date inTime = new Date();
		// inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		// Date outTime = new Date();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, false);
		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
	}

	@Test
	public void calculateFareBike() {

		// Date inTime = new Date();
		// inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		// Date outTime = new Date();
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, false);

		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
	}

	@Test
	public void calculateFareUnkownType() {
		// Date inTime = new Date();
		// inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		// Date outTime = new Date();
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, false));
	}

	@Test
	public void calculateFareBikeWithFutureInTime() {
		// Date inTime = new Date();
		// inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		LocalDateTime inTime = LocalDateTime.now().plusHours(1);
		// Date outTime = new Date();
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, false));
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTime() {
		// Date inTime = new Date();
		// inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes
		// parking time should give 3/4th //
		// parking fare Date
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		// Date outTime = new Date();
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, false);
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime() {
		// Date inTime = new Date();
		// inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes
		// parking time should give 3/4th //
		// parking fare Date
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		// Date outTime = new Date();
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, false);
		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithMoreThanADayParkingTime() {
		// Date inTime = new Date();
		// inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24
		// hours parking time should give 24 * //
		// parking fare per hour
		LocalDateTime inTime = LocalDateTime.now().minusDays(1);
		// Date outTime = new Date();
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, false);
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	///////////////////////////// My Tests

	@Test
	public void calculateFreeFareNotRecurringBikeWithLessThan30MinutesParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(29);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean vehicleRegNumberExists = false;
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFreeFareNotRecurringCarWithLessThan30MinutesParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(29);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean vehicleRegNumberExists = false;
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFreeFareRecurringBikeWithLessThan30MinutesParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(29);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean vehicleRegNumberExists = true;
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFreeFareRecurringCarWithLessThan30MinutesParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(29);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean vehicleRegNumberExists = true;
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFreeFareBikeWith30MinutesParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean vehicleRegNumberExists = false;
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((0.5 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFreeFareCarWith30MinutesParkingTime() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean vehicleRegNumberExists = false;
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((0.5 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareRecurringCarWith5PercentDiscount() {

		LocalDateTime inTime = LocalDateTime.now().minusHours(10);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean vehicleRegNumberExists = true;
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((9.5 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareRecurringBikeWith5PercentDiscount() {

		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean vehicleRegNumberExists = true;
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((0.95 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareNotRecurringCarWithout5PercentDiscount() {

		LocalDateTime inTime = LocalDateTime.now().minusHours(10);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean vehicleRegNumberExists = false;

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((10 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareNotRecurringBikeWithout5PercentDiscount() {

		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean vehicleRegNumberExists = false;

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((1 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareNotRecurringCarWithGreatTime() {

		LocalDateTime inTime = LocalDateTime.MIN;
		LocalDateTime outTime = LocalDateTime.MAX;
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean vehicleRegNumberExists = false;

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((8759.983333333334 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareNotRecurringBikeWithGreatTime() {

		LocalDateTime inTime = LocalDateTime.MIN;
		LocalDateTime outTime = LocalDateTime.MAX;
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean vehicleRegNumberExists = false;

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, vehicleRegNumberExists);

		assertEquals((8759.983333333334 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}
}
