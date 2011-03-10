/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chocansimulator.datamangement;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tman
 */
public class ServiceCodeManager extends ManageData {
    private static ServiceCodeManager instance = null;

    public static ServiceCodeManager singletonServiceCodeManager(String inFileName) {
        if ( instance == null ) {
          instance = new ServiceCodeManager(inFileName);
        }

        return instance;

    }

    private ServiceCodeManager(String fileName) {
        super(fileName);

        readFile();
    }

    protected void readFile() {
        //Scanner record = new Scanner(new FileReader(this.fileName));
        Scanner record = null;

        try {
            record = new Scanner(new FileReader(fileName));
        } catch (FileNotFoundException ex) {
            System.out.println("no service codes found");
            return;
        }

        try {
            //first use a Scanner to get each line
            while ( record.hasNextLine() ){
                ServiceCode svc = new ServiceCode(record.nextLine());
                records.add(svc);
            }
        }
        finally {
            record.close();
        }

    }

    public void writeFile() throws IOException {
        ServiceCode svc;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                             new FileOutputStream(fileName)));
        
        Iterator itr = records.iterator();

        while (itr.hasNext()) {
            svc = (ServiceCode) itr.next();
            try {
                out.write(svc.fileDataToString());
            } catch ( IOException ex ) {
                Logger.getLogger(ServiceCodeManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        out.close();
        
    }

    public Object search(int number) {
        Iterator itr = records.iterator();
        ServiceCode svc = null;

        while (itr.hasNext()) {
            svc = (ServiceCode) itr.next();
            if ( svc.getNumber() == number ) {
                return svc;
            }
        }

        return null;
    }
}
