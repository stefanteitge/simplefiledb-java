package de.stefanteitge.kwery.internal;

import java.util.Arrays;

public class KweryUtil {
	
	private KweryUtil() {
	}
	
	public static <R> R[] concat(R[] first, R[] second) {
		R[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}
