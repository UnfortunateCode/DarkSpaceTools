package generictools;
import java.util.Random;

public class GenericDistributions {
	final static public Random RANDOM = new Random(System.currentTimeMillis());
	
	/*
	 * Code from user1417684 on StackExchange
	 */
	static public double nextSkewedBoundedDouble(double min, double max, double skew, double bias) {
        double range = max - min;
        double mid = min + range / 2.0;
        double unitGaussian = RANDOM.nextGaussian();
        double biasFactor = Math.exp(bias);
        double retval = mid+(range*(biasFactor/(biasFactor+Math.exp(-unitGaussian/skew))-0.5));
        return retval;
    }
	
	public static int nextSkewedBoundedInt(int min, int max, double skew, double bias) {
		return (int)Math.floor(nextSkewedBoundedDouble(min, max, skew, bias));
	}

	public static void main(String[] args) {
		int[] results = new int[6];

		for (float j = 0; j < 20; ++j) {
			System.out.print("bias " + (j/10.0) + ": ");
			for (int i = 0; i < results.length; ++i) {
				results[i] = 0;
			}
			for (int i = 0; i < 10000; ++i) {
				++results[(int)Math.floor(nextSkewedBoundedDouble(0,results.length-1,1,-0.6))];
			}

			for (int i = 0; i < results.length; ++i) {
				System.out.print(results[i] + " ");
			}
			System.out.println();
		}
	}

}
