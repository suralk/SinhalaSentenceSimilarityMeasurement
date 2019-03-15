package org.sentensesimilarity.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SentenseFile {

	ArrayList<SentensePair> list = new ArrayList<SentensePair>();

	public void loadSentenses(String path) throws IOException {

		BufferedReader br = null;

		try {

			br = new BufferedReader(new FileReader(path));

			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {

				String[] split = line.split("\t");
				list.add(new SentensePair(split[0], split[1], split[2], "NIL"));

				line = br.readLine();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			br.close();
		}

	}

	public void printSim() {

		int count=0;
		
		for (SentensePair test : list) {
			
			//true - for removing stop words.
			//(boolean lexicalDataBase,double tolerance,boolean stopWords,boolean contexWeight)
			test.evaluateSentenses(true,0.87,true,true);
			
			System.out.println(count +":"+test.getResult());
			

			try {

				File file = new File("result.txt");

				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(test.getResult());
				bw.write("\n");
				bw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			
			count++;
		}

	}

}
