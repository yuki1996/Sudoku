package sudoku.model.heuristic;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sudoku.model.ICell;
import sudoku.model.IGrid;
import util.Contract;

public class Report {

	private Rule rule;
	private Map<YoloEnsembles, Set<ICell>> cellSets;
	private Set<Integer> values;

	// pas nécessaire de le mettre ici : à voir
	public enum YoloEnsembles {
		DECISIVE_CELLS,
		DECISIVE_UNITS,
		DELETION_CELLS,
		DELETION_UNITS;
	}
	
	// CONSTRUCTEUR
	protected Report(Rule rule) {
		Contract.checkCondition(rule != null, "rule est null");
		this.rule = rule;
		cellSets = new EnumMap<YoloEnsembles, Set<ICell>>(YoloEnsembles.class);
		values = new HashSet<Integer>();
	}
	
	public Set<Integer> getValueSet() {
		return new HashSet<Integer>(values);
	}
	
	public Set<ICell> getCellSet(YoloEnsembles ye) {
		return new HashSet<ICell>(cellSets.get(ye));
	}
	
//	public String describe() {
//		return rule.getGenerator().describe(this);
//	}

	public void execute() {
		// à voir
	}
	
	protected abstract class ReportGenerator {
		// methodes de modification des Report pour leur génération
		protected void addValue(int val) {
			values.add(val);
		}
		
		protected void addCell(YoloEnsembles ye, ICell cell) {
			
		}
		
		// génération des Report
		protected abstract Report generate(IGrid grid);
	}

}