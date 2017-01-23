package sudoku.util;

/**
 * Type des coordonnées (non mutables).
 * 
 * 
 * @inv <pre>
 *     equals(c) <==>
 *         c != null
 *         && c.getClass() == getClass()
 *         && c.getCol() == getCol()
 *         && c.getRow() == getRow()
 *     
 * @cons
 * $POST$  
 * 		getCol() == c
 * 		getRow() == r
 * </pre>
 */
public interface ICoord {

    // REQUETES
    
    /**
     * Indique si cette coordonnée est similaire à k.
     */
    boolean equals(Object k);
    
    /**
     * Indice de la colonne.
     */
    int getCol();
    
    /**
     * Indice de la ligne.
     */
    int getRow();
    
    /**
     * Une représentation textuelle de cette coordonnée.
     */
    String toString();
}
