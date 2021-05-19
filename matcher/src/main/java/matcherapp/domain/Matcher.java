package matcherapp.domain;

public class Matcher {

    /**
     * Matches text to regex pattern.
     * @param text Text to be matched.
     * @param pattern Regex pattern to match text against.
     * @return Returns true, if there is a match, false if not.
     */
    public boolean match(String text, String pattern) {
        Compiler compiler = new Compiler();
        Fragment fragment = compiler.compileNFAFragment(pattern);
        State start = fragment.getInput();
        NFASimulator simulator = new NFASimulator(start);
        return simulator.match(text);
    }

}
