# DAS 数据采集系统架构文档

## 1. 项目概述

### 1.1 项目简介
DAS（Data Acquisition System）是江南焊接制造执行系统的数据采集服务，负责从焊接设备（OTC和松下焊机）实时采集焊接数据，并进行协议解析、数据存储和消息分发。

### 1.2 技术栈

| 技术组件 | 版本 | 说明 |
|---------|------|------|
| Spring Boot | 2.6.11 | 应用框架 |
| Java | 11 | 开发语言 |
| MyBatis Plus | 3.5.3.1 | ORM框架 |
| MySQL | 8.0.33 | 关系型数据库 |
| Druid | 1.2.16 | 数据库连接池 |
| Netty | - | 高性能网络通信框架 |
| MQTT | - | 消息队列协议 |
| ProcessDB | 2.0 | 实时数据库 |
| Spring Boot Admin | 2.6.11 | 应用监控 |
| Undertow | - | Web服务器 |
| Fastjson2 | 2.0.25 | JSON处理 |
| Guava | 31.1-jre | Google工具库 |

### 1.3 项目信息
- **项目名称**：das
- **版本**：0.0.1-SNAPSHOT
- **包名**：com.shth.das
- **服务端口**：9090
- **Netty端口**：OTC(5555)、松下(9002)

---

## 2. 系统架构

### 2.1 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                      DAS 数据采集系统                          │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐       │
│  │   Netty服务   │  │  MQTT客户端   │  │  定时任务     │       │
│  │  (5555/9002) │  │  (1883)      │  │  (Scheduled) │       │
│  └──────────────┘  └──────────────┘  └──────────────┘       │
│         │                  │                  │              │
│  ┌──────▼──────────────────▼──────────────────▼──────┐      │
│  │              业务处理层 (Business)                  │      │
│  │  ┌─────────────┐  ┌─────────────┐                 │      │
│  │  │ 数据上传处理 │  │ 数据下发处理 │                 │      │
│  │  │ (DataUp)    │  │ (DataDown)  │                 │      │
│  │  └─────────────┘  └─────────────┘                 │      │
│  └──────────────────────────────────────────────────┘      │
│         │                  │                               │
│  ┌──────▼──────────────────▼────────────────────────────┐   │
│  │              数据访问层 (Service/Mapper)              │   │
│  │  ┌──────────────┐  ┌──────────────┐                 │   │
│  │  │ weldmesdb    │  │ weldmes-rtdata│                 │   │
│  │  │ (业务数据库)  │  │ (实时数据库)   │                 │   │
│  │  └──────────────┘  └──────────────┘                 │   │
│  └──────────────────────────────────────────────────────┘   │
│         │                  │                               │
│  ┌──────▼──────────────────▼────────────────────────────┐   │
│  │              数据存储层                               │   │
│  │  ┌──────────────┐  ┌──────────────┐  ┌───────────┐  │   │
│  │  │   MySQL      │  │  ProcessDB   │  │   MQTT    │  │   │
│  │  │  (双数据源)   │  │  (实时数据库)  │  │ (消息队列) │  │   │
│  │  └──────────────┘  └──────────────┘  └───────────┘  │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 数据流向

```
焊机设备 → Netty服务 → 协议解析 → 数据处理 → 数据库存储
                    ↓
              MQTT消息发布 → 前端展示
                    ↓
              ProcessDB存储 → 实时数据查询
```

---

## 3. 模块划分

### 3.1 模块结构

```
com.shth.das
├── DasApplication.java          # 启动类
├── business/                    # 业务逻辑层
│   ├── datadown/               # 数据下发处理
│   │   ├── BaseProtocol.java
│   │   ├── ProtocolDownContext.java
│   │   ├── defaultHandler/     # 默认处理器
│   │   ├── otc/               # OTC焊机下发处理
│   │   └── sx/                # 松下焊机下发处理
│   └── dataup/                # 数据上传处理
│       ├── base/              # 基础类
│       ├── otc/               # OTC焊机上传处理
│       └── sx/                # 松下焊机上传处理
├── codeparam/                  # 代码参数配置
├── common/                     # 公共工具类
├── config/                     # 配置类
├── job/                        # 定时任务
├── mqtt/                       # MQTT客户端
├── netty/                      # Netty服务
├── pojo/                       # 数据模型
│   ├── db/                    # 数据库实体
│   ├── jnotc/                 # OTC焊机数据模型
│   └── jnsx/                  # 松下焊机数据模型
├── processdb/                  # ProcessDB操作
├── sys/                        # 系统服务
│   ├── rtdata/                # 实时数据服务
│   └── weldmesdb/             # 业务数据服务
└── util/                       # 工具类
```

### 3.2 核心模块说明

#### 3.2.1 Netty服务模块
- **NettyServer**：Netty服务器主类，启动两个端口分别监听OTC和松下焊机
- **NettyChannelInitializer**：通道初始化器，配置编解码器
- **NettyEncoder**：数据编码器
- **NettyDecoder**：数据解码器
- **NettyServerHandler**：服务端处理器，处理接收到的数据

#### 3.2.2 MQTT客户端模块
- **EmqMqttClient**：MQTT客户端，连接EMQ服务器
- **EmqMqttCallback**：MQTT回调处理，接收下发的控制命令

#### 3.2.3 业务处理模块

**数据上传处理（DataUp）**：
- **BaseAnalysis**：协议解析基类
- **BaseHandler**：数据处理基类
- **BaseUnpack**：数据解包基类
- **JnOtcProtocolHandler**：OTC焊机协议处理器
- **JnOtcProtocolAnalysis**：OTC焊机协议解析
- **JnOtcDecoderUnpack**：OTC焊机数据解包
- **JnSxProtocolHandler**：松下焊机协议处理器
- **JnSxProtocolAnalysis**：松下焊机协议解析
- **JnSxDecoderUnpack**：松下焊机数据解包

**数据下发处理（DataDown）**：
- **BaseProtocol**：下发协议基类
- **DefaultDownHandler**：默认下发处理器
- **OtcDownHandler**：OTC焊机下发处理器
- **SxDownHandler**：松下焊机下发处理器

#### 3.2.4 定时任务模块
- **ScheduledTask**：定时任务类，包含多个定时任务
  - 每天晚上23点创建次日实时数据表
  - 每10分钟更新焊工、焊机、任务等数据
  - 每小时执行数据统计
  - 批量处理队列数据
- **AsyncConfig**：异步任务配置
- **PowerBootJob**：启动任务
- **ShutDownServices**：关闭服务处理

#### 3.2.5 数据访问模块

**实时数据服务（rtdata）**：
- **OtcRtDataService**：OTC实时数据服务
- **SxRtDataService**：松下实时数据服务

**业务数据服务（weldmesdb）**：
- **MachineGatherService**：采集设备服务
- **MachineWeldService**：焊机设备服务
- **TaskService**：任务服务
- **WelderService**：焊工服务
- **StatisticsDataService**：统计数据服务
- **WeldOnOffTimeService**：开关机时间服务

---

## 4. 数据库设计

### 4.1 数据源配置

系统配置了两个MySQL数据源：

| 数据源 | 数据库 | 用途 |
|-------|--------|------|
| ds1 | weldmesdb | 业务数据库，存储焊机、焊工、任务等基础数据 |
| ds2 | weldmes-rtdata | 实时数据库，存储焊接实时数据 |

### 4.2 分表策略

实时数据采用按日期分表策略：
- OTC实时数据表：`otc_rtdata_yyyyMMdd`
- 松下实时数据表：`sx_rtdata_yyyyMMdd`

每天晚上23点自动创建次日数据表。

### 4.3 主要数据表

**业务数据库（weldmesdb）**：
- `gather_model`：采集设备表
- `weld_model`：焊机设备表
- `welder_model`：焊工信息表
- `task_model`：任务表
- `task_claim_issue`：任务领取下发表
- `weld_on_off_time`：开关机时间表
- `otc_weld_statistics_data`：OTC焊接统计数据表
- `sx_weld_statistics_data`：松下焊接统计数据表

**实时数据库（weldmes-rtdata）**：
- `otc_rtdata_yyyyMMdd`：OTC实时数据表（按日期分表）
- `sx_rtdata_yyyyMMdd`：松下实时数据表（按日期分表）

---

## 5. 协议处理

### 5.1 支持的焊机品牌

#### 5.1.1 OTC焊机
- **版本**：OTC 1.0
- **端口**：5555
- **协议类型**：自定义TCP协议
- **支持功能**：
  - 实时数据采集（控制命令：22）
  - 工艺下发（控制命令：52）
  - 工艺索取（控制命令：56）
  - 密码下发（控制命令：53）
  - 控制命令下发（控制命令：54）
  - 锁定/解锁焊机（控制命令：18/19）
  - 程序包路径下发（控制命令：11）

#### 5.1.2 松下焊机
- **系列**：GL5、FR2、AT3
- **端口**：9002
- **协议类型**：自定义TCP协议
- **支持功能**：
  - CO2焊机工艺下发
  - TIG焊机工艺下发
  - 通道设定/读取
  - 工艺索取/删除
  - 通道参数查询/删除/下载

### 5.2 协议处理流程

```
接收数据 → 解码器解码 → 协议解析 → 数据处理 → 数据存储
                    ↓
              MQTT消息发布
```

---

## 6. MQTT消息通信

### 6.1 MQTT配置

| 配置项 | 值 |
|-------|-----|
| Host | localhost |
| Port | 1883 |
| ClientId | jndas |
| Username | admin |
| Password | 123456 |

### 6.2 订阅主题（下行）

系统订阅以下主题接收控制命令：

| 主题 | 说明 |
|-----|------|
| TaskClaimIssue | 任务领取下发 |
| OtcV1ProcessIssue | OTC工艺下发 |
| OtcV1ProcessClaim | OTC工艺索取 |
| OtcV1PasswordIssue | OTC密码下发 |
| OtcV1CommandIssue | OTC控制命令下发 |
| SxGl5Co2ProcessIssue | 松下CO2工艺下发 |
| SxGl5TigProcessIssue | 松下TIG工艺下发 |
| SxGl5WeldChannelSet | 松下通道设定/读取 |
| SxGl5ProcessClaim | 松下工艺索取/删除 |
| SxFr2ChannelParamQuery | 松下通道参数查询/删除 |
| SxFr2ChannelParamDownload | 松下FR2通道参数下载 |
| SxAt3ParamDownload | 松下AT3参数下载 |
| OtcV1IssueProgramPath | OTC程序包路径下发 |

### 6.3 发布主题（上行）

系统发布以下主题发送数据：

| 主题 | 说明 |
|-----|------|
| OtcV1RtData | OTC实时数据 |
| SxRtData | 松下实时数据 |
| OtcV1ProcessReturn | OTC工艺下发返回 |
| OtcV1PasswordReturn | OTC密码下发返回 |
| OtcV1CommandReturn | OTC控制命令返回 |

---

## 7. 定时任务

### 7.1 任务列表

| 任务 | 执行时间 | 说明 |
|-----|---------|------|
| 创建OTC次日表 | 每天23:00 | 创建OTC次日实时数据表 |
| 创建松下次日表 | 每天23:00 | 创建松下次日实时数据表 |
| 更新基础数据 | 每10分钟 | 更新焊工、焊机、任务等数据 |
| OTC数据统计 | 每小时 | 统计OTC焊接数据 |
| 松下数据统计 | 每小时 | 统计松下焊接数据 |
| 批量处理OTC数据 | 每5秒 | 批量处理OTC队列数据 |
| 批量处理松下数据 | 每5秒 | 批量处理松下队列数据 |
| 清除任务绑定 | 每天7:00 | 清除任务绑定信息 |

### 7.2 数据统计逻辑

系统每小时对焊接数据进行统计，包括：
- 焊接时长
- 焊接电流
- 焊接电压
- 焊接次数
- 焊接质量指标

---

## 8. 系统配置

### 8.1 应用配置

**开发环境（application-dev.yml）**：
- 服务端口：9090
- Netty端口：OTC(5555)、松下(9002)
- MQTT端口：1883
- 数据累积条数：100条

### 8.2 功能开关

| 配置项 | 默认值 | 说明 |
|-------|--------|------|
| enableOtcFunction | true | 是否启用OTC业务功能 |
| enableSxFunction | false | 是否启用松下业务功能 |
| slotCardEnableDevice | true | 是否启用刷卡启用设备功能 |
| otcStandbySave | true | OTC待机数据是否存储 |
| sxStandbySave | true | 松下待机数据是否存储 |
| enableProcessDB | false | 是否启用ProcessDB |

### 8.3 Druid监控

系统集成了Druid数据库连接池监控，访问地址：`http://localhost:9090/druid`

### 8.4 Spring Boot Admin

系统集成了Spring Boot Admin监控，访问地址：`http://localhost:9090`

---

## 9. 部署说明

### 9.1 环境要求

- JDK 11+
- MySQL 8.0+
- EMQ X（MQTT Broker）
- ProcessDB（可选）

### 9.2 打包命令

```bash
# 开发环境打包
mvn clean package -Pdev

# 生产环境打包
mvn clean package -Pprod
```

### 9.3 运行命令

```bash
java -jar das.jar
```

### 9.4 Docker部署

项目包含Dockerfile，支持Docker容器化部署。

---

## 10. 系统特性

### 10.1 高性能
- 使用Netty作为网络通信框架，支持高并发连接
- 使用Undertow作为Web服务器，性能优于Tomcat
- 使用Druid连接池，优化数据库连接管理
- 批量插入数据，减少数据库IO

### 10.2 高可用
- 支持MQTT自动重连
- 支持优雅关闭
- 定时任务异步执行
- 线程池管理

### 10.3 可扩展
- 采用策略模式支持多种焊机协议
- 采用模板方法模式统一协议处理流程
- 支持动态分表策略
- 支持多数据源配置

### 10.4 可监控
- 集成Spring Boot Admin应用监控
- 集成Druid数据库监控
- 完善的日志记录
- 系统信息采集（CPU、内存）

---

## 11. 关键技术点

### 11.1 协议解析
- 采用责任链模式处理不同控制命令
- 采用工厂模式创建解码对象
- 支持自定义协议扩展

### 11.2 数据处理
- 使用阻塞队列进行数据缓冲
- 批量处理数据，提高性能
- 异步任务处理，不阻塞主流程

### 11.3 数据存储
- 双数据源配置，业务数据和实时数据分离
- 按日期分表，提高查询性能
- 支持ProcessDB实时数据库存储

### 11.4 消息通信
- MQTT发布/订阅模式
- 支持主题批量订阅
- 支持消息重发机制

---

## 12. 注意事项

1. **ProcessDB依赖**：ProcessDB驱动包为本地jar包，打包时需要包含
2. **端口占用**：确保Netty端口（5555、9002）和MQTT端口（1883）未被占用
3. **数据库配置**：确保MySQL数据库已创建并配置正确
4. **MQTT服务**：确保EMQ X服务已启动
5. **定时任务**：定时任务时间可根据实际需求调整
6. **日志配置**：日志文件路径需确保有写入权限

---

## 13. 附录

### 13.1 相关文档
- 采集系统解析文档.docx
- jnzc_weldmesdb.sql（数据库脚本）

### 13.2 参考资料
- Spring Boot官方文档
- Netty官方文档
- MQTT协议规范
- MyBatis Plus官方文档

---

**文档版本**：v1.0  
**创建日期**：2024  
**维护人员**：das开发团队