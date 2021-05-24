package matcherapp.domain;

import java.util.ArrayList;
import java.util.Stack;

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
        Stack<Fragment> stack = new Stack<>();
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
                case '|':
                    i = handleVerticalBar(stack, chars, i);
                    break;
                case '?':
                    handleQuestionMark(stack);
                    break;
                case '+':
                    handlePlus(stack);
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
        State split = new State(null);
        split.setToSplit();
        split.setOut(f1.getInput());
        split.setOut1(f2.getInput());
        Fragment splitFragment = new Fragment(split);
        splitFragment.getOutputs().clear();
        for (State state : f1.getOutputs()) {
            splitFragment.pushOutput(state);
        }
        for (State state : f2.getOutputs()) {
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
    private int handleParenthesis(Stack<Fragment> stack, char[] chars,  int i) {
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
    private int handleVerticalBar(Stack<Fragment> stack, char[] chars, int i) {
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
     * @param stack
     */
    private void handleQuestionMark(Stack<Fragment> stack) {
        Fragment f = stack.pop();
        State split = new State(null);
        split.setToSplit();
        split.setOut1(f.getInput());
        f.pushOutput(split);
        f.setInput(split);
        stack.push(f);
    }

    /**
     * Handles + syntax: one or more
     * I don't know why, but this doesn't work, if this is first fragment in the NFA. We'll figure it out later.
     * @param stack Stack, where the fragments are
     */
    private void handlePlus(Stack<Fragment> stack) {
        Fragment f = stack.pop();
        ArrayList<State> splits = new ArrayList<>();
        for (State s : f.getOutputs()) {
            State split = new State(null);
            split.setToSplit();
            split.setOut1(f.getInput());
            splits.add(split);

            s.setOut(split);
        }
        f.getOutputs().clear();
        for (State split : splits) {
            f.pushOutput(split);
        }
        stack.push(f);
    }

    private void addFinalState(Fragment f) {
        State match = new State(null);
        match.setToMatch();
        for (State s : f.getOutputs()) {
            s.setOut(match);
        }
    }

    private void concatTwoFragmentsInStack(Stack<Fragment> stack) {
        Fragment f2 = stack.pop();
        Fragment f1 = stack.pop();
        stack.push(concatFragments(f1, f2));
    }

}
