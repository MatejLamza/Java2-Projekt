/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 * @author Matej
 */
public class ClassInfoHelper {
     public static final String SEPARATOR = System.lineSeparator();
    
    public static String writeInfo(String name){
        StringBuilder sb = new StringBuilder();
        try {
            Class<?> tmpClass = Class.forName(name);
            Field[] variables = getVariables(tmpClass);
            Method[] methods = getMethods(tmpClass);
            Constructor[] constructors = getConstructors(tmpClass);
            Class[] interfaces = getInterfaces(tmpClass);
            
            sb.append("- - - - - - - - - -" + name + "- - - - - - - - - -" + SEPARATOR);
            printVariables(sb, variables);
            printInterfaces(sb, interfaces);
            printConstructors(sb, constructors);
            printMethods(sb, methods);
            sb.append("---------------------------------------------------------------------------------------------------------");
            sb.append(SEPARATOR + SEPARATOR);
            System.out.println(sb.toString());
            
        } catch (Exception e) {
        }
        return sb.toString();
    }
    
    private static String printMethods(StringBuilder sb, Method[] tempMethods){
        sb.append("METHODS" + SEPARATOR);
        int counter = 0;
        for (Method tempMethod : tempMethods) {
            sb.append(tempMethod.getName() + "; ");
            if (counter == 10) {
                sb.append(SEPARATOR);
            }
        }
        sb.append(SEPARATOR);
        return sb.toString();
    }
    
    private static String printVariables(StringBuilder sb, Field[] variables){
        sb.append("VARIABLES" + SEPARATOR + SEPARATOR);
        
        sb.append("PRIVATE VARIABLES" + SEPARATOR);
        for (Field variable : variables) {
            if (Modifier.isPrivate(variable.getModifiers())) {
                sb.append(variable.getName() + "; ");
            }
        }
        
        sb.append(SEPARATOR + "PUBLIC VARIABLES" + SEPARATOR);
        for (Field variable : variables) {
            if (Modifier.isPublic(variable.getModifiers())) {
                sb.append(variable.getName() + "; ");
            }
        }
        
        sb.append(SEPARATOR + "FINAL VARIABLES" + SEPARATOR);
        for (Field variable : variables) {
            if (Modifier.isFinal(variable.getModifiers())) {
                sb.append(variable.getName() + "; ");
            }
        }
        
        sb.append(SEPARATOR + "STATIC VARIABLES" + SEPARATOR);
        for (Field variable : variables) {
            if (Modifier.isStatic(variable.getModifiers())) {
                sb.append(variable.getName() + "; ");
            }
        }
        sb.append(SEPARATOR);
        return sb.toString();
    }
    
    private static String printInterfaces(StringBuilder sb, Class[] interfaces){
        sb.append("INTERFACES" + SEPARATOR);
        for (Class tmpInterface : interfaces) {
            sb.append(tmpInterface.getName() + "; ");
        }
        sb.append(SEPARATOR);
        return sb.toString();
    }
    
    private static String printConstructors(StringBuilder sb,Constructor[] constructors){
        sb.append("CONSTRUCTORS" + SEPARATOR);
        for (Constructor constructor : constructors) {
            sb.append(constructor.getName() + "; ");
        }
        sb.append(SEPARATOR);
        return sb.toString();
    }
    
    private static Field[] getVariables(Class<?> tempClass){
        Field[] variables = null;
        variables = tempClass.getDeclaredFields();
        return variables;
    }
    
    private static Method[] getMethods(Class<?> tempClass){
        Method[] methods = null;
        methods = tempClass.getMethods();
        return methods;
    }
    
    private static Class[] getInterfaces(Class<?> tempClass){
       Class[] interfaces = null;
       interfaces = tempClass.getInterfaces();
       return interfaces;
    }
    
    private static Constructor[] getConstructors(Class<?> tempClass){
        Constructor[] constructors = null;
        constructors = tempClass.getConstructors();
        return constructors;
    }
}
