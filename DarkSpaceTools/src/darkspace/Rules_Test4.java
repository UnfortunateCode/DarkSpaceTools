package darkspace;

import java.io.IOException;
import java.util.LinkedList;

public class Rules_Test4 extends Rules_Base {
	
	public Rules_Test4() {
		for (int i = 0; i < theseRules.length; ++i) {
			theseRules[i] = new LinkedList<>();
		}
		
		theseRules[1].add(Rule.BACKFIRSTLEAST);
		theseRules[1].add(Rule.NEXT);
		
		theseRules[2].add(Rule.BACKFIRSTLEAST);
		theseRules[2].add(Rule.NEXTNEW);
		
		theseRules[3].add(Rule.BACKFIRSTLEAST);
		theseRules[3].add(Rule.NEXTNEW);
		

		theseRules[4].add(Rule.NEXT);
		
		theseRules[5].add(Rule.NEXT);

		theseRules[6].add(Rule.NEXT);
		
		theseRules[7].add(Rule.NEXT);

	}

	public static void main (String[] args) {
		Rules_Test4 rr;
		int sin;
		
		while(true) {

			rr = new Rules_Test4();
			
			System.out.println(rr);
			
		
			try {
			sin = System.in.read();
			} catch (IOException e) {
				
			}
		}
	}
}
