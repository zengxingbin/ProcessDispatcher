package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.MainController;
import view.PopupController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public class Dispatcher extends Application {
    private Stage mainStage;
    private Scene mainScene;
    private Stage popupStage;
    private PopupController popupController;
    private MainController mainController;
    public Dispatcher() {
      //create popup Stage
        popupStage = new Stage();
        popupStage.setTitle("输入提示");
        popupStage.getIcons().add(new Image("/images/tip.png"));
        popupStage.setResizable(false);
        //create fxml loader to load the fxml file in order to generate the pane
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/PopupView.fxml"));
        Pane popupPane = null;
        try {
            popupPane = loader.load();
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("找不到文件");
        }
        //create scene with the popupPane;
        Scene popupScene = new Scene(popupPane);
        //set scene on the stage
        popupStage.setScene(popupScene);
        //gain the popup controller
        popupController = loader.getController();
        popupController.setDispatcher(this);
        //create another fxml loader to load the fxml file of mainview
        FXMLLoader loader1 = new FXMLLoader();
        loader1.setLocation(getClass().getResource("../view/MainView.fxml"));
        //load the mainview pane
        Pane mainViewPane = null;
        try {
            mainViewPane = loader1.load();
        }catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("找不到文件");
        }
        //create mainview scene
        mainScene = new Scene(mainViewPane);
        //get the main controller
        mainController = loader1.getController();
        mainController.setDispatcher(this);
        
    }
	@Override
	public void start(Stage mainStage) {
		try {
			/*BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());*/
		    mainStage.setTitle("进程调度器");
		    mainStage.getIcons().add(new Image("images/scheduler.png"));
		    mainStage.setResizable(false);
			mainStage.setScene(mainScene);
			mainStage.show();
			popupStage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    public static void main(String[] args) {
		launch(args);
		
	}
	
    public Stage getMainStage() {
        return mainStage;
    }
    public Stage getPopupStage() {
        return popupStage;
    }
    public PopupController getPopupController() {
        return popupController;
    }
    public MainController getMainController() {
        return mainController;
    }
	
}
