package tse.debs;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import junit.framework.TestCase;

public class ReaderTest 
    extends TestCase
{
	@Test
	public void testReader() throws IOException {
		String path = "D:/Utilisateur/Victor/Bureau/Projet Haute performance/Tests/Q1BigTest/posts.dat";
		
		String expected="2010-02-11T19:23:29.459+0000|299394|4661|photo299394.jpg|Michael Wang";
		Reader inputFile=new Reader(path);
		
		ArrayList<String[]> tempList=inputFile.getList();
		String[] result=tempList.get(7);
		String finale=Arrays.toString(result).replace(", ", "|").replaceAll("[\\[\\]]", "");
		
		
		// On vérifie que les lignes soient les mêmes
		assertEquals(expected,finale);
		
		// On vérifie que le nombre de champ est bien égal
		assertEquals(5, result.length);
		
		// On vérifie que le fichier est bien un posts
		assertEquals(1, inputFile.getPostOrComment());
		
	}

}
