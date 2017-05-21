package tse.debs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class CalculScores implements Runnable {
	private Scores scores;
	private List<Integer> deadPosts;
	private DateTime date;

	private AtomicInteger threadCounter;
	private Lock lockMain, lockThread;
	private int nThread, nbThreads;

	private AtomicInteger threadRun;
	// 0 : stanby, -1 : stop, 1 : calcul, 2 : calculMax, 3 : calculMin

	public CalculScores(Scores scores, List<Integer> deadPosts, AtomicInteger threadCounter, Lock lockMain,
			Lock lockThread, AtomicInteger threadRun, int nThread, int nbThreads) {
		super();
		this.scores = scores;
		this.deadPosts = deadPosts;
		this.threadCounter = threadCounter;
		this.lockMain = lockMain;
		this.lockThread = lockThread;
		this.threadRun = threadRun;
		this.nThread = nThread;
		this.nbThreads = nbThreads;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public void run() {
		while (threadRun.get() != -1) {
			// waiting Scores order to run
			synchronized (lockThread) {
				while (threadRun.get() == 0) {
					try {
						lockThread.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			int calculationOrder = threadRun.get();
			if (calculationOrder == -1) {
				break;
			}

			threadRun.set(0);

			if (calculationOrder == 1) {
				calcul();
			} else if (calculationOrder == 2) {
				calculMax();
			} else {
				// calculMin();
			}

			threadCounter.incrementAndGet();

			// Notify scores class
			synchronized (lockMain) {
				lockMain.notify();
			}
		}
	}

	private void calcul() {
		int size = scores.getPostsIds().size();
		int start = size * nThread / nbThreads;
		int end = size * (nThread + 1) / nbThreads;

		for (int i = start; i < end; i++) {
			if (date.isAfter(scores.getPostsDeathDates().get(i))) {
				synchronized (deadPosts) {
					deadPosts.add(i);
				}
			} else {
				int postScore = 10 + Days.daysBetween(date, scores.getPostsStartDates().get(i)).getDays(); // 0.6s
				if (postScore < 0) {
					postScore = 0;
				}

				int commentsScore = 0;
				int sizeComments = scores.getPostsCommentsIds().get(i).size();
				for (int j = 0; j < sizeComments; j++) { // 1.5s
					int score = 10
							+ Days.daysBetween(date, scores.getPostsCommentsStartDates().get(i).get(j)).getDays();
					if (score > 0) {
						commentsScore += score;
					}
				}
				scores.setPostsScores(i, postScore + commentsScore);
			}
		}
	}

	private void calculMax() {
		int size = scores.getMax().size();
		int start = size * nThread / nbThreads;
		int end = size * (nThread + 1) / nbThreads;

		for (int i = start; i < end; i++) {
			int postIndice = scores.getMax().get(i);
			if (date.isAfter(scores.getPostsDeathDates().get(postIndice))) {
				synchronized (deadPosts) {
					deadPosts.add(postIndice);
				}
			} else {
				int postScore = 10 + Days.daysBetween(date, scores.getPostsStartDates().get(postIndice)).getDays(); // 0.6s
				if (postScore < 0) {
					postScore = 0;
				}

				int commentsScore = 0;
				int sizeComments = scores.getPostsCommentsIds().get(postIndice).size();
				for (int j = 0; j < sizeComments; j++) { // 1.5s
					int score = 10 + Days.daysBetween(date, scores.getPostsCommentsStartDates().get(postIndice).get(j))
							.getDays();
					if (score > 0) {
						commentsScore += score;
					}
				}
				scores.setPostsScores(postIndice, postScore + commentsScore);
			}
		}
	}

	private void calculMin() {
		int size = scores.getMin().size();
		int start = size * nThread / nbThreads;
		int end = size * (nThread + 1) / nbThreads;

		for (int i = start; i < end; i++) {
			int postIndice = scores.getMin().get(i);
			if (date.isAfter(scores.getPostsDeathDates().get(postIndice))) {
				synchronized (deadPosts) {
					deadPosts.add(postIndice);
				}
			} else {
				scores.setPostsScores(postIndice, 0);
			}
		}

	}
}
