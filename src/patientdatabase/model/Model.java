package patientdatabase.model;

import patientdatabase.interfaces.DataFrame;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static patientdatabase.factories.FileHandlerFactory.createFileHandler;
import static patientdatabase.view.Constants.DISPLAYED_ROWS;

public abstract class Model extends AbstractTableModel
{
    private final DataFrame data;
    private int start;
    private int end;

    public Model(File file, String type) throws IOException
    {
        data = createFileHandler(type).loadDataFrame(file);
        start = 0;
        end = DISPLAYED_ROWS;
    }

    public DataFrame getDataFrame()
    {
        return data;
    }

    public String[] getRowNames()
    {
        return data.getRowNames();
    }

    public String getRowName(int colIndex)
    {
        return data.getRowNames()[colIndex];
    }

    public int getRowCount()
    {
        return data.getRowCount();
    }

    public int getColumnCount()
    {
        return Math.min(DISPLAYED_ROWS, this.getTotalCols() - this.start - 1);
    }

    public int getTotalCols()
    {
        return data.getColCount();
    }

    public void nextPage() throws IOException
    {
        this.start = end;
        this.end = Math.min(end + DISPLAYED_ROWS, data.getColCount() - 1);
        data.refresh(start, end);
    }

    public void prevPage() throws IOException
    {
        this.end = start;
        this.start = start - DISPLAYED_ROWS;
        data.refresh(start, end);
    }

    public void newPage(int pageStart, int pageEnd) throws IOException
    {
        this.start = pageStart;
        this.end = Math.min(pageEnd, data.getColCount() - 1);
        data.refresh(start, end);
    }

    public int getLastPage()
    {
        return (int) Math.ceil((float) getTotalCols() / DISPLAYED_ROWS);
    }

    public int getStart()
    {
        return start;
    }

    public int getEnd()
    {
        return end;
    }

    public Object getValueAt(int rowIndex, int colIndex)
    {
        return data.getValue(rowIndex, colIndex);
    }

    public String countInRow(String text, int rowIndex) throws IOException
    {
        int count = 0;
        var row = data.getRow(rowIndex);
        for (String s : row)
        {
            if (text.equals(s))
                count++;
        }
        return String.valueOf(count);
    }

    public String countInCol(String text, int colIndex) throws IOException
    {
        int count = 0;
        for (int c = 0; c < data.getRowCount(); c++)
        {
            if (text.equals(data.getValueFromFile(c, colIndex)))
                count++;
        }

        return String.valueOf(count);
    }

    // returns rows with less than 18 unique values
    public String[] getHistogramRows() throws IOException
    {
        var rows = new ArrayList<String>();
        for (int c = 0; c < data.getRowCount(); c++)
        {
            var count = 0;
            var valid = true;
            var freq = new HashMap<String, Integer>();
            var row = data.getRow(c);
            for (String s : row)
            {
                freq.merge(s, 1, Integer::sum);
                if (freq.get(s) == 1)
                {
                    if (++count > 18)
                    {
                        valid = false;
                        break;
                    }
                }
            }
            if (valid) rows.add(data.getRowNames()[c]);
        }
        return rows.toArray(String[]::new);
    }

    public HashMap<String, Integer> getHistogramData(String rowName) throws IOException
    {
        int rowIndex = getRowIndex(rowName);
        var freq = new HashMap<String, Integer>();
        var row = data.getRow(rowIndex);
        for (String key : row)
            freq.merge(key, 1, Integer::sum);

        return freq;
    }

    public String getFirst(int rowIndex) throws IOException
    {
        var row = data.getRow(rowIndex);
        var newRow = new ArrayList<String>();
        for (String s : row)
        {
            if (!s.equals(""))
                newRow.add(s);
        }

        String min = newRow.get(0);
        for (String s : newRow)
        {
            if (s.compareTo(min) < 0)
                min = s;
        }
        return min;
    }

    public String getLast(int rowIndex) throws IOException
    {
        var row = data.getRow(rowIndex);
        var newRow = new ArrayList<String>();
        for (String s : row)
        {
            if (!s.equals(""))
                newRow.add(s);
        }
        var max = newRow.get(0);
        for (String s : newRow)
        {
            if (!s.equals(""))
            {
                if (s.compareTo(max) > 0)
                    max = s;
            }
        }
        return max;
    }

    private int getRowIndex(String rowName)
    {
        int rowIndex = 0;

        for (int c = 0; c < data.getRowCount(); c++)
        {
            if (data.getRowNames()[c].equals(rowName))
                rowIndex = c;
        }

        return rowIndex;
    }
}
