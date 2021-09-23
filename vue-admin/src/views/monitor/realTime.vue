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
                v-show="isMenuShow"
                class="user-l"
                style='height:100%;width:300px;border:1px solid #ddd;'
            >
                <div class="organizational-tit">
                    组织机构菜单
                </div>
                <div style="height:calc(100% - 34px);overflow-y:auto">
                    <organization @currentChangeTree="currentChangeTree"></organization>
                </div>
            </div>
            <div
                style="width:10px"
                class="flex-c btn-show-hide"
                @click="changeMenuShowHide"
            ><span :class="{'el-icon-caret-right':!isMenuShow,'el-icon-caret-left':isMenuShow}"></span></div>
            <div
                class="user-r flex-c real-tit"
                style='height:100%;flex:1; width:0px'
            >
                <div class="organizational-tit fs14 flex real-tj-tit">
                    <div>焊机实时状态监测</div>

                    <div>
                        <span>关机:<strong>{{offnum||0}}</strong></span>
                        <span style="color:#f00">故障:<strong>{{warnArray.length||0}}</strong></span>
                        <span style="color:rgb(1, 209, 95)">待机:<strong>{{standbyArray.length||0}}</strong></span>
                        <span style="color:green">焊接:<strong>{{workArray.length||0}}</strong></span>
                    </div>
                </div>
                <div
                    class="real-con"
                    v-loading="loading"
                >
                    <div class="real-con-box flex-n">
                        <div
                            class="real-con-item flex"
                            v-for="item in list"
                            :key="item.id"
                            @click="handlerWeld(item)"
                        >
                            <span class="real-con-item-img">
                                <img :src="`/swipes/${imgType(item.typeStr)}${statusText(item.weldStatus).imgN}.png`" />
                            </span>
                            <div class="real-con-item-txt">
                                <p :title="item.machineNo"><span>设备编号：</span>{{item.macFlag}}-{{item.machineNo||'--'}}</p>
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
                            <span
                                class="real-con-item-img"
                                @click="drawer=false"
                            >
                                <img :src="`/swipes/${imgType(mqttLastData.typeStr)}${statusText(mqttLastData.weldStatus).imgN}.png`" />
                            </span>
                            <div class="real-con-item-txt">
                                <p><span>设备编号：</span>{{mqttLastData.machineNo||'--'}}</p>
                                <p><span>任务编号：</span>{{mqttLastData.taskNo||'--'}}</p>
                                <p><span>操作人员：</span>{{mqttLastData.welderName||'--'}}</p>
                                <p><span>焊接电流：</span>{{mqttLastData.electricity||mqttLastData.electricity===0?mqttLastData.electricity:'--'}}A</p>
                                <p><span>焊接电压：</span>{{mqttLastData.voltage||mqttLastData.voltage===0?mqttLastData.voltage:'--'}}V</p>
                                <p><span>焊机状态：</span><strong>{{statusText(mqttLastData.weldStatus).str}}</strong></p>
                            </div>
                        </div>
                    </div>
                    <div class="border-tip flex-c">
                        <span class="border-tip-txt">焊接参数</span>
                        <div class="wel-tip">
                            <p><span>焊接电流</span><strong>{{mqttLastData.electricity}}A</strong></p>
                            <p><span>焊接电压</span><strong>{{mqttLastData.voltage}}V</strong></p>
                        </div>
                    </div>
                    <div class="border-tip flex-c">
                        <span class="border-tip-txt">预置参数</span>
                        <div class="wel-tip">
                            <p><span>预置电流</span><strong>{{mqttLastData.presetEle}}A</strong></p>
                            <p><span>预置电压</span><strong>{{mqttLastData.presetVol}}V</strong></p>
                        </div>
                    </div>
                    <div
                        class="border-tip flex-c"
                        style="flex:1;margin-right:0px"
                    >
                        <span class="border-tip-txt">设备特征</span>
                        <div class="wel-text">
                            <el-row>
                                <el-col :span="12">
                                    开机时间：
                                </el-col>
                                <el-col :span="12">
                                    通道总数：30
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="12">
                                    关机时间：
                                </el-col>
                                <el-col :span="12">
                                    当前通道：{{mqttLastData.channelNo==0||mqttLastData.channelNo==255?'自由调节状态':mqttLastData.channelNo}}
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="12">
                                    工作时长：
                                </el-col>
                                <el-col :span="12">
                                    送丝速度：{{mqttLastData.wireFeedRate}}
                                </el-col>

                            </el-row>
                            <el-row>
                                <el-col :span="12">
                                    焊接时长：
                                </el-col>
                                <el-col :span="12">
                                    瞬时功率：{{mqttLastData.electricity*mqttLastData.voltage}}
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
import { getModelFindId } from '_api/productionEquipment/production'
import lineE from './components/lineE.vue'
import LineV from './components/lineV.vue'
import organization from '_c/Organization'
import data from '../pdf/content'
export default {
    components: { lineE, LineV, organization },
    name: 'realTime',
    data () {

        return {
            //mqtt
            newClientMq: {},

            list: [],
            //分页
            page: 1,
            total: 0,

            //搜索条件
            searchObj: {
                id: ''
            },

            loading: false,


            //
            drawer: false,
            //点击的设备
            selectItem: {},

            workArray: [],//焊接
            standbyArray: [],//待机
            warnArray: [],//故障


            //曲线数据
            lineData: [],

            mqttLastData: {},

            isMenuShow: true



        }
    },
    computed: {
        offnum () {
            return this.list.length - (this.workArray.length + this.standbyArray.length + this.warnArray.length)
        }
    },

    created () {
        this.searchObj.id = 1;
        this.getList();
        this.newMqtt();
    },
    methods: {
        //mqtt创建
        newMqtt () {
            const PahoMQTT = require('paho-mqtt');
            const name = new Date().getTime() + 'client';
            this.newClientMq = new PahoMQTT.Client(`${process.env.VUE_APP_MQTT_API}`, Number(8083), name);
            this.newClientMq.connect({
                timeout: 50,
                keepAliveInterval: 60,
                cleanSession: false,
                useSSL: false,
                onFailure: function (e) {
                    console.log(e);
                },
                reconnect: true,
                onSuccess: (res) => {
                    this.newClientMq.subscribe('jnOtcV1RtData', {
                        qos: 0,
                        onSuccess: function (e) {
                            console.log("返回主题订阅成功：jnOtcV1RtData");
                        },
                        onFailure: function (e) {
                            console.log(e);
                        }
                    });
                    this.newClientMq.subscribe('jnSxRtData', {
                        qos: 0,
                        onSuccess: function (e) {
                            console.log("返回主题订阅成功：jnSxRtData");
                        },
                        onFailure: function (e) {
                            console.log(e);
                        }
                    })
                }
            })
            this.newClientMq.onMessageArrived = ({ destinationName, payloadString }) => {
                //otc设备实时监测
                if (destinationName == 'jnOtcV1RtData') {
                    var datajson = JSON.parse(payloadString);
                    console.log(datajson)
                    if (datajson.length > 0) {
                        if (!this.drawer) {
                            //更新列表状态
                            this.setData(datajson);
                        } else {
                            //获取曲线数据
                            this.setLineData(datajson);
                        }
                    }
                }
                //sx设备实时监测
                if (destinationName == 'jnSxRtData') {
                    var datajson = JSON.parse(payloadString);
                    console.log(datajson)
                    if (datajson.length > 0) {
                        if (!this.drawer) {
                            //更新列表状态
                            this.setDataSx(datajson);
                        } else {
                            //获取曲线数据
                            this.setLineDataSx(datajson);
                        }
                    }
                }
            }
        },

        //更新列表
        setData (arr) {
            //统计
            for (let b of arr) {
                let isThat = this.list.filter(item => parseInt(b.gatherNo) == parseInt(item.gatherNo));
                if (this.searchObj.id != 1) {
                    if (isThat.length > 0) {
                        this.totalNum(b);
                    }
                } else {
                    this.totalNum(b);
                }
            }
            let v1 = arr.slice(-1)[0];
            this.list.forEach(item => {
                if (parseInt(v1.gatherNo) == parseInt(item.gatherNo)) {
                    item.voltage = v1.voltage
                    item.electricity = v1.electricity
                    item.welderName = v1.welderName
                    item.taskNo = v1.taskNo
                    item.weldStatus = v1.weldStatus;//状态
                }
            })
        },

        //更新松下列表
        setDataSx (b) {
            //统计
            let isThat = this.list.filter(item => parseInt(b.gatherNo) == parseInt(item.gatherNo));
            if (this.searchObj.id != 1) {
                if (isThat.length > 0) {
                    this.totalNum(b);
                }
            } else {
                this.totalNum(b);
            }
            let v1 = {...b};
            this.list.forEach(item => {
                if (parseInt(v1.gatherNo) == parseInt(item.gatherNo)) {
                    item.voltage = v1.voltage
                    item.electricity = v1.electricity
                    item.welderName = v1.welderName
                    item.taskNo = v1.taskNo
                    item.weldStatus = v1.weldStatus;//状态
                }
            })
        },


        search () {
            this.page = 1;
            this.getList();
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
                // this.offnum = data.total;
                this.total = data.total;
                // this.createConnection();
            }
        },

        //分页切换
        handleCurrentChange (p) {
            this.page = p;
            this.getList();
        },

        currentChangeTree (v) {
            this.workArray = [];//焊接
            this.standbyArray = [];//待机
            this.warnArray = [];//故障
            this.searchObj.id = v.id;
            this.search();
        },

        //点击焊机
        handlerWeld (v) {
            this.lineData = [];
            this.drawer = true;
            this.selectItem = v;
            this.mqttLastData = { ...v };
            // this.$refs.lineComEChild.echartsClear();
            // this.$refs.lineComVChild.echartsClear();            
            this.$nextTick(() => {
                this.$refs.lineComEChild.init(this.lineData);
                this.$refs.lineComVChild.init(this.lineData);
            })

        },

        setLineData (arr) {
            if (this.selectItem.hasOwnProperty('gatherNo')) {
                let filterArr = (arr || []).filter(item => parseInt(item.gatherNo) == parseInt(this.selectItem.gatherNo));
                if (filterArr.length > 0) {
                    this.mqttLastData = filterArr.slice(-1)[0];
                    if (this.lineData.length > 15) {
                        this.lineData.shift();
                        this.lineData.shift();
                        this.lineData.shift();
                    }
                    filterArr.forEach(item => {
                        this.lineData.push(item);
                    })
                    this.$refs.lineComEChild.init(this.lineData);
                    this.$refs.lineComVChild.init(this.lineData);
                }
            }
        },
        //sx更新曲线
        setLineDataSx (arr) {
            if (this.selectItem.hasOwnProperty('gatherNo')) {                
                if (parseInt(arr.gatherNo) == parseInt(this.selectItem.gatherNo)) {
                    this.mqttLastData = {...arr};
                    if (this.lineData.length > 15) {
                        this.lineData.shift();
                    }
                    this.lineData.push(mqttLastData);
                    this.$refs.lineComEChild.init(this.lineData);
                    this.$refs.lineComVChild.init(this.lineData);
                }
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
            switch (v.weldStatus) {
                case -1:
                    this.setOffNum(v);
                    break;
                case 0:
                    this.setStandbyArray(v);
                    break;
                case 3: case 5: case 7:
                    this.setWorkArray(v);
                    break;
                default:
                    this.setWarnArray(v);
                    break;
            }
        },

        //获取数值下标
        isArrNum (str, arr) {
            return arr.indexOf(str);
        },

        //关机状态
        setOffNum (v) {
            //判断焊接是否存在
            let workInNum = this.isArrNum(v.gatherNo, this.workArray);
            //有则删除
            if (workInNum !== -1) {
                this.workArray.splice(workInNum, 1);
            }

            //判断待机是否存在
            let standbyInNum = this.isArrNum(v.gatherNo, this.standbyArray);
            //有则删除
            if (standbyInNum !== -1) {
                this.standbyArray.splice(standbyInNum, 1);
            }

            //判断故障是否存在
            let warnInNum = this.isArrNum(v.gatherNo, this.warnArray);
            //有则删除
            if (warnInNum !== -1) {
                this.warnArray.splice(warnInNum, 1);
            }
        },

        //焊接状态
        setWorkArray (v) {
            //判断焊接是否存在
            let workInNum = this.isArrNum(v.gatherNo, this.workArray);
            //没有则添加
            if (workInNum === -1) {
                this.workArray.push(v.gatherNo);
            }

            //判断待机是否存在
            let standbyInNum = this.isArrNum(v.gatherNo, this.standbyArray);
            //有则删除
            if (standbyInNum !== -1) {
                this.standbyArray.splice(standbyInNum, 1);
            }

            //判断故障是否存在
            let warnInNum = this.isArrNum(v.gatherNo, this.warnArray);
            //有则删除
            if (warnInNum !== -1) {
                this.warnArray.splice(warnInNum, 1);
            }
        },
        //待机状态
        setStandbyArray (v) {
            //判断焊接是否存在
            let workInNum = this.isArrNum(v.gatherNo, this.workArray);
            //有则删除
            if (workInNum !== -1) {
                this.workArray.splice(workInNum, 1);
            }

            //判断待机是否存在
            let standbyInNum = this.isArrNum(v.gatherNo, this.standbyArray);
            //有则删除
            if (standbyInNum === -1) {
                this.standbyArray.push(v.gatherNo);
            }

            //判断故障是否存在
            let warnInNum = this.isArrNum(v.gatherNo, this.warnArray);
            //有则删除
            if (warnInNum !== -1) {
                this.warnArray.splice(warnInNum, 1);
            }
        },

        //故障状态
        setWarnArray (v) {
            //判断焊接是否存在
            let workInNum = this.isArrNum(v.gatherNo, this.workArray);
            //有则删除
            if (workInNum !== -1) {
                this.workArray.splice(workInNum, 1);
            }

            //判断待机是否存在
            let standbyInNum = this.isArrNum(v.gatherNo, this.standbyArray);
            //有则删除
            if (standbyInNum !== -1) {
                this.standbyArray.splice(standbyInNum, 1);
            }

            //判断故障是否存在
            let warnInNum = this.isArrNum(v.gatherNo, this.warnArray);
            //有则删除
            if (warnInNum === -1) {
                this.warnArray.push(v.gatherNo);
            }
        },
        changeMenuShowHide () {
            this.isMenuShow = !this.isMenuShow;
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
    width: 260px;
}
.real-con-item .real-con-item-img {
    margin-right: 6px;
    width: 100px;
}
.real-con-item .real-con-item-txt {
    flex: 1;
}
.real-con-item .real-con-item-txt p {
    margin: 0px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    word-break: break-all;
    width: 130px;
}
.real-con-item .real-con-item-txt p span {
    color: #666;
}

.real-con-layer * {
    color: #333;
}
.real-con-layer .el-dialog__headerbtn {
    font-size: 24px;
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
