package darkspace;

import java.util.LinkedList;

/**
 * Empire holds all of the Member Systems under its control,
 * and Member Systems that are influenced by this and other
 * Empires equally.
 * 
 * @author UnfortunateCode
 */
public class Empire {
	private static double nextID = 0;
	LinkedList<MemberSystem> members;
	LinkedList<MemberSystem> shared;
	String name;
	
	/**
	 * Standard constructor.
	 */
	public Empire() {
		members = new LinkedList<>();
		shared = new LinkedList<>();
		name = EmpireNames.nameOf(nextID++);
	}
	
	/**
	 * Adds a Member System solely controlled
	 * by this Empire.
	 * 
	 * Note: the Member System is not checked for
	 * sole control.
	 * 
	 * @param member the Member System to be solely controlled by this Empire.
	 */
	public void addMember(MemberSystem member) {
		members.add(member);
	}
	
	
	/**
	 * Adds a Member System that is controlled by this
	 * and other Empires.
	 * 
	 * Note: the Member System is not checked for multiple
	 * control.
	 * 
	 * @param member the Member System that is controlled by this and other Empires.
	 */
	public void addShared(MemberSystem member) {
		shared.add(member);
	}

	/**
	 * @return the name of the Empire. By default an alphabetical name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param member the member to be removed from the Empire
	 */
	public void remove(MemberSystem member) {
		members.remove(member);
		shared.remove(member);
		
	}
	
	public boolean isPower() {
		return members.size() > 0;
	}
}
