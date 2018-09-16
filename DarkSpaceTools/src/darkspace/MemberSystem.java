package darkspace;

import java.util.LinkedList;

import fatetools.FATERoll;

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
