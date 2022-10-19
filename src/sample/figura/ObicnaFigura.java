package sample.figura;


import javafx.scene.paint.Color;

public class ObicnaFigura extends Figura {

    public ObicnaFigura(Color color, nacinKretanja kretanje, String name) {
        super(color, kretanje, name);
    }


    @Override
    public String toString() {
        return super.toString() + " obicna figura";
    }

}
