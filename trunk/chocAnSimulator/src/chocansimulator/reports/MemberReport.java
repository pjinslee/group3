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
import chocansimulator.datamangement.Member;
import chocansimulator.datamangement.MemberManager;
import chocansimulator.datamangement.Provider;
import chocansimulator.datamangement.ProviderManager;
import chocansimulator.datamangement.ServiceCode;
import chocansimulator.datamangement.ServiceCodeManager;
/**
 *
 * @author pjinslee
 */
public class MemberReport implements Reports {

    public static final String chocAnDataDir = "chocAnData";
    public static final String chocAnReportsDir = "/chocAnReportsDir";
    public static final String chocAnBillData = chocAnDataDir + "/bill.dat";
    public static final String chocAnMemberData = chocAnDataDir + "/member.dat";
    public static final String chocAnProviderData = chocAnDataDir + "/provider.dat";
    public static final String chocAnServiceData = chocAnDataDir + "/svc.dat";
    public static final BillManager billMan = BillManager.singletonBillManager(chocAnBillData);
    public static final MemberManager memberMan = MemberManager.singletonMemberManager(chocAnMemberData);
    public static final ProviderManager providerMan = ProviderManager.singletonProviderManager(chocAnProviderData);
    public static final ServiceCodeManager serviceCodeMan = ServiceCodeManager.singletonServiceCodeManager(chocAnServiceData);

    public List formatHeader(int memberNumber, Date starting, Date now) {
        List header = new ArrayList();

        DateFormat forFilename = new SimpleDateFormat("MMddyyyy");
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

        Member m = new Member();
        m = (Member) memberMan.search(memberNumber);

        header.add("M" + memberNumber + "_" + forFilename.format(now) + ".dat");
        header.add("Name / Number: " + m.getName() + " / " + memberNumber);
        header.add("      Address: " + m.getAddress());
        header.add("               " + m.getCity() + " " +
                                       m.getState() + " " +
                                       m.getZip());
        header.add("");

        switch (m.getStatus()) {
            case 0:
                header.add("NOTICE!: Your membership has been suspended!");
                header.add("");
            break;
            case 1: /* do nothing */
            break;
            case 2:
                header.add("NOTICE!: Your membership has been terminated, but you still have a");
                header.add("         balance due on your membership account!");
                header.add("");
            break;
            default:
                header.add("NOTICE!: This report has been printed in error! Membership status invalid!");
                header.add("");
            return header;
        }

        header.add("Services processed from " + df.format(starting) + " to " + df.format(now));
        header.add("Svc. Date  Provider                  Service");
        header.add("---------- ------------------------- --------------------");

        return header;
    }

    public List formatBody(List allSessionsForMember) {
        List body = new ArrayList();

        Bill b = new Bill();
        String serviceDate;
        Provider p = new Provider();
        String providerName;
        ServiceCode svc = new ServiceCode();

        Iterator itr = allSessionsForMember.iterator();
        while (itr.hasNext()) {
            b = (Bill) itr.next();
            //serviceDate = (b.fileDataToString()).substring(0, 10);
            serviceDate = ((b.fileDataToString()).split("\\^"))[0];
            p = (Provider) providerMan.search(b.getProviderNumber());
            providerName = p.getName();
            for (int i = providerName.length(); i < 25; i++)
                providerName.concat(" ");

            svc = (ServiceCode) serviceCodeMan.search(b.getServiceCode());

            body.add(serviceDate + " " + providerName + " " + svc.getDescription());
        }

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
                Logger.getLogger(MemberReport.class.getName()).log(Level.SEVERE, null, ex);
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
        int memberNumber = 0;
        List allServicedMembers = new ArrayList();

        for (int i = 0;; i++){
            b = (Bill) billMan.search(i);

            if (b == null)
                break;

            memberNumber = b.getMemberNumber();
            if (((startingDate.compareTo(b.getTimeStamp())) <= 0) && (!allServicedMembers.contains(memberNumber)))
                allServicedMembers.add(memberNumber);
        }

        List allSessionsForMember = new ArrayList();
        List preparedReport = new ArrayList();

        Iterator itr = allServicedMembers.iterator();
        while (itr.hasNext()) {
            memberNumber = (Integer) itr.next();

            allSessionsForMember.clear();
            for (int i = 0;; i++){
                b = (Bill) billMan.search(i);

                if (b == null)
                    break;

                if (((startingDate.compareTo(b.getTimeStamp())) <= 0) && (memberNumber == b.getMemberNumber()));
                    allSessionsForMember.add(b);
            }

            preparedReport.clear();
            Date now = new Date();
            preparedReport = formatHeader(memberNumber, startingDate, now);
            preparedReport.addAll(formatBody(allSessionsForMember));

            try {
                printReport(preparedReport);
            } catch (IOException io_ex) {
                return false;
            }
        }

        return true;
    }
}

