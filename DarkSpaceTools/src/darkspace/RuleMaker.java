package darkspace;

import java.util.Random;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import fatetools.FATERoll;

public class RuleMaker {

	public static void main(String[] args) {
		int numPerGeneration = 1000;
		Rules_Given [] generation = new Rules_Given[numPerGeneration];
		double[] score = new double[generation.length];
		Rules_Given [] nextGeneration = new Rules_Given[generation.length];
		
		double genSum;
		double ranPick;
		double topScore;
		int genDisplay = 1;
		int chosen1, chosen2;
		Random r = new Random();
		int numGens = 0;
		
		Galaxy showing;
		int minSize = 0;
		int maxSize = 83;
		int idealSize = 42;
		int galacticAttempts = 5;
		int attributes = 4; // [RuleRule, Technology, Environment, Resources]
		int low, mid, high;
		Graph graph = new SingleGraph("Galaxy");
		graph.display();
		
		double topAllGensScore = 0;
		Rules_Given topAllGen = null;
		double topGenScore = 0;
		Rules_Given topGen = null;
		
		int[][][] galacticInputs = new int[galacticAttempts][maxSize][attributes]; 
		Galaxy[] created = new Galaxy[galacticAttempts];
		
		int species, galaxyAttempt, systemsCount, attribute;
		
		for (species = 0; species < generation.length; ++species) {
			generation[species] = new Rules_Given(new Rules_Test3());
		}
		
		do {
			
			for (galaxyAttempt = 0; galaxyAttempt < galacticInputs.length; ++galaxyAttempt) {
				for (systemsCount = 0; systemsCount < galacticInputs[0].length; ++systemsCount) {
					for (attribute = 0; attribute < galacticInputs[0][0].length; ++attribute) {
						galacticInputs[galaxyAttempt][systemsCount][attribute] = FATERoll.getRoll();
					}
				}
			}
			genSum = 0;
			
			for (species = 0; species < generation.length; ++species) {
				score[species] = 0;
				topGenScore = 0;
				for (galaxyAttempt = 0; galaxyAttempt < galacticInputs.length; ++galaxyAttempt) {
					created[galaxyAttempt] = new Galaxy(generation[species], minSize, maxSize, false, galacticInputs[galaxyAttempt]);
					
					score[species] += Math.min(0, created[galaxyAttempt].numNonEmpireSystems() - (int)(0.1*created[galaxyAttempt].memberSystems.length));
					score[species] += 50*created[galaxyAttempt].lengthNonBranching();
					score[species] += created[galaxyAttempt].differenceFromIdealSize(idealSize)*created[galaxyAttempt].differenceFromIdealSize(idealSize);
					score[species] += 30*Math.min(0, created[galaxyAttempt].maxConnections() - 4);
					score[species] += Math.min(0, created[galaxyAttempt].numRules() - 15);
					score[species] += 20*created[galaxyAttempt].numOverRatio(1.3);
					score[species] += 20*created[galaxyAttempt].numTriangles();
				}
				
				score[species] = 1.0 / (1.0 + score[species]); 
				
				if (score[species] > topAllGensScore) {
					topAllGensScore = score[species];
					topAllGen = generation[species];
				}
				
				if (score[species] > topGenScore) {
					topGenScore = score[species];
					topGen = generation[species];
				}
				
				genSum += (score[species] * score[species]);
				score[species] = genSum;
			}
			
			
			for (species = 0; species < generation.length; ++species) {
				ranPick = r.nextDouble() * genSum;
				
				low = 0;
				high = generation.length - 1;
				
				do {
					mid = (low + high) / 2;
					if (ranPick == score[mid]) {
						low = mid;
						high = mid;
					} else if (ranPick > score[mid]) {
						low = mid;
					} else {
						high = mid;
					}
				} while (high - low > 1);
				
				if (ranPick < score[low]) {
					chosen1 = low;
				} else {
					chosen1 =high;
				}
				
				
				ranPick = r.nextDouble();
				if (ranPick < 0.69) {
					// crossover
					if (species == generation.length - 1) {
						nextGeneration[species] = new Rules_Given(generation[chosen1]);
					} else {
						ranPick = r.nextDouble() * genSum;
						for (chosen2 = 0; chosen2 < score.length - 1; ++chosen2) {
							ranPick -= score[chosen2];
							if (ranPick < 0) {
								break;
							}
						}
						nextGeneration[species] = new Rules_Given(generation[chosen1]);
						nextGeneration[species+1] = new Rules_Given(generation[chosen2]);
						nextGeneration[species].crossoverWith(nextGeneration[++species]);
					}
					
				} else if (ranPick < 0.90) {
					// reproduction
					nextGeneration[species] = new Rules_Given(generation[chosen1]);
					
				} else if (ranPick < 0.95) {
					// mutation
					nextGeneration[species] = new Rules_Given(generation[chosen1]);
					nextGeneration[species].mutate();
				} else {
					// localSplit
					nextGeneration[species] = new Rules_Given(generation[chosen1]);
					nextGeneration[species].localSplit();
				}
				
			}
			
			
			if (numGens == genDisplay - 1) {
				System.out.println();
				
				System.out.println("Generation: " + (numGens + 1) + " (" + topGenScore + ")");
								
				genDisplay += 50;
						
				
				showing = new Galaxy(topGen, minSize, maxSize, false, galacticInputs[0]);
				showing.showGalaxy(graph);
				showing.displayGalaxy();
				
			} else {
				System.out.print(numGens + " ");
			}
			
			generation = nextGeneration;
			nextGeneration = new Rules_Given[generation.length];
			
			
		} while (++numGens < 10000);
		
		
		System.out.println();
		System.out.println("Best of All Generations (" + topAllGensScore + ")");
		showing = new Galaxy(topAllGen, minSize, maxSize, false);
		showing.showGalaxy(graph);
		showing.displayGalaxy();
		
	}
}
