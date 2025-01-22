package Map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * An all-purpose class for storing useful coordinate-related functions: <b>(x,y)</b>
 * <p>Also can be used for imaginary numbers: <b>x+y<i>i</i></b></p>
 */
public class Crd {
    static double E = Math.E;
    static double PI = Math.PI;
    /**
     * <b>X</b> value of a coordinate
     * <p><b>Real</b> part of an imaginary number</p>
     */
    public double x;
    /**
     * <b>Y</b> value of a coordinate
     * <p><b>Imaginary</b> part of an imaginary number</p>
     */
    public double y;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors

    /**
     * Default constructor for coordinates:<br>
     * Instantiates the origin <b>(0,0)</b>
     * @see #Crd(double, double)
     */
    public Crd() {
        x=0;
        y=0;
    }

    /**
     * Constructor based on a <em>point</em>
     * @param p the point to be converted to the coordinate class
     */
    public Crd(Point p) {
        x=p.x;
        y=p.y;
    }

    /**
     * Cartesian Constructor: <b>(x,y)</b> or <b>x+y<i>i</i></b>
     * @param X the distance along the <b>x</b> direction
     * @param Y the distance along the <b>y</b> direction
     * @see #Crd()
     */
    public Crd(double X, double Y) {
        x=X;
        y=Y;
    }

    /**
     * Polar Constructor: <b>(r,&theta)</b>
     * <br><i><small>Note: rotated around a <b>center</b>;
     * use <b>OmniLibrary.Coord.polar</b> when the center is <b>(0,0)</b></p></small></i>
     * @see #polar
     * @param center the 'origin' around which the identifier pivots
     * @param radius the distance from the <b>center</b>
     * @param angle the angle (<i>in radians</i>) from the positive <b>x</b> direction towards the positive <b>y</b> direction
     */
    public Crd(Crd center, double radius, double angle) {
        Crd t = polar(radius, angle).add(center);
        x= t.x;
        y= t.y;
    }

    /**
     * Polar Constructor: <b>(r,&theta)</b>
     * @param radius the distance from the origin
     * @param angle the angle (<i>in radians</i>) from the positive <b>x</b> direction towards the positive <b>y</b> direction
     * @return a cartesian Coordinate
     */
    public static Crd polar(double radius, double angle) {
        return new Crd(Math.cos(angle)*radius,Math.sin(angle)*radius);
    }

    /**
     * Random Constructor:
     * <br>creates a random, positive coordinate with values less than <i>1.0</i>
     * <small><br>Based on the <b>Math.random</b> function</small>
     * @return a psuedorandom coordinate in the range of <b>[0,1)</b>x<b>[0,1)</b>
     * @see #random(double, double)
     * @see #randomNeg(double)
     */
    static public Crd random() {
        return new Crd(Math.random(),Math.random());
    }

    /**
     * Random Constructor:
     * <br>creates a random, positive coordinate with values less than the values given
     * <small><br>Based on the <b>Math.random</b> function</small>
     * @param maxX the limit of the possible <b>x</b> values
     * @param maxY the limit of the possible <b>y</b> values
     * @return a psuedorandom coordinate in the range of <b>[0,<i>maxX</i>)</b>x<b>[0,<i>maxY</i>)</b>
     * @see #random()
     * @see #randomNeg(double)
     */
    static public Crd random(double maxX, double maxY) {
        Crd r = Crd.random();
        r = r.scaleX(maxX).scaleY(maxY);
        return r;
    }


    /**
     * Random Constructor
     * <br>creates a random, positive coordinate with values less than the values given a random number generator
     * <small><br>Based on the <b>Math.random</b> function</small>
     * @param r the random number generator
     * @param maxX the limit of the possible <b>x</b> values
     * @param maxY the limit of the possible <b>y</b> values
     * @return a psuedorandom coordinate in the range of <b>[0,<i>maxX</i>)</b>x<b>[0,<i>maxY</i>)</b>
     * @see #random()
     * @see #randomNeg(double)
     */
    static public Crd randomSeed(Random r, double maxX, double maxY) {

        Crd ret = new Crd(r.nextDouble() * maxX, r.nextDouble() * maxY);
        return ret;
    }

    public void nextRandom(Random r, double maxX, double maxY) {
        Crd ret = randomSeed(r, maxX, maxY);
        x = ret.x;
        y = ret.y;
    }

    //------------------------------------------------------------------------------------------------------------------
    // State Observers

    /**
     * Determines if the given coordinate is <b>NaN</b>
     * @return a boolean: true if there are any <b>NaN</b> values
     */
    public boolean isNaN() {
        return ((Double)x).isNaN() || ((Double)y).isNaN();
    }

    /**
     * Compares two Coordinates, to see if they are equal
     * @param b the Coordinate to compare to
     * @return a boolean; true if equal
     */
    public boolean is(Crd b) {
        if (b==null) return false;
        return (x==b.x && y==b.y);
    }

    /**
     *
     * @return the ordered set representation of the Coordinate
     */
    @Override
    public String toString() {
        return "("+x+","+y+")";
    }

    /**
     *
     * @return
     */
    public String toStringI() {
        return x+"+"+y+"i";
    }

    /**
     *
     */
    public void print() {
        System.out.println(this);
    }

    /**
     *
     */
    public void printI() {
        System.out.println(toStringI());
    }

    /**
     *
     * @param g
     */
    public void draw(Graphics g) {
        draw(g,Color.black);
    }

    /**
     *
     * @param g
     * @param size
     */
    public void draw(Graphics g, double size) {
        draw(g,Color.black,size);
    }

    /**
     *
     * @param g
     * @param c
     */
    public void draw(Graphics g, Color c) {
        draw(g,c,1);
    }

    /**
     *
     * @param g
     * @param c
     * @param size
     */
    public void draw(Graphics g, Color c, double size) {
        g.setColor(c);
        g.fillRect((int) (x-size/2), (int) (y-size/2), (int) size, (int) size);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Single variable Manipulators and Relations

    /**
     * Useful retriever of the <b>x</b> value for <b><i>int</i></b> inputs
     * @return the <b>x</b>-value as an <b><i>int</i></b>
     * @see #x
     */
    public int x() {
        return (int) x;
    }
    /**
     * Useful retriever of the <b>y</b> value for <b><i>int</i></b> inputs
     * @return the <b>y</b>-value as an <b><i>int</i></b>
     * @see #y
     */
    public int y() {
        return (int) y;
    }

    /**
     *
     * @return
     */
    public Crd negX() {
        return new Crd(-x,y);
    }

    /**
     *
     * @return
     */
    public Crd negY() {
        return new Crd(x,-y);
    }

    /**
     *
     * @param X
     * @return
     */
    public Crd addX(double X) {
        Crd r = new Crd(x+X,y);
        return r;
    }

    /**
     *
     * @param Y
     * @return
     */
    public Crd addY(double Y) {
        Crd r = new Crd(x,y+Y);
        return r;
    }

    /**
     *
     * @param a
     * @return
     */
    public Crd scaleX(double a) {
        return new Crd(x*a,y);
    }

    /**
     *
     * @param a
     * @return
     */
    public Crd scaleY(double a) {
        return new Crd(x,y*a);
    }

    /**
     *
     * @param b
     * @return
     */
    public double distX(Crd b) {
        double r = b.x-x;
        return r;
    }

    /**
     *
     * @param b
     * @return
     */
    public double distY(Crd b) {
        double r = b.y-y;
        return r;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Simple Two-variable Manipulators and Relations

    /**
     *
     * @return
     */
    public Crd abs() {
        return new Crd(Math.abs(x),Math.abs(y));
    }

    /**
     *
     * @return
     * @see #negX()
     * @see #negY()
     */
    public Crd neg() {
        return new Crd(-x,-y);
    }

    /**
     *
     * @return
     */
    public Crd toInt() {
        return new Crd((int)x,(int)y);
    }

    /**
     *
     * @return
     */
    public Crd round() {
        return new Crd(Math.round(x),Math.round(y));
    }

    /**
     *
     * @param X
     * @param Y
     * @return
     */
    public Crd add(double X, double Y) {
        return new Crd(x+X,y+Y);
    }

    /**
     *
     * @param b
     * @return
     */
    public Crd add(Crd b) {
        double x = this.x + b.x;
        double y = this.y + b.y;
        Crd r= new Crd(x,y);
        return r;
    }

    /**
     *
     * @param a
     * @return
     */
    public Crd scale(double a) {
        return new Crd(x*a,y*a);
    }

    /**
     *
     * @param b
     * @return
     */
    public double distTaxi(Crd b) {
        return Math.abs(distX(b))+Math.abs(distY(b));
    }

    /**
     *
     * @param b
     * @return
     */
    public Crd distXY(Crd b) {
        Crd r = new Crd(distX(b),distY(b));
        return r;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Normal Manipulators and Relations

    /**
     *
     * @return
     */
    public Crd unitScale() {
        if (this==new Crd()) {return new Crd();}
        return scale(1/dist());
    }

    /**
     *
     * @param radians
     * @return
     */
    public Crd rotate(double radians) {
        return rotate(new Crd(),radians);
    }

    /**
     *
     * @param axis
     * @param radians
     * @return
     */
    public Crd rotate(Crd axis, double radians) {
        Crd c = add(axis.neg());

        double d = c.dist();
        double a = c.angle();
        a+=radians;

        Crd r = new Crd( d*Math.cos(a), d*Math.sin(a) );
        return r.add(axis);
    }

    /**
     *
     * @return
     */
    public double dist() {
        return dist(new Crd());
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    static public double dist(double x, double y) {
        return new Crd(x,y).dist();
    }

    /**
     *
     * @param b
     * @return
     */
    public double dist(Crd b) {
        double r = Math.sqrt( Math.pow(b.x-x,2) + Math.pow(b.y-y,2) );
        return r;
    }

    // returns the distance, using an oval scale rather than a circular scale, where a point on the oval of the dists is 1
    /**
     *
     * @param distX
     * @param distY
     * @return
     */
    public double distOval(double distX, double distY) {
        return (Math.pow(x, 2)/Math.pow(distX, 2)) + (Math.pow(y, 2)/Math.pow(distY, 2));
    }

    /**
     *
     * @return
     */
    public double slope() {
        return slope(new Crd());
    }

    /**
     *
     * @param b
     * @return
     */
    public double slope(Crd b) {
        double r=0;
        if (b.x-x!=0) {
            r = (b.y-y)/(b.x-x);
        }
        return r;
    }

    /**
     *
     * @return
     */
    public double angle() {
        return angle(new Crd());
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    static public double angle(double x, double y) {
        return new Crd(x,y).angle();
    }

    /**
     *
     * @param center
     * @return
     */
    public double angle(Crd center) {
        return Math.atan2(y-center.y, x-center.x);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Complex Manipulators and Relations

    /**
     *
     * @return
     */
    public Crd inv() {
        return new Crd(x/(x*x+y*y),-y/(x*x+y*y));
    }

    /**
     *
     * @param z
     * @return
     */
    public Crd mult(Crd z) {
        return new Crd(x*z.x-y*z.y,x*z.y+y*z.x);
    }

    /**
     *
     * @param i
     * @return
     */
    public Crd multI(double i) {
        return new Crd(y*i,x*i);
    }

    /**
     *
     * @return
     */
    public Crd e() {
        return new Crd(Math.pow(E, x)*Math.cos(y), Math.pow(E, x)*Math.sin(y));
    }

    /**
     *
     * @return
     */
    public Crd ln() {
        return ln(0);
    }

    /**
     *
     * @param branch
     * @return
     */
    public Crd ln(int branch) {
        return new Crd(Math.log(dist()),angle());
    }

    /**
     *
     * @return
     */
    public Crd sin() {
        Crd theta = multI(1);
        theta = theta.e().add(theta.neg().e().neg());
        return theta.scale(-0.5).multI(1);
    }

    /**
     *
     * @return
     */
    public Crd cos() {
        Crd theta = multI(1);
        theta = theta.e().add(theta.neg().e());
        return theta.scale(0.5);
    }

    /**
     *
     * @return
     */
    public Crd tan() {
        return sin().mult(cos().inv());
    }

    /**
     *
     * @param z
     * @return
     */
    public Crd pow(Crd z) {
        double mag = Math.pow(E, z.x*Math.log(dist())-z.y*angle());
        double theta = z.x*angle()+z.y*Math.log(dist());
        return new Crd(mag*Math.cos(theta), mag*Math.sin(theta));
    }

    /**
     *
     * @param re
     * @return
     */
    public Crd powR(double re) {
        double theta = angle()*re;
        double d = Math.pow(dist(), re);
        return polar(d, theta);
    }

    /**
     *
     * @param im
     * @return
     */
    public Crd powI(double im) {
        return new Crd(Math.pow(E,angle()*im)*Math.cos(im*Math.log(dist())),Math.pow(E, angle()*im)*Math.sin(im*Math.log(dist())));
    }

    //------------------------------------------------------------------------------------------------------------------
    // Vector Manipulators

    /**
     *
     * @param b
     * @return
     */
    public double dot(Crd b) {
        return x*b.x+y*b.y;
    }

    // flattens (moves perpendicular to b) the current vector to lie on vector b

    /**
     *
     * @param b
     * @return
     */
    public Crd vecFlat(Crd b) {
        return scale(dot(b)/Math.pow(dist(),2));
    }

    // returns this aligned to OmniLibrary.Coord b and the axis, maintaining the distance from the axis

    /**
     *
     * @param axis
     * @param b
     * @return
     */
    public Crd alignRot(Crd axis, Crd b) {
        Crd r = alignOppRot(axis, b);
        Crd rr = r.rotate(axis,PI);
        if (dist(r)==dist(rr)) {return alignOppRot(axis,b);}
        if (dist(r)<dist(rr)) {return r;} else {return rr;}
    }

    // returns the OmniLibrary.Coord aligned rotationally opposite to OmniLibrary.Coord b, maintaining the distance from the axis

    /**
     *
     * @param axis
     * @param b
     * @return
     */
    public Crd alignOppRot(Crd axis, Crd b) {
        double radians = b.rotate(axis, PI).angle(axis);
        double radius = this.dist(axis);
        return polar(radius, radians).add(axis);
    }

    // returns the OmniLibrary.Coord aligned rotationally opposite to OmniLibrary.Coord b, 'flattening' it

    /**
     *
     * @param axis
     * @param b
     * @return
     */
    public Crd alignFlat(Crd axis, Crd b) {
        double radians = b.rotate(axis,PI).angle(axis);
        double dradians = Math.abs(radians-this.angle(axis));
        double radius = this.dist(axis)*Math.cos(dradians);
        return polar(radius,radians).add(axis);
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public boolean isLeft(Crd a, Crd b){
        return ((b.x - a.x)*(y - a.y) - (b.y - a.y)*(x - a.x)) > 0;
    }

    /**
     *
     * @param a
     * @param b1
     * @param b2
     * @return
     */
    public boolean sameSide(Crd a, Crd b1, Crd b2) {
        boolean aLeft = a.isLeft(b1, b2);
        boolean thisLeft = isLeft(b1, b2);
        return aLeft && thisLeft;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public boolean inside(Crd a, Crd b, Crd c) {
        return this.sameSide(a, b, c) && this.sameSide(c, a, b) && this.sameSide(b, c, a);
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    public boolean inside(Crd a, Crd b, Crd c, Crd d) {
        return this.inside(a, b, c) || this.inside(a, b, d) || this.inside(b, c, d) || this.inside(a, c, d);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Special Manipulators and Relations

    /**
     *
     * @param b
     * @param t
     * @return
     */
    public Crd lerp(Crd b, double t) {
        return scale(1-t).add(b.scale(t));
    }

    /**
     *
     * @param p
     * @param T
     * @return
     */
    public Crd bezier(Crd[] p, double T) {

        if (p.length==1) return p[0];
        Crd[] r = new Crd[p.length-1];
        for (int c=0; c<p.length-1;c++) {
            r[c] = p[c].lerp(p[c+1], T);
        }
        return bezier(r,T);
    }

    /**
     * A function that randomly negates the <b>x</b> or <b>y</b> coordinates
     * @param chance the probability that each coordinate will be turned negative
     * @return <i>This coord</i> with possibly inverted coordinates
     */
    public Crd randomNeg(double chance) {
        Crd r = new Crd(x,y);
        if (Math.random()<chance) r = r.negX();
        if (Math.random()<chance) r = r.negY();
        return r;
    }
    /**
     * A function that randomly negates the <b>x</b> or <b>y</b> coordinates
     * <br><small>using a provided <b>Random Number Generator</b></small>
     * @param R the random number generator used to produce the random number
     * @param chance the probability that each coordinate will be turned negative
     * @return <i>This coord</i> with possibly inverted coordinates
     */
    public Crd randomSeedNeg(Random R, double chance) {
        Crd r = new Crd(x,y);
        if (R.nextDouble()<chance) {
            r = r.negX();
        }
        if (R.nextDouble()<chance) {
            r = r.negY();
        }
        return r;
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public Crd confine(double min, double max) {		// adjusts coord to be inside the range, otherwise keeps the same
        Crd copy = this;
        copy.x = Math.min(Math.max(copy.x, min), max);
        copy.y = Math.min(Math.max(copy.y, min), max);
        return copy;
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public Crd confine(Crd min, Crd max) {
        Crd copy = this;
        copy.x = Math.min(Math.max(copy.x, min.x), max.x);
        copy.y = Math.min(Math.max(copy.y, min.y), max.y);
        return copy;
    }

    /**
     *
     * @param a
     * @param rollval
     * @return
     */
    static public double roll(double a, double rollval) {
        double A = a%rollval;
        if (A<0) {A=rollval+A;}
        return A;
    }

    /**
     *
     * @param rolldist
     * @return
     */
    public Crd roll(double rolldist) {
        return new Crd(roll(x,rolldist),roll(y,rolldist));
    }

    /**
     *
     * @param rollX
     * @param rollY
     * @return
     */
    public Crd rollXY(double rollX, double rollY) {
        return new Crd(roll(x,rollX),roll(y,rollY));
    }

    /**
     *
     * @param b
     * @param rollX
     * @param rollY
     * @return
     */
    public double rollDist(Crd b, int rollX, int rollY) {
        Crd[] r = new Crd[5];
        r[0] = b;
        r[1] = b.addX(rollX);
        r[2] = b.addX(-rollX);
        r[3] = b.addY(rollY);
        r[4] = b.addY(-rollY);

        return r[getClosest(r)].dist(this);
    }

    /**
     *
     * @param b
     * @return
     */
    public Crd rangeAnd(Crd b) {		// gives intersection of two ranges, given that they intersect
        Crd r = new Crd();
        if (Math.abs(x-b.y)<Math.abs(y-b.x)) {
            r = new Crd(Math.min(x, b.y),Math.max(x, b.y));
        } else {
            r = new Crd(Math.min(y, b.x),Math.max(y, b.x));
        }
        return r;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Coordinate Array Manipulators

    /**
     *
     * @param b
     * @return
     */
    public int getClosest(Crd[] b) {
        if (b.length<2) {return 0;}
        int closest = 0;
        double closestDist = dist(b[0]);
        for (int c=1;c<b.length;c++) {
            if (dist(b[c])<closestDist) {closest=c; closestDist= dist(b[c]);}
        }
        return closest;
    }

    /**
     *
     * @param b
     * @param range
     * @return
     */
    public Integer[] getWithinRange(Crd[] b, double range) {
        ArrayList<Crd> B = new ArrayList<Crd>(Arrays.asList(b));
        ArrayList<Integer> r = new ArrayList<Integer>();

        for (int c=0; c<b.length; c++) {
            Crd[] C = B.toArray(new Crd[0]);
            int closest = getClosestWithinRange(C,range);
            if (closest==-1) {c=b.length;} else {
                r.add(closest); B.remove(closest);
            }
        }

        if (r.isEmpty()) {return null;}
        return r.toArray(new Integer[0]);
    }

    /**
     *
     * @param b
     * @param range
     * @return
     */
    public int getClosestWithinRange(Crd[] b, double range) {
        int c = getClosest(b);
        if (dist(b[c])<=range) {return c;} else {return -1;}
    }

    /**
     *
     * @param range
     * @return
     */
    public Crd[] getGridWithinRange(double range) {
        Crd ths = this.round();
        ArrayList<Crd> r = new ArrayList<Crd>();
        for (int dy=(int) -Math.ceil(range);dy<Math.ceil(range);dy++) {
            for (int dx=(int) -Math.ceil(range);dx<Math.ceil(range);dx++) {
                if (ths.dist(this.add(dx, dy))<=range) {r.add(ths.add(dx, dy));}
            }
        }
        return r.toArray(new Crd[0]);
    }

    /**
     *
     * @param scale
     * @return
     */
    public Crd[] getCornersScaled(Crd scale) {
        Crd rel = new Crd(Math.floor(x / scale.x), Math.floor(y / scale.y));
        Crd[] R = new Crd[]{rel, rel.addX(1), rel.addY(1), rel.add(1, 1)};
        return R;
    }
}
