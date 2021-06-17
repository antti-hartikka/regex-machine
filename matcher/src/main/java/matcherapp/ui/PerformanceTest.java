package matcherapp.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import matcherapp.domain.Matcher;

import java.util.Scanner;

public class PerformanceTest extends Application {

    private final Matcher m = new Matcher();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("max n:");
        int n = Integer.parseInt(scanner.nextLine());
        System.out.println("step size:");
        int step = Integer.parseInt(scanner.nextLine());
        System.out.println("text to be matched (repeated n times):");
        String text = scanner.nextLine();
        System.out.println("first part of regex (repeated n times):");
        String pattern1 = scanner.nextLine();
        System.out.println("second part of regex (repeated n times):");
        String pattern2 = scanner.nextLine();
        System.out.println("third part of regex (not repeated):");
        String pattern3 = scanner.nextLine();
        System.out.println("max time for one match in milliseconds:");
        int timeout = Integer.parseInt(scanner.nextLine());

        LineChart<Number, Number> chart = getChart();
        HBox hBox = new HBox();
        hBox.getChildren().add(chart);
        Scene scene = new Scene(hBox);
        stage.setTitle("testing");
        stage.setScene(scene);
        stage.show();

        XYChart.Series<Number, Number> data = new XYChart.Series<>();
        chart.getData().add(data);
        long time;
        String textRepeated, regex;
        for (int i = step; i <= n; i += step) {
            textRepeated = text.repeat(i);
            regex = pattern1.repeat(i) + pattern2.repeat(i) + pattern3;
            time = System.currentTimeMillis();
            m.match(textRepeated, regex);
            time = System.currentTimeMillis() - time;
            data.getData().add(new XYChart.Data<>(i, time));
            if (time > timeout) {
                break;
            }
        }
    }

    public LineChart<Number, Number> getChart() {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        x.setLabel("n");
        y.setLabel("time (ms)");
        LineChart<Number, Number> chart = new LineChart<>(x, y);
        chart.setLegendVisible(false);
        chart.setCreateSymbols(false);
        chart.setAnimated(true);
        return chart;
    }
}
