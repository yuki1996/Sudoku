package sudoku.model;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 * Type d'une cellule.
 * @inv
 *      0 <= getValue()
 *      getValue() != 0 <==> hasValue()
 *      getCardinalCandidates() > 0
 *      candidates().length == getCardinalCandidates()
 *      forall int i : canTakeValue(i) <==> candidates()[i - 1] && isModifiable()
 *      
 * @cons <pre>
 *     $DESC$ Une cellule modifiable sans valeur.
 *      
 *     $ARGS$ int cardinal
 *     
 *     $PRE$ cardinal > 0
 *      
 *     $POST$ 
 *     		getCardinalCandidates() == cardinal
 *     		! hasValue()
 *      	isModifiable()
 *         	forall int i : candidates()[i]
 *    </pre>
 *    
 * @cons <pre>
 *     $DESC$ Une cellule de valeur value.
 *     
 *     $ARGS$ int value, boolean modifiable, int cardinal
 *     
 *     $PRE$ 0 <= value && value <= cardinal && cardinal > 0
 *         
 *     $POST$
 *     	    getValue() == value
 *         	isModifiable() == modifiable
 *          forall int i : candidates()[i]
 *          getCardinalCandidates() == cardinal
 *    </pre>
 *    
 * @cons <pre>
 *     $DESC$ Une cellule modifiable sans valeur ayant un tableau de candidats.
 *     
 *     $ARGS$ boolean[] Candidates 
 *     
 *     $PRE$ candidates != null && candidates.length > 0
 *         
 *     $POST$ 
 *          ! hasValue()
 *          isModifiable()
 *         	candidates().equals(candidates)
 *          getCardinalCandidates() == Candidates.length
 *    </pre>
 */
public interface ICell extends Serializable, Cloneable  {
	
	// PROPRIETES
	String VALUE = "value";
	String CANDIDATE = "candidate";
	
	//REQUETES
	
	/**
	 * Renvoie le nombre de candidats.
	 */
	int getCardinalCandidates();
	
	/**
	 * Donne la valeur actuel de la cellule.
	 */
	int getValue();
	
	/**
	 * Renvoie vrai si la cellule a une valeur.
	 * Renvoie faux sinon.
	 */
	boolean hasValue();

	/**
	 * Renvoie vrai si n est une valeur candidate pour la cellule.
	 * Renvoie faux sinon.
	 * @pre
	 * 		0 < n <= getCardinalCandidates()
	 */
	boolean isCandidate(int n);
	
	/**
	 * Renvoie vrai si la case est modifiable.
	 */
	boolean isModifiable();
	
	/**
	 * Renvoie un clone de la cellule.
	 */
	Object clone();
	
	//COMMANDES
	/**
	 * Change la valeur de la cellule.
	 * @pre
	 * 		isModifiable()
	 *      0 < n <= getCardinalCandidates()
	 * @post
	 *      getValue() == n
	 */
	void setValue(int n);
	
	/**
	 * Met la valeur de la cellule à 0 si elle est modifiable.
	 * @pre
	 *      isModifiable()
	 * @post
	 *      getValue() == 0
	 */
	void removeValue();
	
	/**
	 * Ajoute n comme candidat si la cellule ne la possède pas déjà.
	 * @pre
	 *      0 < n <= getCardinalCandidates()
	 *      isModifiable()
	 * @post
	 * 		candidates()[n - 1]
	 */
	void addCandidate(int n);
	
	/**
	 * Supprime le candidat n si la cellule la possède.
	 * @pre
	 *      0 < n <= getCardinalCandidates()
	 *      isModifiable()
	 * @post
	 * 		! candidates()[n - 1]
	 */
	void removeCandidate(int n);
	
	/**
	 * Alterne la présence du candidat n.
	 * Supprime le candidat n s'il est présent, l'ajoute sinon.
	 * @pre
	 * 		0 < n <= getCandidates()
	 * 		isModifiable()
	 * @post
	 * 		candidates()[n - 1] == old !candidates()[n - 1]
	 */
	void toggleCandidate(int n);
	
	/**
	 * Modifie la modifiabilité de la cellule
	 * @post
	 * 		isModifiable() == bool
	 */
	void setModifiable(boolean bool);
	
	/**
	 * Met la cellule comme étant modifiable avec comme valeur nulle et possède tous les candidats
	 * @post
	 *     	    getValue() == 0
	 *         	isModifiable() == true
	 *         	forall int i : candidates()[i]
	 */
	void reset();
	
	void addPropertyChangeListener(String property, PropertyChangeListener l);
}
