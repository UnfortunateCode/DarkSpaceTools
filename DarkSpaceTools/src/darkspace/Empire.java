package darkspace;

import java.util.LinkedList;

public class Empire {
	LinkedList<MemberSystem> members;
	LinkedList<MemberSystem> shared;
	
	public Empire() {
		members = new LinkedList<>();
		shared = new LinkedList<>();
		
	}
	
	public void addMember(MemberSystem member) {
		members.add(member);
	}
	
	public void addShared(MemberSystem member) {
		shared.add(member);
	}
}
