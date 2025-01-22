package Map;

import java.util.Random;

public class RndCrd extends Crd {
    public Random r;
    public double X; // the max X for each random coord
    public double Y; // the max Y for each random coord

    public RndCrd(Random R, double maxX, double maxY) {
        r = R;
        X = maxX;
        Y = maxY;
    }

    public Crd next() {
        nextRandom(r, X, Y);
        return new Crd(x,y);
    }
}
