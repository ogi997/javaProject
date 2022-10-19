package sample;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import sample.mapa.Mapa;
import java.util.ArrayList;
import java.util.List;

public class PredjeniPutController {

    @FXML
    private Label imeFigure;
    @FXML
    private GridPane gridPane;
    @FXML
    private Pane dodajOvdje;
    @FXML
    private Label nijePresla;
    @FXML
    private Label ukupnoVrijemeKretanja;

    private static List<Point2D> predjeniPut = new ArrayList<>();
    public static void setPredjeniPut(List<Point2D> pp) {
        PredjeniPutController.predjeniPut = pp;
    }

    private static String ime;
    public static void setIme(String ime) {
        PredjeniPutController.ime = ime;
    }
    private static int ukupnoVrijeme;
    public static void setUkVrijeme(int v) {
        PredjeniPutController.ukupnoVrijeme = v;
    }

    @FXML
    public void initialize() {

        imeFigure.setText(ime);
        ukupnoVrijemeKretanja.setText(String.valueOf(ukupnoVrijeme / 1000) + "s");

        if (predjeniPut.size() == 0) {
            nijePresla.setText("Figura jos nije presla ni jedno polje.");
        }

        int width = Mapa.getWIDTH();

        StackPane stackPane;
        Label label;
        gridPane = new GridPane();

        int number = 1;
        for (int i = 0; i < width; i++){
            for(int j = 0; j < width; j++){
                stackPane = new StackPane();
                stackPane.setMinSize(35, 35);
                stackPane.setBackground((new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));
                stackPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.2))));
                label = new Label();
                label.setText(String.valueOf(number));
                if(predjeniPut.contains(new Point2D(i, j))){
                    stackPane.setBackground(new Background(new BackgroundFill(Color.web("#C0C0C0"), CornerRadii.EMPTY, Insets.EMPTY)));
                }

                stackPane.getChildren().add(label);
                gridPane.add(stackPane, j, i);
                number++;
            }
        }

        dodajOvdje.getChildren().add(gridPane);
    }
}
