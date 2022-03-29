/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package projectmanagementsoftware.linkedlist;

/**
 *
 * @author david
 */
public interface IStack<T> {
    public void push(T element);
    public T pop();
    public T peek();
    public boolean isEmpty();
    public int length();
}
