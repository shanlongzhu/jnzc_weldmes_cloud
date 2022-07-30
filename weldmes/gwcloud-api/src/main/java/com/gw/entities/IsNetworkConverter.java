package com.gw.entities;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.springframework.stereotype.Component;

@Component
public class IsNetworkConverter implements Converter<Long> {

    @Override
    public Class supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Long convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
        return "是".equals(cellData.getStringValue()) ? 0L : 1L;
    }

    @Override
    public CellData convertToExcelData(Long value, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
        return new CellData("0".equals(value.toString()) ? "是" : "否");
    }

}
