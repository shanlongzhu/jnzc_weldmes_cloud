package com.shth.das.processdb;

import com.processdb.connection.DBPoint;
import com.processdb.connection.DBRoot;
import com.processdb.connection.DBTable;
import com.processdb.connection.DBase;
import com.processdb.driver.common.ConstantValue;
import com.processdb.driver.common.DatabaseInfo;
import com.processdb.driver.common.PointInfo;
import com.processdb.driver.common.TableInfo;
import com.processdb.driver.dao.DBRealtimeDataDao;
import com.processdb.driver.record.RecordData;
import com.shth.das.common.CommonFunction;
import com.shth.das.common.CommonQueue;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.pojo.jnsx.SxRtDataDb;
import com.shth.das.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * @description: 库，表，点，数据操作方法
 * @author: Shan Long
 * @create: 2021-09-27
 */
@Configuration
@Slf4j
public class DBCreateMethod {

    @Value("${processDB.config.dbName}")
    private String dbName;
    @Value("${processDB.config.otcTableName}")
    private String otcTableName;
    @Value("${processDB.config.sxTableName}")
    private String sxTableName;

    private static final ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * 添加OTC实时数据到ProcessDB的阻塞队列中
     */
    public void addOtcRtDataToProcessDbQueue(JNRtDataDB data) {
        //存储电流的点
        final DBPoint eleDbPoint = getPointByGatherNo(data.getGatherNo(), "ELE");
        if (null != eleDbPoint) {
            setOtcRecordData(eleDbPoint, data.getElectricity().floatValue(), data.getWeldTime());
        }
        //存储电压的点
        final DBPoint volDbPoint = getPointByGatherNo(data.getGatherNo(), "VOL");
        if (null != volDbPoint) {
            setOtcRecordData(volDbPoint, data.getVoltage().floatValue(), data.getWeldTime());
        }
    }

    /**
     * 添加松下实时数据到ProcessDB的阻塞队列
     *
     * @param data 松下实时数据
     */
    public void addSxRtDataToProcessDbQueue(SxRtDataDb data) {
        final String weldCid = Integer.valueOf(data.getWeldCid()).toString();
        //根据设备CID获取电流点对象
        final DBPoint eleDbPoint = getPointByWeldCid(weldCid, "ELE");
        if (null != eleDbPoint) {
            setSxRecordData(eleDbPoint, data.getRealityWeldEle().floatValue(), data.getWeldTime());
        }
        //根据设备CID获取电压点对象
        final DBPoint volDbPoint = getPointByWeldCid(weldCid, "VOL");
        if (null != volDbPoint) {
            setSxRecordData(volDbPoint, data.getRealityWeldVol().floatValue(), data.getWeldTime());
        }
    }

    /**
     * 对象赋值
     *
     * @param dbPoint  点对象
     * @param value    值
     * @param weldTime 时间
     */
    private void setOtcRecordData(DBPoint dbPoint, float value, String weldTime) {
        if (null != dbPoint) {
            RecordData recordData = new RecordData();
            //指定点的ID
            recordData.setPointId(dbPoint.getPointId());
            //标签类型
            recordData.setTagType(dbPoint.getTagType());
            //电压
            recordData.setFloat32(value);
            //时间
            recordData.setTimeStamp(getDateFormat(weldTime));
            //质量
            recordData.setQuality((short) 0);
            CommonQueue.OTC_ADD_PROCESS_DB_QUEUE.offer(recordData);
        }
    }

    /**
     * 对象赋值
     *
     * @param dbPoint  点对象
     * @param value    值
     * @param weldTime 时间
     */
    private void setSxRecordData(DBPoint dbPoint, float value, String weldTime) {
        if (null != dbPoint) {
            RecordData recordData = new RecordData();
            //指定点的ID
            recordData.setPointId(dbPoint.getPointId());
            //标签类型
            recordData.setTagType(dbPoint.getTagType());
            //电压
            recordData.setFloat32(value);
            //时间
            recordData.setTimeStamp(getDateFormat(weldTime));
            //质量（默认0：良好）
            recordData.setQuality((short) 0);
            CommonQueue.SX_ADD_PROCESS_DB_QUEUE.offer(recordData);
        }
    }

    /**
     * 时间格式化
     *
     * @param dateTime 时间
     * @return Date
     */
    private static Date getDateFormat(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            try {
                return dateFormatThreadLocal.get().parse(dateTime);
            } catch (Exception e) {
                log.error("时间转换异常：", e);
            }
        }
        return new Date();
    }

    /**
     * 根据采集编号获取点对象
     *
     * @param gatherNo  采集编号
     * @param pointType 点属性（ele、vol）
     * @return 点对象
     */
    public DBPoint getPointByGatherNo(String gatherNo, String pointType) {
        if (StringUtils.isNotBlank(gatherNo)) {
            gatherNo = CommonUtils.stringLengthJoint(gatherNo, 4);
            //点名
            String pointName = "OTC_" + gatherNo + "_" + pointType;
            //库名
            final String processDbName = CommonFunction.getProcessDbName();
            //OTC表名
            final String processDbOtcTableName = CommonFunction.getProcessDbOtcTableName();
            //根据库名获取库对象
            DBase dBase = DBCreateConnect.getDbConnect().getDBaseByName(processDbName);
            if (null == dBase) {
                //添加库
                dBase = addDataBase(DBCreateConnect.getDbRoot(), processDbName);
            }
            //根据表名获取表对象
            DBTable dbTable = dBase.getDBTableByName(processDbOtcTableName);
            if (null == dbTable) {
                //添加表
                dbTable = addTable(dBase, processDbOtcTableName);
            }
            //根据点名获取点对象
            final DBPoint dbPoint = dbTable.getPointByName(pointName);
            if (null != dbPoint) {
                return dbPoint;
            }
            //添加并返回点
            return addPoint(dbTable, pointName);
        }
        return null;
    }

    /**
     * 根据松下设备CID查询点对象
     *
     * @param weldCid 设备CID
     * @return DBPoint
     */
    public DBPoint getPointByWeldCid(String weldCid, String pointType) {
        if (StringUtils.isNotBlank(weldCid)) {
            weldCid = CommonUtils.stringLengthJoint(weldCid, 4);
            //点名
            String pointName = "SX_" + weldCid + "_" + pointType;
            //库名
            final String processDbName = CommonFunction.getProcessDbName();
            //松下表名
            final String processDbSxTableName = CommonFunction.getProcessDbSxTableName();
            //根据库名获取库对象
            DBase dBase = DBCreateConnect.getDbConnect().getDBaseByName(processDbName);
            if (null == dBase) {
                //添加库
                dBase = addDataBase(DBCreateConnect.getDbRoot(), processDbName);
            }
            //根据表名获取表对象
            DBTable dbTable = dBase.getDBTableByName(processDbSxTableName);
            if (null == dbTable) {
                //添加表
                dbTable = addTable(dBase, processDbSxTableName);
            }
            //根据点名获取点对象
            final DBPoint dbPoint = dbTable.getPointByName(pointName);
            if (null != dbPoint) {
                return dbPoint;
            }
            //添加并返回点
            return addPoint(dbTable, pointName);
        }
        return null;
    }

    /**
     * 添加库，表
     */
    public void addDbaseTablePoint() {
        final DBRoot dbRoot = DBCreateConnect.getDbRoot();
        final DBase dBase = addDataBase(dbRoot, dbName);
        if (null != dBase) {
            addTable(dBase, otcTableName);
            addTable(dBase, sxTableName);
        }
    }

    /**
     * 添加数据库
     *
     * @param dbRoot       用户
     * @param dataBaseName 数据库名
     * @return 库对象
     */
    public static DBase addDataBase(DBRoot dbRoot, String dataBaseName) {
        if (null != dbRoot && StringUtils.isNotBlank(dataBaseName)) {
            final DBase dBase = dbRoot.getDBaseByName(dataBaseName);
            if (dBase != null) {
                log.warn("------" + dataBaseName + ":数据库已存在------");
                return dBase;
            }
            DatabaseInfo databaseInfo = new DatabaseInfo();
            databaseInfo.setDatabaseName(dataBaseName);
            final int add = dbRoot.add(databaseInfo);
            if (add != 0) {
                log.error("添加数据库：{} 失败,错误码：{}", dataBaseName, add);
            } else {
                log.info("添加数据库：{} 成功", dataBaseName);
                return dbRoot.getDBaseByName(dataBaseName);
            }
        }
        return null;
    }

    /**
     * 添加表
     *
     * @param dBase     指定数据库
     * @param tableName 表名
     * @return 表对象
     */
    public static DBTable addTable(DBase dBase, String tableName) {
        if (null != dBase && StringUtils.isNotBlank(tableName)) {
            final DBTable dbTable = dBase.getDBTableByName(tableName);
            if (dbTable != null) {
                log.warn("------" + tableName + ":表已存在------");
                return dbTable;
            }
            final TableInfo tableInfo = new TableInfo();
            tableInfo.setTableName(tableName);
            final int add = dBase.add(tableInfo);
            if (add != 0) {
                log.error("添加表：{} 失败,错误码：{}", tableName, add);
            } else {
                log.info("添加表：{} 成功", tableName);
                return dBase.getDBTableByName(tableName);
            }
        }
        return null;
    }

    /**
     * 添加点
     *
     * @param dbTable   指定表
     * @param pointName 点名
     */
    public DBPoint addPoint(DBTable dbTable, String pointName) {
        if (null != dbTable && StringUtils.isNotBlank(pointName)) {
            final DBPoint dbPoint = dbTable.getPointByName(pointName);
            if (dbPoint != null) {
                log.warn("------" + pointName + ":点已存在------");
                return dbPoint;
            }
            PointInfo pointInfo = new PointInfo();
            pointInfo.setPointName(pointName);//点名
            pointInfo.setPointType(ConstantValue.POINT_PHY);//采样点（点类型）
            pointInfo.setUnit("N/A");//单位
            pointInfo.setDataType(ConstantValue.TAG_TYPE_FLOAT32);//数据类型
            pointInfo.setIOType(ConstantValue.IO_TYPE_INPUT);//io类型
            pointInfo.setBottomScale(0.0F);//量程低限
            pointInfo.setTopScale(200.0F);//量程高限
            pointInfo.setArchived(ConstantValue.POINT_ARCHIVED_YES);//设置存入历史库
            //pointInfo.setCompress(ConstantValue.POINT_COMPRESS_DEAD);//采用死区压缩算法
            //pointInfo.setDevType(ConstantValue.POINT_DEVTYPE_PERCENT);//选择按百分比进行判断是否被压缩
            //pointInfo.setDeviation(0.05f);//压缩死区大小，这和上一行强相关，如果选择百分百压缩，表示变换率在0.05%变化之内会被丢弃掉
            final int add = dbTable.add(pointInfo);
            if (add != 0) {
                log.error("添加点：{} 失败,错误码：{}", pointName, add);
            } else {
                log.info("添加点：{} 成功", pointName);
                return dbTable.getPointByName(pointName);
            }
        }
        return null;
    }

    /**
     * 添加数据
     *
     * @param vector 数据集合
     */
    public static void addPointData(Vector<RecordData> vector) {
        try {
            if (vector != null && vector.size() > 0) {
                final DBRealtimeDataDao dbRealtimeDataDao = DBCreateConnect.getDbRealtimeDataDao();
                if (null != dbRealtimeDataDao) {
                    final int insertRealtimeData = dbRealtimeDataDao.insertRealtimeData(vector);
                    if (insertRealtimeData != 0) {
                        log.error("添加数据失败,错误码：{}", insertRealtimeData);
                    }
                }
            }
        } catch (Exception e) {
            log.error("添加数据到ProcessDB异常：", e);
        }
    }

}
