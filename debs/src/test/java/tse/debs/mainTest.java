package tse.debs;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Test;

public class mainTest {

	@Test
	public void test() throws IOException {
		String path = "D:/Utilisateur/Victor/Bureau/Projet Haute performance/Tests/Q1Case4/posts.dat";
		String path2 = "D:/Utilisateur/Victor/Bureau/Projet Haute performance/Tests/Q1Case4/comments.dat";
		
		Reader r=new Reader(path);
		Reader r2=new Reader(path2);
		
		ArrayList<String[]> a = r2.getList();
	
	System.out.println("size a "+a.size());
	String[] line = a.get(0);
		int i=0;
		while (i<line.length) {
			System.out.println("ibis="+i+" : " +line[i]);
			i++;
		}
		

		Scores sc=new Scores();
		sc.processFile(r.getList(),r.getPost());
		sc.processFile(r2.getList(),r2.getPost());

		//System.out.println("comment"+Scores.getCommentsIds().get(0));
		
	
		Tri tr= new Tri();
		
		int [] fin=tr.Trier(Scores.getPostsScores());
		
		//System.out.println("      "+sc.getPostsScores().get(fin[0])+"  "+sc.getPostsScores().get(fin[1])+"  "+sc.getPostsScores().get(fin[2]));
		Display disp=new Display(Scores.getPostsScores(),Scores.getPostsNbComments(),Scores.getPostsStartDates(), Scores.getPostsAuthors(),Scores.getPostsIds(), fin);
		
		
		//fail("Not yet implemented");
	}

}
