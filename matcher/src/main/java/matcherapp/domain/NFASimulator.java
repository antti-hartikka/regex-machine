package matcherapp.domain;

import matcherapp.utils.StateList;

import java.util.Arrays;

/**
 * Simulates NFA and find out if we get a match with given String
 */
public class NFASimulator {

    private final StateList currentStates = new StateList();
    private final StateList nextStates = new StateList();
    private int listID;

    /**
     * Constructor
     * @param startingState First state to begin with
     */
    public NFASimulator(State startingState) {
        listID = (int) System.nanoTime() % Integer.MAX_VALUE;
        addState(startingState);
        currentStates.addAll(nextStates.getAll());
        nextStates.clear();
    }

    /**
     * Handles matching: moves to next step and rotates lists of states.
     * @param text String to be matched to regex pattern
     * @return True, if there is a match, false if not.
     */
    public boolean match(String text) {

        char[] chars = text.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            System.out.println(i);
            listID = (int) System.nanoTime() % Integer.MAX_VALUE;
            step(chars[i]);
            currentStates.clear();
            currentStates.addAll(nextStates.getAll());
            nextStates.clear();
        }

        return isMatch();
    }

    /**
     * Single step for one character in the input string to be matched.
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
     * Adds states to be handled in next step. Checks if state is null, in the list already, or split state.
     * @param state State to be added.
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
     * Finds final matches from state lists.
     * @return Returns true, if we hit the goal, false if not.
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
