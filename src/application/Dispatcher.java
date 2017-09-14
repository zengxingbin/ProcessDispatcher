package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ProcessPCB;
import util.ProcessComparator;
import util.Queue;
import view.Controller;
import view.MainController;
import view.MainController2;
import view.PopupController;
import view.ProcessDetailController;
import view.StartController;
import view.WarningController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
/**
 * 
 * @author ���ұ�(bingoo)
 * email:2045620125@qq.com
 * 2017��9��10�� ����1:07:16
 *
 */
public class Dispatcher extends Application {
    //the choice of scheduling 
    private int schedulingChoice;
    //time list to create a new process when time coming
    private ArrayList<Integer> timeList = new ArrayList<>();
    private static int timeCounter;
    private static long startTime;
    private static long currentTime;
    private static int processCounter = 0;// as the the process id
    private final static int PROCESSMAXNUM = 10;// maximum number of processe
                                                // allowed
    private static int TIMESLICING = 1;// time slice
    private Stage startStage;
    private Stage mainStage;
    private Scene mainScene;
    private Stage popupStage;
    private Stage warnStage;
    private Stage processStage;
    private Stage mainStage2;
    private ProcessDetailController processController;
    private PopupController popupController;
    private MainController mainController;
    private MainController2 mainController2;
    private WarningController warnController;
    // ready queue
    private ObservableList<ProcessPCB> readyQueue = FXCollections.observableArrayList();
    /*this queue Synchronize with the readyQueue,
     aim to replace the readyQueue to sort processes 
     in order to  gain the next running process from readyQueue
     instead of operate the readyQueue directly which will change
     the order of processes in readyQueue
     */
    private ObservableList<ProcessPCB> synchronizeReadyQueue = FXCollections.observableArrayList();
    // wait queue
    private ObservableList<ProcessPCB> waitQueue = FXCollections.observableArrayList();
    // finish queue
    private ObservableList<ProcessPCB> finishQueue = FXCollections.observableArrayList();
    // running
    private ObservableList<ProcessPCB> runningProcess = FXCollections.observableArrayList();
    //sum of all the process service time
    private int totalServiceTime;
    // current running process
    private ProcessPCB process;
    // the progress of the dispatch
    private double progress;
    // scheduling strategy
    private int schedulingStrategy = -1;// 0.rr 1.priority 2.SPN 3.SRT
    // contention strategy
    private boolean isContention;
    // decide another thread if needing waiting
    private boolean needWait;
    // a signal to clear all the list
    private boolean clearSignal;
    // create a thread to perform scheduling
    // has the dispatch thread started
    // private boolean hasStartDispatch;
    // the dispatch thread
    private Thread dispathThread = new Thread(new DispatchRun());
    // record the last completed dispathThread to decide whether create a new
    // dispatch thread
    // private Thread completedThread = null;
    private boolean isFirstThread = true;
    // this thread is responsible for updating the readyQueue
    private Thread updateReadyQueue = new Thread(new Runnable() {

        @Override
        public void run() {
            System.out.println("update thread lauch");

            while (true) {
                if (removeSignal && !readyQueue.isEmpty()) {
                    process = readyQueue.remove(0);
                    removeSignal = false;
                } else if (addSignal) {
                    readyQueue.add(process);
                    addSignal = false;
                } else if (waitSignal) {
                    for (ProcessPCB process : readyQueue)
                        process.setWaitTime(process.getWaitTime() + 1);
                    waitSignal = false;
                }
            }
        }
    });
    private Thread randdomAddProcessThread = new Thread(new Runnable() {
        
        @Override
        public void run() {
            System.out.println("random add thread start");
            while(true) {
                // when the dispatchThread does't start or it finish,reset the start
                // time and current time
                if (!dispathThread.isAlive()) {
                    /*dispatcher.setStartTime(System.currentTimeMillis() / 1000);
                    dispatcher.setCurrentTime(dispatcher.getStartTime());*/
                    timeCounter = 0;
                }
                // random generate the priority and service time of process
                Random rand = new Random();
                
                for (int i = 0; i < popupController.getProNum(); i++) {
                    ProcessPCB process = new ProcessPCB();
                    // initialize process
                    process.setpName("process" + getProcessCounter());
                    process.setpNameProperty(process.getpName());
                    process.setPid(processCounter);
                    process.setPidProperty(process.getPid());
                    processCounter += 1;
                    process.setPriority(rand.nextInt(20));
                    process.setPriorityProperty(process.getPriority());
                    process.setStatus(0);
                    process.setFirstTime(true);
                    process.setServiceTime(rand.nextInt(10) + 1);
                    process.setServiceTimeProperty(process.getServiceTime());
                    process.setRemainingTime(process.getServiceTime());
                    process.setRemainingTimeProperty(process.getRemainingTime());
                    process.setWaitTime(0);
                    process.setWaitTimeProperty(process.getWaitTime());
                    
                    if (dispathThread.isAlive()) {
                        /*
                         * int count = 0; while(true) { //avoid current thread to wait all
                         * the time if(count >= 1000) break; try {
                         * Thread.currentThread().sleep(100); } catch (InterruptedException
                         * e) { // TODO Auto-generated catch block e.printStackTrace(); }
                         * if(dispatcher.isAllowdAdd()) break; count += 100; }
                         */
                        if (sizeOfReadyQueue + 1 < PROCESSMAXNUM) {
                            isRequestAdd = true;
                            isFinishAdd = false;
                            int count = 0;
                            while (isRequestAdd) {
                                try {
                                    Thread.currentThread().sleep(100);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                count += 100;
                                if(count >= 2000)
                                    break;
                            }
                            process.setArrivalTime(timeCounter);
                            process.setArrivalTimeProperty(process.getArrivalTime());
                            // record current time
                            // dispatcher.setCurrentTime(System.currentTimeMillis() / 1000);
                            // join in the readyQueue
                            readyQueue.add(process);
                            // add a new process,so the total service time should increase
                            totalServiceTime += process.getServiceTime();
                            // join in the synchronizedreadyQueue at the same time
                            synchronizeReadyQueue.add(process);

                            // judge what the schedulingStrategy is and sort the queue
                            if ("����������(HPN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // if the strategy is round robin time,sort the
                                // readyQueue
                                // according to the priority every time you add new
                                // process
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(0));
                                synchronizeReadyQueue.sort(new ProcessComparator(0));

                            } else if ("��̽�������(SPN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // if the strategy is round robin time,sort the
                                // readyQueue
                                // according to the serviceTime every time you add new
                                // process
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(1));
                                synchronizeReadyQueue.sort(new ProcessComparator(1));
                            } else if ("���ʣ��ʱ��(SRT)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // sort the ready queue according to the remaining time;
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(2));
                                synchronizeReadyQueue.sort(new ProcessComparator(2));
                            } else if ("�����ȷ���(FCFS)".equals(mainController.getSchedulingStrategy().getValue())) {

                            } else if ("ʱ��Ƭ��ת(RR)".equals(mainController.getSchedulingStrategy().getValue())) {

                            } else if ("�����Ӧ��(HRRN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                synchronizeReadyQueue.sort(new ProcessComparator(3));
                            }
                            sizeOfReadyQueue += 1;
                            isFinishAdd = true;
                            // System.out.println(process.getpName() + "���ʱ��:" +
                            // dispatcher.getTimeCounter());
                        } else {
                            // join int the wait queue
                            waitQueue.add(process);
                        }
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }else {
                        if (sizeOfReadyQueue < PROCESSMAXNUM) {
                            process.setArrivalTime(timeCounter);
                            process.setArrivalTimeProperty(process.getArrivalTime());
                            // record current time
                            // dispatcher.setCurrentTime(System.currentTimeMillis() / 1000);
                            // join in the readyQueue
                            readyQueue.add(process);
                            // add a new process,so the total service time should increase
                            totalServiceTime += process.getServiceTime();
                            // join in the synchronizedreadyQueue at the same time
                            synchronizeReadyQueue.add(process);

                            // judge what the schedulingStrategy is and sort the queue
                            if ("����������(HPN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // if the strategy is round robin time,sort the
                                // readyQueue
                                // according to the priority every time you add new
                                // process
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(0));
                                synchronizeReadyQueue.sort(new ProcessComparator(0));

                            } else if ("��̽�������(SPN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // if the strategy is round robin time,sort the
                                // readyQueue
                                // according to the serviceTime every time you add new
                                // process
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(1));
                                synchronizeReadyQueue.sort(new ProcessComparator(1));
                            } else if ("���ʣ��ʱ��(SRT)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // sort the ready queue according to the remaining time;
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(2));
                                synchronizeReadyQueue.sort(new ProcessComparator(2));
                            } else if ("�����ȷ���(FCFS)".equals(mainController.getSchedulingStrategy().getValue())) {

                            } else if ("ʱ��Ƭ��ת(RR)".equals(mainController.getSchedulingStrategy().getValue())) {

                            } else if ("�����Ӧ��(HRRN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                synchronizeReadyQueue.sort(new ProcessComparator(3));
                            }
                            sizeOfReadyQueue += 1;
                        }else {
                            // join int the wait queue
                            waitQueue.add(process);
                        }
                    }
                }
                popupController.setProNum(0);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    });
    //a thread to add only one procesds
    private Thread addAProcessThread = new Thread(new Runnable() {
        
        @Override
        public void run() {
            System.out.println("start add a process thread");
            while(true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                if(mainController.isAddSignal()) {
                 // create the new process then set the property
                    ProcessPCB process = new ProcessPCB();
                    process.setpName(mainController.getProName());
                    process.setpNameProperty(process.getpName());
                    process.setPriority(mainController.getProPriority());
                    process.setPriorityProperty(process.getPriority());
                    process.setServiceTime(mainController.getProServiceTime());
                    process.setServiceTimeProperty(process.getServiceTime());
                    process.setPid(processCounter);
                    process.setPidProperty(process.getPid());
                    processCounter += 1;
                    process.setStatus(0);
                    // when the dispatchThread does't start or it finish,reset the start
                    // time and current time
                    if (!dispathThread.isAlive()) {
                        /*
                         * dispatcher.setStartTime(System.currentTimeMillis() / 1000);
                         * dispatcher.setCurrentTime(dispatcher.getStartTime());
                         */
                        timeCounter = 0;
                    }
                    process.setFirstTime(true);
                    process.setRemainingTime(process.getServiceTime());
                    process.setRemainingTimeProperty(process.getRemainingTime());
                    process.setWaitTime(0);
                    process.setWaitTimeProperty(process.getWaitTime());
                    // only when the dispatch thread is alive;

                    if (dispathThread.isAlive()) {
                        /*

                         * int count = 0; while(true) { //avoid current thread to wait all
                         * the time if(count >= 1000) break; try {
                         * Thread.currentThread().sleep(100); } catch (InterruptedException
                         * e) { // TODO Auto-generated catch block e.printStackTrace(); }
                         * if(dispatcher.isAllowdAdd()) break; count += 100; }

                         */
                        if (sizeOfReadyQueue + 1 < PROCESSMAXNUM) {
                            isRequestAdd = true;
                            isFinishAdd = false;
                            int count = 0;
                            while (isRequestAdd) {
                                try {

                                    Thread.currentThread().sleep(100);

                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                count += 100;
                                if(count >= 2000)
                                    break;
                            }
                            process.setArrivalTime(timeCounter);
                            process.setArrivalTimeProperty(process.getArrivalTime());
                            // record current time
                            // dispatcher.setCurrentTime(System.currentTimeMillis() / 1000);
                            // join in the readyQueue
                            readyQueue.add(process);
                            // add a new process,so the total service time should increase
                            totalServiceTime += process.getServiceTime();
                            // join in the synchronizedreadyQueue at the same time
                            synchronizeReadyQueue.add(process);
                            // judge what the schedulingStrategy is and sort the queue

                            if ("����������(HPN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // if the strategy is round robin time,sort the
                                // readyQueue
                                // according to the priority every time you add new
                                // process
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(0));
                                synchronizeReadyQueue.sort(new ProcessComparator(0));
                            } else if ("��̽�������(SPN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // if the strategy is round robin time,sort the
                                // readyQueue
                                // according to the serviceTime every time you add new
                                // process
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(1));
                                synchronizeReadyQueue.sort(new ProcessComparator(1));

                            } else if ("���ʣ��ʱ��(SRT)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // sort the ready queue according to the remaining time;
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(2));
                                synchronizeReadyQueue.sort(new ProcessComparator(2));

                            } else if ("�����ȷ���(FCFS)".equals(mainController.getSchedulingStrategy().getValue())) {

                            } else if ("ʱ��Ƭ��ת(RR)".equals(mainController.getSchedulingStrategy().getValue())) {

                            } else if ("�����Ӧ��(HRRN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                synchronizeReadyQueue.sort(new ProcessComparator(3));
                            }
                            sizeOfReadyQueue += 1;
                            isFinishAdd = true;
                            // System.out.println(process.getpName() + "���ʱ��:" +
                            // dispatcher.getTimeCounter());
                        } else {
                            // join int the wait queue
                            waitQueue.add(process);
                        }

                    } else {
                        if (sizeOfReadyQueue < PROCESSMAXNUM) {
                            process.setArrivalTime(timeCounter);
                            process.setArrivalTimeProperty(process.getArrivalTime());
                            // record current time
                            // dispatcher.setCurrentTime(System.currentTimeMillis() / 1000);
                            // join in the readyQueue
                            readyQueue.add(process);
                            // add a new process,so the total service time should increase
                            totalServiceTime += process.getServiceTime();
                            // join in the synchronizedreadyQueue at the same time
                            synchronizeReadyQueue.add(process);
                            // judge what the schedulingStrategy is and sort the queue
                            if ("����������(HPN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // if the strategy is round robin time,sort the
                                // readyQueue
                                // according to the priority every time you add new
                                // process
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(0));
                                synchronizeReadyQueue.sort(new ProcessComparator(0));

                            } else if ("��̽�������(SPN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // if the strategy is round robin time,sort the
                                // readyQueue
                                // according to the serviceTime every time you add new
                                // process
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(1));
                                synchronizeReadyQueue.sort(new ProcessComparator(1));
                            } else if ("���ʣ��ʱ��(SRT)".equals(mainController.getSchedulingStrategy().getValue())) {
                                // sort the ready queue according to the remaining time;
                                // dispatcher.getReadyQueue().sort(new
                                // PriorityComparator(2));
                                synchronizeReadyQueue.sort(new ProcessComparator(2));

                            } else if ("�����ȷ���(FCFS)".equals(mainController.getSchedulingStrategy().getValue())) {

                            } else if ("ʱ��Ƭ��ת(RR)".equals(mainController.getSchedulingStrategy().getValue())) {

                            } else if ("�����Ӧ��(HRRN)".equals(mainController.getSchedulingStrategy().getValue())) {
                                synchronizeReadyQueue.sort(new ProcessComparator(3));

                            }
                            sizeOfReadyQueue += 1;

                        } else {
                            // join int the wait queue
                            waitQueue.add(process);
                        }

                    }
                }
                mainController.setAddSignal(false);
            }
        }
    });
    
    /*if allowed to add new process to the reaydQueue
     * when the current running process has't run for a unit of time(that is 1)
     * it is not allowed to add new process to the ready queue util the
     * current running process has't run for a unit of time(that is 1)
     * in order to make sure the time is right
     */
    // is allow to add new process
    private boolean isAllowdAdd;
    //is request add new process
    private boolean isRequestAdd;
    //is finish the task of adding new process
    private boolean isFinishAdd;
    // create some signal to realize the communication among threads
    // signal for updating the ready queue
    private boolean removeSignal;// signal for removing process
    private boolean addSignal;// signal for adding process
    private boolean sortSignal;// signal for sorting the list
    private boolean waitSignal;// signal for increase the wait time
    // signal for updating the wait queue
    private boolean removeSignal1;
    private boolean addSignal1;
    private boolean sortSignal1;
    private boolean waitSignal1;
    // signal for updating the finish queue
    private boolean removeSignal2;
    private boolean addSignal2;
    private boolean sortSignal2;
    private boolean waitSignal2;
    // signal for updating the running process
    private boolean removeSignal3;
    private boolean addSignal3;
    private boolean sortSignal3;
    private boolean waitSignal3;
    /*
     * record the size of readyQueue
     * when dispatching the process 
     * the size of readyQueue may be change 
     * but actually ,the size of readyQueue did't change at all
     */
    private int sizeOfReadyQueue = 0;
    class AddProcessRun implements Runnable {

        @Override
        public void run() {
            System.out.println("start the add thread");
            while(true) {
                //when the dispatch thread is over ,this should be over
                if(!dispathThread.isAlive())
                    break;
                /*
                 * every time the size of finishQueue isn't equal to the finishNum
                 * that is say a process of readyQueue has finished
                 * add process of waitqueue to the readyqueue at the
                 * same time,record the last finishQueue's size
                 */
                if(sizeOfReadyQueue < PROCESSMAXNUM && !waitQueue.isEmpty()) {
                    //finishNum = finishQueue.size();
                    //send add signal to the dispatch thread
                    isRequestAdd = true;
                    while(isRequestAdd) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    ProcessPCB pro = waitQueue.remove(0);
                    totalServiceTime += pro.getServiceTime();
                    pro.setArrivalTime(timeCounter);
                    //System.out.println("timeCount" + timeCounter);
                    pro.setArrivalTimeProperty(timeCounter);
                    readyQueue.add(pro);
                    synchronizeReadyQueue.add(pro);
                    
                 // judge what the schedulingStrategy is and sort the queue
                    if ("����������(HPN)".equals(mainController.getSchedulingStrategy().getValue())) {
                        
                        synchronizeReadyQueue.sort(new ProcessComparator(0));

                    } else if ("��̽�������(SPN)".equals(mainController.getSchedulingStrategy().getValue())) {
                        
                        synchronizeReadyQueue.sort(new ProcessComparator(1));
                    } else if ("���ʣ��ʱ��(SRT)".equals(mainController.getSchedulingStrategy().getValue())) {
                        
                        synchronizeReadyQueue.sort(new ProcessComparator(2));
                    } else if ("�����ȷ���(FCFS)".equals(mainController.getSchedulingStrategy().getValue())) {

                    } else if ("ʱ��Ƭ��ת(RR)".equals(mainController.getSchedulingStrategy().getValue())) {

                    } else if ("�����Ӧ��(HRRN)".equals(mainController.getSchedulingStrategy().getValue())) {
                        synchronizeReadyQueue.sort(new ProcessComparator(3));
                    }
                    
                    sizeOfReadyQueue++;
                    isFinishAdd = true;
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        
    }
    class DispatchRun implements Runnable {

        @Override
        public void run() {
            System.out.println("start the dispatch thread!");
            //initialize the progress bar
            mainController.getProgressHbox().setVisible(true);
            switch (schedulingStrategy) {
            case 0:
                rrDispatcher();
                break;
            case 1:
                priorityNumDispatch();
                break;
            case 2:
                SPNDAndSRTDispatch();
                break;
            case 3:
                SPNDAndSRTDispatch();
                break;
            case 4:
                FCFSDispatch();
                break;
            case 5:
                HRRNDispatch();
                break;
            }
            //reset proccess Counter
            processCounter = 0;
            // rmove the last completed process from runningProcess table
            runningProcess.remove(0);
            //clear the readyqueue
            readyQueue.clear();
            synchronizeReadyQueue.clear();
            // reset the hasStartDispatch value when the dispatch thread finish
            // hasStartDispatch = false;
            //reset the time counter;
            timeCounter = 0;
            
            //finsht dispatch,reset the progress bar
            //reset totalServiceTime
            totalServiceTime = 0;
            //reset the size of readyqueue
            sizeOfReadyQueue = 0;
            // reset the processCounter
            mainController.getProgressHbox().setVisible(false);
            mainController.getRunProcessText().setText("");
            mainController.getPercentage().setText("    0%");
            mainController.getProgressBar().setProgress(0);
            Platform.runLater(new Runnable() {
                
                @Override
                public void run() {
                    if("����".equals(mainController.getPauseAndContinueButton().getText())) {
                        
                        mainController.getPauseAndContinueButton().setText("��ͣ");
                    }
                    
                }
            });
            
            progress = 0;
            mainController.getReadyQueue().setEditable(true);
            Platform.runLater(new Runnable() {
                
                @Override
                public void run() {
                    if(processStage.isShowing()) {
                        processStage.close();
                        processController.getQueue().getSelectionModel().clearSelection();
                    }
                    
                }
            });
            
        }

        public void rrDispatcher() {
            int timeSlicing = 0;
            if (!readyQueue.isEmpty()) {
                // first process to be run
                process = readyQueue.remove(0);
                // removeSignal = true;
                // wait for the readyQueue remove first process
                /*
                 * while (true) { try { Thread.currentThread().sleep(100); }
                 * catch (InterruptedException e) { e.printStackTrace(); } if
                 * (process != null) break; }
                 */
                // System.out.println(process);
                // add the process to running table
                runningProcess.add(process);
                /*
                 * startTime = System.currentTimeMillis() / 1000; currentTime =
                 * startTime;
                 */
                process.setStartTime(timeCounter);
                process.setStartTimeProperty(process.getStartTime());
                process.setFirstTime(false);
                process.setHasRun(true);
                mainController.getRunProcessText().setText(process.getpName());
            } else {
                System.out.println("There is no  process in ready queue! ");
                JOptionPane.showMessageDialog(null, "�������������κν��̣����ȴ���һЩ���̣�", "��ʾ", JOptionPane.WARNING_MESSAGE);
                return;
            }
            while (true) {

                /*
                 * synchronized (this) { while(needWait) { try {
                 * System.out.println("�ȴ���"); this.wait();
                 * System.out.println("�ӵȴ��ĵط���ʼ����������ִ��"); } catch
                 * (InterruptedException e) { // TODO Auto-generated catch block
                 * e.printStackTrace(); }
                 * 
                 * }
                 * 
                 * }
                 */
                /*// dispatch thread suspend
                while (needWait) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }*/
                /*when current process has't run for a competed a unit of time
                 *it is not allowed to add new process to the reayd queue in order 
                 *to make sure the time is right
                 */
                //isAllowdAdd = false;
                process.setStatus(1);// 1 represents running
                process.setRunTime(process.getRunTime() + 1);
                process.setRunTimeProperty(process.getRunTime());
                process.setRemainingTime(process.getRemainingTime() - 1);
                process.setRemainingTimeProperty(process.getRemainingTime());
                System.out.println("ʱ��Ƭ�ֻ�" + "**Currnet running process:" + process + "**");
                // update the runningprocess,the runningprocess list will be
                // updated only the quote of current process is changed
                /*runningProcess.remove(0);
                try {
                    process = (ProcessPCB) process.clone();
                    runningProcess.add(process);
                } catch (CloneNotSupportedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }*/
                // current process has run for one seconds,increase the time
                // slicing
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // increase the time counter
                timeCounter++;
                progress = (timeCounter * 1.0) / totalServiceTime;
    
                mainController.getProgressBar().setProgress(progress);
                mainController.getPercentage().setText("    " + String.format("%.1f", progress * 100) + "%");
                
                timeSlicing++;
                // other process wait for the time slicing
                for (ProcessPCB process : readyQueue) {
                    process.setWaitTime(process.getWaitTime() + 1);
                    process.setWaitTimeProperty(process.getWaitTime());
                }
                /*ObservableList<ProcessPCB> readyQueueTemp = FXCollections.observableArrayList();
                for(ProcessPCB process :readyQueue) {
                    try {
                        ProcessPCB processPCB = (ProcessPCB) process.clone();
                        processPCB.setWaitTime(processPCB.getWaitTime()+1);
                        readyQueueTemp.add(processPCB);
                    } catch (CloneNotSupportedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                readyQueue.clear();
                readyQueue.addAll(readyQueueTemp);
                synchronizeReadyQueue.clear();
                synchronizeReadyQueue.addAll(readyQueue);*/
                //synchronizeReadyQueue.sort(new ProcessComparator(mode));
                // if the clearSignal is true,end the thread
                if (clearSignal) {
                    clearSignal = false;
                    // reset the processCounter
                    processCounter = 0;
                    //clear the ready queue and the synchronize ready queue
                    readyQueue.clear();
                    synchronizeReadyQueue.clear();
                    return;
                }
                // dispatch thread suspend
                while (needWait) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if(isRequestAdd) {
                    isRequestAdd = false;
                    while(!isFinishAdd) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    isFinishAdd = false;
                    //System.out.println("�����ɣ���ǰʱ��:" + timeCounter);
                }
                /*if (!readyQueue.isEmpty()) {
                    if (timeSlicing >= TIMESLICING || process.isFinish()) {
                        // next process to be run
                        process = readyQueue.remove(0);
                        runningProcess.remove(0);
                        runningProcess.add(process);
                        mainController.getRunProcessText().setText(process.getpName());
                        
                        if (process.isFirstTime()) {
                            // currentTime = System.currentTimeMillis() / 1000;
                            // process.setStartTime((int) (currentTime -
                            // startTime));
                            System.out.println(process.getpName() + "��ʼʱ��:" + timeCounter);
                            process.setStartTime(timeCounter);
                            process.setFirstTime(false);
                            process.setHasRun(true);
                            
                        }

                        // clear the time slicing for every process
                        timeSlicing = 0;
                    }
                }*/
                
                if(process.getRemainingTime() == 0) {
                    process.setEndTime(timeCounter);
                    process.setEndTimeProperty(process.getEndTime());
                    // compute the turnaround time
                    process.setTurnaroundTime(process.getWaitTime() + process.getServiceTime());
                    process.setTurnAroundTimeProperty(process.getTurnaroundTime());
                    // compute the normalized turnaround time
                    process.setNormalizedTurnaroundTime(
                            (double) process.getTurnaroundTime() / process.getServiceTime());
                    process.setNormalizedTurnAroundTimeProperty(process.getNormalizedTurnaroundTime());
                    // change the status of process:finish
                    process.setStatus(2);// represents process completed
                    process.setFinish(true);
                    //add the current process to the finish queue
                    finishQueue.add(process);
                    
                    sizeOfReadyQueue--;
                    if(timeSlicing >= TIMESLICING) {
                        timeSlicing = 0;
                    }
                    if(!readyQueue.isEmpty()) {
                        //get next process to be run
                        process = readyQueue.remove(0);
                        
                        mainController.getRunProcessText().setText(process.getpName());
                        if (process.isFirstTime()) {
                            process.setStartTime(timeCounter);
                            process.setStartTimeProperty(process.getStartTime());
                            
                            process.setFirstTime(false);
                            process.setHasRun(true);
                            
                        }
                        runningProcess.remove(0);
                        runningProcess.add(process);
                        
                    }else {
                        System.out.println("--All processes have been completed!--");
                        // print the finishQueue
                        for (ProcessPCB process : finishQueue)
                            System.out.println(process);
                        break;//dispatch over
                    }
                }else {
                    if(timeSlicing >= TIMESLICING) {
                        //add current running process to the ready queue
                        readyQueue.add(process);
                        //get next process to be run ,may be still the current running process
                        process = readyQueue.remove(0);
                        
                        mainController.getRunProcessText().setText(process.getpName());
                        if (process.isFirstTime()) {
                            //System.out.println(process.getpName() + "��ʼʱ��:" + timeCounter);
                            process.setStartTime(timeCounter);
                            process.setStartTimeProperty(process.getStartTime());
                            System.out.println(process.getpName() + "��ʼ" + process.getStartTime());
                            process.setFirstTime(false);
                            process.setHasRun(true);
                            
                        }
                        runningProcess.remove(0);
                        runningProcess.add(process);
                        //reset the timeslice
                        timeSlicing = 0;
                    }
                }

            }
         // give a tip of finishing the dispatch
            JOptionPane.showMessageDialog(null, "���н��̵�����ɣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
        }

        public void priorityNumDispatch() {
            // System.out.println("����������");
            // is the readyQueue empty
            /*
             * if (readyQueue.isEmpty()) {
             * System.out.println("There is no  process in ready queue! ");
             * JOptionPane.showMessageDialog(null, "�������������κν��̣����ȴ���һЩ���̣�", "��ʾ",
             * JOptionPane.WARNING_MESSAGE); return; }
             */
            // sort the readyQueue according to priority
            //readyQueue.sort(new PriorityComparator(0));
            // sort the synchronizedReadyQueue according to the priority
            synchronizeReadyQueue.sort(new ProcessComparator(0));
            if(!isContention) {
            // run the first process which has highest priority
            // first process to be run
                // process = readyQueue.removeLast();
                //process = readyQueue.remove(0);
              //from the synchronizedReadyQueue get the first running process
                process = synchronizeReadyQueue.remove(0);
                //synchronized to the real readyQueue
                readyQueue.remove(process);
                /*
                 * startTime = System.currentTimeMillis() / 1000; currentTime =
                 * startTime;
                 */
                process.setStartTime(timeCounter);
                process.setStartTimeProperty(process.getStartTime());
                process.setHasRun(true);
                process.setFirstTime(false);
                // add to the running process
                runningProcess.add(process);
                mainController.getRunProcessText().setText(process.getpName());
                while (true) {
                    /*// dispatch thread suspend
                    while (needWait) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }*/
                    //isAllowdAdd = false;
                    process.setStatus(1);// 1 represents running
                    process.setRunTime(process.getRunTime() + 1);
                    process.setRunTimeProperty(process.getRunTime());
                    process.setRemainingTime(process.getRemainingTime() - 1);
                    process.setRemainingTimeProperty(process.getRemainingTime());
                    System.out.println("���ȼ�" + "**Currnet running process:" + process + "**");
                    /*runningProcess.remove(0);
                    try {
                        process = (ProcessPCB) process.clone();
                        runningProcess.add(process);
                    } catch (CloneNotSupportedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }*/
                    // current process run for one second in every circle
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // increase the timeCounter
                    timeCounter++;
                    progress = (timeCounter * 1.0) / totalServiceTime;
                    
                    mainController.getProgressBar().setProgress(progress);
                    mainController.getPercentage().setText("    " + String.format("%.1f", progress * 100) + "%");
                    // other process wait for the currnet process to finish
                    for (ProcessPCB process : readyQueue) {
                        process.setWaitTime(process.getWaitTime() + 1);
                        process.setWaitTimeProperty(process.getWaitTime());
                    }
                    /*ObservableList<ProcessPCB> readyQueueTemp = FXCollections.observableArrayList();
                    for(ProcessPCB process :readyQueue) {
                        try {
                            ProcessPCB processPCB = (ProcessPCB) process.clone();
                            processPCB.setWaitTime(processPCB.getWaitTime()+1);
                            readyQueueTemp.add(processPCB);
                        } catch (CloneNotSupportedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    readyQueue.clear();
                    readyQueue.addAll(readyQueueTemp);
                    synchronizeReadyQueue.clear();
                    synchronizeReadyQueue.addAll(readyQueue);
                    synchronizeReadyQueue.sort(new ProcessComparator(0));*/
                    //isAllowdAdd = true;
                    // if the clearSignal is true,end the thread
                    if (clearSignal) {
                        clearSignal = false;
                        // reset the processCounter
                        processCounter = 0;
                        //clear the ready queue and the synchronize ready queue
                        readyQueue.clear();
                        synchronizeReadyQueue.clear();
                        return;
                    }
                    // dispatch thread suspend
                    while (needWait) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if(isRequestAdd) {
                        isRequestAdd = false;
                        while(!isFinishAdd) {
                            try {
                                Thread.currentThread().sleep(100);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        isFinishAdd = false;
                        //System.out.println("�����ɣ���ǰʱ��:" + timeCounter);
                    }
                    //synchronize with the readyQueue
                    /*for(ProcessPCB process : synchronizeReadyQueue)
                        process.setWaitTime(process.getWaitTime() + 1);*/
                    if (process.getRemainingTime() == 0) {
                        // currentTime = System.currentTimeMillis() / 1000;
                        // set the end time of process
                        // process.setEndTime((int) (currentTime - startTime));
                        process.setEndTime(timeCounter);
                        process.setEndTimeProperty(process.getEndTime());
                        // compute the turnaround time
                        process.setTurnaroundTime(process.getWaitTime() + process.getServiceTime());
                        process.setTurnAroundTimeProperty(process.getTurnaroundTime());
                        // compute the normalized turnaround time
                        process.setNormalizedTurnaroundTime(
                                (double) process.getTurnaroundTime() / process.getServiceTime());
                        process.setNormalizedTurnAroundTimeProperty(process.getNormalizedTurnaroundTime());
                        // change the status of process:finish
                        process.setStatus(2);// represents process completed
                        // join to the finishQueue
                        // finishQueue.addFirst(process);
                        finishQueue.add(process);
                        sizeOfReadyQueue--;
                        process.setFinish(true);
                        // process = null;
                        if (readyQueue.isEmpty()) {
                            /*
                             * process = null; System.out.
                             * println("--All processes have been completed!--"
                             * ); // print the finishQueue for (ProcessPCB
                             * process : finishQueue)
                             * System.out.println(process); break;
                             */
                            /*
                             * while (readyQueue.isEmpty()) System.out.
                             * println("wait the process added to the ready queue"
                             * ); try { Thread.currentThread().sleep(1000); }
                             * catch (InterruptedException e) { // TODO
                             * Auto-generated catch block e.printStackTrace(); }
                             */
                            System.out.println("--All processes have been completed!--");
                            // print the finishQueue
                            for (ProcessPCB process : finishQueue)
                                System.out.println(process);

                            break;
                        } else {
                            // if current process finish,run next process which has highest priority
                            //process = readyQueue.remove(0);
                            process = synchronizeReadyQueue.remove(0);
                            //synchronize with the synchronizeReadyQueue
                            readyQueue.remove(process);
                            // currentTime = System.currentTimeMillis() / 1000;
                            // process.setStartTime((int) currentTime - (int)
                            // startTime);
                           
                            mainController.getRunProcessText().setText(process.getpName());
                            if (process.isFirstTime()) {
                                process.setStartTime(timeCounter);
                                process.setStartTimeProperty(process.getStartTime());
                                process.setFirstTime(false);
                                process.setHasRun(true);
                            }
                            //as the running process
                            runningProcess.remove(0);
                            runningProcess.add(process);
                        }
                    }
                }
            }else {
                // System.out.println("��ռ�㷨");
                // run the first process which has highest priority
                // first process to be run
                
                //process = readyQueue.remove(0);
                process = synchronizeReadyQueue.remove(0);
                System.out.println("���ȼ���ռ" + process.getpName());
                readyQueue.remove(process);
                
                 /* startTime = System.currentTimeMillis() / 1000; currentTime =
                  startTime;*/
                 
                runningProcess.add(process);
                process.setStartTime(timeCounter);
                process.setStartTimeProperty(process.getStartTime());
                process.setHasRun(true);
                process.setFirstTime(false);
                mainController.getRunProcessText().setText(process.getpName());
                while (true) {
                    
                    //isAllowdAdd = false;
                    process.setStatus(1);// 1 represents running
                    process.setRunTime(process.getRunTime() + 1);
                    process.setRunTimeProperty(process.getRunTime());
                    process.setRemainingTime(process.getRemainingTime() - 1);
                    process.setRemainingTimeProperty(process.getRemainingTime());
                    System.out.println("���ȼ���ռ" + "**Currnet running process:" + process + "**");
                    /*// dispatch thread suspend
                    while (needWait) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }*/
                    /*runningProcess.remove(0);
                    try {
                        process = (ProcessPCB) process.clone();
                        runningProcess.add(process);
                    } catch (CloneNotSupportedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }*/
                    // current process run for one second in every circle
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                 // increase the timeCounter
                    timeCounter++;
                    progress = (timeCounter * 1.0) / totalServiceTime;
                    
                    mainController.getProgressBar().setProgress(progress);
                    mainController.getPercentage().setText("    " + String.format("%.1f", progress * 100) + "%");
                    // other process wait for the currnet process to finish
                    for (ProcessPCB process : readyQueue) {
                        process.setWaitTime(process.getWaitTime() + 1);
                        process.setWaitTimeProperty(process.getWaitTime());
                    }
                    /*ObservableList<ProcessPCB> readyQueueTemp = FXCollections.observableArrayList();
                    for(ProcessPCB process :readyQueue) {
                        try {
                            ProcessPCB processPCB = (ProcessPCB) process.clone();
                            processPCB.setWaitTime(processPCB.getWaitTime()+1);
                            readyQueueTemp.add(processPCB);
                        } catch (CloneNotSupportedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    readyQueue.clear();
                    readyQueue.addAll(readyQueueTemp);
                    synchronizeReadyQueue.clear();
                    synchronizeReadyQueue.addAll(readyQueue);
                    synchronizeReadyQueue.sort(new ProcessComparator(0));*/
                    //isAllowdAdd = true;
                    // if the clearSignal is true,end the thread
                    if (clearSignal) {
                        clearSignal = false;
                        // reset the processCounter
                        processCounter = 0;
                        //clear the ready queue and the synchronize ready queue
                        readyQueue.clear();
                        synchronizeReadyQueue.clear();
                        return;
                    }
                    // dispatch thread suspend
                    while (needWait) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if(isRequestAdd) {
                        isRequestAdd = false;
                        while(!isFinishAdd) {
                            try {
                                Thread.currentThread().sleep(100);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        isFinishAdd = false;
                    }
                    /*for(ProcessPCB process : synchronizeReadyQueue)
                        process.setWaitTime(process.getWaitTime() + 1);*/
                    if (process.getRemainingTime() == 0) {
                        
                        // currentTime = System.currentTimeMillis() / 1000;
                        // set the end time of process
                        process.setEndTime(timeCounter);
                        process.setEndTimeProperty(process.getEndTime());
                        // compute the turnaround time
                        process.setTurnaroundTime(process.getWaitTime() + process.getServiceTime());
                        process.setTurnAroundTimeProperty(process.getTurnaroundTime());
                        // compute the normalized turnaround time
                        process.setNormalizedTurnaroundTime(
                                (double) process.getTurnaroundTime() / process.getServiceTime());
                        process.setNormalizedTurnAroundTimeProperty(process.getNormalizedTurnaroundTime());
                        // change the status of process:finish
                        process.setStatus(2);// represents process completed
                        // join to the finishQueue
                        finishQueue.add(process);
                        sizeOfReadyQueue--;
                        process.setFinish(true);
                        if (readyQueue.isEmpty()) {
                            // process = null;
                            System.out.println("--All processes have been completed!--");
                            // print the finishQueue
                            for (ProcessPCB process : finishQueue)
                                System.out.println(process);
                            // reset the processCounter
                            processCounter = 0;
                            break;
                        } else {
                            // if current process finish,run next process which
                            // has
                            // highest priority
                            
                            //process = readyQueue.remove(0);
                            process = synchronizeReadyQueue.remove(0);
                            readyQueue.remove(process);
                            
                            mainController.getRunProcessText().setText(process.getpName());
                            // currentTime = System.currentTimeMillis() / 1000;
                            if (process.isFirstTime()) {
                                process.setStartTime(timeCounter);
                                process.setStartTimeProperty(process.getStartTime());
                                process.setFirstTime(false);
                                process.setHasRun(true);
                            }
                            //as the running process
                            runningProcess.remove(0);
                            runningProcess.add(process);
                        }
                    } else if(isContention){
                        // if there is process which has higher priority in
                        // ready queue,then run it,or continue running the
                        // current process
                        if (!readyQueue.isEmpty()) {
                            
                             /* the last process in ready queue has highest
                              priority*/
                             
                            //ProcessPCB process2 = readyQueue.get(0);
                            ProcessPCB process2 = synchronizeReadyQueue.get(0);
                            if (process2.getPriority() > process.getPriority()) {
                                // current running process' prioirty decrease
                                process.setPriority(process.getPriority() - 1);
                                process.setPriorityProperty(process.getPriority());
                                //add current running process to the ready queue
                                readyQueue.add(process);
                                synchronizeReadyQueue.add(process);
                                synchronizeReadyQueue.sort(new ProcessComparator(0));
                                synchronizeReadyQueue.remove(0);
                                readyQueue.remove(process2);
                                process = process2;
                                mainController.getRunProcessText().setText(process.getpName());
                                if (process.isFirstTime()) {
                                    process.setStartTime(timeCounter);
                                    process.setStartTimeProperty(process.getStartTime());
                                    process.setFirstTime(false);
                                    process.setHasRun(true);
                                }
                              //as the running process
                                runningProcess.remove(0);
                                runningProcess.add(process);
                            }
                        }
                    }
                }
            }
         // give a tip of finishing the dispatch
            JOptionPane.showMessageDialog(null, "���н��̵�����ɣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
        }

        /**
         * shortest process next(SPN)
         * 
         */
        public void SPNDAndSRTDispatch() {

            // run the first process which has highest priority
            // first process to be run
            if (!isContention) {
                //readyQueue.sort(new PriorityComparator(1));
                synchronizeReadyQueue.sort(new ProcessComparator(1));
                
            } else {
                //readyQueue.sort(new PriorityComparator(2));
                synchronizeReadyQueue.sort(new ProcessComparator(2));
            }
            // process = readyQueue.removeLast();
            //process = readyQueue.remove(0);
            process = synchronizeReadyQueue.remove(0);
            readyQueue.remove(process);

            /*
             * startTime = System.currentTimeMillis() / 1000; currentTime =
             * startTime;
             */
            // add to the running process
            runningProcess.add(process);
            process.setStartTime(timeCounter);
            process.setStartTimeProperty(process.getStartTime());
            process.setHasRun(true);
            process.setFirstTime(false);
            mainController.getRunProcessText().setText(process.getpName());
            while (true) {
                
                //isAllowdAdd = false;
                process.setStatus(1);// 1 represents running
                process.setRunTime(process.getRunTime() + 1);
                process.setRunTimeProperty(process.getRunTime());
                process.setRemainingTime(process.getRemainingTime() - 1);
                process.setRemainingTimeProperty(process.getRemainingTime());
                System.out.println("��̽�������" + "**Currnet running process:" + process + "**");
                /*// dispatch thread suspend
                while (needWait) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }*/
                /*runningProcess.remove(0);
                try {
                    process = (ProcessPCB) process.clone();
                    runningProcess.add(process);
                } catch (CloneNotSupportedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }*/
                // current process run for one second in every circle
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // increase the timeCounter
                timeCounter++;
                progress = (timeCounter * 1.0) / totalServiceTime;
                
                mainController.getProgressBar().setProgress(progress);
                mainController.getPercentage().setText("    " + String.format("%.1f", progress * 100) + "%");
                // other process wait for the currnet process to finish
                for (ProcessPCB process : readyQueue) {
                    process.setWaitTime(process.getWaitTime() + 1);
                    process.setWaitTimeProperty(process.getWaitTime());
                }
                    
                /*ObservableList<ProcessPCB> readyQueueTemp = FXCollections.observableArrayList();
                for(ProcessPCB process :readyQueue) {
                    try {
                        ProcessPCB processPCB = (ProcessPCB) process.clone();
                        processPCB.setWaitTime(processPCB.getWaitTime()+1);
                        readyQueueTemp.add(processPCB);
                    } catch (CloneNotSupportedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                readyQueue.clear();
                readyQueue.addAll(readyQueueTemp);
                synchronizeReadyQueue.clear();
                synchronizeReadyQueue.addAll(readyQueue);*/
                if(!isContention) {
                    synchronizeReadyQueue.sort(new ProcessComparator(1));
                }else {
                    synchronizeReadyQueue.sort(new ProcessComparator(2));
                }
                //isAllowdAdd = true;
                // if the clearSignal is true,end the thread
                if (clearSignal) {
                    clearSignal = false;
                    // reset the processCounter
                    processCounter = 0;
                    //clear the ready queue and the synchronize ready queue
                    readyQueue.clear();
                    synchronizeReadyQueue.clear();
                    return;
                }
                // dispatch thread suspend
                while (needWait) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if(isRequestAdd) {
                    isRequestAdd = false;
                    while(!isFinishAdd) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    isFinishAdd = false;
                }
                /*for(ProcessPCB process : synchronizeReadyQueue)
                    process.setWaitTime(process.getWaitTime() + 1);*/
                if (process.getRemainingTime() == 0) {
                    // currentTime = System.currentTimeMillis() / 1000;
                    // set the end time of process
                    // process.setEndTime((int) (currentTime - startTime));
                    process.setEndTime(timeCounter);
                    process.setEndTimeProperty(process.getEndTime());
                    // compute the turnaround time
                    process.setTurnaroundTime(process.getWaitTime() + process.getServiceTime());
                    process.setTurnAroundTimeProperty(process.getTurnaroundTime());
                    // compute the normalized turnaround time
                    process.setNormalizedTurnaroundTime(
                            (double) process.getTurnaroundTime() / process.getServiceTime());
                    process.setNormalizedTurnAroundTimeProperty(process.getNormalizedTurnaroundTime());
                    // change the status of process:finish
                    process.setStatus(2);// represents process completed
                    // join to the finishQueue
                    // finishQueue.addFirst(process);
                    finishQueue.add(process);
                    sizeOfReadyQueue--;
                    process.setFinish(true);
                    // process = null;
                    if (readyQueue.isEmpty()) {
                        /*
                         * process = null; System.out.
                         * println("--All processes have been completed!--" );
                         * // print the finishQueue for (ProcessPCB process :
                         * finishQueue) System.out.println(process); break;
                         */
                        /*
                         * while (readyQueue.isEmpty()) System.out.
                         * println("wait the process added to the ready queue"
                         * ); try { Thread.currentThread().sleep(1000); } catch
                         * (InterruptedException e) { // TODO Auto-generated
                         * catch block e.printStackTrace(); }
                         */
                        System.out.println("--All processes have been completed!--");
                        // print the finishQueue
                        for (ProcessPCB process : finishQueue)
                            System.out.println(process);
                        break;
                    } else {
                        // if current process finish,run next process which has
                        // highest priority
                        //process = readyQueue.remove(0);
                        process = synchronizeReadyQueue.remove(0);
                        readyQueue.remove(process);
                        
                        // currentTime = System.currentTimeMillis() / 1000;
                        // process.setStartTime((int) currentTime - (int)
                        // startTime);
                        mainController.getRunProcessText().setText(process.getpName());
                        if (process.isFirstTime()) {
                            process.setStartTime(timeCounter);
                            process.setStartTimeProperty(process.getStartTime());
                            process.setFirstTime(false);
                            process.setHasRun(true);
                        }
                        //as the running process
                        runningProcess.remove(0);
                        runningProcess.add(process);
                    }
                } else if(isContention) {
                    /*
                     * get the process which has shortest remaining time from
                     * ready queue then compare it to the current running
                     * process
                     */
                    if (!readyQueue.isEmpty()) {
                        //ProcessPCB process2 = readyQueue.get(0);
                        ProcessPCB process2 = synchronizeReadyQueue.get(0);
                        if (process2.getRemainingTime() < process.getRemainingTime()) {
                            // remove the process which has shortest remaining
                            // time
                            // from the ready queue
                            //add current running process to the ready queue
                            readyQueue.add(process);
                            synchronizeReadyQueue.add(process);
                            synchronizeReadyQueue.sort(new ProcessComparator(2));
                            readyQueue.remove(process2);
                            synchronizeReadyQueue.remove(0);
                            process = process2;
                            
                            mainController.getRunProcessText().setText(process.getpName());
                            if (process.isFirstTime()) {
                                process.setStartTime(timeCounter);
                                process.setStartTimeProperty(process.getStartTime());
                                process.setFirstTime(false);
                                process.setHasRun(true);
                            }
                          //as the running process
                            runningProcess.remove(0);
                            runningProcess.add(process);
                        }
                    }

                }
            }

         // give a tip of finishing the dispatch
            JOptionPane.showMessageDialog(null, "���н��̵�����ɣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
        }
        // First Come First Service
        public void FCFSDispatch() {
            //get the first process to run
            process = readyQueue.remove(0);

            /*
             * startTime = System.currentTimeMillis() / 1000; currentTime =
             * startTime;
             */
            // add to the running process
            runningProcess.add(process);
            process.setStartTime(timeCounter);
            process.setStartTimeProperty(process.getStartTime());
            process.setHasRun(true);
            process.setFirstTime(false);
            mainController.getRunProcessText().setText(process.getpName());
            while (true) {
                
                //isAllowdAdd = false;
                process.setStatus(1);// 1 represents running
                process.setRunTime(process.getRunTime() + 1);
                process.setRunTimeProperty(process.getRunTime());
                process.setRemainingTime(process.getRemainingTime() - 1);
                process.setRemainingTimeProperty(process.getRemainingTime());
                System.out.println("�����ȷ���" + "**Currnet running process:" + process + "**");
                /*// dispatch thread suspend
                while (needWait) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }*/
               /* runningProcess.remove(0);
                try {
                    process = (ProcessPCB) process.clone();
                    runningProcess.add(process);
                } catch (CloneNotSupportedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }*/
                // current process run for one second in every circle
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // increase the timeCounter
                timeCounter++;
                progress = (timeCounter * 1.0) / totalServiceTime;
                
                mainController.getProgressBar().setProgress(progress);
                mainController.getPercentage().setText("    " + String.format("%.1f", progress * 100) + "%");
                // other process wait for the currnet process to finish
                for (ProcessPCB process : readyQueue) {
                    process.setWaitTime(process.getWaitTime() + 1);
                    process.setWaitTimeProperty(process.getWaitTime());
                }
                    
               /* ObservableList<ProcessPCB> readyQueueTemp = FXCollections.observableArrayList();
                for(ProcessPCB process :readyQueue) {
                    try {
                        ProcessPCB processPCB = (ProcessPCB) process.clone();
                        processPCB.setWaitTime(processPCB.getWaitTime()+1);
                        readyQueueTemp.add(processPCB);
                    } catch (CloneNotSupportedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                readyQueue.clear();
                readyQueue.addAll(readyQueueTemp);
                synchronizeReadyQueue.clear();
                synchronizeReadyQueue.addAll(readyQueue);*/
                //synchronizeReadyQueue.sort(new ProcessComparator(mode));
                //isAllowdAdd = true;
                // if the clearSignal is true,end the thread
                if (clearSignal) {
                    clearSignal = false;
                    // reset the processCounter
                    processCounter = 0;
                    //clear the ready queue and the synchronize ready queue
                    readyQueue.clear();
                    synchronizeReadyQueue.clear();
                    return;
                }
                // dispatch thread suspend
                while (needWait) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if(isRequestAdd) {
                    isRequestAdd = false;
                    while(!isFinishAdd) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    isFinishAdd = false;
                }
                /*for(ProcessPCB process : synchronizeReadyQueue)
                    process.setWaitTime(process.getWaitTime() + 1);*/
                if (process.getRemainingTime() == 0) {
                    // currentTime = System.currentTimeMillis() / 1000;
                    // set the end time of process
                    // process.setEndTime((int) (currentTime - startTime));
                    process.setEndTime(timeCounter);
                    process.setEndTimeProperty(process.getEndTime());
                    // compute the turnaround time
                    process.setTurnaroundTime(process.getWaitTime() + process.getServiceTime());
                    process.setTurnAroundTimeProperty(process.getTurnaroundTime());
                    // compute the normalized turnaround time
                    process.setNormalizedTurnaroundTime(
                            (double) process.getTurnaroundTime() / process.getServiceTime());
                    process.setNormalizedTurnAroundTimeProperty(process.getNormalizedTurnaroundTime());
                    // change the status of process:finish
                    process.setStatus(2);// represents process completed
                    // join to the finishQueue
                    // finishQueue.addFirst(process);
                    finishQueue.add(process);
                    sizeOfReadyQueue--;
                    process.setFinish(true);
                    // process = null;
                    if (readyQueue.isEmpty()) {
                        /*
                         * process = null; System.out.
                         * println("--All processes have been completed!--" );
                         * // print the finishQueue for (ProcessPCB process :
                         * finishQueue) System.out.println(process); break;
                         */
                        /*
                         * while (readyQueue.isEmpty()) System.out.
                         * println("wait the process added to the ready queue"
                         * ); try { Thread.currentThread().sleep(1000); } catch
                         * (InterruptedException e) { // TODO Auto-generated
                         * catch block e.printStackTrace(); }
                         */
                        System.out.println("--All processes have been completed!--");
                        // print the finishQueue
                        for (ProcessPCB process : finishQueue)
                            System.out.println(process);
                        break;
                    } else {
                        // if current process finish,run next process which has
                        // highest priority
                        process = readyQueue.remove(0);
                        // currentTime = System.currentTimeMillis() / 1000;
                        // process.setStartTime((int) currentTime - (int)
                        // startTime);
                        
                        mainController.getRunProcessText().setText(process.getpName());
                        if (process.isFirstTime()) {
                            process.setStartTime(timeCounter);
                            process.setStartTimeProperty(process.getStartTime());
                            process.setFirstTime(false);
                            process.setHasRun(true);
                        }
                      //as the running proccess 
                        runningProcess.remove(0);
                        runningProcess.add(process);
                    }
                }
            }

         // give a tip of finishing the dispatch
            JOptionPane.showMessageDialog(null, "���н��̵�����ɣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
        }
        public void HRRNDispatch() {
            // run the first process which has highest priority
            // first process to be run
            
            synchronizeReadyQueue.sort(new ProcessComparator(3));
            
            // process = readyQueue.removeLast();
            //process = readyQueue.remove(0);
            process = synchronizeReadyQueue.remove(0);
            readyQueue.remove(process);

            /*
             * startTime = System.currentTimeMillis() / 1000; currentTime =
             * startTime;
             */
            // add to the running process
            runningProcess.add(process);
            process.setStartTime(timeCounter);
            process.setStartTimeProperty(process.getStartTime());
            process.setHasRun(true);
            process.setFirstTime(false);
            mainController.getRunProcessText().setText(process.getpName());
            while (true) {
                
                //isAllowdAdd = false;
                process.setStatus(1);// 1 represents running
                process.setRunTime(process.getRunTime() + 1);
                process.setRunTimeProperty(process.getRunTime());
                process.setRemainingTime(process.getRemainingTime() - 1);
                process.setRemainingTimeProperty(process.getRemainingTime());
                System.out.println("�����Ӧ��" + "**Currnet running process:" + process + "**");
                /*// dispatch thread suspend
                while (needWait) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }*/
                /*runningProcess.remove(0);
                try {
                    process = (ProcessPCB) process.clone();
                    runningProcess.add(process);
                } catch (CloneNotSupportedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }*/
                // current process run for one second in every circle
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // increase the timeCounter
                timeCounter++;
                progress = (timeCounter * 1.0) / totalServiceTime;
                
                mainController.getProgressBar().setProgress(progress);
                mainController.getPercentage().setText("    " + String.format("%.1f", progress * 100) + "%");
                // other process wait for the currnet process to finish
                /*for (ProcessPCB process : readyQueue)
                    process.setWaitTime(process.getWaitTime() + 1);*/
               /* ObservableList<ProcessPCB> readyQueueTemp = FXCollections.observableArrayList();
                for(ProcessPCB process :readyQueue) {
                    try {
                        ProcessPCB processPCB = (ProcessPCB) process.clone();
                        processPCB.setWaitTime(processPCB.getWaitTime()+1);
                        readyQueueTemp.add(processPCB);
                    } catch (CloneNotSupportedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                readyQueue.clear();
                readyQueue.addAll(readyQueueTemp);
                synchronizeReadyQueue.clear();
                synchronizeReadyQueue.addAll(readyQueue);
                synchronizeReadyQueue.sort(new ProcessComparator(3));*/
                //isAllowdAdd = true;
                // if the clearSignal is true,end the thread
                if (clearSignal) {
                    clearSignal = false;
                    // reset the processCounter
                    processCounter = 0;
                    //clear the ready queue and the synchronize ready queue
                    readyQueue.clear();
                    synchronizeReadyQueue.clear();
                    return;
                }
                // dispatch thread suspend
                while (needWait) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if(isRequestAdd) {
                    isRequestAdd = false;
                    while(!isFinishAdd) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    isFinishAdd = false;
                }
                /*for(ProcessPCB process : synchronizeReadyQueue)
                    process.setWaitTime(process.getWaitTime() + 1);*/
                if (process.getRemainingTime() == 0) {
                    // currentTime = System.currentTimeMillis() / 1000;
                    // set the end time of process
                    // process.setEndTime((int) (currentTime - startTime));
                    process.setEndTime(timeCounter);
                    process.setEndTimeProperty(process.getEndTime());
                    // compute the turnaround time
                    process.setTurnaroundTime(process.getWaitTime() + process.getServiceTime());
                    process.setTurnAroundTimeProperty(process.getTurnaroundTime());
                    // compute the normalized turnaround time
                    process.setNormalizedTurnaroundTime(
                            (double) process.getTurnaroundTime() / process.getServiceTime());
                    process.setNormalizedTurnAroundTimeProperty(process.getNormalizedTurnaroundTime());
                    // change the status of process:finish
                    process.setStatus(2);// represents process completed
                    // join to the finishQueue
                    // finishQueue.addFirst(process);
                    finishQueue.add(process);
                    sizeOfReadyQueue--;
                    process.setFinish(true);
                    // process = null;
                    if (readyQueue.isEmpty()) {
                        /*
                         * process = null; System.out.
                         * println("--All processes have been completed!--" );
                         * // print the finishQueue for (ProcessPCB process :
                         * finishQueue) System.out.println(process); break;
                         */
                        /*
                         * while (readyQueue.isEmpty()) System.out.
                         * println("wait the process added to the ready queue"
                         * ); try { Thread.currentThread().sleep(1000); } catch
                         * (InterruptedException e) { // TODO Auto-generated
                         * catch block e.printStackTrace(); }
                         */
                        System.out.println("--All processes have been completed!--");
                        // print the finishQueue
                        for (ProcessPCB process : finishQueue)
                            System.out.println(process);
                        break;
                    } else {
                        // if current process finish,run next process which has
                        // highest priority
                        //process = readyQueue.remove(0);
                        process = synchronizeReadyQueue.remove(0);
                        readyQueue.remove(process);
                        // currentTime = System.currentTimeMillis() / 1000;
                        // process.setStartTime((int) currentTime - (int)
                        // startTime);
                        mainController.getRunProcessText().setText(process.getpName());
                        if (process.isFirstTime()) {
                            process.setStartTime(timeCounter);
                            process.setStartTimeProperty(process.getStartTime());
                            process.setFirstTime(false);
                            process.setHasRun(true);
                        }
                        //as the running proccess 
                        runningProcess.remove(0);
                        runningProcess.add(process);
                    }
                } 
            }

         // give a tip of finishing the dispatch
            JOptionPane.showMessageDialog(null, "���н��̵�����ɣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
        }
        //feedback dispatch
        public void FBDispatch() {
            int timeSlicing = 0;
            if (!readyQueue.isEmpty()) {
                process = synchronizeReadyQueue.remove(0);
                readyQueue.remove(process);
                // add the process to running table
                runningProcess.add(process);
                /*
                 * startTime = System.currentTimeMillis() / 1000; currentTime =
                 * startTime;
                 */
                process.setStartTime(timeCounter);
                process.setStartTimeProperty(process.getStartTime());
                process.setFirstTime(false);
                process.setHasRun(true);
                mainController.getRunProcessText().setText(process.getpName());
            } else {
                System.out.println("There is no  process in ready queue! ");
                JOptionPane.showMessageDialog(null, "�������������κν��̣����ȴ���һЩ���̣�", "��ʾ", JOptionPane.WARNING_MESSAGE);
                return;
            }
            while (true) {

                /*when current process has't run for a competed a unit of time
                 *it is not allowed to add new process to the reayd queue in order 
                 *to make sure the time is right
                 */
                //isAllowdAdd = false;
                process.setStatus(1);// 1 represents running
                process.setRunTime(process.getRunTime() + 1);
                process.setRunTimeProperty(process.getRunTime());
                process.setRemainingTime(process.getRemainingTime() - 1);
                process.setRemainingTimeProperty(process.getRemainingTime());
                System.out.println("����" + "**Currnet running process:" + process + "**");
                // update the runningprocess,the runningprocess list will be
                // updated only the quote of current process is changed
                /*runningProcess.remove(0);
                try {
                    process = (ProcessPCB) process.clone();
                    runningProcess.add(process);
                } catch (CloneNotSupportedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }*/
                // current process has run for one seconds,increase the time
                // slicing
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // increase the time counter
                timeCounter++;
                progress = (timeCounter * 1.0) / totalServiceTime;
    
                mainController.getProgressBar().setProgress(progress);
                mainController.getPercentage().setText("    " + String.format("%.1f", progress * 100) + "%");
                
                timeSlicing++;
                // other process wait for the time slicing
                for (ProcessPCB process : readyQueue) {
                    process.setWaitTime(process.getWaitTime() + 1);
                    process.setWaitTimeProperty(process.getWaitTime());
                }
                /*ObservableList<ProcessPCB> readyQueueTemp = FXCollections.observableArrayList();
                for(ProcessPCB process :readyQueue) {
                    try {
                        ProcessPCB processPCB = (ProcessPCB) process.clone();
                        processPCB.setWaitTime(processPCB.getWaitTime()+1);
                        readyQueueTemp.add(processPCB);
                    } catch (CloneNotSupportedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                readyQueue.clear();
                readyQueue.addAll(readyQueueTemp);
                synchronizeReadyQueue.clear();
                synchronizeReadyQueue.addAll(readyQueue);*/
                //synchronizeReadyQueue.sort(new ProcessComparator(mode));
                // if the clearSignal is true,end the thread
                if (clearSignal) {
                    clearSignal = false;
                    // reset the processCounter
                    processCounter = 0;
                    //clear the ready queue and the synchronize ready queue
                    readyQueue.clear();
                    synchronizeReadyQueue.clear();
                    return;
                }
                // dispatch thread suspend
                while (needWait) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if(isRequestAdd) {
                    isRequestAdd = false;
                    while(!isFinishAdd) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    isFinishAdd = false;
                    //System.out.println("�����ɣ���ǰʱ��:" + timeCounter);
                }
                
                if(process.getRemainingTime() == 0) {
                    process.setEndTime(timeCounter);
                    process.setEndTimeProperty(process.getEndTime());
                    // compute the turnaround time
                    process.setTurnaroundTime(process.getWaitTime() + process.getServiceTime());
                    process.setTurnAroundTimeProperty(process.getTurnaroundTime());
                    // compute the normalized turnaround time
                    process.setNormalizedTurnaroundTime(
                            (double) process.getTurnaroundTime() / process.getServiceTime());
                    process.setNormalizedTurnAroundTimeProperty(process.getNormalizedTurnaroundTime());
                    // change the status of process:finish
                    process.setStatus(2);// represents process completed
                    process.setFinish(true);
                    //add the current process to the finish queue
                    finishQueue.add(process);
                    
                    sizeOfReadyQueue--;
                    if(timeSlicing >= TIMESLICING) {
                        timeSlicing = 0;
                    }
                    if(!readyQueue.isEmpty()) {
                        //get next process to be run
                        process = synchronizeReadyQueue.remove(0);
                        readyQueue.remove(process);
                        runningProcess.remove(0);
                        runningProcess.add(process);
                        mainController.getRunProcessText().setText(process.getpName());
                        if (process.isFirstTime()) {
                            System.out.println(process.getpName() + "��ʼʱ��:" + timeCounter);
                            process.setStartTime(timeCounter);
                            process.setStartTimeProperty(process.getStartTime());
                            process.setFirstTime(false);
                            process.setHasRun(true);
                            
                        }
                        //as the running proccess 
                        runningProcess.remove(0);
                        runningProcess.add(process);
                        
                    }else {
                        System.out.println("--All processes have been completed!--");
                        // print the finishQueue
                        for (ProcessPCB process : finishQueue)
                            System.out.println(process);
                        break;//dispatch over
                    }
                }else {
                    if(timeSlicing >= TIMESLICING) {
                        //the prioirty decrease every time the current process is preempted 
                        process.setPriority(process.getPriority() - 1);
                        process.setPriorityProperty(process.getPriority());
                        //add current running process to the ready queue
                        readyQueue.add(process);
                        synchronizeReadyQueue.add(process);
                        synchronizeReadyQueue.sort(new ProcessComparator(0));
                        //get next process to be run ,may be still the current running process
                        process = synchronizeReadyQueue.remove(0);
                        readyQueue.remove(process);
                        runningProcess.remove(0);
                        runningProcess.add(process);
                        mainController.getRunProcessText().setText(process.getpName());
                        if (process.isFirstTime()) {
                            //System.out.println(process.getpName() + "��ʼʱ��:" + timeCounter);
                            process.setStartTime(timeCounter);
                            process.setStartTimeProperty(process.getStartTime());
                            process.setFirstTime(false);
                            process.setHasRun(true);
                            
                        }
                        //as the running proccess 
                        runningProcess.remove(0);
                        runningProcess.add(process);
                        //reset the timeslice
                        timeSlicing = 0;
                    }
                }

            }
         // give a tip of finishing the dispatch
            JOptionPane.showMessageDialog(null, "���н��̵�����ɣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
        }
    }
    //this thread is responsible for adding the process in waitqueue to readyQueue dynamically
    private Thread addProcessThread = new Thread(new AddProcessRun());
    public Dispatcher() {
       /* //create main2 stage
        mainStage2 = new Stage();
        mainStage2.setTitle("���̵�����");
        mainStage2.getIcons().add(new Image("/images/scheduler.png"));
        mainStage2.setResizable(false);
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(getClass().getResource("../view/MainView2.fxml"));
        Pane mainPane2 = null;
        try {
            mainPane2 = mainLoader.load();
        }catch(IOException e) {
            System.out.println("�ļ�δ����");
        }
        Scene mainScene2 = new Scene(mainPane2);
        //get the mainController2
        mainController2 = mainLoader.getController();
        mainController2.setDispatcher(this);
        mainController2.setInitialData();
        mainStage2.setScene(mainScene2);*/
        //create start stage
        startStage = new Stage();
        startStage.setTitle("��������");
        startStage.getIcons().add(new Image("/images/scheduler.png"));
        startStage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../view/StartView.fxml"));
        Pane startPane = null;
        try {
            startPane = fxmlLoader.load();
        }catch(IOException e) {
            System.out.println("�ļ�δ����!");
        }
        Scene startScene = new Scene(startPane);
        startStage.setScene(startScene);
        
        // create popup Stage
        popupStage = new Stage();
        popupStage.setTitle("������ʾ");
        popupStage.getIcons().add(new Image("/images/tip.png"));
        popupStage.setResizable(false);
        popupStage.initModality(Modality.WINDOW_MODAL);
        // create fxml loader to load the fxml file in order to generate the
        // pane
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/PopupView.fxml"));
        Pane popupPane = null;
        try {
            popupPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("�Ҳ����ļ�");
        }
        // create scene with the popupPane;
        Scene popupScene = new Scene(popupPane);
        StartController controller = fxmlLoader.getController();
        controller.setDispatcher(this);
        // set scene on the stage
        popupStage.setScene(popupScene);
        // gain the popup controller
        popupController = loader.getController();
        popupController.setDispatcher(this);
        // create warn Stage
        warnStage = new Stage();
        warnStage.setTitle("����");
        warnStage.getIcons().add(new Image("/images/tip.png"));
        warnStage.initModality(Modality.WINDOW_MODAL);
        // create fxml loader to load the fxml file of waringView
        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getResource("../view/WarningView.fxml"));
        Pane warnPane = null;
        try {
            warnPane = loader2.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("�ļ�δ����");
        }
        // create the warn scene
        Scene warnScene = new Scene(warnPane);
        warnStage.setScene(warnScene);
        warnController = loader2.getController();
        warnController.setDispatcher(this);
        // create another fxml loader to load the fxml file of mainview
        FXMLLoader loader1 = new FXMLLoader();
        loader1.setLocation(getClass().getResource("../view/MainView.fxml"));
        Pane mainViewPane = null;

        try {
            // generate the main pane
            mainViewPane = loader1.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("�Ҳ����ļ�");
        }
        // create mainview scene
        mainScene = new Scene(mainViewPane);
        // get the main controller
        mainController = loader1.getController();
        mainController.setDispatcher(this);
        mainController.setInitialData();
        processStage = new Stage();
        processStage.setTitle("������Ϣ");
        processStage.getIcons().add(new Image("/images/tip.png"));
        processStage.setResizable(false);
        processStage.initModality(Modality.WINDOW_MODAL);
        FXMLLoader loader3 = new FXMLLoader();
        loader3.setLocation(getClass().getResource("../view/ProcessDetailView.fxml"));
        Pane processPane = null;
        try {
            processPane = loader3.load();
        }catch(IOException e) {
            System.out.println("�ļ�δ����!");
        }
        Scene processScene = new Scene(processPane);
        processStage.setScene(processScene);
        processController = loader3.getController();
        processController.setDispatcher(this);
        
    }

    @Override
    public void start(Stage mainStage) {
        try {
            /*
             * BorderPane root = new BorderPane(); Scene scene = new
             * Scene(root,400,400);
             * scene.getStylesheets().add(getClass().getResource(
             * "application.css").toExternalForm());
             * 
             */
            this.mainStage = mainStage;
            mainStage.setTitle("���̵�����");
            mainStage.getIcons().add(new Image("images/scheduler.png"));
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            popupStage.initOwner(mainStage);
            warnStage.initOwner(mainStage);
            processStage.initOwner(mainStage);
            //mainStage.show();
            //popupStage.showAndWait();
            startStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);

    }

    /**
     * Time slice round robin dispatch
     * 
     * @param readyQueue
     */

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

    public ObservableList<ProcessPCB> getReadyQueue() {
        return readyQueue;
    }

    public ObservableList<ProcessPCB> getWaitQueue() {
        return waitQueue;
    }

    public ObservableList<ProcessPCB> getFinishQueue() {
        return finishQueue;
    }

    public ObservableList<ProcessPCB> getRunningProcess() {
        return runningProcess;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime(long startTime) {
        Dispatcher.startTime = startTime;
    }

    public static long getCurrentTime() {
        return currentTime;
    }

    public static void setCurrentTime(long currentTime) {
        Dispatcher.currentTime = currentTime;
    }

    public static int getProcessCounter() {
        return processCounter;
    }

    public static void setProcessCounter(int processCounter) {
        Dispatcher.processCounter = processCounter;
    }

    public static int getProcessmaxnum() {
        return PROCESSMAXNUM;
    }

    public ProcessPCB getProcess() {
        return process;
    }

    public void setProcess(ProcessPCB process) {
        this.process = process;
    }

    public Thread getUpdateReadyQueue() {
        return updateReadyQueue;
    }

    public Thread getDispathThread() {
        return dispathThread;
    }

    public int getSchedulingStrategy() {
        return schedulingStrategy;
    }

    public void setSchedulingStrategy(int schedulingStrategy) {
        this.schedulingStrategy = schedulingStrategy;
    }

    public boolean isContention() {
        return isContention;
    }

    public void setContention(boolean isContention) {
        this.isContention = isContention;
    }

    public boolean isNeedWait() {
        return needWait;
    }

    public void setNeedWait(boolean needWait) {
        this.needWait = needWait;
    }

    /*
     * public boolean isHasStartDispatch() { return hasStartDispatch; }
     * 
     * public void setHasStartDispatch(boolean hasStartDispatch) {
     * this.hasStartDispatch = hasStartDispatch; }
     */
    public void notifyDispatchThread() {
        synchronized (this) {
            this.notify();
        }
    }

    public void createNewDispatchThread() {
        dispathThread = new Thread(new DispatchRun());
    }

    public boolean isClearSignal() {
        return clearSignal;
    }

    public void setClearSignal(boolean clearSignal) {
        this.clearSignal = clearSignal;
    }

    public Stage getWarnStage() {
        return warnStage;
    }

    public WarningController getWarnController() {
        return warnController;
    }

    public static int getTimeCounter() {
        return timeCounter;
    }

    public static void setTimeCounter(int timeCounter) {
        Dispatcher.timeCounter = timeCounter;
    }

    public boolean iSFirstThread() {
        return isFirstThread;
    }

    public void setIsFirstThread(boolean siFirstThread) {
        this.isFirstThread = siFirstThread;
    }

    public ObservableList<ProcessPCB> getSynchronizeReadyQueue() {
        return synchronizeReadyQueue;
    }

    public boolean isAllowdAdd() {
        return isAllowdAdd;
    }

    public void setAllowdAdd(boolean isAllowdAdd) {
        this.isAllowdAdd = isAllowdAdd;
    }

    public int getTotalServiceTime() {
        return totalServiceTime;
    }

    public void setTotalServiceTime(int totalServiceTime) {
        this.totalServiceTime = totalServiceTime;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public boolean isRequestAdd() {
        return isRequestAdd;
    }

    public void setRequestAdd(boolean isRequestAdd) {
        this.isRequestAdd = isRequestAdd;
    }

    public boolean isFinishAdd() {
        return isFinishAdd;
    }

    public void setFinishAdd(boolean isFinishAdd) {
        this.isFinishAdd = isFinishAdd;
    }

    public static int getTIMESLICING() {
        return TIMESLICING;
    }

    public static void setTIMESLICING(int tIMESLICING) {
        TIMESLICING = tIMESLICING;
    }

    public Thread getAddProcessThread() {
        return addProcessThread;
    }
    
    public void createNewAddThread() {
        addProcessThread = new Thread(new AddProcessRun());
    }

    public int getSizeOfReadyQueue() {
        return sizeOfReadyQueue;
    }

    public void setSizeOfReadyQueue(int sizeOfReadyQueue) {
        this.sizeOfReadyQueue = sizeOfReadyQueue;
    }

    public Stage getStartStage() {
        return startStage;
    }

    public Stage getProcessStage() {
        return processStage;
    }

    public ProcessDetailController getProcessController() {
        return processController;
    }

    public Thread getRanddomAddProcessThread() {
        return randdomAddProcessThread;
    }

    public Thread getAddAProcessThread() {
        return addAProcessThread;
    }

    public Stage getMainStage2() {
        return mainStage2;
    }

    public MainController2 getMainController2() {
        return mainController2;
    }

    public int getSchedulingChoice() {
        return schedulingChoice;
    }

    public void setSchedulingChoice(int schedulingChoice) {
        this.schedulingChoice = schedulingChoice;
    }

    public ArrayList<Integer> getTimeList() {
        return timeList;
    }
    
}
