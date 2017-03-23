package sudoku.model.history.cmd;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.util.ICoord;
import util.Contract;

public class AddCandidate extends AbstractCommand {
	
	// ATTRIBUTS
	private CellModel cell;
	private int value;

	public AddCandidate(GridModel grid, ICoord coord, int value) {
		this(grid, grid.getCell(coord), value);
	}

	public AddCandidate(GridModel grid, CellModel cell, int value) {
		super(grid);
		Contract.checkCondition(grid != null, "la grille est null");
		Contract.checkCondition(cell != null, "la coordonnée est null");
		this.cell = cell;
		this.value = value;
	}

	@Override
	protected void doIt() {
		Contract.checkCondition(!cell.hasValue(), "");
		cell.addCandidate(value);
	}

	@Override
	protected void undoIt() {
		Contract.checkCondition(!cell.hasValue(), "");
		cell.removeCandidate(value);
	}
	
}
