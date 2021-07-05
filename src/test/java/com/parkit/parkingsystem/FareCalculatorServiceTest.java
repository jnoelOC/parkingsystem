package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

		assertEquals((1.7531639991215E13 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
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

		assertEquals((1.7531639991215E13 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	//////////// MY PARAMETERIZED TESTS : calculateReduction()

	@ParameterizedTest
	@MethodSource("com.parkit.parkingsystem.DurationAndBooleanProviders1#durationAndBoolean1")
	public void CalculateReductionTest_ShouldReturn1(Duration duration, boolean isRecurringCustomer) {

		assertEquals(1, fareCalculatorService.CalculateReduction(duration, isRecurringCustomer));
	}

	@ParameterizedTest
	@MethodSource("com.parkit.parkingsystem.DurationAndBooleanProviders0#durationAndBoolean0")
	public void CalculateReductionTest_ShouldReturn0(Duration duration, boolean isRecurringCustomer) {

		assertEquals(0, fareCalculatorService.CalculateReduction(duration, isRecurringCustomer));
	}

	@ParameterizedTest
	@MethodSource("com.parkit.parkingsystem.DurationAndBooleanProviders095#durationAndBoolean095")
	public void CalculateReductionTest_ShouldReturn095(Duration duration, boolean isRecurringCustomer) {

		assertEquals(0.95, fareCalculatorService.CalculateReduction(duration, isRecurringCustomer));
	}

	//////////// MY PARAMETERIZED TESTS : calculateTime()

	@ParameterizedTest
	@MethodSource("com.parkit.parkingsystem.LocalDateTimeProviders#LoDaTi")
	public void CalculateTimeTest_ShouldReturn0Time(LocalDateTime ldt1, LocalDateTime ldt2) {

		Duration dur = fareCalculatorService.CalculateTime(ldt1, ldt2);
		assertEquals(Duration.ZERO, dur);

	}
}

class LocalDateTimeProviders {
	static Stream<Arguments> LoDaTi() {
		return Stream.of(Arguments.of(LocalDateTime.now(), LocalDateTime.now()),
		// return a great figure of duration
//				Arguments.of(LocalDateTime.of(LocalDate.MIN, LocalTime.MIN),
//						LocalDateTime.of(LocalDate.MAX, LocalTime.MAX)),
				Arguments.of(LocalDateTime.MIN, LocalDateTime.MIN), Arguments.of(LocalDateTime.MAX, LocalDateTime.MAX));
	}
}

class DurationAndBooleanProviders1 {
	static Stream<Arguments> durationAndBoolean1() {
		return Stream.of(Arguments.of(Duration.ofMinutes(30), false), Arguments.of(Duration.ofHours(1), false),
				Arguments.of(Duration.ofHours(10), false), Arguments.of(Duration.ofHours(Integer.MAX_VALUE), false));
	}
}

class DurationAndBooleanProviders0 {
	static Stream<Arguments> durationAndBoolean0() {
		return Stream.of(Arguments.of(Duration.ofMinutes(1), false), Arguments.of(Duration.ofMinutes(29), false),
				Arguments.of(Duration.ZERO, false), Arguments.of(Duration.ofHours(Integer.MIN_VALUE), false));
	}
}

class DurationAndBooleanProviders095 {
	static Stream<Arguments> durationAndBoolean095() {
		return Stream.of(Arguments.of(Duration.ofMinutes(30), true), Arguments.of(Duration.ofHours(123), true),
				// Arguments.of(Duration.ofHours(Long.MAX_VALUE), true), ERROR
				Arguments.of(Duration.ofHours(Integer.MAX_VALUE), true));
	}
}
