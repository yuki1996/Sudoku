
/**
 * Type d'une cellule modifiable.
 * 
 * @inv 
 * 		0 <= getValue <= 9
 * 		getValue() != 0 => !possibilities()  
 * 
 */
interface ICell {
	
	/**
	 * Donne la valeur actuel de la cellule.
	 * 
	 */
	int getValue();
	
	/**
	 * Tableau des possibilités. 
	 * 
	 */
	boolean[] possibilities();
	
	/** 
	 * Fixe la valeur value à la valeur de la cellule.
	 * 
	 * @pre 
	 * 		1 <= value <= 9
	 * 
	 * @post
	 * 		getValue() == value
	 */
	void setValue(int value);
	
	/**
	 * Ajoute une nouvelle possibilitée 
	 * pour la valeur de la cellule.
	 * 
	 * @pre 
	 * 		value ne doit pas être déjà dans possibilities()
	 * 
	 * @post 
	 * 		possibilities() = possibilities().length + 1
	 * 		value est ajouté dans possibilities() 
	 * 
	 */
	void addpossibiliy(int value);
	
	/**
	 * Supprime une possibilitée pour la valeur de la cellule.
	 * 
	 * @pre 
	 * 		value doit être dans possibilities()
	 * 
	 * @post 
	 * 		possibilities() = possibilities().length + 1 
	 * 		value n'est plus dans possibilities() 
	 */
	void removepossibiliy(int value);
}
