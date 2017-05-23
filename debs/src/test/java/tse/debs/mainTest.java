package tse.debs;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;

public class MainTest {
	// static String testsPath = "D:/Utilisateur/Victor/Bureau/Projet Haute
	static String testsPath = "D:/Users/Baptiste/Documents/Telecom_Saint-Etienne/FISE_2/Semestre_8/ProgrammationHautePerformance/Projet/Tests";

	 static String folderName = "Q1Basic";
	// static String folderName = "Q1Basic2";
	// static String folderName = "Q1BigTest";
	// static String folderName = "Q1Case1";
	// static String folderName = "Q1Case2";
	// static String folderName = "Q1Case3";
	// static String folderName = "Q1Case4";
	// static String folderName = "Q1Case5";
	// static String folderName = "Q1CommentCount";
	// static String folderName = "Q1PostExpiredComment";
	// static String folderName = "Q1PostExpiredComment2";
	
	//static String folderName = "2_000_000";

	static String path = testsPath + "/" + folderName + "/";
	static String fileName = "multithreading_" + folderName;
	boolean alwaysDisplay = true;

	@Test
	public void test() throws IOException {
		Main main = new Main(path, fileName, alwaysDisplay);
		main.run();
		comparaison();
	}

	public static void comparaison() throws IOException {
		FileReader file = new FileReader(new File("../../TestFiles/" + fileName + ".txt"));
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
		System.out.println(folderName + " : Lignes fausses :" + falseLines);

		if (tempExpected != null || temp != null) {
			fail("nombre de lignes différentes");
		}

		assertEquals(0, falseLines.size());

		br.close();
		brExpected.close();
	}

}
