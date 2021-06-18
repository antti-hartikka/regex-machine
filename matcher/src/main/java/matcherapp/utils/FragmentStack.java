package matcherapp.utils;

import matcherapp.domain.Fragment;

/**
 * Stack type data structure for fragments.
 */
public class FragmentStack {

    private Fragment[] table;
    private int size;

    /**
     * Constructor, inits new stack.
     */
    public FragmentStack() {
        table = new Fragment[10];
        size = 0;
    }

    /**
     * Adds given fragment to the stack.
     * @param fragment Fragment to be added.
     */
    public void push(Fragment fragment) {
        if (size == table.length) {
            Fragment[] copy = new Fragment[table.length * 2];
            for (int i = 0; i < table.length; i++) {
                copy[i] = table[i];
            }
            table = copy;
        }
        table[size++] = fragment;
    }

    /**
     * Returns top fragment from stack.
     * @return Fragment from the top of this stack.
     */
    public Fragment pop() {
        return table[(size--) - 1];
    }

    /**
     * Returns value describing number of fragments in this stack.
     * @return Size of the stack.
     */
    public int size() {
        return size;
    }
}
