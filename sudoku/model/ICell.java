package sudoku.model;


/**
 * Type d'une cellule.
 * @inv
 *      0 <= getValue()
 *      getValue() != 0 <==> hasValue()
 *      forall int i : canTakeValue(i) <==> possibilities()[i - 1] && isModifiable()
 *      getCardinalPossibilities() > 0
 *      possibilities().length == getCardinalPossibilities()
 *      
 * @cons <pre>
 *     $DESC$ Une cellule modifiable sans valeur.
 *      
 *     $ARGS$ int cardinal
 *     
 *     $PRE$ cardinal > 0
 *      
 *     $POST$ 
 *     		getCardinalPossibilities() == cardinal
 *     		! hasValue()
 *      	isModifiable()
 *         	forall int i : possibilities()[i]
 *    </pre>
 *    
 * @cons <pre>
 *     $DESC$ Une cellule de valeur value.
 *     
 *     $ARGS$ int value, boolean modifiable, int cardinal
 *     
 *     $PRE$ value > 0 && cardinal > 0 && value <= cardinal
 *         
 *     $POST$
 *     	    getValue() == value
 *         	isModifiable() == modifiable
 *          forall int i : possibilities()[i]
 *          getCardinalPossibilities() == cardinal
 *    </pre>
 *    
 * @cons <pre>
 *     $DESC$ Une cellule modifiable sans valeur ayant un tableau de possibilité.
 *     
 *     $ARGS$ boolean[] possibilities 
 *     
 *     $PRE$ possibilities != null && possibilities.length > 0
 *         
 *     $POST$ 
 *          ! hasValue()
 *          isModifiable()
 *         	possibilities().equals(possibilities)
 *          getCardinalPossibilities() == possibilities.length
 *    </pre>
 */
interface ICell {
	
	//REQUETES
	
	/**
	 * Renvoie le nombre de possibilité.
	 */
	int getCardinalPossibilities();
	
	/**
	 * Donne la valeur actuel de la cellule.
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
	
	/**
	 * Renvoie le tableau des possibilités.
	 */
	boolean[] possibilities();
	
	//COMMANDES
	/**
	 * Change la valeur de la cellule.
	 * @pre
	 *      0 < n <= getCardinalPossibilities()
	 *      canTakeValue(n)
	 * @post
	 *      getValue() == n
	 */
	void setValue(int n);
	
	/**
	 * Met la valeur de la cellule à 0 si elle est modifiable.
	 * @pre
	 *      isModifiable()
	 * @post
	 *      getValue() == 0
	 */
	void removeValue();
	
	/**
	 * Ajoute n comme possibilite si la cellule ne la posséde pas déjà.
	 * @pre
	 *      0 < n <= getCardinalPossibilities()
	 *      isModifiable()
	 * @post
	 * 		possibilities()[n - 1]
	 *      canTakeValue(n);
	 */
	void addPossibility(int n);
	
	/**
	 * Supprime la possibilité n si la cellule la posséde.
	 * @pre
	 *      0 < n <= getCardinalPossibilities()
	 *      isModifiable()
	 * @post
	 * 		! possibilities()[n - 1]
	 *      ! canTakeValue(n);
	 */
	void removePossibility(int n);
}
