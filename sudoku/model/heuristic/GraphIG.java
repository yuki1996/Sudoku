package sudoku.model.heuristic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sudoku.model.CellModel;
import sudoku.model.GridModel;
import sudoku.util.Couple;
import sudoku.util.ICoord;
import util.Contract;

public class GraphIG {
	//ATTRIBUTS
	private final Set<Integer> candidates;
	private final List<ICoord> coordList;
	private final Map<Integer, GraphIG> sons;
	private int cellNb;
	
	//CONSTRUCTEURS
	public GraphIG(Set<Integer> candidates) {
		Contract.checkCondition(candidates != null);
		this.candidates = candidates;
		coordList = new ArrayList<ICoord>();
		sons = new HashMap<Integer, GraphIG>();
		cellNb = 0;
	}
	
	//REQUETES
	/*
	 * Renvoie une liste de couples composés d'un ensemble de n candidats et
	 * d'une liste de coordonnées qui ne possedent pas d'autres candidats que
	 * ceux présent dans l'ensemble de candidats.
	 */
	public List<Couple<Set<Integer>, List<ICoord>>> getGoodGroup() {
		List<Couple<Set<Integer>, List<ICoord>>> res = new ArrayList<Couple<Set<Integer>, List<ICoord>>>();
		for (GraphIG graph : sons.values()) {
			res.addAll(graph.getGoodGroup());
		}
		if (cellNb == candidates.size() && ! coordList.isEmpty()) {
			res.add(new Couple<Set<Integer>, List<ICoord>>(candidates, getCoord()));
		}
		return res;
	}
	
	/*
	 * Renvoie la liste de coordonnées du graphe et de ces successeurs
	 */
	public List<ICoord> getCoord() {
		Set<ICoord> res = new HashSet<ICoord>(coordList);
		for (GraphIG graph : sons.values()) {
			res.addAll(graph.getCoord());
		}
		return new ArrayList<ICoord>(res);
	}
	
	/*
	 * Renvoie un ensemble contenant les candidats du graphe.
	 */
	public Set<Integer> getCandidates() {
		return candidates;
	}
	
	//COMMANDES
	public void add(ICoord c, GridModel g) {
		Contract.checkCondition(c != null);
		Contract.checkCondition(g != null);
		Contract.checkCondition(g.isValidCoord(c));
		
		++cellNb;
		Set<Integer> setCandidates = getCandidates(g.getCell(c));
		if (setCandidates.size() == candidates.size()) {
			coordList.add(c);
			return;
		}
		for (int i = 1; i <= g.numberCandidates(); ++i) {
			if (! setCandidates.contains(i) && candidates.contains(i)) {
				GraphIG graph = sons.get(i);
				if (graph == null) {
					Set<Integer> graphArg = new HashSet<Integer>(candidates);
					graphArg.remove(i);
					sons.put(i, new GraphIG(graphArg));
					graph = sons.get(i);
				}
				graph.add(c, g);
			}
		}
	}
	
	//OUTILS
	/*
	 * Renvoie un ensemble contenant les candidats de c.
	 */
	private Set<Integer> getCandidates(CellModel c) {
		assert c != null;
		Set<Integer> res = new HashSet<Integer>();
		for (int i = 1; i <= c.getCardinalCandidates(); ++i) {
			if (c.isCandidate(i)) {
				res.add(i);
			}
		}
		return res;
	}
}
