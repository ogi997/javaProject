package sample.karta;


public abstract class Karta {

//    private Image image;
    String pathToImage;
    private String opis;


    public Karta(String pathToImage, String opis) {
        this.pathToImage = pathToImage;
        this.opis = opis;
    }

    public String getImage() {
        return pathToImage;
    }

    public void setImage(String image) {
        this.pathToImage = image;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @Override
    public String toString() {
        return "Karta{" +
                "opis='" + opis + '\'' +
                '}';
    }
}
