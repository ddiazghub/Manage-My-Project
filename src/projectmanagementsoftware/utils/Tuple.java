/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.utils;

/**
 *
 * @author david
 */
public class Tuple<T, K> {
    private T first;
    private K second;
    
    public Tuple(T first, K second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public K getSecond() {
        return second;
    }
    
    public Tuple<T, K> change(T first, K second) {
        return new Tuple<>(first, second);
    }
}
