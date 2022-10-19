package sample.karta;

import sample.Simulacija;

import java.util.logging.Level;

public class Komentator extends Thread {
    IzvlacenjeKarte informacije;

    public Komentator(IzvlacenjeKarte informacije) {
        this.informacije = informacije;
    }

    @Override
    public void run() {
//        long pocetak = System.currentTimeMillis();
        while (/*System.currentTimeMillis() - pocetak <= 120000*/ true) { // dvije minute
            try {
                // generisi poruku
                informacije.dodajKartu();
                // spavaj 10 sekundi
                sleep(5);
            } catch (InterruptedException e) {
                Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            }
        }
    }
}