package patientdatabase.columns;
import patientdatabase.interfaces.Column;

import java.util.Arrays;
import java.util.List;

public class ArrayColumn implements Column
{
    private final String columnName;
    private String[] rows;

    public ArrayColumn(List<String> rowData)
    {
        this.columnName = rowData.get(0);
        this.rows = rowData.toArray(String[]::new);
    }
    public String getName()
    {
        return columnName;
    }

    public int getSize()
    {
        return rows.length;
    }

    public String getRowValue(int rowIndex) throws IndexOutOfBoundsException
    {
        return rows[rowIndex];
    }

    public void setRowValue(int rowIndex, String value) throws IndexOutOfBoundsException
    {
        rows[rowIndex] = value;
    }

    public void addRowValue(String value)
    {
        var newRows = Arrays.copyOf(rows, rows.length + 1);
        newRows[rows.length] = value;
        rows = newRows;
    }
}
