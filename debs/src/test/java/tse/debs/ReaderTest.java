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
		String path = "D:/Utilisateur/Victor/Bureau/Projet Haute performance/Tests/Q1BigTest/";
		String path1 = "D:/Utilisateur/Victor/Bureau/Projet Haute performance/Tests/Q1BigTest/posts.dat";
		String path2 = "D:/Utilisateur/Victor/Bureau/Projet Haute performance/Tests/Q1BigTest/comments.dat";

		Reader inputFile = new Reader(path);

		String[] tempListComment = null;
		String[] tempListPost = null;


		FileReader file = new FileReader(new File(path1));
		BufferedReader br = new BufferedReader(file);
		String temp = null;
		temp = br.readLine();
		while (temp != null && temp!="" && !temp.equals("") && !temp.equals(null)) {
			//System.out.println("temp : " +temp);

			tempListPost = inputFile.readLinePosts();
			String finale = Arrays.toString(tempListPost).replace(", ", "|").replaceAll("[\\[\\]]", "");
			//System.out.println("finale " +finale);
			// On vérifie que les lignes soient les mêmes
			assertEquals(temp, finale);

			// On vérifie que le nombre de champ est bien égal
			//assertEquals(5, tempListPost.length);

			temp = br.readLine();
		}

		br.close();
		inputFile.closePosts();
		FileReader file2 = new FileReader(new File(path2));
		BufferedReader br2 = new BufferedReader(file2);

		temp = null;
		temp = br2.readLine();
		while (temp != null && temp!="" && !temp.equals("") && !temp.equals(null)) {
			//System.out.println("temp comment: " +temp);
			
			
			tempListComment = inputFile.readLineComments();
			String finale = Arrays.toString(tempListComment).replace(", ", "|").replaceAll("[\\[\\]]", "");
			//System.out.println("finale comment " +finale );
			// On vérifie que les lignes soient les mêmes
			assertEquals(temp, finale);

			// On vérifie que le nombre de champ est bien égal
			//assertEquals(5, tempListPost.length);

			temp = br2.readLine();
		}
		br2.close();
		inputFile.closeComments();
		// On vérifie que le fichier est bien un posts
		//assertEquals(true, inputFile.getPost());

	}

}
