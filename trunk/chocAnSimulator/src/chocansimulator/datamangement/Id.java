/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chocansimulator.datamangement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tman
 */
public class Id {
    private static int memberId = 0;
    private static int providerId = 0;
    private static int serviceCodeId = 0;
    private static String fileName;
    private static Id instance = null;


    public static Id singletonId(String inFileName) {

        if ( instance == null ) {
          // it's ok, we can call this constructor
          fileName = inFileName;
          instance = new Id();
        }

        return instance;
    }


    private Id()  {

        Scanner scanFile = null;
        String record = null;
        Scanner scan = null;
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new FileReader(fileName));
            record = reader.readLine();
        } catch (Exception ex) {
            //System.out.println("no ids found, setting all to 0");
            return;
        } finally {
            try {
            if (reader != null)
                reader.close();
            } catch (IOException e) {}
        }

        scan = new Scanner(record);
        scan.useDelimiter("\\^");
        if ( scan.hasNext() ) {
            memberId = (scan.nextInt());
            providerId = (scan.nextInt());
            serviceCodeId = (scan.nextInt());
        } else {
            System.out.println("Invalid2 " + fileName + " Unable to process");
            System.exit(1);
        }

        
         
    }

    public void writeFile() throws IOException {
        Id id;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                             new FileOutputStream(fileName)));

        try {
            out.write(getCurrentMemberId() + "^" + getCurrentProviderId() + "^" +
                      getCurrentServiceCodeId() + "\n");
        } catch ( IOException ex ) {
            Logger.getLogger(ProviderManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.close();

    }

    /**
     * @return the memberId
     */
    public int getMemberId() {
        return memberId++;
    }

    /**
     * @return the providerId
     */
    public int getProviderId() {
        return providerId++;
    }

    /**
     * @return the ServiceCodeId
     */
    public int getServiceCodeId() {
        return serviceCodeId++;
    }

     /**
     * @return the memberId
     */
    //public int getFinalMemberId() {
    //    return (memberId -1);
    //}

    /**
     * @return the providerId
     */
    //public int getFinalProviderId() {
     //   return (providerId - 1);
    //}

    /**
     * @return the ServiceCodeId
     */
    //public int getFinalServiceCodeId() {
    //    return (serviceCodeId - 1);
    //}

    /**
     * @return the memberId
     */
    public int getCurrentMemberId() {
        return memberId;
    }

    /**
     * @return the providerId
     */
    public int getCurrentProviderId() {
        return providerId;
    }

    /**
     * @return the ServiceCodeId
     */
    public int getCurrentServiceCodeId() {
        return serviceCodeId;
    }

}
