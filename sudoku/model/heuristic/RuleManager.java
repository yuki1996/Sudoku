package sudoku.model.heuristic;

import sudoku.model.GridModel;
import sudoku.model.history.cmd.Command;
import sudoku.util.ICoord;
import util.Contract;

public class RuleManager {
  //ATTRIBUTS
	
  private GridModel grid;

  private Report lastReport;
  
  //CONSTRUCTEURS
  
  public RuleManager(GridModel g) {
	  Contract.checkCondition(g != null);
	  grid = g;
  }

  //REQUETES
  
  public String describe() {
	  return lastReport != null ? lastReport.describe() : null;
  }

  public Command generateCommand() {
	  if (lastReport != null) {
		  Report r = lastReport;
		  lastReport = null;
		  return r.generateCommand();
	  }
	  return null;
  }
  
  //COMMANDES
  
  public void findRule() {
		lastReport = null;
		for (int i = 0 ; i < Rule.values().length && lastReport == null; i++) {
			if (Rule.values()[i].getGenerator() != null) {
				lastReport = Rule.values()[i].getGenerator().generate(grid);
			}
		}
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