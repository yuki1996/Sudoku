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

import sudoku.model.heuristic.RuleManager;
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

public class StdSudokuModel implements SudokuModel {

	//ATTRIBUTS
	public static final String SEPARATOR = " ";
	public static final int HISTORY_SIZE = 1024;
	
	private GridModel gridPlayer;
	private GridModel gridSoluce;
	
	private History<Command> history;

	//CONSTRUCTEUR
	public StdSudokuModel(int width, int height)  {
		Contract.checkCondition(width > 0 && height > 0);
		gridPlayer = new StdGridModel(width, height);
		gridSoluce = new StdGridModel(width, height);
		history = new StdHistory<Command>(HISTORY_SIZE);
	}
	
	public StdSudokuModel(File textFile) throws IOException {
		BufferedReader fr = new BufferedReader(new FileReader(textFile));
		try {
			String line = fr.readLine();
			String[] tokens = line.split(SEPARATOR);
			final int width = Integer.parseInt(tokens[0]);
			final int height = Integer.parseInt(tokens[1]);
			
			gridSoluce = new StdGridModel(width, height);
			gridPlayer = new StdGridModel(width, height);
			for (int k = 0; k < width * height; ++k) {
				line = fr.readLine();
				tokens = line.split(SEPARATOR);
				CellModel[] gridPlayerLine = gridPlayer.cells()[k];
				for (int j = 0; j < width * height; ++j) {
					int value = Integer.parseInt(tokens[j]);
					CellModel gridPlayerCell = gridPlayerLine[j];
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
						updateEasyPossibilities(new Coord(i, j));
					}
				}
			}
			gridSoluce = (StdGridModel) gridPlayer.clone();
		} finally {
			fr.close();
		}
	}

	//REQUETES
	public GridModel getGridPlayer() {
		return gridPlayer;
	}

	public GridModel getGridSoluce() {
		return gridSoluce;
	}
	
	public boolean isWin() {
		CellModel[][] tabPlayer = getGridPlayer().cells();
		CellModel[][] tabSoluce = getGridSoluce().cells();
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
		CellModel[][] tabPlayer  = getGridPlayer().cells();
		CellModel[][] tabSoluce  = getGridSoluce().cells();
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
		RuleManager rm = new RuleManager(gridPlayer);
		rm.findRule();
		return rm.describe();
	}


	//COMMANDES
	public void updateEasyPossibilities(ICoord c) {
		Contract.checkCondition(c != null
				&& isValidCoord(c) 
				&& getGridPlayer().getCell(c).getValue() > 0);
		int n = getGridPlayer().getCell(c).getValue();
		Set<CellModel> set = getGridPlayer().getUnitCells(c);
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
	public void resolve(GridModel g) {
		Contract.checkCondition(g != null);
		RuleManager rm = new RuleManager(g);
		rm.findRule();
		rm.executeRule();
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
			gridPlayer = (StdGridModel) ois.readObject();
			gridSoluce = (StdGridModel) ois.readObject();
		} finally {
			ois.close();
		}
	}
}