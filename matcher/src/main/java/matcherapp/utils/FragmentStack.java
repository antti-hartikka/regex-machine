package matcherapp.utils;

import matcherapp.domain.Fragment;

/**
 * Stack for fragments.
 */
public class FragmentStack {

    Fragment[] t;
    int size;

    /**
     * Inits new table with length of 10.
     */
    public FragmentStack() {
        t = new Fragment[10];
        size = 0;
    }

    /**
     * Adds fragment to the top of stack.
     * @param fragment Fragment to be added.
     */
    public void push(Fragment fragment) {
        if (size == t.length) {
            Fragment[] copy = new Fragment[t.length];
            for (int i = 0; i < t.length; i++) {
                copy[i] = t[i];
            }
            t = copy;
        }
        t[size++] = fragment;
    }

    /**
     * Pops top fragment from stack.
     * @return Fragment from stack.
     */
    public Fragment pop() {
        return t[(size--) - 1];
    }

    /**
     * Tells how many fragments are in the stack.
     * @return Size of the stack.
     */
    public int size() {
        return size;
    }
}
