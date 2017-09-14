package view;

import application.Dispatcher;
import javafx.fxml.FXML;

public class StartController {
    private Dispatcher dispatcher;
    @FXML
    public void realTimeDispath() {
        dispatcher.getStartStage().close();
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
        
    }
    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    
}
