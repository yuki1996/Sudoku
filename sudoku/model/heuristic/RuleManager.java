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
	  return lastReport.describe();
  }

  public static boolean solve(int i, int j, GridModel g) {
	  if (g.isFull()) {	
		  return true; 
	  }
	  if (g.cells()[i][j].hasValue()) {
		  return solve(i + 1, j, g);
	  }
	  for (int val = 1; val < g.numberCandidates(); ++val) {
		  if (legal(i, j, val, g)) {
			  g.cells()[i][j].setValue(val);
			  if (solve(i + 1, j, g)) {
				  return true;
			  }
		  }
	  }
	  if (g.cells()[i][j].isModifiable()) {
		  g.cells()[i][j].removeValue();
	  }
	  return false;
  }
  
  private static boolean legal(int col, int row, int val, GridModel g) {
	  for (ICoord c : g.getRow(row)) {
		  if (val == g.getCell(c).getValue()) {
			  return false;
		  }
	  }
	  for (ICoord c : g.getCol(col)) {
		  if (val == g.getCell(c).getValue()) {
			  return false;
		  }
	  }
	  for (ICoord c : g.getSector(row,col)) {
		  if (val == g.getCell(c).getValue()) {
			  return false;
		  }
	  }
	  return true;
  }

  public Command getCommand() {
	  return lastReport.getCommand();
  }
  
  //COMMANDES
  
  public void findRule() {
	for (int i = 0 ; i < Rule.values().length; i++) {
		lastReport = Rule.values()[i].getGenerator().generate(grid);
		if (lastReport != null) {
			break;
		}
	}
  }
  
}