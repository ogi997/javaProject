package sample.figura;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class Figura {

    private String namee;
    private Color color;
    private nacinKretanja kretanje;
    private int bonusKretanje;

    private final int vrijemeKretanja = 1000; //1 sekunda
    private int ukupnoVrijemeKretanja = 0;

    public Boolean aktivna = false;
    List<Point2D> predjenPut;


    public Figura(Color color, nacinKretanja kretanje, String name) {
        this.color = color;
        this.kretanje = kretanje;

        this.namee = name;
        this.bonusKretanje = 0;
        this.predjenPut = new ArrayList<>();
    }
    public int getVrijemeKretanja() {
        return this.vrijemeKretanja;
    }

    public void setUkupnoVrijemeKretanja(int vrijemeKretanja) {
        this.ukupnoVrijemeKretanja += vrijemeKretanja;
    }

    public int getUkupnoVrijemeKretanja() {
        return this.ukupnoVrijemeKretanja;
    }

    public void setOnePredjenPut(Point2D point) {
        this.predjenPut.add(point);
    }

    public List<Point2D> getPredjenPut() {
        return this.predjenPut;
    }

    public int getBonusKretanje() {
        return this.bonusKretanje;
    }

    public void setBonusKretanje(int bonusKretanje) {
        this.bonusKretanje += bonusKretanje;
    }

    public String getNamee() {
        return namee;
    }

    public void setNamee(String name) {
        this.namee = name;
    }

    public void setKretanje(nacinKretanja kretanje) {
        this.kretanje = kretanje;
    }

    public nacinKretanja getKretanje() {
        return this.kretanje;
    }

    public void setFigura(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public enum nacinKretanja {
        OBICNO,
        LEDBECA,
        SUPERBRZO
    }

    public boolean isAktivna() {
        return aktivna;
    }

    public void setAktivna(boolean aktivna) {
        this.aktivna = aktivna;
    }

    @Override
    public String toString() {
        return "Igra: " + getNamee() + " Aktivna: " + isAktivna() + " Figura: boje: " + getColor() + " se krece: " + getKretanje();
    }

}
