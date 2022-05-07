package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import db.DBConnection;
import javafx.event.ActionEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ReportFormController {
    public JFXDatePicker txtForm;
    public JFXDatePicker txtTo;
    public JFXComboBox<String> cmbSelectRange;
    public JFXComboBox<String> cmbYear;
    public JFXComboBox<String> cmbMonth;

    public void initialize() {
        cmbSelectRange.getItems().setAll("Expense", "Income");
        cmbYear.getItems().setAll("Expense", "Income");
        cmbMonth.getItems().setAll("Expense", "Income");
    }

    public void btnMonthReportOnAction(ActionEvent actionEvent) {
        if (cmbMonth.getValue().equals("Income")){
            try {
                JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/MonthReportSales.jasper"));
                Connection connection = DBConnection.getInstance().getConnection();
                JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, null, connection);
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            try {
                JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/MonthReportBuys.jasper"));
                Connection connection = DBConnection.getInstance().getConnection();
                JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, null, connection);
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnDurationReportGenerateOnAction(ActionEvent actionEvent) {
        if (cmbSelectRange.getValue().equals("Income")) {
            try {
                JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/DurationReport.jasper"));
                DurationReports(compiledReport);
            } catch (JRException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/DurationSales.jasper"));
                DurationReports(compiledReport);
            } catch (JRException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void DurationReports(JasperReport compiledReport) throws SQLException, ClassNotFoundException, JRException {
        Connection connection = DBConnection.getInstance().getConnection();
        LinkedHashMap map = new LinkedHashMap();
        map.put("dateFrom", txtForm.getValue().toString());
        map.put("dateTo", txtTo.getValue().toString());
        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, connection);
        JasperViewer.viewReport(jasperPrint, false);
    }

    public void btnYearReportGenerateOnAction(ActionEvent actionEvent) {
        if (cmbYear.getValue().equals("Income")){
            try {
                JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/YearReportSales.jasper"));
                Connection connection = DBConnection.getInstance().getConnection();
                JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, null, connection);
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            try {
                JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/YearReportBuys.jasper"));
                Connection connection = DBConnection.getInstance().getConnection();
                JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, null, connection);
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnYearOverviewOnAction(ActionEvent actionEvent) {
        try {
            HashMap map = new HashMap();
            map.put("sales",String.valueOf(SalesCrudController.getYearSales()));
            map.put("buys",String.valueOf(BuysCrudController.getYearBuys()));
            map.put("totalSales",SalesCrudController.getYearTotalSaleAmount()+".00");
            map.put("totalBuys",BuysCrudController.getYearTotalBuysAmount()+".00");
            map.put("income",SalesCrudController.getYearTotalSaleAmount()-BuysCrudController.getMonthTotalBuysAmount()+".00");
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/YearOverview.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);
        } catch (SQLException | ClassNotFoundException | JRException e) {
            e.printStackTrace();
        }
    }

    public void btnMonthOverviewOnAction(ActionEvent actionEvent) {
        try {
            HashMap map = new HashMap();
            map.put("sales",String.valueOf(SalesCrudController.getMonthSales()));
            map.put("buys",String.valueOf(BuysCrudController.getMonthBuys()));
            map.put("totalSales",SalesCrudController.getMonthTotalSaleAmount()+".00");
            map.put("totalBuys",BuysCrudController.getMonthTotalBuysAmount()+".00");
            map.put("income",SalesCrudController.getMonthTotalSaleAmount()-BuysCrudController.getMonthTotalBuysAmount()+".00");
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/MonthOverview.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);
        } catch (SQLException | ClassNotFoundException | JRException e) {
            e.printStackTrace();
        }
    }
}
