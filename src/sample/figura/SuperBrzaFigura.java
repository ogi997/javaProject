package sample.figura;


import javafx.scene.paint.Color;

public class SuperBrzaFigura extends Figura {

    public SuperBrzaFigura(Color color, nacinKretanja kretanje, String name) {
        super(color, kretanje, name);
    }


    @Override
    public String toString() {
        return super.toString() + " super brza figura";
    }

}
