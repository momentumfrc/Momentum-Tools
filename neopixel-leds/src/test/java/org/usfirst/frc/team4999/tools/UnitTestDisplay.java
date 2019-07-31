package org.usfirst.frc.team4999.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.usfirst.frc.team4999.lights.Display;
import org.usfirst.frc.team4999.lights.Packet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UnitTestDisplay implements Display {

    private static class DifferenceShower {

        private static Object lock = new Object();

        public static void showDifference(Color[] buff1, Color[] buff2) {
            final int pixel_size = 20;
            final int txt_offset = 4;
            JComponent component = new JComponent() {
                @Override
                public void paintComponent(Graphics gd) {
                    Graphics2D g = (Graphics2D) gd;
                    g.setPaint(Color.BLACK);
                    for(int i = 0; i < buff1.length; i++) {
                        Rectangle rect = new Rectangle(i * pixel_size, 0, pixel_size, pixel_size);
                        String num = Integer.toString(i);
                        g.draw(rect);
                        g.drawString(num, i*pixel_size + txt_offset, pixel_size - txt_offset);
                    }
                    for(int i = 0; i < buff1.length; i++) {
                        Rectangle rect = new Rectangle(i * pixel_size, pixel_size, pixel_size, pixel_size);
                        g.setPaint(buff1[i]);
                        g.fill(rect);
                    }
                    for(int i = 0; i < buff2.length; i++) {
                        Rectangle rect = new Rectangle(i * pixel_size, pixel_size * 2, pixel_size, pixel_size);
                        g.setPaint(buff2[i]);
                        g.fill(rect);
                    }
                }
            };
            component.setPreferredSize(new Dimension(Math.max(buff1.length, buff2.length) * pixel_size, pixel_size * 3));

            JFrame frame = new JFrame();
            frame.add(component);
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.pack();
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent arg) {
                    synchronized(lock) {
                        frame.setVisible(false);
                        lock.notifyAll();
                    }
                }
            });

            frame.setVisible(true);

            synchronized(lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            frame.dispose();
        }
    }

    public final SwingDisplay window;
    private Vector<Color[]> displayHistory;

    private static final String animationFileLocation = "animationFiles";

    public UnitTestDisplay(int numPixels) {
        window = new SwingDisplay(numPixels);
        displayHistory = new Vector<Color[]>();
    }

    @Override
    public void show(Packet[] commands) {
        window.show(commands);

        displayHistory.add(window.getCurrentDisplayBuff());
    }

    public void writeToFile(String filename) {
        FileOutputStream fileOut = null;
        ObjectOutputStream objectOut = null;
        try {
            fileOut = new FileOutputStream(Path.of(animationFileLocation,filename).toString());
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(displayHistory);
            objectOut.close();
            fileOut.close();
            System.out.println("The display history was succesfully saved");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
            if(fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        }
    }

    @SuppressWarnings("unchecked")
    public boolean compareToFile(String filename) {
        FileInputStream fileIn = null;
        ObjectInputStream objectIn = null;
        Vector<Color[]> readHistory = null;
        try {
            fileIn = new FileInputStream(Path.of(animationFileLocation,filename).toString());
            objectIn = new ObjectInputStream(fileIn);
            
            Object obj = objectIn.readObject();
            readHistory = (Vector<Color[]>) obj;

            System.out.println("The display history was succesfully read");
                
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
            if(fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        }

        if(readHistory == null) {
            System.out.println("The display history could not be read");
            return false;
        }

        if(displayHistory.size() != readHistory.size()) {
            return false;
        }

        for(int i = 0; i < displayHistory.size(); i++) {
            Color[] curr = displayHistory.get(i);
            Color[] currKey = readHistory.get(i);

            if(curr.length != currKey.length) {
                DifferenceShower.showDifference(curr, currKey);
                return false;
            }

            for(int j = 0; j < curr.length; j++) {
                if(!curr[j].equals(currKey[j])) {
                    System.out.format("(frame:%d color:%d) %s != %s\n", i, j, curr[j], currKey[j]);
                    DifferenceShower.showDifference(curr, currKey);
                    return false;
                }
            }
        }

        return true;
    }

}
