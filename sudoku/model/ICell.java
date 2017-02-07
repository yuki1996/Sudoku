package sudoku.model;

import java.io.Serializable;

/**
 * Type d'une cellule.
 * @inv
 *      0 <= getValue()
 *      getValue() != 0 <==> hasValue()
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
 * @cons <pre>
 *     $DESC$ Une cellule équivalente à src
 *     
 *     $ARGS$ ICell src 
 *     
 *     $PRE$ src != null
 *         
 *     $POST$ 
 *          getValue() == src.getValue()
 *          isModifiable() <==> src.isModifiable()
 *         	possibilities().equals(src.possibilities())
 *    </pre>
 */
interface ICell extends Serializable, Cloneable  {
	
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
	
	/**
	 * Renvoie un clone de la cellule.
	 */
	Object clone();
	
	//COMMANDES
	/**
	 * Change la valeur de la cellule.
	 * @pre
	 *      0 < n <= getCardinalPossibilities()
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
	 */
	void addPossibility(int n);
	
	/**
	 * Supprime la possibilité n si la cellule la posséde.
	 * @pre
	 *      0 < n <= getCardinalPossibilities()
	 *      isModifiable()
	 * @post
	 * 		! possibilities()[n - 1]
	 */
	void removePossibility(int n);
	
	/**
	 * Modifie la modifiabilité de la cellule
	 * @post
	 * 		isModifiable() == bool
	 */
	void setModifiable(boolean bool);
	
	/**
	 * Met la cellule comme étant modifiable avec comme valeur nulle et posséde toute possibilité
	 * @post
	 *     	    getValue() == 0
	 *         	isModifiable() == true
	 *         	forall int i : possibilities()[i]
	 */
	void reset();
}
