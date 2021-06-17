package matcherapp.domain;

import matcherapp.utils.CharacterSet;
import matcherapp.utils.FragmentStack;
import matcherapp.utils.StateList;

import java.util.regex.PatternSyntaxException;

/**
 * Manages compiling process of the regex to NFA.
 */
public class Compiler {

    /**
     * Returns the first state of the compiled NFA to be used in the NFASimulator.
     * @param regex Regex pattern to be compiled.
     * @return First state of compiled NFA.
     * @throws PatternSyntaxException if syntax error is detected.
     */
    public State getFirstState(String regex) throws PatternSyntaxException {
        Fragment f = compileNFAFragment(regex);
        addFinalState(f);
        return f.getInput();
    }

    /**
     * Compiles NFA fragment from input string.
     * @param regex Regex pattern to be compiled into a NFA fragment.
     * @return Compiled fragment.
     */
    private Fragment compileNFAFragment(String regex) throws PatternSyntaxException {
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
                case '{':
                    i = handleBraces(stack, chars, i);
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
     * Handles [abc-e] syntax, single character: a, b, or all characters between c and e.
     * @param stack Stack containing compiled fragments.
     * @param chars Regex pattern as character array.
     * @param i Index to keep track where we are going.
     * @return Index to keep track where to continue.
     */
    private int handleBrackets(FragmentStack stack, char[] chars, int i) throws PatternSyntaxException {
        CharacterSet set = new CharacterSet();
        i++;
        char c = chars[i];
        while (c != ']') {

            // if c is escape char
            if (c == '\\') {
                i++;
                set.add(chars[i]);

                // if c is "-", it is preceded with char (is in set), and in "a-b" syntax a comes before b.
            } else if (c == '-' && !set.isEmpty() && chars[i + 1] - chars[i - 1] > 0) {

                // add all characters between two to the set
                for (int j = chars[i - 1] + 1; j <= chars[i + 1]; j++) {
                    set.add((char) j);
                }
                i++;
            } else {
                set.add(c);
            }
            i++;

            // handle syntax error
            if (i == chars.length) {
                handleSyntaxError(chars, -1, "']' was not found");
            }
            c = chars[i];
        }
        stack.push(new Fragment(new State(set)));
        return i;
    }

    /**
     * Returns two fragments concatenated as one.
     * @param f1 First fragment.
     * @param f2 Second fragment.
     * @return Fragment, where input is from first parameter, and output is from second parameter.
     */
    private Fragment concatFragments(Fragment f1, Fragment f2) {
        f1.setOutputStates(f2.getInput());
        f2.setInput(f1.getInput());
        return f2;
    }

    /**
     * Splits two fragments into one fragment with two branches.
     * @param f1 First fragment.
     * @param f2 Second fragment.
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
     * @param stack Stack containing compiled fragments.
     * @param chars Character array containing the regex pattern.
     * @param i Index to keep track, where we are at the character array.
     * @return Index to keep track, where to continue.
     */
    private int handleParenthesis(FragmentStack stack, char[] chars,  int i) throws PatternSyntaxException {
        String s = "";
        int parenthesesCounter = 0;
        i++;
        if (i == chars.length) {
            handleSyntaxError(chars, i, "nothing after '('");
        }
        char c = chars[i];
        while (c != ')' || parenthesesCounter != 0) {
            if (c == '(') {
                parenthesesCounter++;
            } else if (c == ')') {
                parenthesesCounter--;
            }
            s += c;
            i++;

            if (i == chars.length) {
                handleSyntaxError(chars, -1, "Couldn't find matching ')'");
            }
            c = chars[i];
        }
        Fragment f = compileNFAFragment(s);
        stack.push(f);
        return i;
    }

    /**
     * Handles | syntax: a|b -> a or b.
     * @param stack Stack containing compiled fragments.
     * @param chars Char array from compiler.
     * @param i Index to keep track, where we are at the char array.
     * @return Index to keep track, where to continue after calling this method.
     */
    private int handleVerticalBar(FragmentStack stack, char[] chars, int i) throws PatternSyntaxException {
        if (i == 0 || chars.length == i + 1) {
            handleSyntaxError(chars, i, "Found 'a|b' syntax without 'a' or 'b'");
        }
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
     * @param stack Stack containing compiled fragments.
     */
    private void handleQuestionMark(FragmentStack stack) throws PatternSyntaxException {
        if (stack.size() == 0) {
            throw new PatternSyntaxException("found '?' syntax without context", "", -1);
        }
        Fragment f = stack.pop();
        State split = newSplit(f.getInput());
        f.pushOutput(split);
        f.setInput(split);
        stack.push(f);
    }

    /**
     * Handles + syntax: one or more
     * @param stack Stack containing compiled fragments.
     */
    private void handlePlus(FragmentStack stack) throws PatternSyntaxException {
        if (stack.size() == 0) {
            throw new PatternSyntaxException("found '+' syntax without context", "", -1);
        }
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
     * @param stack Stack containing compiled fragments.
     */
    private void handleStar(FragmentStack stack) throws PatternSyntaxException {
        if (stack.size() == 0) {
            throw new PatternSyntaxException("found '*' syntax without context", "", -1);
        }
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
     * @param stack Stack containing compiled fragments.
     */
    private void concatTwoFragmentsInStack(FragmentStack stack) {
        Fragment f2 = stack.pop();
        Fragment f1 = stack.pop();
        stack.push(concatFragments(f1, f2));
    }

    /**
     * Creates new split state with one output routed.
     * @param out1 State to route one end of the split (out1).
     * @return New state set to split and given output routed.
     */
    private State newSplit(State out1) {
        State split = new State(null);
        split.setToSplit();
        split.setOut1(out1);
        return split;
    }

    /**
     * Handles {a,b} syntax, from a to b times. Also {a}, {a,} or {,b} is possible.
     * @param stack Stack containing compiled fragments.
     * @param chars Character array containing regex pattern.
     * @param i Index to keep track, where we are at the char array.
     * @return Index to keep track, where to continue.
     */
    private int handleBraces(FragmentStack stack, char[] chars, int i) {
        if (stack.size() == 0) {
            throw new PatternSyntaxException("found '{a,b}' syntax without context", "", -1);
        }
        int[] quantifiers = getQuantifiers(chars, i);
        int min = quantifiers[0];
        int max = quantifiers[1];

        String pattern = getPrevRegex(chars, i - 1);

        if (min == -1) {
            Fragment f = stack.pop();
            for (int j = 1; j < max; j++) {
                f = concatFragments(f, compileNFAFragment(pattern));
            }
            stack.push(f);
        } else {
            parseMinMaxQuantifiers(stack, pattern, min, max);
        }

        while (chars[i] != '}') {
            i++;
        }
        return i;
    }

    /**
     * Parses single fragment that matches {min,max} quantifiers and pushes it to the stack given.
     * @param stack Stack containing compiled fragments.
     * @param pattern Regex pattern to be used compiling fragments.
     * @param min Min amount pattern should be repeated.
     * @param max Max amount pattern should be repeated.
     */
    private void parseMinMaxQuantifiers(FragmentStack stack, String pattern, int min, int max) {
        Fragment f = stack.pop();
        if (min > 0) {
            for (int j = 1; j < min; j++) {
                f = concatFragments(f, compileNFAFragment(pattern));
            }
        } else {
            State split = newSplit(f.getInput());
            f.pushOutput(split);
            f.setInput(split);
            min++;
        }
        if (max == -1) {
            Fragment f1 = compileNFAFragment(pattern);

            State split1 = newSplit(f1.getInput());
            for (State s : f1.getOutputs().getAll()) {
                s.setOut(split1);
            }
            f1.getOutputs().clear();
            f1.pushOutput(split1);

            State split2 = newSplit(f1.getInput());
            f1.pushOutput(split2);
            f1.setInput(split2);

            f = concatFragments(f, f1);
        } else {
            for (int j = min; j < max; j++) {
                Fragment f1 = compileNFAFragment(pattern);
                State split = newSplit(f1.getInput());
                f1.pushOutput(split);
                f1.setInput(split);
                f = concatFragments(f, f1);
            }
        }
        stack.push(f);
    }

    /**
     * Finds previous regex pattern that forms single fragment.
     * @param chars Character array containing regex pattern.
     * @param i Index pointing to the last character of the regex pattern before quantifiers.
     * @return String containing previous regex fragment pattern.
     */
    private String getPrevRegex(char[] chars, int i) {
        String s = "";
        char c = chars[i];
        if (i == 0) {
            return s + c;
        }
        if (c != ')' && c != ']' && chars[i - 1] != '\\') {
            return s + c;
        } else if (c == ')') {
            return handleBracketsFromBehind(s, chars, i, '(', ')');
        } else if (c == ']') {
            return handleBracketsFromBehind(s, chars, i, '[', ']');
        }
        return s;
    }

    /**
     * Handles different brackets when tracing previous fragment from regex pattern.
     * @param s String to be modified.
     * @param chars Characters containing all regex.
     * @param i Index to start from.
     * @param open Open bracket character, usually ( or [.
     * @param close Closing bracket character, usually ) or ].
     * @return Index to keep track on progress.
     */
    private String handleBracketsFromBehind(String s, char[] chars, int i, char open, char close) {
        int counter = 0;
        char c = chars[i--];
        while (c != open || counter != 1) {
            if (c == close) {
                counter++;
            } else if (c == open) {
                counter--;
            }
            s = c + s;
            c = chars[i--];
        }
        s = c + s;
        return s;
    }

    /**
     * Gets min and max value from {a,b} syntax.
     * @param chars Characters containing all regex.
     * @param i Index pointing to { character
     * @return Array, where min value is in index 0, and max value is in index 1.
     */
    private int[] getQuantifiers(char[] chars, int i) {
        int min = -1;
        int max = -1;
        String extractedValue = "";

        i++;
        char c = chars[i];
        while (c != '}') {
            if (c == ',') {
                if (extractedValue.length() == 0) {
                    min = 0;
                } else {
                    min = Integer.parseInt(extractedValue);
                    extractedValue = "";
                }
            } else {
                extractedValue += c;
            }
            i++;
            if (i == chars.length) {
                handleSyntaxError(chars, -1, "'}' not found");
            }
            c = chars[i];
        }
        if (min == -1) {
            return new int[]{-1, Integer.parseInt(extractedValue)};
        }
        if (extractedValue.length() > 0) {
            max = Integer.parseInt(extractedValue);
        }

        return new int[]{min, max};
    }

    /**
     * Throws PatternSyntaxException with given parameters.
     * @param chars Character array containing the regex pattern, null if not known.
     * @param i Index to point syntax error location, -1 if not known
     * @param description Description to clarify the nature of exception.
     */
    private void handleSyntaxError(char[] chars, int i, String description) throws PatternSyntaxException {
        String regex = "";
        for (char c1 : chars) {
            regex += c1;
        }
        throw new PatternSyntaxException(description, regex, i);
    }

}
