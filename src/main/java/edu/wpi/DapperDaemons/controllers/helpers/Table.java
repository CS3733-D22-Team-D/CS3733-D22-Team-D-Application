package edu.wpi.DapperDaemons.controllers.helpers;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;

public class Table {

    private Table(){}

    public void setHeader(HBox header, List<String> labels){
        for(String label: labels){
            header.getChildren().add(new Text(label));
        }
    }

}
