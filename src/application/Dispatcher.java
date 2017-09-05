package application;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import util.PriorityComparator;

public class Dispatcher {
    private static Object LOCK = new Object();
    private static int processCounter = 0;// as the the process id
    private final static int PROCESSMAXNUM = 10;// maximum number of processe allowed
    private boolean isContention;
    private static long startTime;
    private static long currentTime;
    private static LinkedList<ProcessPCB> readyQueue = new LinkedList<>();
    private static LinkedList<ProcessPCB> waitQueue = new LinkedList<>();
    private static LinkedList<ProcessPCB> finishQueue = new LinkedList<>();
    private static ProcessPCB process = null;// process running
    private final static int TIMESLICING = 3;
    private static Random rand = new Random();//random generator
    private static int choice;
    private static Thread addProcess = new Thread(new Runnable() {
        /**
         * create another thread in order to dynamically add to the ready queue  
         */
        @Override
        public void run() {
            System.out.println("start to add process......");
            while(true) {
                //create a new a process every two seconds
                ProcessPCB newPorcess = new ProcessPCB();
                // initialize process
                newPorcess.setpName("process" + processCounter);
                newPorcess.setPid(processCounter++);
                newPorcess.setPriority(rand.nextInt(20));
                newPorcess.setStatus(0);
                newPorcess.setArrivalTime((int) (currentTime - startTime));
                newPorcess.setFirstTime(true);
                // record current time
                currentTime = System.currentTimeMillis() / 1000;
                newPorcess.setServiceTime(rand.nextInt(10) + 1);
                newPorcess.setRemainingTime(newPorcess.getServiceTime());
                //add to the ready queue
                synchronized (LOCK) {
                    if(readyQueue.size() >= PROCESSMAXNUM) {
                        //add to the wait queue
                        waitQueue.addFirst(newPorcess);
                    }else {
                        readyQueue.addFirst(newPorcess);
                        if(choice == 2) {//priority number dispatch and it's preemptive 
                            //restart sorting the ready queue according to the priority every thime you add new process to the ready queue
                            readyQueue.sort(new PriorityComparator());
                           
                        }
                    }
                }
                System.out.println("add a new process");
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        }
    });

    /**
     * create fixed number of process
     * 
     * @param num
     */
    public static void createPorcess(int num) {
        startTime = System.currentTimeMillis() / 1000;
        currentTime = startTime;
        // random generate the priority and service time of process
        
        for (int i = 0; i < num; i++) {
            ProcessPCB process = new ProcessPCB();
            // initialize process
            process.setpName("process" + processCounter);
            process.setPid(processCounter++);
            process.setPriority(rand.nextInt(20));
            process.setStatus(0);
            process.setArrivalTime((int) (currentTime - startTime));
            process.setFirstTime(true);
            // record current time
            currentTime = System.currentTimeMillis() / 1000;
            process.setServiceTime(rand.nextInt(10) + 1);
            process.setRemainingTime(process.getServiceTime());
            // join in the readyQueue
            readyQueue.addFirst(process);
        }
    }

    /**
     * Time slice round robin dispatch
     * 
     * @param readyQueue
     */
    public static void rrDispatcher(LinkedList<ProcessPCB> readyQueue) {
        int timeSlicing = 0;
        if (!readyQueue.isEmpty()) {
            // first process to be run
            process = readyQueue.removeLast();
            startTime = System.currentTimeMillis() / 1000;
            currentTime = startTime;
            process.setStartTime((int) (currentTime - startTime));
            process.setFirstTime(false);
        } else {
            System.out.println("There is no  process in ready queue! ");
            return;
        }
        while (true) {
            process.setStatus(1);// 1 represents running
            process.setRunTime(process.getRunTime() + 1);
            process.setRemainingTime(process.getRemainingTime() - 1);
            System.out.println("**Currnet running process:" + process + "**");
            // current process has run for one seconds,increase the time slicing
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            timeSlicing++;
            // if process finish
            if (process.getRemainingTime() == 0) {
                currentTime = System.currentTimeMillis() / 1000;
                // set the end time of process
                process.setEndTime((int) (currentTime - startTime));
                // compute the turnaround time
                process.setTurnaroundTime(process.getWaitTime() + process.getServiceTime());
                // compute the normalized turnaround time
                process.setNormalizedTurnaroundTime((double) process.getTurnaroundTime() / process.getServiceTime());
                // change the status of process:finish
                process.setStatus(2);
                // join to the finishQueue
                finishQueue.addFirst(process);
                process.setFinish(true);
                if (readyQueue.isEmpty()) {
                    process = null;
                    System.out.println("--All processes have been completed!--");
                    // print the finishQueue
                    for (ProcessPCB process : finishQueue)
                        System.out.println(process);
                    break;
                }
            } else if (timeSlicing >= TIMESLICING) {// join in the readyQueue
                                                    // when time out
                // change the status of process
                process.setStatus(0);
                // join in the readyQueue
                readyQueue.addFirst(process);
            }

            if (!readyQueue.isEmpty()) {
                if (timeSlicing >= TIMESLICING || process.isFinish()) {
                    // next process to be run
                    process = readyQueue.removeLast();
                    // other process wait for the time slicing
                    for (ProcessPCB process : readyQueue)
                        process.setWaitTime(process.getWaitTime() + 1);
                    // record the first time to start
                    if (process.isFirstTime()) {
                        currentTime = System.currentTimeMillis() / 1000;
                        process.setStartTime((int) (currentTime - startTime));
                        process.setFirstTime(false);
                    }

                    // clear the time slicing for every process
                    timeSlicing = 0;
                }
            }
        }
    }

    /**
     * process priority scheduling(non-contention and contention)
     * 
     * @param readyQueue
     * @param isContention
     *            decide if contention or not
     */
    public static void priorityNumDispatch(LinkedList<ProcessPCB> readyQueue, boolean isContention) {
        // is the readyQueue empty
        if (readyQueue.isEmpty()) {
            System.out.println("There is no process in ready queue!");
            return;
        }
        // sort the readyQueue according to priority
        synchronized (LOCK) {
            readyQueue.sort(new PriorityComparator());
        }
        // judge if is contention
        if (!isContention) {
            // run the first process which has highest priority
            // first process to be run
            synchronized (LOCK) {
                process = readyQueue.removeLast();
            }
            startTime = System.currentTimeMillis() / 1000;
            currentTime = startTime;
            process.setStartTime((int) (currentTime - startTime));
            while (true) {
                process.setStatus(1);// 1 represents running
                process.setRunTime(process.getRunTime() + 1);
                process.setRemainingTime(process.getRemainingTime() - 1);
                System.out.println("**Currnet running process:" + process + "**");
                // current process run for one second in every circle
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // other process wait for the currnet process to finish
                for (ProcessPCB process : readyQueue)
                    process.setWaitTime(process.getWaitTime() + 1);
                if (process.getRemainingTime() == 0) {
                    currentTime = System.currentTimeMillis() / 1000;
                    // set the end time of process
                    process.setEndTime((int) (currentTime - startTime));
                    // compute the turnaround time
                    process.setTurnaroundTime(process.getWaitTime() + process.getServiceTime());
                    // compute the normalized turnaround time
                    process.setNormalizedTurnaroundTime(
                            (double) process.getTurnaroundTime() / process.getServiceTime());
                    // change the status of process:finish
                    process.setStatus(2);// represents process completed
                    // join to the finishQueue
                    finishQueue.addFirst(process);
                    process.setFinish(true);
                    process = null;
                    if (readyQueue.isEmpty()) {
                        /*process = null;
                        System.out.println("--All processes have been completed!--");
                        // print the finishQueue
                        for (ProcessPCB process : finishQueue)
                            System.out.println(process);
                        break;*/
                        while(readyQueue.isEmpty())
                            System.out.println("wait the process added to the ready queue");
                            try {
                                Thread.currentThread().sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                    } else {
                        // if current process finish,run next process which has
                        // highest priority
                        synchronized (LOCK) {
                            
                            process = readyQueue.removeLast();
                        }
                        currentTime = System.currentTimeMillis() / 1000;
                        process.setStartTime((int) currentTime - (int) startTime);
                    }
                }
            }
        } else {
            // run the first process which has highest priority
            // first process to be run
            synchronized (LOCK) {
                
                process = readyQueue.removeLast();
            }
            startTime = System.currentTimeMillis() / 1000;
            currentTime = startTime;
            while(true) {
                process.setStatus(1);// 1 represents running
                process.setRunTime(process.getRunTime() + 1);
                process.setRemainingTime(process.getRemainingTime() - 1);
                System.out.println("**Currnet running process:" + process + "**");
                // current process run for one second in every circle
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // other process wait for the currnet process to finish
                for (ProcessPCB process : readyQueue)
                    process.setWaitTime(process.getWaitTime() + 1);
                if (process.getRemainingTime() == 0) {
                    currentTime = System.currentTimeMillis() / 1000;
                    // set the end time of process
                    process.setEndTime((int) (currentTime - startTime));
                    // compute the turnaround time
                    process.setTurnaroundTime(process.getWaitTime() + process.getServiceTime());
                    // compute the normalized turnaround time
                    process.setNormalizedTurnaroundTime(
                            (double) process.getTurnaroundTime() / process.getServiceTime());
                    // change the status of process:finish
                    process.setStatus(2);// represents process completed
                    // join to the finishQueue
                    finishQueue.addFirst(process);
                    process.setFinish(true);
                    if (readyQueue.isEmpty()) {
                        process = null;
                        System.out.println("--All processes have been completed!--");
                        // print the finishQueue
                        for (ProcessPCB process : finishQueue)
                            System.out.println(process);
                        break;
                    } else {
                        // if current process finish,run next process which has
                        // highest priority
                        synchronized (LOCK) {
                            process = readyQueue.removeLast();
                        }
                        currentTime = System.currentTimeMillis() / 1000;
                        process.setStartTime((int) currentTime - (int) startTime);
                    }
                }else {
                    //if there is process which has higher priority in ready queue,then run it,or continue running the current process
                    if(!readyQueue.isEmpty()) {
                        ProcessPCB process2 = readyQueue.getLast();//the last process in ready queue has highest priority
                        if(process2.getPriority() > process.getPriority())
                            process = process2;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the number(between 1 and 10) of process you want to create:");
        int num;
        while (true) {
            num = input.nextInt();
            if (num >= 1 && num <= 10)
                break;
            else
                System.out.println("Illegal number!Please try again!");
        }
        createPorcess(num);
        for (ProcessPCB process : readyQueue)
            System.out.println(process);
        System.out.println("============================");
        System.out.println("1.RR 2.priority 3.SPN 4.SRT");
        System.out.print("Please choose the dispatch algorithm:");
        choice = input.nextInt();
        switch (choice) {
        case 1:
            rrDispatcher(readyQueue);
            addProcess.start();
            break;
        case 2:
            System.out.println("priority dispatch");
            addProcess.start();
            priorityNumDispatch(readyQueue, false);
            /*for (int i = 0; i < readyQueue.size(); i++)
                System.out.println(readyQueue.get(i));*/
            
            break;
        case 3:
            System.out.println("SPN dispatch");
            break;
        case 4:
            System.out.println("SRT dispatch");
            break;
        default:
            System.out.println("illegal choic");
            break;
        }

    }
}
