package view;

import application.Dispatcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PopupController {
    @FXML
    private Label inputTip;
    @FXML
    private Label invalidInputTip;
    @FXML
    private TextField initProNum;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    private Dispatcher dispatcher;
    @FXML
    private void initialize() {
        //add focus listener to the initProNum textfield
        initProNum.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                invalidInputTip.setVisible(false);
                 
            }

        });
    }
    @FXML
    private void confirm() {
        int proNum;
        try {
            proNum = Integer.parseInt(initProNum.getText());
        }catch(NumberFormatException e) {
            System.out.println(e);
            invalidInputTip.setVisible(true);
            return;
        }
        if(proNum < 0 || proNum > 10) {
            invalidInputTip.setVisible(true);
            return;
        }
        dispatcher.getPopupStage().close();
    }
    @FXML
    private void cancel() {
        dispatcher.getPopupStage().close();
    }
    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    public Label getInvalidInputTip() {
        return invalidInputTip;
    }
    public void setInvalidInputTip(Label invalidInputTip) {
        this.invalidInputTip = invalidInputTip;
    }
    public TextField getInitProNum() {
        return initProNum;
    }
    public void setInitProNum(TextField initProNum) {
        this.initProNum = initProNum;
    }
    public Label getInputTip() {
        return inputTip;
    }
    public void setInputTip(Label inputTip) {
        this.inputTip = inputTip;
    }
    
    
}
