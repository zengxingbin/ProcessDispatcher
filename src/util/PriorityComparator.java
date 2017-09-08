package util;

import java.util.Comparator;

import model.ProcessPCB;

public class PriorityComparator implements Comparator<ProcessPCB> {

    @Override
    public int compare(ProcessPCB pro1, ProcessPCB pro2) {
        return pro2.getPriority() - pro1.getPriority();
    }

}
