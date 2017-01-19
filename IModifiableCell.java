package appli_info;

/**
 * @inv
 * 0 <= getValue <= getPossibilities().size
 * pour tout i de 0 a getPossibilities().size - 1:
 * 		canTakeValue(i) <==> getPossibilities()[i]
 * getValue != 0 <==> haveValue()
 * 
 * @cons
 * $DESC$ Une cellule modifiable qui peut prendre toutes les valeurs
 * 	entre 0 et n.
 * $ARGS$ int n
 * $POST$
 * 		getValue() == 0
 *      getPossibilities().size == n
 * 		pour tout i de 0 a n - 1:
 * 			getPossibilities()[i]
 *
 */
public interface IModifiableCell extends ICell {
	// REQUETES
	/**
	 * Renvoie un tableau de booleens qui specifie les valeurs que peut
	 * prendre la cellule.
	 */
	boolean[] getPossibilities();
	
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
	
	// COMMANDES
	/**
	 * Change la valeur de la cellule.
	 * @pre
	 * 	0 < n <= getPossibilities().size
	 * 	canTakeValue(n)
	 * @post
	 * 	getValue() == n
	 */
	void takeValue(int n);
	
	/**
	 * Supprime la valeur de la cellule.
	 * @post
	 * 	!haveValue()
	 */
	void removeValue();
	
	/**
	 * Ajoute n comme possibilite si la cellule ne la possede pas deja.
	 * @pre
	 * 	0 < n <= getPossibilities().size
	 * @post
	 * 	canTakeValue(n);
	 */
	void addPossibility(int n);
	
	/**
	 * Supprime la possibilite n si la cellule la possede.
	 * @pre
	 * 	0 < n <= getPossibilities().size
	 * @post
	 * 	!canTakeValue(n);
	 */
	void removePossibility(int n);
	
	/**
	 * Met a vrai toutes les possibilites.
	 * @post
	 * 	pour tout i de 0 a getPossibilities().size - 1:
	 * 		canTakeValue(n)
	 */
	void resetPossibilities();
}
