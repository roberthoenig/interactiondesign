package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.*;
import uk.ac.cam.cl.interactiondesign.group8.utils.*;

import java.io.*;

import java.util.Date;
import java.time.*;
import java.util.function.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class DateBar extends JPanel {

    private class Slider extends JLayeredPane {
        private int numDays = 4;

        Consumer<Date> callback;

        private Date start;
        private Date end;
        private Date current;

        private JPanel bar;
        private JImage sliderImg;
        private JPanel sliderContainer;

        public void setStartDate(Date s) {
            start = s;
            end = Date.from(s.toInstant().plusSeconds(numDays * 86400));

            if (start.after(current)) setCurrentDate(start);
        }

        public void setCurrentDate(Date c) {
            // Check c is within the bounds of the slider
            if (start.after(c)) c = start;
            if (end.before(c)) c = end;

            current = c;
            callback.accept(current);

            // Update slider position
            long deltaSec = current.toInstant().getEpochSecond() - start.toInstant().getEpochSecond();
            long pos = deltaSec * (sliderContainer.getWidth() - sliderImg.getWidth()) / (numDays * 86400);
            sliderImg.setBounds((int)pos, 0, 20, sliderContainer.getHeight());
            sliderImg.revalidate();
        }

        public void setNumDays(int i) {
            numDays = i;
            end = Date.from(start.toInstant().plusSeconds(numDays * 86400));

            createBar();
        }

        private void createBar() {
            // Fill slider
            for (int i = 0; i <= numDays; ++i) {
                JLabel l = new JLabel(Integer.toString(i) + "                                       ");
                bar.add(l);
            }
        }

        public Slider(Consumer<Date> cb) {
            callback = cb;

            // Place the slider in a valid state
            start = new Date();
            end = new Date();
            current = new Date();

            // Construct UI
            setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            setOpaque(false);
            
            sliderContainer = new JPanel();
            sliderContainer.setOpaque(false);
            sliderContainer.setLayout(null);
            sliderImg = new JImage();
            sliderImg.setMaximumSize(new Dimension(20, Integer.MAX_VALUE));
            try {
                sliderImg.setImage(ResourceLoader.loadImage("scenecomponents/slider.png"));
            }
            catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            sliderContainer.add(sliderImg);
            add(sliderContainer);
            

            JPanel barBorder = new JPanel();
            barBorder.setOpaque(false);
            barBorder.setLayout(new BorderLayout(0,0));
            barBorder.setBorder(new EmptyBorder(10, 20, 10, 20));

            bar = new JPanel();
            bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
            bar.setPreferredSize(new Dimension(999999, 20));
            barBorder.add(bar, BorderLayout.CENTER);

            add(barBorder);

            addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    barBorder.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
                    sliderContainer.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
                }
            });


            createBar();

            // Enable mouse drag
            Slider s = this;
            MouseAdapter ma = new MouseAdapter() {

                private Point origin;

                @Override
                public void mousePressed(MouseEvent e) {
                    origin = new Point(e.getPoint());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (origin != null) {
                        // Calculate target time based on movement
                        int delta = e.getX() - origin.x;
                        origin = e.getPoint();
                        int deltaSecs = (delta * numDays * 86400) / getWidth();
                        Instant newTime = current.toInstant().plusSeconds(deltaSecs);
                        setCurrentDate(Date.from(newTime));
                    }
                }

            };

            addMouseListener(ma);
            addMouseMotionListener(ma);
        }
    }

    private WeatherApp app;


    private Slider slider;
    private JLabel timeLabel;

    // Updates the limits on the slider to reflect the current time
    public void updateTime() {
        slider.setStartDate(new Date());
    }

    public DateBar(WeatherApp wa) {
        app = wa;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        slider = new Slider((Date d) -> {
            timeLabel.setText(d.toString());
            app.setTime(d);
        });

        add(slider);

        timeLabel = new JLabel("NULL");
        add(timeLabel);
        timeLabel.setAlignmentX(0.5f);
    }
}