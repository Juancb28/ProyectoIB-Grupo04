package ec.edu.epn.model;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    public static List<Integer> loadFromXLSX(String filePath) {
        List<Integer> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.NUMERIC) {
                        data.add((int) cell.getNumericCellValue());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}