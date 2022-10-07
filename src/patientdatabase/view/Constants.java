package patientdatabase.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class Constants
{
    public static final int CELL_WIDTH = 100;
    public static final int DISPLAYED_ROWS = 1000;
    public static final Color DARK_PLUM = new Color(83, 67, 85);
    public static final Color DUSTY_ROSE = new Color(176, 110, 107);
    public static final Color COSMIC_LATTE = new Color(255, 248, 231);
    public static final Color LIGHT_PLUM = new Color(104, 85, 107);
    public static final Border EMPTY_BORDER = new EmptyBorder(0,0,0,0);
    public static final Border SIDELESS_BORDER = new EmptyBorder(5,0,5,0);
    public static final Border TOPLESS_BORDER = new EmptyBorder(0,8,8,8);
    public static final Border ETCHED_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    public static final Border COMP_BORDER = new CompoundBorder(ETCHED_BORDER, SIDELESS_BORDER);

    public static void showFileSaved()
    {
        JOptionPane.showMessageDialog(new JFrame(),
                "File saved successfully",
                "Success", JOptionPane.INFORMATION_MESSAGE);

    }

    public static void showLoadFileError()
    {
        JOptionPane.showMessageDialog(new JFrame(),
                "Error: Couldn't load file. Please try again.",
                "Unable to Load File", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSaveFileError()
    {
        JOptionPane.showMessageDialog(new JFrame(),
                "Error: Couldn't save file. Please try again.",
                "Unable to Save File", JOptionPane.ERROR_MESSAGE);
    }

    public static void showImpossibleError()
    {
        JOptionPane.showMessageDialog(new JFrame(),
                "If you're reading this then I may have goofed.",
                "Impossible Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInvalidInputWarning(String inputType)
    {
        JOptionPane.showMessageDialog(new JFrame(),
                "Please input a valid " + inputType + ".",
                "Invalid Input", JOptionPane.WARNING_MESSAGE);

    }
}
