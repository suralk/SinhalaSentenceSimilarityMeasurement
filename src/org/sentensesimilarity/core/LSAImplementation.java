package org.sentensesimilarity.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.sentensesimilarity.dictionary.DictionaryLookUp;

import Jama.Matrix;

public class LSAImplementation {

	private NLP nlpObject;
	private ArrayList<String> inputs;
	private ArrayList<String> modelAnswers;
	private ArrayList<String> allStringList;
	private String testSubject;
	ArrayList<String> domainWordSet;
	private List<String> bagOfWords;
	private double[] weightVector;
	private double[][] matrix;
	private double[][] weightmatrix;
	private double[][] wordOrderMatrix;
	private double[][] similarityMatrix;
	private double[][] wordOrdersimilarityMatrix;
	private double[][] tf_idf_matrix;
	private int toleranceValue = 0;

	public LSAImplementation() {

	}

	public double[][] getTFIDFSimilarity() {

		return tfIdfCalculator();

	}

	/**
	 * @return the inputs
	 */
	public ArrayList<String> getInputs() {
		return inputs;
	}

	/**
	 * @param inputs
	 *            the inputs to set
	 */
	public void setInputs(ArrayList<String> inputs) {
		this.inputs = inputs;
	}

	/**
	 * @return the modelAnswers
	 */
	public ArrayList<String> getModelAnswers() {
		return modelAnswers;
	}

	/**
	 * @param modelAnswers
	 *            the modelAnswers to set
	 */
	public void setModelAnswers(ArrayList<String> modelAnswers) {
		this.modelAnswers = modelAnswers;
	}

	/**
	 * @return the allStringList
	 */
	public ArrayList<String> getAllStringList() {
		return allStringList;
	}

	/**
	 * @param allStringList
	 *            the allStringList to set
	 */
	public void setAllStringList(ArrayList<String> allStringList) {
		this.allStringList = allStringList;
	}

	/**
	 * @return the testSubject
	 */
	public String getTestSubject() {
		return testSubject;
	}

	/**
	 * @param testSubject
	 *            the testSubject to set
	 */
	public void setTestSubject(String testSubject) {
		this.testSubject = testSubject;
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
		this.nlpObject.setBagOfWords(bagOfWords);

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
	 * @return the toleranceValue
	 */
	public int getToleranceValue() {
		return toleranceValue;
	}

	/**
	 * @param toleranceValue
	 *            the toleranceValue to set
	 */
	public void setToleranceValue(int toleranceValue) {
		this.toleranceValue = toleranceValue;
	}

	/**
	 * @return the wordOrderMatrix
	 */
	public double[][] getWordOrderMatrix() {
		return wordOrderMatrix;
	}

	/**
	 * @param wordOrderMatrix
	 *            the wordOrderMatrix to set
	 */
	public void setWordOrderMatrix(double[][] wordOrderMatrix) {
		this.wordOrderMatrix = wordOrderMatrix;
	}

	/**
	 * @return the wordOrdersimilarityMatrix
	 */
	public double[][] getWordOrdersimilarityMatrix() {
		return wordOrdersimilarityMatrix;
	}

	/**
	 * @param wordOrdersimilarityMatrix
	 *            the wordOrdersimilarityMatrix to set
	 */
	public void setWordOrdersimilarityMatrix(double[][] wordOrdersimilarityMatrix) {
		this.wordOrdersimilarityMatrix = wordOrdersimilarityMatrix;
	}

	public boolean setBagOfWords(ArrayList<String> inputs) {

		if (inputs != null && inputs.size() > 0) {

			this.inputs = inputs;

			ArrayList<String> tempBagOfWords = new ArrayList<String>();
			ArrayList<Integer> tempcount = new ArrayList<Integer>();

			nlpObject = new NLP();

			bagOfWords = new ArrayList<String>();

			ArrayList<String[]> wordTokenize = wordTokenize(inputs);

			for (int i = 0; i < wordTokenize.size(); i++) {

				for (int j = 0; j < wordTokenize.get(i).length; j++) {

					String word = wordTokenize.get(i)[j];

					if (!tempBagOfWords.contains(word)) {

						tempBagOfWords.add(word);

						tempcount.add(1);

					} else {

						int indexOf = tempBagOfWords.indexOf(word);
						Integer integer = tempcount.get(indexOf);

						tempcount.set(indexOf, integer + 1);

					}

				}

			}

			for (int k = 0; k < tempBagOfWords.size(); k++) {

				if (tempcount.get(k) > toleranceValue) {

					bagOfWords.add(tempBagOfWords.get(k));
				}

			}

			bagOfWords.remove(" ");
			bagOfWords.remove("");

			nlpObject.setBagOfWords(bagOfWords);
			return true;

		} else {

			return false;
		}

	}

	public double[] createWeightVector(String path) {

		if (bagOfWords != null) {
			weightVector = new double[bagOfWords.size()];

			try {
				corpusStatistics(path);
				int count = 0;
				for (String test : bagOfWords) {
					weightVector[count] = tfCalculator(domainWordSet, test);
					count++;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				return null;
			}

			return weightVector;

		} else {

			return null;
		}

	}

	public double corpusStatistics(String path) throws IOException {

		domainWordSet = new ArrayList<String>();

		BufferedReader br = null;

		try {

			br = new BufferedReader(new FileReader(path));

			String line = br.readLine();

			while (line != null) {

				// String[] split = line.split("\t");
				domainWordSet.add(line);

				line = br.readLine();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			br.close();
		}

		return 0.0;
	}

	public double tfCalculator(ArrayList<String> totalterms, String termToCheck) {

		if (!termToCheck.equals("") && totalterms != null) {
			double count = 0; // to count the overall occurrence of the term
								// termToCheck
			for (String s : totalterms) {
				if (s.contains(termToCheck)) {
					count++;
				}
			}

			double prob = (count + 1) / (totalterms.size() + 1);

			// TF-IDF calculation
			double val = -Math.log(prob) / Math.log(totalterms.size() + 1);

			// Inverse Weighting
			double invsval = 1 / val;

			// double val2= 1-(Math.log(count+1)/Math.log(totalterms.size()+1));

			return invsval;

		} else {
			return 0;

		}
	}

	public double[][] getSimilarity(String testSubject, boolean lexicalDataBase) {

		this.testSubject = testSubject;
		nlpObject.setTestingString(testSubject);

		// true- Dictionary lookups
		nlpObject.setMatrix(createLSAMatrix(lexicalDataBase));

		getCosineSimilarityResult(nlpObject.getMatrix());

		nlpObject.setSimilarityMatrix(similarityMatrix);

		nlpObject.setWordOrderMatrix(wordOrderMatrix);

		nlpObject.setOrderSimValue(getWordOrderSimilarityResult(nlpObject.getWordOrderMatrix()));

		return nlpObject.getSimilarityMatrix();

	}

	public boolean getMostSimilarityValue(double[][] matrix) {

		int maxIndex = 0;
		double maxValue = 0;

		if (matrix != null) {

			for (int i = 1; i < matrix[0].length; i++) {

				if (matrix[0][i] > maxValue) {

					maxValue = matrix[0][i];
					maxIndex = i;
				}
			}

			nlpObject.setJointSimValue(maxValue);
			if (maxIndex != 0)
				nlpObject.setSelectedModel(modelAnswers.get(maxIndex - 1));
			return true;
		} else {

			return false;
		}

	}

	public double getOverallSimilarityValue(double tolerance) {

		getMostSimilarityValue(this.similarityMatrix);

		double val = this.nlpObject.getJointSimValue() * tolerance
				+ this.nlpObject.getOrderSimValue() * (1 - tolerance);

		this.nlpObject.setMaxSimValue(val);

		return val;

	}

	public double[][] createLSAMatrix(boolean dictionaryLookup) {

		if (!testSubject.equals("") && bagOfWords != null && modelAnswers != null) {
			int count = 0, orderV = 0;

			allStringList = new ArrayList<String>();

			allStringList.add(testSubject);

			for (String test : modelAnswers) {

				allStringList.add(test);
			}

			matrix = new double[bagOfWords.size()][allStringList.size()];
			wordOrderMatrix = new double[bagOfWords.size()][allStringList.size()];

			DictionaryLookUp dictionaryLookUp = new DictionaryLookUp();

			ArrayList<ArrayList<String>> sentences = wordTokenizeAsList(allStringList);

			for (int i = 0; i < bagOfWords.size(); i++) {

				for (int j = 0; j < allStringList.size(); j++) {

					count = 0;
					orderV = 0;

					ArrayList<String> test = sentences.get(j);

					if (test.contains(bagOfWords.get(i))) {

						orderV = test.indexOf(bagOfWords.get(i)) + 1;

						count = 1;

					} else {
						if (dictionaryLookup) {
							int order = 0;

							for (String word : test) {
								order++;
								count = (int) dictionaryLookUp.isSimilar(word, bagOfWords.get(i));
								if (count == 1) {

									orderV = order;
									break;
								}

							}
						} else {

							count = 0;
						}

					}

					matrix[i][j] = count;
					wordOrderMatrix[i][j] = orderV;
				}

			}

		}
		if (weightVector != null && domainWordSet != null) {

			weightmatrix = new double[bagOfWords.size()][allStringList.size()];

			for (int k = 0; k < bagOfWords.size(); k++) {

				for (int l = 0; l < allStringList.size(); l++) {

					weightmatrix[k][l] = matrix[k][l] * weightVector[k];

				}
			}

			matrix = weightmatrix;

		}

		return matrix;

	}

	public double[][] wordOrderMatrix() {

		if (!testSubject.equals("") && bagOfWords != null && modelAnswers != null) {
			int count = 0;

			allStringList = new ArrayList<String>();

			allStringList.add(testSubject);

			for (String test : modelAnswers) {

				allStringList.add(test);
			}

			wordOrderMatrix = new double[bagOfWords.size()][allStringList.size()];

			DictionaryLookUp dictionaryLookUp = new DictionaryLookUp();

			ArrayList<ArrayList<String>> sentences = wordTokenizeAsList(allStringList);

			for (int i = 0; i < bagOfWords.size(); i++) {

				for (int j = 0; j < allStringList.size(); j++) {

					count = 0;

					ArrayList<String> test = sentences.get(j);

					if (test.contains(bagOfWords.get(i))) {

						count = 1;

					} else {

						for (String word : test) {
							count = (int) dictionaryLookUp.isSimilar(word, bagOfWords.get(i));
							if (count == 1) {
								break;
							}

						}

					}

					wordOrderMatrix[i][j] = count;

				}

			}

		}
		return wordOrderMatrix;

	}

	public double cosineSimilarity(double[] docVector1, double[] docVector2) {
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;

		for (int i = 0; i < docVector1.length; i++) // docVector1 and docVector2
													// must be of same length
		{
			dotProduct += docVector1[i] * docVector2[i]; // a.b
			magnitude1 += Math.pow(docVector1[i], 2); // (a^2)
			magnitude2 += Math.pow(docVector2[i], 2); // (b^2)
		}

		magnitude1 = Math.sqrt(magnitude1);// sqrt(a^2)
		magnitude2 = Math.sqrt(magnitude2);// sqrt(b^2)

		if ((magnitude1 != 0.0) && (magnitude2 != 0.0)) {
			cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		} else {
			return 0.0;
		}
		return cosineSimilarity;
	}

	private double tfCalculator(String[] totalterms, String termToCheck) {
		double count = 0; // to count the overall occurrence of the term
							// termToCheck
		for (String s : totalterms) {
			if (s.equalsIgnoreCase(termToCheck)) {
				count++;
			}
		}
		return count / totalterms.length;
	}

	private double idfCalculator(List<String[]> allTerms, String termToCheck) {
		double count = 0;
		for (String[] ss : allTerms) {
			for (String s : ss) {
				if (s.equalsIgnoreCase(termToCheck)) {
					count++;
					break;
				}
			}
		}

		if (count != 0) {
			return Math.log(allTerms.size() / count);

		} else {

			return 0;
		}

	}

	public double[][] tfIdfCalculator() {

		double tf; // term frequency
		double idf; // inverse document frequency
		double tfidf; // term frequency inverse document frequency

		if (testSubject != null && bagOfWords != null && modelAnswers != null) {

			tf_idf_matrix = new double[bagOfWords.size()][allStringList.size()];

			for (int i = 0; i < bagOfWords.size(); i++) {

				for (int j = 0; j < allStringList.size(); j++) {

					tf = tfCalculator(wordTokenize(allStringList.get(j)), bagOfWords.get(i));
					idf = idfCalculator(wordTokenize(allStringList), bagOfWords.get(i));
					tfidf = tf * idf;

					tf_idf_matrix[i][j] = tfidf;

				}

			}

		}

		nlpObject.setTf_idf_matrix(tf_idf_matrix);
		return tf_idf_matrix;

	}

	public ArrayList<String[]> wordTokenize(ArrayList<String> sentences) {

		ArrayList<String[]> tokenizedTerms = new ArrayList<String[]>();

		for (int i = 0; i < sentences.size(); i++) {

			String[] tempTerms = wordTokenize(sentences.get(i)); // to
			tokenizedTerms.add(tempTerms);

		}

		return tokenizedTerms;
	}

	public ArrayList<ArrayList<String>> wordTokenizeAsList(ArrayList<String> sentences) {

		ArrayList<ArrayList<String>> tokenizedTerms = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < sentences.size(); i++) {

			String[] tempTerms = wordTokenize(sentences.get(i)); // to
			ArrayList<String> tokenizedTemp = new ArrayList<String>();

			for (String temp : tempTerms) {

				tokenizedTemp.add(temp);
			}

			tokenizedTemp.remove(" ");
			tokenizedTemp.remove("");

			tokenizedTerms.add(tokenizedTemp);

		}

		return tokenizedTerms;
	}

	public String[] wordTokenize(String sentences) {

		String[] tempTerms = sentences.toString().split(" "); // to

		return tempTerms;
	}

	public void printTF_IDF_Result(double[][] matrix) {

		NumberFormat formatter = new DecimalFormat("#0.00");

		String text = "	";
		System.out.println("----------Similarity Matrix Printing----------");
		if (allStringList != null && bagOfWords != null) {

			for (int k = 0; k < allStringList.size(); k++) {

				text = text + "S-" + (k + 1) + "	";

			}
			System.out.println(text);

			for (int i = 0; i < bagOfWords.size(); i++) {
				text = bagOfWords.get(i);
				for (int j = 0; j < allStringList.size(); j++) {

					text = text + "	" + formatter.format(matrix[i][j]);

				}
				System.out.println(text);
			}
		}
		System.out.println("---------Over----------");
	}

	public void printCosineSimilarityResult(Matrix matrix) {

		NumberFormat formatter = new DecimalFormat("#0.00");

		String text = "	";

		System.out.println("---------Cosine similarity Printing---------");
		if (allStringList != null && bagOfWords != null) {

			for (int k = 0; k < allStringList.size(); k++) {

				text = text + "S-" + (k + 1) + "	";

			}
			System.out.println(text);

			for (int i = 0; i < allStringList.size(); i++) {
				text = "S-" + (i + 1);
				for (int j = 0; j < allStringList.size(); j++) {

					double[] columnPackedCopy1 = matrix.getMatrix(0, matrix.getRowDimension() - 1, i, i)
							.getColumnPackedCopy();
					double[] columnPackedCopy2 = matrix.getMatrix(0, matrix.getRowDimension() - 1, j, j)
							.getColumnPackedCopy();

					text = text + "	" + formatter.format(cosineSimilarity(columnPackedCopy1, columnPackedCopy2));

				}
				System.out.println(text);
			}
		}
		System.out.println("---------Over----------");
	}

	public void getCosineSimilarityResult(double[][] lsaMatrix) {

		if (allStringList != null && bagOfWords != null && lsaMatrix != null) {

			similarityMatrix = new double[allStringList.size()][allStringList.size()];
			Matrix matrix = new Matrix(lsaMatrix);

			for (int i = 0; i < allStringList.size(); i++) {

				for (int j = 0; j < allStringList.size(); j++) {

					double[] columnPackedCopy1 = matrix.getMatrix(0, matrix.getRowDimension() - 1, i, i)
							.getColumnPackedCopy();
					double[] columnPackedCopy2 = matrix.getMatrix(0, matrix.getRowDimension() - 1, j, j)
							.getColumnPackedCopy();

					similarityMatrix[i][j] = cosineSimilarity(columnPackedCopy1, columnPackedCopy2);

				}

			}
		}

	}

	public double getWordOrderSimilarityResult(double[][] orderMatrix) {

		if (allStringList != null && bagOfWords != null && orderMatrix != null) {

			Matrix matrix = new Matrix(orderMatrix);

			double[] columnPackedCopy1 = matrix.getMatrix(0, matrix.getRowDimension() - 1, 0, 0).getColumnPackedCopy();

			double[] columnPackedCopy2 = matrix.getMatrix(0, matrix.getRowDimension() - 1, 1, 1).getColumnPackedCopy();

			// double cosineSimilarity =
			// cosineSimilarity(columnPackedCopy1,columnPackedCopy2);

			double[] sum, sub;

			sum = new double[columnPackedCopy2.length];
			sub = new double[columnPackedCopy2.length];

			for (int i = 0; i < columnPackedCopy1.length; i++) {

				sum[i] = columnPackedCopy1[i] + columnPackedCopy2[i];
				sub[i] = columnPackedCopy1[i] - columnPackedCopy2[i];
			}

			double sumT = 0, subT = 0;

			for (int i = 0; i < columnPackedCopy1.length; i++) {

				sumT = sumT + sum[i] * sum[i];
				subT = subT + sub[i] * sub[i];
			}

			double sqrt = (1-(Math.sqrt(subT)/Math.sqrt(sumT)));

			return sqrt;

		} else {

			return -1.00;
		}

	}

}
