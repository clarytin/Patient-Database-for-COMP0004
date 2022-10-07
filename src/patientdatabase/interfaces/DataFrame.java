package patientdatabase.interfaces;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface DataFrame
{
    void addColumnNames(List<String> names);
    void addColumn(List<String> rowData);
    String[] getRowNames();
    int getRowCount();
    int getColCount();
    void setColCount(int columnCount);
    String getValue(int rowIndex, int colIndex) throws IndexOutOfBoundsException;
    String getValueFromFile(int rowIndex, int colIndex) throws IndexOutOfBoundsException, IOException;
    void putValue(int rowIndex, int colIndex, String value);
    void addValue(int colIndex, String value);
    String getFilepath();
    void setFilePath(String filePath);
    void refresh(int start, int end) throws IOException;
    void setFileType(String fileType);
    ArrayList<String> getRow(int rowIndex) throws IOException;

}
