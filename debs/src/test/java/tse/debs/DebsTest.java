package tse.debs;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Test;


public class DebsTest {
	//String path = "C:/Tests/Q1BigTest/"
	//String mainPath = "C:/Tests/";
	String mainPath = "D:/Users/Baptiste/Documents/Telecom Saint-Etienne/FISE 2/Semestre 8/ProgrammationHautePerformance/Projet/Tests";
	String folderName = "Q1CommentCount";
	String path = mainPath + "/" + folderName + "/";
	
	boolean bestpost=false;
	@Test
	public void test() throws IOException {
		Debs d = new Debs(path, folderName);
		d.calcul(bestpost);
		comparaison();
	}
	
	public void comparaison() throws IOException {
		FileReader file = new FileReader(new File("../../TestFiles/" + folderName + ".txt"));
		BufferedReader br = new BufferedReader(file);
		String temp = null;
		temp = br.readLine();

		FileReader fileExpected = new FileReader(new File(path + "_expectedQ1.txt"));
		BufferedReader brExpected = new BufferedReader(fileExpected);
		String tempExpected = null;
		tempExpected = brExpected.readLine();

		ArrayList<Integer> falseLines = new ArrayList<Integer>();
		int line = 0;

		while (tempExpected != null && temp != null) {
			line++;
			if (!tempExpected.toString().equals(temp.toString())) {
				falseLines.add(line);
			}
			tempExpected = brExpected.readLine();
			temp = br.readLine();
		}

		System.out.println("Lignes fausses :" + falseLines);
		
		if (tempExpected != null || temp != null) {
			fail("nombre de lignes différentes");
		}

		assertEquals(0, falseLines.size());

		br.close();
		brExpected.close();
	}
}
