package tse.debs;

import java.util.ArrayList;
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
	static ArrayList<DateTime> postsDeathDates = new ArrayList<DateTime>();
	static ArrayList<String> postsAuthors = new ArrayList<String>();

	static ArrayList<ArrayList<Integer>> postsCommentsScores = new ArrayList<ArrayList<Integer>>();
	static ArrayList<ArrayList<Integer>> postsCommentsIds = new ArrayList<ArrayList<Integer>>();
	static ArrayList<ArrayList<DateTime>> postsCommentsStartDates = new ArrayList<ArrayList<DateTime>>();

	static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
			.withLocale(Locale.ROOT).withChronology(ISOChronology.getInstanceUTC());

	public Scores() {
		super();
	}

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

	public static ArrayList<ArrayList<Integer>> getPostsCommentsScores() {
		return postsCommentsScores;
	}

	public static ArrayList<ArrayList<Integer>> getPostsCommentsIds() {
		return postsCommentsIds;
	}

	public static ArrayList<ArrayList<DateTime>> getPostsCommentsStartDates() {
		return postsCommentsStartDates;
	}

	public static void openPost(String[] line) {
		postsScores.add(10);
		postsIds.add(Integer.parseInt(line[1]));
		postsNbComments.add(0);
		postsStartDates.add(formatter.parseDateTime(line[0]));
		postsDeathDates.add(formatter.parseDateTime(line[0]).plusDays(10));
		postsAuthors.add(line[4]);

		postsCommentsScores.add(new ArrayList<Integer>());
		postsCommentsIds.add(new ArrayList<Integer>());
		postsCommentsStartDates.add(new ArrayList<DateTime>());
	}

	public static void openComment(String[] line) {
		int linkId = -1;
		// int size = commentsScores.size();
		int size = postsIds.size();

		// The comment is linked to a post
		if (line[5].equals("")) {
			linkId = Integer.parseInt(line[6]);
			int id = -1;
			int i = 0;

			while (i < size) {
				id = postsIds.get(i);

				if (id == linkId) {

					DateTime commentDate = formatter.parseDateTime(line[0]);

					// We first verify that the post is not dead
					if (commentDate.isAfter(postsDeathDates.get(i))) {
						// Delete the dead post
						deletePost(i);

					} else {
						ArrayList<Integer> scores = postsCommentsScores.get(i);
						scores.add(10);
						postsCommentsScores.set(i, scores);

						ArrayList<Integer> ids = postsCommentsIds.get(i);
						ids.add(Integer.parseInt(line[1]));
						postsCommentsIds.set(i, ids);

						ArrayList<DateTime> startDates = postsCommentsStartDates.get(i);
						startDates.add(commentDate);
						postsCommentsStartDates.set(i, startDates);

						// Update the death date of the post
						postsDeathDates.set(i, commentDate.plusDays(10));

						// Increment the counter of comments
						postsNbComments.set(i, postsNbComments.get(i) + 1);
					}
					break;
				}

				i++;
			}

		} else { // The comment is linked to another comment
			linkId = Integer.parseInt(line[5]);
			int i = 0;

			postsLoop: while (i < size) {

				ArrayList<Integer> ids = postsCommentsIds.get(i);
				int sizeIds = ids.size();
				int j = 0;

				while (j < sizeIds) {
					if (linkId == ids.get(j)) {
						DateTime commentDate = formatter.parseDateTime(line[0]);

						// We first verify that the post is not dead
						if (commentDate.isAfter(postsDeathDates.get(i))) {
							// Delete the dead post
							deletePost(i);

						} else {
							// We link the comment to a post

							ArrayList<Integer> scores = postsCommentsScores.get(i);
							scores.add(10);
							postsCommentsScores.set(i, scores);

							ids.add(Integer.parseInt(line[1]));
							postsCommentsIds.set(i, ids);

							ArrayList<DateTime> startDates = postsCommentsStartDates.get(i);
							startDates.add(commentDate);
							postsCommentsStartDates.set(i, startDates);

							// Update the death date of the post
							postsDeathDates.set(i, commentDate.plusDays(10));
						}
						break postsLoop;
					}

					j++;
				}

				i++;
			}

		}

	}

	public void calculMax(ArrayList<Integer> max, DateTime date) {

		for (int i = 0; i < max.size(); i++) {
			postsScores.set(max.get(i), 10 + Days.daysBetween(date, postsStartDates.get(max.get(i))).getDays());
		}
		for (int i = 0; i < max.size(); i++) {
			for (int j = 0; j < postsCommentsScores.get(max.get(i)).size(); j++) {
				postsCommentsScores.get(max.get(i)).set(j,
						10 + Days.daysBetween(date, postsCommentsStartDates.get(max.get(i)).get(j)).getDays());
			}
		}
		for (int i = 0; i < max.size(); i++) {
			for (int j = 0; j < postsCommentsScores.get(max.get(i)).size(); j++) {
				postsScores.set(i, postsScores.get(max.get(i)) + postsCommentsScores.get(max.get(i)).get(j));
			}
		}

	}

	public void calcul(DateTime date) {

		for (int i = 0; i < postsScores.size(); i++) {
			postsScores.set(i, 10 + Days.daysBetween(date, postsStartDates.get(i)).getDays());
		}
		for (int i = 0; i < postsScores.size(); i++) {
			for (int j = 0; j < postsCommentsScores.get(i).size(); j++) {
				postsCommentsScores.get(i).set(j,
						10 + Days.daysBetween(date, postsCommentsStartDates.get(i).get(j)).getDays());
			}
		}
		for (int i = 0; i < postsScores.size(); i++) {
			for (int j = 0; j < postsCommentsScores.get(i).size(); j++) {
				postsScores.set(i, postsScores.get(i) + postsCommentsScores.get(i).get(j));
			}
		}
	}

	public void calculMin(ArrayList<Integer> min, DateTime date) {

		for (int i = 0; i < min.size(); i++) {
			if (date.isAfter(postsDeathDates.get(min.get(i)))) {
				deletePost(min.get(i));
			}
		}
	}

	public static void deletePost(int i) {
		int id = postsIds.get(i);

		// Delete the post
		postsScores.remove(i);
		postsIds.remove(i);
		postsNbComments.remove(i);
		postsStartDates.remove(i);
		postsAuthors.remove(i);

		// Delete the comments linked to the posts
		postsCommentsScores.remove(i);
		postsCommentsIds.remove(i);
		postsCommentsStartDates.remove(i);
	}
}