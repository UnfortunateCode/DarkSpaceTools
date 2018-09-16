package darkspace;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.Random;

import fatetools.FATERoll;
import generictools.GenericDistributions;

public class Rules_Given extends Rules_Base {

	private int[] counts = new int[5];
	
	public Rules_Given() {		
		int numRules;
		Rule[] rules = Rule.values();
		Random rand = new Random();
		
		
		for (int listsCounter = 0; listsCounter < theseRules.length; ++listsCounter) {
			
		}
		
		for (int rollCounter = 0; rollCounter < theseRules.length; ++rollCounter) {
			theseRules[rollCounter] = new LinkedList<>();
			numRules = Math.abs(FATERoll.getRoll());
			++counts[numRules];
			
			for (int ruleCounter = 0; ruleCounter < numRules; ++ruleCounter) {
				theseRules[rollCounter].add(rules[rand.nextInt(rules.length)]);
			}
			
		}
	}
	
	public Rules_Given(Rules_Base rules) {
		// TODO Auto-generated constructor stub
	}

	public void addRule(int dieRoll, Rule rule) {
		theseRules[dieRoll+4].add(rule);
	}
	
	public boolean containsRule(int dieRoll, Rule rule) {
		return theseRules[dieRoll+4].contains(rule);
	}
	
	public void removeRule(int dieRoll, Rule rule) {
		theseRules[dieRoll+4].removeFirstOccurrence(rule);
	}


	public static void main (String[] args) {
		Rules_Given rr;
		int sin;
		
		while(true) {

			rr = new Rules_Given();
			
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
