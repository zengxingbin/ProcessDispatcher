package view;

import application.Dispatcher;
import javafx.fxml.FXML;

public class StartController {
    private Dispatcher dispatcher;
    @FXML
    public void realTimeDispath() {
        dispatcher.getStartStage().close();
        dispatcher.getMainStage().show();
        dispatcher.getPopupStage().show();
    }
    @FXML
    public void simulateDispatch() {
        
    }
    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    
}
