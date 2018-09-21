package darkspace;


/**
 * EmpireNames is an iterator on the alphabet to provide
 * a unique character string. The static method nameOf
 * can be used to provide a specific character string.
 * 
 * Names start with A through Z, continuing to AA, etc.
 *
 * @author UnfortunateCode
 */
public class EmpireNames {
	double nextName = 0;

	/**
	 * @return a string of capital letters, different from previous strings.
	 */
	public String getNextName() {
		return nameOf(nextName++);
	}

	/**
	 * @return the number of names that has already been provided.
	 */
	public double getCount() {
		return nextName;
	}
	
	/**
	 * Provides the unique String given the whole number portion
	 * of the parameter. This would be the string provided if getNextName
	 * were called id number of times.
	 * 
	 * @param id the id to convert into a unique string
	 * @return the string that corresponds to the id
	 */
	public static String nameOf(double id) {
		String result = "";
		if (id == 0) {
			result = "A";
		} else {
			double derivedName = id;
			int nextChar;
			char base = 'A';
			for (double place = Math.floor((Math.log(id) / Math.log(26))); place >= 0; --place) {
				nextChar = (int)(derivedName / Math.pow(26, place));
				result += (char)(base + nextChar);

				derivedName -= nextChar * Math.pow(26, place);
			}
		}
		
		return result;
	}
	
	/* 
	 * Main method for testing purposes.
	 * 
	public static void main (String[] args) {
		EmpireNames emp = new EmpireNames();

		for (int i = 0; i < 106; ++i) {
			System.out.print(emp.getNextName() + ", ");
		}
		System.out.println();
		
		emp.nextName = Double.MAX_VALUE;
		System.out.println(emp.getNextName());
	}
	*
	*/
}
