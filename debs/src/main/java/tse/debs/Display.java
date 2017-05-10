package tse.debs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.*;

import org.joda.time.DateTime;

public class Display {

	static Scores scores;
	static Logger logger;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.FRANCE);

	public Display(Scores scores) {
		this.scores = scores;

		FileHandler fh;
		try {
			// This block configure the logger with handler and formatter
			fh = new FileHandler("../MyLog.txt",true);
			fh.setFormatter(new Formatter() {
				public String format(LogRecord record) {//if(record.getLevel() == Level.INFO){
					return record.getMessage() + "\r\n";
				}
			});
			logger = Logger.getLogger("Mylog");
			logger.setUseParentHandlers(false);
			logger.addHandler(fh);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addLine(int[] indexOfBest) {
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));    
		String line = sdf.format((scores.getPostsStartDates().get(scores.getPostsStartDates().size() - 1)).toDate()).toString();
		int i = 0;
		while (i < 3) {
			if (indexOfBest[i] != -1) {
				//System.out.println("i = " + i);
				line = line.concat(("," + scores.getPostsIds().get(indexOfBest[i]).toString() + ","
						+ scores.getPostsAuthors().get(indexOfBest[i]).toString() + ","
						+ scores.getPostsScores().get(indexOfBest[i]).toString() + ","
						+ scores.getPostsNbComments().get(indexOfBest[i])).toString());
			} else {
				line = line.concat(",-,-,-,-");
			}
			i++;
		}
		logger.info(line);
	}


	public static String lineDisplayTest(ArrayList<DateTime> dates,ArrayList<Integer> postsId,ArrayList<String> authors,ArrayList<Integer> postsScores,ArrayList<Integer> postsNbComments,int[] indexOfBest){
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));    
		String line = sdf.format(dates.get(dates.size() - 1).toDate()).toString();

		int i = 0;
		while (i < 3) {
			if (indexOfBest[i] != -1) {
				//System.out.println("i = " + i);
				line = line.concat(("," + postsId.get(indexOfBest[i]).toString() + ","
						+ authors.get(indexOfBest[i]).toString() + ","
						+ postsScores.get(indexOfBest[i]).toString() + ","
						+ postsNbComments.get(indexOfBest[i])).toString());

			} else {

				line = line.concat(",-,-,-,-");

			}
			i++;
		}
		logger.info(line);
		return line;
	}
}
