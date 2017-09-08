package util;

import java.util.Comparator;

import model.ProcessPCB;

public class PriorityComparator implements Comparator<ProcessPCB> {
    private int mode;
    public PriorityComparator(int mode) {
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
        return 0;
    }

}
