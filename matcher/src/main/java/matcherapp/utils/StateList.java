package matcherapp.utils;

import matcherapp.domain.State;

/**
 * List for states.
 */
public class StateList {

    State[] t;
    int size = 0;

    /**
     * Inits new table with length of 10.
     */
    public StateList() {
        t = new State[10];
    }

    /**
     * Adds state to the list.
     * @param state State to be added.
     */
    public void add(State state) {
        // check if table is full, and replace it with twice as big if necessary.
        if (size == t.length) {
            State[] copy = new State[t.length * 2];
            for (int i = 0; i < t.length; i++) {
                copy[i] = t[i];
            }
            t = copy;
        }
        t[size++] = state;
    }

    /**
     * Add multiple states to list.
     * @param t1 Table containing states to be added.
     */
    public void addAll(State[] t1) {
        for (State s : t1) {
            add(s);
        }
    }

    /**
     * To get all the states as a table.
     * @return Table containing all the states from the list.
     */
    public State[] getAll() {
        State[] copy = new State[size];
        for (int i = 0; i < size; i++) {
            copy[i] = t[i];
        }
        return copy;
    }

    /**
     * Makes the list empty.
     */
    public void clear() {
        size = 0;
    }

}
