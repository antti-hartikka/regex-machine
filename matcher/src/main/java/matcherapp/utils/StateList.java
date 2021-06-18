package matcherapp.utils;

import matcherapp.domain.State;

/**
 * Very simple list structure for State objects.
 */
public class StateList {

    private State[] table;
    /**
     * Acts also as a end pointer of this list.
     */
    private int size = 0;

    /**
     * Constructor for new StateList.
     */
    public StateList() {
        table = new State[10];
    }

    /**
     * Adds state to the list.
     * @param state State to be added.
     */
    public void add(State state) {
        // check if table is full, and replace it with twice as big if necessary.
        if (size == table.length) {
            State[] copy = new State[table.length * 2];
            for (int i = 0; i < table.length; i++) {
                copy[i] = table[i];
            }
            table = copy;
        }
        table[size++] = state;
    }

    /**
     * Adds multiple states to list.
     * @param states Table of State objects containing states to be added.
     */
    public void addAll(State[] states) {
        for (State s : states) {
            add(s);
        }
    }

    /**
     * Returns all the states as a table of State objects.
     * @return Table containing all the states from the list.
     */
    public State[] getAll() {
        State[] copy = new State[size];
        for (int i = 0; i < size; i++) {
            copy[i] = table[i];
        }
        return copy;
    }

    /**
     * Sets the end pointer of this list to index 0.
     */
    public void clear() {
        size = 0;
    }

}
