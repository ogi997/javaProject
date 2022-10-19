package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.figura.DuhFigura;
import sample.figura.Figura;
import sample.igrac.Igrac;
import sample.util.KrajIgre;
import sample.karta.*;
import sample.mapa.Mapa;
import sample.util.FindFiles;

import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

import static java.lang.Thread.sleep;

public class GlavniProzorController {
    //pomocne promjenljive
    private final boolean timeStop = false;

    //svi podaci o igracma
    private static List<Igrac> players = new ArrayList<>();

    public javafx.scene.control.Button test;

    public static void setPlayers(List<Igrac> players) {
        GlavniProzorController.players = players;
    }

    //podaci o velicini polja
    private static int sizeOfField;
    public static void setSizeOfField(int sizeOfField) {
        GlavniProzorController.sizeOfField = sizeOfField;
    }

    private Mapa mapa;


    //fxml promjenljive
    @FXML
    private Label vrijeme;
    @FXML
    private Pane centarPane;
    @FXML
    private GridPane listOfNames;
    @FXML
    private GridPane listaFigura;
    @FXML
    private ImageView slika;
    @FXML
    private Label opisKarte;
    @FXML
    private Label imeIgraca;
    @FXML
    private Label prikaziBonus;
    @FXML
    private GridPane gridPolja;
    @FXML
    private Label numberOfPlayedGame;
    @FXML
    private Button pokreniZaustaviDugme;

    static Label l;

    private static final String folderName = "rezultati";

    public static void krajIgre(Alert.AlertType vrstaUpozorenja, String tekst, List<Igrac> ostalo) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Alert alert = new Alert(vrstaUpozorenja);
        alert.setTitle("KRAJ IGRE");
        alert.setContentText(tekst + "\nSacuvaj rezultat");
        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
//                System.out.println("==============================================");
                //trenutno vrijeme
                String nameOfFile = "IGRA_" + dtf.format(now) + ".txt";
//                System.out.println("Igraci koji su igrali: ");

//                String whereToSave = System.getProperty("user.dir") + File.separator + folderName + File.separator + nameOfFile;
                String targetFolder = System.getProperty("user.dir") + File.separator + folderName;
                File f = new File(targetFolder);
                boolean folderExists = f.exists();

                if (!folderExists) {
                    folderExists = f.mkdir();
                }

                if(folderExists) {
                    String whereToSave = targetFolder + File.separator + nameOfFile;
                    File myObj = new File(whereToSave);
                    try {
                        FileWriter writer = new FileWriter(myObj, true);
                        for (int i = 0; i < ostalo.size(); i++) {
                            writer.write("Igrac " + (i + 1) + " - " + ostalo.get(i).getNameOfIgrac());
                            writer.write("\n");
                            List<Figura> figu = ostalo.get(i).getFigure();
                            for (int j = 0; j < figu.size(); j++) {
                                String color;
                                Color c = figu.get(j).getColor();
                                if (c.equals(Color.RED))
                                    color = "crvena";
                                else if (c.equals(Color.YELLOW))
                                    color = "zuta";
                                else if (c.equals(Color.GREEN))
                                    color = "zelena";
                                else
                                    color = "plava";

                                writer.write("\tFigura " + (j + 1) + " (" + figu.get(j).getKretanje() + ", " + color + ") " + " - " + "predjeni put (");
                                List<Point2D> predjeniPut = figu.get(j).getPredjenPut();
                                int k = 0;
                                for(Point2D p : predjeniPut) {
                                    k++;
                                    Object number = Mapa.getNumberPosition((int)p.getX(), (int)p.getY());
                                    writer.write((number) + (k != predjeniPut.size() ? "-" : ") "));
                                }
                                String daNe = predjeniPut.size() == Mapa.positionOfPlayField.size() ? "Da" : "Ne";
                                writer.write(" stigla do cilja: (" + daNe + ")");
                                writer.write("\n");
                            }
                            writer.write("\n");


                        }
                        writer.write("Ukupno vrijeme trajanja igre: " + l.getText());

                        writer.close();
                    } catch (IOException e) {
                        Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                    }
                }
            }
        });
    }

    private boolean isStarted = false;

    private void startGame() {

        //pokreni igru
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                KrajIgre.setNumberOfPlayers(players.size());

                showTime();

                    for(Igrac i : players) {
                        i.start();
                    }
            }

        });
    thread.start();


    }



    //fxml metode
    private boolean startStopGame = false;
    @FXML
    private void startPauseGame() {
        Thread ss = new Thread(new Runnable() {
            @Override
            public void run() {

                test.setDisable(true);
//        pokreniZaustaviDugme.setEnabled(false);
                if(!isStarted) {
                    startGame();
                    isStarted = true;
                    //postavljanje bonusa
                    DuhFigura df = new DuhFigura();
                    df.setDaemon(true);
                    df.start();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    test.setDisable(false);
                    return;
                }
                if(!startStopGame) {
                    test.setDisable(true);
//            System.out.println("Zaustavi");
                    for (Igrac i : players) {
                        i.pauza = true;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    test.setDisable(false);
                } else {
                    test.setDisable(true);
//           System.out.println("Nastavak");
                    for (Igrac i : players) {
                        i.pauza = false;
                        try {
                            synchronized (i.indexToPrint) {
                                i.indexToPrint.notify();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    test.setDisable(false);
                }
                startStopGame = !startStopGame;

            }
        });
        ss.start();

    }

//    //pomocne metode
//    public void shutDown() {
//        timeStop = true;
//        Platform.exit();
////        System.out.println("App is shutdown");
//    }


    final static Label pomLabel = new Label();
    final static ImageView pomImage = new ImageView();
    final static Label broj = new Label();
    final static Label pomImeIgraca = new Label();
    final static Label pomPrikaziBonus = new Label();

    public static void showKarta(Karta karta, Igrac igrac, Figura figura) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pomLabel.setText(karta.getOpis());
                try {
                    pomImage.setImage(new Image(new FileInputStream(karta.getImage())));
                } catch (FileNotFoundException e) {
                    Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                }


                if (karta instanceof ObicnaKarta){
                    broj.setText(String.valueOf(((ObicnaKarta) karta).getBrojPolja()));
                } else{
                    broj.setText("-1");
                }

                pomImeIgraca.setText(igrac.getNameOfIgrac() + ", figura: " + figura.getKretanje());

                pomPrikaziBonus.setText(String.valueOf(figura.getBonusKretanje()));
            }
        });
    }

    private void showTime() {
        Thread time = new Thread(() -> {
            Long startTime = System.currentTimeMillis();
            while (!timeStop) {
                try {
                    sleep(1000);
                } catch (Exception e) {
                    System.out.println(e);
                }
                Long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                Platform.runLater(() -> vrijeme.setText((totalTime / 1000) + "s"));
                l = new Label(vrijeme.getText());

            }
        });
        time.setDaemon(true);
        time.start();
    }

    @FXML
    private void openRezultate() {
        Stage newStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("prikazRezultata.fxml"));
            Parent root = loader.load();
            newStage.setTitle("Prikaz rezultata");
            newStage.setScene(new Scene(root));
            newStage.setResizable(false);
            newStage.show();
        } catch (Exception e) {
            Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }

    }

//    static Image image;
    @FXML
    public void initialize() {

        pomPrikaziBonus.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                prikaziBonus.setText(pomPrikaziBonus.getText());
            }
        });

        pomImeIgraca.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                imeIgraca.setText(pomImeIgraca.getText());
            }
        });

        pomLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                opisKarte.setText(pomLabel.getText());
            }
        });

        broj.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                gridPolja.getChildren().clear();

                for (int i = 0; i < Integer.parseInt(broj.getText()); i++) {
                    gridPolja.add(new javafx.scene.shape.Rectangle(35, 35), i,0);
                }
            }
        });

        pomImage.imageProperty().addListener(new ChangeListener<Image>() {
            @Override
            public void changed(ObservableValue<? extends Image> observableValue, Image image, Image t1) {
                slika.setImage(pomImage.getImage());
            }
        });

        numberOfPlayedGame.setText(String.valueOf(FindFiles.numberOfFiles()));

        //nasumicno igranje igraca
        Collections.shuffle(players);

        //kreiraj mapu
        mapa = new Mapa(sizeOfField, sizeOfField, centarPane);


        //prikazi imena
        Label label1;
        int counter = 0;
        for (Igrac player:players) {
            label1 = new Label();
            label1.setText(player.getNameOfIgrac());
            label1.setTextFill(player.getFigure().get(0).getColor());
            listOfNames.add(label1, counter, 0);
            counter++;
        }

        //prikazi listu figura
        Label label2;
        int counter1 = 0;
        for(Igrac player : players) {
            List<Figura> f = player.getFigure();
            for (Figura figura : f) {
                label2 = new Label();
                label2.setTextFill(f.get(0).getColor());
                label2.setText(player.getNameOfIgrac() + " - " + figura.getKretanje().toString());
                label2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        List<Point2D> predjeno = figura.getPredjenPut();

                        Stage newStage = new Stage();

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("predjeniPut.fxml"));
                            PredjeniPutController.setIme(String.valueOf(figura.getKretanje()));
                            PredjeniPutController.setUkVrijeme(figura.getUkupnoVrijemeKretanja());
                            PredjeniPutController.setPredjeniPut(predjeno);
                            Parent root = loader.load();
                            newStage.setTitle("Predjeni put");
                            newStage.setScene(new Scene(root));
                            newStage.setResizable(false);
                            newStage.show();
                        } catch (Exception e) {
                            Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                        }
                    }
                });
                listaFigura.add(label2, 0, counter1);
                counter1++;
            }
        }
    }
}
