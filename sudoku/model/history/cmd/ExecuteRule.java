package sudoku.model.history.cmd;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.model.heuristic.Report;
import sudoku.model.heuristic.Report.CellSetName;
import util.Contract;

public class ExecuteRule extends AbstractCommand {

	// ATTRIBUTS
	private Set<Command> actions;
	
	// CONSTRUCTEUR
	public ExecuteRule(GridModel grid, Report report) {
		super(grid);
		Contract.checkCondition(report != null, "report est null");
		
		actions = new HashSet<Command>();
		Set<Integer> values = report.getValueSet();
		Set<CellModel> deletionCells = report.getCellSet(CellSetName.DELETION_CELLS);
		if (deletionCells != null) {
			for (CellModel c : deletionCells) {
				for (Integer n : values) {
					actions.add(new RemoveCandidate(grid, c, n));
				}
			}
		} else {
			Set<CellModel> decisiveCells = report.getCellSet(CellSetName.DECISIVE_CELLS);
			for (CellModel c : decisiveCells) {
				actions.add(new AddValue(grid, c, values.iterator().next()));
			}
		}
	}
	
	@Override
	protected void doIt() {
		for (Command c : actions) {
			c.act();
		}
	}

	@Override
	protected void undoIt() {
		for (Command c : actions) {
			c.act();
		}
	}
	
}