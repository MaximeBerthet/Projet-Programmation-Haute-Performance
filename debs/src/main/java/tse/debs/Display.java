package tse.debs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.*;

import org.joda.time.DateTime;

public class Display {

	static Scores scores;
	static Logger logger;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.FRANCE);
	static File file;
	static FileHandler fh;

	public Display(Scores scores, String fileName) {
		this.scores = scores;
		try {
			File directory = new File("../../TestFiles");
			if (!directory.exists()) {
				if (directory.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
			// This block configure the logger with handler and formatter
			file = new File("../../TestFiles/" + fileName + ".txt");

			fh = new FileHandler(file.getPath(), true);

			fh.setFormatter(new Formatter() {
				public String format(LogRecord record) {// if(record.getLevel()
														// == Level.INFO){
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

	public void addLine(int[] indexOfBest, DateTime date) {
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String line = sdf.format(date.toDate()).toString();
		int i = 0;
		while (i < 3) {
			if (indexOfBest[i] != -1) {
				// System.out.println("i = " + i);
				line = line.concat(("," + scores.getPostsIds().get(indexOfBest[i]).toString() + ","
						+ scores.getPostsAuthors().get(indexOfBest[i]).toString() + ","
						+ scores.getPostsScores().get(indexOfBest[i]).toString() + ","
						+ scores.getPostsCommentsAuthorsNb().get(indexOfBest[i])).toString());
			} else {
				line = line.concat(",-,-,-,-");
			}
			i++;
		}
		logger.info(line);
	}

	public static String lineDisplayTest(ArrayList<DateTime> dates, ArrayList<Integer> postsId,
			ArrayList<String> authors, ArrayList<Integer> postsScores, ArrayList<Integer> postsNbComments,
			int[] indexOfBest) {
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String line = sdf.format(dates.get(dates.size() - 1).toDate()).toString();

		int i = 0;
		while (i < 3) {
			if (indexOfBest[i] != -1) {
				// System.out.println("i = " + i);
				line = line.concat(("," + postsId.get(indexOfBest[i]).toString() + ","
						+ authors.get(indexOfBest[i]).toString() + "," + postsScores.get(indexOfBest[i]).toString()
						+ "," + postsNbComments.get(indexOfBest[i])).toString());
			} else {
				line = line.concat(",-,-,-,-");
			}
			i++;
		}
		logger.info(line);
		return line;
	}

	public void resetLogs() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(file);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
