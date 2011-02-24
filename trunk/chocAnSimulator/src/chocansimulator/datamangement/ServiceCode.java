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
public class ServiceCode {
    private int number;
    private String description;
    private float fee;
    private int status;

    public ServiceCode() {
        number = Id.singletonId(ChocAnUserInterface.chocAnId).getServiceCodeId();
        setStatus(1);
    }

    public ServiceCode(String input) {
        parseFileData(input);
    }

    protected void parseFileData(String input) {
        Scanner scanner = new Scanner(input);

        scanner.useDelimiter("\\^");
        if ( scanner.hasNext() ){
            setNumber(scanner.nextInt());
            setStatus(scanner.nextInt());
            setDescription(scanner.next());
            setFee(scanner.nextFloat());
        }
        else {
            System.out.println("Empty or invalid line. Unable to process svc.dat.");
            System.exit(1);
        }
    }

    @Override
    public String toString() {
       return(getNumber() + " " + getStatus() + " " + getDescription() + " " +
              getFee());

    }

    public String fileDataToString() {
       return(getNumber() + "^" + getStatus() + "^" + getDescription() + "^" +
              getFee() + "\n");

    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the fee
     */
    public float getFee() {
        return fee;
    }

    /**
     * @param fee the fee to set
     */
    public void setFee(float fee) {
        this.fee = fee;
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
