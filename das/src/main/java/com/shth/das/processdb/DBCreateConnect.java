package com.shth.das.processdb;

import com.processdb.connection.DBConnect;
import com.processdb.connection.DBConnectManager;
import com.processdb.connection.DBRoot;
import com.processdb.driver.common.ConnectInfo;
import com.processdb.driver.dao.DBHistoryDataDao;
import com.processdb.driver.dao.DBRealtimeDataDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: processdb实时数据库创建连接存到IOC容器中
 * @author: Shan Long
 * @create: 2021-09-27
 */
@Component
@Slf4j
public class DBCreateConnect {

    private static DBConnect dbConnect;
    private static DBRealtimeDataDao dbRealtimeDataDao;
    private static DBHistoryDataDao dbHistoryDataDao;
    private static DBRoot dbRoot;

    @Value("${processDB.connectName}")
    private String connectName;
    @Value("${processDB.hostName}")
    private String hostName;
    @Value("${processDB.hostPort}")
    private int hostPort;
    @Value("${processDB.userName}")
    private String userName;
    @Value("${processDB.password}")
    private String password;

    public void start() {
        createDbConnect();
        createDbRealtimeDataDao();
        createDbHistoryDataDao();
        createDbRoot();
    }

    private void createDbConnect() {
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
            if (dbConnect == null) {
                dbConnect = DBConnectManager.getInstance().getConnect(host);
            }
        } catch (Exception e) {
            log.error("创建ProcessDb连接异常：" + e);
        }
    }

    private void createDbRealtimeDataDao() {
        if (dbConnect != null) {
            if (dbRealtimeDataDao == null) {
                dbRealtimeDataDao = new DBRealtimeDataDao(dbConnect);
            }
        }
    }

    private void createDbHistoryDataDao() {
        if (dbConnect != null) {
            if (dbHistoryDataDao == null) {
                dbHistoryDataDao = new DBHistoryDataDao(dbConnect);
            }
        }
    }

    private void createDbRoot() {
        if (dbConnect != null) {
            if (dbRoot == null) {
                dbRoot = DBRoot.getInstance(dbConnect);
            }
        }
    }

    public static DBConnect getDbConnect() {
        return dbConnect;
    }

    public static DBRealtimeDataDao getDbRealtimeDataDao() {
        return dbRealtimeDataDao;
    }

    public static DBHistoryDataDao getDbHistoryDataDao() {
        return dbHistoryDataDao;
    }

    public static DBRoot getDbRoot() {
        return dbRoot;
    }

}
