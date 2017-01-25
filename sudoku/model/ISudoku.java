package sudoku.model;

import java.util.List;

import sudoku.util.ICoord;

interface ISudoku {
	
	//REQUÊTES
	/**
	 * Retourne la grille de Sudoku.
	 */
	IGrid getGrid();
	
	/**
	 * Retourne si on a gagné.
	 */
	boolean isWin();
	
	/**
	 * Retourne si une cellule à coordonnée coord est modifiable.
	 * 
	 * @pre <pre>
	 * 		coord != null
	 * 		0 <= coord.col() < getGrid().numberPossibility()
	 * 		0 <= coord.row() < getGrid().numberPossibility()
	 * </pre>
	 */
	boolean isModifiableCell(ICoord coord);
	
	/**
	 * Retourne la liste des conflits par coordonnées.
	 */
	List<ICoord> check(); 
	
	//COMMANDES
	/**
	 * Mise à jour possibilités par rapport ajout de valeur.
	 */
	void updateEasyPossibilities();
	
	/**
	 * Ajout la valeur n dans la cellule de coordonnée c
	 * @pre : <pre>
	 * 		c != null
	 * 		0 <= c.getCol() < getGrid().size()
	 * 		0 <= c.getRow() < getGrid().size()
	 * 		1 <= n <= getGrid().numberPossibility()
	 * </pre>
	 * @post <pre>
	 * 		getGrid().getCell(c).value() == n
	 * </pre>
	 */
	void setValue(ICoord c, int n);
	
	/**
	 * Supprime la valeur de la cellule de coordonnée c.
	 * @pre : <pre>
	 * 		c != null
	 * 		0 <= c.getCol() < getGrid().getGridsize()
	 * 		0 <= c.getRow() < getGrid().size()
	 * </pre>
	 * @post <pre>
	 * 		getGrid().getCell(c).value() == 0
	 * </pre>
	 */
	void removeValue(ICoord c);
	
	/**
	 * Ajoute la valeur n dans les possibilités la cellule de coordonnée c.
	 * @pre : <pre>
	 * 		c != null
	 * 		0 <= c.getCol() < getGrid().size()
	 * 		0 <= c.getRow() < getGrid().size()
	 * 		1 <= n <= getGrid().numberPossibility()
	 * </pre>
	 * @post <pre>
	 * 		getGrid().getCell(c).possibility[n]
	 * </pre>
	 */
	void addPossibility(ICoord c, int n);
	
	/**
	 * Supprime la valeur n dans les possibilités la cellule de c.
	 * @pre : <pre>
	 * 		c != null
	 * 		0 <= c.getCol() < getGrid().size()
	 * 		0 <= c.getRow() < getGrid().size()
	 * 		1 <= n <= getGrid().numberPossibility()
	 * </pre>
	 * @post <pre>
	 * 		! getGrid().getCell(c).possibility[n]
	 * </pre>
	 */
	void removePossibility(ICoord c, int n);
	
	/**
	 * Apporte un indice.
	 */
	void help();
	
	/**
	 * Résous la partie.
	 */
	void finish();
	
	/**
	 * Résous pas à pas la grille
	 */
	void Resolve();
	
	/**
	 * Enregistre la grille.
	 */
	void save();
	
	/**
	 * Charge une grille.
	 */
	void load();
	
	
	/**
	 * Réinitialise la grille.
	 */
	void reset();
}
