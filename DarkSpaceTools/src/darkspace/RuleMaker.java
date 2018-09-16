package darkspace;

import fatetools.FATERoll;

public class RuleMaker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Rules_Base [] generation = new Rules_Base[30000];
		int[] score = new int[30000];
		Rules_Base [] nextGeneration = new Rules_Base[30000];
		int numGens = 0;
		int[][][] galacticInputs = new int[3][60][4]; // 3 Galaxies, 60 MemberSystemsMax, [RuleRule, Technology, Environment, Resources]
		Galaxy[] created = new Galaxy[3];
		
		for (int speciesCounter = 0; speciesCounter < generation.length; ++speciesCounter) {
			generation[speciesCounter] = new Rules_Given(new Rules_RandomFATE());
		}
		
		do {
			
			for (int galaxyAttempt = 0; galaxyAttempt < galacticInputs.length; ++galaxyAttempt) {
				for (int systemsCount = 0; systemsCount < galacticInputs[0].length; ++systemsCount) {
					for (int attribute = 0; attribute < galacticInputs[0][0].length; ++attribute) {
						galacticInputs[galaxyAttempt][systemsCount][attribute] = FATERoll.getRoll();
					}
				}
			}
			
			for (int species = 0; species < generation.length; ++species) {
				score[species] = 0;
				for (int galaxyAttempt = 0; galaxyAttempt < galacticInputs.length; ++galaxyAttempt) {
					created[galaxyAttempt] = new Galaxy(galacticInputs[galaxyAttempt]);
					
					score[species] += numNonEmpireSystems(created[galaxyAttempt]);
					score[species] += lengthNonBranching(created[galaxyAttempt]);
					score[species] += differenceFromIdealSize(created[galaxyAttempt]);
					
				}
			
				
			}
			
		} while (++numGens < 10000);
	}

}
