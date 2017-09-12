package test;

import java.awt.Button;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.ProcessPCB;

public class Test2 extends Application{
    public void start(Stage primaryStage) throws Exception {
        Stage stage = new Stage();
        GridPane pane = new GridPane();
        
        Node node2 = new ProgressBar();
        for(int i = 0; i < 5; i++) {
            pane.addRow(i, new ProgressBar(),new ProgressBar(),new ProgressBar(),new ProgressBar());
        }
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        
    }
}
