package patientdatabase.factories;

import patientdatabase.filehandlers.CSVHandler;
import patientdatabase.filehandlers.JSONHandler;
import patientdatabase.interfaces.FileHandler;

public class FileHandlerFactory
{
    public static FileHandler createFileHandler(String type)
    {
        if ("csv".equalsIgnoreCase(type))
            return new CSVHandler();
        else
            return new JSONHandler();
    }
}
