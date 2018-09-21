package darkspace;

import java.io.IOException;
import java.util.LinkedList;

public class Rules_Test3 extends Rules_Base {
	
	public Rules_Test3() {
		for (int i = 0; i < theseRules.length; ++i) {
			theseRules[i] = new LinkedList<>();
		}
		theseRules[0].add(Rule.BACKFIRSTLEAST);
		
		theseRules[1].add(Rule.BACKFIRSTLEAST);
				
		theseRules[3].add(Rule.NEXT);

		theseRules[4].add(Rule.NEXTNEW);
		
		theseRules[5].add(Rule.NEXTNEW);

		theseRules[6].add(Rule.NEXT);
		theseRules[6].add(Rule.NEXTNEW);
		theseRules[6].add(Rule.NEXTNEW);
		
		theseRules[7].add(Rule.NEXT);
		theseRules[7].add(Rule.NEXTNEW);
		theseRules[7].add(Rule.NEXTNEW);

		theseRules[8].add(Rule.NEXT);
		theseRules[8].add(Rule.NEXTNEW);
		theseRules[8].add(Rule.NEXTNEW);
	}

	public static void main (String[] args) {
		Rules_Test3 rr;
		int sin;
		
		while(true) {

			rr = new Rules_Test3();
			
			System.out.println(rr);
			
		
			try {
			sin = System.in.read();
			} catch (IOException e) {
				
			}
		}
	}
}
