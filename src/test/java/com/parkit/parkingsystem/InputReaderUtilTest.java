package com.parkit.parkingsystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.parkit.parkingsystem.util.InputReaderUtil;

public class InputReaderUtilTest {

	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() {
		inputReaderUtil = new InputReaderUtil();
	}

	@BeforeEach
	private void setUpPerTest() {

	}

	//////////////////////////////////////////////////////////
	//////////// MY PARAMETERIZED TESTS : readSelection()

//	@Test
////	@ParameterizedTest
////	@MethodSource("StringReturnInteger")
//	public void readSelectionTest() {
//		Integer in = 3;
//		System.out.println("Tapez 3, svp.");
//		assertEquals(in, inputReaderUtil.readSelection());
//	}
//
//	// with its data
////	private static Stream<Arguments> StringReturnInteger() {
////		return Stream.of(Arguments.of("0"), Arguments.of("-1"), Arguments.of("111"), Arguments.of("9999999999999999"));
////	}
//
//	@Test
//	public void readVehicleRegistrationNumberTest() throws Exception {
//		String str = "ABCDEF";
//		System.out.println("Tapez ABCDEF, svp.");
//		assertEquals(str, inputReaderUtil.readVehicleRegistrationNumber());
//	}

}
