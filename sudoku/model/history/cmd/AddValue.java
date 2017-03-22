package sudoku.model.history.cmd;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.util.ICoord;
import util.Contract;

public class AddValue extends AbstractCommand {
	
	// ATTRIBUTS
	private CellModel cell;
	private int value;
	private Set<Command> sideEffects;

	public AddValue(GridModel grid, ICoord coord, int value) {
		this(grid, grid.getCell(coord), value);
	}
	
	public AddValue(GridModel grid, CellModel cell, int value) {
		super(grid);
		Contract.checkCondition(grid != null, "la grille est null");
		Contract.checkCondition(cell != null, "la coordonnée est null");
		this.cell = cell;
		this.value = value;
		sideEffects = new HashSet<Command>();
		for (ICoord c : grid.getUnitCoords(grid.getCoord(cell))) {
			if (!grid.getCell(c).hasValue() && grid.getCell(c).isCandidate(value)) {
				sideEffects.add(new RemoveCandidate(grid, c, value));
			}
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
