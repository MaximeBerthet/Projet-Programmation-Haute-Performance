package tse.debs;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.junit.Test;

import junit.framework.TestCase;

public class ReaderTest extends TestCase {
	@Test
	public void testReader() throws IOException {
		String path = "D:/Utilisateur/Victor/Bureau/Projet Haute performance/Tests/Q1BigTest/posts.dat";

		Reader inputFile = new Reader(path);

		ArrayList<String[]> tempList = inputFile.getList();

		FileReader file = new FileReader(new File(path));
		BufferedReader br = new BufferedReader(file);
		String temp = null;
		int i = 0;
		while ((temp = br.readLine()) != null) {
			String[] result = tempList.get(i);
			String finale = Arrays.toString(result).replace(", ", "|").replaceAll("[\\[\\]]", "");

			// On vérifie que les lignes soient les mêmes
			assertEquals(temp, finale);

			// On vérifie que le nombre de champ est bien égal
			assertEquals(5, result.length);

			i++;
		}
		br.close();

		// On vérifie que le fichier est bien un posts
		assertEquals(1, inputFile.getPostOrComment());

	}

}
