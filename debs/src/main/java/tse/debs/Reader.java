package tse.debs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Reader {

	private ArrayList<String[]> list = null;
	private int PostOrComment = 0; // 1 for posts, 2 for comments

	public Reader(String path) throws IOException {
		String type = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".")).toLowerCase();

		if (type.equals("posts")) {
			setPostOrComment(1);
			FileReader file = new FileReader(new File(path));
			BufferedReader br = new BufferedReader(file);
			String temp = null;

			// Define ArrayList
			list = new ArrayList<String[]>();
			while ((temp = br.readLine()) != null) {
				String[] elements = temp.split("[\\|]");
				list.add(elements);
			}
			br.close();
		} else if (type.equals("comments")) {
			setPostOrComment(2);
			FileReader file = new FileReader(new File(path));
			BufferedReader br = new BufferedReader(file);
			String temp = null;

			// Define ArrayList
			list = new ArrayList<String[]>();
			while ((temp = br.readLine()) != null) {
				String[] elements = new String[7];
				String[] tempElements = temp.split("[\\|]");

				for (int i = 0; i < tempElements.length; i++) {
					elements[i] = tempElements[i];
				}
				list.add(elements);
			}
			br.close();
		} else {
			System.out.println("Wrong file");
		}

	}

	public int getPostOrComment() {
		return PostOrComment;
	}

	public void setPostOrComment(int postOrComment) {
		PostOrComment = postOrComment;
	}

	public ArrayList<String[]> getList() {
		return list;
	}

	public void setList(ArrayList<String[]> list) {
		this.list = list;
	}

}
