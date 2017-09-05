/******************************************************************************
 * This file is licensed under the Unlicense. See License.txt for details.
 *
 * Author:
 *   Alinshans (https://github.com/Alinshans/GoClientDev)
 *   zengxingbin (https://github.com/zengxingbin/GoClientDev)
 *   lecenca (https://github.com/lecenca/GoClientDev)
 *
 * Copyright (c) 2017. All rights reserved.
 *****************************************************************************/

package util;

import java.util.AbstractCollection;
import java.util.AbstractQueue;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Queue<T> {

    private ConcurrentLinkedQueue<T> queue;

    public Queue() {
        queue = new ConcurrentLinkedQueue<T>();
    }

    public void add(T t) {
        queue.add(t);
    }

    public T remove() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public ConcurrentLinkedQueue<T> getQueue() {
       
        return queue;
    }
    public int size() {
        return queue.size();
    }
}
