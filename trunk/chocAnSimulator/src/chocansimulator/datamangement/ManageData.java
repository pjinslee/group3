/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chocansimulator.datamangement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 *
 * @author tman
 */
public abstract class ManageData {
    protected String fileName;
    protected List records;

    public ManageData(String fileName) {
        records = new ArrayList();
        this.fileName = fileName;
    }


    abstract void readFile();
    abstract void writeFile() throws Exception;
    abstract Object search(int number);

    public boolean addData(Object o) {
        records.add(o);
        return true;
    }

    public boolean deleteData(int number) {
        Contact o;
        //TODO - only works for contacts
        Iterator itr = records.iterator();

        while ( itr.hasNext() ) {
            o = (Contact) itr.next();
            if ( o.getNumber() == number ) {
                itr.remove();
                System.out.println("found " + number + " to delete");
                return true;
            }
        }
        System.out.println("NOT found " + number + " to delete");
        return false;
    }

    public void print() {
        Object o;
        Iterator itr = records.iterator();

        while (itr.hasNext()) {
            o = (Object) itr.next();
            System.out.println(o);
        }
        
    }

    public int numOfRecords() {
        return records.size();
    }
}
