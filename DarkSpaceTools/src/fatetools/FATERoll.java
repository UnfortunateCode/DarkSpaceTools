package fatetools;

import java.util.Random;

/**
 * Simple roller for FATE. A FATE die has 1/3rd chance of -1,
 * a 1/3rd chance of +0, and 1/3rd chance of +1. Four dice are
 * rolled together to get a value betwee -4 and +4.
 * 
 * @author UnfortunateCode
 */
public class FATERoll {
	private static Random rollRandom = new Random();
	
	/**
	 * No modifier roll using standard percentages.
	 * 
	 * @return the result of the roll
	 */
	public static int getRoll() {
		// using one random is about 2.3x faster than summing 4 randoms
		
		int perc = rollRandom.nextInt(81); 
		
		if (perc < 1) {
			return -4;
		} else if (perc < 5) {
			return -3;
		} else if (perc < 15) {
			return -2;
		} else if (perc < 31) {
			return -1;
		} else if (perc < 50) {
			return 0;
		} else if (perc < 66) {
			return 1;
		} else if (perc < 76) {
			return 2;
		} else if (perc < 80) {
			return 3;
		} else {
			return 4;
		}
	}
	
	private static int getActualRoll() {
		return rollRandom.nextInt(3) + rollRandom.nextInt(3) + rollRandom.nextInt(3) + rollRandom.nextInt(3) - 4;
	}
	
	/*
	public static void main(String[] args) {
		
		// Check speed of implementations
		int[][] distribution = new int[2][9];
		long timing;
		
		timing = System.currentTimeMillis();
		for (int i = 0; i < 100000000; ++i) {
			++distribution[0][getRoll()+4];
		}
		timing = System.currentTimeMillis() - timing;
		System.out.println("if/else: " + timing);
		
		timing = System.currentTimeMillis();
		for (int i = 0; i < 100000000; ++i) {
			++distribution[1][getActualRoll()+4];
		}
		timing = System.currentTimeMillis() - timing;
		System.out.println("4xRand: " + timing);
		
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 9; ++j) {
				System.out.print(distribution[i][j] + " ");
			}
			System.out.println();
		}
	}*/

}
