package sample.igrac;


import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import sample.GlavniProzorController;
import sample.Simulacija;
import sample.figura.Figura;
import sample.figura.SuperBrzaFigura;
import sample.karta.*;
import sample.mapa.Mapa;
import sample.util.Bonus;
import sample.util.KrajIgre;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class Igrac extends Thread {

    private String nameOfIgrac;
    private List<Figura> figure;

    private final Gledalac gledalac;

    static public final AtomicInteger indexToPrint = new AtomicInteger(0);
    static private int threadNumber = 0;
    final private int index;

    public int trenutnaPozicija = 0;
    public int sljedecaPozicija = 0;

    public Boolean pauza = false;

    public boolean ostaoBezFigura;


    public Point2D lastPoint;
    public Igrac(String name, List<Figura> figure) {
        super();
        this.nameOfIgrac = name;
        this.figure = figure;

        index = threadNumber++;

        this.ostaoBezFigura = false;

        lastPoint = null;

        IzvlacenjeKarte izvuviKartu = new IzvlacenjeKarte();
        Komentator inf = new Komentator(izvuviKartu);
        gledalac = new Gledalac(izvuviKartu);

        inf.start();

    }

    private int nextIndex() {
        return (index + 1) % threadNumber;
    }

    public String getNameOfIgrac() {
        return this.nameOfIgrac;
    }

    public void setNameOfIgrac(String name) {
        this.nameOfIgrac = name;
    }

    public List<Figura> getFigure() {
        return figure;
    }

    public void setFigure(List<Figura> figure) {
        this.figure = figure;
    }

    private void deleteBonus(Point2D point, Figura figura) {
        int x = (int)point.getX();
        int y = (int)point.getY();

        Platform.runLater(() -> {
            Mapa.deleteBonus(x, y, figura);
        });
    }

    private void obradiBonus(Figura figura, Point2D point) {
        Thread obradaBon = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                int x = (int)point.getX();
                int y = (int)point.getY();
                if (Mapa.isAlreadyBonus(x, y)) {
                    Bonus bonus = (Bonus) Mapa.getBonus(x, y);
                    figura.setBonusKretanje(bonus.getVrijednost());

                    deleteBonus(point, figura);

                }

            }
        });
        obradaBon.start();
    }



    @Override
    public String toString() {
        return "Igrac{" +
                "name='" + getNameOfIgrac() + '\'' +
                '}';
    }

    private static final List<Igrac> ostaloBezFigura = new ArrayList<>();

    final List<Point2D>[] rupePoints = new List[]{new ArrayList<>()};

    @Override
    public synchronized void run() {

        //sve dostupne figure za igraca
        List<Figura> fi = getFigure();

        Figura figura = fi.get(0);
        figura.setAktivna(true);//postavi prvu figuru da je aktivna
        int indexOfFigura = 0;
        while(true) {

            synchronized (indexToPrint) {
                while (indexToPrint.get() != index) {

                    try {
                        indexToPrint.wait();

                    } catch (InterruptedException e) {
                        Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                    }

                }


                //logika kretanja
                if(rupePoints[0].size() > 0)
                    rupePoints[0].clear();

                if(indexOfFigura >= 4) {
//                    System.out.println(this + " je ostao bez figura");
                    if(!ostaloBezFigura.contains(this)) {
//                        System.out.println("Sacuvaj igraca: " + this);
                        ostaloBezFigura.add(this);
                    }

                    if (KrajIgre.numberOfPlayers == KrajIgre.howMuchPlayerLeftFigura) {
                        Platform.runLater(() -> {
                            GlavniProzorController.krajIgre(Alert.AlertType.INFORMATION, "Igra je zavrsena!", ostaloBezFigura);
                        });
                        return;
                    }

                    ostaoBezFigura = true;
                    indexToPrint.set(nextIndex());
                    indexToPrint.notifyAll();
                } else {
                    if(!figura.isAktivna()) {
                        indexOfFigura++;
                        if(indexOfFigura >= 4) {
                            if(!ostaloBezFigura.contains(this)) {
                                ostaloBezFigura.add(this);
                            }
                            ostaoBezFigura = true;

                            if (KrajIgre.numberOfPlayers == KrajIgre.howMuchPlayerLeftFigura) {
                                Platform.runLater(() -> {
                                    GlavniProzorController.krajIgre(Alert.AlertType.INFORMATION, "Igra je zavrsena!", ostaloBezFigura);
                                });
                                return;
                            }


                            indexToPrint.set(nextIndex());
                            indexToPrint.notifyAll();
                        }

                        if(ostaoBezFigura)
                            KrajIgre.howMuchPlayerLeftFigura++;

                        if (!ostaoBezFigura) {
                            trenutnaPozicija = sljedecaPozicija = 0;
                            figura = fi.get(indexOfFigura);
                            figura.setAktivna(true);
                        }
                    }
                }

                if (!ostaoBezFigura) {

                    if(pauza) {
                        try {
                            indexToPrint.wait();
                        } catch (InterruptedException e) {
                            Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                        }
                    }

                    //move
                    Karta karta = gledalac.getIzvucenaKarta(this, figura);

                    if (karta instanceof ObicnaKarta) {
                        Point2D newPoint = lastPoint;
                        if (!Mapa.isLastPolje(newPoint)) {
                            Platform.runLater(() -> {
                                Mapa.removeFigura(newPoint);
                            });
                        }
                    }

                    int dovde;

                    if (karta instanceof ObicnaKarta) {
//
                        if (figura instanceof SuperBrzaFigura) {
                            dovde = figura.getBonusKretanje() + ((ObicnaKarta) karta).getBrojPolja() * 2;
                            figura.setBonusKretanje(0);
                        } else {
                            dovde = figura.getBonusKretanje() + ((ObicnaKarta) karta).getBrojPolja();
                            figura.setBonusKretanje(0);
                        }
                        Point2D point = null;
                        for (int i = 0; i < dovde; i++) {
                            if(pauza) {
                                try {
                                    indexToPrint.wait();
                                } catch (InterruptedException e) {
                                    Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                                }
                            }

                            //tacka gdje se nalazimo
                            point = Mapa.positionOfPlayField.get(trenutnaPozicija + i);

                            //sacuvaj predjeni put za svaku figuru
                            figura.setOnePredjenPut(point);

                            //obradi bonus
                            obradiBonus(figura, point);

                            //koordinate na kojima treba figuru obrisati
                            final Point2D[] pointToDelete = new Point2D[1];

                            //nacrtaj figuru
                            if (!Mapa.isPlayer((int) point.getX(), (int) point.getY())) {

                                Figura finalFigura = figura;
                                Point2D finalPoint1 = point;
                                Platform.runLater(() -> {
                                    pointToDelete[0] = Mapa.drawFigura(finalFigura, finalPoint1);
                                });

                            }

                            //provjeri ako ima vec figura jedna na tom mjestu
                            if (i == dovde - 1) {

                                int x = (int) point.getX();
                                int y = (int) point.getY();
                                Object object = Mapa.getPositionOfPlatField(x, y);
                                if (object instanceof Figura) {
                                    dovde++;
                                }
                                lastPoint = point;
                            }

//                      odmor kod igraca
                            try {
                                figura.setUkupnoVrijemeKretanja(figura.getVrijemeKretanja());
                                Thread.sleep(figura.getVrijemeKretanja());
                            } catch (InterruptedException e) {
                                Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                            }

                            //provjera da li je neki igrac dosao do kraja mape
                            if (trenutnaPozicija + i + 1 == Mapa.positionOfPlayField.size()) {
                                dovde = 0; //nema dalje
//                            figura je dosla do kraja mape
                                figura.setAktivna(false); //postavimo figuru da vise nije aktivna

                                //optimizacija
                                if (indexOfFigura == 3) {
                                    ostaoBezFigura = true;
                                }
                                lastPoint = null;

                                indexToPrint.set(nextIndex());
                                indexToPrint.notifyAll();
                            }

                            if(pauza) {
                                try {
                                    indexToPrint.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            //brisi sve do zadnjeg
                            if (i != dovde - 1) {
                                Point2D finalPoint = pointToDelete[0];
                                Platform.runLater(() -> {
                                    Mapa.removeFigura(finalPoint);
                                });

                            }

                        } //kraj for-loop

                        sljedecaPozicija = dovde;
                        trenutnaPozicija += sljedecaPozicija;
                        lastPoint = point;
                    } else {

                        Platform.runLater(() -> {
                            rupePoints[0] = Mapa.crtajRupe();
                        });

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                        }

                        Platform.runLater(() -> {
                            Mapa.deleteRupe(rupePoints);
                        });

                        for (int i = 0; i < rupePoints[0].size(); i++) {
                            Point2D p = rupePoints[0].get(i);
                            if (Mapa.isLedbecaFigura((int)p.getX(), (int)p.getY())) {
                                Platform.runLater(() -> {
                                    Object obj = Mapa.getPositionOfPlatField((int)p.getX(), (int)p.getY());
                                    Mapa.removeFigura(p);
                                    Mapa.setToPlayFieldFiguru((int)p.getX(), (int)p.getY(), obj);
                                });
                            }
                        }
                    }
                }

                //novi krug
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                }

                indexToPrint.set(nextIndex());
                indexToPrint.notifyAll();

            }

        }

    }

}
