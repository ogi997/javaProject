package sample.util;

public class KrajIgre {

    public static int numberOfPlayers;
    public static int howMuchPlayerLeftFigura = 0;

    public static void setNumberOfPlayers(int numberOfPlayers) {
        KrajIgre.numberOfPlayers = numberOfPlayers;
    }

    public static void increase() {
        KrajIgre.howMuchPlayerLeftFigura++;
    }


}
