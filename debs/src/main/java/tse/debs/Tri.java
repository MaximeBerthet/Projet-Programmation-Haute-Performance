package tse.debs;

import java.util.ArrayList;

public class Tri {
	int[] L = new int[3];

	public Tri() {
		super();

	}

	public int[] Trier(ArrayList<Integer> tab) {
		for (int j = 0; j < L.length; j++) {
			L[j] = -1;
		}
		int M1 = 0;
		int M2 = 0;
		int M3 = 0;
		for (int i = 0; i < tab.size(); i++) {
			if (tab.get(i) > M3) {
				if (tab.get(i) > M2) {
					if (tab.get(i) > M1) {
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

	public void Maximiser(ArrayList<Integer> tab, int M1, int M2, ArrayList<Integer> max, ArrayList<Integer> min,
			boolean post) {
		if (tab.size() == 9) {
			for (int i = 0; i < tab.size(); i++) {
				if (tab.get(i) * 10 > M2) {
					max.add(max.size(), i);
				} else {
					min.add(min.size(), i);
				}

			}

		} else if (M2 > M1) {
			if (post == true) {
				for (int i = 0; i < max.size(); i++) {
					if (tab.get(max.get(i)) * 10 < M2) {

						min.add(min.size(), max.get(i));
						max.remove(i);
					}

				}
				if (M2 <= 10) {
					max.add(max.size(), tab.size() - 1);
				} else {
					min.add(min.size(), tab.size() - 1);
				}
			}
			if (post == false) {

				for (int i = 0; i < max.size(); i++) {
					if (tab.get(max.get(i)) * 10 + 10 < M2) {
						min.add(min.size(), max.get(i));
						max.remove(i);
					}

				}

			}

		} else if (M2 <= M1) {
			if (post == true) {
				for (int i = 0; i < min.size(); i++) {
					if (tab.get(min.get(i)) * 10 >= M2) {

						max.add(max.size(), min.get(i));
						min.remove(i);
					}

				}
				if (M2 <= 10) {
					max.add(max.size(), tab.size() - 1);
				} else {
					min.add(min.size(), tab.size() - 1);
				}
			}
			if (post == false) {

				for (int i = 0; i < min.size(); i++) {
					if (tab.get(min.get(i)) * 10 + 10 >= M2) {
						max.add(max.size(), min.get(i));
						min.remove(i);
					}

				}

			}
		}

	}
}
