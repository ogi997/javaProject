package sample.figura;


import javafx.scene.paint.Color;

public class LedbecaFigura extends Figura {

    public LedbecaFigura(Color color, nacinKretanja kretanje, String name) {
        super(color, kretanje, name);
    }


    @Override
    public String toString() {
        return super.toString() + " ledbeca figura";
    }

}
