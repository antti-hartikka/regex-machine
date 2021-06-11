package matcherapp.ui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import matcherapp.domain.Matcher;

public class PerformanceTest {

    private final Matcher m = new Matcher();


    public LineChart<Number, Number> getChart() {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        x.setLabel("n");
        y.setLabel("time (ms)");
        LineChart<Number, Number> chart = new LineChart<>(x, y);
        chart.setTitle("Time consumption with (a)^n matching against (a)^n");
        chart.setLegendVisible(false);
        chart.setCreateSymbols(false);
        XYChart.Series<Number, Number> data = new XYChart.Series<>();
        long time;
        for (int n = 10000; n <= 10000000; n += 10000) {
            String text = getText(n);
            //String regex = getRegex(n);
            time = System.currentTimeMillis();
            m.match(text, text);
            //text.matches(text);
            time = System.currentTimeMillis() - time;
            data.getData().add(new XYChart.Data<>(n, time));
            System.out.println(n + " -- " + time);
            if (time > 10000) {
                break;
            }
        }
        chart.getData().add(data);
        return chart;
    }

    private String getText(int n) {
        String s = "aaaaaaaaaa";
        while(s.length() < n) {
            s += s;
        }
        return s.substring(0, n);
    }

    private String getRegex(int n) {
        String s = "a?a?a?a?a?a?";
        while (s.length() < n) {
            s += s;
        }
        return s.substring(0, n) + getText(n);
    }

}
