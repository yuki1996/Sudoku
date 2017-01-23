package Sudoku;
/**
 * Type d'une cellule.
 * @inv
 * 	0 <= getValue()
 * 	getValue() != 0 <==> haveValue()
 */
interface ICell {
	//REQUETES
	/**
	 * Donne la valeur actuel de la cellule.
	 * 
	 */
	int getValue();
	
	/**
	 * Renvoie vrai si la cellule peut prendre la valeur n.
	 * Renvoie faux sinon.
	 */
	boolean canTakeValue(int n);
	
	/**
	 * Renvoie vrai si la cellule a une valeur (differente de 0).
	 * Renvoie faux sinon.
	 */
	boolean haveValue();
	
	//COMMANDES
	/**
	 * Change la valeur de la cellule.
	 * @pre
	 * 	0 <= n
	 * 	canTakeValue(n)
	 * @post
	 * 	getValue() == n
	 */
	void setValue(int n);
	
	/**
	 * Ajoute n comme possibilite si la cellule ne la possede pas deja.
	 * @pre
	 * 	0 < n
	 * @post
	 * 	canTakeValue(n);
	 */
	void addPossibility(int n);
	
	/**
	 * Supprime la possibilite n si la cellule la possede.
	 * @pre
	 * 	0 < n
	 * @post
	 * 	!canTakeValue(n);
	 */
	void removePossibility(int n);
}
