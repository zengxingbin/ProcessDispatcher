package view;

import java.sql.Time;
import java.util.ArrayList;

import javax.print.attribute.standard.Finishings;
import javax.swing.JOptionPane;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;

import application.Dispatcher;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.ProcessPCB;
import util.ProcessComparator;

/**
 * 
 * @author 曾幸彬(bingoo) email:2045620125@qq.com 2017年9月10日 下午1:08:31
 *
 */

public class MainController2 implements Controller{
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
    private TextField newProArrivalTime;
    @FXML
    private Label arrivalTimeTip;
    @FXML
    private Label serviceTimeTip;
    @FXML
    private TextField newProPriority;
    @FXML
    private Label priorityTip;
    @FXML
    private Label proNameTip;
    @FXML
    private Button addButton;
    @FXML
    private Button pauseAndContinueButton;
    @FXML
    private Button exitButoon;
    @FXML
    private Text runProcessText;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Text percentage;
    @FXML
    private HBox progressHbox;
    // jude the if the dispatcher button has been clidked
    private boolean isClicked;
    // sure whether reset
    private boolean sureReset;
    // sure whether exit
    private boolean sureExit;
    // the textfield of time slice
    //send the sinal to a thread to add a new process
    private boolean addSignal;
    //name of new process
    private String proName;
    //prioirty of new process
    private int proPriority;
    //service time of new process
    private int proServiceTime;
    //arrival time of new process
    private int proArrivalTime;
    @FXML
    private TextField timeSlicField;
    
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
        schedulingStrategy.getItems().add("时间片轮转(RR)");
        schedulingStrategy.getItems().add("优先数调度(HPN)");
        schedulingStrategy.getItems().add("最短进程优先(SPN)");
        schedulingStrategy.getItems().add("最短剩余时间(SRT)");
        schedulingStrategy.getItems().add("先来先服务(FCFS)");
        schedulingStrategy.getItems().add("最高响应比(HRRN)");
        schedulingStrategy.getItems().add("反馈(FB)");
        isContention.setItems(FXCollections.observableArrayList());
        isContention.getItems().add("抢占");
        isContention.getItems().add("非抢占");
        // add focus listener to the newProSeviceTime textfield
        newProSeviceTime.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                serviceTimeTip.setVisible(false);

            }

        });
        // add focus listener to the newProPriority textfield
        newProPriority.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                priorityTip.setVisible(false);
            }

        });
        // add focus listener to the newProName textfield
        newProName.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                proNameTip.setVisible(false);
            }

        });
        schedulingStrategy.valueProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue != null) {
                if ("时间片轮转(RR)".equals(newPropertyValue)) {
                    isContention.setValue("抢占");
                    isContention.setDisable(true);
                    timeSlicField.setDisable(false);
                } else if ("最短进程优先(SPN)".equals(newPropertyValue)) {
                    isContention.setValue("非抢占");
                    isContention.setDisable(true);
                    timeSlicField.setDisable(true);
                } else if ("优先数调度(HPN)".equals(newPropertyValue)) {
                    isContention.setDisable(false);
                    timeSlicField.setDisable(true);
                } else if ("最短剩余时间(SRT)".equals(newPropertyValue)) {
                    isContention.setValue("抢占");
                    isContention.setDisable(true);
                    timeSlicField.setDisable(true);
                } else if ("先来先服务(FCFS)".equals(newPropertyValue)) {
                    isContention.setValue("非抢占");
                    isContention.setDisable(true);
                    timeSlicField.setDisable(true);
                } else if ("最高响应比(HRRN)".equals(newPropertyValue)) {
                    isContention.setValue("非抢占");
                    isContention.setDisable(true);
                    timeSlicField.setDisable(true);
                } else if ("反馈(FB)".equals(newPropertyValue)) {
                    isContention.setValue("抢占");
                    isContention.setDisable(true);
                    timeSlicField.setDisable(false);
                }
            }

        });
        Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                readyQueue.getSelectionModel().selectedItemProperty()
                .addListener((Observable, oldValue, newValue) -> showProcessDetail(newValue,readyQueue));
                waitQueue.getSelectionModel().selectedItemProperty()
                .addListener((Observable, oldValue, newValue) -> showProcessDetail(newValue,waitQueue));
                finishQueue.getSelectionModel().selectedItemProperty()
                .addListener((Observable, oldValue, newValue) -> showProcessDetail(newValue,finishQueue));
                runningTable.getSelectionModel().selectedItemProperty()
                .addListener((Observable, oldValue, newValue) -> showProcessDetail(newValue,runningTable));
               
            }
        });

    }
    public void removeTableListener() {
        readyQueue.getSelectionModel().selectedItemProperty().removeListener((Observable, oldValue, newValue) -> showProcessDetail(newValue,readyQueue));
        waitQueue.getSelectionModel().selectedItemProperty().removeListener((Observable, oldValue, newValue) -> showProcessDetail(newValue,waitQueue));
        finishQueue.getSelectionModel().selectedItemProperty().removeListener((Observable, oldValue, newValue) -> showProcessDetail(newValue,finishQueue));
        runningTable.getSelectionModel().selectedItemProperty().removeListener((Observable, oldValue, newValue) -> showProcessDetail(newValue,runningTable));
    }
    public void showProcessDetail(ProcessPCB process,TableView<ProcessPCB> queue) {
        Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                ProcessDetailController controller = dispatcher.getProcessController();
                try {

                    controller.getProcessIdLabel().setText(Integer.toString(process.getPid()));

                    controller.getName().setText(process.getpName());
                    controller.getPriority().setText(Integer.toString(process.getPriority()));
                    controller.getProcessArrivalTimeLabel().setText(Integer.toString(process.getArrivalTime()));
                    controller.getServiceTime().setText(Integer.toString(process.getServiceTime()));
                    controller.getProcessStartTimeLabel().setText(process.getStartTimeProperty().getValue());
                    controller.getProcessWaitTimeLabel().setText(Integer.toString(process.getWaitTime()));
                    controller.getProcessRunTimeLabel().setText(process.getRunTimeproperty().getValue());
                    controller.getProcessRemainTimeLabel().setText(Integer.toString(process.getRemainingTime()));
                    if(queue == finishQueue) {
                        controller.getEndTime().setText(Integer.toString(process.getEndTime()));
                        controller.getTurnAroundTime().setText(Integer.toString(process.getTurnaroundTime()));
                        controller.getNormalizedTrunAroundTime().setText(Double.toString(process.getNormalizedTurnaroundTime()));
                    }
                    else {
                        controller.getEndTime().setText("");
                        controller.getTurnAroundTime().setText("");
                        controller.getNormalizedTrunAroundTime().setText("");
                    }
                } catch (Exception e) {

                }
                System.out.println();
                controller.setProcess(process);
                controller.setQueue(queue);
                if (dispatcher.getDispathThread().isAlive() || queue == finishQueue || queue == runningTable) {
                    controller.getName().setDisable(true);
                    controller.getServiceTime().setDisable(true);
                    controller.getPriority().setDisable(true);
                }
                else {
                    controller.getName().setDisable(false);
                    controller.getServiceTime().setDisable(false);
                    controller.getPriority().setDisable(false);
                }
                if(!queue.getItems().isEmpty() && queue.getSelectionModel().getSelectedItem() != null) {
                    if(queue == readyQueue)
                        dispatcher.getProcessStage().setTitle("进程信息" + "("+ "就绪队列"+")");
                    else if(queue == waitQueue)
                        dispatcher.getProcessStage().setTitle("进程信息" + "(" + "等待队列" + ")");
                    else if(queue == finishQueue)
                        dispatcher.getProcessStage().setTitle("进程信息" + "(" + "完成队列" + ")");
                    else if(queue == runningTable)
                        dispatcher.getProcessStage().setTitle("进程信息" + "(" + "正在运行" + ")");
                    dispatcher.getProcessStage().show();
                }
                
            }
        });
        
            
        
        //queue.getSelectionModel().clearSelection();
        
    }

    public void setInitialData() {
        readyQueue.setItems(dispatcher.getReadyQueue());
        initializeTable(readyQueue, pId, pName, priority, arrivalTime, serviceTime, startTime, waitTime, runTime,
                remainingTime, endTime, turnaroundTime, normalizedTurnaroundTime);
        waitQueue.setItems(dispatcher.getWaitQueue());
        initializeTable(waitQueue, pId1, pName1, priority1, arrivalTime1, serviceTime1, startTime1, waitTime1, runTime1,
                remainingTime1, endTime1, turnaroundTime1, normalizedTurnaroundTime1);
        finishQueue.setItems(dispatcher.getFinishQueue());
        initializeTable(finishQueue, pId2, pName2, priority2, arrivalTime2, serviceTime2, startTime2, waitTime2,
                runTime2, remainingTime2, endTime2, turnaroundTime2, normalizedTurnaroundTime2);
        runningTable.setItems(dispatcher.getRunningProcess());
        initializeTable(runningTable, pId3, pName3, priority3, arrivalTime3, serviceTime3, startTime3, waitTime3,
                runTime3, remainingTime3, endTime3, turnaroundTime3, normalizedTurnaroundTime3);

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
        if (dispatcher.getReadyQueue().isEmpty()) {
            System.out.println("There is no  process in ready queue! ");
            JOptionPane.showMessageDialog(null, "就绪队列中无任何进程，请先创建一些进程！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        readyQueue.setEditable(false);
        // gain the scheduling strategy
        String scheduleModel = schedulingStrategy.getValue();
        // gain the contention strategy
        String contention = isContention.getValue();
        if (scheduleModel == null) {
            // default scheduleModel
            scheduleModel = "时间片轮转(RR)";
            schedulingStrategy.setValue("时间片轮转(RR)");
        }
        // set the default contention strategy
        if (contention == null) {
            contention = "非抢占";
            isContention.setValue("非抢占");
        }
        // set the scheduling strategy in dispatcher
        if ("时间片轮转(RR)".equals(scheduleModel)) {
            isContention.setValue("抢占");
            dispatcher.setContention(false);
            dispatcher.setSchedulingStrategy(0);

        } else if ("优先数调度(HPN)".equals(scheduleModel)) {
            if ("非抢占".equals(contention))
                dispatcher.setContention(false);
            else
                dispatcher.setContention(true);
            dispatcher.setSchedulingStrategy(1);
        } else if ("最短进程优先(SPN)".equals(scheduleModel)) {
            isContention.setValue("非抢占");
            dispatcher.setContention(false);
            dispatcher.setSchedulingStrategy(2);
        } else if ("最短剩余时间(SRT)".equals(scheduleModel)) {
            isContention.setValue("抢占");
            dispatcher.setContention(true);
            dispatcher.setSchedulingStrategy(3);
        } else if ("先来先服务(FCFS)".equals(scheduleModel)) {
            isContention.setValue("非抢占");
            dispatcher.setContention(false);
            dispatcher.setSchedulingStrategy(4);
        } else if ("最高响应比(HRRN)".equals(scheduleModel)) {
            isContention.setValue("非抢占");
            dispatcher.setContention(false);
            dispatcher.setSchedulingStrategy(5);
        } else if ("反馈(FB)".equals(scheduleModel)) {
            isContention.setValue("抢占");
            dispatcher.setContention(false);
            dispatcher.setSchedulingStrategy(6);
        }

        // dispatcher.rrDispatcher();
        // start the update readyQueue thread
        /*
         * dispatcher.getUpdateReadyQueue().setDaemon(true);
         * dispatcher.getUpdateReadyQueue().start();
         */
        // start the dispathcer thread in dispatcher

        if (!dispatcher.getDispathThread().isAlive()) {
            // dispatcher.getDispathThread().setDaemon(true);
            /*
             * if (dispatcher.isHasStartDispatch()) { //conntinue to dispatch
             * pauseAndContinueButton.setText("暂停"); } else {
             * dispatcher.getDispathThread().start();
             * dispatcher.setHasStartDispatch(true); }
             */
            /*
             * compare the dispatch thread with the last completed dispatch to
             * decide whether starting the current thread or creating a new
             * dispatch to start because the completed thread cant't back to the
             * former state once it ends you can refer to the operation system
             * principle and find the answer from the graph of five state if you
             * don't understand
             */

            if (dispatcher.iSFirstThread()) {
                dispatcher.getDispathThread().setDaemon(true);
                dispatcher.getDispathThread().start();
                dispatcher.getAddProcessThread().setDaemon(true);
                dispatcher.getAddProcessThread().start();
                dispatcher.setIsFirstThread(false);
            } else {
                // clear the last finish queue
                dispatcher.getFinishQueue().clear();
                dispatcher.createNewDispatchThread();
                dispatcher.createNewAddThread();
                dispatcher.getDispathThread().setDaemon(true);
                dispatcher.getDispathThread().start();
                dispatcher.getAddProcessThread().setDaemon(true);
                dispatcher.getAddProcessThread().start();
            }
            // set the time slice no matter what the schedulingStrategy
            // is,because it
            // won't influnence other scheduling strategy except the RR dispatch
            // algorithm
            // get the time slice(default value is 1)
            String text = dispatcher.getMainController().getTimeSlicField().getText().trim();
            int timeSlice = 0;
            try {
                timeSlice = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // if what you input is illegal,reset the time slice to the
                // default value
                dispatcher.getMainController().getTimeSlicField().setText("1");
                timeSlice = 1;
            }
            dispatcher.setTIMESLICING(timeSlice);

        } else {
            if ("继续".equals(pauseAndContinueButton.getText())) {
                pauseAndContinueButton.setText("暂停");
                dispatcher.setNeedWait(false);
            } else
                JOptionPane.showMessageDialog(null, "已经开始调度！", "提示", JOptionPane.WARNING_MESSAGE);

        }

    }

    /**
     * handling reset event
     */
    /**
     * reset to clear all the list
     */
    @FXML
    public void reset() {
        /*
         * Thread clearThread = new Thread(new Runnable() {
         * 
         * @Override public void run() { while (true) { synchronized
         * (dispatcher.getObj()) { dispatcher.getReadyQueue().clear();
         * dispatcher.getWaitQueue().clear();
         * dispatcher.getFinishQueue().clear();
         * dispatcher.getRunningProcess().clear(); break; } } } });
         * clearThread.start();
         */

        // at the begining,sure the value of sureRest is false
        sureReset = false;
       
        if (dispatcher.getDispathThread().isAlive()) {
            // suspend the dispatch thread when the tip view appear
            dispatcher.setNeedWait(true);
            dispatcher.getWarnController().getText().setText("进程正在执行，确认要重置吗？");
            dispatcher.getWarnStage().showAndWait();
            if (!sureReset)
                return;
            // send the clear signal to clear all the list
            dispatcher.setClearSignal(true);
            int countTime = 0;
            while (dispatcher.isClearSignal()) {
                try {
                    if (countTime > 2000)
                        return;
                    Thread.currentThread().sleep(100);
                    countTime += 100;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        dispatcher.getReadyQueue().clear();
        dispatcher.getSynchronizeReadyQueue().clear();
        dispatcher.getWaitQueue().clear();
        dispatcher.getFinishQueue().clear();
        dispatcher.getRunningProcess().clear();
        dispatcher.setTimeCounter(0);
        dispatcher.setProcessCounter(0);
        dispatcher.setTotalServiceTime(0);
        dispatcher.setSizeOfReadyQueue(0);
        dispatcher.setProgress(0);
        if ("继续".equals(pauseAndContinueButton.getText())) {
            pauseAndContinueButton.setText("暂停");
        }
        addButton.setDisable(false);
        randomGenButton.setDisable(false);
        //removeTableListener();
    }

    /**
     * reset to clear what you input
     */
    @FXML
    public void reset2() {
        newProName.clear();
        newProPriority.clear();
        newProSeviceTime.clear();
        dispatcher.setTimeCounter(0);
    }

    /**
     * this method will be called once you click the add button
     */
    @FXML
    public void addnewProcess() {
        // check your input
        boolean addSuccess = true;
        proName = newProName.getText();
        if (proName == null)
            proNameTip.setVisible(true);
        else
            addSuccess = true;
        if ("".equals(proName.trim())) {
            proNameTip.setVisible(true);
            addSuccess = false;
        }
        try {
            proArrivalTime = Integer.parseInt(newProArrivalTime.getText().trim());
        }catch(NumberFormatException e) {
            arrivalTimeTip.setVisible(true);
            addSuccess = false;
        }
        try {
            proServiceTime = Integer.parseInt(newProSeviceTime.getText().trim());
        } catch (NumberFormatException e) {
            // e.printStackTrace();
            serviceTimeTip.setVisible(true);
            addSuccess = false;
        }
        try {
            
            proPriority = Integer.parseInt(newProPriority.getText().trim());
        }catch(NumberFormatException e) {
            priorityTip.setVisible(true);
            return;
        }
        if (!addSuccess)
            return;
        //create a new process,and add to the ready queue
        ProcessPCB process = new ProcessPCB();
        process.setpName(proName);
        process.setpNameProperty(process.getpName());
        process.setPriority(proPriority);
        process.setPriorityProperty(process.getPriority());
        process.setArrivalTime(proArrivalTime);
        //add the arrival time to the timelist
        dispatcher.getTimeList().add(proArrivalTime);
        process.setArrivalTimeProperty(process.getArrivalTime());
        process.setServiceTime(proServiceTime);
        process.setServiceTimeProperty(process.getServiceTime());
        process.setPid(dispatcher.getProcessCounter());
        process.setPidProperty(process.getPid());
        dispatcher.setProcessCounter(dispatcher.getProcessCounter() + 1);
        process.setStatus(0);
        process.setFirstTime(true);
        process.setRemainingTime(process.getServiceTime());
        process.setRemainingTimeProperty(process.getRemainingTime());
        process.setWaitTime(0);
        process.setWaitTimeProperty(process.getWaitTime());
        //add to the reay queue
        dispatcher.getReadyQueue().add(process);
        //sure to add a new process
        //addSignal = true;
        
        // clear the text field
        /*
         * newProName.clear(); newProPriority.clear(); newProSeviceTime.clear();
         */
    }

    /**
     * this method will be called once the suspend and continue button is
     * clicked
     */
    @FXML
    public void suspendAndContinue() {
        String text = pauseAndContinueButton.getText();
        // System.out.println(dispatcher.getUpdateReadyQueue().isAlive());
        if (dispatcher.getDispathThread().isAlive()) {
            if ("暂停".equals(text)) {
                // you can't add new process by any way when the dispatcher
                // pasue
                // so the add button should be disabled
                addButton.setDisable(true);
                randomGenButton.setDisable(true);
                pauseAndContinueButton.setText("继续");
                // the dispatch thread need wait
                dispatcher.setNeedWait(true);
            } else if ("继续".equals(text)) {
                // set the add button to be able
                addButton.setDisable(false);
                randomGenButton.setDisable(false);
                pauseAndContinueButton.setText("暂停");
                // notify the dispatch thread
                /*
                 * synchronized (dispatcher) { dispatcher.setNeedWait(false);
                 * dispatcher.notifyDispatchThread(); System.out.println("唤醒");
                 * System.out.println("dispathcer thread:" +
                 * dispatcher.getDispathThread().isAlive());
                 * 
                 * }
                 */
                dispatcher.setNeedWait(false);
            }
        }
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

    @FXML
    public void exit() {
        if (dispatcher.getDispathThread().isAlive()) {
            // suspend the dispatch thread when the tip view appear
            // suspend the dispatch thread when the tip view appear
            dispatcher.setNeedWait(true);
            dispatcher.setNeedWait(true);
            dispatcher.getWarnController().getText().setText("进程正在执行，确认要退出吗？");
            dispatcher.getWarnStage().showAndWait();
            if (!sureExit)
                return;
        }
        dispatcher.getMainStage().close();

    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public TableView<ProcessPCB> getReadyQueue() {
        return readyQueue;
    }

    public boolean isSureReset() {
        return sureReset;
    }

    public void setSureReset(boolean sureReset) {
        this.sureReset = sureReset;
    }

    public boolean isSureExit() {
        return sureExit;
    }

    public void setSureExit(boolean sureExit) {
        this.sureExit = sureExit;
    }

    public ComboBox<String> getSchedulingStrategy() {
        return schedulingStrategy;
    }

    public void setSchedulingStrategy(ComboBox<String> schedulingStrategy) {
        this.schedulingStrategy = schedulingStrategy;
    }

    public ComboBox<String> getIsContention() {
        return isContention;
    }

    public void setIsContention(ComboBox<String> isContention) {
        this.isContention = isContention;
    }

    public Text getRunProcessText() {
        return runProcessText;
    }

    public ProgressBar getProgressBar() {

        return progressBar;
    }

    public Text getPercentage() {
        return percentage;
    }

    public HBox getProgressHbox() {
        return progressHbox;
    }

    public TextField getTimeSlicField() {
        return timeSlicField;
    }

    public TableView<ProcessPCB> getFinishQueue() {
        return finishQueue;
    }

    public TableView<ProcessPCB> getWaitQueue() {
        return waitQueue;
    }
    public boolean isAddSignal() {
        return addSignal;
    }
    public void setAddSignal(boolean addSignal) {
        this.addSignal = addSignal;
    }
    public String getProName() {
        return proName;
    }
    public int getProPriority() {
        return proPriority;
    }
    public int getProServiceTime() {
        return proServiceTime;
    }
    public Button getPauseAndContinueButton() {
        return pauseAndContinueButton;
    }
    
}
