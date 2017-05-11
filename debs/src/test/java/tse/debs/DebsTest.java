package tse.debs;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Test;


public class DebsTest {
	
	@Test
	public void test() throws IOException {
		//String path = "C:/Tests/Q1BigTest/";
		String mainPath = "D:/Users/Baptiste/Documents/Telecom Saint-Etienne/FISE 2/Semestre 8/ProgrammationHautePerformance/Projet/Tests";
		String folderName = "Q1BigTest";
		String path = mainPath + "/" + folderName + "/";
		Debs d = new Debs(path, folderName);
		d.calcul();
	}
}
