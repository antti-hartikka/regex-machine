package matcherapp.utils;

/**
 * Class to represent a single Node in CharacterSet class.
 */
public class CharacterNode {

    private final char c;
    private CharacterNode next;

    public CharacterNode(char c) {
        this.c = c;
    }

    public void setNext(CharacterNode next) {
        this.next = next;
    }

    public CharacterNode getNext() {
        return next;
    }

    public char getChar() {
        return c;
    }

}
