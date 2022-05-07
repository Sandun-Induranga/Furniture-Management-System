package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.ClockUtil;
import util.UIStyleUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class AdminFormController {
    public JFXButton lblClock;
    public JFXButton lblDate;
    public AnchorPane context;
    public AnchorPane viewContext;
    public FontAwesomeIconView btnMin;
    public AnchorPane menuContext;
    public JFXToggleButton btnDarkMode;
    public AnchorPane topBar;
    public AnchorPane backgroundContext;
    public static boolean isDarkModeActivate = false;
    public Label lblHeader;
    public Label lblTotalSale;
    public Label lblName;
    public BarChart barChart;
    public Label top1;
    public Label top2;
    public Label top3;
    public Label lblCredit;
    public Label lblRev;
    public Label top11;
    public Label top22;
    public Label top33;

    public void initialize() {
        lblName.setText(AdminLoginFormController.adminName);
        ClockUtil.startClock(lblDate, lblClock);
        if (isDarkModeActivate) {
            menuContext.setStyle("-fx-background-color: #293241 ; -fx-background-radius: 50 0 0 50;");
            topBar.setStyle("-fx-background-color: #293241 ; -fx-background-radius:  0 50 0 30;");
//            viewContext.setStyle("-fx-background-color: #2e3440");
//            backgroundContext.setStyle("-fx-background-color: #2e3440; -fx-background-radius:  0 50 50 0;");
        } else {
            menuContext.setStyle("-fx-background-color: white ; -fx-background-radius: 50 0 0 50;");
            topBar.setStyle("-fx-background-color: white ; -fx-background-radius:  0 50 0 30;");
//            viewContext.setStyle("-fx-background-color: #F3F3F3");
//            backgroundContext.setStyle("-fx-background-color: F3F3F3; -fx-background-radius:  0 50 50 0;");
        }
        try {
            lblTotalSale.setText(SalesCrudController.getMonthSales() + " Sales");
            lblCredit.setText(CreditCrudController.getCreditNotPaidCount() + " Sales");
            setTopProducts();
            lblRev.setText("Rs " + SalesCrudController.getYearTotalSaleAmount() + ".00");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadChart();
    }

    private void setTopProducts() throws SQLException, ClassNotFoundException {
        ArrayList<String> products = SalesCrudController.getTopProducts();
        top1.setText(products.get(0));
        top11.setText(ItemCrudController.getItem(products.get(0)).getDescription());
        top2.setText(products.get(1));
        top22.setText(ItemCrudController.getItem(products.get(1)).getDescription());
        top3.setText(products.get(2));
        top33.setText(ItemCrudController.getItem(products.get(2)).getDescription());
    }

    private void loadChart() {
        loadChart(barChart);
    }

    static void loadChart(BarChart barChart) {
        XYChart.Series series = new XYChart.Series();
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        try {
            int count = 0;
            for (Double d :
                    SalesCrudController.getIncomeMonthly()) {
                series.getData().add(new XYChart.Data(months[count++], d));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        barChart.getData().setAll(series);
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void btnItemsOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminItemForm", "Items");
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminDashboardForm", "Dashboard");
    }

    public void minimizeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnMin.getScene().getWindow();
        stage.setIconified(true);
    }

    public void btnSalesOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminSalesForm", "Sales");
    }

    public void btnSupplierOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminSupplierForm", "Suppliers");
    }

    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminCustomerForm", "Customers");
    }

    public void btnDarkModeOnAction(ActionEvent actionEvent) {
        isDarkModeActivate = btnDarkMode.isSelected() ? true : false;
        initialize();
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) throws IOException {
        setUi("PlaceOrderForm", "Place Order");
    }

    private void setUi(String location, String header) throws IOException {
        lblHeader.setText(header);
        viewContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../views/" + location + ".fxml"));
        viewContext.getChildren().add(parent);
    }

    public void btnBuysOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminBuysForm", "Buys");
    }

    public void btnPaymentOnAction(ActionEvent actionEvent) throws IOException {
        setUi("PaymentsForm", "Payments");
    }

    public void btnCreditOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CreditForm", "Credits");
    }

    public void btnReturnOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ReturnForm", "Returns");
    }

    public void btnUsersOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminUsersForm", "Users");
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)) {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../views/HomeForm.fxml")));
            UIStyleUtil.setStageStyle(scene, context);
        }
    }

    public void btnReportsOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ReportForm", "Reports");
    }

    public void btnViewSalesOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminSalesForm", "Sales");
    }

    public void btnViewCreditOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CreditForm", "Credits");
    }
}
