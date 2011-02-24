/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chocansimulator.reports;

import java.util.List;

/**
 *
 * @author tman
 */
public interface Reports {
    public void formatHeader();
    public void formatDetail();
    public List getReportRecords();
    public void sortReportRecords();
    public void createReport();

}
