package matcherapp.domain;

import java.util.regex.PatternSyntaxException;

/**
 * Class for handling the matching procedure.
 */
public class Matcher {

    /**
     * Matches text to regex pattern and returns result as a boolean.
     * @param text Text to be matched.
     * @param pattern Regex pattern to match text against.
     * @return Returns true, if there is a match, false if not.
     */
    public boolean match(String text, String pattern) throws PatternSyntaxException {
        if (text.isEmpty() || pattern.isEmpty()) {
            throw new PatternSyntaxException("Empty text or pattern", "", -1);
        }
        Compiler compiler = new Compiler();
        State firstState = compiler.getFirstState(pattern);
        NFASimulator simulator = new NFASimulator(firstState);
        return simulator.match(text);
    }

}
