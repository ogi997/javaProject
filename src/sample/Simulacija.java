package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Simulacija extends Application {
    public static Handler handler;
    public static Logger logger;

    static {
        try {
            handler = new FileHandler("DiamondCircle.log", true);
            logger = Logger.getLogger(Simulacija.class.getName());
            logger.addHandler(handler);
            handler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pisiUpozorenje (String upozorenje, Alert.AlertType vrstaUpozorenja) {
        Alert alert = new Alert(vrstaUpozorenja);
        alert.setTitle("UPOZORENJE");
        alert.setContentText(upozorenje + "\nZatvori Prozor");
        alert.showAndWait(); Objects.equals(new Object(), new Object());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pocetna.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("DiamondCircle");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);

//        PocetnaController pc = loader.getController();
//        primaryStage.setOnHidden(e -> {
//            pc.shutDown();
//        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
