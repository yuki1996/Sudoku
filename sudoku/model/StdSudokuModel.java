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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private RuleManager ruleManager; 
    private History<Command> history;
    
    private PropertyChangeSupport propertySupport;

    //CONSTRUCTEUR
    public StdSudokuModel(int width, int height)  {
        Contract.checkCondition(width > 0 && height > 0);
        gridPlayer = new StdGridModel(width, height);
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
        } finally {
            fr.close();
        }
    }

    //REQUETES
    public GridModel getGridPlayer() {
        return gridPlayer;
    }

    
    public boolean isWin() {
        return getGridPlayer().isFull() && check().isEmpty();
    }

    public boolean isModifiableCell(ICoord coord) {
        Contract.checkCondition(coord != null
                && isValidCoord(coord));
        return getGridPlayer().getCell(coord).isModifiable();
    }

    public Set<ICoord> check() {
        Set<ICoord> set = new HashSet<ICoord>();
        set.addAll(checkLine());
        set.addAll(checkColumn());
        set.addAll(checkSector());
        return set;
    }

    public boolean isValidCoord(ICoord coord) {
        Contract.checkCondition(coord != null);
        return 0 <= coord.getCol() && coord.getCol() < getGridPlayer().size()
                && 0 <= coord.getRow() && coord.getRow() < getGridPlayer().size();
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
    	while (!gridPlayer.isFull()) {
            resolve();
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
                        for (int k = 1; k <= getGridPlayer().numberCandidates(); ++k) {
                        	if (getGridPlayer().getCell(new Coord(i, j)).isCandidate(k)) {
		                        act(new AddValue(getGridPlayer(), new Coord(i, j), k));
		                        return;
                        	}
                        }
                    }
                    
                }
            }
        }
        act(cmd);
    }

    public void reset() {
        //getGridPlayer().reset();
        history.clear();
    }
    
    public void save(String name) throws IOException {
        Contract.checkCondition(name != null && !name.equals(""));
        File fichier =  new File(name);
        ObjectOutputStream oos =  new ObjectOutputStream(new FileOutputStream(fichier)) ;
        try {
            oos.writeObject(getGridPlayer());
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
            gridPlayer = gp;
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
        propertySupport.firePropertyChange(FINISH, false, isWin());
    }
    
    public void undo() {
        Contract.checkCondition(canUndo());
        history.getCurrentElement().act();
        history.goBackward();
    }
    
    public void redo() {
        Contract.checkCondition(canRedo());
        history.goForward();
        history.getCurrentElement().act();
    }
    
    public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener l) {
    	if (propertyName.equals(RuleManager.LAST_REPORT)) {
    		ruleManager.addPropertyChangeListener(propertyName, l);
    	} else {
    		propertySupport.addPropertyChangeListener(propertyName, l);
    	}
    }
    
    private Set<ICoord> checkLine() {
    	Set<ICoord> setFinish = new HashSet<ICoord>();
    	Map<Integer, List<ICoord>> map = new HashMap<Integer, List<ICoord>>();
        for (int i = 0; i < getGridPlayer().size(); i++) {
        	for (int k = 1; k <= getGridPlayer().numberCandidates(); k++) {
	    		map.put(k, new ArrayList<ICoord>());
	    	}
        	for (int j = 0; j < getGridPlayer().size(); j++) {
        		if (getGridPlayer().cells()[i][j].hasValue()) {
        			int value = getGridPlayer().cells()[i][j].getValue();
        			List<ICoord> l = map.get(value);
        			Coord c = new Coord(i, j);
        			if (!l.isEmpty()) {
        				setFinish.add(l.get(0));
        				setFinish.add(c);
        			}
        			l.add(c);
        			map.put(value, l);
        		}
      	   }
        	
        }
        return setFinish;
    }

    private Set<ICoord> checkColumn() {
    	Set<ICoord> setFinish = new HashSet<ICoord>();
    	Map<Integer, List<ICoord>> map = new HashMap<Integer, List<ICoord>>();
        for (int i = 0; i < getGridPlayer().size(); i++) {
        	for (int k = 1; k <= getGridPlayer().numberCandidates(); k++) {
	    		map.put(k, new ArrayList<ICoord>());
	    	}
        	for (int j = 0; j < getGridPlayer().size(); j++) {
        		if (getGridPlayer().cells()[j][i].hasValue()) {
        			int value = getGridPlayer().cells()[j][i].getValue();
        			List<ICoord> l = map.get(value);
        			Coord c = new Coord(j, i);
        			if (!l.isEmpty()) {
        				setFinish.add(l.get(0));
        				setFinish.add(c);
        			}
        			l.add(c);
        			map.put(value, l);
        		}
      	   }
        	
        }
        return setFinish;
    }

    private Set<ICoord> checkSector() {
    	Set<ICoord> setFinish = new HashSet<ICoord>();
    	Map<Integer, List<ICoord>> map = new HashMap<Integer, List<ICoord>>();

		int nbSW = getGridPlayer().getNumberSectorByWidth();
		int nbSH = getGridPlayer().getNumberSectorByHeight();
		
		for (int i = 0; i < getGridPlayer().getWidthSector(); i++) {
			for (int j = 0; j < getGridPlayer().getHeightSector(); j++) {
				for (int k = 1; k <= getGridPlayer().numberCandidates(); k++) {
		    		map.put(k, new ArrayList<ICoord>());
				}
				for (int m = i * nbSW; m < getGridPlayer().getHeightSector() * (i + 1); m++) {
					for (int n = j * nbSH; n < getGridPlayer().getWidthSector() * (j + 1); n++) {
						int value = getGridPlayer().cells()[m][n].getValue();
	        			List<ICoord> l = map.get(value);
	        			Coord c = new Coord(m, n);
	        			if (!l.isEmpty()) {
	        				setFinish.add(l.get(0));
	        				setFinish.add(c);
	        			}
	        			l.add(c);
	        			map.put(value, l);
	        		}
	      	   }
	        	
	       }
        }
        return setFinish;
    }
}
