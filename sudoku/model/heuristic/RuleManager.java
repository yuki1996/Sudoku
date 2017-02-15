package sudoku.model.heuristic;

import java.util.Vector;
import sudoku.model.IGrid;
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
	Report report = new Report();
	for (int i = 0; i < Rule.values().length && report == null; i++) {
		report.setRule(Rule.values()[i]);
	}
	lastReport = report;
  }

  //COMMANDES
  public void executeRule() {
  }

}