package sample.mapa;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.Simulacija;
import sample.figura.Figura;
import sample.figura.LedbecaFigura;
import sample.figura.ObicnaFigura;
import sample.figura.SuperBrzaFigura;
import sample.util.Bonus;
import sample.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class Mapa {

    private static int WIDTH;

    private final int HEIGHT;

    private static Pane pane;

    private static GridPane gridPane;

    private static Object[][] playfield;
    public static Object[][] matrix;

    public static int numberOfRupa = 0;

    public static List<Point2D> positionOfPlayField;

    public Mapa(int width, int height, Pane pane) {
        WIDTH = width;
        this.HEIGHT = height;
        Mapa.pane = pane;
        playfield = new Object[WIDTH][HEIGHT];
        matrix = new Object[WIDTH][HEIGHT];

        int count = 1;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < this.HEIGHT; j++) {
                matrix[j][i] = count;
                count++;
                playfield[j][i] = new StackPane();
            }
        }

        if (WIDTH == 8) {
            positionOfPlayField = Util.spiralListSeven();
        } else if (WIDTH == 10) {
            positionOfPlayField = Util.spiralListTen();
        } else {
            positionOfPlayField = Util.spiralDiamondView(matrix, 0, 0, WIDTH - 1, this.HEIGHT - 1, (WIDTH * this.HEIGHT) - ((this.HEIGHT + 1) / 2) * 4);
        }

        StackPane stackPane;
        Label label;
        gridPane = new GridPane();
        int number = 1;
        for (int i = 0; i < WIDTH; i++){
            for(int j = 0; j < HEIGHT; j++){
                stackPane = new StackPane();
                stackPane.setMinSize(35, 35);
                stackPane.setBackground((new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));
                stackPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.2))));
                label = new Label();
                label.setText(String.valueOf(number));
                if(positionOfPlayField.contains(new Point2D(i, j))){
                    stackPane.setBackground(new Background(new BackgroundFill(Color.web("#C0C0C0"), CornerRadii.EMPTY, Insets.EMPTY)));
                }

                stackPane.getChildren().add(label);
                gridPane.add(stackPane, j, i);
                playfield[j][i] = stackPane;
                number++;
            }
        }

        pane.getChildren().add(gridPane);

    }

    private static Lighting setColorForImage(Color color) {
        Lighting lighting = new Lighting(new Light.Distant(45, 90, color));
        ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
        lighting.setContentInput(bright);
        lighting.setSurfaceScale(0.0);

        return lighting;
    }

    public static Point2D drawFigura(Figura figura, Point2D point) {

        StackPane stackPane = new StackPane();

        //odredimo kako figura izgleda
        if(figura instanceof ObicnaFigura) {
            try {
                ImageView iv = new ImageView(new Image(new FileInputStream(new File("slike" + File.separator + "obicnaFigura.png").getAbsoluteFile())));
                iv.setEffect(setColorForImage(figura.getColor()));
                stackPane.getChildren().add(iv);
            } catch (FileNotFoundException e) {
                Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            }
        } else if (figura instanceof SuperBrzaFigura) {
            try {
                ImageView iv = new ImageView(new Image(new FileInputStream(new File("slike" + File.separator + "superBrzaFigura.png").getAbsoluteFile())));
                iv.setEffect(setColorForImage(figura.getColor()));
                stackPane.getChildren().add(iv);
            } catch (FileNotFoundException e) {
                Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            }
        } else {
            try {
                ImageView iv = new ImageView(new Image(new FileInputStream(new File("slike" + File.separator + "ledbecaFigura.png").getAbsoluteFile())));
                iv.setEffect(setColorForImage(figura.getColor()));
                stackPane.getChildren().add(iv);
            } catch (FileNotFoundException e) {
                Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            }
        }

        //koordinate gdje se crta figura
        int x = (int)point.getX();
        int y = (int)point.getY();

        //dodajmo prikaz na mapu
        gridPane.add(stackPane, y, x);

        //oznacimo da se tu nalazi figura
        playfield[y][x] = figura;

        //prikazimo mapu korisniku
        pane.getChildren().clear();
        pane.getChildren().add(gridPane);

        return point;
    }

    public static void removeFigura(Point2D point) {

        if (point == null) {
            return;
        }

        int x = (int)point.getX();
        int y = (int)point.getY();

        StackPane stackPane = null;
        ObservableList<Node> ol = gridPane.getChildren();
        for (Node node : ol) {
            if ( node instanceof StackPane
                    && (GridPane.getColumnIndex(node) == y)
                    && (GridPane.getRowIndex(node) == x) ) {
                stackPane = (StackPane) node;
            }
        }
        StackPane stackPane1;
        Label label;
        if( stackPane != null) {
            ObservableList<Node> oln = stackPane.getChildren();
//            System.out.println("Velicina: " + oln.size());
            if (oln.size() > 0){
                stackPane.getChildren().remove(oln.size() - 1);
                playfield[y][x] = new StackPane(stackPane);
            }
//            System.out.println("VElicina2: " + oln.size());
            if(oln.size() == 0) {
//                System.out.println("Test");
                stackPane1 = new StackPane();
                stackPane1.setMinSize(35, 35);
                stackPane1.setBackground((new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));
                stackPane1.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.2))));
                label = new Label();
                label.setText(String.valueOf(Mapa.getNumberPosition(x, y)));

                gridPane.add(stackPane1, y, x);

                playfield[y][x] = new StackPane(stackPane1);

            }
        } else {
            System.out.println("Nema");
        }

//        if(oln.size() == 0)
//        playfield[y][x] = new StackPane(stackPane);

        pane.getChildren().clear();
        pane.getChildren().add(gridPane);

//        System.out.println("=============================");
    }

    public static Point2D getRandomCoordinate() {
        int randomNumber;
        do {
            randomNumber = (int) (Math.random() * positionOfPlayField.size());

        } while(randomNumber == ( positionOfPlayField.size() - 1 ) );
        return positionOfPlayField.get(randomNumber);
    }

    public static void drawBonus(int x, int y) {

        Random random = new Random();
        int numberOfBonus = 2 + random.nextInt(5);
        Bonus bonus = new Bonus(numberOfBonus);
        playfield[y][x] = bonus;

    }

    public static void deleteBonus(int x, int y, Figura figura) {


        playfield[y][x] = isPlayer(x, y) ? figura : new StackPane();

    }

    public static List<Point2D> crtajRupe() {

//        int n = 24; //broj rupa koje ce se prikazati na mapi
        int n = numberOfRupa;
        List<Point2D> rupePoints = new ArrayList<>();
        int x, y;
        while(n > 0) {

            Point2D point2D = Mapa.getRandomCoordinate();
            x = (int)point2D.getX();
            y = (int)point2D.getY();
            Point2D newPoint = new Point2D(x, y);
            if(!rupePoints.contains(newPoint) ) {
                rupePoints.add(newPoint);
                n--;
            }
        }
        StackPane stackPane;
        for(Point2D p : rupePoints) {
            int x1 = (int)p.getX();
            int y1 = (int)p.getY();

            stackPane = new StackPane();
            Rectangle r = new Rectangle(35d, 35d);
            r.setFill(Color.BLACK);
            stackPane.getChildren().add(r);

            Object current = Mapa.getPositionOfPlatField(x1, y1);
            if( current instanceof ObicnaFigura) {
                Mapa.removeFigura(p);
                ((Figura) current).setAktivna(false);
                Mapa.setToPlayFieldFiguru(x1, y1, new StackPane(stackPane));
            }

            if( current instanceof SuperBrzaFigura) {
                Mapa.removeFigura(p);
                ((Figura) current).setAktivna(false);
                Mapa.setToPlayFieldFiguru(x1, y1, new StackPane(stackPane));
            }

            if (current instanceof LedbecaFigura) {
                Mapa.setToPlayFieldFiguru(x1, y1, current);
            }

            gridPane.add(stackPane, y1, x1);

            pane.getChildren().clear();
            pane.getChildren().add(gridPane);
        }
        return rupePoints;
    }

    public static void deleteRupe(List<Point2D>[] rupePoints) {

        StackPane stackPane = null;
        for(List<Point2D> p : rupePoints) {
            for (int i = 0; i < p.size(); i++) {
                int x1 = (int)p.get(i).getX();
                int y1 = (int)p.get(i).getY();

                ObservableList<Node> ol = gridPane.getChildren();
                for (Node node : ol) {
                    if ( node instanceof StackPane
                            && (GridPane.getColumnIndex(node) == y1)
                            && (GridPane.getRowIndex(node) == x1) ) {
                        stackPane = (StackPane) node;

                    }

                }

                if( stackPane != null) {
                    ObservableList<Node> oln = stackPane.getChildren();

                    stackPane.getChildren().remove(oln.size() - 1);

                } else {
                    System.out.println("Nema");
                }

                gridPane.add(new StackPane(stackPane), y1, x1);

                pane.getChildren().clear();
                pane.getChildren().add(gridPane);

            }

        }


    }

    public Object[][] getMapa() {
        return playfield;
    }

    public void setMapa(Object[][] mapa) {
        playfield = mapa;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public static boolean isExist(int x, int y) {
        return positionOfPlayField.contains(new Point2D(y, x));
    }

    public static boolean isAlreadyBonus(int x, int y) {
        return playfield[y][x] instanceof Bonus;
    }

    public static boolean isPlayer(int x, int y) {
        return playfield[y][x] instanceof Figura;
    }

    public static boolean isLedbecaFigura(int x, int y) {
        return playfield[y][x] instanceof LedbecaFigura;
    }

    public static void setToPlayFieldFiguru(int x, int y, Object figura) {
        playfield[y][x] = figura;
    }

    public static Object getPositionOfPlatField(int x, int y) {
        return playfield[y][x];
    }

    public static Object getBonus(int x, int y) {
        return playfield[y][x];
    }

    public static Object getNumberPosition(int x, int y) {
        return matrix[y][x];
    }

    public static boolean isLastPolje(Point2D point) {
        Point2D checkPoint = positionOfPlayField.get(positionOfPlayField.size() - 1);
        return checkPoint.equals(point);
    }
}
