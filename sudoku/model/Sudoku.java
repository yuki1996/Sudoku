package sudoku.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public class Sudoku implements ISudoku {

	//ATTRIBUTS
	private IGrid gridPlayer;
	private IGrid gridSoluce;

	//CONSTRUCTEUR
	public Sudoku(int width, int height)  {
		Contract.checkCondition(width > 0 && height > 0);
		gridPlayer = new Grid(width, height);
		gridSoluce = new Grid(width, height);
	}

	//REQUETES
	public IGrid getGridPlayer() {
		return gridPlayer;
	}

	public IGrid getGridSoluce() {
		return gridSoluce;
	}
	
	public boolean isWin() {
		ICell[][] tabPlayer  = getGridPlayer().cells();
		ICell[][] tabSoluce  = getGridSoluce().cells();
		for (int i = 0 ; i < tabPlayer.length; i++) {
			for (int j = 0 ; j < tabPlayer[i].length; j++) {
				if (tabPlayer[i][j].getValue() != tabSoluce[i][j].getValue()) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isModifiableCell(ICoord coord) {
		Contract.checkCondition(coord != null
				&& isValidCoord(coord));
		return getGridPlayer().getCell(coord).isModifiable();
	}

	public List<ICoord> check() {
		List<ICoord> list = new LinkedList<ICoord>();
		ICell[][] tabPlayer  = getGridPlayer().cells();
		ICell[][] tabSoluce  = getGridSoluce().cells();
		for (int i = 0 ; i < tabPlayer.length; i++) {
			for (int j = 0 ; j < tabPlayer[i].length; j++) {
				if (tabPlayer[i][j].getValue() != 0) {
					if (tabPlayer[i][j].getValue() != tabSoluce[i][j].getValue()) {
						list.add(new Coord(i,j));
					}
				}
			}
		}
		return list;
	}

	public boolean isValidCoord(ICoord coord) {
		Contract.checkCondition(coord != null);
		return 0 <= coord.getCol() && coord.getCol() < getGridSoluce().size()
				&& 0 <= coord.getRow() && coord.getRow() < getGridSoluce().size();
	}

	public String help() {
		// TODO ATTENDRE LES REGLES
		return null;
	}


	//COMMANDES
	public void updateEasyPossibilities(ICoord c) {
		Contract.checkCondition(c != null
				&& isValidCoord(c) 
				&& getGridPlayer().getCell(c).getValue() > 0);
		int n = getGridPlayer().getCell(c).getValue();
		Set<ICell> set = getGridPlayer().getUnitCells(c);
		for (ICell cell : set) {
			cell.removePossibility(n);
		}
	}

	public void setValue(ICoord c, int n) {
		Contract.checkCondition(c != null
				&& isValidCoord(c) && n > 0
				&& 1 <= n  && n <= getGridPlayer().numberPossibility());
		getGridPlayer().changeValue(c, n);
	}

	public void removeValue(ICoord c) {
		Contract.checkCondition(c != null
				&& isValidCoord(c));
		getGridPlayer().resetValue(c);
	}

	public void addPossibility(ICoord c, int n) {
		Contract.checkCondition(c != null
				&& isValidCoord(c) && 1 <= n 
				&& n <= getGridPlayer().numberPossibility());
		getGridPlayer().addPossibility(c, n);
	}

	public void removePossibility(ICoord c, int n) {
		Contract.checkCondition(c != null
				&& isValidCoord(c) && 1 <= n 
				&& n <= getGridPlayer().numberPossibility());
		getGridPlayer().removePossibility(c, n);
	}

	public void finish() {
		gridPlayer = getGridSoluce();
	}


	@Override
	public void resolve() {
		// TODO Auto-generated method stub
		
	}

	public void reset() {
		getGridPlayer().reset();
	}	
	
	public void save(String name) throws IOException {
		Contract.checkCondition(name != null && !name.equals(""));
		File fichier =  new File(name);
		ObjectOutputStream oos =  new ObjectOutputStream(new FileOutputStream(fichier)) ;
		oos.writeObject(getGridPlayer());
	}

	public void load(File fichier) throws ClassNotFoundException, IOException {
		Contract.checkCondition(fichier != null);
		ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(fichier)) ;
		gridPlayer = (Grid) ois.readObject();
	}
}
