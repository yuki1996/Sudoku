package sudoku.model.history;

import java.util.ArrayDeque;

import util.Contract;

public class StdHistory<E> implements History<E> {

    // ATTRIBUTS
    
    private final int maxHeight;
    private ArrayDeque<E> previousStack;
    private ArrayDeque<E> forwardStack;
    
    // CONSTRUCTEURS
    
    public StdHistory(int maxHeight) {
        Contract.checkCondition(maxHeight > 0);
        this.maxHeight = maxHeight;
        previousStack = new ArrayDeque<E>(maxHeight);
        forwardStack = new ArrayDeque<E>(maxHeight);
    }
    
    // REQUETES
    
    public int getMaxHeight() {
        return maxHeight;
    }

    public int getCurrentPosition() {
        return previousStack.size();
    }

    public E getCurrentElement() {
        Contract.checkCondition(getCurrentPosition() > 0);
        return previousStack.element();
    }

    public int getEndPosition() {
        return previousStack.size() + forwardStack.size();
    }

    // COMMANDES
    
    public void add(E e) {
        Contract.checkCondition(e != null);
        forwardStack.clear();
        if (previousStack.size() == maxHeight) {
            previousStack.removeLast();
        }
        previousStack.push(e);
    }

    public void goForward() {
        Contract.checkCondition(getCurrentPosition() < getEndPosition());
        previousStack.push(forwardStack.pop());
    }

    public void goBackward() {
        Contract.checkCondition(getCurrentPosition() > 0);
        forwardStack.push(previousStack.pop());
    }
    
}
