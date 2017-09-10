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
 * 2017年9月10日
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
        int proNum;
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
        // when the dispatchThread does't start or it finish,reset the start
        // time and current time
        if (!dispatcher.getDispathThread().isAlive()) {
            /*dispatcher.setStartTime(System.currentTimeMillis() / 1000);
            dispatcher.setCurrentTime(dispatcher.getStartTime());*/
            dispatcher.setTimeCounter(0);
        }
        // random generate the priority and service time of process
        Random rand = new Random();
        for (int i = 0; i < proNum; i++) {
            ProcessPCB process = new ProcessPCB();
            // initialize process
            process.setpName("process" + dispatcher.getProcessCounter());
            process.setPid(dispatcher.getProcessCounter());
            dispatcher.setProcessCounter(dispatcher.getProcessCounter() + 1);
            process.setPriority(rand.nextInt(20));
            process.setStatus(0);
            process.setFirstTime(true);
            process.setServiceTime(rand.nextInt(10) + 1);
            process.setRemainingTime(process.getServiceTime());
            // join in the readyQueue
            if (dispatcher.getReadyQueue().size() < dispatcher.getProcessmaxnum()) {
                //process.setArrivalTime((int) (dispatcher.getCurrentTime() - dispatcher.getStartTime()));
                if(!isFirstPopup && dispatcher.getDispathThread().isAlive()) {
                    /*int count = 0;
                    while(true) {
                        //avoid current thread to wait all the time 
                          if(count >= 1000)
                              break;
                          try {
                              Thread.currentThread().sleep(100);
                          } catch (InterruptedException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                          }
                          count += 100;
                          if(dispatcher.isAllowdAdd())
                              break;
                      }*/
                    dispatcher.setRequestAdd(true);
                    while(dispatcher.isRequestAdd()) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }else {
                    isFirstPopup = false;
                }
                
                process.setArrivalTime(dispatcher.getTimeCounter());
                // record current time
                //dispatcher.setCurrentTime(System.currentTimeMillis() / 1000);
                dispatcher.getReadyQueue().add(process);
                //add a new process,so the totalServiceTime should increase
                dispatcher.setTotalServiceTime(dispatcher.getTotalServiceTime() + process.getServiceTime());
                // add to the synchronized queue at the same time
                dispatcher.getSynchronizeReadyQueue().add(process);
                if(dispatcher.getDispathThread().isAlive()) {
                    //judge what the schedulingStrategy is 
                    if("优先数调度(HPN)".equals(dispatcher.getMainController().getSchedulingStrategy().getValue())) {
                        //if the strategy is round robin time,sort the readyQueue every time you add new process
                        //dispatcher.getReadyQueue().sort(new PriorityComparator(0));
                      //if the strategy is round robin time,sort the synchronizedreadyQueue every time you add new process
                        dispatcher.getSynchronizeReadyQueue().sort(new ProcessComparator(0));
                        
                    }else if("最短进程优先(SPN)".equals(dispatcher.getMainController().getSchedulingStrategy().getValue())) {
                     // if the strategy is round robin time,sort the readyQueue according to the serviceTime every time you add new process
                        //dispatcher.getReadyQueue().sort(new PriorityComparator(1));
                     // if the strategy is round robin time,sort the synchronizedreadyQueue according to the serviceTime every time you add new process
                        dispatcher.getSynchronizeReadyQueue().sort(new ProcessComparator(1));
                    }else if("最短剩余时间(SRT)".equals(dispatcher.getMainController().getSchedulingStrategy().getValue())) {
                        //sort the ready queue according to the remaining time;
                        //dispatcher.getReadyQueue().sort(new PriorityComparator(2));
                        //sort the ready queue according to the remaining time;
                        dispatcher.getSynchronizeReadyQueue().sort(new ProcessComparator(2));
                    }
                    else if("先来先服务(FCFS)".equals(dispatcher.getMainController().getSchedulingStrategy().getValue())) {
                        
                    }
                    else if("时间片轮转(RR)".equals(dispatcher.getMainController().getSchedulingStrategy().getValue())) {
                        
                    }else if("最高向颖彬(HRRN)".equals(dispatcher.getMainController().getSchedulingStrategy().getValue())) {
                        dispatcher.getSynchronizeReadyQueue().sort(new ProcessComparator(2));
                    }
                }
                dispatcher.setFinishAdd(true);
            }else {
                //join in the wait queue
                dispatcher.getWaitQueue().add(process);
            }
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

}
