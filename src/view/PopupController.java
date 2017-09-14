package view;

import java.util.Random;

import application.Dispatcher;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.ProcessPCB;
import util.ProcessComparator;
/**
 * 
 * @author 曾幸彬(bingoo)
 * email:2045620125@qq.com
 * 2017年9月10日 下午1:08:41
 *
 */
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
    //judge the if popup pop up for the first time
    private boolean isFirstPopup = true;
    //get the number of process you want to create
    private int proNum;
    @FXML
    private void initialize() {
        // add focus listener to the initProNum textfield
        initProNum.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                invalidInputTip.setVisible(false);

            }

        });
        // confirm the initial number of process once the the key enter was
        // released
        initProNum.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    confirm();
                }
            }
        });
    }

    /**
     * confirm to generate several process
     */
    @FXML
    private void confirm() {
        try {
            proNum = Integer.parseInt(initProNum.getText());
        } catch (NumberFormatException e) {
            System.out.println(e);
            invalidInputTip.setVisible(true);
            return;
        }
        if (proNum < 0 || proNum > 10) {
            invalidInputTip.setVisible(true);
            return;
        }
        
        dispatcher.getPopupStage().close();
        dispatcher.getMainController().getReadyQueue().requestFocus();
        // dispatcher.getMainController().getReadyQueue().getSelectionModel().select(0);

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

    public int getProNum() {
        return proNum;
    }

    public void setProNum(int proNum) {
        this.proNum = proNum;
    }
    
}
