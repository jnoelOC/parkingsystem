package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket, boolean isRecurringCustomer) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

//		int inHour = ticket.getInTime().get(null);
//		int outHour = ticket.getOutTime().getHour();

		LocalDateTime inTime = ticket.getInTime();
		LocalDateTime outTime = ticket.getOutTime();

		double minutesRatio = 0;

		// TODO: Some tests are failing here. Need to check if this logic is correct
		// int duration = outHour - inHour;
		Duration duration = CalculateTime(inTime, outTime);

		double reduction = CalculateReduction(duration, isRecurringCustomer);

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {

			minutesRatio = ((double) duration.toMinutes()) / 60;
			ticket.setPrice(((duration.toHours() > 0) ? duration.toHours() : minutesRatio) * Fare.CAR_RATE_PER_HOUR
					* reduction);

			break;
		}
		case BIKE: {

			minutesRatio = ((double) duration.toMinutes()) / 60;
			ticket.setPrice(((duration.toHours() > 0) ? duration.toHours() : minutesRatio) * Fare.BIKE_RATE_PER_HOUR
					* reduction);

			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

	private double CalculateReduction(Duration duration, boolean isRecurringCustomer) {
		double reduc = 1; // without reduction

		if (duration.toMinutes() < 30) { // FreeParkUnder30Minutes
			reduc = 0;
		} else {
			if (isRecurringCustomer == true) { // 5% discount for recurring customer
				reduc = 0.95;
			}
		}

		return reduc;
	}

	private Duration CalculateTime(LocalDateTime inTime, LocalDateTime outTime) {
		// TODO Auto-generated method stub
		Duration duration;

		duration = Duration.between(inTime, outTime);

		// Duration.between(startLocalDateTime, endLocalDateTime).toMillis();
//		String.format("%d minutes %d seconds", 
//				  TimeUnit.MILLISECONDS.toMinutes(millis),
//				  TimeUnit.MILLISECONDS.toSeconds(millis) - 
//				  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

		return duration;
	}

}