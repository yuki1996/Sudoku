package sudoku.model.heuristic;

import java.util.Vector;
import sudoku.model.IGrid;
import sudoku.model.heuristic.Rule.Report;
import util.Contract;

public class RuleManager {

  //ATTRIBUTS
  private IGrid grid;
  private Report lastReport;
  public Vector  myReport;



  //CONSTRUCTEUR
  public RuleManager(IGrid g) {
    Contract.checkCondition(g != null);
    grid = g;
	lastReport = null;
  }
  
  //REQUÊTES
  public void findRule() {
	Report report = Rule.values()[0].generate(grid);
	report.setRule(Rule.values()[0]);
	for (int i = 1; i < Rule.values().length && report.describe(grid) == null; i++) {
		report = Rule.values()[i].generate(grid);
		report.setRule(Rule.values()[i]);
	}
	lastReport = report;
  }

  //COMMANDES
  public void executeRule() {
	  lastReport.execute(grid);
  }

}