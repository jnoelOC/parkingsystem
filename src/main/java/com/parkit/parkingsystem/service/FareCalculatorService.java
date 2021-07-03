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

		LocalDateTime inTime = ticket.getInTime();
		LocalDateTime outTime = ticket.getOutTime();

		double minutesRatio = 0;

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

	public double CalculateReduction(Duration duration, boolean isRecurringCustomer) {
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

	public Duration CalculateTime(LocalDateTime inTime, LocalDateTime outTime) {
		Duration duration;

		duration = Duration.between(inTime, outTime);

		return duration;
	}

}