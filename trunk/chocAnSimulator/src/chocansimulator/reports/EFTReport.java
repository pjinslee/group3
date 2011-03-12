/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chocansimulator.reports;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chocansimulator.datamangement.Bill;
import chocansimulator.datamangement.BillManager;
import chocansimulator.datamangement.Provider;
import chocansimulator.datamangement.ProviderManager;
/**
 *
 * @author pjinslee
 */
public class EFTReport implements Reports {

    public static final String chocAnDataDir = "chocAnData";
    public static final String chocAnReportsDir = chocAnDataDir + "/Reports";
    public static final String chocAnBillData = chocAnDataDir + "/bill.dat";
    public static final String chocAnProviderData = chocAnDataDir + "/provider.dat";
    public static final BillManager billMan = BillManager.singletonBillManager(chocAnBillData);
    public static final ProviderManager providerMan = ProviderManager.singletonProviderManager(chocAnProviderData);

    public List formatHeader(int providerNumber, Date dummyDate, Date now) {
        List header = new ArrayList();

        DateFormat forFilename = new SimpleDateFormat("MMddyyyy");

        Provider p = new Provider();
        p = (Provider) providerMan.search(providerNumber);

        header.add("EFT" + providerNumber + "_" + forFilename.format(now) + ".txt");
        header.add(p.getName() + "^" + providerNumber + "^");

        return header;
    }

    public List formatBody(List dummyList) {
        /* not used */
        return dummyList;
    }

    public boolean printReport(List preparedReport) throws IOException {
        Iterator itr = preparedReport.iterator();

        if (!itr.hasNext())
            return false;

        String firstLine = new String((String) itr.next());
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                             new FileOutputStream(chocAnReportsDir + "/" + firstLine)));

        String newline = System.getProperty("line.separator");

        try {
            out.write(firstLine + newline);
        } catch (IOException ex) {
            Logger.getLogger(EFTReport.class.getName()).log(Level.SEVERE, null, ex);
            out.close();
            return false;
        }
        while (itr.hasNext()) {
            try {
                out.write(itr.next() + newline);
            } catch (IOException ex) {
                Logger.getLogger(EFTReport.class.getName()).log(Level.SEVERE, null, ex);
                out.close();
                return false;
            }
        }

        out.close();
        return true;
    }

    public Date getStartingDate(Calendar wrkDate) {
        /* Set starting date to 1 minute past 11:59pm of last Friday,
           (i.e., 12:00am Saturday) */
        int today = wrkDate.get(Calendar.DAY_OF_WEEK);
        if (today != 7) /* if today is Saturday, then today is also the start date */
            wrkDate.add(Calendar.DAY_OF_WEEK, -today); /* otherwise, reset to last Saturday */

        wrkDate.set(Calendar.HOUR_OF_DAY, 0);
        wrkDate.set(Calendar.MINUTE, 0);
        wrkDate.set(Calendar.SECOND, 0);
        wrkDate.set(Calendar.MILLISECOND, 0);

        return wrkDate.getTime();
    }

    public boolean createReport() {
        Calendar currentDate = Calendar.getInstance();
        Date startingDate = getStartingDate(currentDate);

        Bill b = new Bill();
        int providerNumber = 0;
        List allBillingProviders = new ArrayList();
        float feeTotal = 0;

        for (int i = 0;; i++){
            b = (Bill) billMan.search(i);

            if (b == null)
                break;

            providerNumber = b.getProviderNumber();
            if (((startingDate.compareTo(b.getTimeStamp())) <= 0) && (!allBillingProviders.contains(providerNumber)))
                allBillingProviders.add(providerNumber);
        }

        List allBillsFromProvider = new ArrayList();
        List preparedReport = new ArrayList();
        StringBuilder lastLine = new StringBuilder();

        Iterator itr = allBillingProviders.iterator();
        while (itr.hasNext()) {
            providerNumber = (Integer) itr.next();
            feeTotal = 0;

            allBillsFromProvider.clear();
            for (int i = 0;; i++){
                b = (Bill) billMan.search(i);

                if (b == null)
                    break;

                if (((startingDate.compareTo(b.getTimeStamp())) <= 0) && (providerNumber == b.getProviderNumber())) {
                    allBillsFromProvider.add(b);
                    feeTotal = feeTotal + b.getFee();
                }
            }

            preparedReport.clear();
            Date now = new Date();
            preparedReport = formatHeader(providerNumber, startingDate, now);

            lastLine.setLength(0);
            lastLine.append((String) preparedReport.get(1));
            lastLine.append(String.format("%.2f", feeTotal));
            preparedReport.set(1, lastLine);

            try {
                printReport(preparedReport);
            } catch (IOException io_ex) {
                return false;
            }
        }

        return true;
    }
}

