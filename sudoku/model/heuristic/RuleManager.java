package sudoku.model.heuristic;

import java.util.Vector;
import sudoku.model.IGrid;

public class RuleManager {

  private IGrid grid;

  private Report lastReport;

  public void findRule() {
	  
  }

  public void executeRule() {
	  // à voir
  }

  //COMMANDES
  public void executeRule() {
	  lastReport.execute(grid);

}