package patientdatabase.filehandlers;

import patientdatabase.interfaces.DataFrame;
import patientdatabase.interfaces.FileHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import static patientdatabase.factories.DataFrameFactory.createDataFrame;
import static patientdatabase.view.Constants.DISPLAYED_ROWS;

public class JSONHandler implements FileHandler
{
    private final Pattern start = Pattern.compile("\\{\\s*\".*\"\\s*:\\s*\\[\\s*\\{");
    private final Pattern string = Pattern.compile("\\s*\"[^\"]*\",?\\s*");
    private final Pattern lBracket = Pattern.compile("\\s*\\{\\s*");
    private final Pattern rBracket = Pattern.compile("\\s*},?\\s*");

    private DataFrame dataFrame;
    private ArrayList<String> colNames;

    public DataFrame loadDataFrame(File file, String type) throws IOException
    {
        dataFrame = createDataFrame("type");
        return readToDataFrame(file, dataFrame);
    }

    public DataFrame loadDataFrame(File file) throws IOException
    {
        dataFrame = createDataFrame();
        return readToDataFrame(file, dataFrame);
    }

    public void writeToFile(DataFrame data, String filePath) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write("{\n\t\"patients\":[\n");

        var colCount = data.getColCount();
        int end = Math.min(DISPLAYED_ROWS, colCount);
        for (int c = 1; end < colCount; c++)
        {
            end = Math.min(DISPLAYED_ROWS * c, colCount);
            data.refresh(DISPLAYED_ROWS * (c - 1), end);
            var numCols = end - (DISPLAYED_ROWS * (c - 1));
            writeHelper(writer, data, numCols);
        }

        writer.write("\t]\n}\n");
        writer.flush();
        writer.close();
    }

    private void writeHelper(BufferedWriter writer, DataFrame data, int numCols) throws IOException
    {
        var rowCount = data.getRowCount();
        var header = data.getRowNames();

        for (int col = 0; col < numCols; col++)
        {
            writer.write("\t\t{\n");
            for (int row = 0; row < rowCount; row++)
            {
                writer.write("\t\t\t\"" + header[row] + "\":");
                writer.write("\"" + data.getValue(row,col) + "\"");
                writer.write((row != rowCount - 1) ? ",\n" : "\n");
            }
            writer.write((col != numCols - 1) ? "\t\t},\n" : "\t\t}\n");
        }
    }

    @Override
    public String getValueFromFile(int rowIndex, int colIndex, String filePath)
    {
        return null;
    }

    @Override
    public void getSection(DataFrame dataFrame, int start, int end)
    {
        // this version only loads CSV files for now
    }

    @Override
    public ArrayList<String> getRow(DataFrame dataFrame, int rowIndex)
    {
        return null;
    }

    private DataFrame readToDataFrame(File file, DataFrame dataFrame) throws IOException
    {
        var scanner = new Scanner(file).useDelimiter("[:,\n]+");
        getColumnNames(scanner);
        getRowData(scanner, colNames.size());
        scanner.close();
        dataFrame.setFilePath(file.getAbsolutePath());
        return dataFrame;
    }

    private void getColumnNames(Scanner scanner)
    {
        colNames = new ArrayList<>();
        var rowData = new ArrayList<String>();
        scanner.skip(start);
        while(scanner.hasNext(string))
        {
            colNames.add(scanner.next().replace("\"","").strip());
            rowData.add(scanner.next().replaceAll("[\",]","").strip());
        }
        dataFrame.addColumnNames(colNames);
        dataFrame.addColumn(rowData);
    }

    private void getRowData(Scanner scanner, int rowLength)
    {
        int count = 0;
        while(scanner.skip(rBracket).hasNext(lBracket) && (count++ < 1000))
        {
            var rowData = new ArrayList<String>();
            scanner.skip(lBracket);
            for(int c = 0; c < rowLength; c++)
            {
                scanner.next();
                rowData.add(scanner.next().replaceAll("[\",]","").strip());
            }
            dataFrame.addColumn(rowData);
        }
    }
}


