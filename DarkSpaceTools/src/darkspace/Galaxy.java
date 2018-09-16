package darkspace;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import darkspace.Rules_Base.Rule;
import fatetools.FATERoll;

/**
 * Contains a galaxy of connected systems.
 * 
 * @author UnfortunateCode
 */
public class Galaxy {
	MemberSystem[] memberSystems;
	Empire[] empires;
	Rules_Base galacticRules;
	int[] rulesChosen ;
	
	public Galaxy() {
		buildGalaxy(new Rules_RandomFATE(), 25, 60);
	}
	
	public Galaxy(Rules_Base rules, int minSize, int maxSize) {
		buildGalaxy(rules, minSize, maxSize);
		
	}
	
	private void buildGalaxy(Rules_Base rules, int minSize, int maxSize) {
		galacticRules = rules;
		memberSystems = new MemberSystem[maxSize];
		rulesChosen = new int[maxSize];
		
//System.err.println(galacticRules);
//System.err.println(minSize + " - " + maxSize);
		
		memberSystems[0] = new MemberSystem("0");
		int currentSystem = 0;
		int lastSystem = 0;
		LinkedList<Rule> currentRules;
		
		for (int ruleChoice = 0; ruleChoice < maxSize; ++ruleChoice) {
			rulesChosen[ruleChoice] = FATERoll.getRoll();
		}
		
		while (currentSystem < minSize || (currentSystem < memberSystems.length && memberSystems[currentSystem] != null)) {
			if (memberSystems[currentSystem] == null) {
				memberSystems[currentSystem] = new MemberSystem("" + currentSystem);
			}
			
//System.err.println("Working on " + memberSystems[currentSystem] + ": " + memberSystems[currentSystem].printConnections());
			
			currentRules = galacticRules.getRule(rulesChosen[currentSystem]);
			
			for (Rule rr : currentRules) {
//System.err.println("Enforcing " + rr);
				switch (rr) {
				case BACKFIRSTLEAST:
					lastSystem = processBackFirstLeast(currentSystem, lastSystem);
					break;
				case BACKFIRSTMOST:
					lastSystem = processBackFirstMost(currentSystem, lastSystem);
					break;
				case NEXT:
					lastSystem = processNext(currentSystem, lastSystem);
					break;
				case NEXTNEW:
					lastSystem = processNextNew(currentSystem, lastSystem);
					break;
				case BACKRANDOM:
					lastSystem = processBackRandom(currentSystem, lastSystem);
					break;
				}
			}
			
			++currentSystem;
			
		}
		
		if (currentSystem < memberSystems.length) {
			MemberSystem[] newArr = new MemberSystem[currentSystem];
			
			for (int i = 0; i < newArr.length; ++i) {
				newArr[i] = memberSystems[i];
			}
			
			memberSystems = newArr;
		}
		
		buildEmpires();
	}
	
	private void buildEmpires() {
		EmpireNames empName = new EmpireNames();
		MemberSystem ms;
		int capability = 0;
		
		for (int sysCount = 0; sysCount < memberSystems.length; ++sysCount) {
			ms = memberSystems[sysCount];
			
			if (ms.getEmpireCapability() > 2) {
				 ms.addEmpire(empName.getNextName());
				 ms.setEmpirePower(ms.getEmpireCapability());
				 ms.setCapital(true);
				 
				 capability = Math.max(capability, ms.getEmpirePower());
			}
		}
		
		if (capability == 0) {
			return;
		}
		
		for (; capability > 0; --capability) {
			for (int sysCount = 0; sysCount < memberSystems.length; ++sysCount) {
				ms = memberSystems[sysCount];
				
				if (ms.getEmpirePower() == capability) {
					ms.spreadEmpire();
				}
			}
		}
		
		empires = new Empire[(int)empName.getCount()];
		
		for (int sysCount = 0; sysCount < memberSystems.length; ++sysCount) {
			
		}
		
	}
	
	private int processBackFirstLeast(int currentSystem, int lastSystem) {
		int connections = memberSystems.length;
		MemberSystem ms = null;
		
		for (int nextPotential = 0; nextPotential < currentSystem; ++nextPotential) {
			if (!memberSystems[currentSystem].isLinkedTo(memberSystems[nextPotential])) {
				if (connections > memberSystems[nextPotential].numConnections()) {
					ms = memberSystems[nextPotential];
					connections = ms.numConnections();
				}
			}
		}
		
		if (ms != null) {
			memberSystems[currentSystem].linkTo(ms);
			ms.linkTo(memberSystems[currentSystem]);
		}

		
		return lastSystem;
	}

	private int processBackFirstMost(int currentSystem, int lastSystem) {
		int connections = -1;
		MemberSystem ms = null;
		
		for (int nextPotential = 0; nextPotential < currentSystem; ++nextPotential) {
			if (!memberSystems[currentSystem].isLinkedTo(memberSystems[nextPotential])) {
				if (connections < memberSystems[nextPotential].numConnections()) {
					ms = memberSystems[nextPotential];
					connections = ms.numConnections();
				}
			}
		}
		
		if (ms != null) {
			memberSystems[currentSystem].linkTo(ms);
			ms.linkTo(memberSystems[currentSystem]);
		}

		
		return lastSystem;
	}

	private int processBackRandom(int currentSystem, int lastSystem) {
		LinkedList<MemberSystem> availSystems = new LinkedList<>();
		
		for (int nextPotential = 0; nextPotential < currentSystem; ++nextPotential) {
			if (!memberSystems[currentSystem].isLinkedTo(memberSystems[nextPotential])) {
				availSystems.add(memberSystems[nextPotential]);
			}
		}
		
		if (availSystems.size() == 0) {
			return lastSystem;
		}
		
		Random r = new Random();
		MemberSystem ms;
		ms = memberSystems[r.nextInt(availSystems.size())];
		
		memberSystems[currentSystem].linkTo(ms);
		ms.linkTo(memberSystems[currentSystem]);
		
		return lastSystem;
	}

	private int processNextNew(int currentSystem, int lastSystem) {
		int nextPotential = currentSystem + 1;
		
		while (true) {
			if (nextPotential >= memberSystems.length) {
				for (nextPotential = 0; nextPotential < currentSystem; ++nextPotential) {
					if (!memberSystems[currentSystem].isLinkedTo(memberSystems[nextPotential])) {
						memberSystems[currentSystem].linkTo(memberSystems[nextPotential]);
						memberSystems[nextPotential].linkTo(memberSystems[currentSystem]);
						
						return lastSystem;
					}
				}
				
				return lastSystem;
			}
			
			if (memberSystems[nextPotential] == null) {
				memberSystems[nextPotential] = new MemberSystem("" + nextPotential);
				memberSystems[currentSystem].linkTo(memberSystems[nextPotential]);
				memberSystems[nextPotential].linkTo(memberSystems[currentSystem]);
				
				return nextPotential;
			}
			
			++nextPotential;
		}
		
	}

	private int processNext(int currentSystem, int lastSystem) {
		int nextPotential = currentSystem + 1;
		
		while (true) {
			if (nextPotential >= memberSystems.length) {
				nextPotential = 0;
			}
			
			if (nextPotential == currentSystem) {
				return lastSystem;
			}
			
			if (memberSystems[nextPotential] == null) {
				memberSystems[nextPotential] = new MemberSystem("" + nextPotential);
				memberSystems[currentSystem].linkTo(memberSystems[nextPotential]);
				memberSystems[nextPotential].linkTo(memberSystems[currentSystem]);
				
				return nextPotential;
			}
			
			if (memberSystems[currentSystem].isLinkedTo(memberSystems[nextPotential])) {
				++nextPotential;
			} else {
				memberSystems[currentSystem].linkTo(memberSystems[nextPotential]);
				memberSystems[nextPotential].linkTo(memberSystems[currentSystem]);
				
				return lastSystem;
			}
		}
		
	}
	
	public void displayGalaxy() {
		System.out.println(galacticRules);
		
		for (int i = 0; i < memberSystems.length; ++i) {
			if (memberSystems[i] != null) {
				System.out.println(memberSystems[i] + "(" + rulesChosen[i] + "): " + memberSystems[i].printConnections() + " " + memberSystems[i].printEmpires());
			}
		}
	}

	public static void main(String[] args) {
		Galaxy g = new Galaxy(new Rules_Test1(), 25, 60);
		
		g.displayGalaxy();
	}

}
