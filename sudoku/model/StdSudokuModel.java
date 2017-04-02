package sudoku.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import sudoku.model.heuristic.Report;
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
	private RuleManager ruleManager; 
	private History<Command> history;

	private PropertyChangeSupport propertySupport;

	//CONSTRUCTEUR
	public StdSudokuModel(int width, int height)  {
		Contract.checkCondition(width > 0 && height > 0);
		gridPlayer = new StdGridModel(width, height);
		gridSoluce = new StdGridModel(width, height);
		ruleManager = new RuleManager(gridPlayer);
		history = new StdHistory<Command>(HISTORY_SIZE);

		propertySupport = new PropertyChangeSupport(this);
	}
	
	public StdSudokuModel(File textFile) throws IOException {

		propertySupport = new PropertyChangeSupport(this);
		BufferedReader fr = new BufferedReader(new FileReader(textFile));
		try {
			String line = fr.readLine();
			String[] tokens = line.split(SEPARATOR);
			final int width = Integer.parseInt(tokens[0]);
			final int height = Integer.parseInt(tokens[1]);
			
			gridSoluce = new StdGridModel(width, height);
			gridPlayer = new StdGridModel(width, height);

			ruleManager = new RuleManager(gridPlayer);
			history = new StdHistory<Command>(HISTORY_SIZE);
			for (int k = 0; k < width * height; ++k) {
				line = fr.readLine();
				tokens = line.split(SEPARATOR);
				for (int j = 0; j < width * height; ++j) {
					int value = Integer.parseInt(tokens[j]);
					CellModel gridPlayerCell = gridPlayer.cells()[k][j];
					if (value != 0){
						new AddValue(gridPlayer, gridPlayerCell, value).act();
						gridPlayerCell.setModifiable(false);
					}
				}
			}
			gridSoluce = (StdGridModel) gridPlayer.clone();
			RuleManager rm = new RuleManager(gridSoluce);
			while (!gridSoluce.isFull()) {
				rm.findRule();
				Command r = rm.generateCommand();
				if (r != null) {
					r.act();
				} else {
					rm.backtracking();
				}
			}
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

	public Set<ICoord> check() {
		Set<ICoord> set = new HashSet<ICoord>();
		CellModel[][] tabPlayer  = getGridPlayer().cells();
		CellModel[][] tabSoluce  = getGridSoluce().cells();
		for (int i = 0 ; i < tabPlayer.length; i++) {
			for (int j = 0 ; j < tabPlayer[i].length; j++) {
				if (tabPlayer[i][j].getValue() != 0) {
					if (tabPlayer[i][j].getValue() != tabSoluce[i][j].getValue()) {
						set.add(new Coord(i, j));
					}
				}
			}
		}
		return set;
	}

	public boolean isValidCoord(ICoord coord) {
		Contract.checkCondition(coord != null);
		return 0 <= coord.getCol() && coord.getCol() < getGridSoluce().size()
				&& 0 <= coord.getRow() && coord.getRow() < getGridSoluce().size();
	}

	public String help() {
		ruleManager.findRule();
		return ruleManager.describe();
	}
	
	public Report getLastReport() {
		return ruleManager.getLastReport();
	}

	public boolean canRedo() {
		return history.getCurrentPosition() < history.getEndPosition();
	}
	
	public boolean canUndo() {
		return history.getCurrentPosition() > 0;
	}
  
	//COMMANDES
	public void setValue(ICoord c, int n) {
		Contract.checkCondition(c != null
				&& isValidCoord(c) && n > 0
				&& 1 <= n  && n <= getGridPlayer().numberCandidates());
		act(new AddValue(gridPlayer, c, n));
	}

	public void removeValue(ICoord c) {
		Contract.checkCondition(c != null
				&& isValidCoord(c));
		act(new RemoveValue(gridPlayer, c));
	}

	public void addCandidate(ICoord c, int n) {
		Contract.checkCondition(c != null
				&& isValidCoord(c) && 1 <= n 
				&& n <= getGridPlayer().numberCandidates());
		act(new AddCandidate(gridPlayer, c, n));
	}

	public void removeCandidate(ICoord c, int n) {
		Contract.checkCondition(c != null
				&& isValidCoord(c) && 1 <= n 
				&& n <= getGridPlayer().numberCandidates());
		act(new RemoveCandidate(gridPlayer, c, n));
	}

	public void finish() {
		for (int i = 0 ; i < getGridPlayer().size(); i++) {
			for (int j = 0 ; j < getGridPlayer().size(); j++) {
				if (getGridPlayer().cells()[i][j].isModifiable()) {
					act(new AddValue(getGridPlayer(), new Coord(i, j), 
							getGridSoluce().getCell(new Coord(i, j)).getValue()));
				}
			}
		}
	}


	@Override
	public void resolve() {
		ruleManager.findRule();
		Command cmd = ruleManager.generateCommand();
		if (cmd == null) {
			for (int i = 0; i < getGridPlayer().size(); ++i) {
				for (int j = 0; j < getGridPlayer().size(); ++j) {
					if (! getGridPlayer().getCell(new Coord(i, j)).hasValue()) {
						act(new AddValue(getGridPlayer(), new Coord(i, j), 
							getGridSoluce().getCell(new Coord(i, j)).getValue()));
						return;
					}
					
				}
			}
		}
		act(cmd);
	}

	public void reset() {
    	while (canUndo()) {
			undo();
		}
        history.clear();
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
		GridModel oldModel = gridPlayer;
		try {
			GridModel gp = (GridModel) ois.readObject();
			GridModel gs = (GridModel) ois.readObject();
			gridPlayer = gp;
			gridSoluce = gs;
		} finally {
		    ois.close();
		}
		history.clear();
		ruleManager.setGrid(gridPlayer);
		propertySupport.firePropertyChange(GRID, oldModel, gridPlayer);
	}

	public void act(Command cmd) {
		Contract.checkCondition(cmd != null, "cmd est null");
		cmd.act();
		history.add(cmd);
		ruleManager.clear();
		propertySupport.firePropertyChange(FINISH, false, isWin());
	}
	
	public void undo() {
		Contract.checkCondition(canUndo());
		history.getCurrentElement().act();
		history.goBackward();
		ruleManager.clear();
	}
	
	public void redo() {
		Contract.checkCondition(canRedo());
		history.goForward();
		history.getCurrentElement().act();
		ruleManager.clear();
	}
	

    public void removePropertyChangeListener(PropertyChangeListener l) {
    		ruleManager.removePropertyChangeListener(l);
    		propertySupport.removePropertyChangeListener(l);
    }
    
    public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener l) {
    	if (propertyName.equals(RuleManager.LAST_REPORT)) {
    		ruleManager.addPropertyChangeListener(propertyName, l);
    	} else {
    		propertySupport.addPropertyChangeListener(propertyName, l);
    	}
	}
	
}
