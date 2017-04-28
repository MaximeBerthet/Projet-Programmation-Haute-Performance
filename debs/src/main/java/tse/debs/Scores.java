package tse.debs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Scores {
	static ArrayList<String[]> file = new ArrayList<String[]>();

	static ArrayList<Integer> postsScores = new ArrayList<Integer>();
	static ArrayList<Integer> postsIds = new ArrayList<Integer>();
	static ArrayList<Integer> postsNbComments = new ArrayList<Integer>();
	static ArrayList<DateTime> postsStartDates = new ArrayList<DateTime>();
	static ArrayList<String> postsAuthors = new ArrayList<String>();

	static ArrayList<Integer> commentsScores = new ArrayList<Integer>();
	static ArrayList<Integer> commentsIds = new ArrayList<Integer>();
	static ArrayList<Integer> commentsLinkedIds = new ArrayList<Integer>();
	static ArrayList<DateTime> commentsStartDates = new ArrayList<DateTime>();

	public static ArrayList<Integer> getPostsScores() {
		return postsScores;
	}

	public static ArrayList<Integer> getPostsIds() {
		return postsIds;
	}

	public static ArrayList<Integer> getPostsNbComments() {
		return postsNbComments;
	}

	public static ArrayList<DateTime> getPostsStartDates() {
		return postsStartDates;
	}

	public static ArrayList<String> getPostsAuthors() {
		return postsAuthors;
	}

	public static ArrayList<Integer> getCommentsScores() {
		return commentsScores;
	}

	public static ArrayList<Integer> getCommentsIds() {
		return commentsIds;
	}

	public static ArrayList<Integer> getCommentsLinkedIds() {
		return commentsLinkedIds;
	}

	public static ArrayList<DateTime> getCommentsStartDates() {
		return commentsStartDates;
	}

	static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
			.withLocale(Locale.ROOT).withChronology(ISOChronology.getInstanceUTC());;

	public Scores() {
		super();
	}

	public void processFile(ArrayList<String[]> file, boolean post) {
		if (post) {
			openPost(file);
		} else {
			openComment(file);
		}
		calcul();
	}

	private static void openPost(ArrayList<String[]> file) {
		Iterator<String[]> it = file.iterator();

		while (it.hasNext()) {
			String[] line = (String[]) it.next();

			postsScores.add(10);
			postsIds.add(Integer.parseInt(line[1]));
			postsNbComments.add(0);
			postsStartDates.add(formatter.parseDateTime(line[0]));
			postsAuthors.add(line[4]);

		}
	}

	private static void openComment(ArrayList<String[]> file) {
		Iterator<String[]> it = file.iterator();

		while (it.hasNext()) {
			String[] line = (String[]) it.next();

			// The comment is linked to a post
			if (line[5] == "") {
				commentsScores.add(10);
				commentsIds.add(Integer.parseInt(line[1]));
				commentsLinkedIds.add(Integer.parseInt(line[6]));
				commentsStartDates.add(formatter.parseDateTime(line[0]));

			} else { // The comment is linked to another comment
				int size = commentsScores.size();
				int i = 0;
				commentsScores.add(10);
				commentsIds.add(Integer.parseInt(line[1]));
				commentsStartDates.add(formatter.parseDateTime(line[0]));
				int linkId = Integer.parseInt(line[5]);
				boolean validLink = false;
				while (!validLink && i < size) {
					if (linkId == commentsIds.get(i)) {
						commentsLinkedIds.add(commentsLinkedIds.get(i));
						validLink = true;
					}
					i++;
				}
				if (!validLink) {
					commentsLinkedIds.add(-1); // Invalid link
				}
			}
		}
	}

	public void calcul() {
		if (commentsStartDates.size() > 0) {
			if (postsStartDates.get(postsStartDates.size() - 1)
					.isAfter(commentsStartDates.get(commentsStartDates.size() - 1))) {
				for (int i = 0; i < postsScores.size(); i++) {
					postsScores.set(i, 10
							+ Days.daysBetween(postsStartDates.get(postsStartDates.size() - 1), postsStartDates.get(i))
									.getDays());
				}
				for (int i = 0; i < commentsScores.size(); i++) {
					commentsScores.set(i, 10 + Days
							.daysBetween(postsStartDates.get(postsStartDates.size() - 1), commentsStartDates.get(i))
							.getDays());
				}

			} else {
				for (int i = 0; i < postsScores.size(); i++) {
					postsScores.set(i, 10 + Days
							.daysBetween(commentsStartDates.get(commentsStartDates.size() - 1), postsStartDates.get(i))
							.getDays());
				}
				for (int i = 0; i < commentsScores.size(); i++) {
					commentsScores.set(i, 10 + Days
							.daysBetween(commentsStartDates.get(commentsStartDates.size() - 1), postsStartDates.get(i))
							.getDays());
				}
			}
			for (int i = 0; i < postsScores.size(); i++) {
				for (int j = 0; j < commentsScores.size(); j++) {
					if (commentsLinkedIds.get(j) == postsIds.get(i)) {
						postsScores.set(i, postsScores.get(i) + commentsScores.get(j));
					}
				}
			}
		} else {
			for (int i = 0; i < postsScores.size(); i++) {
				postsScores.set(i, 10
						+ Days.daysBetween(postsStartDates.get(postsStartDates.size() - 1), postsStartDates.get(i))
								.getDays());
			}
		}
	}

}
