package sudoku.model.history.cmd;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import sudoku.util.ICoord;
import util.Contract;

public class RemoveValue extends AbstractCommand {
	
	// ATTRIBUTS
	private ICell cell;
	private int value;

	public RemoveValue(IGrid grid, ICoord coord) {
		super(grid);
		Contract.checkCondition(grid != null, "la grille est null");
		Contract.checkCondition(cell != null, "la coordonnée est null");
		Contract.checkCondition(grid.isValidCoord(coord), "mauvaise coordonnée");
		cell = grid.getCell(coord);
		this.value = cell.getValue();
	}

	@Override
	protected void doIt() {
		Contract.checkCondition(!cell.hasValue(), "");
		cell.removeValue();
	}

	@Override
	protected void undoIt() {
		Contract.checkCondition(!cell.hasValue(), "");
		cell.setValue(value);
	}
	
}
