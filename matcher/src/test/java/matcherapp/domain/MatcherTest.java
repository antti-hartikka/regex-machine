package matcherapp.domain;

import org.junit.Test;

import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.*;

public class MatcherTest {

    Matcher m = new Matcher();

    @Test
    public void matchingWorksWithSimpleString() {
        assertTrue(m.match("abba", "abba"));
    }

    @Test
    public void matchingWorksWithPipeSyntax() {
        assertTrue(m.match("abcdfg", "abcd|efg"));
        assertTrue(m.match("abcefg", "abcd|efg"));
    }

    @Test
    public void matchingWorksWithPipesAndParenthesis() {
        assertTrue(m.match("abcdfg", "ab(cd)|efg"));
        assertTrue(m.match("abefg", "ab(cd)|efg"));

        assertTrue(m.match("abcdg", "ab(cd)|(ef)g"));
        assertTrue(m.match("abefg", "ab(cd)|(ef)g"));

        assertTrue(m.match("abcdf", "ab((cd)|ef)|g"));
        assertTrue(m.match("abef", "ab((cd)|ef)|g"));
        assertTrue(m.match("abg", "ab((cd)|ef)|g"));
    }

    @Test
    public void matchingWorksWithQuestionMark() {
        assertTrue(m.match("abcd", "abcde?"));
        assertTrue(m.match("abcde", "abcde?"));
    }

    @Test
    public void matchingWorksWithPlusSyntax() {
        assertTrue(m.match("aaaaaaaa", "a+"));
        assertTrue(m.match("baaaaaaaa", "ba+"));
    }

    @Test
    public void matchingWorksWithBraceSyntax() {
        assertTrue(m.match("aaaaa", "a{5}"));
        assertTrue(m.match("abccc", "abc{3}"));
        assertTrue(m.match("abccc", "abc{1,5}"));
        assertTrue(m.match("abccc", "abc{,5}"));
        assertTrue(m.match("abccc", "abc{2,}"));
        assertTrue(m.match("abcabcdefghij", "(abc){,3}[d-j]{4,10}"));
    }

    @Test
    public void escapeCharacterEscapes() {
        assertTrue(m.match("(\\)?*{{}}[]", "\\(\\\\\\)\\?\\*[\\{\\}]{4}\\[\\]"));
    }

    @Test(expected = PatternSyntaxException.class)
    public void syntaxErrorThrowsExceptionWithBrackets() {
        m.match("abba", "[a-z+");
    }

    @Test(expected = PatternSyntaxException.class)
    public void syntaxErrorThrowsExceptionWithBraces() {
        m.match("abba", "ab{2a");
    }

    @Test(expected = PatternSyntaxException.class)
    public void syntaxErrorThrowsExceptionWithParenthesis() {
        m.match("abba", "(abba)|(abbb");
    }
}