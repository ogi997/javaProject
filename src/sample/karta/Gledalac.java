package sample.karta;

import sample.GlavniProzorController;
import sample.figura.Figura;
import sample.igrac.Igrac;

public class Gledalac {
    static IzvlacenjeKarte informacije;

    public Gledalac(IzvlacenjeKarte informacije){
        Gledalac.informacije =informacije;
    }

    public synchronized Karta getIzvucenaKarta(Igrac koIzvlaciKartu, Figura figura) {

        Karta karta;
        karta = informacije.izvuciKartu();
        GlavniProzorController.showKarta(karta, koIzvlaciKartu, figura);

        return karta;
    }

}
