package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		int inHour = ticket.getInTime().getHour();
		int outHour = ticket.getOutTime().getHour();
		// System.out.println(inHour);
		// System.out.println(outHour);
		int inMinutes = ticket.getInTime().getMinute();
		int outMinutes = ticket.getOutTime().getMinute();
		int inDays = ticket.getInTime().getDayOfYear();
		int outDays = ticket.getOutTime().getDayOfYear();

		// TODO: Some tests are failing here. Need to check if this logic is correct
		// int duration = outHour - inHour;
		double duration = CalculateTime(inMinutes, outMinutes, inHour, outHour, inDays, outDays);

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
			break;
		}
		case BIKE: {
			ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

	private double CalculateTime(int inMinutes, int outMinutes, int inHour, int outHour, int inDays, int outDays) {
		// TODO Auto-generated method stub
		double totalTime = 0;
		double totalDays = outDays - inDays;
		totalDays *= 24; // divide by 24 hours
		double totalHours = outHour - inHour;
		double totalMinutes = outMinutes - inMinutes;

		totalTime = CalculateFreeParkUnder30Minutes(totalMinutes, totalHours, totalDays);

		return totalTime;
	}

	private double CalculateFreeParkUnder30Minutes(double totalMinutes, double totalHours, double totalDays) {
		// TODO Auto-generated method stub
		double totalTime = 0;

		if (totalDays == 0 && totalHours == 0 && totalMinutes < 30) {
			totalTime = 0; // case where free park under 30 minutes
		} else {
			totalMinutes /= 60; // divide by 60 minutes ( = 1 hour )
			totalTime = totalDays + totalHours + totalMinutes;
		}

		return totalTime;
	}
}