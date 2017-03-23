package sudoku.model.history.cmd;

import java.util.Set;

import sudoku.model.GridModel;
import util.Contract;

public class CommandSet extends AbstractCommand {

	// ATTRIBUTS
	private Set<Command> actions;
	
	// CONSTRUCTEUR
	public CommandSet(GridModel grid, Set<Command> commandSet) {
		super(grid);
		Contract.checkCondition(commandSet != null, "report est null");
		actions = commandSet;
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