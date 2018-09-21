package darkspace;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import darkspace.Rules_Base.Rule;
import fatetools.FATERoll;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 * Contains a galaxy of connected systems. This class handles the 
 * creation of the galaxy, as well as provides a number of analytical
 * methods.
 * 
 * @author UnfortunateCode
 */
public class Galaxy {
	// Setup variables
	int[][] galacticInputs;
	Rules_Base galacticRules;
	
	// Storage variables
	MemberSystem[] memberSystems;
	Empire[] empires;

	
	// Constructors
	
	/**
	 * Standard constructor, using the RandomFATE ruleset. This uses
	 * the FATE die probability to determine the number of rules per
	 * each level.
	 */
	public Galaxy() {
		int minSize = 25;
		int maxSize = 60;

		galacticInputs = new int[maxSize][4];

		for (int sysCount = 0; sysCount < galacticInputs.length; ++sysCount) {
			for (int attribute = 0; attribute < galacticInputs[0].length; ++attribute) {
				galacticInputs[sysCount][attribute] = FATERoll.getRoll();
			}
		}
		buildGalaxy(new Rules_RandomFATE(), minSize, maxSize, true);
	}

	/**
	 * All parameter constructor. 
	 * 
	 * @param rules the ruleset to use to generate the galaxy.
	 * @param minSize the point at which new Member Systems will stop being forced.
	 * @param maxSize the most Member Systems to be handled.
	 * @param connectForward true if the last system should be connected forward to a forced system.
	 * @param galacticInputs the sets of die rolls for the generation. [-4,4]
	 */
	public Galaxy(Rules_Base rules, int minSize, int maxSize, boolean connectForward, int[][] galacticInputs) {
		this.galacticInputs = galacticInputs;
		buildGalaxy(rules, minSize, maxSize, connectForward);

	}

	/**
	 * Constructor using random inputs.
	 * 
	 * @param rules the ruleset to use to generate the galaxy.
	 * @param minSize the point at which new Member Systems will stop being forced.
	 * @param maxSize the most Member Systems to be handled.
	 * @param connectForward true if the last system should be connected forward to a forced system.
	 */
	public Galaxy(Rules_Base rules, int minSize, int maxSize, boolean connectForward) {
		galacticInputs = new int[maxSize][4];

		for (int sysCount = 0; sysCount < galacticInputs.length; ++sysCount) {
			for (int attribute = 0; attribute < galacticInputs[0].length; ++attribute) {
				galacticInputs[sysCount][attribute] = FATERoll.getRoll();
			}
		}

		buildGalaxy(rules, minSize, maxSize, connectForward);
	}

	
	// Construction methods
	
	private void buildGalaxy(Rules_Base rules, int minSize, int maxSize, boolean connectForward) {
		galacticRules = rules;
		memberSystems = new MemberSystem[maxSize];

		memberSystems[0] = new MemberSystem("0", galacticInputs[0][1], galacticInputs[0][2], galacticInputs[0][3]);
		int currentSystem = 0;
		LinkedList<Rule> currentRules;

		while (currentSystem < minSize || (currentSystem < memberSystems.length && memberSystems[currentSystem] != null)) {
			if (memberSystems[currentSystem] == null) {
				if (connectForward) {
					processNextNew(currentSystem - 1);
				} else {
					memberSystems[currentSystem] = new MemberSystem("" + currentSystem, galacticInputs[currentSystem][1],
						galacticInputs[currentSystem][2], galacticInputs[currentSystem][3]);
				} 
			}


			currentRules = galacticRules.getRule(galacticInputs[currentSystem][0]);

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
				memberSystems[nextPotential] = new MemberSystem("" + nextPotential, galacticInputs[nextPotential][1],
						galacticInputs[nextPotential][2], galacticInputs[nextPotential][3]);
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
				memberSystems[nextPotential] = new MemberSystem("" + nextPotential, galacticInputs[nextPotential][1],
						galacticInputs[nextPotential][2], galacticInputs[nextPotential][3]);
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

	}

	
	// Analytical methods
	
	public int numNonEmpireSystems() {
		int num = 0;

		for (int sysCount = 0; sysCount < memberSystems.length; ++sysCount) {
			if (memberSystems[sysCount].numEmpires() == 0) {
				++num;
			}
		}
		return num;
	}

	public int lengthNonBranching() {
		int num = 0;
		LinkedList<MemberSystem> cur;

		LinkedList<MemberSystem> found = new LinkedList<>();

		for (int sysCount = 0; sysCount < memberSystems.length; ++sysCount){
			if (memberSystems[sysCount].getConnections().size() == 2 &&
					!found.contains(memberSystems[sysCount])) {
				cur = memberSystems[sysCount].numNonBranching();

				found.addAll(cur);
				num = Math.max(num, cur.size());
			}
		}

		return num;
	}

	public int numClusters() {
		int num = 0;

		LinkedList<MemberSystem> found = new LinkedList<>();

		for (int sysCount = 0; sysCount < memberSystems.length; ++sysCount) {
			if (!found.contains(memberSystems[sysCount])) {
				found.addAll(memberSystems[sysCount].allConnected());
				++num;
			}
		}

		return num;
	}

	public int maxConnections() {
		int num = 0;

		for (int sysCount = 0; sysCount < memberSystems.length; ++sysCount) {
			num = Math.max(num, memberSystems[sysCount].getConnections().size());

		}

		return num;
	}

	public int numRules() {
		LinkedList<Rule> rules = new LinkedList<>();
		for (int ruleCounter = -4; ruleCounter < 5; ++ruleCounter) {
			rules.addAll(galacticRules.getRule(ruleCounter));
		}

		return rules.size();
	}

	public int numOverRatio (double ratio) {
		return Math.max(0, (numConnections() - (int)(memberSystems.length * ratio)));
	}

	public int numConnections() {
		int num = 0;

		for (int sysCount = 0; sysCount < memberSystems.length; ++sysCount) {
			num += memberSystems[sysCount].numConnections();
		}

		return (num / 2);
	}
	
	public int numTriangles() {
			
		int num = 0;
		
		for (MemberSystem baseSystem : memberSystems) {
			for (MemberSystem secondarySystem : baseSystem.getConnections()) {
				for (MemberSystem ternarySystem : secondarySystem.getConnections()) {
					if (baseSystem.getConnections().contains(ternarySystem)) {
						++num;
					}
				}
			}
		}
		
		return num / 6;
	}
	
	public int maxNumPowers() {
		int num = 0;
		
		for (MemberSystem ms : memberSystems) {
			num = Math.max(num, ms.numPowers());
		}
		
		return num;
	}

	public int differenceFromIdealSize(int idealSize) {

		return Math.abs(idealSize - memberSystems.length);
	}

	// Display methods

	public void displayGalaxy() {
		System.out.println(galacticRules);

		for (int i = 0; i < memberSystems.length; ++i) {
			if (memberSystems[i] != null) {
				System.out.println(memberSystems[i] + "(" + galacticInputs[i][0] + 
						" {" + galacticInputs[i][1] + "," + galacticInputs[i][2] + "," + galacticInputs[i][3] + 
						"}): " + memberSystems[i].printConnections() + " " + memberSystems[i].printEmpires());
			}
		}
		
		System.out.println("lengthNonBranching: " + lengthNonBranching() + 
				", numNonEmpire: " + numNonEmpireSystems() +
				", diffFrom42: " + differenceFromIdealSize(42) + 
				", numClusters: " + numClusters() + 
				", maxConnections: " + maxConnections() + 
				", numRules: " + numRules() + 
				", numOverRatio: " + numOverRatio(1.3) + 
				", numTriangles: " + numTriangles() + 
				", maxNumPowers: " + maxNumPowers());
	}

	public void showGalaxy(Graph graph) {
		graph.clear();
		Random r = new Random();
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		String styleSheet =
				"node {" +
						"	fill-color: rgb(" + (128+r.nextInt(128)) + "," + (128+r.nextInt(128)) + "," + (128+r.nextInt(128)) + ");" +
						"} ";
		graph.removeAttribute("ui.stylesheet");  
		graph.addAttribute("ui.stylesheet", styleSheet);

		graph.setStrict(false);
		graph.setAutoCreate(false);

		MemberSystem ms;
		Node n;
		LinkedList<String> labels = new LinkedList<>();

		for (int i = 0; i < memberSystems.length; ++i) {
			ms = memberSystems[i];
			n = graph.addNode(ms.getName());
			n.addAttribute("ui.label", ms.getName() + ":" + ms.labelEmpires());
			n.addAttribute("ui.class", "" + ms.labelEmpires().toUpperCase());

			if (!labels.contains(ms.labelEmpires().toUpperCase())) {
				labels.add(ms.labelEmpires().toUpperCase());
			}
		}

		for (String label : labels) {
			styleSheet = 
					"node." + label + " {" +
							"	fill-color: rgb(" + (64+r.nextInt(192)) + "," + (64+r.nextInt(192)) + "," + (64+r.nextInt(192)) + ");" +
							"} ";

			graph.addAttribute("ui.stylesheet", styleSheet);
		}

		for (int i = 0; i < memberSystems.length; ++i) {
			ms = memberSystems[i];
			for (MemberSystem msConn : ms.getConnections()) {
				graph.addEdge(ms + " " + msConn, ms.getName(), msConn.getName());
			}
		}
	}

	/*
	 * Main method for testing purposes
	 *
	public static void main(String[] args) {

		Rules_Given rules = new Rules_Given(true);
		rules.addRule(-4, Rule.NEXTNEW);
		//rules.addRule(-4, Rule.NEXT);
		//rules.addRule(-4, Rule.NEXT);

		rules.addRule(-3, Rule.NEXTNEW);
		//rules.addRule(-3, Rule.BACKFIRSTLEAST);
		rules.addRule(-3, Rule.NEXT);

		rules.addRule(-2, Rule.NEXT);
		//rules.addRule(-2, Rule.BACKFIRSTLEAST);
		rules.addRule(-2, Rule.NEXTNEW);

		rules.addRule(-1, Rule.NEXT);
		rules.addRule(-1, Rule.NEXT);
		//rules.addRule(-1, Rule.BACKFIRSTLEAST);

		rules.addRule(0, Rule.NEXT);
		rules.addRule(0, Rule.NEXT);
		//rules.addRule(0, Rule.BACKFIRSTLEAST);

		rules.addRule(1, Rule.NEXT);
		//rules.addRule(1, Rule.BACKFIRSTLEAST);
		rules.addRule(1, Rule.NEXT);

		rules.addRule(2, Rule.NEXT);
		//rules.addRule(2, Rule.BACKFIRSTLEAST);
		//rules.addRule(2, Rule.BACKFIRSTLEAST);

		rules.addRule(3, Rule.NEXT);
		rules.addRule(3, Rule.NEXTNEW);
		//rules.addRule(3, Rule.BACKFIRSTLEAST);

		rules.addRule(4, Rule.NEXT);
		//rules.addRule(4, Rule.NEXT);
		//rules.addRule(4, Rule.BACKFIRSTLEAST);

		int sin;
		Galaxy g;


		while(true) {
			g = new Galaxy(new Rules_Test3(), 42, 42, true);
			
			while (g.memberSystems.length < 25 || g.memberSystems.length > 60) {
				g = new Galaxy(new Rules_Test3(), 42, 42, true);
			}


			g.displayGalaxy();


			Graph graph = new SingleGraph("Galaxy");
			graph.display();
			g.showGalaxy(graph);

			try {
				sin = System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	*
	*/


}
