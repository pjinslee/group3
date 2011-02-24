/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chocansimulator.datamangement;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author debbj
 */
public class MemberTest {

    public MemberTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of toString method, of class Member.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Member instance = new Member("777^travj^last street^tigard^OR^97230");
        String expResult = "777 travj last street tigard OR 97230";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of outputString method, of class Member.
     */
    @Test
    public void testFileDataToString() {
        System.out.println("outputString");
        Member instance = new Member("777^travj^last street^tigard^OR^97230");
        String expResult = "777^travj^last street^tigard^OR^97230\n";
        String result = instance.fileDataToString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStatus method, of class Member.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        Member instance = new Member();
        int expResult = 0;
        int result = instance.getStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setStatus method, of class Member.
     */
    @Test
    public void testSetStatus() {
        System.out.println("setStatus");
        int status = 0;
        Member instance = new Member();
        instance.setStatus(status);
    }


}