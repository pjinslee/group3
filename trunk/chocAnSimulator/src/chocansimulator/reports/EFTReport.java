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
    public static final String chocAnProviderData = chocAnDataDir + "/provider.dat";
    public static final String chocAnBillData = chocAnDataDir + "/bill.dat";
    public static final BillManager billMan = BillManager.singletonBillManager(chocAnBillData);
    public static final ProviderManager providerMan = ProviderManager.singletonProviderManager(chocAnProviderData);

    public List formatHeader(int providerNumber, Date dummyDate, Date now) {
        List header = new ArrayList();

        DateFormat forFilename = new SimpleDateFormat("mmddyyyy");

        Provider p = new Provider();
        p = (Provider) providerMan.search(providerNumber);

        header.add("EFT" + providerNumber + "_" + forFilename.format(now) + ".dat");
        header.add(p.getName() + "^" + providerNumber + "^");

        return header;
    }

    public List formatBody(List allBillsFromProvider) {
        List body = new ArrayList();

        Bill b = new Bill();
        float feeSum = 0;

        Iterator itr = allBillsFromProvider.iterator();
        while (itr.hasNext()) {
            b = (Bill) itr.next();
            feeSum = feeSum + b.getFee();
        }

        body.add(feeSum);

        return body;
    }

    public boolean printReport(List preparedReport) throws IOException {
        Iterator itr = preparedReport.iterator();

        if (!itr.hasNext())
            return false;

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                             new FileOutputStream("chocAnData/Reports/" + itr.next())));

        while (itr.hasNext()) {
            try {
                out.write(itr.next() + "\n");
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
        String lastLine = new String();

        Iterator itr = allBillingProviders.iterator();
        while (itr.hasNext()) {
            providerNumber = (Integer) itr.next();

            allBillsFromProvider.clear();
            for (int i = 0;; i++){
                b = (Bill) billMan.search(i);

                if (b == null)
                    break;

                if (((startingDate.compareTo(b.getTimeStamp())) <= 0) && (providerNumber == b.getProviderNumber()));
                    allBillsFromProvider.add(b);
            }

            preparedReport.clear();
            Date now = new Date();
            preparedReport = formatHeader(providerNumber, startingDate, now);

            lastLine = (String) preparedReport.get(1);
            lastLine.concat((String) formatBody(allBillsFromProvider).get(0));
            preparedReport.add(lastLine);

            try {
                printReport(preparedReport);
            } catch (IOException io_ex) {
                return false;
            }
        }

        return true;
    }
}

