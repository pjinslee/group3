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

    public ServiceCodeManager(String fileName) {
        super(fileName);

        readFile();
    }

    protected void readFile() {
        //Scanner record = new Scanner(new FileReader(this.fileName));
        Scanner record = null;

        try {
            record = new Scanner(new FileReader(fileName));
        } catch (FileNotFoundException ex) {
           // Logger.getLogger(MemberManager.class.getName()).log(Level.WARNING, null, ex);
            System.out.println("no file found");
            return;
        }

        try {
            //first use a Scanner to get each line
            while ( record.hasNextLine() ){
                ServiceCode svc = new ServiceCode(record.nextLine());
                records.add(svc);
                //System.out.println(svc.toString());
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


}
