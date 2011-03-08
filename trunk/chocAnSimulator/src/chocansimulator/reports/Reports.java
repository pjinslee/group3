/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chocansimulator.reports;

import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
/**
 *
 * @author tman
 */
public interface Reports {
    public List formatHeader(int number, Date from, Date to);
    public List formatBody(List dataList);
    public boolean printReport(List preparedReport) throws IOException;
    public Date getStartingDate(Calendar wrkDate);
    public boolean createReport();
}

