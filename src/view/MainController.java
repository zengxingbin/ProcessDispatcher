package view;

import application.Dispatcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.ProcessPCB;

public class MainController {
    private Dispatcher dispatcher;
    @FXML
    private TextField userName;
    @FXML
    private ComboBox<Object> schedulingStrategy;
    @FXML
    private ComboBox<Object> isContention;
    @FXML
    private Button startDispatchButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button randomGenButton;
    @FXML
    private TableView<ProcessPCB> readyQueue;
    @FXML
    private TableColumn<ProcessPCB,Integer> pId;
    @FXML
    private TableColumn<ProcessPCB, String> pName;
    @FXML
    private TableColumn<ProcessPCB, Integer> priority;
    @FXML
    private TableColumn<ProcessPCB, Integer> arrivalTime;
    @FXML
    private TableColumn<ProcessPCB, Integer> serviceTime;
    @FXML
    private TableColumn<ProcessPCB, Integer> startTime;
    @FXML
    private TableColumn<ProcessPCB, Integer> waitTime;
    @FXML
    private TableColumn<ProcessPCB, Integer>runTime;
    @FXML
    private TableColumn<ProcessPCB, Integer> remainingTime;
    @FXML
    private TableColumn<ProcessPCB, Integer> endTime;
    @FXML
    private TableColumn<ProcessPCB, Integer> turnaroundTime;
    @FXML
    private TableColumn<ProcessPCB, Integer> normalizedTurnaroundTime;
    
    @FXML
    private TableView<ProcessPCB> waitQueue;
    @FXML
    private TableColumn<ProcessPCB,Integer> pId1;
    @FXML
    private TableColumn<ProcessPCB, String> pName1;
    @FXML
    private TableColumn<ProcessPCB, Integer> priority1;
    @FXML
    private TableColumn<ProcessPCB, Integer> arrivalTime1;
    @FXML
    private TableColumn<ProcessPCB, Integer> serviceTime1;
    @FXML
    private TableColumn<ProcessPCB, Integer> startTime1;
    @FXML
    private TableColumn<ProcessPCB, Integer> waitTime1;
    @FXML
    private TableColumn<ProcessPCB, Integer>runTime1;
    @FXML
    private TableColumn<ProcessPCB, Integer> remainingTime1;
    @FXML
    private TableColumn<ProcessPCB, Integer> endTime1;
    @FXML
    private TableColumn<ProcessPCB, Integer> turnaroundTime1;
    @FXML
    private TableColumn<ProcessPCB, Integer> normalizedTurnaroundTime1;
    
    @FXML
    private TableView<ProcessPCB> finishQueue;
    @FXML
    private TableColumn<ProcessPCB,Integer> pId2;
    @FXML
    private TableColumn<ProcessPCB, String> pName2;
    @FXML
    private TableColumn<ProcessPCB, Integer> priority2;
    @FXML
    private TableColumn<ProcessPCB, Integer> arrivalTime2;
    @FXML
    private TableColumn<ProcessPCB, Integer> serviceTime2;
    @FXML
    private TableColumn<ProcessPCB, Integer> startTime2;
    @FXML
    private TableColumn<ProcessPCB, Integer> waitTime2;
    @FXML
    private TableColumn<ProcessPCB, Integer>runTime2;
    @FXML
    private TableColumn<ProcessPCB, Integer> remainingTime2;
    @FXML
    private TableColumn<ProcessPCB, Integer> endTime2;
    @FXML
    private TableColumn<ProcessPCB, Integer> turnaroundTime2;
    @FXML
    private TableColumn<ProcessPCB, Integer> normalizedTurnaroundTime2;
    
    @FXML
    private TableView<ProcessPCB> runningTable;
    @FXML
    private TableColumn<ProcessPCB,Integer> pId3;
    @FXML
    private TableColumn<ProcessPCB, String> pName3;
    @FXML
    private TableColumn<ProcessPCB, Integer> priority3;
    @FXML
    private TableColumn<ProcessPCB, Integer> arrivalTime3;
    @FXML
    private TableColumn<ProcessPCB, Integer> serviceTime3;
    @FXML
    private TableColumn<ProcessPCB, Integer> startTime3;
    @FXML
    private TableColumn<ProcessPCB, Integer> waitTime3;
    @FXML
    private TableColumn<ProcessPCB, Integer>runTime3;
    @FXML
    private TableColumn<ProcessPCB, Integer> remainingTime3;
    @FXML
    private TableColumn<ProcessPCB, Integer> endTime3;
    @FXML
    private TableColumn<ProcessPCB, Integer> turnaroundTime3;
    @FXML
    private TableColumn<ProcessPCB, Integer> normalizedTurnaroundTime3;
    
    @FXML
    private TextField newProName;
    @FXML
    private TextField newProSeviceTime;
    @FXML
    private TextField newProPriority;
    @FXML
    private Button addButton;
    @FXML
    private Button pauseAndContinueButton;
    @FXML
    private Button exitButoon;
    /**
     * handling start dispatching event
     */
    @FXML
    public void startDispatch() {
        
    }
    /**
     * handling reset event
     */
    @FXML
    public void reset() {
        
    }
    /**
     * this method will be called once you click the add button
     */
    @FXML
    public void addnewProcess() {
        
    }
    /**
     * this method will be called once the suspend and continue button is clicked
     */
    @FXML
    public void suspendAndContinue() {
        
    }
    @FXML
    public void randomGenPro() {
        dispatcher.getPopupController().getInitProNum().clear();
        dispatcher.getPopupController().getInvalidInputTip().setVisible(false);
        if(dispatcher.getPopupStage().isShowing())
            dispatcher.getPopupStage().close();
        dispatcher.getPopupController().getInputTip().setText("   请输入随机进程个数:");
        dispatcher.getPopupStage().showAndWait();
    }
    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    
}
