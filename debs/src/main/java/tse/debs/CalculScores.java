package tse.debs;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CalculScores implements Runnable {
	Scores scores;
	int start, end;
	ArrayList<Integer> deadPosts = new ArrayList<Integer>();
	DateTime date;
	AtomicInteger threadCounter;

	public CalculScores(Scores scores, int start, int end, DateTime date, ArrayList<Integer> deadPosts,
			AtomicInteger threadCounter) {
		super();
		this.scores = scores;
		this.start = start;
		this.end = end;
		this.date = date;
		this.deadPosts = deadPosts;
		this.threadCounter = threadCounter;
	}

	public void run() {
		for (int i = start; i < end; i++) {
			if (date.isAfter(scores.getPostsDeathDates().get(i))) {
				deadPosts.add(i);
			} else {
				int postScore = scores.getPostsScores().get(i);
				
				if (date.isBefore(scores.getPostsStartDates().get(i).plusDays(10))) {
					postScore = 10 + Days.daysBetween(date, scores.getPostsStartDates().get(i)).getDays();
				} else {
					postScore = 0;
				}

				ArrayList<Integer> commentsScores = new ArrayList<Integer>();
				for (int j = 0; j < scores.getPostsCommentsIds().get(i).size(); j++) {
					if (date.isBefore(scores.getPostsCommentsStartDates().get(i).get(j).plusDays(10))) {
						commentsScores.add(10
								+ Days.daysBetween(date, scores.getPostsCommentsStartDates().get(i).get(j)).getDays());
					}
				}
				// scores.setPostsCommentsScores(i, commentsScores);
				
				for (int j = 0; j < commentsScores.size(); j++) {
					postScore = postScore + commentsScores.get(j);
				}
				scores.getPostsScores().set(i, postScore);
			}
		}
		threadCounter.incrementAndGet();
	}
}
