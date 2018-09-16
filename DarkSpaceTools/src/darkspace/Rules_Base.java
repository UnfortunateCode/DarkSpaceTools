package darkspace;

import java.util.LinkedList;

import darkspace.Rules_Base.Rule;

public abstract class Rules_Base {
	public static enum Rule { BACKFIRSTLEAST, BACKFIRSTMOST, NEXT, NEXTNEW, RANDOMCAPITAL, BACKRANDOM}
	
	@SuppressWarnings("unchecked")
	protected LinkedList<Rule>[] theseRules = new LinkedList[9];
	
	public String toString() {
		String result = "";
		for (int i = 0; i < theseRules.length; ++i) {

			result += (i-4) + ": ";
			for (Rule curRule : theseRules[i]) {
				result += curRule + " ";
			}
			result += "\n";
		}
		
		return result;
	}
	
	public LinkedList<Rule> getRule(int dieResult) throws IndexOutOfBoundsException {
		return theseRules[dieResult+4];
	}
}
