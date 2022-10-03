package com.gw.service.processdb;

import com.gw.common.CommonUtil;
import com.gw.common.DateTimeUtils;
import com.gw.entities.OtcSxHistoryData;
import com.processdb.connection.DBConnect;
import com.processdb.connection.DBConnectManager;
import com.processdb.connection.DBPoint;
import com.processdb.driver.common.ConnectInfo;
import com.processdb.driver.common.ConstantValue;
import com.processdb.driver.dao.DBHistoryDataDao;
import com.processdb.driver.record.HistoryRecordSet;
import com.processdb.driver.record.PointHistory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ProcessDB历史数据查询
 */
@Slf4j
public class DbHistoryQuery {

    private final static String connectName = "jnzcrtdb_cloud";
    private final static String hostName = "127.0.0.1";
    private final static int hostPort = 8301;
    private final static String userName = "root";
    private final static String password = "root";
    private final static String dbaseName = "JNZCRTDB";
    private final static String otcTable = "OTC";
    private final static String sxTable = "SX";

    /**
     * 根据采集编号和时间段查询ProcessDB历史数据
     *
     * @param gatherNo  采集编号
     * @param startTime 开始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   结束时间 yyyy-MM-dd HH:mm:ss
     * @return MAP
     */
    public Map<String, Object> selectOtcHistoryFromDb(String gatherNo, String startTime, String endTime) {
        Map<String, Object> map = new HashMap<>();
        List<OtcSxHistoryData> otcHistoryList = new ArrayList<>();
        int otcDataNum = 0;
        if (StringUtils.isNotBlank(gatherNo) && StringUtils.isNotBlank(startTime)) {
            //如果结束时间为空，则默认当前系统时间
            if (StringUtils.isBlank(endTime)) {
                endTime = DateTimeUtils.getNowDateTime();
            }
            final String eleGlobalName = getOtcGlobalName(gatherNo, "ele");
            final HistoryRecordSet eleHistoryRecordSet = getHistoryRecordSet(eleGlobalName, startTime, endTime);
            final String volGlobalName = getOtcGlobalName(gatherNo, "vol");
            final HistoryRecordSet volHistoryRecordSet = getHistoryRecordSet(volGlobalName, startTime, endTime);
            if (eleHistoryRecordSet != null && volHistoryRecordSet != null) {
                int recordNum = eleHistoryRecordSet.getRecordNum();
                if (recordNum > volHistoryRecordSet.getRecordNum()) {
                    recordNum = volHistoryRecordSet.getRecordNum();
                }
                otcDataNum = recordNum;
                for (int i = 0; i < recordNum; i++) {
                    OtcSxHistoryData data = new OtcSxHistoryData();
                    data.setElectricity(eleHistoryRecordSet.getFloat32(i));
                    data.setVoltage(volHistoryRecordSet.getFloat32(i));
                    data.setWeldTime(getDateFormat(eleHistoryRecordSet.getTime(i)));
                    otcHistoryList.add(data);
                }
            }
        }
        //数据集合
        map.put("otcHistoryList", otcHistoryList);
        //总条数
        map.put("otcDataNum", otcDataNum);
        return map;
    }

    /**
     * 根据CID和时间段查询ProcessDB历史数据
     *
     * @param weldCid   设备CID
     * @param startTime 开始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   结束时间 yyyy-MM-dd HH:mm:ss
     * @return MAP
     */
    public Map<String, Object> selectSxHistoryFromDb(String weldCid, String startTime, String endTime) {
        Map<String, Object> map = new HashMap<>();
        List<OtcSxHistoryData> sxHistoryList = new ArrayList<>();
        int sxDataNum = 0;
        if (StringUtils.isNotBlank(weldCid) && StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            final String eleGlobalName = getSxGlobalName(weldCid, "ele");
            final HistoryRecordSet eleHistoryRecordSet = getHistoryRecordSet(eleGlobalName, startTime, endTime);
            final String volGlobalName = getSxGlobalName(weldCid, "vol");
            final HistoryRecordSet volHistoryRecordSet = getHistoryRecordSet(volGlobalName, startTime, endTime);
            if (eleHistoryRecordSet != null && volHistoryRecordSet != null) {
                int recordNum = eleHistoryRecordSet.getRecordNum();
                if (recordNum > volHistoryRecordSet.getRecordNum()) {
                    recordNum = volHistoryRecordSet.getRecordNum();
                }
                sxDataNum = recordNum;
                for (int i = 0; i < recordNum; i++) {
                    OtcSxHistoryData data = new OtcSxHistoryData();
                    data.setElectricity(eleHistoryRecordSet.getFloat32(i));
                    data.setVoltage(volHistoryRecordSet.getFloat32(i));
                    data.setWeldTime(getDateFormat(eleHistoryRecordSet.getTime(i)));
                    sxHistoryList.add(data);
                }
            }
        }
        //数据集合
        map.put("sxHistoryList", sxHistoryList);
        //总条数
        map.put("sxDataNum", sxDataNum);
        return map;
    }

    /**
     * 根据时间段和点路径名获取历史数据
     *
     * @param globalName 点的全路径名
     * @param startTime  开始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime    结束时间 yyyy-MM-dd HH:mm:ss
     * @return HistoryRecordSet
     */
    public HistoryRecordSet getHistoryRecordSet(String globalName, String startTime, String endTime) {
        final DBConnect dbConnect = createDbConnect();
        try {
            if (StringUtils.isNotBlank(globalName) && StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                if (dbConnect == null) {
                    log.error("ProcessDB连接创建失败");
                    return null;
                }
                final DBHistoryDataDao dbHistoryDataDao = new DBHistoryDataDao(dbConnect);
                //根据全路径名获取点对象
                final DBPoint dbPoint = dbConnect.getPointByGlobalName(globalName);
                if (dbPoint == null) {
                    log.error("查询点对象为空:{}", globalName);
                    return null;
                }
                //时间间隔：1秒
                int interval = 1;
                Date startDate = getDateFormat(startTime);
                Date endDate = getDateFormat(endTime);
                final PointHistory pointHistory = new PointHistory(dbPoint, startDate, endDate, interval, ConstantValue.HIS_VALUE_SAMPLE);
                final int pointHistoryById = dbHistoryDataDao.requestPointHistoryById(pointHistory);
                if (pointHistoryById != 0) {
                    log.error("查询ProcessDB历史数据为空:{}", pointHistoryById);
                }
                return pointHistory.getSampleRecordSet();
            }
        } catch (Exception e) {
            throw new RuntimeException("查询ProcessDB历史数据异常：", e);
        } finally {
            if (dbConnect != null) {
                dbConnect.close();
            }
        }
        return null;
    }

    /**
     * 根据采集编号和类型获取点全路径名
     *
     * @param gatherNo 采集编号
     * @param type     类型（ELE：电流，VOL：电压）
     * @return 点全路径名（库.表.点）
     */
    public static String getOtcGlobalName(String gatherNo, String type) {
        if (StringUtils.isNotBlank(gatherNo) && StringUtils.isNotBlank(type)) {
            //长度拼接到4位
            gatherNo = CommonUtil.stringLengthJoint(gatherNo, 4);
            //转大写
            type = type.toUpperCase();
            return dbaseName + "." + otcTable + "." + "OTC_" + gatherNo + "_" + type;
        }
        return null;
    }

    /**
     * 根据设备CID和类型获取点全路径名
     *
     * @param weldCid 设备CID
     * @param type    类型（ELE：电流，VOL：电压）
     * @return 点全路径名（库.表.点）
     */
    public static String getSxGlobalName(String weldCid, String type) {
        if (StringUtils.isNotBlank(weldCid) && StringUtils.isNotBlank(type)) {
            //防止多于4位
            weldCid = Integer.valueOf(weldCid).toString();
            //长度拼接到4位
            weldCid = CommonUtil.stringLengthJoint(weldCid, 4);
            //转大写
            type = type.toUpperCase();
            return dbaseName + "." + sxTable + "." + "SX_" + weldCid + "_" + type;
        }
        return null;
    }

    /**
     * 时间格式转换
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return Date
     */
    private static Date getDateFormat(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
            } catch (Exception e) {
                throw new RuntimeException("时间转换异常：", e);
            }
        }
        return new Date();
    }

    /**
     * 时间格式转换
     *
     * @param date Date
     * @return String
     */
    private static String getDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } catch (Exception e) {
            throw new RuntimeException("时间转换异常：", e);
        }
    }

    /**
     * 创建ProcessDB数据库连接
     *
     * @return DBConnect
     */
    private DBConnect createDbConnect() {
        try {
            ConnectInfo host = new ConnectInfo();
            //连接名称
            host.setConnectName(connectName);
            //IP
            host.setHostName(hostName);
            //端口
            host.setHostPort(hostPort);
            //账号
            host.setUserName(userName);
            //密码
            host.setPassword(password);
            return DBConnectManager.getInstance().getConnect(host);
        } catch (Exception e) {
            throw new RuntimeException("创建ProcessDb连接异常：" + e);
        }
    }

}
