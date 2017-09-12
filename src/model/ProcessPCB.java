package model;

import javafx.beans.property.StringProperty;

import java.security.GeneralSecurityException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
/**
 * 
 * @author 曾幸彬(bingoo)
 * email:2045620125@qq.com
 * 2017年9月10日 下午1:07:47
 *
 */
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
    
    private final StringProperty pidProperty = new SimpleStringProperty();
    private final StringProperty pNameProperty = new SimpleStringProperty();
    private final StringProperty priorityProperty = new SimpleStringProperty();
    private final StringProperty arrivalTimeProperty = new SimpleStringProperty();
    private final StringProperty startTimeProperty = new SimpleStringProperty();
    private final StringProperty serviceTimeProperty = new SimpleStringProperty();
    private final StringProperty endTimeProperty = new SimpleStringProperty();
    private final StringProperty waitTimeProperty = new SimpleStringProperty();
    private final StringProperty turnAroundTimeProperty = new SimpleStringProperty();
    private final StringProperty normalizedTurnAroundTimeProperty = new SimpleStringProperty();
    private final StringProperty runTimeProperty = new SimpleStringProperty();
    private final StringProperty remainingTimeProperty = new SimpleStringProperty();
    
    
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
        return pidProperty;
    }

    public StringProperty getPNameProperty() {
        return pNameProperty;
    }

    public StringProperty getPriorityProperty() {
        return priorityProperty;
    }

    public StringProperty getArrivalTimeProperty() {
        return arrivalTimeProperty;
    }

    public StringProperty getServiceTimeProperty() {
        return serviceTimeProperty;
    }

    public StringProperty getStartTimeProperty() {
        if (isHasRun)
            return startTimeProperty;
        return new SimpleStringProperty("未开始");
    }

    public StringProperty getEndTimeProperty() {
        return endTimeProperty;
    }

    public StringProperty getWaitTimeProperty() {
        return waitTimeProperty;
    }

    public StringProperty getTurnAroundTimeProperty() {
        return turnAroundTimeProperty;
    }

    public StringProperty getNormalizedTurnAroundTimeProperty() {
        return normalizedTurnAroundTimeProperty;
    }

    public StringProperty getRunTimeproperty() {
        if (isHasRun)
            return runTimeProperty;
        return new SimpleStringProperty("未运行");
    }

    public StringProperty getRemainingTimeProperty() {
        return remainingTimeProperty;
    }

    public StringProperty getLineProperty() {
        return new SimpleStringProperty("----------");
    }
    
    
    public void setPidProperty(int pid) {
        this.pidProperty.set(Integer.toString(pid));
    }

    public void setpNameProperty(String pName) {
        this.pNameProperty.set(pName);
    }

    public void setPriorityProperty(int priority) {
        this.priorityProperty.set(Integer.toString(priority));
    }

    public void setArrivalTimeProperty(int arrivalTime) {
        this.arrivalTimeProperty.set(Integer.toString(arrivalTime));
    }

    public void setStartTimeProperty(int startTime) {
        this.startTimeProperty.set(Integer.toString(startTime));
    }

    public void setServiceTimeProperty(int serviceTime) {
        this.serviceTimeProperty.set(Integer.toString(serviceTime));
    }

    public void setEndTimeProperty(int endTime) {
        this.endTimeProperty.set(Integer.toString(endTime));
    }

    public void setWaitTimeProperty(int waitTime) {
        this.waitTimeProperty.set(Integer.toString(waitTime));
    }

    public void setTurnAroundTimeProperty(int turnAroundTime) {
        this.turnAroundTimeProperty.set(Integer.toString(turnAroundTime));
    }

    public void setNormalizedTurnAroundTimeProperty(double normalizedTurnAroundTime) {
        this.normalizedTurnAroundTimeProperty.set(Double.toString(normalizedTurnAroundTime));
    }

    public void setRunTimeProperty(int runTime) {
        this.runTimeProperty.set(Integer.toString(runTime));
    }

    public void setRemainingTimeProperty(int remainingTime) {
        this.remainingTimeProperty.set(Integer.toString(remainingTime));
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
