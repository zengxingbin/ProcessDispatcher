package model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProcessPCB implements Cloneable{
    private int pid;// process id
    private String pName;// process name
    // status of process:0.ready 1.run 2.finish
    private int status;
    private int priority;// priority of process
    private int arrivalTime;// arrival Time of process
    private int serviceTime;// service time of process
    private int startTime;
    private int endTime;
    private int waitTime;
    private int turnaroundTime;
    private double normalizedTurnaroundTime;// normalizedTurnaroundTime=turnaroundTime/serviceTime
    private int runTime;// Time already running
    private int remainingTime;// remaining time of process
    private boolean isFirstTime;// is start to run first time
    private boolean isFinish;// is the process finish
    private boolean isHasRun;// Has the process run?

    public ProcessPCB() {

    }

    public ProcessPCB(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public double getNormalizedTurnaroundTime() {
        return normalizedTurnaroundTime;
    }

    public void setNormalizedTurnaroundTime(double normalizedTurnaroundTime) {
        this.normalizedTurnaroundTime = normalizedTurnaroundTime;
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public boolean isHasRun() {
        return isHasRun;
    }

    public void setHasRun(boolean isHasRun) {
        this.isHasRun = isHasRun;
    }

    public StringProperty getPidPorperty() {
        return new SimpleStringProperty(Integer.toString(pid));
    }

    public StringProperty getPNameProperty() {
        return new SimpleStringProperty(pName);
    }

    public StringProperty getPriorityProperty() {
        return new SimpleStringProperty(Integer.toString(priority));
    }

    public StringProperty getArrivalTimeProperty() {
        return new SimpleStringProperty(Integer.toString(arrivalTime));
    }

    public StringProperty getServiceTimeProperty() {
        return new SimpleStringProperty(Integer.toString(serviceTime));
    }

    public StringProperty getStartTimeProperty() {
        if (isHasRun)
            return new SimpleStringProperty(Integer.toString(startTime));
        return new SimpleStringProperty("未开始");
    }

    public StringProperty getEndTimeProperty() {
        return new SimpleStringProperty(Integer.toString(endTime));
    }

    public StringProperty getWaitTimeProperty() {
        return new SimpleStringProperty(Integer.toString(waitTime));
    }

    public StringProperty getTurnAroundTimeProperty() {
        return new SimpleStringProperty(Integer.toString(turnaroundTime));
    }

    public StringProperty getNormalizedTurnAroundTimeProperty() {
        return new SimpleStringProperty(Double.toString(normalizedTurnaroundTime));
    }

    public StringProperty getRunTimeproperty() {
        if (isHasRun)
            return new SimpleStringProperty(Integer.toString(runTime));
        return new SimpleStringProperty("未运行");
    }

    public StringProperty getRemainingTimeProperty() {
        return new SimpleStringProperty(Integer.toString(remainingTime));
    }

    public StringProperty getLineProperty() {
        return new SimpleStringProperty("----------");
    }

    @Override
    public String toString() {
        return "ProcessPCB [pid=" + pid + ", pName=" + pName + ", status=" + status + ", priority=" + priority
                + ", arrivalTime=" + arrivalTime + ", serviceTime=" + serviceTime + ", startTime=" + startTime
                + ", endTime=" + endTime + ", waitTime=" + waitTime + ", turnaroundTime=" + turnaroundTime
                + ", normalizedTurnaroundTime=" + normalizedTurnaroundTime + ", runTime=" + runTime + ", remainingTime="
                + remainingTime + ", isFirstTime=" + isFirstTime + ", isFinish=" + isFinish + "]";
    }
    //overwirte the clone method

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return (ProcessPCB)super.clone();
    }
    
}
