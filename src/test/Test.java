package test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ProcessPCB;

public class Test {
    private static ObservableList<ProcessPCB> list = FXCollections.observableArrayList();
    static boolean signal;
    public static void main(String[] args) {
        
        /*list.add(new ProcessPCB(1));
        list.add(new ProcessPCB(2));
        list.add(new ProcessPCB(3));
        list.add(new ProcessPCB(4));
        for(int i = 0; i < list.size(); i++)
            System.out.println(list.get(i).getPid());
        System.out.println("---------------");
        list.remove(0);
        for(int i = 0; i < list.size(); i++)
            System.out.println(list.get(i).getPid());
        System.out.println("---------------");
        list.remove(0);
        for(int i = 0; i < list.size(); i++)
            System.out.println(list.get(i).getPid());
        System.out.println("---------------");
        list.add(new ProcessPCB(5));
        for(int i = 0; i < list.size(); i++)
            System.out.println(list.get(i).getPid());*/
        Thread thread = new Thread(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while(true) {
                    if(signal)
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                }
            }
        });
        thread.start();
        for(int i = 0; i < 10; i++) {
            System.out.println(thread.isAlive());
            if(i == 5)
                signal = true;
        }
        
    }
}
