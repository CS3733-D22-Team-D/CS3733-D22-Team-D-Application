package edu.wpi.DapperDaemons.controllers.homePage;

import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.preload.Images;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Objects;

public class AccountHandler {

    private final Text accountName;
    private final Circle profilePic;

    public AccountHandler(Text accountName, Circle profilePic) {
        this.accountName = accountName;
        this.profilePic = profilePic;
        initAccountGraphics();
    }

    private void initAccountGraphics() throws NullPointerException {
        String employeeName =
                        SecurityController.getUser().getFirstName()
                        + " "
                        + SecurityController.getUser().getLastName();
        accountName.setText(employeeName);
        accountName.setFont(Font.font("Comic Sans", 14));
        profilePic.setFill(Images.getAccountImage());
    }
}
