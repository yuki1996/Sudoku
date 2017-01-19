package appli_info;
import java.util.Set;

/**
 * @inv <pre>
 *	    size() > 0
 * 		isFull() <==> forall ICell cell in cells() : cell.getValue() != 0
 * 		forall ICoord c : getCell(c) <==> cells[c.getCol()][c.getRow]
 * 		size() == cells().size()
 *      </pre>
 * @cons <pre>
 *     $DESC$ Une grille de taille n et avec les valeurs de départ
 *     
 *     $ARGS$ int n, Map map<ICoord, Integer>
 *     
 *     $PRE$ 
 *         n != null && map != null 
 *         
 *     $POST$ 
 *         size() == n
 * 		   forall coord : map, getCell(coord).value() == value
 * 								&& cells.contains(getCell(coord))
 *    </pre>
 * 
 * @cons <pre>
 *     $DESC$ Une grille de taille n
 *     
 *     $ARGS$ int n
 *     
 *     $PRE$ 
 *         n != null 
 *         
 *     $POST$ 
 *         size() == n
 * 		   cells() == null       CONTREDIT L'INVARIANT
 *    </pre>
 *    
 * @cons <pre>
 *     $DESC$ Une grille de taille standard 9 
 *         
 *     $POST$ 
 *         size() == DEFAULT_SIZE
 * 		   cells() == null
 *    </pre>
 */
public interface IGrid {
	
	int DEFAULT_SIZE = 9;
	
	//REQUÊTE
	/**
	 * Retourne la taille de la grille
	 */
	int size();
	
	/**
	 * Retourne le tableau de cellules
	 */
	ICell[][] cells();
	
	/**
	 * Retourne la cellule à coordonnée c.
	 * @pre: <pre>
	 * 		c != null
	 * </pre>
	 */
	ICell getCell(Coord c);
	
	/**
	 * Retourne si la grille est complète
	 */
	boolean isFull();
	
	/**
	 * Retourne l'ensemble des cellules de la ligne d'où se trouve la 
	 * coordonnée coord
	 * @pre : <pre>
	 * 		coord != null
	 * </pre>
	 */
	Set<ICell> getRow(ICoord coord);
	
	/**
	 * Retourne l'ensemble des cellules de la colonne d'où se trouve la 
	 * coordonnée coord
	 * @pre : <pre>
	 * 		coord != null
	 * </pre>
	 */
	Set<ICell> getCol(ICoord coord);
	
	/**
	 * Retourne l'ensemble des cellules de la région d'où se trouve la 
	 * coordonnée coord
	 * @pre : <pre>
	 * 		coord != null
	 * </pre>
	 */
	Set<ICell> getSector(ICoord coord);
	
	//COMMANDE
	
	/**
	 * Efface toutes les valeurs de la grille qui ne sont pas celle de
	 * départ
	 * @post
	 */
	void reset();
	
	/**
	 * Efface toutes les valeurs de la grille.
	 * @post :<pre>
	 * 		cells == null
	 * </pre>
	 */
	void clear();
	
	/**
	 * Change la valeur de la cellule de coord par value.
	 * @pre <pre>
	 * 		coord != null
	 * 		1 <= value <= size()
	 * </pre>
	 * @post <pre>
	 * 		getCell(coord).value == value
	 * </pre>
	 */
	void changeValue(ICoord coord, int value);
	
	/**
	 * Réinitialise la valeur de la cellule de coord par 0.
	 * @pre <pre>
	 * 		coord != null
	 * </pre>
	 * @post <pre>
	 * 		getCell(coord).value == 0
	 * </pre>
	 */
	void resetValue(ICoord coord);
	
	/**
	 * Ajoute la valeur value dans les possibilités la cellule de coord.
	 * @pre <pre>
	 * 		coord != null
	 * 		1 <= value <= size()
	 * </pre>
	 * @post <pre>
	 * 		getCell(coord).possibility[value]
	 * </pre>
	 */
	void addPossibility(ICoord coord, int value);
	
	/**
	 * Supprime la valeur value dans les possibilités la cellule de coord.
	 * @pre <pre>
	 * 		coord != null
	 * 		1 <= value <= size()
	 * </pre>
	 * @post <pre>
	 * 		!getCell(coord).possibility[value]
	 * </pre>
	 */
	void removePossibility(ICoord coord, int value);	
	
	/**
	 * Change le tableau de cellules par tabCells.
	 * @pre <pre>
	 * 		tabCells != null
	 *		forall  cell : tabCells, cell != null
	 *		tabCells.size() == size()
	 * </pre>
	 * @post <pre>
	 * 		cells() == tabCells
	 * </pre>
	 */
	void changeCells(ICell[][] tabCells);
}
