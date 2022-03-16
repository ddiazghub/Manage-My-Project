/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import projectmanagementsoftware.linkedlist.LinkedList;

/**
 *
 * @author david
 */
public class LinkedListTest {
    public LinkedList<Integer> list;
    
    @BeforeClass
    public static void setUpClass() {
        
    }
 
    @AfterClass
    public static void tearDownClass() {
        
    }
 
    @Before
    public void setUpTest() {
        this.list = new LinkedList<>();
        
        for (int i = 0; i < 10; i++)
            list.add(i);
    }
 
    @After
    public void tearDownTest() {
        
    }

    
    @Test
    public void canAddData() {
        for (int i = 0; i < 10; i++)
            Assert.assertEquals((int) list.get(i), i);
    }
}
