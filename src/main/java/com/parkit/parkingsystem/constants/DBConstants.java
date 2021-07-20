package com.parkit.parkingsystem.constants;

/**
 * 
 * This is the SQL requests for production database and tests database
 * 
 */
public class DBConstants {

	public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
	public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

//	public static final String SAVE_TICKET = "insert into ticket(ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?,?)";
	public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
	public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
	public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME DESC limit 1";
//	public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME limit 1";

	public static final String VERIFY = "select t.ID from ticket t where t.VEHICLE_REG_NUMBER=?";
	///////// TESTS
	public static final String GET_TICKET_TEST = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.PARKING_NUMBER = t.PARKING_NUMBER and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME DESC limit 1";
	public static final String GET_LAST_ID = "select MAX(ID) from ticket";

	public static final String GET_FARE_AND_OUTTIME_TEST = "select t.PRICE, t.OUT_TIME from ticket t where t.VEHICLE_REG_NUMBER = ?";
	public static final String GET_AVAILABILITY_TEST = "select p.AVAILABLE from parking p, ticket t where t.VEHICLE_REG_NUMBER = ? and t.PARKING_NUMBER=p.PARKING_NUMBER";
}
