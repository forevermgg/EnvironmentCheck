package com.mgg.environmentcheck;

public class DoubleConversion {
	public static final String doubleToString(double value) {
		return nativeDoubleToString(value);
	}
	
	private static native String nativeDoubleToString(double value);
	
	public static final double stringToDouble(String value) {
		return nativeStringToDouble(value);
	}
	
	private static native double nativeStringToDouble(String value);
}
