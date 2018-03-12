package atos.net.poLPI;

import junit.framework.TestResult;
import junit.framework.TestSuite;

public class AppTestSuite {

	public static void main(String[] a) {
		
		TestSuite suite = new TestSuite(AppInitTestCases.class);
		suite.addTestSuite(AppSequenceTestCases.class);
		
		TestResult result = new TestResult();
		suite.run(result);
		
		System.out.println("Was it successful? " + result.wasSuccessful());
		System.out.println("How many tests were there? " + result.runCount());
	}
}
