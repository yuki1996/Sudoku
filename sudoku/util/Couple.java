package sudoku.util;

public class Couple<E, V> {
	//ATTRIBUTS
	private E first;
	private V second;
	
	//CONSTRUCTEURS
	public Couple(E firstElement, V secondElement) {
		first = firstElement;
		second = secondElement;
	}
	
	public E getFirst() {
		return first;
	}
	
	public V getSecond() {
		return second;
	}
}
