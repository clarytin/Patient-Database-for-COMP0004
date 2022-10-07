package patientdatabase.filehandlers;

import patientdatabase.interfaces.DataFrame;
import patientdatabase.interfaces.FileHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static patientdatabase.factories.DataFrameFactory.createDataFrame;
import static patientdatabase.view.Constants.DISPLAYED_ROWS;

public class  CSVHandler implements FileHandler
{
    public DataFrame loadDataFrame(File file, String type) throws IOException
    {
        DataFrame dataFrame = createDataFrame(type);
        return readToDataFrame(file, dataFrame);
    }

    public DataFrame loadDataFrame(File file) throws IOException
    {
        DataFrame dataFrame = createDataFrame();
        return readToDataFrame(file, dataFrame);
    }

    public void writeToFile(DataFrame data, String filePath) throws IOException
    {
        var colCount = data.getColCount();
        var headers = data.getRowNames();

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        for (var header : headers)
            writer.write( header + ",");

        int end = Math.min(DISPLAYED_ROWS, colCount);
        for (int c = 1; end < colCount; c++)
        {
            end = Math.min(DISPLAYED_ROWS * c, colCount);
            data.refresh(DISPLAYED_ROWS * (c - 1), end);
            var numCols = end - (DISPLAYED_ROWS * (c - 1));
            writeHelper(writer, data, numCols);
        }
        writer.flush();
        writer.close();
    }

    private void writeHelper(BufferedWriter writer, DataFrame data, int numCols) throws IOException
    {
        int numRows = data.getRowCount();
        for (int col = 0; col < numCols; col++)
        {
            writer.write("\n");
            for (int row = 0; row < numRows; row++)
                writer.write( data.getValue(row,col) + ",");
        }
    }


    public String getValueFromFile(int rowIndex, int colIndex, String filePath) throws IndexOutOfBoundsException, IOException
    {
        var reader = new BufferedReader(new FileReader(new File(filePath)));
        for (int c = 0; c <= colIndex; c++)
            reader.readLine();

        String[] rows = reader.readLine().split(",",-1);
        return rows[rowIndex];
    }

    public void getSection(DataFrame dataFrame, int start, int end) throws IOException
    {
        var file = new File(dataFrame.getFilepath());
        var reader = new BufferedReader(new FileReader(file));
        int curr = 0;

        while (curr++ < start)
            reader.readLine();

        do
        {
            var line = reader.readLine();
            if (line == null) break;
            String[] rows = line.split(",",-1);
            dataFrame.addColumn(new ArrayList<>(Arrays.asList(rows)));
        } while (curr++ <= end);
    }

    public ArrayList<String> getRow(DataFrame dataFrame, int rowIndex) throws IOException
    {
        var file = new File(dataFrame.getFilepath());
        var reader = new BufferedReader(new FileReader(file));
        var row = new ArrayList<String>();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null)
        {
            String[] rows = line.split(",",-1);
            row.add(rows[rowIndex]);
        }

        return row;
    }

    private DataFrame readToDataFrame(File file, DataFrame dataFrame) throws IOException
    {
        var reader = new BufferedReader(new FileReader(file));
        String line;
        if ((line = reader.readLine()) != null)
        {
            String[] rows = line.split(",");
            dataFrame.addColumnNames(new ArrayList<>(Arrays.asList(rows)));
        }
        int count = 0;
        while ((count++ < DISPLAYED_ROWS) && (line = reader.readLine()) != null)
        {
            String[] rows = line.split(",",-1);
            dataFrame.addColumn(new ArrayList<>(Arrays.asList(rows)));
        }

        Stream<String> lines = Files.lines(Path.of(file.getPath()));
        dataFrame.setColCount((int) lines.count());
        dataFrame.setFilePath(file.getAbsolutePath());
        dataFrame.setFileType("csv");

        return dataFrame;
    }
}