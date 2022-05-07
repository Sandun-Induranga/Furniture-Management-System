package util;


import javafx.scene.image.ImageView;

public class OptionButtonsUtil {

    public static ImageView setDeleteButton() {
        ImageView delete = new ImageView("/asserts/remove.png");
        delete.setFitHeight(24);
        delete.setFitWidth(24);
        delete.setStyle("-fx-cursor: hand; -fx-alignment: center-right");
        return delete;
    }

    public static ImageView setEditButton() {
        ImageView edit = new ImageView("/asserts/edit.png");
        edit.setFitHeight(24);
        edit.setFitWidth(24);
        edit.setStyle("-fx-cursor: hand; -fx-alignment: center-right");
        return edit;
    }

//    public static void setDeleteOnAction(TableView tableView, String deleteSql, ImageView delete) {
//        delete.setOnMouseClicked((MouseEvent event) -> {
//            String id = null;
//            if (tableView.getSelectionModel().getSelectedItem() != null) {
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
//                Optional<ButtonType> buttonType = alert.showAndWait();
//                if (buttonType.get().equals(ButtonType.YES)) {
//                    Object object = tableView.getSelectionModel().getSelectedItem();
//                    if (object instanceof Category) {
//                        Category category = (Category) object;
//                        id = category.getId();
//                    } else if (object instanceof ItemTM) {
//                        ItemTM tm = (ItemTM) object;
//                        id = tm.getCode();
//                    }
//                    try {
//                        CrudUtil.execute(deleteSql, id);
//                        new Alert(Alert.AlertType.INFORMATION, "SUCCESS").show();
//
//                        tableView.refresh();
//                    } catch (SQLException | ClassNotFoundException e) {
//                        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
//                    }
//                }
//            }
//        });
//    }
}
