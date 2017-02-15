package sudoku.model.heuristic;

import java.util.HashSet;
import java.util.Vector;
import java.util.Set;

import sudoku.model.ICell;
import util.Contract;

class Report {

  private Rule rule;
  private Set decisiveCells;
  private Set contextualCells;
  private Set deletionCells;
  private Set actualDeletionCells;
  private Set values;

  public Vector  myRule;
  public Vector  myRuleManager;
  
  //CONSTRUCTEURS
  public Report() {
	rule = null;
	decisiveCells = new HashSet<ICell>();
	contextualCells = new HashSet<ICell>();
	deletionCells = new HashSet<ICell>();
	actualDeletionCells = new HashSet<ICell>();
	values = new HashSet<Integer>();
  }
  
  //REQUÊTE
  public String describe() {
	return rule.describe(this);
  }
  
  //COMMANDE
  public void execute() {
	rule.execute(this);
  }
  
  public void setRule(Rule r) {
	  Contract.checkCondition(r != null);
	  rule = r;
	  decisiveCells.clear();
	  contextualCells.clear();
	  deletionCells.clear();
	  actualDeletionCells.clear();
	  values.clear();
  }

}
