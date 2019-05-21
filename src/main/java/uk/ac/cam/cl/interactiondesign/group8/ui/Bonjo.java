package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.utils.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Bonjo extends JPanel implements ICharacter {
    private JImage bonjoImg;

    private JLayeredPane messagePanel;
    private JLabel messageText;
    private JImage messageImg;
    private JLayeredPane bonjoPanel;

    private ChatSelector chatSelector;

    // Creates a speech bubble with the message
    public void displayMessage(String message) {
        messagePanel.setOpaque(true);
        messageText.setText("<html>"+message+"</html>");
        messageImg.setVisible(true);
    }

    // Hides the message panel again
    public void hideMessage() {
        messagePanel.setOpaque(false);
        messageImg.setVisible(false);
        messageText.setText("");
    }

    // Updates the character to wear the clothing
    public void setClothing(EClothing clothing) {
    //    if (clothing == EClothing.NONE)
    //        bonjoClothes.setVisible(false);
    //    else
     //       bonjoClothes.setVisible(true);
    }

    public void randomChat(IChatInterface... options) {
        ChatSelector.Result result = chatSelector.getChatMessage(options);

        // Handle the text message
        displayMessage(result.getMessage());

        // Handle the audio
//        if (result.getAudio() != null) {}

        // Handle the clothing change
//        if (result.getClothing() != null) {}
    }

    public void randomChat(IChatInterface[] options1, IChatInterface... options2) {
        var out = new ArrayList<>(Arrays.asList(options1));
        out.addAll(Arrays.asList(options2));

        randomChat(out.toArray(new IChatInterface[0]));
    }

    public void rescale(int width, int height){
        messagePanel.setPreferredSize(new Dimension(width*3/10, height/2));
        //bonjoPanel.setPreferredSize(new Dimension(width*6/10, width*6/10/10*9));
        messageImg.setMaximumSize(new Dimension(width*3/10, height/2));

        messageText.setFont(new Font(messageText.getFont().getName(), Font.PLAIN, height/30));
    }

    public Bonjo() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        // Set up the chat ability
        chatSelector = new ChatSelector("localization/bonjo.json", ChatSelector.LanguageCode.en_GB);

        // Panel for Bonjo's messages
        messagePanel = new JLayeredPane();
        //messagePanel.setLayout(new OverlayLayout(messagePanel));
        messagePanel.setOpaque(false);
        messagePanel.setPreferredSize(new Dimension(250, 200));
        messagePanel.setBackground(new Color(0,0,0,1));

        BufferedImage messageImage;
        try{
            messageImage = ResourceLoader.loadImage("bonjocomponents/speechbubble.png");
        } catch (IOException ioe){
            throw new RuntimeException(ioe.getMessage());
        }
        messageImg = new JImage();
        messageImg.setImage(messageImage);
        //messageImg.setAlignmentX(0.5f);
        //messageImg.setAlignmentY(0.5f);
        //messageImg.setMaximumSize(new Dimension(250, 200));

        messageText = new JLabel();
        //messageText.setAlignmentX(0.8f);
        //messageText.setAlignmentY(0.5f);

        messagePanel.add(messageText, JLayeredPane.PALETTE_LAYER);
        messagePanel.add(messageImg, JLayeredPane.DEFAULT_LAYER);

        // Hide panel onClick
        messagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                hideMessage();
            }
        });

        messagePanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                messageImg.setBounds(
                        (int)(0f * e.getComponent().getWidth()), (int)(0.15f * e.getComponent().getHeight()),
                        (int)(0.9f * e.getComponent().getWidth()), (int)(0.6f * e.getComponent().getHeight()));
                messageText.setBounds(
                        (int)(0.1f * e.getComponent().getWidth()), (int)(0.15f * e.getComponent().getHeight()),
                        (int)(0.6f * e.getComponent().getWidth()), (int)(0.5f * e.getComponent().getHeight()));
                messageText.setFont(new Font(messageText.getFont().getName(), Font.BOLD, (int)(0.05f*e.getComponent().getHeight())));

            }
        });
        hideMessage(); // Default hidden

        add(messagePanel);

        // Panel containing Bonjo + Clothes
        JLayeredPane bonjoPanel = new JLayeredPane();
        //bonjoPanel.setLayout(new OverlayLayout(bonjoPanel));
        bonjoPanel.setOpaque(false);
        //bonjoPanel.setPreferredSize(new Dimension(100, 100));


        //jp = new JPanel();
        //jp.setLayout(new BorderLayout(0, 0));
        //jp.setOpaque(false);
        BufferedImage img;
        try {
            img = ResourceLoader.loadImage("bonjocomponents/bonjodefault.png");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        bonjoImg = new JImage();
        bonjoImg.setImage(img);

        bonjoImg.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                displayMessage("Hello World!");
            }
        });
        bonjoImg.setPreferredSize(new Dimension(250, 200));

        bonjoPanel.add(bonjoImg, BorderLayout.CENTER);
        //bonjoPanel.add(jp);
        bonjoPanel.setPreferredSize(new Dimension(250, 200));
        bonjoPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                System.out.println(e.getComponent().getWidth());
                bonjoImg.setBounds(
                        (int)(0f * e.getComponent().getWidth()), (int)(0f * e.getComponent().getHeight()),
                        (int)(e.getComponent().getWidth()), (int)(0.9f * e.getComponent().getHeight()));
            }
        });

        add(bonjoPanel);
    }
}