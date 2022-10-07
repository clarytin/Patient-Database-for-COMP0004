package patientdatabase.factories;

import patientdatabase.columns.ArrayColumn;
import patientdatabase.columns.ArrayListColumn;
import patientdatabase.columns.LinkedListColumn;
import patientdatabase.interfaces.Column;

import java.util.List;

public class ColumnFactory
{
    public static Column createColumn(String type, List<String> rowData)
    {
        if ("Array".equalsIgnoreCase(type))
            return new ArrayColumn(rowData);
        else if ("LinkedList".equalsIgnoreCase(type))
            return new LinkedListColumn(rowData);
        else if ("ArrayList".equalsIgnoreCase(type))
            return new ArrayListColumn(rowData);
        else
            return createColumn(rowData);
    }

    public static Column createColumn(List<String> rowData)
    {
        return new ArrayListColumn(rowData);
    }
}
