/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.wicketstuff.javaee.naming;

/**
 *
 * @author juliano
 */
public class JavaEE6NamingStrategy implements IJndiNamingStrategy{

    private String applicationNamePrefix;


    public JavaEE6NamingStrategy(String applicationName) {

        applicationNamePrefix="java:global/"+applicationName+"/";
    }

    public String calculateName(String ejbName, Class ejbType) {

        String name=ejbName;

        if(name==null) {

            name=ejbType.getName();
           int i=name.lastIndexOf('.');
           if(i!=-1 && i<name.length()-1)
               name=name.substring(i+1);

        }

        return applicationNamePrefix+name;


    }

}
