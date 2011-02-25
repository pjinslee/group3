/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chocansimulator.datamangement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author tman
 */
public class Bill {
    private Date timeStamp;
    private Date serviceDate;
    private int providerNumber;
    private int memberNumber;
    private int serviceCode;
    private float fee;
    private String comment;

    public Bill () {
        setTimeStamp(new Date());
    }

    public Bill(String input) {
        parseFileData(input);
    }

    protected void parseFileData(String input) {
        Scanner scanner = new Scanner(input);
        String wrkStringDate = null;
        String wrkStringTimeStamp = null;

        scanner.useDelimiter("\\^");
        if ( scanner.hasNext() ){
            wrkStringDate = scanner.next();
            setProviderNumber(scanner.nextInt());
            setMemberNumber(scanner.nextInt());
            setServiceCode(scanner.nextInt());
            setFee(scanner.nextFloat());
            setComment(scanner.next());
            wrkStringTimeStamp = scanner.next();
        }
        else {
            System.out.println("Empty or invalid line. Unable to process bill.dat.");
            System.exit(1);
        }

        DateFormat dfServiceDate = new SimpleDateFormat("mm dd yyyy");
        DateFormat dfTimeStamp = new SimpleDateFormat("mm dd yyyy HH:mm:ss");

        try {
            setServiceDate(dfServiceDate.parse(wrkStringDate));
            setTimeStamp(dfTimeStamp.parse(wrkStringTimeStamp));
        } catch (ParseException e) {
            System.out.println("Invalid date. Unable to process bill.dat." +
                                wrkStringDate + " "  + wrkStringTimeStamp);
            System.exit(1);
        }
    }

    
    @Override
    public String toString() {
       DateFormat dfServiceDate = new SimpleDateFormat("mm dd yyyy");
       DateFormat dfTimeStamp = new SimpleDateFormat("mm dd yyyy HH:mm:ss");

       return(dfServiceDate.format(getServiceDate()) + " " +
               getProviderNumber() + " " + getMemberNumber() + " " +
               getServiceCode() + " " + getFee() + " " + getComment() + " " +
               dfTimeStamp.format(getTimeStamp()));

    }

    public String fileDataToString() {
        DateFormat dfServiceDate = new SimpleDateFormat("mm dd yyyy");
        DateFormat dfTimeStamp = new SimpleDateFormat("mm dd yyyy HH:mm:ss");
           
        return(dfServiceDate.format(getServiceDate()) + "^" +
               getProviderNumber() + "^" + getMemberNumber() + "^" +
               getServiceCode() + "^" + getFee() + "^" + getComment() + "^" +
               dfTimeStamp.format(getTimeStamp()) + "\n");

    }
    
    /**
     * @return the timeStamp
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public final void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @return the serviceDate
     */
    public Date getServiceDate() {
        return serviceDate;
    }

    /**
     * @param serviceDate the serviceDate to set
     */
    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    /**
     * @return the providerNumber
     */
    public int getProviderNumber() {
        return providerNumber;
    }

    /**
     * @param providerNumber the providerNumber to set
     */
    public void setProviderNumber(int providerNumber) {
        this.providerNumber = providerNumber;
    }

    /**
     * @return the memberNumber
     */
    public int getMemberNumber() {
        return memberNumber;
    }

    /**
     * @param memberNumber the memberNumber to set
     */
    public void setMemberNumber(int memberNumber) {
        this.memberNumber = memberNumber;
    }

    /**
     * @return the serviceCode
     */
    public int getServiceCode() {
        return serviceCode;
    }

    /**
     * @param serviceCode the serviceCode to set
     */
    public void setServiceCode(int serviceCode) {
        this.serviceCode = serviceCode;
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
     * @return the comments
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comments the comments to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
