package view;

import application.Dispatcher;
import javafx.fxml.FXML;

public class StartController {
    private Dispatcher dispatcher;
    @FXML
    public void realTimeDispath() {
        dispatcher.getStartStage().close();
        //0 represents real time dispatch 
        dispatcher.setSchedulingChoice(0);
        dispatcher.getMainStage().show();
        //start the add process thread
        dispatcher.getRanddomAddProcessThread().setDaemon(true);
        dispatcher.getRanddomAddProcessThread().start();
        //start the thread of adding a process
        dispatcher.getAddAProcessThread().setDaemon(true);
        dispatcher.getAddAProcessThread().start();
        dispatcher.getPopupStage().show();
    }
    @FXML
    public void simulateDispatch() {
        dispatcher.getStartStage().close();
        //1 represents simulate dispatch
        dispatcher.setSchedulingChoice(1);
        dispatcher.getMainStage2().show();
    }
    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    
}
