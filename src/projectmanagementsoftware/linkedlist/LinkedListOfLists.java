/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.linkedlist;

import projectmanagementsoftware.utils.IVoidFunction1;

/**
 *
 * @author david
 */
public class LinkedListOfLists<T> {
    private LinkedList<LinkedList<T>> list;
    
    public LinkedListOfLists() {
        this.list = new LinkedList<>();
    }
    
    public void add(T element) {
        LinkedList<T> row = new LinkedList<>();
        row.add(element);
        this.list.add(row);
    }
    
    public void add(T element, int row) {
        this.getRow(row).add(element);
    }
    
    public void add(T element, int row, int index) {
        this.getRow(row).add(element, index);
    }
    
    public void add(LinkedList<T> row) {
        this.list.add(row);
    }
    
    public T get(int row, int index) {
        return this.getRow(row).get(index);
    }
    
    public void set(T element, int row, int index) {
        this.getRow(row).set(element, index);
    }
    
    public LinkedList<T> getRow(int row) {
        return this.list.get(row);
    }
    
    public void remove(int row) {
        this.list.remove(row);
    }
    
    public void remove(int row, int index) {
        this.getRow(row).remove(index);
    }
    
    public int length() {
        return this.list.length();
    }
    
    public int width(int row) {
        return this.list.get(row).length();
    }
    
    public LinkedList<Integer> widths() {
        LinkedList<Integer> widths = new LinkedList<>();
        
        this.list.forEach(row -> {
            widths.add(row.length());
        });
        
        return widths;
    }
    
    public void forEach(IVoidFunction1<T> function) {
        this.list.forEach(row -> {
            row.forEach(node -> {
                function.action(node);
            });
        });
    }
}
