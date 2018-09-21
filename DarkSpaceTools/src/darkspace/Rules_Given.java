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
		
		for (int rollCounter = 0; rollCounter < theseRules.length; ++rollCounter) {
			theseRules[rollCounter] = new LinkedList<>();
			numRules = Math.abs(FATERoll.getRoll());
			++counts[numRules];
			
			for (int ruleCounter = 0; ruleCounter < numRules; ++ruleCounter) {
				theseRules[rollCounter].add(rules[rand.nextInt(rules.length)]);
			}
			
		}
	}
	
	public Rules_Given(boolean clear) {
		for (int rollCounter = 0; rollCounter < theseRules.length; ++rollCounter) {
			theseRules[rollCounter] = new LinkedList<>();
		}
	}
	
	public Rules_Given(Rules_Base rules) {
		for (int rollCounter = 0; rollCounter < theseRules.length; ++ rollCounter) {
			theseRules[rollCounter] = (LinkedList<Rule>)rules.theseRules[rollCounter].clone();
		}
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
		Rules_Given r1, r2;
		int sin;
		
		while(true) {

			r1 = new Rules_Given();
			r2 = new Rules_Given();
			
			System.out.println(r1);
			System.out.println(r2);
			
			System.out.println("Crossover:");
			r1.crossoverWith(r2);

			System.out.println(r1);
			System.out.println(r2);
			
			System.out.println("Mutations:");
			r1.mutate();
			r2.mutate();

			System.out.println(r1);
			System.out.println(r2);
			
			System.out.println("Local Split:");
			r1.localSplit();
			r2.localSplit();

			System.out.println(r1);
			System.out.println(r2);
			
			try {
			sin = System.in.read();
			} catch (IOException e) {
				
			}
		}
	}

	public void crossoverWith(Rules_Base rules) {
		Random r = new Random();
		
		int index1 = r.nextInt(9);
		int index2 = r.nextInt(9);
		
		LinkedList<Rule> toSwitch = theseRules[index1];
		theseRules[index1] = rules.theseRules[index2];
		rules.theseRules[index2] = toSwitch;
		
		
	}

	public void mutate() {
		Rules_Given newRandRules = new Rules_Given();
		Random r = new Random();
		
		theseRules[r.nextInt(9)] = newRandRules.theseRules[r.nextInt(9)];
		
	}

	public void localSplit() {

		Random r = new Random();
		int index = r.nextInt(9);
		
		
		LinkedList<Rule> toSplit = theseRules[index];
		LinkedList<Rule> fromSplit;
		theseRules[index] = new LinkedList<>();
		
		while (!toSplit.isEmpty()) {
			fromSplit = theseRules[r.nextInt(9)];
			if (!fromSplit.isEmpty()) {
				theseRules[index].add(fromSplit.remove(r.nextInt(fromSplit.size())));
			} 
			fromSplit.add(toSplit.pop());
		}
		
	}

}
