package tse.debs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;

import org.joda.time.DateTime;

public class Display {

	ArrayList<Integer> postsScores = new ArrayList<Integer>();
	ArrayList<Integer> postsnbComments = new ArrayList<Integer>();
	ArrayList<DateTime> postsStartDates = new ArrayList<DateTime>();
	ArrayList<String> postsAuthors = new ArrayList<String>();
	ArrayList<Integer> postsId = new ArrayList<Integer>();
	int[] indexOfBest;


	public Display(ArrayList<Integer> postsScores, ArrayList<Integer> postsnbComments,
			ArrayList<DateTime> postsStartDates, ArrayList<String> postsAuthors, ArrayList<Integer> postsId, int[] indexOfBest) {
		this.postsScores = postsScores;
		this.postsnbComments = postsnbComments;
		this.postsStartDates = postsStartDates;
		this.postsAuthors = postsAuthors;
		this.postsId = postsId;
		this.indexOfBest = indexOfBest;

		FileHandler fh;
		Logger logger = Logger.getLogger("Mylog");
		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler("../Mylog.txt");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();

			fh.setFormatter(formatter);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 0;
		
		String toDisplay = postsStartDates.get(postsStartDates.size()-1).toString();
		while (i<3) {
			if(indexOfBest[i] != -1){
				System.out.println("i = "+i);
				toDisplay=toDisplay.concat(("," + postsId.get(indexOfBest[i]).toString() + "," + postsAuthors.get(indexOfBest[i]).toString() + ","
						+ postsScores.get(indexOfBest[i]).toString() + "," + postsnbComments.get(indexOfBest[i])).toString());
				
			}else{
	
					toDisplay=toDisplay.concat(",-,-,-,-");
	
			}
			i++;
		}
	
		System.out.println("test    "+toDisplay);
		logger.info(toDisplay);

	}

	public void Logger() {

	}
}
