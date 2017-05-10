package tse.debs;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class DisplayTest {
	static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
			.withLocale(Locale.ROOT).withChronology(ISOChronology.getInstanceUTC());
		
	
	@Test
	public void test() {
		Scores s=new Scores();
		Display d=new Display(s);
		ArrayList<DateTime> dates = new ArrayList<DateTime>(Arrays.asList(formatter.parseDateTime("2010-02-01T05:12:32.921+0000"),formatter.parseDateTime("2010-02-02T19:53:43.226+0000"),formatter.parseDateTime("2010-02-09T04:05:10.421+0000")));
		ArrayList<Integer> postsId=new ArrayList<Integer>(Arrays.asList(1039993, 299101, 529360));
		ArrayList<String> authors= new ArrayList<String>(Arrays.asList("Lei Liu","Michael Wang","Wei Zhu"));
		ArrayList<Integer> postsScores=new ArrayList<Integer>(Arrays.asList(10,9,8));
		ArrayList<Integer> postsNbComments=new ArrayList<Integer>(Arrays.asList(0,0,0));
		int[] indexOfBest={0,-1,-1};

		String expected="2010-02-09T04:05:10.421+0000,1039993,Lei Liu,10,0,-,-,-,-,-,-,-,-";
		String line=Display.lineDisplayTest(dates, postsId, authors, postsScores, postsNbComments,indexOfBest);
		assertTrue(line.equals(expected));
		
	
		
		indexOfBest[0]=1;
		indexOfBest[1]=0;
		expected= "2010-02-09T04:05:10.421+0000,299101,Michael Wang,9,0,1039993,Lei Liu,10,0,-,-,-,-";
		line=Display.lineDisplayTest(dates, postsId, authors, postsScores, postsNbComments,indexOfBest);
		assertTrue(line.equals(expected));
		
		indexOfBest[0]=2;
		indexOfBest[1]=1;
		indexOfBest[2]=0;
		expected="2010-02-09T04:05:10.421+0000,529360,Wei Zhu,8,0,299101,Michael Wang,9,0,1039993,Lei Liu,10,0";
		line=Display.lineDisplayTest(dates, postsId, authors, postsScores, postsNbComments,indexOfBest);
		assertTrue(line.equals(expected));
	}

}
