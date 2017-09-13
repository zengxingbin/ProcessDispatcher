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
    private Label endTime;
    @FXML
    private Label turnAroundTime;
    @FXML
    private Label normalizedTrunAroundTime;
    @FXML
    private TextField name;
    @FXML
    private TextField priority;
    @FXML
    private TextField serviceTime;
    private ProcessPCB process;
    private TableView<ProcessPCB> queue;
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
                JOptionPane.showMessageDialog(null, "�����������޸ģ�", "����", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            process.setpName(name.getText());
            process.setpNameProperty(process.getpName());
            process.setServiceTime(Integer.parseInt(serviceTime.getText()));
            process.setServiceTimeProperty(process.getServiceTime());
            process.setPriority(Integer.parseInt(priority.getText()));
            process.setPriorityProperty(process.getPriority());
            

        }
        dispatcher.getProcessStage().close();
        queue.getSelectionModel().clearSelection();
        return;
    }

    @FXML
    public void returnButton() {
        dispatcher.getProcessStage().close();
        queue.getSelectionModel().clearSelection();
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
    public void setQueue(TableView<ProcessPCB> queue) {
        this.queue = queue;
    }

    public Label getEndTime() {
        return endTime;
    }

    public Label getTurnAroundTime() {
        return turnAroundTime;
    }

    public Label getNormalizedTrunAroundTime() {
        return normalizedTrunAroundTime;
    }
    
}
