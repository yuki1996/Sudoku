package sudoku.model.history.cmd;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import sudoku.util.ICoord;
import util.Contract;

public class RemoveCandidate extends AbstractCommand {
	
	// ATTRIBUTS
	private ICell cell;
	private int value;

	public RemoveCandidate(IGrid grid, ICoord coord, int value) {
		this(grid, grid.getCell(coord), value);
	}

	public RemoveCandidate(IGrid grid, ICell cell, int value) {
		super(grid);
		Contract.checkCondition(grid != null, "la grille est null");
		Contract.checkCondition(cell != null, "la coordonnée est null");
		this.cell = cell;
		this.value = value;
	}

	@Override
	protected void doIt() {
		Contract.checkCondition(!cell.hasValue(), "");
		cell.removeCandidate(value);
	}

	@Override
	protected void undoIt() {
		Contract.checkCondition(!cell.hasValue(), "");
		cell.addCandidate(value);
	}
	
}
