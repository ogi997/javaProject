package sample.karta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Spil {

    List<Karta> spilKarata;

    public Spil() {
        spilKarata = new ArrayList<>();
        ObicnaKarta ok;
        SpecijalnaKarta sk;

        //obicna karta sa kojom se prelazi jedno polje
        for(int i = 0; i < 10; i++) {

            ok = new ObicnaKarta(new File("slike" + File.separator + "diamond1.png").getAbsolutePath(), "Obicna karta. Mozete preci 1 polje.", 1);
            spilKarata.add(ok);
        }

        //obicna karta sa kojom se prelazi dva polja
        for(int i = 0; i < 10; i++) {
            ok = new ObicnaKarta(new File("slike" + File.separator + "diamond2.png").getAbsolutePath(), "Obicna karta. Mozete preci 2 polje.", 2);
            spilKarata.add(ok);
        }

        //obicna karta sa kojom se prelazi tri polja
        for(int i = 0; i < 10; i++) {
            ok = new ObicnaKarta(new File("slike" + File.separator + "diamond3.png").getAbsolutePath(), "Obicna karta. Mozete preci 3 polje.", 3);
            spilKarata.add(ok);
        }

        //obicna karta sa kojom se prelazi cetiri polja
        for(int i = 0; i < 10; i++) {
            ok = new ObicnaKarta(new File("slike" + File.separator + "diamond4.png").getAbsolutePath(), "Obicna karta. Mozete preci 4 polje.", 4);
            spilKarata.add(ok);
        }

        for(int i = 0; i < 12; i++) {
            sk = new SpecijalnaKarta(new File("slike" + File.separator + "special.png").getAbsolutePath(), "Specijalna karta.");
            spilKarata.add(sk);
        }
    }

    public List<Karta> getSpilKarata() {
        Collections.shuffle(this.spilKarata);
        return this.spilKarata;
    }

    public void setSpilKarata(List<Karta> spilKarata) {
        this.spilKarata = spilKarata;
    }

    public Karta getOneKarta(int n) {

        Karta karta = this.spilKarata.get(n);
        this.spilKarata.remove(n);
        this.spilKarata.add(karta);

        return karta;
    }
}
