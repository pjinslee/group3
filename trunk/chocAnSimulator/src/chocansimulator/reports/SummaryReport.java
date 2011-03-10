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
public class SummaryReport implements Reports {

    public static final String chocAnDataDir = "chocAnData";
    public static final String chocAnReportsDir = "/chocAnReportsDir";
    public static final String chocAnBillData = chocAnDataDir + "/bill.dat";
    public static final String chocAnProviderData = chocAnDataDir + "/provider.dat";
    public static final BillManager billMan = BillManager.singletonBillManager(chocAnBillData);
    public static final ProviderManager providerMan = ProviderManager.singletonProviderManager(chocAnProviderData);

    public List formatHeader(int dummyNumber, Date starting, Date now) {
        List header = new ArrayList();

        DateFormat forFilename = new SimpleDateFormat("MMddyyyy");
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

        header.add("Sum_" + forFilename.format(now) + ".dat");
        header.add("Services processed from " + df.format(starting) + " to " + df.format(now));
        header.add("Provider                 Provider # Consults Fee");
        header.add("------------------------- ---------      --- ---------");

        return header;
    }

    public List formatBody(List allBillsFromProvider) {
        List body = new ArrayList();

        Bill b = new Bill();
        int pNumber = 0;
        Provider p = new Provider();
        String providerName = new String();
        int consultations = 0;
        float feeTotal = 0;

        Iterator itr = allBillsFromProvider.iterator();
        if (itr.hasNext()) {
            b = (Bill) itr.next();

            pNumber = b.getProviderNumber();
            p = (Provider) providerMan.search(pNumber);
            providerName = p.getName();

            consultations++;
            feeTotal = feeTotal + b.getFee();
        } else {
            return body;
        }

        while (itr.hasNext()) {
            b = (Bill) itr.next();

            consultations++;
            feeTotal = feeTotal + b.getFee();
        }

        for (int i = providerName.length(); i < 25; i++)
            providerName.concat(" ");

        String temp = new String();

        temp = Integer.toString(pNumber);
        String providerNumber = new String();
        for (int i = temp.length(); i < 9; i++)
            providerNumber.concat(" ");
        providerNumber.concat(temp);

        temp = Integer.toString(consultations);
        String consults = new String();
        for (int i = temp.length(); i < 3; i++)
            consults.concat(" ");
        consults.concat(temp);

        body.add(providerName  + " " + providerNumber + "      " + consults + " $" + feeTotal);

        return body;
    }

    public boolean printReport(List preparedReport) throws IOException {
        Iterator itr = preparedReport.iterator();

        if (!itr.hasNext())
            return false;

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                             new FileOutputStream(chocAnReportsDir + "/" + itr.next())));

        while (itr.hasNext()) {
            try {
                out.write(itr.next() + "\n");
            } catch (IOException ex) {
                Logger.getLogger(SummaryReport.class.getName()).log(Level.SEVERE, null, ex);
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
        String line = new String();
        int providers = 0;
        int total_consultations = 0;
        float total_fee = 0;

        Date now = new Date();
        List preparedReport = new ArrayList();
        preparedReport = formatHeader(0, startingDate, now);

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

            /* There should only be one line in the list returned from 'formatBody()'. I take the first item just in case. */
            line = (String) formatBody(allBillsFromProvider).get(0);

            providers++;
            /* The next lines rely on formatting to get 'consultations' (max 3 digits) and 'fee' from the line. */
            total_consultations = total_consultations + Integer.parseInt(line.substring(41, 43).trim());
            total_fee = total_fee + Float.parseFloat(line.substring(46).trim());

            preparedReport.add(line);
        }

        preparedReport.add("Total number of providers: " + providers);
        preparedReport.add("Total number of consultations: " + total_consultations);
        preparedReport.add("Total fee: $" + total_fee);

        try {
            printReport(preparedReport);
        } catch (IOException io_ex) {
            return false;
        }

        return true;
    }
}

