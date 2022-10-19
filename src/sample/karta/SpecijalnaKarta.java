package sample.karta;

public class SpecijalnaKarta extends Karta {

    public SpecijalnaKarta(String image, String opis) {
        super(image, opis);
    }

    @Override
    public String toString() {
        return super.toString() + " specijalna karta, kreira se n crnih rupa ";
    }
}
