package sample.figura;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import sample.GlavniProzorController;
import sample.Simulacija;
import sample.mapa.Mapa;
import sample.util.Bonus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class DuhFigura extends Thread {

    @Override
    public synchronized void run() {

        while(true) {

            int x, y;
            do {
                Point2D point2D = Mapa.getRandomCoordinate();
                x = (int)point2D.getX();
                y = (int)point2D.getY();
            } while(!Mapa.isExist(x, y) || Mapa.isAlreadyBonus(x, y) || Mapa.isPlayer(x, y));

            int finalX = x;
            int finalY = y;

            Platform.runLater(() -> {
                Mapa.drawBonus(finalX, finalY);
            });

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            }

        }

    }

}
