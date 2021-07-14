package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	/*
	 * * This method allows to calculate fare according to ticket information
	 * (inTime and outTime, vehicle type).
	 * 
	 * @param A Ticket parameter and a boolean parameter indicating if recurring
	 * customer
	 * 
	 */
	public void calculateFare(Ticket ticket, boolean isRecurringCustomer) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		LocalDateTime inTime = ticket.getInTime();
		LocalDateTime outTime = ticket.getOutTime();

		double minutesRatio = 0;

		Duration duration = calculateTime(inTime, outTime);

		double reduction = calculateReduction(duration, isRecurringCustomer);

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

	/*
	 * * This method allows to calculate discounts according to a Duration variable.
	 * 
	 * @param A Duration parameter and a boolean parameter indicating if recurring
	 * customer
	 * 
	 * @return A double is returned If without reduction thus 1 If under 30 minutes
	 * then free park thus 0 If recurring customer thus 0.95 (i.e. 5% discount)
	 * 
	 */
	public double calculateReduction(Duration duration, boolean isRecurringCustomer) {
		// without reduction
		double reduc = 1;
		// FreeParkUnder30Minutes
		if (duration.toMinutes() < 30) {
			reduc = 0;
		} else {
			// 5% discount for recurring customer
			if (isRecurringCustomer == true) {
				reduc = 0.95;
			}
		}

		return reduc;
	}

	/*
	 * * This method allows to calculate time between two LocalDateTime variables
	 * 
	 * @param Two LocalDateTime parameters
	 * 
	 * @return A variable of Duration type is returned
	 * 
	 */
	public Duration calculateTime(LocalDateTime inTime, LocalDateTime outTime) {
		Duration duration;

		duration = Duration.between(inTime, outTime);

		return duration;
	}

}