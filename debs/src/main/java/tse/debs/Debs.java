package tse.debs;

import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Debs {
	static String folderName;
	static String fileName;
	static String[] comment = new String[7];
	static String[] post = new String[5];

	static Scores scores = new Scores();
	static Display display;
	static Reader reader;
	Tri tri = new Tri();

	static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
			.withLocale(Locale.ROOT).withChronology(ISOChronology.getInstanceUTC());
	static DateTime date = null;

	boolean p = false;
	boolean r = false;
	int nbpost = 0;
	int nbpostAtt = 5;
	int nbpostAtt2 = 5;
	int M1 = 0;
	int M2 = 0;
	int[] list;

	public Debs(String folder, String fileName) {
		super();
		this.folderName = folder;
		this.fileName = fileName;

		display = new Display(scores, fileName);
		display.resetLogs();
		reader = new Reader(folder);
	}

	public void calcul() {
		

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
						tri.Maximiser(scores.postsCommentsIds, M1, M2, scores.max, scores.min, p,r);
						scores.calculMax(dateComment);
						scores.calculMin(dateComment);
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
						tri.Maximiser(scores.postsCommentsIds, M1, M2, scores.max, scores.min, p,r);
						scores.calculMax(datePost);
						scores.calculMin(datePost);

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
						tri.Maximiser(scores.postsCommentsIds, M1, M2, scores.max, scores.min, p,r);
						scores.calculMax(datePost);
						scores.calculMin(datePost);
					}
					comment = reader.readLineComments();
					date = dateComment;

				} else {
					scores.openPost(post);
					nbpost = scores.getPostsIds().size();
					if (nbpost < nbpostAtt) {
						scores.calcul(datePost);
					} else {
						tri.Maximiser(scores.postsCommentsIds, M1, M2, scores.max, scores.min, p,r);
						scores.calculMax(dateComment);
						scores.calculMin(dateComment);
					}
					post = reader.readLinePosts();
					date = datePost;

				}
			}

			list=tri.Trier(scores.postsScores);
			display.addLine(list, date);
			M1=M2;
			nbpost=scores.getPostsIds().size();
			if (nbpost >=4) {
				M2=scores.postsScores.get(list[2]);
			}
			if (M2<=30){
				nbpostAtt=1000000;
			}
			else{
				nbpostAtt=nbpostAtt2;
				r=true;
			}
		}
	}
}