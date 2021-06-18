package matcherapp.utils;

/**
 * Hash set type data structure for characters.
 */
public class CharacterSet {

    private CharacterNode[] table;
    private int size = 0;

    /**
     * Constructor for new hash set for characters.
     */
    public CharacterSet() {
        table = new CharacterNode[17];
    }

    /**
     * Adds a single character to the set.
     * @param c Character to be added.
     */
    public void add(char c) {
        int i = index(c);
        CharacterNode node = new CharacterNode(c);
        if (table[i] != null) {
            node.setNext(table[i]);
        }
        table[i] = node;
        size++;

        if ((double) size / table.length > 0.75) {
            rehash();
        }
    }

    /**
     * Checks if character is in set or not.
     * @param c Character to be looked at.
     * @return True, if set contains specified character, false if not.
     */
    public boolean contains(char c) {
        CharacterNode node = table[index(c)];
        while (node != null) {
            if (node.getChar() == c) {
                return true;
            }
            node = node.getNext();
        }
        return false;
    }

    /**
     * Checks if this set is empty.
     * @return True, if set is empty, false if not.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Calculates characters index in table.
     * @param c Character, to calculate character for.
     * @return Returns calculated value.
     */
    private int index(char c) {
        return c % table.length;
    }

    /**
     * Replaces table with new table twice as big as the old one, and transfers all nodes to it.
     */
    private void rehash() {
        CharacterNode[] original = table;
        table = new CharacterNode[original.length * 2];
        size = 0;
        for (CharacterNode node : original) {
            while (node != null) {
                add(node.getChar());
                node = node.getNext();
            }
        }
    }

}
