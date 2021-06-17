package matcherapp;

import matcherapp.ui.MatcherUi;
import matcherapp.ui.PerformanceTest;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("test? (y/n)");
        if (new Scanner(System.in).nextLine().equals("y")) {
            PerformanceTest.main(args);
        }
        MatcherUi.main(args);
    }
}