package sudoku.model;

import java.util.List;

import sudoku.util.ICoord;

interface ISudoku {
	//REQUETES
	IGrid getGrid();
	boolean isWin();
	boolean isModifiableCell();
	List<ICoord> check(); //probleme
	
	//COMMANDES
	void updateEasyPossibilities();
	void setValue(ICoord c, int n);
	void removeValue(ICoord c);
	void addPossibility(ICoord c, int n);
	void removePossibility(ICoord c, int n);
	void help();
	void finish();
	void save();
	void load();
}
