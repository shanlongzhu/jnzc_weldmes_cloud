package com.gw.common;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class ExcelUtils {

    public static Object getValue(Cell cell) {
        Object value = null;
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {

                    value = (int)cell.getNumericCellValue();
                }
                break;
        }
        return value;
    }
}
