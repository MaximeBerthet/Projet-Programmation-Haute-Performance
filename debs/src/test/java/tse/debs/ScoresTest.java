package tse.debs;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Test;

public class ScoresTest {

	Scores scores = new Scores();

	/*
	 * @Test public void testScores() { fail("Not yet implemented"); }
	 */

	@Test
	public void Q1Basic2() {
		ArrayList<String[]> postFile = new ArrayList<String[]>();
		String[] postLine1 = { "2010-02-01T05:12:32.921+0000", "1039993", "3981", "", "Lei Liu" };
		String[] postLine2 = { "2010-02-02T19:53:43.226+0000", "299101", "4661", "photo299101.jpg", "Michael Wang" };
		String[] postLine3 = { "2010-02-09T04:05:10.421+0000", "529360", "2608", "", "Wei Zhu" };
		postFile.add(postLine1);
		postFile.add(postLine2);
		postFile.add(postLine3);

		ArrayList<Integer> ids = new ArrayList<Integer>();
		int id1 = 1039993;
		int id2 = 299101;
		int id3 = 529360;
		ids.add(id1);
		ids.add(id2);
		ids.add(id3);
		
		ArrayList<Integer> postsScores = new ArrayList<Integer>();
		postsScores.add(3);
		postsScores.add(4);
		postsScores.add(10);

		scores.processFile(postFile, true);
		
		//System.out.print(scores.getPostsStartDates());
		//System.out.print(scores.getPostsScores());
		assertTrue("scores", postsScores.equals(scores.getPostsScores()));
		assertTrue("postsIds match", ids.equals(scores.getPostsIds()));

		ArrayList<String[]> commentFile = new ArrayList<String[]>();
		String[] commentLine1 = { "2010-02-10T04:05:20.777+0000", "529590", "2886", "LOL", "Baoping Wu", "",
				"1039993" };
		commentFile.add(commentLine1);
		scores.processFile(commentFile, false);
		
		assertTrue("add multiple files", ids.equals(scores.getPostsIds()));
		
		ArrayList<Integer> commentFileId = new ArrayList<Integer>();
		commentFileId.add(529590);
		assertTrue("commentsIds match",commentFileId.equals(scores.getCommentsIds()));
		
		ArrayList<Integer> commentFileLinkedId = new ArrayList<Integer>();
		commentFileLinkedId.add(1039993);
		assertTrue("commentsIdsLinked match",commentFileLinkedId.equals(scores.getCommentsLinkedIds()));
		
		postsScores = new ArrayList<Integer>();
		postsScores.add(2);
		postsScores.add(3);
		postsScores.add(9);

		assertTrue("scores", postsScores.equals(scores.getPostsScores()));
	}
}
