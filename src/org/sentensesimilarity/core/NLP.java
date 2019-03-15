package org.sentensesimilarity.core;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NLP {

	private boolean isCorrect = false;
	private boolean negationCheck = false;
	private double maxSimValue = 0.0;
	private double jointSimValue=0.0;
	private double orderSimValue=0.0;
	private String testingString = "";
	private String selectedModel = "";
	private List<String> bagOfWords;
	private double [] weightVector;
	private double[][] matrix;
	private double[][] wordOrderMatrix;
	private double[][] similarityMatrix;
	private double[][] tf_idf_matrix;

	public NLP() {

	}

	
	
	
	/**
	 * @return the weightVector
	 */
	public double[] getWeightVector() {
		return weightVector;
	}




	/**
	 * @param weightVector the weightVector to set
	 */
	public void setWeightVector(double[] weightVector) {
		this.weightVector = weightVector;
	}




	/**
	 * @return the isCorrect
	 */
	public boolean isCorrect() {
		return isCorrect;
	}

	/**
	 * @param isCorrect
	 *            the isCorrect to set
	 */
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	/**
	 * @return the isNegation
	 */
	public boolean isNegation() {
		return negationCheck;
	}

	/**
	 * @param isNegation
	 *            the isNegation to set
	 */
	public void setNegation(boolean negationCheck) {
		this.negationCheck = negationCheck;
	}

	/**
	 * @return the maxSimValue
	 */
	public double getMaxSimValue() {
		return maxSimValue;
	}

	/**
	 * @param maxSimValue
	 *            the maxSimValue to set
	 */
	public void setMaxSimValue(double maxSimValue) {
		this.maxSimValue = maxSimValue;
	}

	/**
	 * @return the selectedModel
	 */
	public String getSelectedModel() {
		return selectedModel;
	}

	/**
	 * @param selectedModel
	 *            the selectedModel to set
	 */
	public void setSelectedModel(String selectedModel) {
		this.selectedModel = selectedModel;
	}

	/**
	 * @return the bagOfWords
	 */
	public List<String> getBagOfWords() {
		return bagOfWords;
	}

	/**
	 * @param bagOfWords
	 *            the bagOfWords to set
	 */
	public void setBagOfWords(List<String> bagOfWords) {
		this.bagOfWords = bagOfWords;
	}

	/**
	 * @return the matrix
	 */
	public double[][] getMatrix() {
		return matrix;
	}

	/**
	 * @param matrix
	 *            the matrix to set
	 */
	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}

	/**
	 * @return the tf_idf_matrix
	 */
	public double[][] getTf_idf_matrix() {
		return tf_idf_matrix;
	}

	/**
	 * @param tf_idf_matrix
	 *            the tf_idf_matrix to set
	 */
	public void setTf_idf_matrix(double[][] tf_idf_matrix) {
		this.tf_idf_matrix = tf_idf_matrix;
	}

	/**
	 * @return the similarityMatrix
	 */
	public double[][] getSimilarityMatrix() {
		return similarityMatrix;
	}

	/**
	 * @param similarityMatrix
	 *            the similarityMatrix to set
	 */
	public void setSimilarityMatrix(double[][] similarityMatrix) {
		this.similarityMatrix = similarityMatrix;
	}

	/**
	 * @return the testingString
	 */
	public String getTestingString() {
		return testingString;
	}

	/**
	 * @param testingString
	 *            the testingString to set
	 */
	public void setTestingString(String testingString) {
		this.testingString = testingString;
	}

	public double similarityScore() {

		return maxSimValue;
	}
	
	

	/**
	 * @return the negationCheck
	 */
	public boolean isNegationCheck() {
		return negationCheck;
	}

	/**
	 * @param negationCheck the negationCheck to set
	 */
	public void setNegationCheck(boolean negationCheck) {
		this.negationCheck = negationCheck;
	}

	/**
	 * @return the wordOrderMatrix
	 */
	public double[][] getWordOrderMatrix() {
		return wordOrderMatrix;
	}

	/**
	 * @param wordOrderMatrix the wordOrderMatrix to set
	 */
	public void setWordOrderMatrix(double[][] wordOrderMatrix) {
		this.wordOrderMatrix = wordOrderMatrix;
	}

	/**
	 * @return the jointSimValue
	 */
	public double getJointSimValue() {
		return jointSimValue;
	}

	/**
	 * @param jointSimValue the jointSimValue to set
	 */
	public void setJointSimValue(double jointSimValue) {
		this.jointSimValue = jointSimValue;
	}

	/**
	 * @return the orderSimValue
	 */
	public double getOrderSimValue() {
		return orderSimValue;
	}

	/**
	 * @param orderSimValue the orderSimValue to set
	 */
	public void setOrderSimValue(double orderSimValue) {
		this.orderSimValue = orderSimValue;
	}

	public boolean negationCheck() {

		String negationMain = "නැති|නැත";
		String negationAddVerb = "නො";
		
		String conjuc="සහ";

		int testCount = 0, modelCount = 0;

		Pattern p = Pattern.compile(negationMain);
		Matcher m = p.matcher(testingString);
		int count = 0;
		while (m.find()) {
			count += 1;
		}

		Pattern p2 = Pattern.compile(negationAddVerb);
		Matcher m2 = p2.matcher(testingString);
		int count2 = 0;
		while (m2.find()) {
			count2 += 1;
		}

		testCount = count + count2;
		

		p = Pattern.compile(negationMain);
		m = p.matcher(selectedModel);
		count = 0;
		while (m.find()) {
			count += 1;
		}

		p2 = Pattern.compile(negationAddVerb);
		m2 = p2.matcher(selectedModel);
		count2 = 0;
		while (m2.find()) {
			count2 += 1;
		}

		modelCount = count + count2;

		if (modelCount == testCount) {

			negationCheck = true;
			return true;
			
		} else {

			return false;
		}

	}

}
