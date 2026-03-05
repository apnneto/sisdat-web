/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frw.base.web.system;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 *
 * @author juliano
 */
@WebListener
public class LifecycleManager implements ServletContextListener {

   

    public void contextDestroyed(ServletContextEvent sce) {
       
        try {
          //  System.err.println("Disabling finalization...");
        //    disableFinalization();
            System.err.println("Starting thread locals cleanup..");
            cleanThreadLocals();
            System.err.println("End thread locals cleanup");


        } catch (Throwable t) {
            t.printStackTrace();
            
        }
    }

    public void contextInitialized(ServletContextEvent sce) {

    }

    private void cleanThreadLocals() throws NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {

        Thread[] threadgroup = new Thread[256];
        Thread.enumerate(threadgroup);

        for (int i = 0; i < threadgroup.length; i++) {
            if (threadgroup[i] != null) {
                cleanThreadLocals(threadgroup[i]);
            }
        }
    }

    private void cleanThreadLocals(Thread thread) throws NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {

        Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
        threadLocalsField.setAccessible(true);

        Class threadLocalMapKlazz = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
        Field tableField = threadLocalMapKlazz.getDeclaredField("table");
        tableField.setAccessible(true);

        Object fieldLocal=threadLocalsField.get(thread);
        if(fieldLocal==null)
            return;
        Object table = tableField.get(fieldLocal);

        int threadLocalCount = Array.getLength(table);
        


        

        Map<String,Integer> leakCounts=new HashMap<String,Integer>();

        for (int i = 0; i < threadLocalCount; i++) {
            Object entry = Array.get(table, i);
            if (entry != null) {
                Field valueField = entry.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                Object value = valueField.get(entry);
                if (value != null) {
                   Integer currentCount=leakCounts.get(value.getClass().getName());
                   if(currentCount==null)
                       currentCount=0;
                   currentCount=currentCount+1;
                   leakCounts.put(value.getClass().getName(), currentCount);
                   if(value.getClass().getName().equals("com.sun.enterprise.security.authorize.HandlerData")) {
                      valueField.set(entry, null);
                      System.err.println("Cleaned 1 stale reference");
                    }
                } 
               
            }
        }

       /* System.err.print("Thread ["+ thread.getName()+"] possible ThreadLocal leaks: ");
        Iterator<String> classes=leakCounts.keySet().iterator();
        while(classes.hasNext()) {
            String name=classes.next();
            System.err.print(name+"("+leakCounts.get(name)+") ,");
        }
        System.err.println("\n\n");
     */

    }

    private void disableFinalization() throws ClassNotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        Class finalizerClass=Class.forName("java.lang.ref.Finalizer");
        Field unfinalized=finalizerClass.getDeclaredField("unfinalized");
        unfinalized.setAccessible(true);
        unfinalized.set(null, null);



    }
}
