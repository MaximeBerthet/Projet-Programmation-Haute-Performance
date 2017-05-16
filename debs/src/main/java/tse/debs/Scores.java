package tse.debs;

import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Scores {
	private static ArrayList<Integer> postsScores = new ArrayList<Integer>();
	private static ArrayList<Long> postsIds = new ArrayList<Long>();
	private static ArrayList<DateTime> postsStartDates = new ArrayList<DateTime>();
	private static ArrayList<DateTime> postsDeathDates = new ArrayList<DateTime>();
	private static ArrayList<String> postsAuthors = new ArrayList<String>();

	private static ArrayList<ArrayList<Integer>> postsCommentsScores = new ArrayList<ArrayList<Integer>>();
	private static ArrayList<ArrayList<Long>> postsCommentsIds = new ArrayList<ArrayList<Long>>();
	private static ArrayList<ArrayList<DateTime>> postsCommentsStartDates = new ArrayList<ArrayList<DateTime>>();
	private static ArrayList<ArrayList<Long>> postsCommentsAuthorsIds = new ArrayList<ArrayList<Long>>();
	private static ArrayList<Integer> postsCommentsAuthorsNb = new ArrayList<Integer>();

	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
			.withLocale(Locale.ROOT).withChronology(ISOChronology.getInstanceUTC());

	private static ArrayList<Integer> max = new ArrayList<Integer>();
	private static ArrayList<Integer> min = new ArrayList<Integer>();
	
	private static int premierpassage = 0;
	
	public Scores() {
		super();
	}

	public static ArrayList<Integer> getPostsScores() {
		return postsScores;
	}

	public static ArrayList<Long> getPostsIds() {
		return postsIds;
	}

	public static ArrayList<Integer> getPostsCommentsAuthorsNb() {
		return postsCommentsAuthorsNb;
	}

	public static  ArrayList<DateTime> getPostsStartDates() {
		return postsStartDates;
	}

	public static ArrayList<String> getPostsAuthors() {
		return postsAuthors;
	}

	public static ArrayList<ArrayList<Integer>> getPostsCommentsScores() {
		return postsCommentsScores;
	}

	public static ArrayList<ArrayList<Long>> getPostsCommentsIds() {
		return postsCommentsIds;
	}

	public static ArrayList<ArrayList<DateTime>> getPostsCommentsStartDates() {
		return postsCommentsStartDates;
	}

	public static ArrayList<ArrayList<Long>> getPostsCommentsAuthorsIds() {
		return postsCommentsAuthorsIds;
	}

	public static void openPost(String[] line) {
		postsScores.add(10);
		postsIds.add(Long.parseLong(line[1]));
		postsStartDates.add(formatter.parseDateTime(line[0]));
		postsDeathDates.add(formatter.parseDateTime(line[0]).plusDays(10));
		postsAuthors.add(line[4]);

		postsCommentsScores.add(new ArrayList<Integer>());
		postsCommentsIds.add(new ArrayList<Long>());
		postsCommentsStartDates.add(new ArrayList<DateTime>());
		postsCommentsAuthorsNb.add(0);
		postsCommentsAuthorsIds.add(new ArrayList<Long>());
	}

	public static void openComment(String[] line) {
		long linkId = -1;
		int size = postsIds.size();

		// The comment is linked to a post
		if (line[5].equals("") || line[5].equals("-1")) {
			linkId = Long.parseLong(line[6]);
			long id = -1;
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

						ArrayList<Long> ids = postsCommentsIds.get(i);
						ids.add(Long.parseLong(line[1]));
						postsCommentsIds.set(i, ids);

						ArrayList<DateTime> startDates = postsCommentsStartDates.get(i);
						startDates.add(commentDate);
						postsCommentsStartDates.set(i, startDates);

						// Update the death date of the post
						postsDeathDates.set(i, commentDate.plusDays(10));

						int sizeCommentsAuthorsIds = postsCommentsAuthorsIds.get(i).size();
						long commentAuthorId = Long.parseLong(line[2]);
						for (int j = 0; j <= sizeCommentsAuthorsIds; j++) {
							if (j == sizeCommentsAuthorsIds) {
								postsCommentsAuthorsIds.get(i).add(commentAuthorId);
								// Increment the number of comments authors
								postsCommentsAuthorsNb.set(i, postsCommentsAuthorsNb.get(i) + 1);
								break;
							} else if (postsCommentsAuthorsIds.get(i).get(j) == commentAuthorId) {
								break;
							}
						}
					}
					break;
				}

				i++;
			}

		} else { // The comment is linked to another comment
			linkId = Long.parseLong(line[5]);
			int i = 0;

			postsLoop: while (i < size) {

				ArrayList<Long> ids = postsCommentsIds.get(i);
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

							ids.add(Long.parseLong(line[1]));
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

	public static void calculMax(DateTime date) {

		for (int i = 0; i < max.size(); i++) {
			if (date.isBefore(postsStartDates.get(max.get(i)).plusDays(10))) {
				postsScores.set(max.get(i), 10 + Days.daysBetween(date, postsStartDates.get(max.get(i))).getDays());
			} else {
				postsScores.set(max.get(i), 0);
			}
		//}
		//for (int i = 0; i < max.size(); i++) {
			for (int j = 0; j < postsCommentsScores.get(max.get(i)).size(); j++) {
				if (date.isBefore(postsCommentsStartDates.get(max.get(i)).get(j).plusDays(10))) {
					
					postsCommentsScores.get(max.get(i)).set(j,
							10 + Days.daysBetween(date, postsCommentsStartDates.get(max.get(i)).get(j)).getDays());
					
				} else {
					postsCommentsScores.get(max.get(i)).set(j, 0);
				}
			}
		//}
		//for (int i = 0; i < max.size(); i++) {
			for (int j = 0; j < postsCommentsScores.get(max.get(i)).size(); j++) {
				postsScores.set(max.get(i), postsScores.get(max.get(i)) + postsCommentsScores.get(max.get(i)).get(j));
			}
			if (postsScores.get(max.get(i)) == 0) {
				deletePost(max.get(i));
				

			}
		}

	}

	public static void calcul(DateTime date) {

		for (int i = 0; i < postsScores.size(); i++) {
			if (date.isBefore(postsStartDates.get(i).plusDays(10))) {
				postsScores.set(i, 10 + Days.daysBetween(date, postsStartDates.get(i)).getDays());
			} else {
				postsScores.set(i, 0);
			}

		//}
		//for (int i = 0; i < postsScores.size(); i++) {
			for (int j = 0; j < postsCommentsScores.get(i).size(); j++) {
				if (date.isBefore(postsCommentsStartDates.get(i).get(j).plusDays(10))) {
					postsCommentsScores.get(i).set(j,
							10 + Days.daysBetween(date, postsCommentsStartDates.get(i).get(j)).getDays());
				} else {
					postsCommentsScores.get(i).set(j, 0);
				}
			}
		//}
		//for (int i = 0; i < postsScores.size(); i++) {
			for (int j = 0; j < postsCommentsScores.get(i).size(); j++) {
				postsScores.set(i, postsScores.get(i) + postsCommentsScores.get(i).get(j));
			}
			if (postsScores.get(i) == 0) {
				deletePost(i);
				
			}
		}
	}

	public static void calculMin(DateTime date) {

		for (int i = 0; i < min.size(); i++) {
			if (date.isAfter(postsDeathDates.get(min.get(i)))) {
				deletePost(min.get(i));
				
			} else {
				postsScores.set(min.get(i), 1);
			}
		}

	}

	private static void deletePost(int i) {
		// Delete the post
		postsScores.remove(i);
		postsIds.remove(i);
		postsStartDates.remove(i);
		postsDeathDates.remove(i);
		postsAuthors.remove(i);

		// Delete the comments linked to the posts
		postsCommentsScores.remove(i);
		postsCommentsIds.remove(i);
		postsCommentsStartDates.remove(i);
		postsCommentsAuthorsNb.remove(i);
		postsCommentsAuthorsIds.remove(i);

		for (int j = 0; j < min.size(); j++) {
			if (min.get(j) > i) {
				min.set(j, min.get(j) - 1);
			}
			if (min.get(j) == i) {
				min.remove(j);
				j--;
			}

		}
		for (int j = 0; j < max.size(); j++) {
			if (max.get(j) > i) {
				max.set(j, max.get(j) - 1);
			}
			if (max.get(j) == i) {
				max.remove(j);
				j--;
			}

		}
	}

	public static void Maximiser(int M1, int M2, boolean post, boolean r, int size) {
		

		if ((postsCommentsIds.size() == size) && (premierpassage == 0) || r == true) {
			for (int i = 0; i < postsCommentsIds.size(); i++) {
				if (10 + postsCommentsIds.get(i).size() * 10 >= M2) {
					max.add(i);
				} else {
					min.add(i);

				}

			}
			premierpassage = 1;
			r = false;

		} else if (M2 > M1) {
			if (post == true) {
				for (int i = 0; i < max.size(); i++) {
					 if (10 + postsCommentsIds.get(max.get(i)).size() * 10 <
					 M2) {
					//if (this.getPostsScores().get(max.get(i)) < M2) {
						min.add(max.get(i));
						max.remove(i);
						i--;
					}

				}
				if (M2 <= 10) {
					max.add(postsCommentsIds.size() - 1);
				} else {
					min.add(postsCommentsIds.size() - 1);
				}
			}
			if (post == false) {

				for (int i = 0; i < max.size(); i++) {
					 if (10 + postsCommentsIds.get(max.get(i)).size() * 10 +
					 10 < M2) {
					//if (this.getPostsScores().get(max.get(i)) + 10 < M2) {
						min.add(max.get(i));
						max.remove(i);
						i--;
					}

				}

			}

		} else if (M2 <= M1) {
			if (post == true) {
				if (min.size() > 0) {
					for (int i = 0; i < min.size(); i++) {
						if (10 + postsCommentsIds.get(min.get(i)).size() * 10 >= M2) {

							max.add(min.get(i));
							min.remove(i);
							i--;
						}

					}
				}
				if (M2 < 11) {
					max.add(postsCommentsIds.size() - 1);
				} else {
					min.add(postsCommentsIds.size() - 1);
				}
			}
			if (post == false) {
				if (min.size() > 0) {

					for (int i = 0; i < min.size(); i++) {
						if (10 + postsCommentsIds.get(min.get(i)).size() * 10 + 10 >= M2) {
							max.add(min.get(i));
							min.remove(i);
							i--;
						}

					}

				}
			}
		}
	}
}