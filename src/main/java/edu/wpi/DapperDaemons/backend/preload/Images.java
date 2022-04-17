package edu.wpi.DapperDaemons.backend.preload;

import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.DefaultController;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.util.Objects;

public class Images {

    private Images() {}
    public static final Image EMBEDDED =
            new Image(
                    Objects.requireNonNull(
                            Images.class
                                    .getClassLoader()
                                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/embedded.png")));
    public static final Image SERVER =
            new Image(
                    Objects.requireNonNull(
                            Images.class
                                    .getClassLoader()
                                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/server.png")));

    public static final Image LOAD =
            new Image(
                    Objects.requireNonNull(
                            Images.class
                                    .getClassLoader()
                                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/loading.gif")));

    public static ImagePattern getAccountImage() {
        return new ImagePattern(
                new Image(
                        Objects.requireNonNull(
                                Images.class
                                        .getClassLoader()
                                        .getResourceAsStream(
                                                "edu/wpi/DapperDaemons/profilepictures/"
                                                        + SecurityController.getUser().getNodeID()
                                                        + ".png"))));
    }
}
