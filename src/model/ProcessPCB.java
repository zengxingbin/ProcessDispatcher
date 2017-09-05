package model;

public class ProcessPCB {
    private int pid;//process id
    private String pName;//process name
    // status of process:0.ready 1.run 2.finish
    private int status;
    private int priority;//priority of process
    private int arrivalTime;//arrival Time of process
    private int serviceTime;//service time of process
    private int startTime;
    private int endTime;
    private int waitTime;
    private int turnaroundTime;
    private double normalizedTurnaroundTime;//normalizedTurnaroundTime=turnaroundTime/serviceTime
    private int runTime;//Time already running
    private int remainingTime;//remaining time of process
    private boolean isFirstTime;//is start to run first time
    private boolean isFinish;// is the process finish
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
    @Override
    public String toString() {
        return "ProcessPCB [pid=" + pid + ", pName=" + pName + ", status=" + status + ", priority=" + priority
                + ", arrivalTime=" + arrivalTime + ", serviceTime=" + serviceTime + ", startTime=" + startTime
                + ", endTime=" + endTime + ", waitTime=" + waitTime + ", turnaroundTime=" + turnaroundTime
                + ", normalizedTurnaroundTime=" + normalizedTurnaroundTime + ", runTime=" + runTime + ", remainingTime="
                + remainingTime + ", isFirstTime=" + isFirstTime + ", isFinish=" + isFinish + "]";
    }
    
    
       
}
