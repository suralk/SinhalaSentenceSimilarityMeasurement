package org.sentensesimilarity.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class SentensePair {

	private String sentense01;
	private String sentense02;
	private String manualSimVal;
	private String SimVal;
	private String manual_entailment_judgment;
	private String entailment_judgment;
	private String result;
	private NLP nlpObject;

	public SentensePair() {

	}

	public SentensePair(String sentense01, String sentense02) {

		this.sentense01 = sentense01;
		this.sentense02 = sentense02;

	}

	public SentensePair(String sentense01, String sentense02, String manualSimVal, String manual_entailment_judgment) {

		this.sentense01 = sentense01;
		this.sentense02 = sentense02;
		this.manualSimVal = manualSimVal;
		this.manual_entailment_judgment = manual_entailment_judgment;

	}

	/**
	 * @return the sentense01
	 */
	public String getSentense01() {
		return sentense01;
	}

	/**
	 * @param sentense01
	 *            the sentense01 to set
	 */
	public void setSentense01(String sentense01) {
		this.sentense01 = sentense01;
	}

	/**
	 * @return the sentense02
	 */
	public String getSentense02() {
		return sentense02;
	}

	/**
	 * @param sentense02
	 *            the sentense02 to set
	 */
	public void setSentense02(String sentense02) {
		this.sentense02 = sentense02;
	}

	/**
	 * @return the manualSimVal
	 */
	public String getManualSimVal() {
		return manualSimVal;
	}

	/**
	 * @param manualSimVal
	 *            the manualSimVal to set
	 */
	public void setManualSimVal(String manualSimVal) {
		this.manualSimVal = manualSimVal;
	}

	/**
	 * @return the simVal
	 */
	public String getSimVal() {
		return SimVal;
	}

	/**
	 * @param simVal
	 *            the simVal to set
	 */
	public void setSimVal(String simVal) {
		SimVal = simVal;
	}

	/**
	 * @return the manual_entailment_judgment
	 */
	public String getManual_entailment_judgment() {
		return manual_entailment_judgment;
	}

	/**
	 * @param manual_entailment_judgment
	 *            the manual_entailment_judgment to set
	 */
	public void setManual_entailment_judgment(String manual_entailment_judgment) {
		this.manual_entailment_judgment = manual_entailment_judgment;
	}

	/**
	 * @return the entailment_judgment
	 */
	public String getEntailment_judgment() {
		return entailment_judgment;
	}

	/**
	 * @param entailment_judgment
	 *            the entailment_judgment to set
	 */
	public void setEntailment_judgment(String entailment_judgment) {
		this.entailment_judgment = entailment_judgment;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the nlpObject
	 */
	public NLP getNlpObject() {
		return nlpObject;
	}

	/**
	 * @param nlpObject
	 *            the nlpObject to set
	 */
	public void setNlpObject(NLP nlpObject) {
		this.nlpObject = nlpObject;
	}

	public String toString() {

		// result=this.sentense01 + "\t" + this.sentense02 + "\t" +
		// this.manualSimVal + "\t"
		// + this.manual_entailment_judgment + "\t" + this.SimVal + "\t" +
		// this.entailment_judgment;

		// result = this.sentense01 + "\t" + this.sentense02 + "\t\t\t\t Manual
		// :" + this.manualSimVal + "\t Automatic :"
		// + this.SimVal + "\t" + this.entailment_judgment;

		// result=this.sentense01 + "\t" + this.sentense02 +"\t"+ this.SimVal +
		// "\t" + this.entailment_judgment;

		// result=this.sentense01 + "\t" + this.sentense02 +"\t"+ this.SimVal;
		result = this.SimVal;

		return result;
	}

	public void evaluateSentenses(boolean lexicalDataBase, double tolerance, boolean stopWords, boolean contexWeight) {

		String sentense1 = this.sentense01;
		String sentense2 = this.sentense02;

		NumberFormat formatter = new DecimalFormat("#0.00");
		// System.out.println(formatter.format(4.0));

		ArrayList<String> allParameter = new ArrayList<>();

		if (!sentense1.equals("") && !sentense2.equals("")) {

			allParameter.add(sentense1);
			allParameter.add(sentense2);

			LSAImplementation lsaImplementation = new LSAImplementation();
			lsaImplementation.setBagOfWords(allParameter);

			if (stopWords) {

				try {

					ArrayList<String> loadStopWords = loadStopWords();

					List<String> bagOfWords = lsaImplementation.getBagOfWords();
					List<String> bagOfWordsTemp = new ArrayList<String>();

					for (String test : bagOfWords) {

						if (!loadStopWords.contains(test)) {

							bagOfWordsTemp.add(test);
						}

					}
					lsaImplementation.setBagOfWords(bagOfWordsTemp);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (contexWeight) {

				lsaImplementation.createWeightVector("Glossary/Geometric.si");

			}

			ArrayList<String> modelArray = new ArrayList<>();

			modelArray.add(sentense1);
			lsaImplementation.setModelAnswers(modelArray);

			double[][] similarity = lsaImplementation.getSimilarity(sentense2, lexicalDataBase);

			// word order - 20% corpus - 80%
			lsaImplementation.getOverallSimilarityValue(tolerance);

			NLP nlpObject = lsaImplementation.getNlpObject();

			double correctnessCheck = nlpObject.similarityScore();

			this.setSimVal(formatter.format(correctnessCheck));

			boolean negationCheck = nlpObject.negationCheck();

			if (!negationCheck) {

				this.setEntailment_judgment("CONTRADICTION");

			} else {

				if (correctnessCheck > 0.7) {

					this.setEntailment_judgment("SIMILAR");
				} else if (correctnessCheck > 0.4) {

					this.setEntailment_judgment("MEDIUM");
				} else {
					this.setEntailment_judgment("POOR");

				}

			}

			this.nlpObject = lsaImplementation.getNlpObject();
		}

		this.toString();

	}

	ArrayList<String> loadStopWords() throws IOException {

		ArrayList<String> stop = new ArrayList<String>();

		BufferedReader br = null;
		try {

			br = new BufferedReader(new FileReader("stopWords.txt"));

			String line = br.readLine();

			int count = 0;

			while (line != null) {

				if (!stop.contains(line))
					stop.add(line);

				line = br.readLine();
				count++;
				// System.out.println(count);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			br.close();
		}
		return stop;

	}

}
