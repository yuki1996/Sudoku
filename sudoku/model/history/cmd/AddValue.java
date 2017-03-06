package sudoku.model.history.cmd;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import sudoku.util.ICoord;
import util.Contract;

public class AddValue extends AbstractCommand {
	
	// ATTRIBUTS
	private ICell cell;
	private int value;
	private Set<Command> sideEffects;

	public AddValue(IGrid grid, ICoord coord, int value) {
		this(grid, grid.getCell(coord), value);
	}
	
	public AddValue(IGrid grid, ICell cell, int value) {
		super(grid);
		Contract.checkCondition(grid != null, "la grille est null");
		Contract.checkCondition(cell != null, "la coordonnée est null");
		this.cell = cell;
		this.value = value;
		sideEffects = new HashSet<Command>();
		for (ICell c : grid.getUnitCells(grid.getCoord(cell))) {
			sideEffects.add(new RemoveCandidate(grid, c, value));
		}
	}

	@Override
	protected void doIt() {
		cell.setValue(value);
		for (Command c : sideEffects) {
			c.act();
		}
	}

	@Override
	protected void undoIt() {
		cell.removeValue();
		for (Command c : sideEffects) {
			c.act();
		}
	}
	
}
