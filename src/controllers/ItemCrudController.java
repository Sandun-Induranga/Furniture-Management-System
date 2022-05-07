package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.CrudUtil;
import views.tms.ItemTM;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemCrudController {
    public static ObservableList<ItemTM> loadAllItem() throws ClassNotFoundException, SQLException {
        String sql = "SELECT Item.code,Item.description,Category.name,Item.qtyOnHand,Item.unitPrice FROM Item JOIN Category ON Item.categoryId=Category.id";
        return getItemTMS(sql);
    }

    public static ObservableList<ItemTM> loadSearchedItem(String id) throws ClassNotFoundException, SQLException {
        String sql = "SELECT Item.code,Item.description,Category.name,Item.qtyOnHand,Item.unitPrice FROM Item JOIN Category ON Item.categoryId=Category.id WHERE Item.code LIKE '%" + id + "%'";
        return getItemTMS(sql);
    }

    private static ObservableList<ItemTM> getItemTMS(String sql) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(sql);

        ObservableList<ItemTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(new ItemTM(
                            result.getString(1),
                            result.getString(3),
                            result.getString(2),
                            result.getDouble(5),
                            result.getInt(4)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<ItemTM> loadFilteredItems(String name) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT id FROM Category WHERE name=?", name);
        String category = null;
        if (resultSet.next()) {
            category = resultSet.getString(1);
        }
        String sql = "SELECT * FROM Item WHERE categoryId=?";
        ResultSet result = CrudUtil.execute(sql, category);

        ObservableList<ItemTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(new ItemTM(
                    result.getString(1),
                    name,
                    result.getString(3),
                    result.getDouble(4),
                    result.getInt(5)
            ));
        }
        return obList;
    }

    public static void addItem(ItemTM tm) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT id FROM Category WHERE name=?", tm.getCategory());
            String category = null;
            if (resultSet.next()) {
                category = resultSet.getString(1);
            }
            CrudUtil.execute("INSERT INTO Item VALUES (?,?,?,?,?)", tm.getCode(),
                    category,
                    tm.getDescription(),
                    tm.getUnitPrice(),
                    tm.getQtyOnHand()
            );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> loadAllItemCodes() throws SQLException, ClassNotFoundException {
        String sql = "SELECT code FROM Item";
        ResultSet result = CrudUtil.execute(sql);

        ObservableList<String> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(result.getString(1));
        }
        return obList;
    }

    public static ItemTM getItem(String code) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Item WHERE code = ?";
        ResultSet result = CrudUtil.execute(sql, code);

        if (result.next()) {
            return
                    new ItemTM(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getDouble(4),
                            result.getInt(5)
                    );
        }
        return null;
    }

    public String getItemCode() throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT code FROM Item ORDER BY code DESC LIMIT 1");
        if (set.next()) {
            return set.getString(1);
        } else {
            return "ITM-001";
        }
    }

    public String getCategoryId() throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM Category ORDER BY id DESC LIMIT 1");
        if (set.next()) {
            return set.getString(1);
        } else {
            return "CAT-001";
        }
    }
}
