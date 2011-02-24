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
public class ContactTest {

    public ContactTest() {
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
     * Test of toString method, of class Contact.
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
     * Test of fileDataToString method, of class Contact.
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
     * Test of getAddress method, of class Contact.
     */
    @Test
    public void testGetAddress() {
        System.out.println("getAddress");
        Contact instance = new Contact();
        String expResult = "Address";
        instance.setAddress("Address");
        String result = instance.getAddress();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setAddress method, of class Contact.
     */
    @Test
    public void testSetAddress() {
        System.out.println("setAddress");
        String address = "Address";
        Contact instance = new Contact();
        instance.setAddress(address);
    }

    /**
     * Test of getName method, of class Contact.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Contact instance = new Contact();
        instance.setName("Name field");
        String expResult = "Name field";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class Contact.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "Name";
        Contact instance = new Contact();
        instance.setName(name);
    }

    /**
     * Test of getCity method, of class Contact.
     */
    @Test
    public void testGetCity() {
        System.out.println("getCity");
        Contact instance = new Contact();
        instance.setCity("City field");
        String expResult = "City field";
        String result = instance.getCity();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCity method, of class Contact.
     */
    @Test
    public void testSetCity() {
        System.out.println("setCity");
        String city = "";
        Contact instance = new Contact();
        instance.setCity(city);
    }

    /**
     * Test of getState method, of class Contact.
     */
    @Test
    public void testGetState() {
        System.out.println("getState");
        Contact instance = new Contact();
        instance.setState("XX");
        String expResult = "XX";
        String result = instance.getState();
        assertEquals(expResult, result);
    }

    /**
     * Test of setState method, of class Contact.
     */
    @Test
    public void testSetState() {
        System.out.println("setState");
        String state = "";
        Contact instance = new Contact();
        instance.setState(state);
    }

    /**
     * Test of getZip method, of class Contact.
     */
    @Test
    public void testGetZip() {
        System.out.println("getZip");
        Contact instance = new Contact();
        instance.setZip(97229);
        int expResult = 97229;
        int result = instance.getZip();
        assertEquals(expResult, result);
    }

    /**
     * Test of setZip method, of class Contact.
     */
    @Test
    public void testSetZip() {
        System.out.println("setZip");
        int zip = 0;
        Contact instance = new Contact();
        instance.setZip(zip);
    }

    /**
     * Test of getNumber method, of class Contact.
     */
    @Test
    public void testGetNumber() {
        System.out.println("getNumber");
        Contact instance = new Contact();
        instance.setNumber(123);
        int expResult = 123;
        int result = instance.getNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNumber method, of class Contact.
     */
    @Test
    public void testSetNumber() {
        System.out.println("setNumber");
        int number = 0;
        Contact instance = new Contact();
        instance.setNumber(number);
    }

}