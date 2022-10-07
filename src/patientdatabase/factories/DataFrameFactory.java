package patientdatabase.factories;

import patientdatabase.dataframes.ArrayDataFrame;
import patientdatabase.dataframes.ArrayListDataFrame;
import patientdatabase.dataframes.LinkedListDataFrame;
import patientdatabase.interfaces.DataFrame;

public class DataFrameFactory
{
    public static DataFrame createDataFrame(String type)
    {
        if ("Array".equalsIgnoreCase(type))
            return new ArrayDataFrame();
        else if ("LinkedList".equalsIgnoreCase(type))
            return new LinkedListDataFrame();
        else if ("ArrayList".equalsIgnoreCase(type))
            return new ArrayListDataFrame();
        else
            return createDataFrame();
    }

    public static DataFrame createDataFrame()
    {
        return new ArrayListDataFrame();
    }
}
