package sample.karta;

import sample.Simulacija;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;

public class IzvlacenjeKarte {

    public SynchronousQueue<Karta> izvucenaKarta = new SynchronousQueue<Karta>();

    Spil s = new Spil();
//    static Spil s = Test.getInstance();
    public void dodajKartu(){

        //pravimo generator slucajnih brojeva
        Random generator = new Random();

        izvucenaKarta.offer(s.getOneKarta(generator.nextInt(51 - 1 + 1) + 1));
    }

    public Karta izvuciKartu(){
        Karta karta = null;
        try{
            karta = izvucenaKarta.take();
        }catch(InterruptedException exc){
            Simulacija.logger.log(Level.SEVERE, exc.fillInStackTrace().toString());
        }
        return karta;
    }
}
