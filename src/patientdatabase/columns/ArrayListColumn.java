package patientdatabase.columns;
import patientdatabase.interfaces.Column;

import java.util.ArrayList;
import java.util.List;

public class ArrayListColumn implements Column
{
    private final String columnName;
    private final ArrayList<String> rows;

    public ArrayListColumn(List<String> rowData)
    {
        this.columnName = rowData.get(0);
        this.rows = new ArrayList<>(rowData);
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
