package tse.debs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Reader {

	private ArrayList<String[]> list = null;
	private boolean Post = false; // true for posts, false for comments

	public Reader(String path) throws IOException {
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
	}

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

}
