package patientdatabase.model;

import java.io.File;
import java.io.IOException;

/*
    Wrapper class for Model
    Switches rows and columns for making the JTable
*/

public class TransposedModel extends Model
{
    public TransposedModel(File file, String type) throws IOException
    {
        super(file, type);
    }

    public String[] getHistogramCols() throws IOException
    {
        return super.getHistogramRows();
    }

    public String[] getColumnNames()
    {
        return super.getRowNames();
    }

    public String getColumnName(int colIndex)
    {
        return super.getRowNames()[colIndex];
    }

    @Override
    public int getRowCount()
    {
        return super.getColumnCount();
    }

    @Override
    public int getColumnCount()
    {
        return super.getRowCount();
    }

    public int getTotalRows()
    {
        return super.getTotalCols();
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex)
    {
        return super.getValueAt(colIndex, rowIndex);
    }

    @Override
    public String countInCol(String text, int index) throws IOException
    {
        return super.countInRow(text, index);
    }

    @Override
    public String countInRow(String text, int index) throws IOException
    {
        return super.countInCol(text, index);
    }
}
