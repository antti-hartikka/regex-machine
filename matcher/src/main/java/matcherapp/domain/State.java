package matcherapp.domain;

import matcherapp.utils.CharacterSet;

/**
 * Represents a single state in NFA. State can function also as a split or final match, and in these
 * cases transition doesn't consume character. If state consumes a character, accepted characters are in CharacterSet class.
 */
public class State {

    private CharacterSet set;
    private char acceptedChar;
    private State out;
    private State out1;
    private int lastList;
    private boolean isMatch = false;
    private boolean isSplit = false;

    /**
     * Constructor, takes CharacterSet containing accepted characters as a parameter,
     * but can be null if state functions as a split or a match.
     * @param set CharacterSet containing accepted characters, can be null if acting as a split or a match.
     */
    public State(CharacterSet set) {
        this.set = set;
    }

    /**
     * Constructor, takes single character as a parameter to be only character accepted in this state.
     * @param character Only character accepted in the state being constructed.
     */
    public State(char character) {
        acceptedChar = character;
    }

    /**
     * Returns boolean describing if given character is accepted by this state.
     * @param character Character to be matched against this state.
     * @return
     */
    public boolean matchesCharacter(char character) {
        if (set == null) {
            return character == acceptedChar;
        }
        return set.contains(character);
    }

    public State getOut() {
        return out;
    }

    public State getOut1() {
        return out1;
    }

    public void setOut(State out) {
        this.out = out;
    }

    public void setOut1(State out1) {
        this.out1 = out1;
    }

    public void setToSplit() {
        isSplit = true;
    }

    public void setToMatch() {
        isMatch = true;
    }

    public boolean isSplit() {
        return isSplit;
    }

    public boolean isMatch() {
        return isMatch;
    }

    /**
     * Checks if this state is already added to the next step.
     * @param listID List id value to be checked if it equals this states last list value.
     * @return
     */
    public boolean isInList(int listID) {
        return listID == lastList;
    }

    /**
     * Sets last list value to one given in the parameter.
     * @param lastList New value for setting the last list value.
     */
    public void setLastList(int lastList) {
        this.lastList = lastList;
    }

}
