package sudoku.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import sudoku.model.history.History;
import sudoku.model.history.StdHistory;
import sudoku.model.history.cmd.AddCandidate;
import sudoku.model.history.cmd.AddValue;
import sudoku.model.history.cmd.Command;
import sudoku.model.history.cmd.RemoveCandidate;
import sudoku.model.history.cmd.RemoveValue;
import sudoku.util.Coord;
import sudoku.util.ICoord;
import util.Contract;

public class Sudoku implements ISudoku {

	//ATTRIBUTS
	public static final String SEPARATOR = " ";
	public static final int HISTORY_SIZE = 1024;
	
	private IGrid gridPlayer;
	private IGrid gridSoluce;
	
	private History<Command> history;

	//CONSTRUCTEUR
	public Sudoku(int width, int height)  {
		Contract.checkCondition(width > 0 && height > 0);
		gridPlayer = new Grid(width, height);
		gridSoluce = new Grid(width, height);
		history = new StdHistory<Command>(HISTORY_SIZE);
	}
	
	public Sudoku(File textFile) throws IOException {
		BufferedReader fr = new BufferedReader(new FileReader(textFile));
		try {
			String line = fr.readLine();
			String[] tokens = line.split(SEPARATOR);
			final int width = Integer.parseInt(tokens[0]);
			final int height = Integer.parseInt(tokens[1]);
			
			gridSoluce = new Grid(width, height);
			gridPlayer = new Grid(width, height);
			for (int k = 0; k < width * height; ++k) {
				line = fr.readLine();
				tokens = line.split(SEPARATOR);
				ICell[] gridPlayerLine = gridPlayer.cells()[k];
				for (int j = 0; j < width * height; ++j) {
					int value = Integer.parseInt(tokens[j]);
					ICell gridPlayerCell = gridPlayerLine[j];
					if (value == 0){
						gridPlayerCell.reset();
					} else {
						gridPlayerCell.setValue(value);
						gridPlayerCell.setModifiable(false);
					}
				}
			}
			for (int i = 0; i < gridPlayer.size(); i++) {
				for (int j = 0; j < gridPlayer.size(); j++) {
					if (gridPlayer.cells()[i][j].hasValue()) {
						updateEasyPossibilities(new Coord(j, i));
					}
				}
			}
			gridSoluce = (Grid) gridPlayer.clone();
		} finally {
			fr.close();
		}
	}

	//REQUETES
	public IGrid getGridPlayer() {
		return gridPlayer;
	}

	public IGrid getGridSoluce() {
		return gridSoluce;
	}
	
	public boolean isWin() {
		ICell[][] tabPlayer = getGridPlayer().cells();
		ICell[][] tabSoluce = getGridSoluce().cells();
		if (!getGridPlayer().isFull()) {
			return false;
		}
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
		return getGridSoluce().getCell(coord).isModifiable();
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
		for (int i = 0 ; i < getGridPlayer().numberCandidates(); i++) {
			for (int j = 0 ; j < getGridPlayer().numberCandidates(); j++) {
				if (set.contains(getGridPlayer().cells()[i][j]) && getGridPlayer().cells()[i][j].isModifiable()) {
					getGridPlayer().cells()[i][j].removeCandidate(n);
				}
			}
		} 
		
	}

	public void setValue(ICoord c, int n) {
		Contract.checkCondition(c != null
				&& isValidCoord(c) && n > 0
				&& 1 <= n  && n <= getGridPlayer().numberCandidates());
		history.add(new AddValue(gridPlayer, c, n));
	}

	public void removeValue(ICoord c) {
		Contract.checkCondition(c != null
				&& isValidCoord(c));
		history.add(new RemoveValue(gridPlayer, c));
	}

	public void addPossibility(ICoord c, int n) {
		Contract.checkCondition(c != null
				&& isValidCoord(c) && 1 <= n 
				&& n <= getGridPlayer().numberCandidates());
		history.add(new AddCandidate(gridPlayer, c, n));
	}

	public void removePossibility(ICoord c, int n) {
		Contract.checkCondition(c != null
				&& isValidCoord(c) && 1 <= n 
				&& n <= getGridPlayer().numberCandidates());
		history.add(new RemoveCandidate(gridPlayer, c, n));
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
		try {
			oos.writeObject(getGridPlayer());
			oos.writeObject(getGridSoluce());
		} finally {
			oos.close();
		}
	}

	public void load(File fichier) throws ClassNotFoundException, IOException {
		Contract.checkCondition(fichier != null);
		ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(fichier)) ;
		try {
			gridPlayer = (Grid) ois.readObject();
			gridSoluce = (Grid) ois.readObject();
		} finally {
			ois.close();
		}
	}
	
	//OUTILS
	
	/**
	 * fait les mise à jour simple pour la grille g
	 */
	private void updateEasyGrid(IGrid g) {
		assert g != null;
		for (int i = 0; i < g.size(); ++i) {
			for (int j = 0; j < g.size(); ++j) {
				Coord c = new Coord(i,j);
				if (g.getCell(c).hasValue()) {
					updateEasyPossibilities(g, c);
				}
			}
		}
	}
	
	private void updateEasyPossibilities(IGrid g, ICoord c) {
		assert c != null;
		assert isValidCoord(c) ;
		assert g.getCell(c).hasValue();
		int n = g.getCell(c).getValue();
		Set<ICell> set = g.getUnitCells(c);
		for (ICell cell : set) {
			if (cell.isModifiable()) {
				cell.removeCandidate(n);
			}
		}
	}
	
	/**
	 * premier algo de résolution
	 */
	
	private void singleCandidate(IGrid g, ICoord c) {
		/*
		 * a revoir
		 */
		assert c != null;
		assert g != null;
		assert g.isValidCoord(c);
		ICell src = g.getCell(c);
		assert src.isModifiable();
		Set<ICell> sector = g.getSector(c);
		sector.remove(src);
		int sectorCellsNb = sector.size();
		int i = 1;
		while (i <= src.getCardinalCandidates()) {
			if (src.canTakeValue(i)) {
				int n = sectorCellsNb;
				for (ICell cell : sector) {
					if (! cell.isModifiable() || ! cell.canTakeValue(i)) {
						--n;
					} else {
						break;
					}
				}
				if (n == 0) {
					src.setValue(i);
					return;
				}
				++i;
			}
		}
		
		sector = g.getRow(c);
		sector.remove(src);
		i = 1;
		while (i <= src.getCardinalCandidates()) {
			if (src.canTakeValue(i)) {
				int n = sectorCellsNb;
				for (ICell cell : sector) {
					if (! cell.isModifiable() || ! cell.canTakeValue(i)) {
						--n;
					}
				}
				if (n == 0) {
					src.setValue(i);
					return;
				}
				++i;
			}
		}
		
		sector = g.getCol(c);
		sector.remove(src);
		i = 1;
		while (i <= src.getCardinalCandidates()) {
			if (src.canTakeValue(i)) {
				int n = sectorCellsNb;
				for (ICell cell : sector) {
					if (! cell.isModifiable() || ! cell.canTakeValue(i)) {
						--n;
					}
				}
				if (n == 0) {
					src.setValue(i);
					return;
				}
				++i;
			}
		}
	}
	
	/**
	 * deuxieme algo de resolution
	 */
	
	private void oneCandidate(IGrid g, ICoord c) {
		/*
		 * a revoir
		 */
		assert c != null;
		assert g != null;
		assert g.isValidCoord(c);
		ICell src = g.getCell(c);
		assert src.isModifiable();
		
		int v = 0;
		for (int i = 0; i < src.candidates().length; ++i) {
			if (src.candidates()[i]) {
				if (v == 0) {
					v = i + 1;
				} else {
					return;
				}
			}
		}
		src.setValue(v);
	}
	
	/**
	 * troisieme algo de resolution
	 */
	
	private void siblings(IGrid g, ICoord c) {
		
	}
}
