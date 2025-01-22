package Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class manages the tiles of the map and map generation
 */
public class Map {

    Crd size;
    int tileSize;
    public double[][] tiles;

    int seed = (int) (Math.random()*1000000);
    Random random = new Random(seed);
    RndCrd rc = new RndCrd(random, 10, 10);

    public boolean updateFlag = true;

    public Map(Crd mapsize) {
        size = mapsize;
        tiles = new double[size.x()][size.y()];
    }

    public Map(Crd mapsize, int Seed) {
        seed = Seed;
        random = new Random(seed);
        rc = new RndCrd(random, 10, 10);
    }

    public void test() {
        Map m = new Map(size);
        m.initTiles(3.5);
        m.addTilingNoise(0.045, 5);
        m.addTilingNoise(1.111, 2);
        m.addTilingNoise(0.111, 5);
        m.addTilingNoise(0.063, 2);
        m.addTilingNoise(0.036, 2);
        //addNoise(100, 4);
        //addNoise(40, 1);
        Map m2 = new Map(size);
        Map m3 = new Map(size);
        Map m4 = new Map(size);

        m2.initTiles(0.6);
        m2.addTilingNoise(0.111,0.4, 0.5);
        //m2.tableTiles(0.8,1, 0.03, 0.05);

        m3.initTiles(0.5);
        m3.addTilingNoise(0.111,0.5, 0.5);
        m3.setValuesOut(0.5, 0.15, 0);
        m3.setValuesIn(0.5, 0.15, 1);
        //m3.tableTiles(0.2,0.8, 0.08, 0.05);
        //m3.scaleTiles(8);

        m4.initTiles(0.8);
        m4.addTilingNoise(0.018, 0.2, 0.5);

        //m3.floor(0,0);
        m2.mult(m3);
        m2.convolution(8);
        m4.convolution(3);
        m2.mult(m4);

        m4.initTiles(0.5);
        m4.addTilingNoise(0.194, 0.5, 0.5);
        m4.setValuesOut(0.8, 0.2, 0);
        m4.convolution(15);

        m2.mult(m4);
        m2.setValuesIn(0.85, 0.15, 0.7);
        m2.incrementTiles(1);
        m.mult(m2);
        m.convolution(3);

        //m4.layeredTiling(3, 1, 5, 0.5, 0.5, 0.5);
        //m.customConvolution(10, new double[]{-1,0.5,1,2,3.5,5,5.5,6,7,8,9,10,11,12,13,14});
        //addTilingNoise(10,2);
        //addTilingNoise(5,1.5);
        //convolution(8);
        //tileBasedConvo(new int[]{12,10,8,6,10,4,4,2,2,20,20,5,2,2,2,2,2,2,2,2,2});
        //crimp( size.x/6, 0.5, 100, 100);
        //tileBasedConvo(new int[]{12,10,8,6,5,4,4,2,2,20,20,5,2,2,2,2,2,2,2,2,2});


        initTiles(5);
        layeredTiling(7, 0.7, 3.5, 0.5, 0.5, 0.7);
        convolution(3);
        initTiles(0);
        add(m3);
        //setValuesIn(0.25, 0.25, 0);
    }

    public double calcMean() {
        double d = 0;
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                d += tiles[x][y];
            }
        }
        return d/(size.x()*size.y());
    }

    public double calcVariance() {
        double d = 0;
        double m = calcMean();
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                d += (tiles[x][y] - m)*(tiles[x][y] - m);
            }
        }
        return d/(size.x()*size.y());
    }

    public Crd calcRange() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                min = Math.min(min, tiles[x][y]);
                max = Math.max(max, tiles[x][y]);
            }
        }
        return new Crd(min, max);
    }

    public void calcDerivX() {
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                tiles[x][y] = tiles[(x + 1)%size.x()][y] - tiles[x][y];
            }
        }
    }

    // Method designed to test wavelength calibration factor for tiling noise
    // returns the range of a number of tests
    // tests each map to see how much the range is restricted by
    // a restiction to 1/2 of the true range should be the 'unit wavelength'
    public static void testWavelength(int trials, Crd size, double wavelength) {
        Map m = new Map(size);
        double successes = 0;
        for (int t = 0; t < trials; t++) {
            m.initTiles(0.5);
            m.addTilingNoise(wavelength, 0.5);
            Crd range = m.calcRange();
            System.out.println("Trial " + t +": range is " + range + "-> varies " + (range.y - range.x));
            successes += (range.y - range.x < 0.5) ? 1 : 0;
        }

        double successRate = successes/trials*100;
        System.out.println("Percentage of tries with restriction to 1/2 range: " + successRate + "%");
    }

    public void initTiles(double tile) {
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                tiles[x][y] = tile;
            }
        }
    }

    public void incrementTiles(double incr) {
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                tiles[x][y] += incr;
            }
        }
    }

    public void setValuesIn(double target, double tolerance, double set) {
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                if (tiles[x][y] >= target - tolerance && tiles[x][y] <= target + tolerance) {
                    tiles[x][y] = set;
                }
            }
        }
    }

    public void setValuesOut(double target, double tolerance, double set) {
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                if (tiles[x][y] <= target - tolerance || tiles[x][y] >= target + tolerance) {
                    tiles[x][y] = set;
                }
            }
        }
    }

    public void scaleTiles(double scale) {
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                tiles[x][y] *= scale;
            }
        }
    }

    public void tableTiles(double min, double max, double e, double i) {
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                tiles[x][y] = tableFunc(tiles[x][y], min, max, e, i);
            }
        }
    }

    public void addNoise(double wavelength, double amplitude) {
        Crd ofst = rc.next();

        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                tiles[x][y] += ((SimplexNoise.noise(ofst.x + x/wavelength, ofst.y + y/wavelength)) * amplitude);
            }
        }
    }

    public void addTilingNoise(double wavelength, double amplitude) {
        addTilingNoise(wavelength, amplitude, 0.5);
    }

    // Calibrated so that a wavelength  of 1 corresponds to 50% of maps
    // having a range of the upper or lower half of the possible range
    // ** wavelength adjusts to the size of the map; a wavelength of 0.5
    // should experience both a crest and a trough
    public void addTilingNoise(double wavelength, double amplitude, double power) {
        Crd ofst1 = rc.next();
        Crd ofst2 = rc.next();

        wavelength *= 9; // Calibration factor

        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                Crd xCrd = Crd.polar(1.0/wavelength, 2*Math.PI*x/size.x()).add(ofst1);
                Crd yCrd = Crd.polar(1.0/wavelength, 2*Math.PI*y/size.y()).add(ofst2);
                double noise = SimplexNoise.noise(xCrd.x, xCrd.y, yCrd.x, yCrd.y);
                tiles[x][y] += (Math.signum(noise) * Math.pow(Math.abs(noise), power) * amplitude);
            }
        }
    }

    public void addAltNoise(double wavelength, double amplitude) {
        Crd ofst = rc.next();

        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                tiles[pmod( x + size.x()/2, size.x())][pmod( y + size.y()/2, size.y())] += ((SimplexNoise.noise(ofst.x + x/wavelength, ofst.y + y/wavelength)) * amplitude);
            }
        }
    }

    /*
     *  Adds fractal layers of noise to the map
     *  wavelengths start at startWavelength and change by a factor of the contraction
     *  Amplitudes start at startAmplitude and change by a factor of the persistence
     *  **both should be less than 1 to decay**
     *  power affects the shape of the waves made by the noise, since the noise is naturally warped
     */
    public void layeredTiling(int layers, double startWavelength, double startAmplitude, double pow, double contraction, double persistence) {
        for (int i = 0; i < layers; i++) {
            addTilingNoise(startWavelength * Math.pow(contraction, i), startAmplitude * Math.pow(persistence, i), pow);
        }
    }

    public void crimp(double erosion, double integrity, double variability, double wavelength) {
        Crd ofst1 = rc.next();
        Crd ofst2 = rc.next();

        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                Crd p = new Crd(x,y).add(
                        variability * SimplexNoise.noise(ofst1.x + x/wavelength, ofst1.y + y/wavelength),
                        variability * SimplexNoise.noise(ofst2.x + x/wavelength, ofst2.y + y/wavelength)
                        );
                tiles[x][y] *= tableFunc(p.x, 0, size.x, erosion, integrity) * tableFunc(p.y, 0, size.y, erosion, integrity);
            }
        }
    }

    public void floor(double floorHeight, double floorSet) {
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                tiles[x][y] = tiles[x][y] < floorHeight ? floorSet : tiles[x][y];
            }
        }
    }

    public void invFloor(double height, double set) {
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                tiles[x][y] = tiles[x][y] < height ? set : tiles[x][y];
            }
        }
    }

    public void convolution(int dim) {
        double[][] copy = new double[size.x()][size.y()];
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                copy[x][y] = tiles[x][y];
            }
        }
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                double sum = 0;
                int count = 0;
                for (int dx = -dim; dx <= dim; dx++) {
                    for (int dy = -dim; dy <= dim; dy++) {
                        sum += copy[pmod(x+dx, size.x())][pmod(y+dy, size.y())];
                        count++;
                    }
                }
                tiles[x][y] = sum/count;
            }
        }
    }

    public void customConvolution(int dim, double[] gates) {
        double[][] copy = new double[size.x()][size.y()];
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                copy[x][y] = tiles[x][y];
            }
        }
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                double sum = 0;
                int count = 0;
                for (int dx = -dim; dx <= dim; dx++) {
                    for (int dy = -dim; dy <= dim; dy++) {
                        sum += copy[pmod(x+dx, size.x())][pmod(y+dy, size.y())];
                        count++;
                    }
                }
                tiles[x][y] = getGate(sum/count, gates);
            }
        }
    }

    public int getGate(double d, double[] gates) {
        int ret = 0;
        while (ret < gates.length && d > gates[ret]) ret++;
        return ret;
    }

    public void tileBasedConvo(int[] specs) {
        double[][] copy = tiles.clone();
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                double sum = 0;
                int count = 0;
                int dim = specs[(int) Math.max(tiles[x][y], 0)];
                for (int dx = -dim; dx <= dim; dx++) {
                    for (int dy = -dim; dy <= dim; dy++) {
                        sum += copy[pmod(x+dx, size.x())][pmod(y+dy, size.y())];
                        count++;
                    }
                }
                tiles[x][y] = sum/count;
            }
        }
    }

    public void add(Map m) {
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                tiles[x][y] = tiles[x][y] + m.tiles[x][y];
            }
        }
    }

    public void mult(Map m) {
        Map r = new Map(size);
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                tiles[x][y] = tiles[x][y] * m.tiles[x][y];
            }
        }
    }

    public static int pmod(int a, int mod) {return ((a % mod) + mod) % mod;}

    // x - input
    // e - erosion
    // i - integrity
    // a - base beginning
    // b - base ending
    public static double tableFunc(double x, double a, double b, double e, double i) {
        double s = i / e; // scale
        double func = (  Math.tanh( s * (x - a) - i)
                + Math.tanh( s * (b - x) - i)
                - Math.tanh(-s * a - i)
                - Math.tanh( s * b - i))/ (
                  Math.tanh(-s * a + i)
                + Math.tanh( s * b + i)
                - Math.tanh(-s * a - i)
                - Math.tanh( s * b - i)
                );
        return Math.min( 1, Math.max(func, 0));
    }

}
