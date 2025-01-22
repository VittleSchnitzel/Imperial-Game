package GameWindow;

import Map.Crd;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Class responsible for holding all the graphics resources
 */
public class Portfolio {
    // Tiles is organized by [size][tileType]
    BufferedImage[][] tiles;
    BufferedImage mapImage;

    // These are organized by [state][animationFrame]
    BufferedImage[][] agents;
    BufferedImage[][] projectiles;
    BufferedImage[][] resources;
    BufferedImage[][] structures;

    BufferedImage[] guiComponents;

    static final int[] tileSize = new int[]{1,2,3};
    int tileSizeSetting = 0;

    public Portfolio() {
        Color[] colors = new Color[]{
                new Color(0,0,100),
                new Color(0,0,200),
                new Color(0, 100, 255),
                new Color(0, 200, 255),
                new Color(70, 225,255),
                new Color(150,250,255),
                new Color(250, 250, 200),
                new Color(200, 250, 120),
                new Color(10, 200, 50),
                new Color(50, 150, 80),
                new Color(80, 120, 100),
                new Color(100, 100, 80),
                new Color(130, 100, 100),
                new Color(100, 75, 75),
                new Color(70, 50, 50),
                new Color(100,100,100),
                new Color(200, 200, 200)
        };
        tiles = new BufferedImage[1][colors.length];
        int tSize = tileSize[tileSizeSetting];
        for (int i = 0; i < colors.length; i++) {
            tiles[0][i] = new BufferedImage( tSize, tSize, BufferedImage.TYPE_INT_ARGB);
            Graphics g = tiles[0][i].getGraphics();
            g.setColor(colors[i]);
            g.fillRect(0,0, tSize, tSize);
        }

    }

    public void drawTestMap(double[][] tileType, double min, double max, int tileSizeSetting) {
        mapImage = new BufferedImage(tileType.length * tiles[tileSizeSetting][(int) tileType[0][0]].getWidth(),
                tileType[0].length * tiles[tileSizeSetting][(int) tileType[0][0]].getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = mapImage.getGraphics();
        int length = tileSize[tileSizeSetting];

        for (int x = 0; x < tileType.length; x++) {
            for (int y = 0; y < tileType[0].length; y++) {
                int input = (int) (255*Math.max(Math.min((tileType[x][y] - min)/(max - min), 1), 0));
                Color hue = new Color( input, input, input);
                g.setColor(hue);

                g.fillRect(length*x, length*y, length*(x+1), length*(y+1));
            }
        }

        System.out.println("finished");
    }

    // Creates a new image of the map when the map gets updated
    public void drawMap(double[][] tileType, int tileSizeSetting) {
        mapImage = new BufferedImage(tileType.length * tiles[tileSizeSetting][0].getWidth(),
                tileType[0].length * tiles[tileSizeSetting][0].getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = mapImage.getGraphics();
        g.setColor(Color.magenta);
        g.fillRect(0,0, mapImage.getWidth(), mapImage.getHeight());
        int length = tiles[tileSizeSetting][0].getHeight();
        for (int x = 0; x < tileType.length; x++) {
            for (int y = 0; y < tileType[x].length; y++) {
                try {
                    g.drawImage(tiles[tileSizeSetting][(int) tileType[x][y]], x*length,y*length, null);
                } catch (Exception e) {}
            }
        }
    }

    public void drawMap(Graphics g, Crd offset) {
        g.drawImage(mapImage, offset.x() + 6, offset.y() + 29, null);
    }
}
