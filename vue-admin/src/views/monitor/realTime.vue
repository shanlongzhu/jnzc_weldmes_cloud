<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div
            class="table-con flex"
            style="flex:1; height:0px"
        >
            <div
                class="user-l"
                style='height:100%;width:300px;border:1px solid #ddd;margin-right:10px'
            >
                <div class="organizational-tit">
                    组织机构菜单
                </div>
                <div style="height:calc(100% - 34px);overflow-y:auto">
                    <el-tree
                        :data="treeData"
                        ref="treeDom"
                        :expand-on-click-node="false"
                        v-loading="treeLoading"
                        :props="defaultProps"
                        default-expand-all
                        @current-change="currentChangeTree"
                        highlight-current
                        node-key="id"
                    ></el-tree>
                </div>
            </div>
            <div
                class="user-r flex-c real-tit"
                style='height:100%;flex:1; width:0px'
            >
                <div class="organizational-tit fs14 flex real-tj-tit">
                    <div>焊机实时状态监测</div>

                    <div>
                        <span>关机:<strong>{{offnum}}</strong></span>
                        <span style="color:#f00">故障:<strong>{{warnArray}}</strong></span>
                        <span style="color:rgb(1, 209, 95">待机:<strong>{{standbyArray}}</strong></span>
                        <span style="color:green">焊接:<strong>{{workArray}}</strong></span>
                    </div>
                </div>
                <div
                    class="real-con"
                    v-loading="loading"
                >
                    <div class="real-con-box flex-n">
                        <div
                            class="real-con-item flex-n"
                            v-for="item in list"
                            :key="item.id"
                            @click="handlerWeld(item)"
                        >
                            <span class="real-con-item-img">
                                <img :src="`/swipes/${imgType(item.typeStr)}${statusText(item.weldStatus).imgN}.png`" />
                            </span>
                            <div class="real-con-item-txt">
                                <p><span>设备编号：</span>{{item.machineNo||'--'}}</p>
                                <p><span>任务编号：</span>{{item.taskNo||'--'}}</p>
                                <p><span>操作人员：</span>{{item.welderName||'--'}}</p>
                                <p><span>焊接电流：</span>{{item.electricity||item.electricity===0?item.electricity:'--'}}A</p>
                                <p><span>焊接电压：</span>{{item.voltage||item.voltage===0?item.voltage:'--'}}V</p>
                                <p><span>焊机状态：</span><strong>{{statusText(item.weldStatus).str}}</strong></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- <el-pagination
            class="p10"
            :current-page.sync="page"
            :page-size="10"
            align="right"
            background
            layout="total, prev, pager, next"
            :total="total"
            @current-change="handleCurrentChange"
        /> -->
        <el-dialog
            title="设备运行参数监控"
            :visible.sync="drawer"
            fullscreen
            class="real-con-layer"
        >
            <div
                class="flex-c"
                style="height:calc(100vh - 100px)"
            >
                <div class="real-layer-top flex">
                    <div class="border-tip flex-c">
                        <span class="border-tip-txt">设备信息</span>
                        <div
                            class="real-con-item flex-n"
                            style="width:300px"
                        >
                            <span class="real-con-item-img">
                                <img :src="`/swipes/${imgType(selectItem.typeStr)}${statusText(selectItem.weldStatus).imgN}.png`" />
                            </span>
                            <div class="real-con-item-txt">
                                <p><span>设备编号：</span>{{selectItem.machineNo||'--'}}</p>
                                <p><span>任务编号：</span>{{selectItem.taskNo||'--'}}</p>
                                <p><span>操作人员：</span>{{selectItem.welderName||'--'}}</p>
                                <p><span>焊接电流：</span>{{selectItem.electricity||selectItem.electricity===0?selectItem.electricity:'--'}}A</p>
                                <p><span>焊接电压：</span>{{selectItem.voltage||selectItem.voltage===0?selectItem.voltage:'--'}}V</p>
                                <p><span>焊机状态：</span><strong>{{statusText(selectItem.weldStatus).str}}</strong></p>
                            </div>
                        </div>
                    </div>
                    <div class="border-tip flex-c">
                        <span class="border-tip-txt">焊接参数</span>
                        <div class="wel-tip">
                            <p><span>电流</span><strong>100A</strong></p>
                            <p><span>电压</span><strong>30V</strong></p>
                        </div>
                    </div>
                    <div
                        class="border-tip flex-c"
                        style="flex:1;margin-right:0px"
                    >
                        <span class="border-tip-txt">设备特征</span>
                        <div class="wel-text">
                            <el-row>
                                <el-col :span="6">
                                    开机时长：
                                </el-col>
                                <el-col :span="6">
                                    通道总数：
                                </el-col>
                                <el-col :span="6">
                                    气体流量：
                                </el-col>
                                <el-col :span="6">
                                    预置电流：
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="6">
                                    离线时长：
                                </el-col>
                                <el-col :span="6">
                                    当前通道：自由调节状态
                                </el-col>
                                <el-col :span="6">
                                    瞬时功率：
                                </el-col>
                                <el-col :span="6">
                                    预置电压：
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="6">
                                    工作时长：
                                </el-col>
                                <el-col :span="6">
                                    焊接控制：
                                </el-col>
                                <el-col :span="6">
                                    提前送气时间：
                                </el-col>
                                <el-col :span="6">
                                    初期电流：
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="6">
                                    焊接时长：
                                </el-col>
                                <el-col :span="6">
                                    送丝速度：
                                </el-col>
                                <el-col :span="6">
                                    滞后停气时间：
                                </el-col>
                                <el-col :span="6">
                                    收弧电流：
                                </el-col>
                            </el-row>
                        </div>
                    </div>
                </div>
                <div class="model-line-box ele-box flex">
                    <div class="line-box-l flex-c">
                        <span class="line-box-l-tit">电流曲线</span>
                        <span class="line-box-l-unit">A</span>
                    </div>
                    <line-e ref="lineComEChild"></line-e>
                </div>
                <div class="model-line-box vol-box flex">
                    <div class="line-box-l flex-c">
                        <span class="line-box-l-tit">电压曲线</span>
                        <span class="line-box-l-unit">V</span>
                    </div>
                    <line-v ref="lineComVChild"></line-v>
                </div>
            </div>

        </el-dialog>
    </div>
</template>

<script>
import mqtt from 'mqtt'
import { getTeam } from '_api/productionProcess/process'
import { getUserTree, getTreeDeptInfo, addDept, findIdDeptInfo, editDept, delDept } from '_api/system/systemApi'
import { getModelFindId } from '_api/productionEquipment/production'
import lineE from './components/lineE.vue'
import LineV from './components/lineV.vue'
export default {
    components: { lineE, LineV },
    name: 'organizational',
    data () {

        return {
            //mqtt
            client: {},
            options: {
                timeout: 50,
                keepAliveInterval: 60,
                cleanSession: false,
                useSSL: false,
                reconnect: true,
                clientId: "adminTest" + new Date().getTime()
            },
            timeout: '',

            list: [],
            //分页
            page: 1,
            total: 0,

            //搜索条件
            searchObj: {
                id: ''
            },

            loading: false,

            //部门tree数据
            treeLoading: false,
            treeData: [],
            defaultProps: {
                children: 'list',
                label: 'name'
            },
            //
            drawer: false,
            //点击的设备
            selectItem: {},
            //
            offnum: 0,
            workArray: 0,
            standbyArray: 0,
            warnArray: 0,


            //曲线数据
            lineDataTime: [],
            lineDataValueE: [],
            lineDataValueV: []
        }
    },
    created () {
        this.searchObj.id = 1;
        this.getList();
        this.getUserTreeFun();
    },
    methods: {
        //mqtt创建
        createConnection () {
            let connectUrl = `ws://${process.env.VUE_APP_MQTT_API}:8083/mqtt`
            try {
                this.client = mqtt.connect(connectUrl, this.options)
            } catch (error) {
                console.log('连接失败', error)
            }
            this.client.on('connect', () => {
                // console.log('连接成功')
                this.doSubscribe();

            })
            this.client.on('error', error => {
                console.log('连接失败', error)
            })
            this.client.on('message', (topic, message) => {
                if (topic == 'rtcdata') {
                    var datajson = JSON.parse(`${message}`);                
                    if(datajson.length>0){
                        //获取曲线数据
                        this.setLineData(datajson);
                        //更新列表状态
                        this.setData(datajson);
                    }      
                }
            })
        },
        //更新列表
        setData (arr) {
            let v1 = arr.slice(-1)[0];
            console.log(v1)
            this.offnum = 0;
            this.warnArray = 0;
            this.standbyArray = 0;
            this.workArray = 0;
            this.list.forEach(item => {                
                if (parseInt(v1.gatherNo) == parseInt(item.gatherNo)) {
                    item.voltage = v1.voltage
                    item.electricity = v1.electricity
                    item.welderName = v1.welderName
                    item.taskNo = v1.taskNo
                    item.weldStatus = v1.weldStatus;//状态
                }
                this.totalNum(item.weldStatus);
            })
        },

        //订阅主题
        doSubscribe () {
            this.client.subscribe('rtcdata', 0, (error, res) => {
                // console.log('订阅rtcdata')
                if (error) {
                    this.$message.error("订阅rtcdata超时")
                    return
                }
            })
        },




        search () {
            this.page = 1;
            this.getList();
        },
        //获取部门tree
        async getUserTreeFun () {
            this.treeLoading = true;
            let { data, code } = await getUserTree();
            this.treeLoading = false;
            if (code == 200) {
                this.treeData = [data] || [];
                this.$nextTick(() => {
                    this.$refs.treeDom.setCurrentKey(this.searchObj.id)
                })
            }
        },

        //根据部门id获取设备列表
        async getList (id) {
            let req = {
                pn: this.page,
                ...this.searchObj
            }
            this.loading = true;
            let { code, data } = await getModelFindId(req);
            this.loading = false;
            if (code == 200) {
                this.list = (data.list || []).map(item => {
                    let objItem = { ...item };
                    objItem.electricity = '';//电流
                    objItem.voltage = '';//电压
                    objItem.weldIp = '';//IP
                    objItem.weldStatus = -1;//状态
                    objItem.welderName = '';//操作人
                    objItem.taskNo = '';//任务编号
                    return objItem;
                });
                this.total = data.total;
                this.createConnection();
            }
        },

        //分页切换
        handleCurrentChange (p) {
            this.page = p;
            this.getList();
        },

        currentChangeTree (v) {
            this.searchObj.id = v.id;
            this.search();
        },

        //点击焊机
        handlerWeld (v) {
            this.drawer = true;
            this.selectItem = v;
        },

        setLineData (arr) {
            if (this.selectItem.hasOwnProperty('gatherNo')) {
                let filterArr = (arr || []).filter(item => parseInt(item.gatherNo) == parseInt(this.selectItem.gatherNo));
                if(this.lineDataTime.length>10){
                    this.lineDataTime.shift();
                    this.lineDataTime.shift();
                    this.lineDataTime.shift();
                    
                    this.lineDataValueE.shift();
                    this.lineDataValueE.shift();
                    this.lineDataValueE.shift();

                    this.lineDataValueV.shift();
                    this.lineDataValueV.shift();
                    this.lineDataValueV.shift();
                }
                filterArr.forEach(item => {
                    this.lineDataTime.push(item.weldTime);
                    this.lineDataValueE.push(item.electricity);
                    this.lineDataValueV.push(item.voltage)
                })
                this.$refs.lineComEChild.init(this.lineDataValueE, this.lineDataTime);
                this.$refs.lineComVChild.init(this.lineDataValueV, this.lineDataTime)
            }
        },


        //焊机状态
        statusText (v) {
            let str = '关机';
            let imgN = '';
            switch (v) {
                case -1:
                    str = '关机';
                    imgN = '';
                    break;
                case 0:
                    str = '待机';
                    imgN = 'S';
                    break;
                case 3: case 5: case 7:
                    str = '焊接';
                    imgN = 'W';
                    break;
                default:
                    str = '故障';
                    imgN = 'O';
                    break;
            }
            return {
                str: str,
                imgN: imgN
            };
        },
        //图片类型
        imgType (v) {
            let str = '';
            switch (v) {
                case '手工焊':
                    str = 'AT';
                    break;
                case '气保焊':
                    str = 'FR';
                    break;
                default:
                    str = 'GL';
                    break;
            }
            return str;
        },
        totalNum (v) {
            switch (v) {
                case -1:
                    this.offnum++;
                    break;
                case 0:
                    this.standbyArray++;
                    break;
                case 3: case 5: case 7:
                    this.workArray++;
                    break;
                default:
                    this.warnArray++;
                    break;
            }
        }

    }
}
</script>

<style>
.user-l * {
    font-size: 12px;
}
.organizational-tit {
    height: 34px;
    padding: 0 10px;
    line-height: 34px;
    background: #f8f8f8;
    font-weight: bold;
    border-bottom: 1px solid #ddd;
    border-left: 1px solid #ddd;
}
.real-tj-tit {
    justify-content: space-between;
}
.real-tj-tit span {
    margin-left: 20px;
    font-weight: 400;
}

.real-tit {
    border-top: 1px solid #ddd;
}
.real-con {
    flex: 1;
    height: 0;
    overflow-y: scroll;
    border: 1px solid #ddd;
    border-top: none;
}
.real-con-item {
    line-height: 20px;
    font-size: 12px;
    align-items: center;
    padding: 10px;
    width: 230px;
}
.real-con-item .real-con-item-img {
    margin-right: 6px;
}
.real-con-item .real-con-item-txt p {
    margin: 0px;
}
.real-con-item .real-con-item-txt p span {
    color: #666;
}

.real-con-layer * {
    color: #333;
}

.real-con-layer .border-tip {
    border: 1px solid #999;
    position: relative;
    padding-top: 10px;
    padding-bottom: 10px;
    margin-top: 10px;
    margin-right: 10px;
    justify-content: center;
}
.real-con-layer .border-tip-txt {
    position: absolute;
    background: #fff;
    left: 10px;
    top: -7px;
    font-size: 14px;
    line-height: 14px;
    padding: 0 10px;
}
.wel-tip {
    min-width: 250px;
    padding: 0 20px;
    line-height: 36px;
}
.wel-tip span {
    display: inline-block;
    background: rgb(1, 209, 95);
    width: 80px;
    line-height: 36px;
    font-size: 18px;
    text-align: center;
    color: #fff;
    border-radius: 10px;
    vertical-align: middle;
    margin-right: 5px;
}
.wel-tip p:last-child span {
    background: rgb(255, 119, 8);
}
.wel-tip strong {
    font-size: 24px;
    vertical-align: middle;
}
.wel-text {
    padding: 10px 20px;
    line-height: 24px;
}
.wel-text * {
    font-size: 12px;
}

.model-line-box {
    flex: 1;
    height: 0px;
    margin-top: 10px;
}
.line-box-l {
    width: 34px;
    background: rgb(1, 209, 95);
    font-size: 18px;
    padding: 2px;
    text-align: center;
    justify-content: center;
    align-items: center;
}
.line-box-l-tit {
    line-height: 26px;
    color: #fff;
}
.line-box-l-unit {
    background: #fff;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    font-size: 16px;
    line-height: 20px;
    text-align: center;
}
.vol-box .line-box-l {
    background: rgb(255, 119, 8);
}
</style>
