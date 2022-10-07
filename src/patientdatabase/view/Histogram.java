/*
    Histogram
    by Clarissa Sandejas
    23rd March 2021
*/

package patientdatabase.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.util.Collections.max;

/*
    Adapted from the bar graph code in my COMP0004 CW1 submission
*/

public class Histogram extends JPanel
{
    private final static double HEIGHT_FACTOR = 0.82; // height of tallest bar is 0.82 * height of window
    private final static int X_OFFSET = 50;
    private final static int Y_OFFSET = 25;
    private final static int NUM_TICKS = 10;

    private Graphics2D g2d;
    private final int numBars;
    private final String title;
    private final ArrayList<String> xVals;
    private final ArrayList<Integer> yVals;
    private final Color[] colors = { Color.GREEN, Color.CYAN, Color.YELLOW, Color.MAGENTA,
            Color.ORANGE, Color.BLUE, Color.GRAY, Color.RED, Color.PINK};

    public Histogram(ArrayList<String> xVals, ArrayList<Integer> yVals, String title)
    {
         this.xVals = xVals;
         this.yVals = yVals;
         this.title = title;
         this.numBars = xVals.size();
         setPreferredSize(new Dimension(550,350));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight() - Y_OFFSET;
        double pxPerY = (HEIGHT_FACTOR * height) / max(yVals);
        int barWidth = (width - X_OFFSET) / (numBars + 1);

        drawGridLines(width, height, g);
        drawLabeledBars(barWidth, height, pxPerY, g);
        drawAxes(width, height, g);
    }

    private void drawLabeledBars(int barWidth, int height, double pxPerY, Graphics g)
    {
        for(int count = 0; count < numBars; count++)
        {
            String xVal = xVals.get(count);
            int yVal = yVals.get(count);
            int barHeight = (int) (pxPerY * yVal);
            int barX = (int) (barWidth * (count + 0.5)) + X_OFFSET;
            int barY = height - barHeight;

            g2d.setColor(colors[count % colors.length]);
            g2d.fillRect(barX, barY, barWidth, barHeight);
            g2d.setColor(Color.BLACK);

            barX += barWidth / 2;
            int strWidth = g.getFontMetrics().stringWidth(xVal) / 2;
            g2d.drawString(xVal, barX - strWidth, height + 20);

            strWidth = g.getFontMetrics().stringWidth(String.valueOf(yVal)) / 2;
            g2d.drawString(String.valueOf(yVal), barX - strWidth, barY - 5);
        }
    }

    private void drawAxes(int width, int height, Graphics g)
    {
        var strWidth = g.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (width / 2) - (strWidth / 2), 10);
        g2d.setStroke(new BasicStroke(2f));
        g.drawLine(X_OFFSET, Y_OFFSET, X_OFFSET, height);
        g.drawLine(X_OFFSET, height, width, height);
    }

    private void drawGridLines(int width, int height, Graphics g)
    {
        int valIncrement = (int) (max(yVals) / (HEIGHT_FACTOR * (NUM_TICKS + 1)));
        int yIncrement = height / (NUM_TICKS + 1);
        int y = height;
        for (int count = 1; count <= NUM_TICKS; count++)
        {
            y -= yIncrement;
            g2d.setStroke(new BasicStroke(0.5f));
            g.drawLine(X_OFFSET, y, width, y);
            g.drawString(String.valueOf(valIncrement * count), 0, y + 5);

            g2d.setStroke(new BasicStroke(2f));
            g2d.drawLine(X_OFFSET - 5, y, X_OFFSET, y);
        }
    }
}
