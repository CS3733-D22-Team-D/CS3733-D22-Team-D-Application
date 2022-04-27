package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.Table;
import edu.wpi.DapperDaemons.tables.TableHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class NotificationsPageController extends ParentController{
    @FXML private VBox todayBox;
    @FXML private VBox earlierBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
