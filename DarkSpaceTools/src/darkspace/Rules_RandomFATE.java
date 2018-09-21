package darkspace;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.Random;

import fatetools.FATERoll;
import generictools.GenericDistributions;

public class Rules_RandomFATE extends Rules_Base {

	private int[] counts = new int[5];
	
	public Rules_RandomFATE() {		
		int numRules;
		Rule[] rules = Rule.values();
		Random rand = new Random();
		
		for (int rollCounter = 0; rollCounter < theseRules.length; ++rollCounter) {
			theseRules[rollCounter] = new LinkedList<>();
			numRules = Math.abs(FATERoll.getRoll());
			++counts[numRules];
			
			for (int ruleCounter = 0; ruleCounter < numRules; ++ruleCounter) {
				theseRules[rollCounter].add(rules[rand.nextInt(rules.length)]);
			}
			
		}
	}



	public static void main (String[] args) {
		Rules_RandomFATE rr;
		int sin;
		
		while(true) {

			rr = new Rules_RandomFATE();
			
			System.out.println(rr);
			

			for (int i = 0; i < 5; ++i) {
				System.out.print(rr.counts[i] + " ");
			}
			System.out.println();
			
			try {
			sin = System.in.read();
			} catch (IOException e) {
				
			}
		}
	}

}
