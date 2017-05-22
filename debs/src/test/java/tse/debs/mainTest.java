package tse.debs;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class mainTest {

	Tri tri = new Tri();
	int[] bestScores;

	// static String mainPath = "D:/Utilisateur/Victor/Bureau/Projet Haute
	static String mainPath = "D:/Users/Baptiste/Documents/Telecom_Saint-Etienne/FISE_2/Semestre_8/ProgrammationHautePerformance/Projet/Tests";

	static String folderName = "100_000";

	static String path = mainPath + "/" + folderName + "/";
	static String fileOut = "multithreading" + folderName;

	Reader reader = new Reader(path);

	Scores scores = new Scores();

	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ").withLocale(Locale.ROOT)
			.withChronology(ISOChronology.getInstanceUTC());

	Display display = new Display(scores, fileOut);

	DateTime date = null;

	@Test
	public void test() throws IOException {
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

			bestScores = tri.Trier(scores.getPostsScores());
			display.addLine(bestScores, date);

		}
		scores.stopTreads();

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
