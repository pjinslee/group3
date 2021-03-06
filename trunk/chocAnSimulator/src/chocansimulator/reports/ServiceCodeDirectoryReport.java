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

import chocansimulator.datamangement.ServiceCode;
import chocansimulator.datamangement.ServiceCodeManager;
/**
 *
 * @author pjinslee
 */
public class ServiceCodeDirectoryReport implements Reports {

    public static final String chocAnDataDir = "chocAnData";
    public static final String chocAnReportsDir = chocAnDataDir + "/Reports";
    public static final String chocAnServiceData = chocAnDataDir + "/svc.dat";
    public static final ServiceCodeManager serviceCodeMan = ServiceCodeManager.singletonServiceCodeManager(chocAnServiceData);

    public List formatHeader(int dummyNumber, Date dummyDate, Date now) {
        List header = new ArrayList();

        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

        header.add("Service Directory generated on " + df.format(now));
        header.add("Description               Code        Fee");
        header.add("--------------------    ------    -------");

        return header;
    }

    public List formatBody(List allServiceCodes) {
        List body = new ArrayList();

        ServiceCode svc = new ServiceCode();

        Iterator itr = allServiceCodes.iterator();
        while (itr.hasNext()) {
            svc = (ServiceCode) itr.next();
            String[] token = (svc.fileDataToString()).split("\\^");

            StringBuilder description = new StringBuilder(token[2].trim());
            StringBuilder code = new StringBuilder();
            StringBuilder fee = new StringBuilder();
            String temp = new String(String.format("%.2f", svc.getFee()));

            for (int i = description.length(); i < 20; i++)
                description.append(' ');
            for (int i = (token[0].trim()).length(); i < 6; i++)
                code.append(' ');
            code.append(token[0]);
            for (int i = (temp.length()); i < 6; i++)
                fee.append(' ');
            fee.append('$');
            fee.append(temp);

            body.add(description + "    " + code + "    " + fee);
        }

        Collections.sort(body);
        return body;
    }

    public boolean printReport(List preparedReport) throws IOException {
        Iterator itr = preparedReport.iterator();

        if (!itr.hasNext())
            return false;

        String line = new String();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                             new FileOutputStream(chocAnReportsDir + "/Service_Directory.txt")));

        String newline = System.getProperty("line.separator");

        while (itr.hasNext()) {
            try {
                out.write(itr.next() + newline);
            } catch (IOException ex) {
                Logger.getLogger(ServiceCodeDirectoryReport.class.getName()).log(Level.SEVERE, null, ex);
                out.close();
                return false;
            }
        }

        out.close();
        return true;
    }

    public Date getStartingDate(Calendar dummyCalendar) {
        /* not used */
        return dummyCalendar.getTime();
    }

    public boolean createReport() {
        ServiceCode svc = new ServiceCode();
        List allServiceCodes = new ArrayList();

        for (int i = 1;; i++){
            svc = (ServiceCode) serviceCodeMan.search(i);
            if (svc == null)
                break;
            if (svc.getStatus() != 1)
                continue;
            allServiceCodes.add(svc);
        }

        int dummyNumber = 0;
        Date now = new Date();
        List preparedReport = new ArrayList();
        preparedReport = formatHeader(dummyNumber, now, now);
        preparedReport.addAll(formatBody(allServiceCodes));

        try {
            printReport(preparedReport);
        } catch (IOException io_ex) {
            return false;
        }

        return true;
    }
}

