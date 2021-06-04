package matcherapp.utils;

/**
 * Hash set class for characters.
 */
public class CharacterSet {

    private CharacterNode[] t;
    private int size = 0;

    /**
     * Initializes new hash set.
     */
    public CharacterSet() {
        t = new CharacterNode[17];
    }

    /**
     * Adds a single character to the hash set.
     * @param c Character to be added.
     */
    public void add(char c) {
        int i = hash(c);
        CharacterNode node = new CharacterNode(c);
        if (t[i] != null) {
            node.setNext(t[i]);
        }
        t[i] = node;
        size++;

        if ((double) size / t.length > 0.75) {
            rehash();
        }
    }

    /**
     * Checks if character is in a set or not.
     * @param c Character to be checked.
     * @return True, if set contains specified character, false if not.
     */
    public boolean contains(char c) {
        CharacterNode node = t[hash(c)];
        while (node != null) {
            if (node.getChar() == c) {
                return true;
            }
            node = node.getNext();
        }
        return false;
    }

    /**
     * Checks if this hash set is empty.
     * @return True, if set is empty, false if not.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Calculates hash value for character.
     * @param c Character, to calculate hash value for.
     * @return Returns calculated value.
     */
    private int hash(char c) {
        return c % t.length;
    }

    /**
     * Replaces table with twice as big as the old one, and transfers all nodes to it.
     */
    private void rehash() {
        CharacterNode[] original = t;
        t = new CharacterNode[original.length * 2];
        size = 0;
        for (CharacterNode node : original) {
            while (node != null) {
                add(node.getChar());
                node = node.getNext();
            }
        }
    }

}
