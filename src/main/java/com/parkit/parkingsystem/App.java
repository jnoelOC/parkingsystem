package com.parkit.parkingsystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.service.InteractiveShell;

/**
 * This is the main of Parking system application. It permits to calculate the
 * costs of vehicles remaining into the parking system.
 * 
 * @author jean-noel.chambe
 *
 */
public class App {
	private static final Logger logger = LogManager.getLogger("App");

	public static void main(String args[]) {
		logger.info("Initializing Parking System");
		InteractiveShell.loadInterface();
	}
}
