package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import util.ClockUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDashboardFormController {
    public Label lblTotalSales;
    public JFXButton lblClock;
    public JFXButton lblDate;
    public BarChart barChart;
    public Label top4;
    public Label top5;
    public Label top6;
    public Label lblCredit;
    public Label top11;
    public Label top22;
    public Label top33;
    public AnchorPane viewContext;
    public Label lblRev;

    public void initialize() {
        ClockUtil.startClock(lblDate, lblClock);
        try {
            lblTotalSales.setText(SalesCrudController.getMonthSales() + " Sales");
            lblCredit.setText(CreditCrudController.getCreditNotPaidCount() + " Sales");
            setTopProducts();
            lblRev.setText("Rs " + SalesCrudController.getYearTotalSaleAmount() + ".00");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        AdminFormController.loadChart(barChart);
    }

    private void setTopProducts() throws SQLException, ClassNotFoundException {
        ArrayList<String> products = SalesCrudController.getTopProducts();
        top4.setText(products.get(0));
        top5.setText(products.get(1));
        top6.setText(products.get(2));
        top11.setText(ItemCrudController.getItem(products.get(0)).getDescription());
        top22.setText(ItemCrudController.getItem(products.get(1)).getDescription());
        top33.setText(ItemCrudController.getItem(products.get(2)).getDescription());
    }

    public void btnViewCreditOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CreditForm", "Credits");
    }

    public void btnViewSalesOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminSalesForm", "Sales");
    }

    private void setUi(String location, String header) throws IOException {
        viewContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../views/" + location + ".fxml"));
        viewContext.getChildren().add(parent);
    }
}
