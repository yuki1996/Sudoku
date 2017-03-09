package sudoku.model.history.cmd;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.util.ICoord;
import util.Contract;

public class RemoveCandidate extends AbstractCommand {
	
	// ATTRIBUTS
	private CellModel cell;
	private int value;

	public RemoveCandidate(GridModel grid, ICoord coord, int value) {
		this(grid, grid.getCell(coord), value);
	}

	public RemoveCandidate(GridModel grid, CellModel cell, int value) {
		super(grid);
		Contract.checkCondition(grid != null, "la grille est null");
		Contract.checkCondition(cell != null, "la coordonnée est null");
		this.cell = cell;
		this.value = value;
	}

	@Override
	protected void doIt() {
		if (cell.isModifiable()) {
			cell.removeCandidate(value);
		}
	}

	@Override
	protected void undoIt() {
		Contract.checkCondition(!cell.hasValue(), "");
		cell.addCandidate(value);
	}
	
}
