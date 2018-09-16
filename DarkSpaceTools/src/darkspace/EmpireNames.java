package darkspace;

public class EmpireNames {
	double nextName = 0;

	public String getNextName() {
		String result = "";
		if (nextName == 0) {
			result = "A";
		} else {
			double derivedName = nextName;
			int nextChar;
			char base = 'A';
			for (double place = Math.floor((Math.log(nextName) / Math.log(26))); place >= 0; --place) {
				nextChar = (int)(derivedName / Math.pow(26, place));
				result += (char)(base + nextChar);

				derivedName -= nextChar * Math.pow(26, place);
			}
		}
		++nextName;
		return result;
	}

	public double getCount() {
		return nextName;
	}
	
	public static void main (String[] args) {
		EmpireNames emp = new EmpireNames();

		for (int i = 0; i < 106; ++i) {
			System.out.print(emp.getNextName() + ", ");
		}
		System.out.println();
		
		emp.nextName = Double.MAX_VALUE;
		System.out.println(emp.getNextName());
	}
}
