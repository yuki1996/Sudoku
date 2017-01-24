package sudoku.model;
/**
 * Type d'une cellule.
 * @inv
 *      0 <= getValue()
 *      getValue() != 0 <==> hasValue()
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
	boolean hasValue();
	
	/**
	 * Renvoie vrai si la case est modifiable.
	 */
	boolean isModifiable();
	
	//COMMANDES
	/**
	 * Change la valeur de la cellule.
	 * @pre
	 *      0 <= n
	 *      canTakeValue(n)
	 *      isModifiable()
	 * @post
	 * 	    getValue() == n
	 */
	void setValue(int n);
	
	/**
	 * Met la valeur de la cellule à 0 si elle est modifiable.
	 * @pre
	 * 	    isModifiable()
	 * @post
	 * 	    getValue() == 0
	 */
	void removeValue();
	
	/**
	 * Ajoute n comme possibilite si la cellule ne la posséde pas déjà.
	 * @pre
	 * 	    0 < n
	 *      isModifiable()
	 * @post
	 * 	    canTakeValue(n);
	 */
	void addPossibility(int n);
	
	/**
	 * Supprime la possibilité n si la cellule la posséde.
	 * @pre
	 * 	    0 < n
	 *      isModifiable()
	 * @post
	 * 	    !canTakeValue(n);
	 */
	void removePossibility(int n);
}
