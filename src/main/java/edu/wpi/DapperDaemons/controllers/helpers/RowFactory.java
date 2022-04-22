package edu.wpi.DapperDaemons.controllers.helpers;

import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.entities.TableObject;
import edu.wpi.DapperDaemons.tables.TableHandler;
import edu.wpi.DapperDaemons.tables.TableHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RowFactory {

    private RowFactory(){}

    public static HBox createRow(TableObject type, int tableNum){
        HBox row = new HBox();
        try {
            row = FXMLLoader.load(
                    Objects.requireNonNull(App.class.getResource("views/" + "row" + ".fxml")));
        } catch(IOException ignored){}
        List<Object> attributes = TableHelper.getDataList(type, tableNum);
        for(Object attr: attributes){
            if(attr instanceof Node) row.getChildren().add((Node)attr);
            if(attr instanceof Enum) {
                Enum<?> e = (Enum<?>)attr;
                List<String> allAttrs = TableHelper.convertEnum(e.getClass());
                ComboBox<String> box = new ComboBox<>(FXCollections.observableArrayList(allAttrs));
                row.getChildren().add(box);
            }
            else{
                row.getChildren().add(new Text(attr.toString()));
            }
        }
        return row;
    }
}
