package sudoku.rules;

import sudoku.model.IGrid;
import util.Contract;

public class RulesManager {

	//ATTRIBUTS
	private IGrid grid;
	private Report lastReport;
	
	
	//CONSTRUCTEUR
	public RulesManager(IGrid g) {
		Contract.checkCondition(g != null);
		grid = g;
		lastReport = null;
	}
	
	//REQUÊTES
	public Report FindRule() {
		Report report = new Report();
		for (int i = 0; i < Rules.values().length && report == null; i++) {
			report.setRule(Rules.values()[i]);
		}
		lastReport = report;
		return report;
	}
	
	//COMMANDES
	public void executeReport() {
		
	}
}
