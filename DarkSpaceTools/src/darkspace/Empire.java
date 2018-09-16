package darkspace;

import java.util.LinkedList;

public class Empire {
	private static double nextID = 0;
	LinkedList<MemberSystem> members;
	LinkedList<MemberSystem> shared;
	String name;
	
	public Empire() {
		members = new LinkedList<>();
		shared = new LinkedList<>();
		name = EmpireNames.nameOf(nextID++);
	}
	
	public void addMember(MemberSystem member) {
		members.add(member);
	}
	
	public void addShared(MemberSystem member) {
		shared.add(member);
	}

	public String getName() {
		return name;
	}

	public void remove(MemberSystem ms) {
		members.remove(ms);
		shared.remove(ms);
		
	}
}
