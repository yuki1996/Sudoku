package sudoku.model.heuristic;

import sudoku.model.GridModel;

public class RuleManager {

  private GridModel grid;

  private Report lastReport;

  public void findRule() {
	  
  }

  //COMMANDES
  public void executeRule() {
	  lastReport.execute(grid);
  }
}