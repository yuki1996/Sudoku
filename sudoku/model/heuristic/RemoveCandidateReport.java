package sudoku.model.heuristic;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sudoku.model.ICell;

public class RemoveCandidateReport extends Report {
	
	// pas nécessaire de le mettre ici : à voir
	enum CellSetName {
		DECISIVE_CELLS,
		DECISIVE_UNITS,
		DELETION_CELLS,
		DELETION_UNITS;
	}
	
	//ATRIBUTS
	private Map<CellSetName, Set<ICell>> cellSets;
	private Set<Integer> values;
	
	// CONSTRUCTEUR
	RemoveCandidateReport(String ruleName) {
		super(ruleName);
		cellSets = new EnumMap<CellSetName, Set<ICell>>(CellSetName.class);
		for (CellSetName csn : CellSetName.values()) {
			cellSets.put(csn, new HashSet<ICell>());
		}
		values = new HashSet<Integer>();
	}
	
	//REQUETES
	public String describe() {
		String res = new String("Regle : " + getRuleName() + "\n");
		res += "On peut supprimer les candidats";
		for (Integer n : values) {
			res += " " + n;
		}
		/*
		 * Données les cellules affectés les DECISIVE_CELLS, les DECISIVE_UNITS,
		 * les DELETION_CELLS et les DELETION_UNITS
		 * BESOIN D'UN NOM POUR LES CELLULES!!!!!
		 */
		return res;
	}
	
	public Set<Integer> getValueSet() {
		return new HashSet<Integer>(values);
	}
	
	public Set<ICell> getCellSet(CellSetName csn) {
		return new HashSet<ICell>(cellSets.get(csn));
	}

	//COMMANDES
	public void execute() {
		for (ICell c : cellSets.get(CellSetName.DELETION_CELLS)) {
			for (Integer n : values) {
				c.removeCandidate(n);
			}
		}
	}
	
	void addCell(CellSetName csn, ICell cell) {
		cellSets.get(csn).add(cell);
	}
	
	void addValue(int n) {
		values.add(n);
	}
	
	void setCellSet(CellSetName csn, Set<ICell> newSet) {
		cellSets.put(csn, newSet);
	}
}
