package tse.debs;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class DebsTest {
	// String path = "C:/Tests/Q1BigTest/"
	// String mainPath = "C:/Tests/";
	static String mainPath = "D:/Users/Baptiste/Documents/Telecom_Saint-Etienne/FISE_2/Semestre_8/ProgrammationHautePerformance/Projet/Tests";

	static String folderName = "100_000"; // !!

	static String path = mainPath + "/" + folderName + "/";
	static String fileOut = "DEBS" + folderName;

	static boolean bestpost = false;

	@Test
	public void test() throws IOException {
		Debs d = new Debs(path, fileOut);
		d.calcul(bestpost);
		// comparaison();
	}

	public static void comparaison() throws IOException {
		FileReader file = new FileReader(new File("../../TestFiles/" + fileOut + ".txt"));
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
