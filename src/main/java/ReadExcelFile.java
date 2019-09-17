import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ReadExcelFile {
    public static final String SAMPLE_XLSX_FILE_PATH = "/home/manjunath/Desktop/Codes/Cloud/Comm sheet.xlsx";

    public static void main(String[] args) throws IOException, InvalidFormatException {
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        /*
           =============================================================
           Iterating over all the sheets in the workbook (Multiple ways)
           =============================================================
        */

        // 1. You can obtain a sheetIterator and iterate over it
    /*    Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        System.out.println("Retrieving Sheets using Iterator");
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            System.out.println("=> " + sheet.getSheetName());
        }*/

        // 2. Or you can use a for-each loop
        System.out.println("Retrieving Sheets using for-each loop");
        for(Sheet sheet: workbook) {
            System.out.println("=> " + sheet.getSheetName());
        }

        // 3. Or you can use a Java 8 forEach with lambda
   /*     System.out.println("Retrieving Sheets using Java 8 forEach with lambda");
        workbook.forEach(sheet -> {
            System.out.println("=> " + sheet.getSheetName());
        }); */

        /*
           ==================================================================
           Iterating over all the rows and columns in a Sheet (Multiple ways)
           ==================================================================
        */

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        // 1. You can obtain a rowIterator and columnIterator and iterate over them
      /*  System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
            }
            System.out.println();
        }
			*/
        // 2. Or you can use a for-each loop to iterate over the rows and columns
        
        Map<String, Integer> map = new HashMap<String,Integer>(); //Create map
        Row row = sheet.getRow(0); //Get first row
        //following is boilerplate from the java doc
        short minColIx = row.getFirstCellNum(); //get the first column index for a row
        short maxColIx = row.getLastCellNum(); //get the last column index for a row
        for(short colIx=minColIx; colIx<maxColIx; colIx++) { //loop from first to last index
           Cell cell = row.getCell(colIx); //get the cell
           map.put(cell.getStringCellValue(),cell.getColumnIndex()); //add the cell contents (name of column) and cell index to the map
         }
        System.out.println(map.get("Tek Order #"));
        
        int ordercol = map.get("Tek Order #");
        int Netprice = map.get("Net price");
        int Commn = map.get("Commn %");
        int Commnpayable = map.get("Commn payable");

        	
               ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                for (Row row1: sheet) {
                	ArrayList<String> col = new ArrayList<String>();
                    for(Cell cell: row1) {
                        String cellValue = dataFormatter.formatCellValue(cell);
                        col.add(cellValue);
                    }
                    list.add(col);
                }
                workbook.close();
                System.out.println(list);
                ArrayList<ArrayList> det = new ArrayList<ArrayList>();
        	int len = list.size();
        	for(int i=1;i<list.size();i++) {
                ArrayList info = new ArrayList();
        		ArrayList<String> a= list.get(i);
        				String ord = a.get(ordercol);
        				String netprice = a.get(Netprice);
        				String comm = a.get(Commn);
        				double net = convert(netprice);
        				double com = convert(comm);
        				double commPayable =  (net * com /100);
        				info.add(ord);
        				info.add(commPayable);
        				System.out.println(ord);
        				System.out.println(commPayable);
        				System.out.println(info);
        				det.add(info);
        			}
        System.out.println(det);
      /*  System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
        for (Row row1: sheet) {
            for(Cell cell: row1) {
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
            }
            System.out.println();
        }*/
        // 3. Or you can use Java 8 forEach loop with lambda
     /*   System.out.println("\n\nIterating over Rows and Columns using Java 8 forEach with lambda\n");
        sheet.forEach(row -> {
            row.forEach(cell -> {
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
            });
            System.out.println();
        });
*/
        // Closing the workbook
        workbook.close();
    }

	private static double convert(String string) {
		int len = string.length();
		double value;
		String val = "";
		for(int i=0;i<len;i++) {
			if(string.charAt(i)>=48 && string.charAt(i)<=57 || string.charAt(i)==46) {
				val = val + string.charAt(i);
			}
		}
		value=Double.valueOf(val);
		return value;
		// TODO Auto-generated method stub
		
	}
   
}