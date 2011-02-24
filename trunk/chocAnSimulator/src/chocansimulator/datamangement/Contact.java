/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chocansimulator.datamangement;

import chocansimulator.ChocAnUserInterface;
import java.util.Scanner;

/**
 *
 * @author tman
 */
public class Contact {

    private int number;
    private int status;
    private String name;
    private String address;
    private String city;
    private String state;
    private int zip;

    public Contact() {
        setStatus(1);

    }

    public Contact(String input) {
        parseFileData(input);
    }

    protected void parseFileData(String input) {
        Scanner scanner = new Scanner(input);

        scanner.useDelimiter("\\^");
        if ( scanner.hasNext() ){
            setNumber(scanner.nextInt());
            setStatus(scanner.nextInt());
            setName(scanner.next());
            setAddress(scanner.next());
            setCity(scanner.next());
            setState(scanner.next());
            setZip(scanner.nextInt());
        }
        else {
            System.out.println("Empty or invalid line. Unable to process.");
            System.exit(1);
        }
    }

    @Override
    public String toString() {
       return(getNumber() + " " + getStatus() + " " + getName() + " " +
              getAddress() + " " + getCity() + " " + getState() + " " +
              getZip());

    }

    public String fileDataToString() {
       return(getNumber() + "^" + getStatus() + "^" + getName() + "^" +
              getAddress() + "^" + getCity() + "^" + getState() + "^" + getZip()
              + "\n");

    }



    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the firstName
     */
    public String getName() {
        return name;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the zip
     */
    public int getZip() {
        return zip;
    }

    /**
     * @param zip the zip to set
     */
    public void setZip(int zip) {
        this.zip = zip;
    }

    /**
     * @return the id
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    protected void setNumber(int number) {
        this.number = number;
    }


    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

}
