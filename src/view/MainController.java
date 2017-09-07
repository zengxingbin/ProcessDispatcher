package view;

import application.Dispatcher;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
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
    private ComboBox<String> schedulingStrategy;
    @FXML
    private ComboBox<String> isContention;
    @FXML
    private Button startDispatchButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button randomGenButton;
    @FXML
    private TableView<ProcessPCB> readyQueue;
    @FXML
    private TableColumn<ProcessPCB, String> pId;
    @FXML
    private TableColumn<ProcessPCB, String> pName;
    @FXML
    private TableColumn<ProcessPCB, String> priority;
    @FXML
    private TableColumn<ProcessPCB, String> arrivalTime;
    @FXML
    private TableColumn<ProcessPCB, String> serviceTime;
    @FXML
    private TableColumn<ProcessPCB, String> startTime;
    @FXML
    private TableColumn<ProcessPCB, String> waitTime;
    @FXML
    private TableColumn<ProcessPCB, String> runTime;
    @FXML
    private TableColumn<ProcessPCB, String> remainingTime;
    @FXML
    private TableColumn<ProcessPCB, String> endTime;
    @FXML
    private TableColumn<ProcessPCB, String> turnaroundTime;
    @FXML
    private TableColumn<ProcessPCB, String> normalizedTurnaroundTime;

    @FXML
    private TableView<ProcessPCB> waitQueue;
    @FXML
    private TableColumn<ProcessPCB, String> pId1;
    @FXML
    private TableColumn<ProcessPCB, String> pName1;
    @FXML
    private TableColumn<ProcessPCB, String> priority1;
    @FXML
    private TableColumn<ProcessPCB, String> arrivalTime1;
    @FXML
    private TableColumn<ProcessPCB, String> serviceTime1;
    @FXML
    private TableColumn<ProcessPCB, String> startTime1;
    @FXML
    private TableColumn<ProcessPCB, String> waitTime1;
    @FXML
    private TableColumn<ProcessPCB, String> runTime1;
    @FXML
    private TableColumn<ProcessPCB, String> remainingTime1;
    @FXML
    private TableColumn<ProcessPCB, String> endTime1;
    @FXML
    private TableColumn<ProcessPCB, String> turnaroundTime1;
    @FXML
    private TableColumn<ProcessPCB, String> normalizedTurnaroundTime1;

    @FXML
    private TableView<ProcessPCB> finishQueue;
    @FXML
    private TableColumn<ProcessPCB, String> pId2;
    @FXML
    private TableColumn<ProcessPCB, String> pName2;
    @FXML
    private TableColumn<ProcessPCB, String> priority2;
    @FXML
    private TableColumn<ProcessPCB, String> arrivalTime2;
    @FXML
    private TableColumn<ProcessPCB, String> serviceTime2;
    @FXML
    private TableColumn<ProcessPCB, String> startTime2;
    @FXML
    private TableColumn<ProcessPCB, String> waitTime2;
    @FXML
    private TableColumn<ProcessPCB, String> runTime2;
    @FXML
    private TableColumn<ProcessPCB, String> remainingTime2;
    @FXML
    private TableColumn<ProcessPCB, String> endTime2;
    @FXML
    private TableColumn<ProcessPCB, String> turnaroundTime2;
    @FXML
    private TableColumn<ProcessPCB, String> normalizedTurnaroundTime2;

    @FXML
    private TableView<ProcessPCB> runningTable;
    @FXML
    private TableColumn<ProcessPCB, String> pId3;
    @FXML
    private TableColumn<ProcessPCB, String> pName3;
    @FXML
    private TableColumn<ProcessPCB, String> priority3;
    @FXML
    private TableColumn<ProcessPCB, String> arrivalTime3;
    @FXML
    private TableColumn<ProcessPCB, String> serviceTime3;
    @FXML
    private TableColumn<ProcessPCB, String> startTime3;
    @FXML
    private TableColumn<ProcessPCB, String> waitTime3;
    @FXML
    private TableColumn<ProcessPCB, String> runTime3;
    @FXML
    private TableColumn<ProcessPCB, String> remainingTime3;
    @FXML
    private TableColumn<ProcessPCB, String> endTime3;
    @FXML
    private TableColumn<ProcessPCB, String> turnaroundTime3;
    @FXML
    private TableColumn<ProcessPCB, String> normalizedTurnaroundTime3;

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
     * this method will be auto-called after the construction to set the some
     * initial data
     */
    @FXML
    public void initialize() {
        /*
         * readyQueue.setItems(dispatcher.getReadyQueue());
         * pId.setCellValueFactory(cellData ->
         * cellData.getValue().getPidPorperty());
         * pName.setCellValueFactory(cellData ->
         * cellData.getValue().getPNameProperty());
         * priority.setCellValueFactory(cellData ->
         * cellData.getValue().getPriorityProperty());
         * arrivalTime.setCellValueFactory(cellData ->
         * cellData.getValue().getArrivalTimeProperty());
         * serviceTime.setCellValueFactory(cellData ->
         * cellData.getValue().getServiceTimeProperty());
         * startTime.setCellValueFactory(cellData ->
         * cellData.getValue().getStartTimeProperty());
         * waitTime.setCellValueFactory(cellData ->
         * cellData.getValue().getWaitTimeProperty());
         * runTime.setCellValueFactory(cellData ->
         * cellData.getValue().getRunTimeproperty());
         * remainingTime.setCellValueFactory(cellData ->
         * cellData.getValue().getRemainingTimeProperty());
         * endTime.setCellValueFactory(cellData ->
         * cellData.getValue().getLineProperty());
         * turnaroundTime.setCellValueFactory(cellData ->
         * cellData.getValue().getLineProperty());
         * normalizedTurnaroundTime.setCellValueFactory(cellData ->
         * cellData.getValue().getLineProperty());
         */
        // initialize the ComboBox
        schedulingStrategy.setItems(FXCollections.observableArrayList());
        schedulingStrategy.getItems().add("时间片轮转");
        schedulingStrategy.getItems().add("优先数调度");
        schedulingStrategy.getItems().add("最短进程优先");
        schedulingStrategy.getItems().add("最短剩余时间");
        isContention.setItems(FXCollections.observableArrayList());
        isContention.getItems().add("抢占");
        isContention.getItems().add("非抢占");
    }

    public void setInitialData() {
        readyQueue.setItems(dispatcher.getReadyQueue());
        initializeTable(readyQueue, pId, pName, priority, arrivalTime, serviceTime, startTime, waitTime, runTime,
                remainingTime, endTime, normalizedTurnaroundTime, normalizedTurnaroundTime);
        waitQueue.setItems(dispatcher.getWaitQueue());
        initializeTable(waitQueue, pId1, pName1, priority1, arrivalTime1, serviceTime1, startTime1, waitTime1, runTime1,
                remainingTime1, endTime1, normalizedTurnaroundTime1, normalizedTurnaroundTime1);
        finishQueue.setItems(dispatcher.getFinishQueue());
        initializeTable(finishQueue, pId2, pName2, priority2, arrivalTime2, serviceTime2, startTime2, waitTime2,
                runTime, remainingTime2, endTime2, normalizedTurnaroundTime2, normalizedTurnaroundTime2);
        runningTable.setItems(dispatcher.getRunningProcess());
        initializeTable(runningTable, pId3, pName3, priority3, arrivalTime3, serviceTime3, startTime3, waitTime3,
                runTime3, remainingTime3, endTime3, normalizedTurnaroundTime3, normalizedTurnaroundTime3);

        /*
         * pId.setStyle("-fx-alignment:CENTER");
         * pId.setCellValueFactory(cellData ->
         * cellData.getValue().getPidPorperty());
         * pName.setStyle("-fx-alignment:CENTER");
         * pName.setCellValueFactory(cellData ->
         * cellData.getValue().getPNameProperty());
         * priority.setStyle("-fx-alignment:CENTER");
         * priority.setCellValueFactory(cellData ->
         * cellData.getValue().getPriorityProperty());
         * arrivalTime.setStyle("-fx-alignment:CENTER");
         * arrivalTime.setCellValueFactory(cellData ->
         * cellData.getValue().getArrivalTimeProperty());
         * serviceTime.setStyle("-fx-alignment:CENTER");
         * serviceTime.setCellValueFactory(cellData ->
         * cellData.getValue().getServiceTimeProperty());
         * startTime.setStyle("-fx-alignment:CENTER");
         * startTime.setCellValueFactory(cellData ->
         * cellData.getValue().getStartTimeProperty());
         * waitTime.setStyle("-fx-alignment:CENTER");
         * waitTime.setCellValueFactory(cellData ->
         * cellData.getValue().getWaitTimeProperty());
         * runTime.setStyle("-fx-alignment:CENTER");
         * runTime.setCellValueFactory(cellData ->
         * cellData.getValue().getRunTimeproperty());
         * remainingTime.setStyle("-fx-alignment:CENTER");
         * remainingTime.setCellValueFactory(cellData ->
         * cellData.getValue().getRemainingTimeProperty());
         * endTime.setStyle("-fx-alignment:CENTER");
         * endTime.setCellValueFactory(cellData ->
         * cellData.getValue().getLineProperty());
         * turnaroundTime.setStyle("-fx-alignment:CENTER");
         * turnaroundTime.setCellValueFactory(cellData ->
         * cellData.getValue().getLineProperty());
         * normalizedTurnaroundTime.setStyle("-fx-alignment:CENTER");
         * normalizedTurnaroundTime.setCellValueFactory(cellData ->
         * cellData.getValue().getLineProperty());
         */

    }

    public void initializeTable(TableView<ProcessPCB> queue, TableColumn<ProcessPCB, String> pId,
            TableColumn<ProcessPCB, String> pName, TableColumn<ProcessPCB, String> priority,
            TableColumn<ProcessPCB, String> arrivalTime, TableColumn<ProcessPCB, String> serviceTime,
            TableColumn<ProcessPCB, String> startTime, TableColumn<ProcessPCB, String> waitTime,
            TableColumn<ProcessPCB, String> runTime, TableColumn<ProcessPCB, String> remainingTime,
            TableColumn<ProcessPCB, String> endTime, TableColumn<ProcessPCB, String> turnaroundTime,
            TableColumn<ProcessPCB, String> normalizedTurnaroundTime) {
        // set the common property
        pId.setStyle("-fx-alignment:CENTER");
        pName.setStyle("-fx-alignment:CENTER");
        priority.setStyle("-fx-alignment:CENTER");
        serviceTime.setStyle("-fx-alignment:CENTER");
        arrivalTime.setStyle("-fx-alignment:CENTER");
        startTime.setStyle("-fx-alignment:CENTER");
        waitTime.setStyle("-fx-alignment:CENTER");
        runTime.setStyle("-fx-alignment:CENTER");
        remainingTime.setStyle("-fx-alignment:CENTER");
        endTime.setStyle("-fx-alignment:CENTER");
        turnaroundTime.setStyle("-fx-alignment:CENTER");
        normalizedTurnaroundTime.setStyle("-fx-alignment:CENTER");
        pId.setCellValueFactory(cellData -> cellData.getValue().getPidPorperty());
        pName.setCellValueFactory(cellData -> cellData.getValue().getPNameProperty());
        priority.setCellValueFactory(cellData -> cellData.getValue().getPriorityProperty());
        serviceTime.setCellValueFactory(cellData -> cellData.getValue().getServiceTimeProperty());
        remainingTime.setCellValueFactory(cellData -> cellData.getValue().getRemainingTimeProperty());
        // if the queue is readyQueue
        if (queue == this.readyQueue || queue == this.runningTable) {
            arrivalTime.setCellValueFactory(cellData -> cellData.getValue().getArrivalTimeProperty());
            startTime.setCellValueFactory(cellData -> cellData.getValue().getStartTimeProperty());
            waitTime.setCellValueFactory(cellData -> cellData.getValue().getWaitTimeProperty());
            runTime.setCellValueFactory(cellData -> cellData.getValue().getRunTimeproperty());
            /*
             * endTime.setCellValueFactory(cellData ->
             * cellData.getValue().getLineProperty());
             * turnaroundTime.setCellValueFactory(cellData ->
             * cellData.getValue().getLineProperty());
             * normalizedTurnaroundTime.setCellValueFactory(cellData ->
             * cellData.getValue().getLineProperty());
             */
        } else if (queue == waitQueue) {
            /*
             * arrivalTime.setCellValueFactory(cellData ->
             * cellData.getValue().getLineProperty());
             * startTime.setCellValueFactory(cellData ->
             * cellData.getValue().getLineProperty());
             * waitTime.setCellValueFactory(cellData ->
             * cellData.getValue().getLineProperty());
             * runTime.setCellValueFactory(cellData ->
             * cellData.getValue().getLineProperty());
             * endTime.setCellValueFactory(cellData ->
             * cellData.getValue().getLineProperty());
             * turnaroundTime.setCellValueFactory(cellData ->
             * cellData.getValue().getLineProperty());
             * normalizedTurnaroundTime.setCellValueFactory(cellData ->
             * cellData.getValue().getLineProperty());
             */
        } else if (queue == finishQueue) {
            arrivalTime.setCellValueFactory(cellData -> cellData.getValue().getArrivalTimeProperty());
            startTime.setCellValueFactory(cellData -> cellData.getValue().getStartTimeProperty());
            waitTime.setCellValueFactory(cellData -> cellData.getValue().getWaitTimeProperty());
            runTime.setCellValueFactory(cellData -> cellData.getValue().getRunTimeproperty());
            endTime.setCellValueFactory(cellData -> cellData.getValue().getEndTimeProperty());
            turnaroundTime.setCellValueFactory(cellData -> cellData.getValue().getTurnAroundTimeProperty());
            normalizedTurnaroundTime
                    .setCellValueFactory(cellData -> cellData.getValue().getNormalizedTurnAroundTimeProperty());
        }
    }

    /**
     * handling start dispatching event
     */
    @FXML
    public void startDispatch() {
        // gain the scheduling strategy
        String scheduleModel = schedulingStrategy.getValue();
        // set the scheduling strategy in dispatcher
        if ("时间片轮转".equals(scheduleModel))
            dispatcher.setSchedulingStrategy(0);
        else if ("优先级调度".equals(scheduleModel))
            dispatcher.setSchedulingStrategy(1);
        else if ("最短进程优先".equals(scheduleModel))
            dispatcher.setSchedulingStrategy(2);
        else if ("最短剩余时间".equals(scheduleModel))
            dispatcher.setSchedulingStrategy(3);
        // gain the contention strategy
        String contention = isContention.getValue();
        // set the contention strategy in the dispatcher
        if ("非抢占".equals(contention))
            dispatcher.setContention(false);
        else if ("抢占".equals(contention))
            dispatcher.setContention(true);
        // dispatcher.rrDispatcher();
        // start the update readyQueue thread
        dispatcher.getUpdateReadyQueue().setDaemon(true);
        dispatcher.getUpdateReadyQueue().start();
        // start the dispathcer thread in dispatcher
        dispatcher.getDispathThread().setDaemon(true);
        dispatcher.getDispathThread().start();

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
     * this method will be called once the suspend and continue button is
     * clicked
     */
    @FXML
    public void suspendAndContinue() {

    }

    @FXML
    public void randomGenPro() {
        dispatcher.getPopupController().getInitProNum().clear();
        dispatcher.getPopupController().getInvalidInputTip().setVisible(false);
        if (dispatcher.getPopupStage().isShowing())
            dispatcher.getPopupStage().close();
        dispatcher.getPopupController().getInputTip().setText("   请输入随机进程个数:");
        dispatcher.getPopupStage().showAndWait();
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public TableView<ProcessPCB> getReadyQueue() {
        return readyQueue;
    }

}
