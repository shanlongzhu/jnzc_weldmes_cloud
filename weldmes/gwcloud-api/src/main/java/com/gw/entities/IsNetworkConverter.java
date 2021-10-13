package com.gw.entities;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.springframework.stereotype.Component;

/**
 * @Author zhanghan
 * @Date 2021/10/13 13:51
 * @Description 焊机管理excel导出 是否在网码值转换类
 */
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
    public Long convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {

        return "是".equals(cellData.getStringValue()) ? 0L : 1L;
    }

    @Override
    public CellData convertToExcelData(Long value, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {

        return new CellData(value.equals(0L) ? "是" : "否");
    }
}
