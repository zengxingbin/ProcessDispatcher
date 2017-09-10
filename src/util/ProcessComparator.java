package util;

import java.util.Comparator;

import model.ProcessPCB;

public class ProcessComparator implements Comparator<ProcessPCB> {
    private int mode;
    public ProcessComparator(int mode) {
        this.mode = mode;
    }
    @Override
    public int compare(ProcessPCB pro1, ProcessPCB pro2) {
        if(mode == 0)
            return pro2.getPriority() - pro1.getPriority();
        else if(mode == 1)
            return pro1.getServiceTime() - pro2.getServiceTime();
        else if(mode == 2) 
            return pro1.getRemainingTime() - pro2.getRemainingTime();
        else if(mode == 3) 
            return (pro1.getWaitTime() + pro1.getServiceTime())/pro1.getServiceTime() - 
                    (pro2.getWaitTime() + pro2.getServiceTime())/pro2.getServiceTime();
        return 0;
    }

}
