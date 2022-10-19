package sample.karta;

public class ObicnaKarta extends Karta {

    private Integer brojPolja;

    public ObicnaKarta(String image, String opis, Integer brojPolja) {
        super(image, opis);
        this.brojPolja = brojPolja;
    }

    public Integer getBrojPolja() {
        return brojPolja;
    }

    public void setBrojPolja(Integer brojPolja) {
        this.brojPolja = brojPolja;
    }

    @Override
    public String toString() {
        return super.toString() + " obicna karta, igrac moze ici: " + this.getBrojPolja() + " polja naprijed";
    }
}
