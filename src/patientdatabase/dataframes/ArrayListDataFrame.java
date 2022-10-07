package patientdatabase.dataframes;

import patientdatabase.interfaces.Column;
import patientdatabase.interfaces.DataFrame;
import patientdatabase.interfaces.FileHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static patientdatabase.factories.ColumnFactory.createColumn;
import static patientdatabase.factories.FileHandlerFactory.createFileHandler;

public class ArrayListDataFrame implements DataFrame
{
    private String filePath;
    private FileHandler fileHandler;
    private ArrayList<Column> cols;
    private ArrayList<String> columnNames;
    private int columnCount;

    public ArrayListDataFrame()
    {
        cols = new ArrayList<>();
    }

    public void addColumnNames(List<String> names)
    {
        columnNames = new ArrayList<>(names);
    }

    public void addColumn(List<String> rowData)
    {
        cols.add(createColumn("ArrayList", rowData));
    }

    public String[] getRowNames()
    {
        return columnNames.toArray(new String[0]);
    }

    public int getRowCount()
    {
        return columnNames.size();
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
        return cols.get(colIndex).getRowValue(rowIndex);
    }

    public String getValueFromFile(int rowIndex, int colIndex) throws IndexOutOfBoundsException, IOException
    {
        return fileHandler.getValueFromFile(rowIndex, colIndex, filePath);
    }

    public void putValue(int rowIndex, int colIndex, String value) throws IndexOutOfBoundsException
    {
        cols.get(colIndex).setRowValue(rowIndex, value);
    }

    public void addValue(int colIndex, String value) throws IndexOutOfBoundsException
    {
        cols.get(colIndex).addRowValue(value);
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
        cols = new ArrayList<>();
        fileHandler.getSection(this, start, end);
    }

    public ArrayList<String> getRow(int rowIndex) throws IOException
    {
        return fileHandler.getRow(this, rowIndex);
    }

}
