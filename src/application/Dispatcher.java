package application;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.ProcessPCB;
import view.MainController;
import view.PopupController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Dispatcher extends Application {
    private static long startTime = -1;
    private static long currentTime;
    private static int processCounter = 0;// as the the process id
    private final static int PROCESSMAXNUM = 10;// maximum number of processe
                                                // allowed
    private final static int TIMESLICING = 3;// time slice
    private Stage mainStage;
    private Scene mainScene;
    private Stage popupStage;
    private PopupController popupController;
    private MainController mainController;
    // ready queue
    private ObservableList<ProcessPCB> readyQueue = FXCollections.observableArrayList();
    // wait queue
    private ObservableList<ProcessPCB> waitQueue = FXCollections.observableArrayList();
    // finish queue
    private ObservableList<ProcessPCB> finishQueue = FXCollections.observableArrayList();
    // running
    private ObservableList<ProcessPCB> runningProcess = FXCollections.observableArrayList();
    // current running process
    private ProcessPCB process;
    //scheduling strategy 
    private int schedulingStrategy;//0.rr 1.priority 2.SPN 3.SRT
    //contention strategy
    private boolean isContention;
    //create a thread to perform scheduling
    private Thread dispathThread = new Thread(new Runnable() {
        
        @Override
        public void run() {
            System.out.println("start the dispatch thread!");
            
                switch(schedulingStrategy) {
                    case 0:
                        rrDispatcher();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
                        
            
            
        }
        public void rrDispatcher() {
               int timeSlicing = 0;
               if (!readyQueue.isEmpty()) {
                   // first process to be run
                   // process = readyQueue.remove(0);
                   removeSignal = true;
                   // wait for the readyQueue remove first process
                   while (true) {
                       try {
                           Thread.currentThread().sleep(100);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                       if (process != null)
                           break;
                   }
                   System.out.println(process);
                   // add the process to running table
                   runningProcess.add(process);
                   startTime = System.currentTimeMillis() / 1000;
                   currentTime = startTime;
                   process.setStartTime((int) (currentTime - startTime));
                   process.setFirstTime(false);
                   process.setHasRun(true);
               } else {
                   System.out.println("There is no  process in ready queue! ");
                   JOptionPane.showMessageDialog(null, "就绪队列中无任何进程，请先创建一些进程！", "提示", JOptionPane.WARNING_MESSAGE);
                   return;
               }
               while (true) {
                   process.setStatus(1);// 1 represents running
                   process.setRunTime(process.getRunTime() + 1);
                   process.setRemainingTime(process.getRemainingTime() - 1);
                   System.out.println("**Currnet running process:" + process + "**");
                   // current process has run for one seconds,increase the time slicing
                   try {
                       Thread.currentThread().sleep(1000);
                   } catch (InterruptedException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
                   }
                   timeSlicing++;
                   // if process finish
                   if (process.getRemainingTime() == 0) {
                       currentTime = System.currentTimeMillis() / 1000;
                       // set the end time of process
                       process.setEndTime((int) (currentTime - startTime));
                       // compute the turnaround time
                       process.setTurnaroundTime(process.getWaitTime() + process.getServiceTime());
                       // compute the normalized turnaround time
                       process.setNormalizedTurnaroundTime((double) process.getTurnaroundTime() / process.getServiceTime());
                       // change the status of process:finish
                       process.setStatus(2);
                       // join to the finishQueue
                       finishQueue.add(process);
                       process.setFinish(true);
                       if (readyQueue.isEmpty()) {
                           // process = null;
                           System.out.println("--All processes have been completed!--");
                           // print the finishQueue
                           
                             for (ProcessPCB process : finishQueue)
                             System.out.println(process);
                            
                           break;
                       }
                   } else if (timeSlicing >= TIMESLICING) {// join in the readyQueue
                                                           // when time out
                       // change the status of process
                       process.setStatus(0);
                       // join in the readyQueue
                       //readyQueue.add(process);
                       addSignal = true;
                       //wait for the current thread added to the reaydQueue when time out
                       while(true) {
                           try {
                               Thread.currentThread().sleep(100);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           if(!addSignal)
                               break;
                       }
                   }

                   if (!readyQueue.isEmpty()) {
                       if (timeSlicing >= TIMESLICING || process.isFinish()) {
                           // next process to be run
                           //process = readyQueue.remove(0);
                           removeSignal = true;//send the remove signal to the updateReadyQueueThread
                           //wait for the next process to be run
                           while(true) {
                               try {
                                   Thread.currentThread().sleep(100);
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }
                               if(!removeSignal)
                                   break;
                           }
                           // other process wait for the time slicing
                           /*for (ProcessPCB process : readyQueue)
                               process.setWaitTime(process.getWaitTime() + 1);*/
                           waitSignal = true;//send waitSignal to the updateReadyQueueThread
                           // record the first time to start
                           if (process.isFirstTime()) {
                               currentTime = System.currentTimeMillis() / 1000;
                               process.setStartTime((int) (currentTime - startTime));
                               process.setFirstTime(false);
                               process.setHasRun(true);
                           }

                           // clear the time slicing for every process
                           timeSlicing = 0;
                       }
                   }
               }
               // rmove the last completed process from runningProcess table
               runningProcess.remove(0);
           }
    });
    // this thread is responsible for updating the readyQueue
    private Thread updateReadyQueue = new Thread(new Runnable() {

        @Override
        public void run() {
            System.out.println("update thread lauch");

            while (true) {
                if (removeSignal && !readyQueue.isEmpty()) {
                    process = readyQueue.remove(0);
                    removeSignal = false;
                }else if(addSignal) {
                    readyQueue.add(process);
                    addSignal = false;
                }else if(waitSignal) {
                    for (ProcessPCB process : readyQueue)
                        process.setWaitTime(process.getWaitTime() + 1);
                    waitSignal = false;
                }
            }
        }
    });
    // create some signal to realize the communication among threads
    // signal for updating the ready queue
    private boolean removeSignal;// signal for removing process
    private boolean addSignal;// signal for adding process
    private boolean sortSignal;// signal for sorting the list
    private boolean waitSignal;//signal for increase the wait time
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
    public Dispatcher() {
        // create popup Stage
        popupStage = new Stage();
        popupStage.setTitle("输入提示");
        popupStage.getIcons().add(new Image("/images/tip.png"));
        popupStage.setResizable(false);
        // create fxml loader to load the fxml file in order to generate the
        // pane
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/PopupView.fxml"));
        Pane popupPane = null;
        try {
            popupPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("找不到文件");
        }
        // create scene with the popupPane;
        Scene popupScene = new Scene(popupPane);
        // set scene on the stage
        popupStage.setScene(popupScene);
        // gain the popup controller
        popupController = loader.getController();
        popupController.setDispatcher(this);
        // create another fxml loader to load the fxml file of mainview
        FXMLLoader loader1 = new FXMLLoader();
        loader1.setLocation(getClass().getResource("../view/MainView.fxml"));
        Pane mainViewPane = null;

        try {
            // generate the main pane
            mainViewPane = loader1.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("找不到文件");
        }
        // create mainview scene
        mainScene = new Scene(mainViewPane);
        // get the main controller
        mainController = loader1.getController();
        mainController.setDispatcher(this);
        mainController.setInitialData();
    }
    
    
    @Override
    public void start(Stage mainStage) {
        try {
            /*
             * BorderPane root = new BorderPane(); Scene scene = new
             * Scene(root,400,400);
             * scene.getStylesheets().add(getClass().getResource(
             * "application.css").toExternalForm());
             */
            mainStage.setTitle("进程调度器");
            mainStage.getIcons().add(new Image("images/scheduler.png"));
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            mainStage.show();
            popupStage.showAndWait();
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

    
    
}
