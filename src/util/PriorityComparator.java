package util;

import java.util.Comparator;

import model.ProcessPCB;

public class PriorityComparator implements Comparator<ProcessPCB> {

    @Override
    public int compare(ProcessPCB pro1, ProcessPCB pro2) {
        return pro1.getPriority() - pro2.getPriority();
    }

}
