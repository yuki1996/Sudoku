package sudoku.model.heuristic;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import util.Contract;

public class RuleManager {

  private GridModel grid;

  private Report lastReport;

  public RuleManager(GridModel g) {
	  Contract.checkCondition(g != null);
	  grid = g;
  }
  
  public void findRule() {
	for (int i = 0 ; i < Rule.values().length; i++) {
		lastReport = Rule.values()[i].getGenerator().generate(grid);
		if (lastReport != null) {
			break;
		}
	}
  }

  //COMMANDES
  public void executeRule() {
	  if (lastReport != null) {
		  lastReport.execute(grid);
		  lastReport = null;
	  }
  }
  
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
	  for (CellModel c : g.getRow(row)) {
		  if (val == c.getValue()) {
			  return false;
		  }
	  }
	  for (CellModel c : g.getCol(col)) {
		  if (val == c.getValue()) {
			  return false;
		  }
	  }
	  for (CellModel c : g.getSector(row,col)) {
		  if (val == c.getValue()) {
			  return false;
		  }
	  }
	  return true;
  }
}