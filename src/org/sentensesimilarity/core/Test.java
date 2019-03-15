package org.sentensesimilarity.core;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {

			long startTime = System.currentTimeMillis();

			SentensePair sentensePair = new SentensePair();
			sentensePair.setSentense01("ඒකාන්තර කෝණ");
			sentensePair.setSentense02("ඒ. කෝ.");

			// sentensePair.setSentense01("ඒකාන්තර");
			// sentensePair.setSentense02("ඒ.");

			sentensePair.evaluateSentenses(true, 0.89, true, true);

			// all parameter are inside this
			NLP nlpObject = sentensePair.getNlpObject();

			String simVal = sentensePair.getSimVal();
			System.out.println(simVal);

			long stopTime = System.currentTimeMillis();
			System.out.println("Execution  Time " + ((stopTime - startTime) / 1000) + " sec.");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
