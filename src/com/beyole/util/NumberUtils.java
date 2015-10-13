package com.beyole.util;

/**
 * 梯度数据
 * 
 * @author Iceberg
 * 
 */
public class NumberUtils {

	public static void main(String[] args) {
		float val = 1;
		float s = 0.85f;
		int i = 0;
		s = (float) Math.sqrt(1 / s);
		System.out.println(val);
		while (i < 5) {
			val = val * s;
			System.out.println(val);
			i++;
		}
		s = 0.85f;
		i = 0;
		s = (float) Math.sqrt(s);
		while (i < 5) {
			val = val * s;
			System.out.println(val);
			i++;
		}
	}
}
