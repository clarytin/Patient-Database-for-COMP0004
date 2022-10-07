package patientdatabase.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface FileHandler
{
    DataFrame loadDataFrame(File file) throws IOException;
    DataFrame loadDataFrame(File file, String type) throws IOException;
    void writeToFile(DataFrame dataFrame, String filePath) throws IOException;
    String getValueFromFile(int rowIndex, int colIndex, String filePath) throws IOException;
    void getSection(DataFrame dataFrame, int start, int end) throws IOException;
    ArrayList<String> getRow(DataFrame dataFrame, int rowIndex) throws IOException;
}
