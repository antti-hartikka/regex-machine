package matcherapp.domain;

import matcherapp.utils.StateList;


/**
 * Simulates NFA to check if given input string is accepted or not.
 */
public class NFASimulator {

    private final StateList currentStates = new StateList();
    private final StateList nextStates = new StateList();
    private int listID;

    /**
     * Constructor, takes the first state of the NFA as a parameter.
     * @param startingState First state of the NFA.
     */
    public NFASimulator(State startingState) {
        listID = 1;
        addState(startingState);
        currentStates.addAll(nextStates.getAll());
        nextStates.clear();
    }

    /**
     * Matches given input string against NFA given in the constructor method.
     * Returns boolean value indicating if input string is accepted or not.
     * @param text String to be matched
     * @return True, if the string if accepted, false if not.
     */
    public boolean match(String text) {

        char[] chars = text.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            listID += 1;
            step(chars[i]);
            currentStates.clear();
            currentStates.addAll(nextStates.getAll());
            nextStates.clear();
        }

        return isMatch();
    }

    /**
     * Single step in the engine for one character in the input string.
     * Checks for every current state, if character is accepted in that state and adds output into next list if that is the case.
     * @param c Character to be checked.
     */
    private void step(char c) {
        for (State state : currentStates.getAll()) {
            if (state.matchesCharacter(c)) {
                addState(state.getOut());
            }
        }
    }

    /**
     * Adds state to the next step. Checks if state is null, in the list already, or split state, and handles them accordingly.
     * @param state State to be added to the next list.
     */
    private void addState(State state) {
        if (state == null || state.isInList(listID)) {
            return;
        }
        state.setLastList(listID);

        if (state.isSplit()) {
            addState(state.getOut());
            addState(state.getOut1());
        } else {
            nextStates.add(state);
        }
    }

    /**
     * Checks, if there is a state in the current states list, that is a final matching state.
     * @return Returns true, one is found, false if not.
     */
    private boolean isMatch() {
        for (State s : currentStates.getAll()) {
            if (s.isMatch()) {
                return true;
            }
        }
        return false;
    }

}
