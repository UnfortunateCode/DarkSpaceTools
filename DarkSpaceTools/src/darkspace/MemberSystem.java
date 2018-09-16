package darkspace;

import java.util.LinkedList;

import fatetools.FATERoll;

public class MemberSystem {
	private int technology, environment, resources;
	private String name;
	private LinkedList<MemberSystem> linkedSystems = new LinkedList<>();
	private LinkedList<String> empireNames = new LinkedList<>();
	private int empirePower = 0;
	private boolean capital = false;
	
	public void addEmpire(String empireName) {
		empireNames.add(empireName);
	}
	
	public void setCapital(boolean isCapital) {
		capital = isCapital;
	}
	
	public boolean isCapital() {
		return capital;
	}
	
	
	// TODO This breaks private contract : Replace with iterator
	public LinkedList<MemberSystem> getConnections() {
		return linkedSystems;
	}
	public int numEmpires() {
		return empireNames.size();
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
	}
	
	public MemberSystem(String systemName) {
		name = systemName;
		technology = FATERoll.getRoll();
		environment = FATERoll.getRoll();
		resources = FATERoll.getRoll();
	}
	
	public MemberSystem(int tech, int env, int res) {
		name = "Unnamed";
		technology = tech;
		environment = env;
		resources = res;
	}
	
	public MemberSystem(String systemName, int tech, int env, int res) {
		name = systemName;
		technology = tech;
		environment = env;
		resources = res;
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
		if (empireNames.size() == 0) {
			return "";
		}
		
		String result = "[";
		
		for (String name : empireNames) {
			result += name + " ";
		}
		
		result += "- " + empirePower + "]";
		
		return result;
	}
	
	public String labelEmpires() {
		if (empireNames.size() == 0) {
			return "";
		}
		
		String result = "";
		
		for (String name : empireNames) {
			result += name + "_";
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
		for (MemberSystem ms : linkedSystems) {
			if (ms.empirePower < empirePower - 1) {
				ms.empirePower = empirePower - 1;
				ms.empireNames.clear();
				ms.empireNames.addAll(empireNames);
				ms.setCapital(false);
			} else if (ms.empirePower == empirePower - 1) {
				for (String names : empireNames) {
					if (!ms.empireNames.contains(names)) {
						ms.empireNames.add(names);
					}
				}
			}
		}
		
	}
}
