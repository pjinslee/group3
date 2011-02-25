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
public class MemberManager extends ManageData {

    public MemberManager(String fileName) {
        super(fileName);

        readFile();
    }

    protected void readFile() {
        //Scanner record = new Scanner(new FileReader(this.fileName));
        Scanner record = null;

        try {
            record = new Scanner(new FileReader(fileName));
        } catch (FileNotFoundException ex) {
            System.out.println("no members found");
            return;
        }

        try {
            //first use a Scanner to get each line
            while ( record.hasNextLine() ){
                Member wrkMem = new Member(record.nextLine());
                records.add(wrkMem);

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
                Logger.getLogger(MemberManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        out.close();
        
    }

    public Object search(int number) {
        Iterator itr = records.iterator();
        Member m = null;

        while (itr.hasNext()) {
            m = (Member) itr.next();
            if ( m.getNumber() == number ) {
                return m;
            }
        }

        return null;
    }


}
