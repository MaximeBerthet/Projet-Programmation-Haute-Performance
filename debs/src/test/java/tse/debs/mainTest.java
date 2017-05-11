package tse.debs;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class mainTest {

	static Tri tri = new Tri();
	static int[] bestScores;

	// static String mainPath = "D:/Utilisateur/Victor/Bureau/Projet Haute
	static String mainPath = "D:/Users/Baptiste/Documents/Telecom Saint-Etienne/FISE 2/Semestre 8/ProgrammationHautePerformance/Projet";
	static String folderName = "dataDebs";
	static String path = mainPath + "/" + folderName + "/";

	static Reader reader = new Reader(path);

	static Scores scores = new Scores();

	static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
			.withLocale(Locale.ROOT).withChronology(ISOChronology.getInstanceUTC());

	static Display display = new Display(scores, folderName);

	static DateTime date = null;

	@Test
	public void test() throws IOException {
		long tps = System.currentTimeMillis();
		display.resetLogs();

		String[] linePost = reader.readLinePosts();
		String[] lineComment = reader.readLineComments();

		DateTime datePost = null;
		DateTime dateComment = null;

		while (linePost[0] != (null) || lineComment[0] != (null)) {
			if (linePost[0] == (null) || lineComment[0] == (null)) {
				if (linePost[0] == (null)) {
					scores.openComment(lineComment);
					dateComment = formatter.parseDateTime(lineComment[0]);
					scores.calcul(dateComment);
					lineComment = reader.readLineComments();
					date = dateComment;

				} else {
					scores.openPost(linePost);
					datePost = formatter.parseDateTime(linePost[0]);
					scores.calcul(datePost);
					linePost = reader.readLinePosts();
					date = datePost;

				}

			} else {
				datePost = formatter.parseDateTime(linePost[0]);
				dateComment = formatter.parseDateTime(lineComment[0]);
				if (dateComment.isBefore(datePost)) {
					scores.openComment(lineComment);
					scores.calcul(dateComment);
					lineComment = reader.readLineComments();
					date = dateComment;

				} else {
					scores.openPost(linePost);
					scores.calcul(datePost);
					linePost = reader.readLinePosts();
					date = datePost;
				}
			}

			bestScores = tri.Trier(Scores.getPostsScores());
			display.addLine(bestScores, date);
		}

		FileReader file = new FileReader(new File("../../TestFiles/" + folderName + ".txt"));
		BufferedReader br = new BufferedReader(file);
		String temp = null;
		temp = br.readLine();

		FileReader fileExpected = new FileReader(new File(path + "_expectedQ1.txt"));
		BufferedReader brExpected = new BufferedReader(fileExpected);
		String tempExpected = null;
		tempExpected = brExpected.readLine();

		while (tempExpected != null || temp != null /* && temp != "" */) {
			assertEquals(tempExpected, temp);
			tempExpected = brExpected.readLine();
			temp = br.readLine();
		}

		br.close();
		brExpected.close();

		System.out.println((System.currentTimeMillis() - tps) + " ms");
		// fail("Not yet implemented");
	}

}
