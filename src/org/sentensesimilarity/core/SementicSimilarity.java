package org.sentensesimilarity.core;

import java.util.ArrayList;

public class SementicSimilarity {

	private String sentense1;
	private String sentense2;

	public double evaluateSentense(String sentense1, String sentense2) {
		
		
		this.sentense1=sentense1;
		this.sentense2=sentense2;
		

		ArrayList<String> allParameter = new ArrayList<>();

		if (!sentense1.equals("") && !sentense2.equals("")) {

			allParameter.add(sentense1);
			allParameter.add(sentense2);

			LSAImplementation lsaImplementation = new LSAImplementation();
			lsaImplementation.setBagOfWords(allParameter);

			ArrayList<String> modelArray = new ArrayList<>();

			modelArray.add(sentense1);
			lsaImplementation.setModelAnswers(modelArray);

			double[][] similarity = lsaImplementation.getSimilarity(sentense2,true);
			lsaImplementation.getMostSimilarityValue(similarity);
			NLP nlpObject = lsaImplementation.getNlpObject();

			double correctnessCheck = nlpObject.similarityScore();
			
			boolean negationCheck = nlpObject.negationCheck();

			if (negationCheck) {

				return correctnessCheck;

			} else {
				return -1;
			}
		} else {
			return -2;
		}

	}

}
