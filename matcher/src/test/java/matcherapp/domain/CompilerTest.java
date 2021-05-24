package matcherapp.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompilerTest {

    Compiler c = new Compiler();

    @Test
    public void singleCharacteraddsProperStates() {
        State s = c.getFirstState("a");
        assertTrue(s.matchesCharacter('a'));
        assertTrue(s.getOut().isMatch());
    }

    @Test
    public void plusSyntaxAddsProperStates() {
        State s = c.getFirstState("a+");
        assertTrue(s.getOut().isSplit());
        assertEquals(s, s.getOut().getOut1());
        assertTrue(s.getOut().getOut().isMatch());
    }

    @Test
    public void questionMarkSyntaxAddsProperStates() {
        State s = c.getFirstState("a?");
        assertTrue(s.isSplit());
        assertTrue(s.getOut().isMatch());
        assertTrue(s.getOut1().matchesCharacter('a'));
        assertTrue(s.getOut1().getOut().isMatch());
    }

    @Test
    public void pipeSyntaxAddsProperStates() {
        State s = c.getFirstState("a|b");
        assertTrue(s.isSplit());
        assertTrue(s.getOut().getOut().isMatch());
        assertTrue(s.getOut1().getOut().isMatch());
    }
}