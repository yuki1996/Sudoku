package Sudoku;
/**
 * Type d'une cellule non-modifiable.
 * @inv
 * 	forall int i : ! canTakeValue(i)
 * 
 * @cons
 * $DESC$ Une cellule non-modifiable de valeur n.
 * $ARGS$ int n
 * $PRE$ n > 0
 * $POST$
 * 		getValue() == n
 */
public interface IUnmodifiableCell extends ICell {
	
}
