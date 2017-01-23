package Sudoku;

/**
 * @cons
 * $DESC$ Une cellule modifiable.
 * $POST$
 * 		getValue() == 0
 * 		forall int i : ! canTakeValue(i)
 * 
 * @cons
 * $DESC$ Une cellule modifiable de valeur n.
 * $ARGS$ int n
 * $PRE$ n > 0
 * $POST$
 * 		getValue() == n
 * 		forall int i: ! canTakeValue(i)
 * 
 * @cons
 * $DESC$ Une cellule modifiable qui peut prendre comme valeur 
 * 		les elements de l.
 * $ARGS$ List<Integer> l
 * $PRE$ forall Integer i in l : i > 0
 * $POST$
 * 		getValue() == 0
 * 		forall Integer i in l : ! canTakeValue(i)
 */
public interface IModifiableCell extends ICell {
	// REQUETES
	
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
}
