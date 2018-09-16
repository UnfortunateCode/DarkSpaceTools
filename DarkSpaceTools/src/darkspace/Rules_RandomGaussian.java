package darkspace;

import java.util.LinkedList;
import java.util.Random;

import darkspace.Rules_Base.Rule;
import generictools.GenericDistributions;

public class Rules_RandomGaussian extends Rules_Base {

		public Rules_RandomGaussian() {		
		int numRules;
		Rule[] rules = Rule.values();
		Random rand = new Random();
		
		for (int listsCounter = 0; listsCounter < theseRules.length; ++listsCounter) {
			
		}
		
		for (int rollCounter = 0; rollCounter < theseRules.length; ++rollCounter) {
			theseRules[rollCounter] = new LinkedList<>();
			numRules = GenericDistributions.nextSkewedBoundedInt(0, 5, 1, -0.6);
			
			for (int ruleCounter = 0; ruleCounter < numRules; ++ruleCounter) {
				theseRules[rollCounter].add(rules[rand.nextInt(rules.length)]);
			}
			
		}
	}
	
	public static void main (String[] args) {
		Rules_RandomGaussian rr = new Rules_RandomGaussian();
		
		System.out.println(rr);
	}


}
