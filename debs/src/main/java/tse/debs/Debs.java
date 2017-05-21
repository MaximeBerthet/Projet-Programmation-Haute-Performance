package tse.debs;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Debs {
	private static String folderName;
	private static String fileName;
	private static String[] comment = new String[7];
	private static String[] post = new String[5];

	private static Scores scores = new Scores();
	private static Display display;
	private static Reader reader;
	private static Tri tri = new Tri();

	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
			.withLocale(Locale.ROOT).withChronology(ISOChronology.getInstanceUTC());
	private static DateTime date = null;

	private static boolean p = false;
	private static boolean deuxiemepassage = true;
	private static boolean premierpassage = true;
	private static int nbpost = 0;
	private static int nbpostAtt = 5;
	private static int M1 = 0;
	private static int M2 = 0;
	private static int[] list = new int[3];
	private static int[] list2 = new int[3];

	public Debs(String folder, String fileName) {
		super();
		Debs.folderName = folder;
		Debs.fileName = fileName;

		display = new Display(scores, fileName);

		reader = new Reader(folder);
	}

	public static void calcul(boolean bestpost) {

		display.resetLogs();

		comment = reader.readLineComments();
		post = reader.readLinePosts();

		while ((post[0] != null) || (comment[0] != null)) {

			if (post[0] == (null) || comment[0] == (null)) {
				if (post[0] == (null)) {
					p = false;
					scores.openComment(comment);
					nbpost = scores.getPostsIds().size();
					DateTime dateComment = formatter.parseDateTime(comment[0]);
					if (nbpost < nbpostAtt) {
						scores.calcul(dateComment);

					} else {
						scores.Maximiser(M1, M2, p, nbpostAtt);
						scores.calculMaxMin(dateComment);
					}

					// scores.calcul(dateComment);
					comment = reader.readLineComments();
					date = dateComment;

				} else {
					p = true;
					scores.openPost(post);
					nbpost = scores.getPostsIds().size();
					DateTime datePost = formatter.parseDateTime(post[0]);
					if (nbpost < nbpostAtt) {
						scores.calcul(datePost);

					} else {
						scores.Maximiser(M1, M2, p, nbpostAtt);
						scores.calculMaxMin(datePost);

					}
					// scores.calcul(datePost);
					post = reader.readLinePosts();
					date = datePost;

				}

			} else {
				DateTime dateComment = formatter.parseDateTime(comment[0]);
				DateTime datePost = formatter.parseDateTime(post[0]);
				p = dateComment.isAfter(datePost);
				if (!p) {
					scores.openComment(comment);
					nbpost = scores.getPostsIds().size();
					if (nbpost < nbpostAtt) {
						scores.calcul(dateComment);

					} else {
						scores.Maximiser(M1, M2, p, nbpostAtt);
						scores.calculMaxMin(dateComment);
					}
					comment = reader.readLineComments();
					date = dateComment;

				} else {
					scores.openPost(post);
					nbpost = scores.getPostsIds().size();
					if (nbpost < nbpostAtt) {
						scores.calcul(datePost);
					} else {
						scores.Maximiser(M1, M2, p, nbpostAtt);
						scores.calculMaxMin(datePost);
					}
					post = reader.readLinePosts();
					date = datePost;

				}
			}

			list2 = list.clone();
			list = tri.Trier(scores.getPostsScores());
			if (bestpost == true) {
				if ((list2[2] != -1) && (list2[1] != -1) && (list[2] != -1) && (list[1] != -1)) {
					if (((scores.getPostsIds().get(list[0]) == scores.getPostsIds().get(list2[0]))
							&& (scores.getPostsIds().get(list[1]) == scores.getPostsIds().get(list2[1]))
							&& (scores.getPostsIds().get(list[2]) == scores.getPostsIds().get(list2[2])))) {

					} else {
						display.addLine(list, date);
					}
				} else if ((list2[1] != -1) && (list[1] != -1) && (deuxiemepassage == false)) {
					if (((scores.getPostsIds().get(list[0]) == scores.getPostsIds().get(list2[0]))
							&& (scores.getPostsIds().get(list[1]) == scores.getPostsIds().get(list2[1])))) {

					} else {
						display.addLine(list, date);
					}
				} else if ((list2[1] == -1) && (list[1] != -1) && (deuxiemepassage == true)) {
					display.addLine(list, date);
					deuxiemepassage = false;
				} else if ((list2[0] != -1) && (list[0] != -1) && premierpassage == false) {
					if (((scores.getPostsIds().get(list[0]) != scores.getPostsIds().get(list2[0])))) {
						display.addLine(list, date);
					}
				} else if (premierpassage == true) {
					display.addLine(list, date);
					premierpassage = false;
				}

			} else {
				display.addLine(list, date);
				/*
				 * if (scores.tr== true){ System.out.println(k);
				 * scores.tr=false; } k++;
				 */
			}
			M1 = M2;
			nbpost = scores.getPostsIds().size();
			if (nbpost >= 3) {
				M2 = scores.getPostsScores().get(list[2]);
			} else if ((nbpost == 2)) {
				M2 = scores.getPostsScores().get(list[1]);
			} else {
				M2 = scores.getPostsScores().get(list[0]);
			}

		}
		
		scores.stopTreads();
	}
}