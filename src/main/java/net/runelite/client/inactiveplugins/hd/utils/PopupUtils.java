package net.runelite.client.inactiveplugins.hd.utils;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Constants;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.IntConsumer;

import static net.runelite.client.inactiveplugins.hd.utils.ResourcePath.path;

@Slf4j
public class PopupUtils {
    public static void displayPopupMessage(Client client, String title, String message, String[] buttonLabels, IntConsumer buttonIndexConsumer) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 8));

           /* try {
                BufferedImage logoImage = path(HdPlugin.class, "logo.png").loadImage();
                frame.setIconImage(logoImage);
                Image logoScaled = logoImage.getScaledInstance(96, -1, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(logoScaled));
                logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                mainPanel.add(logoLabel, BorderLayout.LINE_START);
            } catch (IOException ex) {
                log.error("Unable to load HD logo: ", ex);
            }*/

            String html = String.format("<html><style>a { color: #dc8a00; }</style><body>%s</body></html>", message);
            JEditorPane messagePane = new JEditorPane("text/html", html);
            messagePane.setBorder(BorderFactory.createEmptyBorder());
            messagePane.setHighlighter(null);
            messagePane.setEditable(false);
            messagePane.setOpaque(false);
            messagePane.addHyperlinkListener(e -> {
                if (Desktop.isDesktopSupported() && e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException | URISyntaxException ex) {
                        log.error("Unable to open link: {}", e.getURL().toString(), ex);
                    }
                }
            });

			JScrollPane scrollPane = new JScrollPane(messagePane);
            scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
			scrollPane.setOpaque(false);
			scrollPane.getViewport().setOpaque(false);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			mainPanel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
            for (int i = 0; i < buttonLabels.length; i++) {
                JButton button = new JButton(buttonLabels[i]);
                int index = i;
                button.addActionListener(actionEvent -> {
                    frame.setVisible(false);
                    buttonIndexConsumer.accept(index);
                });
                buttonPanel.add(button);
            }

            JPanel framePanel = new JPanel(new BorderLayout());
            framePanel.add(mainPanel, BorderLayout.CENTER);
            framePanel.add(buttonPanel, BorderLayout.PAGE_END);

            frame.setContentPane(framePanel);
            frame.pack();
            frame.setResizable(false);

            frame.setLocationRelativeTo(client.getCanvas());
            Point point = frame.getLocation();
            frame.setLocation(point.x + 5, point.y + (Constants.GAME_FIXED_HEIGHT - client.getCanvasHeight()) / 2 - 3);
            frame.setAutoRequestFocus(true);

            JFrame runeLiteWindow = (JFrame) SwingUtilities.getWindowAncestor(client.getCanvas());
            if (runeLiteWindow.isAlwaysOnTop())
                frame.setAlwaysOnTop(true);

            frame.setVisible(true);
        });
    }
}
