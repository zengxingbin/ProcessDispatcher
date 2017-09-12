package view;

import javax.swing.JOptionPane;

import application.Dispatcher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.ProcessPCB;

public class ProcessDetailController {
    private Dispatcher dispatcher;
    @FXML
    private Label processIdLabel;
    @FXML
    private Label processArrivalTimeLabel;
    @FXML
    private Label processStartTimeLabel;
    @FXML
    private Label processWaitTimeLabel;
    @FXML
    private Label processRunTimeLabel;
    @FXML
    private Label processRemainTimeLabel;

    @FXML
    private TextField name;
    @FXML
    private TextField priority;
    @FXML
    private TextField serviceTime;
    private ProcessPCB process;

    @FXML
    public void confirmButton() {
        String errorMessage = "";
        if (!dispatcher.getDispathThread().isAlive()) {
            if (name.getText() == null || name.getText().trim().length() == 0)
                errorMessage += "e";
            else if (serviceTime.getText() == null || serviceTime.getText().length() == 0) {
                errorMessage += "r";
            } else if (priority.getText() == null || priority.getText().length() == 0) {
                errorMessage += "r";
            }
            try {
                Integer.parseInt(serviceTime.getText());
                Integer.parseInt(priority.getText());
            } catch (NumberFormatException e) {
                errorMessage += "o";
            }
            if (errorMessage.length() != 0) {
                JOptionPane.showMessageDialog(null, "数据有误，请修改！", "警告", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            process.setpName(name.getText());
            process.setServiceTime(Integer.parseInt(serviceTime.getText()));
            process.setPriority(Integer.parseInt(priority.getText()));
            System.out.println(process);
        }
        name.setDisable(false);
        serviceTime.setDisable(false);
        priority.setDisable(false);
        dispatcher.getProcessStage().close();
        return;
    }

    @FXML
    public void returnButton() {
        name.setDisable(false);
        serviceTime.setDisable(false);
        priority.setDisable(false);
        dispatcher.getProcessStage().close();
        return;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public Label getProcessIdLabel() {
        return processIdLabel;
    }

    public Label getProcessArrivalTimeLabel() {
        return processArrivalTimeLabel;
    }

    public Label getProcessStartTimeLabel() {
        return processStartTimeLabel;
    }

    public Label getProcessWaitTimeLabel() {
        return processWaitTimeLabel;
    }

    public Label getProcessRunTimeLabel() {
        return processRunTimeLabel;
    }

    public Label getProcessRemainTimeLabel() {
        return processRemainTimeLabel;
    }

    public TextField getName() {
        return name;
    }

    public TextField getPriority() {
        return priority;
    }

    public TextField getServiceTime() {
        return serviceTime;
    }

    public void setProcess(ProcessPCB process) {
        this.process = process;
    }

}
