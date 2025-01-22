package GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class Render extends JFrame{
    private Graphics frameGraphics;
    private final Consumer<Graphics> paint;
    private final Consumer<Void> render;
    public Timer screenTimer;
    public Timer updateTimer;

    public BufferedImage image;
    public BufferedImage finalImage;
    private Graphics imageGraphics;

    // Creates the renderer, ensuring the proper threads are started
    public Render(Consumer<Void> renderer, Consumer<Graphics> painter) {
        render = renderer == null? a -> {} : renderer;
        paint = painter;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSize();
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                turnOff();
            }
        });

        updateSize();
        setRenderThread();
        setPaintThread();
    }

    // The method that smooths painting when resizing the screen
    public void updateSize() {
        int width = Math.max( getWidth(), 1);
        int height = Math.max( getHeight(), 1);
        image = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
        finalImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
        imageGraphics = image.getGraphics();
        frameGraphics = getGraphics();
    }


    // The entire render routine: first updates graphical variables, then graphics - uses a buffered image
    public void routine() {
        render.accept(null);
        paint.accept(imageGraphics);
        finalImage.getGraphics().drawImage(image,0,0,null);
    }

    // creates a separate thread to render the graphics
    public void setRenderThread() {

        updateTimer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                routine();
            }
        });
        updateTimer.start();
    }

    // Creates a separate thread to repaint the buffered image
    public void setPaintThread() {
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        screenTimer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    frameGraphics.drawImage(finalImage, 0, 0, null);
                } catch (Exception f) {
                    f.printStackTrace();
                }
            }
        });
        screenTimer.start();
    }

    // Safely ends all rendering processes
    public void turnOff() {
        screenTimer.stop();
        updateTimer.stop();
    }

}
