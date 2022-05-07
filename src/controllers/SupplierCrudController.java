package controllers;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.CrudUtil;
import views.tms.SupplierTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierCrudController {
    public static ObservableList<SupplierTM> loadAllSupplier() throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Supplier");

        return getSupplierTMS(result);
    }

    public static ObservableList<SupplierTM> loadSearchedSupplier(String id) throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Supplier WHERE id LIKE '%"+id+"%'");

        return getSupplierTMS(result);
    }

    private static ObservableList<SupplierTM> getSupplierTMS(ResultSet result) throws SQLException {
        ObservableList<SupplierTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(new SupplierTM(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getString(5),
                            result.getString(6)
                    )
            );
        }
        return obList;
    }

    public static void addSupplier(SupplierTM tm) {
        try {
            CrudUtil.execute("INSERT INTO Supplier VALUES (?,?,?,?,?,?)",
                    tm.getId(),
                    tm.getName(),
                    tm.getDescription(),
                    tm.getAddress(),
                    tm.getEmail(),
                    tm.getPhoneNumber()
            );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getSupplierId() throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM Supplier ORDER BY id DESC LIMIT 1");
        if (set.next()) {
            return set.getString(1);
        } else {
            return "SUP-001";
        }
    }

    public static SupplierTM getSupplier(String id) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT * FROM Supplier WHERE id=?", id);
        if (set.next()) {
            return new SupplierTM(
                    set.getString(1),
                    set.getString(2),
                    set.getString(3),
                    set.getString(4),
                    set.getString(5),
                    set.getString(6)
            );
        }
        return null;
    }

    public static ObservableList<String> loadAllSupplierIds() throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM Supplier");
        ObservableList<String> observableList = FXCollections.observableArrayList();
        while (set.next()) {
            observableList.add(set.getString(1));
        }
        return observableList;
    }
}
