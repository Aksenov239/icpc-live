package player;/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009, 2010, 2011, 2012, 2013, 2014 Caprica Software Limited.
 */

import player.widgets.Widget;
import player.widgets.controllers.Controller;
import player.widgets.controllers.ControllerGenerator;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.reflect.InvocationTargetException;

/**
 * This simple test player shows how to get direct access to the video frame data.
 * <p/>
 * This implementation uses the new (1.1.1) libvlc video call-backs function.
 * <p/>
 * Since the video frame data is made available, the Java call-back may modify the contents of the
 * frame if required.
 * <p/>
 * The frame data may also be rendered into components such as an OpenGL texture.
 */
public class Player {

    // The size does NOT need to match the mediaPlayer size - it's the size that
    // the media will be scaled to
    // Matching the native size will be faster of course
    private final int width = 720;
    private final int height = 400;

    GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];
//    private final int width = device.getDisplayMode().getWidth();
//    private final int height = device.getDisplayMode().getHeight();

    /**
     * Image to render the video frame data.
     */
    private final BufferedImage image;

    private final MediaPlayerFactory factory;

    private final DirectMediaPlayer mediaPlayer;

    private ImagePane imagePane;

    public Player(String media, final String[] medias, String[] args, final Widget[] widgets) throws InterruptedException, InvocationTargetException {
        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width, height);
        image.setAccelerationPriority(1.0f);

        factory = new MediaPlayerFactory(args);

        final PlayerInstance[] playerInstance2 = new PlayerInstance[1];

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame("ICPC Live Test");
//                frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
                imagePane = new ImagePane(image, widgets);
                imagePane.setSize(width, height);
                imagePane.setMinimumSize(new Dimension(width, height));
                imagePane.setPreferredSize(new Dimension(width, height));

                frame.getContentPane().setLayout(new BorderLayout());
                frame.getContentPane().add(imagePane, BorderLayout.CENTER);

                frame.pack();
//                frame.setResizable(false);
                frame.setVisible(true);

//                smallPlayer.getPlayer().play();

                Controller[] controllers = new Controller[widgets.length];
//                controllers[0] = smallPlayer.controller;
                for (int i = 0; i < widgets.length; i++) {
                    controllers[i] = widgets[i].getController();
                }

                JFrame controller = ControllerGenerator.createControllerFrame("Main frame controller", controllers);
                controller.setVisible(true);
                controller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                device.setFullScreenWindow(frame);

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent evt) {
                        mediaPlayer.release();
                        factory.release();
//                        smallPlayer.getPlayer().release();
                        System.exit(0);
                    }
                });
            }
        });

        mediaPlayer = factory.newDirectMediaPlayer(new TestBufferFormatCallback(), new TestRenderCallback());
        mediaPlayer.setRepeat(true);
        mediaPlayer.setVolume(0);
        mediaPlayer.playMedia(media);


    }

    @SuppressWarnings("serial")
    private final class ImagePane extends JPanel {

        private BufferedImage buffer;
        private final BufferedImage image;

        private final Font font = new Font("Sansserif", Font.BOLD, 36);
        private final Widget[] widgets;

        public ImagePane(BufferedImage image, Widget[] widgets) {
            this.image = image;
            buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            this.widgets = widgets;
        }

        @Override
        public void paint(Graphics g) {
            if (image == null) return;
            Graphics2D g2 = (Graphics2D) buffer.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2.drawImage(image, null, 0, 0);
            for (Widget widget : widgets) {
                widget.paint(g2, width, height);
            }
            g.drawImage(buffer, 0, 0, null);
        }
    }

    private final class TestRenderCallback extends RenderCallbackAdapter {

        public TestRenderCallback() {
            super(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        }

        @Override
        public void onDisplay(DirectMediaPlayer mediaPlayer, int[] data) {
            // The image data could be manipulated here...

            /* RGB to GRAYScale conversion example */
//            for(int i=0; i < data.length; i++){
//                int argb = data[i];
//                int b = (argb & 0xFF);
//                int g = ((argb >> 8 ) & 0xFF);
//                int r = ((argb >> 16 ) & 0xFF);
//                int grey = (r + g + b + g) >> 2 ; //performance optimized - not real grey!
//                data[i] = (grey << 16) + (grey << 8) + grey;
//            }
            imagePane.repaint();
        }
    }

    private final class TestBufferFormatCallback implements BufferFormatCallback {

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            return new RV32BufferFormat(width, height);
        }

    }
}