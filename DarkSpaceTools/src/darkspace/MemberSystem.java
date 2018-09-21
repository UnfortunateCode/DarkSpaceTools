package darkspace;

import java.util.LinkedList;
import java.util.Queue;

import fatetools.FATERoll;


/**
 * MemberSystem holds the basic data for individual solar systems.
 * A system is defined by three statistics: Technology, Environment,
 * and Resources. Empires form out of systems that have high Technology
 * and Environment scores. Corporations form out of systems with decent
 * Environment or Resources. For the fiction, Corporations that arrive
 * out of high Environment scores are generally Service-based, while those
 * arriving out of high Resources are generally Materials-based.
 * 
 *
 * @author UnfortunateCode
 */
public class MemberSystem {
	public static int CapitalLimit = 3;
	
	private int technology, environment, resources;
	private String name;
	private LinkedList<MemberSystem> linkedSystems = new LinkedList<>();
	private LinkedList<Empire> empires = new LinkedList<>();
	private int empirePower = 0;
	private boolean capital = false;
	private Empire capitalOf;
	
	public void addNewEmpire() {
		capitalOf = new Empire();
		empires.add(capitalOf);
		empirePower = getEmpireCapability();
		capital = true;
	}
	
	public void setCapital(boolean isCapital) {
		capital = isCapital;
	}
	
	public boolean isCapital() {
		return capital;
	}
	
	public LinkedList<MemberSystem> getConnections() {
		return (LinkedList<MemberSystem>)linkedSystems.clone();
	}
	public int numEmpires() {
		return empires.size();
	}

	public int getEmpirePower() {
		return empirePower;
	}

	public void setEmpirePower(int empirePower) {
		this.empirePower = empirePower;
	}
	
	public LinkedList<MemberSystem> numNonBranching() {
		LinkedList<MemberSystem> branchSystems = new LinkedList<>();
		
		if (linkedSystems.size() == 2) {
			branchSystems.add(this);
			
			for (MemberSystem ms : linkedSystems) {
				ms.addNext(branchSystems, this);
			}
		}
		
		return branchSystems;
	}
	
	public LinkedList<MemberSystem> allConnected() {
		LinkedList<MemberSystem> all = new LinkedList<>();
		Queue<MemberSystem> lookAt = new LinkedList<>();
		MemberSystem activeMS;
		
		all.add(this);
		
		all.addAll(linkedSystems);
		
		lookAt.addAll(linkedSystems);
		
		while (!lookAt.isEmpty()) {
			activeMS = lookAt.remove();
			
			for (MemberSystem ms : activeMS.linkedSystems) {
				if (!all.contains(ms)) {
					all.add(ms);
					lookAt.add(ms);
				}
			}
		}	
		
		
		return all;
	}
	
	public int numPowers() {
		int num = 0;
		
		for (Empire emp : empires) {
			if (emp.isPower()) {
				++num;
			}
		}
		
		return num;
	}
	
	private void addNext(LinkedList<MemberSystem> branchSystems, MemberSystem prevSystem) {
		if (branchSystems.contains(this)) {
			return;
		}
		if (linkedSystems.size() == 2) {
			branchSystems.add(this);
			if (linkedSystems.getFirst() == prevSystem) {
				linkedSystems.getLast().addNext(branchSystems, this);
			} else {
				linkedSystems.getFirst().addNext(branchSystems, this);
			}
		}
	}

	public MemberSystem() {
		name = "Unnamed";
		technology = FATERoll.getRoll();
		environment = FATERoll.getRoll();
		resources = FATERoll.getRoll();
		
		if (technology + environment >= CapitalLimit) {
			addNewEmpire();
		}
	}
	
	public MemberSystem(String systemName) {
		name = systemName;
		technology = FATERoll.getRoll();
		environment = FATERoll.getRoll();
		resources = FATERoll.getRoll();
		
		if (technology + environment >= CapitalLimit) {
			addNewEmpire();
		}
	}
	
	public MemberSystem(int tech, int env, int res) {
		name = "Unnamed";
		technology = tech;
		environment = env;
		resources = res;
		
		if (technology + environment >= CapitalLimit) {
			addNewEmpire();
		}
	}
	
	public MemberSystem(String systemName, int tech, int env, int res) {
		name = systemName;
		technology = tech;
		environment = env;
		resources = res;
		
		if (technology + environment >= CapitalLimit) {
			addNewEmpire();
		}
	}

	public int getTechnology() {
		return technology;
	}

	public void setTechnology(int technology) {
		this.technology = technology;
	}

	public int getEnvironment() {
		return environment;
	}

	public void setEnvironment(int environment) {
		this.environment = environment;
	}

	public int getResources() {
		return resources;
	}

	public void setResources(int resources) {
		this.resources = resources;
	}
	
	public int getEmpireCapability() {
		return technology + environment;
	}
	
	public int getCorporateCapability() {
		return environment + resources;
	}

	public boolean isLinkedTo(MemberSystem memberSystem) {
		return linkedSystems.contains(memberSystem);
	}

	@Override
	public String toString() {
		return name;
	}
	
	public String printConnections() {
		String result = "";
		
		for (MemberSystem connected : linkedSystems) {
			result += connected + " ";
		}
		
		return result;
	}
	
	public String printEmpires() {
		if (empires.size() == 0) {
			return "";
		}
		
		String result = "[";
		
		for (Empire empire : empires) {
			result += empire.getName() + " ";
		}
		
		result += "- " + empirePower + "]";
		
		return result;
	}
	
	public String labelEmpires() {
		if (empires.size() == 0) {
			return "";
		}
		
		String result = "";
		
		for (Empire empire : empires) {
			if (isCapital() && empire.equals(capitalOf)) {
				result += empire.getName().toUpperCase() + "_";
			} else {
				result += empire.getName().toLowerCase() + "_";
			}
			
		}
				
		return result;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void linkTo(MemberSystem memberSystem) {
		if (!linkedSystems.contains(memberSystem)) {
			linkedSystems.add(memberSystem);
		}
	}

	public int numConnections() {
		return linkedSystems.size();
	}

	public void spreadEmpire() {
		if (empirePower <= 1) {
			return;
		}
		
		int numEmpires = empires.size();
		
		if (numEmpires == 0) {
			System.err.println("MemberSystem: Spreading empires with no empires");
			empirePower = 0;
			return;
		}
		
		boolean single = (numEmpires == 1);
		
		for (MemberSystem ms : linkedSystems) {
			if (ms.empirePower < empirePower - 1) {
				ms.empirePower = empirePower - 1;
				
				for (Empire empire : ms.empires) {
					empire.remove(ms);
				}
				ms.empires.clear();
				ms.empires.addAll(empires);
				
				for (Empire empire : empires) {
					if (single) {
						empire.addMember(ms);
					} else {
						empire.addShared(ms);
					}
				}
				ms.setCapital(false);
			} else if (ms.empirePower == empirePower - 1) {
				for (Empire empire : empires) {
					if (!ms.empires.contains(empire)) {
						if (single) {
							empire.addMember(ms);
						} else {
							empire.addShared(ms);
						}
						ms.empires.add(empire);
					}
				}
			}
		}
		
	}
}
