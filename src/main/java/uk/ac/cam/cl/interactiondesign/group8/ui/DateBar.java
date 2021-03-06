package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.*;
import uk.ac.cam.cl.interactiondesign.group8.Settings.Time;
import uk.ac.cam.cl.interactiondesign.group8.utils.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.*;
import java.util.function.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class DateBar extends JPanel implements Timeable {

    private class Slider extends JLayeredPane {
        private int numDays = 4;

        Consumer<Date> callback;

        private Date start;
        private Date end;
        private Date current;

        private JPanel bar;
        private JImage sliderImg;
        private JPanel sliderContainer;
        private JScrollPane jsp;

        // Set date corresponding to LHS of bar
        public void setStartDate(Date s) {
            start = s;
            end = Date.from(s.toInstant().plusSeconds(numDays * 86400));
            createBar();

            if (start.after(current)) setCurrentDate(start);
        }

        public Date getCurrentDate() {
            return current;
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

        // Number of days to be displayed on bar at once
        public void setNumDays(int i) {
            numDays = i;
            end = Date.from(start.toInstant().plusSeconds(numDays * 86400));

            createBar();
        }

        // Redraw all UI
        private void createBar() {
            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, bar);
            if (viewPort != null) {
                Rectangle view = viewPort.getViewRect();
                
                bar.removeAll();

                // Fill bar
                for (int i = 0; i <= numDays; ++i) {
                    JLayeredPane jlp = new JLayeredPane();
                    JImage background = new JImage();
                    try {
                        background.setImage(ResourceLoader.loadImage("scenecomponents/sliderbackground.png"));
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e.getMessage());
                    }
                    jlp.add(background, 0);
                    JLabel label = new JLabel();
                    label.setText(Integer.toString(i));
                    jlp.add(label, Integer.valueOf(1));

                    jlp.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent e) {
                            background.setBounds(
                                0, 0,
                                e.getComponent().getWidth(), e.getComponent().getHeight());
                            label.setBounds(
                                10, 0,
                                e.getComponent().getWidth() - 10, e.getComponent().getHeight() / 2);
                        }
                    });
                    bar.add(jlp);
                }

                view.x = (int)((bar.getWidth() / (numDays + 1.0f)) * ((start.toInstant().getEpochSecond() % 86400) / 86400.0f));
                view.y = 0;

                bar.scrollRectToVisible(view);
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

            // Scroll pane to move date marks to current time
            jsp = new JScrollPane(bar);
            jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            jsp.setPreferredSize(new Dimension(999999, 20));
            jsp.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    bar.setMinimumSize(new Dimension((int)(jsp.getWidth() * (numDays + 1.0f) / (float)numDays), jsp.getHeight()));
                    bar.setPreferredSize(new Dimension((int)(jsp.getWidth() * (numDays + 1.0f) / (float)numDays), jsp.getHeight()));
                    bar.setMaximumSize(new Dimension((int)(jsp.getWidth() * (numDays + 1.0f) / (float)numDays), jsp.getHeight()));
                }
            });

            barBorder.add(jsp, BorderLayout.CENTER);

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

    public void setTime(Date d) {
        switch(Settings.time) {
            case TWELVE_HOURS: setFormatToTwelveHourClock(); break;
            case TWENTYFOUR_HOURS: setFormatToTwentyFourHourClock(); break;
        }
        app.setTime(d);
    }

    public void updateTime(Time time) {
        switch(time) {
            case TWELVE_HOURS: setFormatToTwelveHourClock(); break;
            case TWENTYFOUR_HOURS: setFormatToTwentyFourHourClock(); break;
        }
    }

    public void setFormatToTwelveHourClock() {
        DateFormat twelveHourDateFormat = new SimpleDateFormat("E hh:mm a");
        timeLabel.setText(twelveHourDateFormat.format(slider.getCurrentDate()));
    }

    public void setFormatToTwentyFourHourClock() {
        DateFormat twelveHourDateFormat = new SimpleDateFormat("E HH:mm");
        timeLabel.setText(twelveHourDateFormat.format(slider.getCurrentDate()));
    }

    public DateBar(WeatherApp wa) {
        app = wa;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        slider = new Slider(this::setTime);

        add(slider);

        timeLabel = new JLabel("NULL");
        timeLabel.setOpaque(true);
        timeLabel.setBackground(Color.WHITE);
        add(timeLabel);
        timeLabel.setAlignmentX(0.5f);
        Settings.timeables.add(this);
    }
}