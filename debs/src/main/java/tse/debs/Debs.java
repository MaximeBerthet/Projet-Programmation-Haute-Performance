package tse.debs;

import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Debs {
	private String folderName;
	private String fileName;
	private String[] comment = new String[7];
	private String[] post = new String[5];

	private Scores scores = new Scores();
	private Display display;
	private Reader reader;
	private Tri tri = new Tri();

	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
			.withLocale(Locale.ROOT).withChronology(ISOChronology.getInstanceUTC());
	private DateTime date = null;

	private boolean p = false;
	private boolean r = false;
	private boolean deuxiemepassage=true;
	private boolean premierpassage=true;
	private int nbpost = 0;
	private int nbpostAtt = 5;
	private int nbpostAtt2 = 5;
	private int M1 = 0;
	private int M2 = 0;
	private int[] list = new int[3];
	private int[] list2 = new int[3];

	public Debs(String folder, String fileName) {
		super();
		this.folderName = folder;
		this.fileName = fileName;

		display = new Display(scores, fileName);
		display.resetLogs();
		reader = new Reader(folder);
	}

	public void calcul(boolean bestpost) {

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
						tri.Maximiser(scores, M1, M2, p, r);
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
						tri.Maximiser(scores, M1, M2, p, r);
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
						tri.Maximiser(scores, M1, M2, p, r);
						scores.calculMax(dateComment);
						scores.calculMin(dateComment);
					}
					comment = reader.readLineComments();
					date = dateComment;

				} else {
					scores.openPost(post);
					nbpost = scores.getPostsIds().size();
					if (nbpost < nbpostAtt) {
						scores.calcul(datePost);
					} else {
						tri.Maximiser(scores, M1, M2, p, r);
						scores.calculMax(datePost);
						scores.calculMin(datePost);
					}
					post = reader.readLinePosts();
					date = datePost;

				}
			}

			list2=list.clone();
			list = tri.Trier(scores.getPostsScores());
			if (bestpost == true)  {
				if ((list2[2] != -1)&&(list2[1] != -1)&&(list[2] != -1)&&(list[1] != -1)){
					if (((scores.getPostsIds().get(list[0]) == scores.getPostsIds().get(list2[0]))
							&& (scores.getPostsIds().get(list[1]) == scores.getPostsIds().get(list2[1]))
							&& (scores.getPostsIds().get(list[2]) == scores.getPostsIds().get(list2[2])))) {
						
					}
					else{
						display.addLine(list, date);
					}
				}
				else if ((list2[1] != -1)&&(list[1] != -1)&&(deuxiemepassage==false)){
					if (((scores.getPostsIds().get(list[0]) == scores.getPostsIds().get(list2[0]))
							&& (scores.getPostsIds().get(list[1]) == scores.getPostsIds().get(list2[1])))) {
						
					}
					else{
						display.addLine(list, date);
					}
				}
				else if ((list2[1] == -1)&&(list[1] != -1)&&(deuxiemepassage==true)){
						display.addLine(list, date);
						deuxiemepassage=false;
				}
				else if ((list2[0] !=-1)&&(list[0] !=-1)&&premierpassage==false){ 
					if (((scores.getPostsIds().get(list[0]) != scores.getPostsIds().get(list2[0])))){
						display.addLine(list, date);
					}
				}
				else if (premierpassage==true){ 
					display.addLine(list, date);
					premierpassage=false;
				}
				
				
			}
			else {
				display.addLine(list, date);
			}
			M1 = M2;
			nbpost = scores.getPostsIds().size();
			if (nbpost >= 4) {
				M2 = scores.getPostsScores().get(list[2]);
			}
			// if (M2<=30){
			// nbpostAtt=1000000;
			// }
			// else{
			// nbpostAtt=nbpostAtt2;
			// r=true;
			// }
		}
	}
}