package matcherapp.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import matcherapp.domain.Matcher;

/**
 * simple user interface
 */
public class MatcherUi extends Application {

    Matcher matcher = new Matcher();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(100, 100, 100, 100));

        HBox labels = new HBox();
        labels.setSpacing(200);
        labels.setPadding(new Insets(10,10,10,100));
        labels.getChildren().add(new Text("input"));
        labels.getChildren().add(new Text("pattern"));
        vBox.getChildren().add(labels);

        HBox textFields = new HBox();
        vBox.getChildren().add(textFields);
        textFields.setSpacing(100);
        textFields.setPadding(new Insets(0,50,50,50));
        TextField input = new TextField();
        TextField pattern = new TextField();
        textFields.getChildren().add(input);
        textFields.getChildren().add(pattern);
        Button button = new Button("match");
        textFields.getChildren().add(button);

        Text output = new Text(null);
        HBox textBox = new HBox();
        textBox.setPadding(new Insets(0, 0, 0, 200));
        textBox.getChildren().add(output);
        vBox.getChildren().add(textBox);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.setTitle("RegexMachine");
        stage.show();

        button.setOnMouseClicked(mouseEvent -> {
            long time = System.currentTimeMillis();
            boolean regexResult = matcher.match(input.getText(), pattern.getText());
            time = System.currentTimeMillis() - time;

            if (regexResult) {
                output.setFill(Color.GREEN);
                output.setText("we have a match! (" + time + " ms)");
            } else {
                output.setFill(Color.RED);
                output.setText("no match! (" + time + " ms)");
            }
        });
    }
}
