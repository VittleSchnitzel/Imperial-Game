package GameWindow;

import Map.Crd;

import java.awt.*;
import java.awt.event.*;

/**
 * Class responsible for graphics and gui
 * Holds graphical resources and the paint thread
 * Starts the intro screen and also manages user interactions
 * needs to communicate with the game class
 */
public class GameWindow {
    public Game game;
    private Portfolio portfolio;
    public UserInterface ui;
    public Render renderer;

    Crd mapSize = new Crd(500, 500);
    int tileSizeSetting = 0;
    Crd mapDim;


    Crd offset = new Crd();
    Crd lstMouse = new Crd();
    Crd currentMouse = new Crd();

    public GameWindow() {
        game = new Game(mapSize);
        portfolio = new Portfolio();
        mapDim = mapSize.scale(portfolio.tiles[tileSizeSetting][0].getWidth());
        renderer = new Render(null, this::paintCycle);
        renderer.setBounds(300,50,600,600);
        renderer.setVisible(true);

        renderer.addMouseListener(mouse);
        renderer.addMouseMotionListener(mouseMotion);
        renderer.addKeyListener(keys);
    }

    public void paintCycle(Graphics g) {

        // TODO figure out how to manage tile size
        try {
            int size = portfolio.tiles[0][0].getHeight()*mapSize.x();
            if (game.map.updateFlag) {
                portfolio.drawMap(game.map.tiles, 0);
                //portfolio.drawTestMap(game.map.tiles, 0, 1, 0);
                game.map.updateFlag = false;
            }
            if (offset.x > -10 && offset.y > -10) portfolio.drawMap(g, offset.add(-size, -size));
            if (offset.x > -10 && offset.y < renderer.getHeight() + 10) portfolio.drawMap(g, offset.addX(-size));
            if (offset.x < renderer.getWidth() + 10 && offset.y > -10) portfolio.drawMap(g, offset.addY(-size));
            if (offset.x < renderer.getWidth() + 10 && offset.y < renderer.getHeight() + 10) portfolio.drawMap(g, offset);
        } catch (Exception e) {

        }
    }

    MouseListener mouse = new MouseListener() {

        @Override public void mouseClicked(MouseEvent e) {}

        @Override public void mousePressed(MouseEvent e) {
            if (e.getButton() == 1) {
                lstMouse = new Crd(e.getPoint());
                Crd mapPoint = lstMouse.add(-6, -29);
                System.out.println(game.map.tiles[mapPoint.x()][mapPoint.y()]);
            }
        }

        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
    };

    MouseMotionListener mouseMotion = new MouseMotionListener() {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (e.getButton() == 0) {
                offset = offset.add(currentMouse.add(lstMouse.neg())).rollXY(mapDim.x, mapDim.y);
                lstMouse = currentMouse;
                currentMouse = new Crd(e.getPoint());
            }
        }

        @Override public void mouseMoved(MouseEvent e) {}
    };

    KeyListener keys = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case 37:
                    offset = offset.addX(10);
                    break;
                case 38:
                    offset = offset.addY(10);
                    break;
                case 39:
                    offset = offset.addX(-10);
                    break;
                case 40:
                    offset = offset.addY(-10);
                    break;
            }
            offset = offset.rollXY(mapDim.x, mapDim.y);
        }

        @Override
        public void keyReleased(KeyEvent e) {}
    };
}
