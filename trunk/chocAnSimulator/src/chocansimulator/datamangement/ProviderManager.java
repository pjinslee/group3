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
public class ProviderManager extends ManageData {

    public ProviderManager(String fileName) {
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
                Member wrkMem = new Member(record.nextLine());
                records.add(wrkMem);
                System.out.println(wrkMem.toString());
            }
        }
        finally {
            record.close();
        }

    }

    public void writeFile() throws IOException {
        Member m;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                             new FileOutputStream(fileName)));
        
        Iterator itr = records.iterator();

        while (itr.hasNext()) {
            m = (Member) itr.next();
            try {
                out.write(m.fileDataToString());
            } catch ( IOException ex ) {
                Logger.getLogger(ProviderManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        out.close();
        
    }

    public Object search(int number) {
        Iterator itr = records.iterator();
        Provider p = null;

        while (itr.hasNext()) {
            p = (Provider) itr.next();
            if ( p.getNumber() == number ) {
                break;
            }
        }

        return p;
    }


}
