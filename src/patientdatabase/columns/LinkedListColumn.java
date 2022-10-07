package patientdatabase.columns;

import patientdatabase.interfaces.Column;

import java.util.LinkedList;
import java.util.List;

public class LinkedListColumn implements Column
{
    private final String columnName;
    private final LinkedList<String> rows;

    public LinkedListColumn(List<String> rowData)
    {
        this.columnName = rowData.get(0);
        this.rows =  new LinkedList<>(rowData);
    }

    public String getName()
    {
        return columnName;
    }

    public int getSize()
    {
        return rows.size();
    }

    public String getRowValue(int rowIndex) throws IndexOutOfBoundsException
    {
        return rows.get(rowIndex);
    }

    public void setRowValue(int rowIndex, String value) throws IndexOutOfBoundsException
    {
        rows.set(rowIndex, value);
    }

    public void addRowValue(String value)
    {
        rows.add(value);
    }

}
