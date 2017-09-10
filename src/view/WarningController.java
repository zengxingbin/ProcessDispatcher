package view;

import application.Dispatcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
/**
 * 
 * @author ÔøÐÒ±ò(bingoo)
 * 2017Äê9ÔÂ10ÈÕ
 */
public class WarningController {
    private Dispatcher dispatcher;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Text text;
    @FXML
    public void confirm() {
        dispatcher.getWarnStage().close();
        //about whether reset
        dispatcher.getMainController().setSureReset(true);
        //about whether exit
        dispatcher.getMainController().setSureExit(true);
      //let the dispatch thread continue to run if it is suspended
        dispatcher.setNeedWait(false);
    }
    @FXML
    public void cancel() {
        dispatcher.getWarnStage().close();
        //let the dispatch thread continue to run if it is suspended
        dispatcher.setNeedWait(false);
    }
    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    public Text getText() {
        return text;
    }
    
}
