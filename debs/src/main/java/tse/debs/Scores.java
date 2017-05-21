package tse.debs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Scores {
	private static List<Integer> postsScores = new ArrayList<Integer>();
	private static List<Long> postsIds = new ArrayList<Long>();
	private static List<DateTime> postsStartDates = new ArrayList<DateTime>();
	private static List<DateTime> postsDeathDates = new ArrayList<DateTime>();
	private static List<String> postsAuthors = new ArrayList<String>();

	private static List<ArrayList<Long>> postsCommentsIds = new ArrayList<ArrayList<Long>>();
	private static List<ArrayList<DateTime>> postsCommentsStartDates = new ArrayList<ArrayList<DateTime>>();
	private static List<ArrayList<Long>> postsCommentsAuthorsIds = new ArrayList<ArrayList<Long>>();
	private static List<Integer> postsCommentsAuthorsNb = new ArrayList<Integer>();

	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
			.withLocale(Locale.ROOT).withChronology(ISOChronology.getInstanceUTC());

	private static ArrayList<Integer> max = new ArrayList<Integer>();
	private static ArrayList<Integer> min = new ArrayList<Integer>();

	private static int premierpassage = 0;

	// CalculScores
	private static AtomicInteger threadCounter = new AtomicInteger();
	private static Lock lockScore = new ReentrantLock();
	private static Lock lockThreads = new ReentrantLock();

	private static AtomicInteger threadRun0 = new AtomicInteger(0);
	private static AtomicInteger threadRun1 = new AtomicInteger(0);
	private static AtomicInteger threadRun2 = new AtomicInteger(0);
	private static AtomicInteger threadRun3 = new AtomicInteger(0);

	private static AtomicInteger threadRun4 = new AtomicInteger(0);
	private static AtomicInteger threadRun5 = new AtomicInteger(0);
	private static AtomicInteger threadRun6 = new AtomicInteger(0);
	private static AtomicInteger threadRun7 = new AtomicInteger(0);

	private static List<Integer> deadPosts = Collections.synchronizedList(new ArrayList<Integer>());
	private static int nbThreads = 8;

	private CalculScores calculScore0 = new CalculScores(this, deadPosts, threadCounter, lockScore, lockThreads,
			threadRun0, 0, nbThreads);
	private CalculScores calculScore1 = new CalculScores(this, deadPosts, threadCounter, lockScore, lockThreads,
			threadRun1, 1, nbThreads);
	private CalculScores calculScore2 = new CalculScores(this, deadPosts, threadCounter, lockScore, lockThreads,
			threadRun2, 2, nbThreads);
	private CalculScores calculScore3 = new CalculScores(this, deadPosts, threadCounter, lockScore, lockThreads,
			threadRun3, 3, nbThreads);

	private CalculScores calculScore4 = new CalculScores(this, deadPosts, threadCounter, lockScore, lockThreads,
			threadRun4, 4, nbThreads);
	private CalculScores calculScore5 = new CalculScores(this, deadPosts, threadCounter, lockScore, lockThreads,
			threadRun5, 5, nbThreads);
	private CalculScores calculScore6 = new CalculScores(this, deadPosts, threadCounter, lockScore, lockThreads,
			threadRun6, 6, nbThreads);
	private CalculScores calculScore7 = new CalculScores(this, deadPosts, threadCounter, lockScore, lockThreads,
			threadRun7, 7, nbThreads);

	public Scores() {
		super();
		new Thread(calculScore0).start();
		new Thread(calculScore1).start();
		new Thread(calculScore2).start();
		new Thread(calculScore3).start();

		new Thread(calculScore4).start();
		new Thread(calculScore5).start();
		new Thread(calculScore6).start();
		new Thread(calculScore7).start();
	}

	public static void setPostsScores(int index, int newValue) {
		postsScores.set(index, newValue);
	}

	public static List<Integer> getPostsScores() {
		return postsScores;
	}

	public static List<Long> getPostsIds() {
		return postsIds;
	}

	public static List<Integer> getPostsCommentsAuthorsNb() {
		return postsCommentsAuthorsNb;
	}

	public static List<DateTime> getPostsStartDates() {
		return postsStartDates;
	}

	public static List<DateTime> getPostsDeathDates() {
		return postsDeathDates;
	}

	public static List<String> getPostsAuthors() {
		return postsAuthors;
	}

	public static List<ArrayList<Long>> getPostsCommentsIds() {
		return postsCommentsIds;
	}

	public static List<ArrayList<DateTime>> getPostsCommentsStartDates() {
		return postsCommentsStartDates;
	}

	public static List<ArrayList<Long>> getPostsCommentsAuthorsIds() {
		return postsCommentsAuthorsIds;
	}

	public static ArrayList<Integer> getMax() {
		return max;
	}

	public static ArrayList<Integer> getMin() {
		return min;
	}

	public static void openPost(String[] line) {
		postsScores.add(10);
		postsIds.add(Long.parseLong(line[1]));
		postsStartDates.add(formatter.parseDateTime(line[0]));
		postsDeathDates.add(formatter.parseDateTime(line[0]).plusDays(10));
		postsAuthors.add(line[4]);

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

	public void calculMaxMin(DateTime date) {
		/* Calcul Max */
		deadPosts.clear();
		threadCounter.set(0);
		setThreadsDate(date);
		setThreadsRun(2);

		// Start threads
		synchronized (lockThreads) {
			lockThreads.notifyAll();
		}

		// Wait for the threads to finish
		synchronized (lockScore) {
			while (threadCounter.get() < nbThreads) {
				try {
					lockScore.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		for (int i = 0; i < deadPosts.size(); i++) {
			deletePost(deadPosts.get(i));
		}
		
		/* Calcul Min */
		deadPosts.clear();
		threadCounter.set(0);
		setThreadsDate(date);
		setThreadsRun(3);

		// Start threads
		synchronized (lockThreads) {
			lockThreads.notifyAll();
		}

		// Wait for the threads to finish
		synchronized (lockScore) {
			while (threadCounter.get() < nbThreads) {
				try {
					lockScore.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		for (int i = 0; i < deadPosts.size(); i++) {
			deletePost(deadPosts.get(i));
		}

	}

	public void calcul(DateTime date) {
		deadPosts.clear();
		threadCounter.set(0);
		setThreadsDate(date);
		setThreadsRun(1);

		// Start threads
		synchronized (lockThreads) {
			lockThreads.notifyAll();
		}

		// Wait for the threads to finish
		synchronized (lockScore) {
			while (threadCounter.get() < nbThreads) {
				try {
					lockScore.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		for (int i = 0; i < deadPosts.size(); i++) {
			deletePost(deadPosts.get(i));
		}
	}

	private void setThreadsDate(DateTime date) {
		calculScore0.setDate(date);
		calculScore1.setDate(date);
		calculScore2.setDate(date);
		calculScore3.setDate(date);

		calculScore4.setDate(date);
		calculScore5.setDate(date);
		calculScore6.setDate(date);
		calculScore7.setDate(date);
	}

	private void setThreadsRun(int i) {
		threadRun0.set(i);
		threadRun1.set(i);
		threadRun2.set(i);
		threadRun3.set(i);

		threadRun4.set(i);
		threadRun5.set(i);
		threadRun6.set(i);
		threadRun7.set(i);
	}

	public void stopTreads() {
		setThreadsRun(-1);

		synchronized (lockThreads) {
			lockThreads.notifyAll();
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
		// postsCommentsScores.remove(i);
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

	public static void Maximiser(int M1, int M2, boolean post, int size) {

		if ((postsCommentsIds.size() == size) && (premierpassage == 0)) {
			for (int i = 0; i < postsCommentsIds.size(); i++) {
				if (10 + postsCommentsIds.get(i).size() * 10 >= M2) {
					max.add(i);
				} else {
					min.add(i);

				}

			}
			premierpassage = 1;

		} else if (M2 > M1) {
			if (post == true) {
				for (int i = 0; i < max.size(); i++) {
					if (10 + postsCommentsIds.get(max.get(i)).size() * 10 < M2) {
						// if (this.getPostsScores().get(max.get(i)) < M2) {
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
					if (10 + postsCommentsIds.get(max.get(i)).size() * 10 + 10 < M2) {
						// if (this.getPostsScores().get(max.get(i)) + 10 < M2)
						// {
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