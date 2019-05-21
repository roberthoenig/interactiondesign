package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.utils.*;

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Bonjo extends JPanel implements ICharacter {
    private JImage bonjoImg;
    private JImage bonjoClothes;
    private JPanel messagePanel;
    private JLabel messageText;
    private JImage messageImg;

    private ChatSelector chatSelector;

    // Creates a speech bubble with the message
    public void displayMessage(String message) {
        messagePanel.setOpaque(true);
        messageText.setText(message);
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
        if (clothing == EClothing.NONE)
            bonjoClothes.setVisible(false);
        else
            bonjoClothes.setVisible(true);
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

    public Bonjo() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setOpaque(false);

        // Set up the chat ability
        chatSelector = new ChatSelector("localization/bonjo.json", ChatSelector.LanguageCode.en_GB);

        // Panel for Bonjo's messages
        messagePanel = new JPanel();
        messagePanel.setLayout(new OverlayLayout(messagePanel));
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
        messageImg.setAlignmentX(0.5f);
        messageImg.setAlignmentY(0.5f);
        messageImg.setMaximumSize(new Dimension(250, 200));

        messageText = new JLabel();
        messageText.setAlignmentX(0.8f);
        messageText.setAlignmentY(0.5f);

        messagePanel.add(messageText);
        messagePanel.add(messageImg);

        // Hide panel onClick
        messagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                hideMessage();
            }
        });
        hideMessage(); // Default hidden

        add(messagePanel);

        // Panel containing Bonjo + Clothes
        JPanel bonjoPanel = new JPanel();
        bonjoPanel.setLayout(new OverlayLayout(bonjoPanel));
        bonjoPanel.setOpaque(false);
        bonjoPanel.setPreferredSize(new Dimension(100, 100));

        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout(0, 0));
        jp.setOpaque(false);
        bonjoClothes = new JImage();
        bonjoClothes.setVisible(false);
        jp.add(bonjoClothes, BorderLayout.CENTER);
        bonjoPanel.add(jp);

        jp = new JPanel();
        jp.setLayout(new BorderLayout(0, 0));
        jp.setOpaque(false);
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


        jp.add(bonjoImg, BorderLayout.CENTER);
        bonjoPanel.add(jp);

        add(bonjoPanel);

        // Testing only
//        displayMessage("Hello World!");
    }
}