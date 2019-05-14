package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.*;

import java.util.Date;
import java.time.*;
import java.util.function.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DateBar extends JPanel {

    private class Slider extends JPanel {
        private int numDays = 4;

        Consumer<Date> callback;

        private Date start;
        private Date end;
        private Date current;

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
            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, this);
            if (viewPort != null) {
                Rectangle view = viewPort.getViewRect();
                long deltaSec = current.toInstant().getEpochSecond() - start.toInstant().getEpochSecond();
                long currentPos = (getWidth() * deltaSec) / (numDays * 86400);
                view.x = Math.max(0, (int) currentPos - getWidth() / 2);
                view.y = 0;

                scrollRectToVisible(view);
            }
        }

        public void setNumDays(int i) {
            numDays = i;
            end = Date.from(start.toInstant().plusSeconds(numDays * 86400));

            createUI();
        }

        private void createUI() {
            // Construct all swing elements
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            // Fill slider
            for (int i = 0; i <= numDays; ++i) {
                JLabel l = new JLabel(Integer.toString(i) + "                                       ");
                add(l);
            }
        }

        public Slider(Consumer<Date> cb) {
            callback = cb;

            // Place the slider in a valid state
            start = new Date();
            end = new Date();
            current = new Date();

            createUI();

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
                        int delta = origin.x - e.getX();
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
        setLayout(new BorderLayout(0, 0));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        slider = new Slider((Date d) -> {
            timeLabel.setText(d.toString());
            app.setTime(d);
        });

        JScrollPane jsp = new JScrollPane(slider);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        container.add(jsp);

        timeLabel = new JLabel("NULL");
        container.add(timeLabel);

        add(container, BorderLayout.PAGE_START);
    }
}