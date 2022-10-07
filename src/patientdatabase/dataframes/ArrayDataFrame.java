package patientdatabase.dataframes;

import patientdatabase.interfaces.Column;
import patientdatabase.interfaces.DataFrame;
import patientdatabase.interfaces.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static patientdatabase.factories.ColumnFactory.createColumn;
import static patientdatabase.factories.FileHandlerFactory.createFileHandler;

public class ArrayDataFrame implements DataFrame
{

    private String filePath;
    private FileHandler fileHandler;
    private Column[] cols;
    private String[] columnNames;
    private int columnCount;


    public ArrayDataFrame()
    {
        cols = new Column[128];
    }

    public void addColumnNames(List<String> names)
    {
        columnNames = names.toArray(String[]::new);
    }

    public void addColumn(List<String> rowData)
    {
        if (cols[(int)(cols.length * 0.75)] != null)
        {
            var newCols = Arrays.copyOf(cols, cols.length * 2);
            newCols[columnCount] = createColumn("Array", rowData);
            cols = newCols;
        }
        else
            cols[columnCount] = createColumn("Array", rowData);

        columnCount++;
    }

    public String[] getRowNames()
    {
        return columnNames;
    }

    public int getRowCount()
    {
        return columnNames.length;
    }

    public int getColCount()
    {
        return columnCount;
    }

    public void setColCount(int columnCount)
    {
        this.columnCount = columnCount;
    }

    public String getValue(int rowIndex, int colIndex) throws IndexOutOfBoundsException
    {
        return cols[colIndex].getRowValue(rowIndex);
    }

    public String getValueFromFile(int rowIndex, int colIndex) throws IndexOutOfBoundsException, IOException
    {
        return fileHandler.getValueFromFile(rowIndex, colIndex, filePath);
    }

    public void putValue(int rowIndex, int colIndex, String value) throws IndexOutOfBoundsException
    {
        cols[colIndex].setRowValue(rowIndex, value);
    }

    public void addValue(int colIndex, String value) throws IndexOutOfBoundsException
    {
        cols[colIndex].addRowValue(value);
    }

    public String getFilepath()
    {
        return this.filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public void setFileType(String fileType)
    {
        this.fileHandler = createFileHandler(fileType);
    }

    public void refresh(int start, int end) throws IOException
    {
        cols = new Column[128];
        fileHandler.getSection(this, start, end);
    }

    public ArrayList<String> getRow(int rowIndex) throws IOException
    {
        return fileHandler.getRow(this, rowIndex);
    }
}
