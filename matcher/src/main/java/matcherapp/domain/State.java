package matcherapp.domain;

import matcherapp.utils.CharacterSet;

/**
 * Represents a single state in NFA. State can funcion as a split or final match, and in these
 * cases transition doesn't consume character. If state consumes a character, accepted characters are in HashSet set.
 */
public class State {

    private CharacterSet set;
    private State out;
    private State out1;
    private int lastList;
    private boolean isMatch = false;
    private boolean isSplit = false;

    public State(CharacterSet set) {
        this.set = set;
    }

    public State(char c) {
        set = new CharacterSet();
        set.add(c);
    }

    public boolean matchesCharacter(char c) {
        if (set == null) {
            return false;
        }
        return set.contains(c);
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

    public boolean isInList(int listID) {
        return listID == lastList;
    }

    public void setLastList(int lastList) {
        this.lastList = lastList;
    }

    public CharacterSet getSet() {
        return set;
    }

    public void setSet(CharacterSet set) {
        this.set = set;
    }
}
