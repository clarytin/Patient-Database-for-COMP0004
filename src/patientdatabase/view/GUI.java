/*
    GUI for Patient Database
    by Clarissa Sandejas
    23rd March 2021
*/

package patientdatabase.view;

import patientdatabase.model.TransposedModel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import static patientdatabase.view.Constants.*;
import static patientdatabase.factories.FileHandlerFactory.createFileHandler;

public class GUI extends JFrame
{
    private TransposedModel model;
    private JPanel northPanel;
    private JSplitPane splitPane;
    private JPanel mainPanel;
    private JPanel westPanel;
    private JPanel eastPanel;

    private JButton loadButton;
    private JButton saveToJSONButton;
    private JButton saveToCSVButton;

    private JPanel menuItemPanel;
    private JLabel filtersLabel;
    private JScrollPane checkBoxScroller;
    private JPanel checkBoxPanel;
    private JCheckBox[] checkBoxList;

    private JPanel selectionPanel;
    private JButton selectButton;
    private JButton deselectButton;

    private JPanel utilitiesPanel;
    private JLabel utilitiesLabel;
    private JPanel histoAuxPanel;
    private JPanel counterAuxPanel;
    private JLabel countLabel;
    private JLabel histogramLabel;

    private JPanel extremesAuxPanel;
    private JLabel extremesLabel;
    private JPanel extremesPanel;
    private JLabel extremesResultLabel;
    private JComboBox<String> extremesDropDown;
    private JComboBox<String> firstOrLast;
    private JPanel counterPanel;
    private JComboBox<String> counterDropDown;
    private JTextField counterSB;
    private JLabel resultLabel;
    private JPanel histogramPanel;
    private JComboBox<String> histogramDropDown;
    private JButton histogramButton;

    private JTextField searchBar;
    private JScrollPane tableScroller;
    private JTable table;
    private TableRowSorter<TableModel> rowSorter;
    private JPanel pagePanel;
    private JLabel pageLabel;
    private JTextField pageTextField;
    private JButton prevPageButton;
    private JButton nextPageButton;

    private Font regularFont;
    private Font boldFont;


    public GUI()
    {
        setTitle("Patient Database");
        createMainPanel();
        makeItPretty();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createMainPanel()
    {
        createNorthPanel();
        createSplitPane();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void createNorthPanel()
    {
        createFileButtons();
        northPanel = new JPanel(new FlowLayout());
        northPanel.add(loadButton);
        northPanel.add(saveToJSONButton);
        northPanel.add(saveToCSVButton);
    }

    private void createFileButtons()
    {
        loadButton = new JButton("Load File");
        loadButton.addActionListener((ActionEvent e) ->
        {
            if (loadFile())
                updateGUI();
        });
        saveToJSONButton = new JButton("Save to JSON");
        saveToJSONButton.addActionListener((ActionEvent e) -> saveToFile("JSON"));
        saveToJSONButton.setEnabled(false);

        saveToCSVButton = new JButton("Save to CSV");
        saveToCSVButton.addActionListener((ActionEvent e) -> saveToFile("CSV"));
        saveToCSVButton.setEnabled(false);
    }

    private void createSplitPane()
    {
        createLeftPanel();
        createRightPanel();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel, eastPanel);
        splitPane.setDividerLocation(250);
        splitPane.setPreferredSize(new Dimension(900,600));
        splitPane.setEnabled(true);
    }

    private void createLeftPanel()
    {
        createMenuItemPanel();
        createUtilitiesPanel();

        westPanel = new JPanel(new BorderLayout());
        westPanel.add(menuItemPanel, BorderLayout.CENTER);
        westPanel.add(utilitiesPanel, BorderLayout.SOUTH);
    }

    private void createMenuItemPanel()
    {
        createCheckBoxScroller();
        createSelectionPanel();

        menuItemPanel = new JPanel(new BorderLayout());
        filtersLabel = new JLabel("Filters", SwingConstants.CENTER);
        menuItemPanel.add(filtersLabel, BorderLayout.NORTH);
        menuItemPanel.add(checkBoxScroller, BorderLayout.CENTER);
        menuItemPanel.add(selectionPanel, BorderLayout.SOUTH);
    }

    private void createUtilitiesPanel()
    {
        createAuxPanels();

        utilitiesLabel = new JLabel("Utilities", SwingConstants.CENTER);
        utilitiesPanel = new JPanel(new BorderLayout());

        JPanel utilitiesAuxPanel = new JPanel(new BorderLayout());
        utilitiesAuxPanel.add(extremesAuxPanel, BorderLayout.NORTH);
        utilitiesAuxPanel.add(counterAuxPanel, BorderLayout.CENTER);
        utilitiesAuxPanel.add(histoAuxPanel, BorderLayout.SOUTH);

        utilitiesPanel.add(utilitiesLabel, BorderLayout.NORTH);
        utilitiesPanel.add(utilitiesAuxPanel, BorderLayout.CENTER);
    }

    private void createAuxPanels()
    {
        createExtremesPanel();
        createCounterPanel();
        createHistogramPanel();

        histoAuxPanel = new JPanel(new BorderLayout());
        histoAuxPanel.add(histogramLabel, BorderLayout.NORTH);
        histoAuxPanel.add(histogramPanel);
        counterAuxPanel = new JPanel(new BorderLayout());
        counterAuxPanel.add(countLabel, BorderLayout.NORTH);
        counterAuxPanel.add(counterPanel);
        extremesAuxPanel = new JPanel(new BorderLayout());
        extremesAuxPanel.add(extremesLabel, BorderLayout.NORTH);
        extremesAuxPanel.add(extremesPanel);
    }

    private void createExtremesPanel()
    {
        extremesLabel = new JLabel("GET FIRST/LAST VALUE", SwingConstants.CENTER);
        extremesResultLabel = new JLabel();

        extremesDropDown = new JComboBox<>();
        extremesDropDown.addActionListener(new ExtremesActionListener());
        extremesDropDown.setEnabled(false);
        firstOrLast = new JComboBox<>(new String[]{"First value", "Last value"});
        firstOrLast.addActionListener(new ExtremesActionListener());
        firstOrLast.setEnabled(false);

        extremesPanel = new JPanel(new GridLayout(3, 2));
        extremesPanel.add(new JLabel("Choose Column:"));
        extremesPanel.add(extremesDropDown);
        extremesPanel.add(new JLabel("First or Last"));
        extremesPanel.add(firstOrLast);
        extremesPanel.add(new JLabel("Count: "));
        extremesPanel.add(extremesResultLabel);
    }

    private void createHistogramPanel()
    {
        histogramLabel = new JLabel("GENERATE HISTOGRAM", SwingConstants.CENTER);
        histogramDropDown = new JComboBox<>();
        histogramDropDown.setEnabled(false);
        histogramButton = new JButton("Make Graph");
        histogramButton.addActionListener((ActionEvent e) ->
        {
            try
            {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                createHistogram();
            } catch (IOException ioException)
            {
                showLoadFileError();
                ioException.printStackTrace();
            }
            this.setCursor(Cursor.getDefaultCursor());
        });
        histogramButton.setEnabled(false);

        histogramPanel = new JPanel(new GridLayout(2, 2));
        histogramPanel.add(new JLabel("Choose Column:"));
        histogramPanel.add(histogramDropDown);
        histogramPanel.add(new JLabel());
        histogramPanel.add(histogramButton);
    }

    private void createCounterPanel()
    {
        countLabel = new JLabel("COUNT INSTANCES IN COLUMN", SwingConstants.CENTER);
        resultLabel = new JLabel();

        counterDropDown = new JComboBox<>();
        counterDropDown.setEnabled(false);
        createCounterSearchBar();

        counterPanel = new JPanel(new GridLayout(3, 2));
        counterPanel.add(new JLabel("Choose Column:"));
        counterPanel.add(counterDropDown);
        counterPanel.add(new JLabel("Text to Match:"));
        counterPanel.add(counterSB);
        counterPanel.add(new JLabel("Count: "));
        counterPanel.add(resultLabel);
    }

    private void createCounterSearchBar()
    {
        String placeholderText = "Match text";
        counterSB = new JTextField(placeholderText);
        counterSB.addFocusListener(new SBFocusListener(counterSB, placeholderText));
        counterSB.addActionListener((ActionEvent e) ->
        {
            String text = counterSB.getText();
            int columnIndex = counterDropDown.getSelectedIndex();
            try
            {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                resultLabel.setText(model.countInCol(text, columnIndex));
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            this.setCursor(Cursor.getDefaultCursor());
        });
        counterSB.setForeground(Color.DARK_GRAY);
        counterSB.setEnabled(false);
    }

    private void createSelectionPanel()
    {
        selectButton = new JButton("Select All");
        selectButton.setEnabled(false);
        selectButton.addActionListener((ActionEvent e) ->
        {
            for (var box : checkBoxList)
                box.setSelected(true);
        });
        deselectButton = new JButton("Deselect All");
        deselectButton.setEnabled(false);
        deselectButton.addActionListener((ActionEvent e) ->
        {
            for (var box : checkBoxList)
                box.setSelected(false);
        });
        selectionPanel = new JPanel(new FlowLayout());
        selectionPanel.add(selectButton);
        selectionPanel.add(deselectButton);
    }

    private void createCheckBoxScroller()
    {
        checkBoxPanel = new JPanel(new GridLayout(0,1));
        checkBoxScroller = new JScrollPane(checkBoxPanel);
        checkBoxScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        checkBoxScroller.setPreferredSize(new Dimension(150,300));
    }

    /*----------------- Right Panel Code -------------------*/

    private void createRightPanel()
    {
        createTableScroller();
        createPagePanel();

        String placeholderText = "Search this section\u200B";
        searchBar = new JTextField(placeholderText);
        searchBar.addFocusListener(new SBFocusListener(searchBar, placeholderText));
        searchBar.getDocument().addDocumentListener(new SBDocuListener(placeholderText));
        searchBar.setEnabled(false);
        searchBar.setForeground(Color.DARK_GRAY);

        eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(searchBar, BorderLayout.NORTH);
        eastPanel.add(tableScroller, BorderLayout.CENTER);
        eastPanel.add(pagePanel, BorderLayout.SOUTH);
    }

    private void createPagePanel()
    {
        pageLabel = new JLabel("of          ");
        pageTextField = new JTextField();
        pageTextField.setColumns(3);

        prevPageButton = new JButton("<");
        nextPageButton = new JButton(">");
        prevPageButton.addActionListener(new PrevPageActionListener());
        nextPageButton.addActionListener(new NextPageActionListener());

        pagePanel = new JPanel(new FlowLayout());
        pagePanel.add(prevPageButton);
        pagePanel.add(new JLabel("Page"));
        pagePanel.add(pageTextField);
        pagePanel.add(pageLabel);
        pagePanel.add(nextPageButton);

        pageTextField.setEnabled(false);
        nextPageButton.setEnabled(false);
        prevPageButton.setEnabled(false);
    }

    private void createTableScroller()
    {
        table = new prettyTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(28);
        rowSorter = new TableRowSorter<>(table.getModel());
        tableScroller = new JScrollPane(table);
        tableScroller.setMinimumSize(table.getSize());
    }

    private void createHistogram() throws IOException
    {
        var colName = (String) histogramDropDown.getSelectedItem();
        var data = model.getHistogramData(colName);
        var xVals = new ArrayList<String>();
        var yVals = new ArrayList<Integer>();
        data.forEach((k,v) -> {xVals.add(k); yVals.add(v);});
        for (int c = 0; c < xVals.size(); c++)
        {
            if (xVals.get(c).equals("")) xVals.set(c, "No data");
        }
        JFrame histogram = new JFrame();
        histogram.add(new Histogram(xVals, yVals, colName + " HISTOGRAM"));
        histogram.pack();
        histogram.setLocationRelativeTo(null);
        histogram.setVisible(true);
        var border = BorderFactory.createEmptyBorder(10,10,10,10);
        histogram.getRootPane().setBorder(border);
    }

    private void saveToFile(String type)
    {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        var typeLower = type.toLowerCase();
        var chooser = new JFileChooser("output/");
        var filter = new FileNameExtensionFilter(type, typeLower);
        chooser.setFileFilter(filter);

        int returnVal = chooser.showSaveDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION)
            return;

        try
        {
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            filePath = (filePath.endsWith("." + typeLower)) ? filePath : filePath + "." + typeLower;
            createFileHandler(type).writeToFile(model.getDataFrame(), filePath);
            showFileSaved();
        }
        catch (IOException exp)
        {
            showSaveFileError();
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    private Boolean loadFile()
    {
        var chooser = new JFileChooser("res/data/");
        var filter = new FileNameExtensionFilter("CSV", "csv");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) return false;

        try
        {
            var type = chooser.getSelectedFile().getName().endsWith("csv") ? "csv" : "json";
            model = new TransposedModel(chooser.getSelectedFile(), type);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            setupTable();
        }
        catch (IOException e)
        {
            showLoadFileError();
            return false;
        }
        catch (IndexOutOfBoundsException e)
        {
            showImpossibleError();
            return false;
        }
        return true;
    }


    private void updateGUI()
    {
        tableScroller.setVisible(true);
        pageLabel.setText(" of " + model.getLastPage());
        pageTextField.addActionListener(new PageTextActionListener());
        pageTextField.setText("1");
        enableThings();
        setupCheckBoxes();
        setupDropDowns();

        makeItPretty();
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void enableThings()
    {
        nextPageButton.setEnabled(model.getLastPage() != 1);
        pageTextField.setEnabled(true);
        searchBar.setEnabled(true);
        counterSB.setEnabled(true);
        counterDropDown.setEnabled(true);
        histogramDropDown.setEnabled(true);
        saveToJSONButton.setEnabled(true);
        saveToCSVButton.setEnabled(true);
        selectButton.setEnabled(true);
        deselectButton.setEnabled(true);
        histogramButton.setEnabled(true);
        pageTextField.setEnabled(true);
        extremesDropDown.setEnabled(true);
        firstOrLast.setEnabled(true);
    }

    private void setupTable()
    {
        tableScroller.setVisible(false);
        table.setModel(model);

        for (int c = 0; c < model.getColumnCount(); c++)
            table.getColumnModel().getColumn(c).setMinWidth(CELL_WIDTH);

        rowSorter.setModel(table.getModel());
        table.setRowSorter(rowSorter);
    }

    private void setupCheckBoxes()
    {
        checkBoxPanel.removeAll();
        int numBoxes = model.getColumnCount();
        checkBoxList = new JCheckBox[numBoxes];
        for (int c = 0; c < numBoxes; c++)
        {
            checkBoxList[c] = new JCheckBox(model.getColumnName(c));
            checkBoxList[c].setName(model.getColumnName(c));
            checkBoxList[c].setSelected(true);
            checkBoxList[c].addItemListener(new CBItemListener());
            checkBoxList[c].setBackground(COSMIC_LATTE);
            checkBoxPanel.add(checkBoxList[c]);
        }
    }

    private void setupDropDowns()
    {
        counterDropDown.setModel(new DefaultComboBoxModel<>(model.getColumnNames()));
        extremesDropDown.setModel(new DefaultComboBoxModel<>(model.getColumnNames()));
        try
        {
            histogramDropDown.setModel(new DefaultComboBoxModel<>(model.getHistogramCols()));
        } catch (IOException e)
        {
            showLoadFileError();
            e.printStackTrace();
        }
    }

    /*----------------- Checkbox Event Code -------------------*/

    private void addColumn(String checkBoxName)
    {
        TableColumnModel curr = table.getColumnModel();
        TableColumnModel full = new JTable(model).getColumnModel();
        var leftHeaders = new ArrayList<String>();

        for (int c = 0; c < full.getColumnCount(); c++)
        {
            var header = (String) full.getColumn(c).getHeaderValue();
            if (header.equals(checkBoxName))
            {
                curr.addColumn(full.getColumn(c));
                curr.getColumn(curr.getColumnCount() - 1).setMinWidth(CELL_WIDTH);
                break;
            }
            leftHeaders.add(header);
        }
        moveToOrigPosition(leftHeaders, curr);
    }

    private void removeColumn(String checkBoxName)
    {
        TableColumnModel tcm = table.getColumnModel();
        for (int c = 0; c < tcm.getColumnCount(); c++)
        {
            var header = (String) tcm.getColumn(c).getHeaderValue();
            if (header.equals(checkBoxName))
            {
                tcm.removeColumn(tcm.getColumn(c));
                return;
            }
        }
    }

    private void moveToOrigPosition(ArrayList<String> leftHeaders, TableColumnModel curr)
    {
        for (int c = 0; c < curr.getColumnCount(); c++)
        {
            String header = (String) curr.getColumn(c).getHeaderValue();
            if (!leftHeaders.contains(header))
            {
                curr.moveColumn(curr.getColumnCount() - 1, c);
                return;
            }
        }
    }

    /*-------------------- Event Listener Classes --------------------*/

    private class ExtremesActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int colIndex = extremesDropDown.getSelectedIndex();
            try
            {
                if (firstOrLast.getSelectedIndex() == 0)
                    extremesResultLabel.setText(model.getFirst(colIndex));
                else
                    extremesResultLabel.setText(model.getLast(colIndex));
            } catch (IOException ioException)
            {
                showLoadFileError();
                ioException.printStackTrace();
            }
        }
    }
    private class PageTextActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
            try {
                var x = Integer.parseInt(pageTextField.getText());
                if (0 < x && x <= model.getLastPage())
                {
                    model.newPage((x - 1) * DISPLAYED_ROWS, x * DISPLAYED_ROWS);
                    model.fireTableDataChanged();
                }
                else
                    throw new NumberFormatException();
                prevPageButton.setEnabled(x != 1);
                nextPageButton.setEnabled(x != model.getLastPage());
            }
            catch (NumberFormatException e)
            {
                showInvalidInputWarning("page number");
            }
            catch (IOException e) {
                showLoadFileError();
            }
            finally {
                pageTextField.setText(String.valueOf((model.getStart() / DISPLAYED_ROWS) + 1));
            }
        }
    }

    private class NextPageActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                model.nextPage();
            }
            catch (IOException ioException)
            {
                showLoadFileError();
            }
            var pageNum = (model.getStart() / DISPLAYED_ROWS) + 1;
            nextPageButton.setEnabled(pageNum != model.getLastPage());
            prevPageButton.setEnabled(true);
            pageTextField.setText(String.valueOf(pageNum));
            model.fireTableDataChanged();
        }
    }

    private class PrevPageActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                model.prevPage();
            }
            catch (IOException ioException)
            {
                showLoadFileError();
            }
            var pageNum = (model.getStart() / DISPLAYED_ROWS) + 1;
            prevPageButton.setEnabled(pageNum != 1);
            nextPageButton.setEnabled(true);
            pageTextField.setText(String.valueOf(pageNum));
            model.fireTableDataChanged();
        }
    }

    private class CBItemListener implements ItemListener
    {
        public void itemStateChanged(ItemEvent e)
        {
            int index = 0;
            int numBoxes = model.getColumnCount();
            Object source = e.getItemSelectable();
            for (int c = 0; c < numBoxes; c++)
            {
                if (source == checkBoxList[c])
                {
                    index = c;
                    break;
                }
            }
            var checkBoxName = checkBoxList[index].getName();
            if (e.getStateChange() == ItemEvent.DESELECTED)
                removeColumn(checkBoxName);
            else
                addColumn(checkBoxName);
        }
    }

    private class SBDocuListener implements DocumentListener
    {
        private final String placeholderText;

        private SBDocuListener(String placeholderText)
        {
            this.placeholderText = placeholderText;
        }

        @Override
        public void insertUpdate(DocumentEvent e) { update(); }

        @Override
        public void removeUpdate(DocumentEvent e) { update(); }

        @Override
        public void changedUpdate(DocumentEvent e) { update(); }

        public void update()
        {
            String text = searchBar.getText();
            if (text.equals(placeholderText))
                rowSorter.setRowFilter(null);
            else
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    // Used this code for placeholder text: https://stackoverflow.com/a/40516250
    private static class SBFocusListener implements FocusListener
    {
        private final JTextComponent component;
        private final String text;

        public SBFocusListener(JTextComponent component, String text) {
            this.component = component;
            this.text = text;
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (component.getText().equals(text)) {
                component.setText("");
                component.setForeground(Color.BLACK);
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (component.getText().isEmpty()) {
                component.setForeground(Color.DARK_GRAY);
                component.setText(text);
            }
        }
    }

    /*------------------- Look & Feel Code -------------------*/

    private static class prettyTable extends JTable
    {
        public boolean getScrollableTracksViewportWidth()
        {
            return getPreferredSize().width < getParent().getWidth();
        }

        public boolean getScrollableTracksViewportHeight()
        {
            return getPreferredSize().height < getParent().getHeight();
        }

        // Used middle part of this answer: https://stackoverflow.com/a/32276414
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
        {
            Component comp = super.prepareRenderer(renderer, row, column);
            ((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
            if (!comp.getBackground().equals(getSelectionBackground()))
                comp.setBackground(row % 2 == 0 ? DARK_PLUM : LIGHT_PLUM);
            return comp;
        }
    }

    private void makeItPretty()
    {
        initializeFonts();
        setFonts(mainPanel);
        setBorders();
        setBackgrounds();
        setTextLook();
        setTableLook();
    }

    private void initializeFonts()
    {
        try
        {
            // Raleway: open-source through SIL Open Font License, Version 1.1
            var is = Files.newInputStream(Paths.get("res/fonts/Raleway-SemiBold.ttf"));
            InputStream fileStream = new BufferedInputStream(is);
            boldFont = Font.createFont(Font.TRUETYPE_FONT, fileStream).deriveFont(14f);

            // Roboto: open-source through Apache License, Version 2.0
            is = Files.newInputStream(Paths.get("res/fonts/Roboto-Regular.ttf"));
            fileStream = new BufferedInputStream(is);
            regularFont = Font.createFont(Font.TRUETYPE_FONT, fileStream).deriveFont(14f);
        }
        catch (FontFormatException | IOException e)
        {
            boldFont = new JLabel().getFont();
            regularFont = new JLabel().getFont();
            e.printStackTrace();
        }
    }

    // Adapted this method for setting fonts: https://stackoverflow.com/a/12731354
    private void setFonts(Component comp)
    {
        if (comp instanceof JButton)
            comp.setFont(boldFont);
        else
            comp.setFont(regularFont);

        if (comp instanceof Container)
            for (var child : ((Container) comp).getComponents()) setFonts(child);
    }

    private void setBorders()
    {
        var borderlessComps = new JComponent[]{mainPanel,
                splitPane, eastPanel, westPanel, tableScroller,
                menuItemPanel, checkBoxScroller, selectionPanel};
        for (var comp : borderlessComps) comp.setBorder(EMPTY_BORDER);

        var sidelessLabels = new JLabel[]{countLabel, histogramLabel, extremesLabel};
        for (var label : sidelessLabels) label.setBorder(SIDELESS_BORDER);

        var toplessPanels = new JPanel[]{counterAuxPanel, histoAuxPanel, extremesAuxPanel};
        for (var panel : toplessPanels) panel.setBorder(TOPLESS_BORDER);

        checkBoxScroller.setBorder(new EmptyBorder(0,5,0,8));
        searchBar.setMargin(new Insets(0,5,0,8));

        filtersLabel.setBorder(COMP_BORDER);
        utilitiesLabel.setBorder(COMP_BORDER);
    }

    private void setBackgrounds()
    {
        var tableHeader = table.getTableHeader();
        var greyComps = new JComponent[]{tableHeader, utilitiesPanel, menuItemPanel};
        for (var comp : greyComps) comp.setBackground(DUSTY_ROSE);

        var whiteComps = new JComponent[]{histogramPanel, counterPanel, extremesPanel, selectionPanel,
                histoAuxPanel, counterAuxPanel, extremesAuxPanel, checkBoxPanel, checkBoxScroller};
        for (var comp : whiteComps) comp.setBackground(COSMIC_LATTE);

        var velvetComps = new JComponent[]{northPanel, table, eastPanel};
        for (var comp : velvetComps) comp.setBackground(DARK_PLUM);
    }

    private void setTextLook()
    {
        var header = table.getTableHeader();
        var boldLabels = new JComponent[]{countLabel, histogramLabel, extremesLabel, header,
                filtersLabel, utilitiesLabel};
        for (var label : boldLabels) label.setFont(boldFont);

        var whiteText = new JComponent[]{filtersLabel, utilitiesLabel, header};
        for (var comp : whiteText) comp.setForeground(Color.WHITE);
    }

    private void setTableLook()
    {
        table.setForeground(Color.WHITE);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);
        table.setRowMargin(0);
        table.setSelectionBackground(COSMIC_LATTE);
    }
}