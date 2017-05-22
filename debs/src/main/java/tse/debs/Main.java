package tse.debs;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Main {
	private Reader reader;
	private Scores scores = new Scores();
	private Tri tri = new Tri();
	private Display display;
	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
			.withLocale(Locale.ROOT).withChronology(ISOChronology.getInstanceUTC());
	private DateTime date = null;

	private boolean alwaysDisplay;

	private static boolean firstPassage = true;
	private static boolean secondPassage = true;

	private static int[] bestScores = new int[3];
	private static Long[] bestScoresOld = new Long[3];

	public Main(String path, String fileName, boolean alwaysDisplay) {
		this.alwaysDisplay = alwaysDisplay;
		this.reader = new Reader(path);
		this.display = new Display(scores, fileName);

		for (int i = 0; i < 3; i++) {
			bestScoresOld[i] = (long) -1;
		}
	}

	public void run() {
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
			display();

		}

		scores.stopTreads();

	}

	private void display() {
		if (alwaysDisplay) {
			display.addLine(bestScores, date);
		} else {
			if ((bestScoresOld[2] != (long) -1) && (bestScoresOld[1] != (long) -1) && (bestScores[2] != -1)
					&& (bestScores[1] != -1)) {
				if (((scores.getPostsIds().get(bestScores[0]) == bestScoresOld[0])
						&& (scores.getPostsIds().get(bestScores[1]) == bestScoresOld[1])
						&& (scores.getPostsIds().get(bestScores[2]) == bestScoresOld[2]))) {

				} else {
					display.addLine(bestScores, date);
				}
			} else if ((bestScoresOld[1] != (long) -1) && (bestScores[1] != (long) -1) && (secondPassage == false)) {
				if (((scores.getPostsIds().get(bestScores[0]) == bestScoresOld[0])
						&& (scores.getPostsIds().get(bestScores[1]) == bestScoresOld[1]))) {

				} else {
					display.addLine(bestScores, date);
				}
			} else if ((bestScoresOld[1] == -1) && (bestScores[1] != -1) && (secondPassage == true)) {
				display.addLine(bestScores, date);
				secondPassage = false;
			} else if ((bestScoresOld[0] != (long) -1) && (bestScores[0] != (long) -1) && firstPassage == false) {
				if (((scores.getPostsIds().get(bestScores[0]) != bestScoresOld[0]))) {
					display.addLine(bestScores, date);
				}
			} else if (firstPassage == true) {
				display.addLine(bestScores, date);
				firstPassage = false;
			}

			for (int i = 0; i < 3; i++) {
				if (bestScores[i] == -1) {
					bestScoresOld[i] = (long) -1;
				} else {
					bestScoresOld[i] = scores.getPostsIds().get(bestScores[i]);
				}

			}
		}
	}
}
