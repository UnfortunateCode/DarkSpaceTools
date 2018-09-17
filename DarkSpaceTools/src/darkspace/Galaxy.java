package darkspace;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import darkspace.Rules_Base.Rule;
import fatetools.FATERoll;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

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
	
	public Galaxy(Rules_Base rules, int minSize, int maxSize, int[][] galacticInputs) {
		// TODO Auto-generated constructor stub
	}

	private void buildGalaxy(Rules_Base rules, int minSize, int maxSize) {
		galacticRules = rules;
		memberSystems = new MemberSystem[maxSize];
		rulesChosen = new int[maxSize];
				
		memberSystems[0] = new MemberSystem("0");
		int currentSystem = 0;
		LinkedList<Rule> currentRules;
		
		for (int ruleChoice = 0; ruleChoice < maxSize; ++ruleChoice) {
			rulesChosen[ruleChoice] = FATERoll.getRoll();
		}
		
		while (currentSystem < minSize || (currentSystem < memberSystems.length && memberSystems[currentSystem] != null)) {
			if (memberSystems[currentSystem] == null) {
				memberSystems[currentSystem] = new MemberSystem("" + currentSystem);
			}
			
			
			currentRules = galacticRules.getRule(rulesChosen[currentSystem]);
			
			for (Rule rr : currentRules) {
				switch (rr) {
				case BACKFIRSTLEAST:
					processBackFirstLeast(currentSystem);
					break;
				case BACKFIRSTMOST:
					processBackFirstMost(currentSystem);
					break;
				case NEXT:
					processNext(currentSystem);
					break;
				case NEXTNEW:
					processNextNew(currentSystem);
					break;
				case BACKRANDOM:
					processBackRandom(currentSystem);
					break;
				case RANDOMCAPITAL:
					processRandomCapital(currentSystem);
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
	
	public int numNonEmpireSystems() {
		int num = 0;
		
		for (int sysCount = 0; sysCount < memberSystems.length; ++sysCount) {
			if (memberSystems[sysCount].numEmpires() > 0) {
				++num;
			}
		}
		return num;
	}
	
	public int lengthNonBranching() {
		int num = 0;
		int cur = 0;
		
		LinkedList<MemberSystem> found = new LinkedList<>();
		
		for (int sysCount = 0; sysCount < memberSystems.length;++sysCount){
			if (memberSystems[sysCount].getConnections().size() == 2 &&
				  !found.contains(memberSystems[sysCount])) {
				cur = memberSystems[sysCount].numNonBranching();
					
				num = Math.max(num, cur);
			}
		}
		
		return num;
	}
	
	private void buildEmpires() {
		EmpireNames empName = new EmpireNames();
		MemberSystem ms;
		int capability = 0;
		
		for (int sysCount = 0; sysCount < memberSystems.length; ++sysCount) {
			ms = memberSystems[sysCount];
			
			if (ms.isCapital()) {				 
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
			// TODO
		}
		
	}
	
	private void processBackFirstLeast(int currentSystem) {
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

		
		return;
	}

	private void processBackFirstMost(int currentSystem) {
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

		
		return;
	}

	private void processBackRandom(int currentSystem) {
		LinkedList<MemberSystem> availSystems = new LinkedList<>();
		
		for (int nextPotential = 0; nextPotential < currentSystem; ++nextPotential) {
			if (!memberSystems[currentSystem].isLinkedTo(memberSystems[nextPotential])) {
				availSystems.add(memberSystems[nextPotential]);
			}
		}
		
		if (availSystems.size() == 0) {
			return;
		}
		
		Random r = new Random();
		MemberSystem ms;
		ms = memberSystems[r.nextInt(availSystems.size())];
		
		memberSystems[currentSystem].linkTo(ms);
		ms.linkTo(memberSystems[currentSystem]);
		
		return;
	}

	private void processNextNew(int currentSystem) {
		int nextPotential = currentSystem + 1;
		
		while (true) {
			if (nextPotential >= memberSystems.length) {
				for (nextPotential = 0; nextPotential < currentSystem; ++nextPotential) {
					if (!memberSystems[currentSystem].isLinkedTo(memberSystems[nextPotential])) {
						memberSystems[currentSystem].linkTo(memberSystems[nextPotential]);
						memberSystems[nextPotential].linkTo(memberSystems[currentSystem]);
						
						return;
					}
				}
				
				return;
			}
			
			if (memberSystems[nextPotential] == null) {
				memberSystems[nextPotential] = new MemberSystem("" + nextPotential);
				memberSystems[currentSystem].linkTo(memberSystems[nextPotential]);
				memberSystems[nextPotential].linkTo(memberSystems[currentSystem]);
				
				return;
			}
			
			++nextPotential;
		}
		
	}
	
	private void processRandomCapital(int currentSystem) {
		LinkedList<MemberSystem> availSystems = new LinkedList<>();
		
		for (int nextPotential = 0; nextPotential < currentSystem; ++nextPotential) {
			if (memberSystems[nextPotential] != null && 
					!memberSystems[currentSystem].isLinkedTo(memberSystems[nextPotential]) &&
					memberSystems[nextPotential].isCapital()) {
				availSystems.add(memberSystems[nextPotential]);
			}
		}
		
		if (availSystems.size() == 0) {
			return;
		}
		
		Random r = new Random();
		MemberSystem ms;
		ms = memberSystems[r.nextInt(availSystems.size())];
		
		memberSystems[currentSystem].linkTo(ms);
		ms.linkTo(memberSystems[currentSystem]);
		
		return;
	}

	private void processNext(int currentSystem) {
		int nextPotential = currentSystem + 1;
		
		while (true) {
			if (nextPotential >= memberSystems.length) {
				nextPotential = 0;
			}
			
			if (nextPotential == currentSystem) {
				return;
			}
			
			if (memberSystems[nextPotential] == null) {
				memberSystems[nextPotential] = new MemberSystem("" + nextPotential);
				memberSystems[currentSystem].linkTo(memberSystems[nextPotential]);
				memberSystems[nextPotential].linkTo(memberSystems[currentSystem]);
				
				return;
			}
			
			if (memberSystems[currentSystem].isLinkedTo(memberSystems[nextPotential])) {
				++nextPotential;
			} else {
				memberSystems[currentSystem].linkTo(memberSystems[nextPotential]);
				memberSystems[nextPotential].linkTo(memberSystems[currentSystem]);
				
				return;
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
		Galaxy g = new Galaxy(new Rules_RandomFATE(), 25, 60);
		
		long timer = System.currentTimeMillis();
		for (int i = 0; i < 10000000; ++i) {
			g = new Galaxy(new Rules_RandomFATE(), 25, 60);
		}
		System.out.println(System.currentTimeMillis() - timer);
		
		
		g.displayGalaxy();
		Random r = new Random();
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		String styleSheet =
		        "node {" +
		        "	fill-color: rgb(" + (128+r.nextInt(128)) + "," + (128+r.nextInt(128)) + "," + (128+r.nextInt(128)) + ");" +
		        "} ";
		        
		
		Graph graph = new SingleGraph("Galaxy");
		graph.setStrict(false);
		graph.setAutoCreate(false);
		
		MemberSystem ms;
		Node n;
		LinkedList<String> labels = new LinkedList<>();
		
		for (int i = 0; i < g.memberSystems.length; ++i) {
			ms = g.memberSystems[i];
			n = graph.addNode(ms.getName());
			n.addAttribute("ui.label", ms.labelEmpires());
			n.addAttribute("ui.class", "" + ms.labelEmpires().toUpperCase());
			
			if (!labels.contains(ms.labelEmpires().toUpperCase())) {
				labels.add(ms.labelEmpires().toUpperCase());
			}
		}
		
		for (String label : labels) {
			styleSheet += 
					"node." + label + " {" +
			        "	fill-color: rgb(" + (64+r.nextInt(192)) + "," + (64+r.nextInt(192)) + "," + (64+r.nextInt(192)) + ");" +
			        "} ";
		}
		
		for (int i = 0; i < g.memberSystems.length; ++i) {
			ms = g.memberSystems[i];
			for (MemberSystem msConn : ms.getConnections()) {
				graph.addEdge(ms + " " + msConn, ms.getName(), msConn.getName());
			}
		}

	    graph.addAttribute("ui.stylesheet", styleSheet);
		graph.display();
	}

}
