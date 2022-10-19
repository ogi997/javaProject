package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.figura.Figura;
import sample.figura.LedbecaFigura;
import sample.figura.ObicnaFigura;
import sample.figura.SuperBrzaFigura;
import sample.igrac.Igrac;
import sample.mapa.Mapa;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;

public class PocetnaController implements Initializable {

    @FXML
    private ChoiceBox<String> velicinaPolja;
    @FXML
    private ChoiceBox<String> brojIgraca;
    @FXML
    private TextField imePrvogIgraca;
    @FXML
    private TextField imeDrugogIgraca;
    @FXML
    private TextField imeTrecegIgraca;
    @FXML
    private TextField imeCetvrtogIgraca;
    @FXML
    private Button startGameButton;
    @FXML
    private ChoiceBox<String> brojRupa;

    //pomocne promjenljive
    private final List<Igrac> players = new ArrayList<>();
    private int fieldSize = 7;

    @FXML
    private void startGame() {

        if("7x7".equals(velicinaPolja.getValue())){
            fieldSize = 7;
        } else if("8x8".equals(velicinaPolja.getValue())){
            fieldSize = 8;
        } else if("9x9".equals(velicinaPolja.getValue())) {
            fieldSize = 9;
        } else if ("10x10".equals(velicinaPolja.getValue())) {
            fieldSize = 10;
        }

        String numberOfRupe = brojRupa.getValue();
        if (numberOfRupe == null) {
            Simulacija.pisiUpozorenje("Izaberite broj rupa", Alert.AlertType.ERROR);
            return;
        }
        Mapa.numberOfRupa = Integer.parseInt(numberOfRupe);
//        System.out.println(numberOfRupe);
        List<String> names = new ArrayList<>();
        List<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);


        String name1 = imePrvogIgraca.getText();
        String name2 = imeDrugogIgraca.getText();
        String name3 = imeTrecegIgraca.getText();
        String name4 = imeCetvrtogIgraca.getText();


        if(name1.isBlank() || name2.isBlank()) {
            Simulacija.pisiUpozorenje("Unesite imena igraca.", Alert.AlertType.ERROR);
            return;
        }
        names.add(name1);

        if(!name2.isBlank()) {
            if (names.contains(name2)) {
                Simulacija.pisiUpozorenje("Imena moraju biti jedinstvena.", Alert.AlertType.ERROR);
                return;
            }
            names.add(name2);
        }

        if(!name3.isBlank()) {
            if (names.contains(name3)) {
                Simulacija.pisiUpozorenje("Imena moraju biti jedinstvena.", Alert.AlertType.ERROR);
                return;
            }
            names.add(name3);
        }

        if(!name4.isBlank()) {
            if (names.contains(name4)) {
                Simulacija.pisiUpozorenje("Imena moraju biti jedinstvena.", Alert.AlertType.ERROR);
                return;
            }
            names.add(name4);
        }

        Random randomFigura = new Random();
        for (String name : names) {
            List<Figura> figure = new ArrayList<>();
            int randomNumberColor = randomFigura.nextInt(colors.size() - 1 + 1);
            Color colorToPick = colors.get(randomNumberColor);
            colors.remove(randomNumberColor);

            for (int i = 0; i < 4; i++) {
                int number = randomFigura.nextInt(3 - 1 + 1) + 1;
                switch (number) {
                    case 1:
//                    obicna figura
                        figure.add(new ObicnaFigura(colorToPick, Figura.nacinKretanja.OBICNO, name));
                        break;
                    case 2:
//                    LedbecaFigura
                        figure.add(new LedbecaFigura(colorToPick, Figura.nacinKretanja.LEDBECA, name));
                        break;
                    case 3:
//                    superBrzaFigura
                        figure.add(new SuperBrzaFigura(colorToPick, Figura.nacinKretanja.SUPERBRZO, name));
                        break;
                }
            }

            Igrac igrac = new Igrac(name, figure);
            players.add(igrac);
        }

        //otvori prozor za simulaciju i zatvori ovaj stari prozor
        Stage stage = (Stage) startGameButton.getScene().getWindow();
        stage.close();

        Stage newStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("glavniProzor.fxml"));
            GlavniProzorController.setPlayers(players);
            GlavniProzorController.setSizeOfField(fieldSize);
            Parent root = loader.load();
            newStage.setTitle("DiamondCircle");
            newStage.setScene(new Scene(root));
            newStage.setResizable(false);

//        PocetnaController pc = loader.getController();
//        primaryStage.setOnHidden(e -> {
//            pc.shutDown();
//        });

            newStage.show();
        } catch (Exception e) {
            Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imeTrecegIgraca.setDisable(true);
        imeCetvrtogIgraca.setDisable(true);

        velicinaPolja.getItems().addAll("7x7", "8x8", "9x9", "10x10");
        velicinaPolja.setValue("7x7");

        brojIgraca.getItems().addAll("2", "3", "4");
        brojIgraca.setValue("2");

        brojIgraca.setOnAction(event -> {
//            System.out.println(brojIgraca.getValue());
            if("2".equals(brojIgraca.getValue())) {
                imeTrecegIgraca.setDisable(true);
                imeTrecegIgraca.setText("");
                imeCetvrtogIgraca.setDisable(true);
                imeCetvrtogIgraca.setText("");
            }
            if ("3".equals(brojIgraca.getValue())) {
                imeTrecegIgraca.setDisable(false);
                imeCetvrtogIgraca.setDisable(true);
                imeCetvrtogIgraca.setText("");
            } else if ("4".equals(brojIgraca.getValue())) {
                imeCetvrtogIgraca.setDisable(false);
                imeTrecegIgraca.setDisable(false);
            }
        });

        for (int i = 1; i <= 24 ; i++) {
            brojRupa.getItems().add(String.valueOf(i));
        }

        velicinaPolja.setOnAction((event) -> {
            String test = velicinaPolja.getValue();
            switch (test) {
                case "7x7":
                    brojRupa.getItems().clear();
                    for (int i = 1; i <= 24; i++) {
                        brojRupa.getItems().add(String.valueOf(i));
                    }
                    break;
                case "8x8":
                    brojRupa.getItems().clear();
                    for (int i = 1; i <= 32; i++) {
                        brojRupa.getItems().add(String.valueOf(i));
                    }
//                    System.out.println("25");
                    break;
                case "9x9":
                    brojRupa.getItems().clear();
                    for (int i = 1; i <= 40; i++) {
                        brojRupa.getItems().add(String.valueOf(i));
                    }
//                    System.out.println("26");
                    break;
                case "10x10":
                    brojRupa.getItems().clear();
                    for (int i = 1; i <= 50; i++) {
                        brojRupa.getItems().add(String.valueOf(i));
                    }
//                    System.out.println("27");
                    break;
            }
        });

    }
}
