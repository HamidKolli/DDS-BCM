package fr.ddspstl.tools;

import java.util.List;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe QuickSort permettant de trier une liste
 */
public class QuickSort {
	/**
	 * Constructeur
	 * 
	 * @param samples : liste des données à trier
	 */
	public  static <T> void quickSort(List<T> samples) {
		
		quickSort(samples, 0, samples.size() - 1);
	}

	/**
	 * Constructeur
	 * 
	 * @param samples : liste des données à trier
	 * @param low : l'indice du premier element de la sous liste
	 * @param high : l'indice du dernier element de la sous liste
	 */
	private static <T> void quickSort(List<T> samples, int low, int high) {
		if (low < high) {
			int pivotIndex = partition(samples, low, high);

			quickSort(samples, low, pivotIndex - 1);
			quickSort(samples, pivotIndex + 1, high);
		}
	}

	/**
	 * Methode partition
	 * 
	 * @param samples : liste des données
	 * @param low : l'indice du premier element de la sous liste
	 * @param high : l'indice du dernier element de la sous liste
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> int partition(List<T> samples, int low, int high) {
		T pivot = samples.get(high);
		int i = low - 1;

		for (int j = low; j < high; j++) {
			if (((Comparable<T>)samples.get(j)).compareTo(pivot) < 0 ) {
				i++;
				swap(samples, i, j);
			}
		}

		swap(samples, i + 1, high);

		return i + 1;
	}

	/**
	 * Methode swap : inverse deux elements de la liste
	 * 
	 * @param samples : liste des données
	 * @param i : indice du 1er element
	 * @param j : indice du 2e element
	 */
	private static <T> void swap(List<T> samples, int i, int j) {
		T temp = samples.get(i);
		samples.set(i, samples.get(j));
		samples.set(j, temp);
	}


}
