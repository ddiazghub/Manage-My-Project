/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package projectmanagementsoftware.utils;

/**
 * Interfaz que representa una función para iterar en una estructura de datos, ya sea una lista o un árbol. Preferiblemente deben usarse Lambdas.
 * @author david
 * @param <T> El Tipo de los elementos sobre los cuales se iterará.
 */
public interface IForEachFunction<T> {
    public void action(T element);
}
