package patientdatabase.interfaces;
public interface Column
{
    String getName();
    int getSize();
    String getRowValue(int rowIndex);
    void setRowValue(int rowIndex, String value);
    void addRowValue(String value);
}
