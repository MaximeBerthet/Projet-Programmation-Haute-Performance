package tse.debs;

import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Scores {
	private ArrayList<Integer> postsScores = new ArrayList<Integer>();
	private ArrayList<Long> postsIds = new ArrayList<Long>();
	private ArrayList<DateTime> postsStartDates = new ArrayList<DateTime>();
	private ArrayList<DateTime> postsDeathDates = new ArrayList<DateTime>();
	private ArrayList<String> postsAuthors = new ArrayList<String>();

	private ArrayList<ArrayList<Integer>> postsCommentsScores = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Long>> postsCommentsIds = new ArrayList<ArrayList<Long>>();
	private ArrayList<ArrayList<DateTime>> postsCommentsStartDates = new ArrayList<ArrayList<DateTime>>();
	private ArrayList<ArrayList<Long>> postsCommentsAuthorsIds = new ArrayList<ArrayList<Long>>();
	private ArrayList<Integer> postsCommentsAuthorsNb = new ArrayList<Integer>();

	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
			.withLocale(Locale.ROOT).withChronology(ISOChronology.getInstanceUTC());

	private ArrayList<Integer> max = new ArrayList<Integer>();
	private ArrayList<Integer> min = new ArrayList<Integer>();

	public Scores() {
		super();
	}

	public ArrayList<Integer> getPostsScores() {
		return postsScores;
	}

	public ArrayList<Long> getPostsIds() {
		return postsIds;
	}

	public ArrayList<Integer> getPostsCommentsAuthorsNb() {
		return postsCommentsAuthorsNb;
	}

	public ArrayList<DateTime> getPostsStartDates() {
		return postsStartDates;
	}

	public ArrayList<String> getPostsAuthors() {
		return postsAuthors;
	}

	public ArrayList<ArrayList<Integer>> getPostsCommentsScores() {
		return postsCommentsScores;
	}

	public ArrayList<ArrayList<Long>> getPostsCommentsIds() {
		return postsCommentsIds;
	}

	public ArrayList<ArrayList<DateTime>> getPostsCommentsStartDates() {
		return postsCommentsStartDates;
	}

	public ArrayList<ArrayList<Long>> getPostsCommentsAuthorsIds() {
		return postsCommentsAuthorsIds;
	}

	public ArrayList<Integer> getMax() {
		return max;
	}

	public void setMax(ArrayList<Integer> max) {
		this.max = max;
	}

	public ArrayList<Integer> getMin() {
		return min;
	}

	public void setMin(ArrayList<Integer> min) {
		this.min = min;
	}

	public void openPost(String[] line) {
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

	public void openComment(String[] line) {
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

	public void calculMax(DateTime date) {

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
			if (postsScores.get(max.get(i)) == 0) {
				deletePost(max.get(i));

			}
		}

	}

	public void calcul(DateTime date) {

		for (int i = 0; i < postsScores.size(); i++) {
			if (date.isBefore(postsStartDates.get(i).plusDays(10))) {
				postsScores.set(i, 10 + Days.daysBetween(date, postsStartDates.get(i)).getDays());
			} else {
				postsScores.set(i, 0);
			}

		}
		for (int i = 0; i < postsScores.size(); i++) {
			for (int j = 0; j < postsCommentsScores.get(i).size(); j++) {
				if (date.isBefore(postsCommentsStartDates.get(i).get(j).plusDays(10))) {
					postsCommentsScores.get(i).set(j,
							10 + Days.daysBetween(date, postsCommentsStartDates.get(i).get(j)).getDays());
				} else {
					postsCommentsScores.get(i).set(j, 0);
				}
			}
		}
		for (int i = 0; i < postsScores.size(); i++) {
			for (int j = 0; j < postsCommentsScores.get(i).size(); j++) {
				postsScores.set(i, postsScores.get(i) + postsCommentsScores.get(i).get(j));
			}
			if (postsScores.get(i) == 0) {
				deletePost(i);
			}
		}
	}

	public void calculMin(DateTime date) {

		for (int i = 0; i < min.size(); i++) {
			if (date.isAfter(postsDeathDates.get(min.get(i)))) {
				deletePost(min.get(i));
			}
		}

	}

	private void deletePost(int i) {
		// Delete the post
		postsScores.remove(i);
		postsIds.remove(i);
		postsStartDates.remove(i);
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
			if (min.get(j)==i){
				min.remove(j);
				j--;
			}
			

		}
		for (int j = 0; j < max.size(); j++) {
			if (max.get(j) > i) {
				max.set(j, max.get(j) - 1);
			}
			if (max.get(j)==i){
				max.remove(j);
				j--;
			}
			
		}

	}
}