package tse.debs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
public class Reader {


	private ArrayList<String[]> list = null;
	private boolean Post = false; // true for posts, false for comments

	FileReader filePosts;
	FileReader fileComments;
	BufferedReader brPosts;
	BufferedReader brComments;
	String[] elementPosts = new String[5];
	String[] elementComments = new String[7];

	public Reader(String path){
		String pathPosts=path.concat("posts.dat");
		String pathComments=path.concat("comments.dat");
		try {
			filePosts = new FileReader(new File(pathPosts));
			fileComments = new FileReader(new File(pathComments));
			brPosts= new BufferedReader(filePosts);
			brComments = new BufferedReader(fileComments);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String[] readLinePosts(){

		String tempPosts = "";
		try {
			tempPosts = brPosts.readLine();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (tempPosts != null && tempPosts!="" && !tempPosts.equals("") && !tempPosts.equals(null)) {
			elementPosts = tempPosts.split("[\\|]");			
		}
		return elementPosts;
	}

	public String[] readLineComments(){

		String tempComments = null;
		String[] tempElements = null;
		try {
			tempComments = brComments.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (tempComments != null && tempComments!="" && !tempComments.equals("") && !tempComments.equals(null)) {
			tempElements = tempComments.split("[\\|]");
		}
		for (int i = 0; i < tempElements.length; i++) {
			elementComments[i] = tempElements[i];
		}
		if (tempElements.length<elementComments.length){
			for (int i = tempElements.length; i < elementComments.length; i++) {
				elementComments[i] = "";
			}
		}
		return elementComments;

	}

	/*public Reader(String path) throws IOException {
		String type = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".")).toLowerCase();
		FileReader file = new FileReader(new File(path));
		BufferedReader br = new BufferedReader(file);
		String temp = "";
		list = new ArrayList<String[]>();

		if (type.equals("posts")) {
			setPost(true);
			// Define ArrayList
			temp = br.readLine();
			while (temp != null && temp!="" && !temp.equals("") && !temp.equals(null) ) {
				String[] elements = temp.split("[\\|]");
				list.add(elements);
				temp = br.readLine();
			}

		} else if (type.equals("comments")) {
			setPost(false);
			// Define ArrayList
			temp = br.readLine();
			while (temp != null && temp!="" && !temp.equals("") && !temp.equals(null) ) {
				String[] elements = temp.split("[\\|]");
				list.add(elements);
				temp = br.readLine();
			}
		} else {
			System.out.println("Wrong file");
		}
		br.close();
	}*/

	public boolean getPost() {
		return Post;
	}

	public void setPost(boolean Post) {
		this.Post = Post;
	}

	public ArrayList<String[]> getList() {
		return list;
	}

	public void setList(ArrayList<String[]> list) {
		this.list = list;
	}
	public String[] getElementComments() {
		return elementComments;
	}
	public String[] getElementPosts() {
		return elementPosts;
	}

	public void closePosts() {
		try {
			brPosts.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void closeComments() {
		try {
			brComments.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
