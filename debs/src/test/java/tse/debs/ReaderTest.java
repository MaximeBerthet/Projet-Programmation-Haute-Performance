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
		tempListPost = inputFile.readLinePosts();
		while (tempListPost[0]!=null) {
			String finale = Arrays.toString(tempListPost).replace(", ", "|").replaceAll("[\\[\\]]", "");
			//System.out.println("finale " +finale);

			// On vérifie que les lignes soient les mêmes
			assertEquals(temp, finale);

			tempListPost = inputFile.readLinePosts();
			temp = br.readLine();
		}

		br.close();
	
		
		FileReader file2 = new FileReader(new File(path2));
		BufferedReader br2 = new BufferedReader(file2);
		temp = null;
		tempListComment = inputFile.readLineComments();
		temp = br2.readLine();
		while (tempListComment[0]!=null) {			
			String finale = Arrays.toString(tempListComment).replace(", ", "|").replaceAll("[\\[\\]]", "");
			//System.out.println("finale comment " +finale );


			// On vérifie que les lignes soient les mêmes
			assertEquals(temp, finale);

			temp = br2.readLine();
			tempListComment = inputFile.readLineComments();
		}
		br2.close();


	}

}
