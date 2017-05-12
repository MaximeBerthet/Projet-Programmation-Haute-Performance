package tse.debs;

import java.util.ArrayList;

public class Tri {
	private int[] L = new int[3];
	private int t = 0;

	public Tri() {
		super();

	}

	int[] Trier(ArrayList<Integer> tab) {
		for (int j = 0; j < L.length; j++) {
			L[j] = -1;
		}
		int M1 = 0;
		int M2 = 0;
		int M3 = 0;
		for (int i = 0; i < tab.size(); i++) {
			if (tab.get(i) >= M3) {
				if (tab.get(i) >= M2) {
					if (tab.get(i) >= M1) {
						M3 = M2;
						L[2] = L[1];
						M2 = M1;
						L[1] = L[0];
						M1 = tab.get(i);
						L[0] = i;
					} else {
						M3 = M2;
						L[2] = L[1];
						M2 = tab.get(i);
						L[1] = i;

					}
				} else {
					M3 = tab.get(i);
					L[2] = i;

				}
			}
		}
		return L;

	}

	public void Maximiser(Scores scores, int M1, int M2, boolean post, boolean r) {
		ArrayList<ArrayList<Long>> postsCommentsIds = scores.getPostsCommentsIds();
		ArrayList<Integer> max = scores.getMax();
		ArrayList<Integer> min = scores.getMin();

		if ((postsCommentsIds.size() == 5) && (t == 0) || r == true) {
			for (int i = 0; i < postsCommentsIds.size(); i++) {
				if (10 + postsCommentsIds.get(i).size() * 10 >= M2) {
					max.add(i);
				} else {
					min.add(i);
				}

			}
			t = 1;
			r = false;

		} else if (M2 > M1) {
			if (post == true) {
				for (int i = 0; i < max.size(); i++) {
					if (10 + postsCommentsIds.get(max.get(i)).size() * 10 < M2) {

						min.add(max.get(i));
						max.remove(i);
					}

				}
				if (M2 <= 10) {
					max.add(postsCommentsIds.size() - 1);
				} else {
					min.add(postsCommentsIds.size() - 1);
				}
			}
			if (post == false) {

				for (int i = 0; i < max.size(); i++) {
					if (10 + postsCommentsIds.get(max.get(i)).size() * 10 + 10 < M2) {
						min.add(max.get(i));
						max.remove(i);
					}

				}

			}

		} else if (M2 <= M1) {
			if (post == true) {
				if (min.size() > 0) {
					for (int i = 0; i < min.size(); i++) {
						if (10 + postsCommentsIds.get(min.get(i)).size() * 10 >= M2) {

							max.add(min.get(i));
							min.remove(i);
						}

					}
				}
				if (M2 < 11) {
					max.add(postsCommentsIds.size() - 1);
				} else {
					min.add(postsCommentsIds.size() - 1);
				}
			}
			if (post == false) {
				if (min.size() > 0) {

					for (int i = 0; i < min.size(); i++) {
						if (10 + postsCommentsIds.get(min.get(i)).size() * 10 + 10 >= M2) {
							max.add(min.get(i));
							min.remove(i);
						}

					}

				}
			}
		}

		scores.setMax(max);
		scores.setMin(min);
	}
}
