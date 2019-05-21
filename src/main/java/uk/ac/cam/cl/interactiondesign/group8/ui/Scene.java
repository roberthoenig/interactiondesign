package uk.ac.cam.cl.interactiondesign.group8.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import uk.ac.cam.cl.interactiondesign.group8.Settings;
import uk.ac.cam.cl.interactiondesign.group8.utils.*;

public class Scene extends JPanel {
    private Cog cog;
    private Bonjo character;
    private Temperature temperature;
    private TimeCarousel timeCarousel;
    private Tree tree;
    private Weather weather;
    private WindSock windSock;
    JPanel widgetPanel;
    // Widget getters
    public Bonjo getCharacter() {
        return character;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public TimeCarousel getTimeCarousel() {
        return timeCarousel;
    }

    public Tree getTree() {
        return tree;
    }

    public WindSock getWindSock() {
        return windSock;
    }

    // Sets the dynamic weather widget
    public void setWeather(EWeather... w) {
        switch (w[0]) {
            case THUNDERSTORM:
                weather = new StormCloud(this);
                break;
            case DRIZZLE:
            case RAIN:
                weather = new RainCloud(this);
                break;
            case SNOW:
                weather = new SnowCloud(this);
                break;
            case FOG:
                weather = new Fog(this);
                break;
            case OVERCAST:
                weather = new Cloud(this);
                break;
            case CLEAR:
            default:
                weather = null;
        }

        if (weather != null) {
            widgetPanel.add(weather);
        }
    }

    public void postMessage(String message) {
        character.displayMessage(message);
    }

    public Scene() {
        JLayeredPane jlp = new JLayeredPane();

        // Background sky
        timeCarousel = new TimeCarousel();
        jlp.add(timeCarousel, JLayeredPane.DEFAULT_LAYER);

        // Background land
        JImage landBackground = new JImage();
        try {
            landBackground.setImage(ResourceLoader.loadImage("scenecomponents/backgroundbonzi.png"));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        jlp.add(landBackground, Integer.valueOf(1));


        // Widget layer
        widgetPanel = new JPanel();
        widgetPanel.setLayout(null);
        widgetPanel.setOpaque(false);

        character = new Bonjo();

        widgetPanel.add(character);

        temperature = new Temperature(this);
        
        widgetPanel.add(temperature);

        tree = new Tree(this);

        windSock = new WindSock(this);
        widgetPanel.add(windSock);

        cog = new Cog(this);
        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
        SettingsDialog settingsDialog = new SettingsDialog(frame, SettingsDialog.generateSummand(10, 99), SettingsDialog.generateSummand(10, 99));
        settingsDialog.pack();
        cog.addMouseListener(new MouseListener(){
            @Override
            public void mouseReleased(MouseEvent arg0) {
                settingsDialog.setVisible(true);
                try {
                    cog.setImage(ResourceLoader.loadImage("scenecomponents/settingsicon.png"));
                } catch (Exception e) {
                    // lalala this cannot happen
                }
            }
            @Override
            public void mousePressed(MouseEvent arg0) {
                try {
                    cog.setImage(ResourceLoader.loadImage("scenecomponents/settingsiconselected.png"));
                } catch (Exception e) {
                    // really. it can't. get over it.
                }
            }
            @Override
            public void mouseExited(MouseEvent arg0) {}
            @Override
            public void mouseEntered(MouseEvent arg0) {}
            @Override
            public void mouseClicked(MouseEvent arg0) {}
        });
        widgetPanel.add(cog);

        widgetPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                character.setBounds(
                        (int)(0.384f * e.getComponent().getWidth()), (int)(0.588f * e.getComponent().getHeight()),
                        (int)(0.308f * e.getComponent().getWidth()), (int)(0.360f * e.getComponent().getHeight()));
                temperature.setBounds(
                        (int)(0.725f * e.getComponent().getWidth()), (int)(0.593f * e.getComponent().getHeight()),
                        (int)(0.250f * e.getComponent().getWidth()), (int)(0.310f * e.getComponent().getHeight()));

                if (weather != null) {
                    weather.setBounds(
                            (int)(weather.getxCorner() * e.getComponent().getWidth()), (int)(weather.getyCorner() * e.getComponent().getHeight()),
                            (int)(weather.getxScale() * e.getComponent().getWidth()), (int)(weather.getyScale() * e.getComponent().getHeight()));
                }

                windSock.setBounds(
                        (int)(0.765f * e.getComponent().getWidth()), (int)(0.257f * e.getComponent().getHeight()),
                        (int)(0.128f * e.getComponent().getWidth()), (int)(0.294f * e.getComponent().getHeight()));

            }
        });

        jlp.add(widgetPanel, JLayeredPane.PALETTE_LAYER);

        // JLP resize policies (all fullscreen)
        jlp.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                timeCarousel.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
                landBackground.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
                widgetPanel.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });

        setLayout(new BorderLayout(0, 0));
        add(jlp, BorderLayout.CENTER);
    }
}
