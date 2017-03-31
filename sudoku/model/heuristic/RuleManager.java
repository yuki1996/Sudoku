package sudoku.model.heuristic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import sudoku.model.GridModel;
import sudoku.model.history.cmd.Command;
import util.Contract;

public class RuleManager {
  // PROPRIETES
  public static final String LAST_REPORT = "report";
  
  //ATTRIBUTS
	
  private GridModel grid;

  private Report lastReport;
  
  private PropertyChangeSupport propertySupport;
  
  //CONSTRUCTEURS
  
  public RuleManager(GridModel g) {
	  Contract.checkCondition(g != null);
	  grid = g;
	  lastReport = null;
	  propertySupport = new PropertyChangeSupport(this);
  }

  //REQUETES
  
  public String describe() {
	  return lastReport != null ? lastReport.describe() : null;
  }

  public Report getLastReport() {
	  return lastReport;
  }
  
  //COMMANDES

  public Command generateCommand() {
	  if (lastReport != null) {
		  Report r = lastReport;
		  clear();
		  return r.generateCommand();
	  }
	  return null;
  }
  
  public void setGrid(GridModel g) {
	  grid = g;
	  clear();
  }
  
  public void findRule() {
		clear();
		for (int i = 0 ; i < Rule.values().length && lastReport == null; i++) {
			if (Rule.values()[i].getGenerator() != null) {
				lastReport = Rule.values()[i].getGenerator().generate(grid);
			}
		}
		if (lastReport != null) {
			propertySupport.firePropertyChange(LAST_REPORT, null, lastReport);
		}
  }
  
  public void clear() {
	  if (lastReport != null) {
		  Report oldReport = lastReport;
		  lastReport = null;
		  propertySupport.firePropertyChange(LAST_REPORT, oldReport, lastReport);
	  }
  }
  
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
	  propertySupport.addPropertyChangeListener(propertyName, l);
  }
  
  public void removePropertyChangeListener(PropertyChangeListener l) {
	  propertySupport.removePropertyChangeListener(l);
  }

  /** The active part begins here */
  public void backtracking() {
	estValide(0);
  }

  private boolean absentSurLigne(int k, int i) {
      for (int j = 0; j < grid.numberCandidates(); j++) {
    	  if (grid.cells()[i][j].getValue() == k) {
    		  return false;
    	  }
      }
      return true;
  }

  private boolean absentSurColonne(int k, int j) {
      for (int i = 0; i < grid.numberCandidates(); i++) {
          if (grid.cells()[i][j].getValue() == k) {
              return false;
          }
      }
      return true;
  }

  boolean absentSurBloc(int k, int i, int j) {
      int rowSector = grid.getNumberSectorByWidth() * (i / grid.getNumberSectorByWidth());
      int colSector = grid.getNumberSectorByHeight() * (j / grid.getNumberSectorByHeight());
      for (i = rowSector; i < rowSector + grid.getNumberSectorByWidth(); i++) {
          for (j = colSector; j < colSector + grid.getNumberSectorByHeight(); j++) {
              if (grid.cells()[i][j].getValue() == k) {
                  return false;
              }
          }
      }
      return true;
  }

  boolean estValide(int position){
      if (position == grid.numberCandidates() * grid.numberCandidates()) {
          return true;
      }
      int i = position / grid.numberCandidates(), j = position % grid.numberCandidates();
      if (grid.cells()[i][j].hasValue()) {
          return estValide(position+1);
      }
      for (int k = 1; k <= grid.numberCandidates(); k++) {
          if (absentSurLigne(k,i) && absentSurColonne(k,j) && absentSurBloc(k,i,j)) {
        	  grid.cells()[i][j].setValue(k);
              if (estValide(position+1)) {
                  return true;
              }
          }
      }
      if (grid.cells()[i][j].isModifiable()) {
    	  grid.cells()[i][j].removeValue();
      }
      return false;

  }
}