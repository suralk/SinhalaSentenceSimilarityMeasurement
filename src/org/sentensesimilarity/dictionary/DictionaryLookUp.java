package org.sentensesimilarity.dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.kadupitiya.dbhandler.DBConnectionManager;


public class DictionaryLookUp {

	// static HashMap<String, Integer> hashMap;
	static HashMap<String, ArrayList<Integer>> hashMap;
	static HashMap<Integer, ArrayList<String>> hashMapSyn;
	static ArrayList<Integer> id;
	static ArrayList<String> word;

	public DictionaryLookUp() {

	}

	static {

		try {
			File toRead = new File("data/idfile.dat");
			FileInputStream fis = new FileInputStream(toRead);
			ObjectInputStream ois = new ObjectInputStream(fis);

			id = (ArrayList<Integer>) ois.readObject();

			ois.close();
			fis.close();
			File toRead2 = new File("data/wordfile.dat");
			FileInputStream fis2 = new FileInputStream(toRead2);
			ObjectInputStream ois2 = new ObjectInputStream(fis2);

			word = (ArrayList<String>) ois2.readObject();

			ois2.close();
			fis.close();

		} catch (Exception e) {
		}

		hashMap = new HashMap<String, ArrayList<Integer>>();
		hashMapSyn = new HashMap<Integer, ArrayList<String>>();

		try {

			for (int i = 0; i < word.size(); i++) {

				if (hashMapSyn.containsKey(id.get(i))) {

					ArrayList<String> arrayList = hashMapSyn.get(id.get(i));
					arrayList.add(word.get(i));

				} else {

					ArrayList<String> arrayList = new ArrayList<String>();
					arrayList.add(word.get(i));
					hashMapSyn.put(id.get(i), arrayList);

				}

				if (hashMap.containsKey(word.get(i))) {

					ArrayList<Integer> arrayList = hashMap.get(word.get(i));
					arrayList.add(id.get(i));

				} else {

					ArrayList<Integer> arrayList = new ArrayList<Integer>();
					arrayList.add(id.get(i));
					hashMap.put(word.get(i), arrayList);

				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	static {
//
//		// hashMap = new HashMap<String, Integer>();
//		hashMap = new HashMap<String, ArrayList<Integer>>();
//
//		hashMapSyn = new HashMap<Integer, ArrayList<String>>();
//		id = new ArrayList<Integer>();
//		word = new ArrayList<String>();
//
//		String query = "SELECT * FROM lexicon WHERE language='SI'";
//
//		DBConnectionManager dbConnectionManager = new DBConnectionManager();
//		ResultSet result = null;
//		try {
//			result = dbConnectionManager.getResult(query);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		try {
//
//			while (result.next()) {
//
//				// hashMap.put(result.getString("word"), result.getInt("id"));
//
//				if (hashMap.containsKey(result.getString("word"))) {
//
//					ArrayList<Integer> arrayList = hashMap.get(result.getString("word"));
//					arrayList.add(result.getInt("id"));
//
//				} else {
//
//					ArrayList<Integer> arrayList = new ArrayList<Integer>();
//					arrayList.add(result.getInt("id"));
//					hashMap.put(result.getString("word"), arrayList);
//
//				}
//
//				word.add(result.getString("word"));
//				id.add(result.getInt("id"));
//
//				if (hashMapSyn.containsKey(result.getInt("id"))) {
//
//					ArrayList<String> arrayList = hashMapSyn.get(result.getInt("id"));
//					arrayList.add(result.getString("word"));
//
//				} else {
//
//					ArrayList<String> arrayList = new ArrayList<String>();
//					arrayList.add(result.getString("word"));
//					hashMapSyn.put(result.getInt("id"), arrayList);
//
//				}
//
//			}
//
//			try {
//				File fileOne = new File("data/idfile.dat");
//				FileOutputStream fos = new FileOutputStream(fileOne);
//				ObjectOutputStream oos = new ObjectOutputStream(fos);
//
//				// ArrayList<String> arrayList2 = hashMapSyn.get(379);
//				// System.out.println(arrayList2.size());
//				oos.writeObject(id);
//				oos.flush();
//				oos.close();
//				fos.close();
//
//				File fileOne2 = new File("data/wordfile.dat");
//				FileOutputStream fos2 = new FileOutputStream(fileOne2);
//				ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
//
//				oos2.writeObject(word);
//				oos2.flush();
//				oos2.close();
//				fos2.close();
//
//			} catch (Exception e) {
//			}
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		dbConnectionManager.closeConnection();
//		// System.out.println(hashMap.size());
//
//	}

	// public int isSimilar(String word, String string) {
	// // TODO Auto-generated method stub
	// return 0;
	// }

	public double isSimilar(String word1, String word2) {

		try {

			ArrayList<Integer> word1ID = null, word2ID = null;

			ArrayList<String> wordList1 = null, wordList2 = null;

			if (hashMap.containsKey(word1)) {

				word1ID = hashMap.get(word1);

			} else {

				int length = word1.length();

				int allowable = length / 2 + 1;

				boolean found = false;

				while (allowable <= length && !found) {

					length--;

					found = hashMap.containsKey(word1.substring(0, length));
					if (found) {
						word1ID = hashMap.get(word1.substring(0, length));
					}

				}

			}

			if (hashMap.containsKey(word2)) {

				word2ID = hashMap.get(word2);

			} else {

				int length = word2.length();

				int allowable = length / 2 + 1;

				boolean found = false;

				while (allowable <= length && !found) {

					length--;

					found = hashMap.containsKey(word2.substring(0, length));
					if (found) {
						word2ID = hashMap.get(word2.substring(0, length));
					}

				}

			}

			if (word1ID != null && word2ID != null) {

				for (int test : word1ID) {
					if (word2ID.contains(test)) {

						return 1.0;
					}

				}

				ArrayList<String> arrayListword1ID = new ArrayList<String>();

				for (int test : word1ID) {

					ArrayList<String> arrayList = hashMapSyn.get(test);
					for (String te : arrayList) {
						arrayListword1ID.add(te);
					}

				}

				ArrayList<String> arrayListword2ID = new ArrayList<String>();

				for (int test : word2ID) {

					ArrayList<String> arrayList = hashMapSyn.get(test);
					for (String te : arrayList) {
						arrayListword2ID.add(te);
					}

				}

				// int indexOf = id.indexOf(word1ID);
				// String word11 = word.get(indexOf);

				// ArrayList<String> arrayList = hashMapSyn.get(word1ID);

				// int indexOf2 = word.indexOf(word2ID);
				// Integer integer2 = id.get(indexOf2);

				// ArrayList<String> arrayList2 = hashMapSyn.get(word2ID);

				for (String test : arrayListword2ID) {
					if (arrayListword1ID.contains(test)) {

						return 1.0;
					}

				}

				return 0.0;

			} else {

				return 0;
			}

		} catch (Exception ex) {

			System.out.println(ex);
			return 0;

		}

	}

	// public double isSimilar(String word1, String word2) {
	// DBConnectionManager dbHandler = new DBConnectionManager();
	// try {
	//
	// int word1ID = 0, word2ID = 0;
	//
	// ArrayList<String> wordList1 = null, wordList2 = null;
	//
	// ResultSet searchR = dbHandler
	// .getResult("SELECT id FROM lexicon WHERE word='" + word1 + "' and
	// language='SI'");
	//
	// if (searchR.next()) {
	//
	// word1ID = searchR.getInt("id");
	//
	// } else {
	//
	// int length = word1.length();
	//
	// int allowable = length / 2 + 1;
	//
	// boolean found = false;
	//
	// while (allowable <= length && !found) {
	//
	// length--;
	//
	// searchR = dbHandler.getResult(
	// "SELECT id FROM lexicon WHERE word='" + word1.substring(0, length) + "'
	// and language='SI'");
	//
	// found = searchR.next();
	// if (found) {
	// word1ID = searchR.getInt("id");
	// }
	//
	// }
	//
	// }
	//
	// ResultSet searchR2 = dbHandler.getResult("SELECT id FROM lexicon WHERE
	// word='" + word2 + "' and language='SI'");
	//
	// if (searchR2.next()) {
	//
	// word2ID = searchR2.getInt("id");
	//
	// } else {
	//
	// int length = word2.length();
	//
	// int allowable = length / 2 + 1;
	//
	// boolean found = false;
	//
	// while (allowable <= length && !found) {
	//
	// length--;
	//
	// searchR = dbHandler.getResult(
	// "SELECT id FROM lexicon WHERE word='" + word2.substring(0, length) + "'
	// and language='SI'");
	//
	// found = searchR.next();
	// if (found) {
	// word2ID = searchR.getInt("id");
	// }
	//
	// }
	//
	// }
	//
	// if (word1ID != 0 && word2ID != 0) {
	//
	// if (word1ID == word2ID) {
	// dbHandler.closeConnection();
	// return 1.0;
	//
	// } else {
	//
	// wordList1 = new ArrayList<String>();
	// wordList2 = new ArrayList<String>();
	//
	// ResultSet searchR3 = dbHandler
	// .getResult("SELECT word FROM lexicon WHERE id='" + word1ID + "' and
	// language='SI'");
	//
	// while (searchR3.next()) {
	//
	// wordList1.add(searchR3.getString("word"));
	//
	// }
	//
	// ResultSet searchR4 = dbHandler
	// .getResult("SELECT word FROM lexicon WHERE id='" + word2ID + "' and
	// language='SI'");
	//
	// while (searchR4.next()) {
	//
	// wordList2.add(searchR4.getString("word"));
	//
	// }
	//
	// for (String test : wordList2) {
	// if (wordList1.contains(test)) {
	// dbHandler.closeConnection();
	// return 1.0;
	// }
	//
	// }
	// dbHandler.closeConnection();
	// return 0.0;
	// }
	//
	// } else {
	// dbHandler.closeConnection();
	// return 0;
	// }
	//
	// } catch (Exception ex) {
	// dbHandler.closeConnection();
	// return 0;
	//
	// }
	//
	// }

}
