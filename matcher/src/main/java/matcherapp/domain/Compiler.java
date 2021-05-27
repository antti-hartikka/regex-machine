package matcherapp.domain;

import matcherapp.utils.CharacterSet;
import matcherapp.utils.FragmentStack;
import matcherapp.utils.StateList;

/**
 * Manages compiling of the regex to NFA.
 */
public class Compiler {

    public State getFirstState(String regex) {
        Fragment f = compileNFAFragment(regex);
        addFinalState(f);
        return f.getInput();
    }

    /**
     * Compiles NFA fragment from input string
     * @param regex Regex pattern to transform into NFA fragment
     * @return Compiled fragment
     */
    private Fragment compileNFAFragment(String regex) {
        FragmentStack stack = new FragmentStack();
        char[] chars = regex.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            // lets concatenate previous fragments if there is no reason not to
            if (stack.size() >= 2 && !(c == '+' || c == '{' || c == '*' || c == '?' || c == '|')) {
                concatTwoFragmentsInStack(stack);
            }

            switch (c) {
                case '(':
                    i = handleParenthesis(stack, chars, i);
                    break;
                case '[':
                    i = handleBrackets(stack, chars, i);
                    break;
                case '|':
                    i = handleVerticalBar(stack, chars, i);
                    break;
                case '?':
                    handleQuestionMark(stack);
                    break;
                case '+':
                    handlePlus(stack);
                    break;
                case '*':
                    handleStar(stack);
                    break;
                case '\\':
                    i++;
                    stack.push(new Fragment(new State(chars[i])));
                    break;
                default:
                    stack.push(new Fragment(new State(c)));
            }
        }

        // lets concatenate all fragments if there are more than one
        while (stack.size() > 1) {
            concatTwoFragmentsInStack(stack);
        }

        return stack.pop();
    }

    /**
     * Handles [abcde] syntax, single character: a, b, c, d or e.
     * @param stack Stack of fragments
     * @param chars Regex pattern as char[]
     * @param i Index to keep track where we are going
     * @return Index back to caller.
     */
    private int handleBrackets(FragmentStack stack, char[] chars, int i) {
        CharacterSet set = new CharacterSet();
        i++;
        char c = chars[i];
        while (c != ']') {
            if (c == '\\') {
                i++;
                set.add(chars[i]);
            } else if (c == '-' && !set.isEmpty() && chars[i + 1] - chars[i - 1] > 0) {
                for (int j = chars[i - 1] + 1; j <= chars[i + 1]; j++) {
                    set.add((char) j);
                }
                i++;
            } else {
                set.add(c);
            }
            i++;
            c = chars[i];
        }
        stack.push(new Fragment(new State(set)));
        return i;
    }

    /**
     * Concatenates two fragments
     * @param f1 First fragment
     * @param f2 Second fragment
     * @return Fragment, where input is from first parameter, and output is from second parameter.
     */
    private Fragment concatFragments(Fragment f1, Fragment f2) {
        f1.setOutputStates(f2.getInput());
        f2.setInput(f1.getInput());
        return f2;
    }

    /**
     * Splits two fragments into two brances.
     * @param f1 First fragment
     * @param f2 Second fragment
     * @return Fragment with both fragments in separate branches.
     */
    private Fragment splitFragments(Fragment f1, Fragment f2) {
        State split = newSplit(f2.getInput());
        split.setOut(f1.getInput());
        Fragment splitFragment = new Fragment(split);
        splitFragment.getOutputs().clear();
        for (State state : f1.getOutputs().getAll()) {
            splitFragment.pushOutput(state);
        }
        for (State state : f2.getOutputs().getAll()) {
            splitFragment.pushOutput(state);
        }
        return splitFragment;
    }

    /**
     * Handles parenthesis (grouping): makes recursive call to compileNFAFragment to deal groups.
     * @param stack Stack containing fragments
     * @param chars Char array from compiler
     * @param i Index to know, where we are at the char array
     * @return Index, so the compiler knows where to continue
     */
    private int handleParenthesis(FragmentStack stack, char[] chars,  int i) {
        String s = "";
        int parenthesesCounter = 0;
        i++;
        char c = chars[i];
        while (c != ')' || parenthesesCounter != 0) {
            if (c == '(') {
                parenthesesCounter++;
            } else if (c == ')') {
                parenthesesCounter--;
            }
            s += c;
            i++;
            c = chars[i];
        }
        Fragment f = compileNFAFragment(s);
        stack.push(f);
        return i;
    }

    /**
     * Handles | syntax: a|b -> a or b
     * @param stack Stack containing fragments
     * @param chars Char array from compiler
     * @param i Index to know, where we are at the char array
     * @return Index, so the compiler knows where to continue
     */
    private int handleVerticalBar(FragmentStack stack, char[] chars, int i) {
        Fragment f1 = stack.pop();
        Fragment f2;
        i++;
        if (chars[i] != '(') {
            f2 = new Fragment(new State(chars[i]));
        } else {
            i = handleParenthesis(stack, chars, i);
            f2 = stack.pop();
        }
        stack.push(splitFragments(f1, f2));
        return i;
    }

    /**
     * Handles ? syntax: zero or one.
     * @param stack Stack containing fragments.
     */
    private void handleQuestionMark(FragmentStack stack) {
        Fragment f = stack.pop();
        State split = newSplit(f.getInput());
        f.pushOutput(split);
        f.setInput(split);
        stack.push(f);
    }

    /**
     * Handles + syntax: one or more
     * @param stack Stack containing fragments.
     */
    private void handlePlus(FragmentStack stack) {
        Fragment f = stack.pop();
        StateList splits = new StateList();
        for (State s : f.getOutputs().getAll()) {
            State split = newSplit(f.getInput());
            splits.add(split);

            s.setOut(split);
        }
        f.getOutputs().clear();
        for (State split : splits.getAll()) {
            f.pushOutput(split);
        }
        stack.push(f);
    }

    /**
     * Handles * syntax, zero or more.
     * @param stack Stack containing fragments.
     */
    private void handleStar(FragmentStack stack) {
        Fragment f = stack.pop();
        State split = newSplit(f.getInput());
        Fragment newFragment = new Fragment(split);
        for (State s : f.getOutputs().getAll()) {
            State split1 = newSplit(f.getInput());
            newFragment.pushOutput(split1);
            s.setOut(split1);
        }
        stack.push(newFragment);
    }

    /**
     * Creates final matching state to complete NFA and concatenates it to fragment given.
     * @param f Fragment to concat the final state
     */
    private void addFinalState(Fragment f) {
        State match = new State(null);
        match.setToMatch();
        for (State s : f.getOutputs().getAll()) {
            s.setOut(match);
        }
    }

    /**
     * Pops two fragments from given stack and pushes one concatenated fragment back to the stack.
     * @param stack Stack containing fragments.
     */
    private void concatTwoFragmentsInStack(FragmentStack stack) {
        Fragment f2 = stack.pop();
        Fragment f1 = stack.pop();
        stack.push(concatFragments(f1, f2));
    }

    /**
     * Creates new split state with one output routed.
     * @param out1 State to route one end of the split (out1)
     * @return New state set to split and given output routed.
     */
    private State newSplit(State out1) {
        State split = new State(null);
        split.setToSplit();
        split.setOut1(out1);
        return split;
    }

    /**
     * I will figure {a,b} syntax out maybe later.
     * @param stack
     * @param chars
     * @param i
     * @return
     */
    private int handleBraces(FragmentStack stack, char[] chars, int i) {
        int min = -1;
        int max = -1;
        String s = "";

        i++;
        char c = chars[i];
        while (c != '}') {
            if (c == ',') {
                if (s.length() == 0) {
                    min = 0;
                } else {
                    min = Integer.parseInt(s);
                    s = "";
                }
            } else {
                s += c;
            }
            i++;
            c = chars[i];
        }
        if (s.length() > 0) {
            max = Integer.parseInt(s);
        }

        return i;
    }

}
