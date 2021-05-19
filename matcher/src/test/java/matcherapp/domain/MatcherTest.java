package matcherapp.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatcherTest {

    Matcher m = new Matcher();

    @Test
    public void matchWorksWithDifferentInputs() {
        assertTrue(m.match("abba", "abba"));

        assertTrue(m.match("abcdfg", "abcd|efg"));
        assertTrue(m.match("abcefg", "abcd|efg"));

        assertTrue(m.match("abcdfg", "ab(cd)|efg"));
        assertTrue(m.match("abefg", "ab(cd)|efg"));

        assertTrue(m.match("abcdg", "ab(cd)|(ef)g"));
        assertTrue(m.match("abefg", "ab(cd)|(ef)g"));

        assertTrue(m.match("abcdf", "ab((cd)|ef)|g"));
        assertTrue(m.match("abef", "ab((cd)|ef)|g"));
        assertTrue(m.match("abg", "ab((cd)|ef)|g"));

        assertTrue(m.match("abcd", "abcde?"));
        assertTrue(m.match("abcde", "abcde?"));

        assertTrue(m.match("baaaaaaaa", "ba+"));
    }
}