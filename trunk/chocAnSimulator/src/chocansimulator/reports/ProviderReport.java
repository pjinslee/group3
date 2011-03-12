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
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chocansimulator.datamangement.Bill;
import chocansimulator.datamangement.BillManager;
import chocansimulator.datamangement.Member;
import chocansimulator.datamangement.MemberManager;
import chocansimulator.datamangement.Provider;
import chocansimulator.datamangement.ProviderManager;
/**
 *
 * @author pjinslee
 */
public class ProviderReport implements Reports {

    public static final String chocAnDataDir = "chocAnData";
    public static final String chocAnReportsDir = chocAnDataDir + "/Reports";
    public static final String chocAnBillData = chocAnDataDir + "/bill.dat";
    public static final String chocAnMemberData = chocAnDataDir + "/member.dat";
    public static final String chocAnProviderData = chocAnDataDir + "/provider.dat";
    public static final BillManager billMan = BillManager.singletonBillManager(chocAnBillData);
    public static final MemberManager memberMan = MemberManager.singletonMemberManager(chocAnMemberData);
    public static final ProviderManager providerMan = ProviderManager.singletonProviderManager(chocAnProviderData);

    public List formatHeader(int providerNumber, Date starting, Date now) {
        List header = new ArrayList();

        DateFormat forFilename = new SimpleDateFormat("MMddyyyy");
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

        Provider p = new Provider();
        p = (Provider) providerMan.search(providerNumber);

        header.add("P" + providerNumber + "_" + forFilename.format(now) + ".txt");
        header.add("Provider / Number: " + p.getName() + " / " + providerNumber);
        header.add("          Address: " + p.getAddress());
        header.add("                   " + p.getCity() + " " +
                                           p.getState() + " " +
                                           p.getZip());
        header.add("");

        switch (p.getStatus()) {
            case 0:
                header.add("NOTICE!: Your status as a provider has been suspended!");
                header.add("");
            break;
            case 1: /* do nothing */
            break;
            case 2:
                header.add("NOTICE!: Your status as a provider has been terminated, but CHOCAN");
                header.add("         still owes you for services provided, as shown below.");
                header.add("");
            break;
            default:
                header.add("NOTICE!: This report has been printed in error! Providership status invalid!");
                header.add("");
            return header;
        }

        header.add("Services processed from " + df.format(starting) + " to " + df.format(now));
        header.add("Svc. Date     Timestamp              Member                          Number      Code        Fee");
        header.add("----------    -------------------    -------------------------    ---------    ------    -------");

        return header;
    }

    public List formatBody(List allBillsFromProvider) {
        List body = new ArrayList();

        Bill b = new Bill();
        List timestamps = new ArrayList();

        Iterator itr = allBillsFromProvider.iterator();
        while (itr.hasNext()) {
            b = (Bill) itr.next();
            timestamps.add(b.getTimeStamp());
        }
        Collections.sort(timestamps);

        Date t = new Date();
        Member m = new Member();
        int consultations = 0;
        float feeTotal = 0;

        Iterator tsItr = timestamps.iterator();
        while (tsItr.hasNext()) {
            t = (Date) tsItr.next();

            for (Iterator billItr = allBillsFromProvider.iterator(); billItr.hasNext();) {
                b = (Bill) billItr.next();

                if (t == b.getTimeStamp()) {
                    String[] token = (b.fileDataToString()).split("\\^");

                    m = (Member) memberMan.search(b.getMemberNumber());
                    StringBuilder memberName = new StringBuilder((m.getName()).trim());
                    StringBuilder memberNumber = new StringBuilder();
                    StringBuilder serviceCode = new StringBuilder();
                    StringBuilder fee = new StringBuilder();
                    String temp = new String(String.format("%.2f", b.getFee()));

                    for (int i = memberName.length(); i < 25; i++)
                        memberName.append(' ');
                    for (int i = token[2].length(); i < 9; i++)
                        memberNumber.append(' ');
                    memberNumber.append(token[2]);
                    for (int i = token[3].length(); i < 6; i++)
                        serviceCode.append(' ');
                    serviceCode.append(token[3]);
                    for (int i = (temp.length()); i < 6; i++)
                        fee.append(' ');
                    fee.append('$');
                    fee.append(String.format(temp));

                    body.add(token[0].trim() + "    " + token[6].trim() + "    " + memberName + "    " + memberNumber + "    " + serviceCode + "    " + fee);

                    consultations++;
                    feeTotal = feeTotal + b.getFee();
                }
            }
        }

        body.add("Total number of consultations with members: " + consultations);
        body.add(String.format("Total fee for week: $%.2f", feeTotal));

        return body;
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
                Logger.getLogger(ProviderReport.class.getName()).log(Level.SEVERE, null, ex);
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

        Iterator itr = allBillingProviders.iterator();
        while (itr.hasNext()) {
            providerNumber = (Integer) itr.next();

            allBillsFromProvider.clear();
            for (int i = 0;; i++){
                b = (Bill) billMan.search(i);

                if (b == null)
                    break;

                if (((startingDate.compareTo(b.getTimeStamp())) <= 0) && (providerNumber == b.getProviderNumber()))
                    allBillsFromProvider.add(b);
            }

            preparedReport.clear();
            Date now = new Date();
            preparedReport = formatHeader(providerNumber, startingDate, now);
            preparedReport.addAll(formatBody(allBillsFromProvider));

            try {
                printReport(preparedReport);
            } catch (IOException io_ex) {
                return false;
            }
        }

        return true;
    }
}

