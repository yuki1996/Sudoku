package sudoku.view;

import java.awt.Dimension;

import javax.swing.Renderer;

/**
 * Interface responsable de l'affichage des symboles.
 * Doit répondre aux stimuli suivants:
 *  - survol de la souris
 *  - redimentionnement
 *  - niveau d'erreur
 */
interface SymboleRenderer extends Renderer {
	
	/**
	 * Cache le symbole indépendement de l'état du modèle.
	 */
	void hide(boolean bool);
	
	/**
	 * Redimensionne les symboles.
	 * Le redimensionnement doit donner un composant carré.
	 */
	void resize(Dimension dim);
	
	//void setSymbols(Collection<JComponent> symbols);
	
}