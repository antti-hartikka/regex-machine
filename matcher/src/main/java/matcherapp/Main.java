package matcherapp;

import matcherapp.ui.MatcherUi;
import matcherapp.ui.PerformanceTest;

import java.util.Scanner;

/**
 * Main class, used to launch user interfaces.
 */
public class Main {

    /**
     * Asks user if performance test ui or matcher app is launched.
     * @param args args
     */
    public static void main(String[] args) {
        System.out.println("launch performance test (matcher app is launched otherwise)? (y/n)");
        if (new Scanner(System.in).nextLine().equals("y")) {
            PerformanceTest.main(args);
        }
        MatcherUi.main(args);
    }
}