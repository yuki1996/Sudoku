package sudoku.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;

import util.Contract;

public class StdCellModel implements CellModel {
	//ATTRIBUTS
	private int value;
	private boolean modifiable;
	private boolean[] candidates;
	
	private transient PropertyChangeSupport propertySupport;
	
	//CONSTRUCTEURS
	
	public StdCellModel(int cardinal) {		
		this(0, true, cardinal);
	}
	
	public StdCellModel(int value, boolean modifiable, int cardinal) {
		Contract.checkCondition(cardinal > 0, "cardinal doit être strictement positif.");
		Contract.checkCondition(0 <= value && value <= cardinal, 
				"value doit être strictement positif.");
		this.value = value;
		this.modifiable = modifiable;
		candidates = new boolean[cardinal];
		for (int i = 0; i < cardinal; ++i) {
			candidates[i] = true;
		}
		
		propertySupport = new PropertyChangeSupport(this);
	}
	
	//REQUÊTES

	public int getCardinalCandidates() {
		return candidates.length;
	}
	
	@Override
	public int getValue() {
		return value;
	}

	@Override
	public boolean hasValue() {
		return value != 0;
	}

	@Override
	public boolean isModifiable() {
		return modifiable;
	}

	@Override
	public boolean isCandidate(int n) {
		Contract.checkCondition(isValid(n));
		return candidates[n - 1];
	}
	
	public Object clone() {
		StdCellModel clone = null;
		try {
			clone = (StdCellModel) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError("Échec clonage");
		}
		clone.value = this.getValue();
		clone.modifiable = this.isModifiable();
		clone.candidates = this.candidates.clone();
		clone.propertySupport = new PropertyChangeSupport(clone);
		return clone;
		
	}
	
	//COMMANDES
	@Override
	public void setValue(int n) {
		Contract.checkCondition(isModifiable());
		Contract.checkCondition(isValid(n));
		
		int oldValue = value;
		value = n;
		propertySupport.firePropertyChange(VALUE, oldValue, value);
	}

	@Override
	public void removeValue() {
		Contract.checkCondition(modifiable);
		
		int oldValue = value;
		value = 0;
		propertySupport.firePropertyChange(VALUE, oldValue, value);
	}

	@Override
	public void addCandidate(int n) {
		Contract.checkCondition(isValid(n));
		Contract.checkCondition(modifiable);
		boolean oldCandidate = candidates[n - 1];
		candidates[n - 1] = true;
		propertySupport.fireIndexedPropertyChange(CANDIDATE, n,
				oldCandidate, candidates[n - 1]);
	}

	@Override
	public void removeCandidate(int n) {
		Contract.checkCondition(isValid(n));
		Contract.checkCondition(modifiable);
		boolean oldCandidate = candidates[n - 1];
		candidates[n - 1] = false;
		propertySupport.fireIndexedPropertyChange(CANDIDATE, n,
				oldCandidate, candidates[n - 1]);
	}
	
	@Override
	public void toggleCandidate(int n) {
		Contract.checkCondition(isValid(n));
		Contract.checkCondition(modifiable);
		boolean oldCandidate = candidates[n - 1];
		candidates[n - 1] = !candidates[n - 1];
		propertySupport.fireIndexedPropertyChange(CANDIDATE, n,
				oldCandidate, candidates[n - 1]);
	}
	
	public void setModifiable(boolean bool) {
		modifiable = bool;
	}
	
	public void reset() {
		for (int i = 1; i <= candidates.length; i++) {
			addCandidate(i);
		}
		removeValue();
		modifiable = true;
	}
	
	public void addPropertyChangeListener(String property, PropertyChangeListener l) {
		Contract.checkCondition(l != null, "l'écouteur est null");
		
		propertySupport.addPropertyChangeListener(property, l);
	}
	
	// OUTILS
	private boolean isValid(int value) {
		return 0 < value && value <= getCardinalCandidates();
	}

}
