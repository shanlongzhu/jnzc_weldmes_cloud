<template>
    <div class="swipe-wrap flex-c">
        <section class="swipe-top flex">
            <h1 class="swipe-top-l">WELDMES信息化焊接派工终端</h1>
            <div class="swipe-top-r">{{time}}</div>
        </section>
        <section class="swipe-info flex">
            <div class="swipe-info-l flex-c">
                <div class="swipe-bottom flex-c">
                    <div class="swipe-bottom-t flex">
                        <h2 class="swipe-bottom-t-m">可选焊机</h2>
                        <div class="swipe-bottom-t-r">
                            <el-select
                                size="mini"
                                v-model="area"
                                placeholder="请选择"
                                style="width:100px;margin-right:10px"
                                @change="changeArea"
                                clearable
                            >
                                <el-option
                                    v-for="item in areaArr"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                            <el-select
                                v-model="bay"
                                size="mini"
                                placeholder="请选择"
                                style="width:130px"
                                @change="getList"
                                clearable
                            >
                                <el-option
                                    v-for="item in straddleArr"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </div>
                    </div>
                    <div
                        class="swipe-bottom-b"
                        v-loading="loading"
                    >
                        <ul class="swipe-pro flex-n">
                            <li
                                v-for="item in list"
                                :key="item.id"
                                @click="handlerItem(item)"
                                :class="{'current':item.id==curModel.id,'taskCur':item.taskFlag}"
                            >
                                <i class="bind-tip">{{item.taskFlag?'已绑定':'空闲'}}</i>
                                <img :src="`/swipes/${imgType(item.typeStr)}${statusText(item.weldStatus).imgN}.png`" />
                                <span>({{item.macFlag}}) {{item.machineNo}}--{{item.gatherNo}}</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="swipe-info-r flex-c">
                <div class="swipe-bottom-t flex">
                    <h2 class="swipe-bottom-t-m">信息列表</h2>
                </div>
                <div
                    class="welding-info"
                    v-loading="infoLoading"
                >
                    <el-row>
                        <el-col :span="12">焊工姓名：{{taskInfo.welderName||'未绑定'}}</el-col>
                        <el-col :span="12">焊工编号：{{taskInfo.welderNo||'未绑定'}}</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">所在班组：{{taskInfo.deptName||'未绑定'}}</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">焊机编号：{{taskInfo.weldeNo||'未绑定'}}</el-col>
                        <el-col :span="12">焊机类型：{{taskInfo.weldeType||'未绑定'}}</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">任务焊工：{{taskInfo.welderName||'未绑定'}}</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">任务编号：{{taskInfo.taskNo||'未绑定'}}</el-col>
                    </el-row>
                </div>
                <div class="swipe-bottom-t flex">
                    <h2 class="swipe-bottom-t-m">任务列表</h2>
                    <span>任务数：<b style="color:green;font-size:18px">{{taskList.length||0}}</b></span>
                </div>
                <div style="flex:1;height:0">
                    <vxe-table
                        border
                        :data="taskList"
                        size="mini"
                        height="auto"
                        :loading="taskLoading"
                        highlight-current-row
                        auto-resize
                        @current-change="handleCurrentChange"
                    >
                        <vxe-table-column
                            type="seq"
                            title="序号"
                            width="50"
                        ></vxe-table-column>
                        <vxe-table-column
                            field="taskNo"
                            title="任务号"
                            min-width="120"
                        ></vxe-table-column>
                        <vxe-table-column
                            field="taskName"
                            title="任务名称"
                            min-width="80"
                        ></vxe-table-column>
                        <vxe-table-column
                            field="statusStr"
                            title="任务状态"
                            width="80"
                        ></vxe-table-column>
                    </vxe-table>
                </div>
            </div>
        </section>
        <section class="tc p10">
            <el-button
                @click="bindTask"
                :loading="butLoading"
            >确定</el-button>
            <el-button @click="backPage">返回</el-button>
        </section>
    </div>
</template>

<script>
import mqtt from 'mqtt'
import moment from 'moment'
import { showLoading, hideLoading } from '@/utils/utilsCom'
import { login } from '_api/user.js'
import { setPublicToken, getPublicToken, removePublicToken, getToken } from '@/utils/auth'
import { getDictionaries } from '_api/productionProcess/process'
import { findByIdArea, getWelderListNoPage, getTaskInfoByWelderId, getWeldClaimTaskInfoById, addTaskClaimInfo } from '_api/productionEquipment/production'
import './swipe.scss'
export default {
    components: {},
    props: {},
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


            area: '',
            bay: '',
            //区域
            areaArr: [],
            //跨间
            straddleArr: [],
            straddleArr2: [],


            time: '',
            date: '',
            timer: '',
            //设备列表
            list: [],
            loading: false,
            //选中设备
            curModel: {},
            //焊工信息
            welderInfo: sessionStorage.getItem('welderInfo') ? JSON.parse(sessionStorage.getItem('welderInfo')) : {},
            //选中的任务
            selectTask: {},
            //任务列表
            taskList: [],
            taskLoading: false,
            //焊机绑定任务信息
            taskInfo: {},
            infoLoading: false,
            //确定按钮loading
            butLoading: false,

            //
            offnum: 0,
            workArray: 0,
            standbyArray: 0,
            warnArray: 0,

        }
    },
    watch: {},
    computed: {},
    methods: {
        //更新列表
        setData (arr) {
            let v1 = arr.slice(-1)[0];
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

        //更新松下列表
        setDataSx (b) {
            let v1 = { ...b };
            this.list.forEach(item => {
                if (parseInt(v1.weldCid) == parseInt(item.gatherNo)) {
                    item.voltage = v1.weldVol || '';
                    item.electricity = v1.weldEle || '';
                    item.welderName = v1.welderName || '';
                    item.taskNo = v1.taskNo || ''
                    item.weldStatus = v1.weldStatus;//状态
                }
            })
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


        async getList () {
            let req = {
                area: this.area,
                bay: this.bay
            }
            this.loading = true;
            let { data, code } = await getWelderListNoPage(req);
            this.loading = false;
            if (code == 200) {
                this.list = (data || []).map(item => {
                    let objItem = { ...item };
                    objItem.electricity = '';//电流
                    objItem.voltage = '';//电压
                    objItem.weldIp = '';//IP
                    objItem.weldStatus = -1;//状态
                    objItem.welderName = '';//操作人
                    objItem.taskNo = '';//任务编号
                    return objItem;
                });
                this.createConnection2();

            }
        },
        //获取数据字典
        async getDicFun () {
            this.getList();
            let { data, code } = await getDictionaries({ "types": ["16", "17"] });
            if (code == 200) {
                this.areaArr = data['16'] || [];
                this.straddleArr2 = data['17'] || [];
            }
        },
        async changeArea (id) {
            if (id) {
                this.bay = "";
                let { code, data } = await findByIdArea({ id });
                if (code == 200) {
                    this.straddleArr = data || []
                }
            } else {
                this.bay = "";
                this.straddleArr = [];
            }

            this.getList();
        },
        //根据焊工id获取任务列表
        async getTaskListFun (welderId) {
            this.taskLoading = true;
            let { data, code } = await getTaskInfoByWelderId({ welderId });
            this.taskLoading = false;
            if (code == 200) {
                this.taskList = data || [];
            }
        },
        async handlerItem (item) {
            this.curModel = item;
            this.infoLoading = true;
            //0：代表otc ，1：松下
            let { data, code } = await getWeldClaimTaskInfoById({ weldeId: item.id,weldType:item.macFlag=='otc'?0:1 });
            this.infoLoading = false;
            if (code == 200) {
                this.taskInfo = data || {};
            }
        },

        handleCurrentChange ({ row }) {
            this.selectTask = row || {};
        },

        //mqtt创建
        createConnection () {
            let connectUrl = `ws://${process.env.VUE_APP_MQTT_API}:8083/mqtt`
            try {
                this.client = mqtt.connect(connectUrl, this.options)
            } catch (error) {
                console.log('连接失败', error)
            }
            this.client.on('connect', () => {
                this.doSubscribe();

            })
            this.client.on('error', error => {
                console.log('连接失败', error)
            })
            this.client.on('message', (topic, message) => {
                if (topic == 'jnOtcV1RtData') {
                    var datajson = JSON.parse(`${message}`);
                    if (datajson.length > 0) {
                        //更新列表状态
                        this.setData(datajson);
                    }
                }
                //松下
                if (topic == 'jnSxRtData') {
                    var datajson = JSON.parse(`${message}`);
                    if (datajson.length > 0) {
                        //更新列表状态
                        this.setDataSx(datajson);
                    }
                }
            })
        },


        //订阅主题
        doSubscribe () {
            // otc
            this.client.subscribe('jnOtcV1RtData', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            });
            //松下
            this.client.subscribe('jnSxRtData', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            })
        },

        doPublish (msg) {
            this.client.publish('jnTaskClaimIssue', msg, 1)
        },

        //任务绑定
        bindTask () {
            if (!this.curModel.hasOwnProperty('id')) {
                return this.$message.error('请选择焊机');
            }
            if (!this.curModel.gatherNo) {
                return this.$message.error('选择的焊机采集序号位空！！！！！');
            }
            if (!this.selectTask.hasOwnProperty("id")) {
                return this.$message.error('请选择任务');
            }
            if(this.selectTask.craftRemarks&&this.selectTask.craftRemarks.trim()!=""){
                this.$confirm(this.selectTask.craftRemarks, '工艺备注', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(() => {
                this.bindTaskData();
            }).catch(() => { })
            }else{
                this.bindTaskData();
            }
        },

        bindTaskData () {
            let msg = {}
            msg['welderId'] = this.welderInfo.id;//焊工ID
            msg['welderName'] = this.welderInfo.welderName;//焊工姓名
            msg['welderDeptId'] = this.welderInfo.deptId;//焊工组织ID
            msg['welderNo'] = this.welderInfo.welderNo;//焊工编号
            msg['taskId'] = this.selectTask.id;//任务ID
            msg['taskName'] = this.selectTask.taskName;//任务名称
            msg['taskNo'] = this.selectTask.taskNo;//任务编号

            msg['machineId'] = this.curModel.id;//焊机ID
            msg['machineNo'] = this.curModel.machineNo;//焊机编号
            msg['machineDeptId'] = this.curModel.deptId;//焊机组织ID



            msg['weldIp'] = "";
            msg['gatherNo'] = "";
            if (this.curModel.macFlag == 'otc') {
                msg['weldType'] = 0;//设备类型
                msg['gatherNo'] = this.curModel.gatherNo;//采集编号
            }
            if (this.curModel.macFlag == 'sx') {
                msg['weldType'] = 1;//设备类型
                msg['weldIp'] = this.curModel.gatherNo || "";//设备IP
            }
            msg['startFlag'] = 0;//开始标记

            this.doPublish(JSON.stringify(msg));
            console.log(msg)
            this.addTaskClaimInfo(msg);
            this.butLoading = true;
            this.$message({
                message: '已发送',
                type: 'success',
                duration: '1000'
            });
            setTimeout(() => {
                this.client.end();
                this.$router.replace({ path: '/swipeCard' })
            }, 1000)
        },



        backPage () {
            this.$router.go(-1);
        },
        //绑定任务
        async addTaskClaimInfo (data) {
            let req = {
                welderId: data.welderId,
                taskId: data.taskId,
                weldId: data.machineId,
                weldType:this.curModel.macFlag=='otc'?0:1//0：代表otc ，1：松下

            }
            let { code } = await addTaskClaimInfo(req);
            if (code == 200) {

            }
        }

    },
    created () {
        //获取任务列表
        this.getTaskListFun(this.welderInfo.id || "");
        //获取区域胯间下拉选项
        this.getDicFun();
        this.timer = setInterval(() => {
            this.time = moment(new Date()).format("YYYY-MM-DD HH:mm:ss")
        }, 1000)
    },
    mounted () {
        this.createConnection();
    },
}
</script>
<style lang="scss">
.swipe-middle {
    height: 250px;
    background: url("/swipes/firstmid.png") no-repeat center -1px;
    background-size: cover;
    justify-content: center;
    align-items: center;
    .swipe-middle-con {
        width: 400px;
        height: 150px;
        background-color: rgba(0, 0, 0, 0.5);
        justify-content: center;
        align-items: center;
        color: #fff;
        border: 2px solid #6686c1;
    }
}
.swipe-bottom {
    flex: 1;
    height: 0px;
}
.swipe-pro {
    padding-left: 10px;
    li {
        width: 122px;
        margin: 0 10px 10px 0px;
        cursor: pointer;
        border: 1px solid #ddd;
        text-align: center;
        padding-top: 6px;
        transition: all 0.3s ease 0s;
        position: relative;
        .bind-tip {
            position: absolute;
            top: 0px;
            right: 0px;
            font-size: 12px;
            color: #fff;
            background: rgb(1, 199, 83);
            padding: 1px 2px;
        }
        span {
            margin-top: 6px;
            display: block;
            font-size: 12px;
            line-height: 22px;
            background: #f8f8f8;
            padding-top: 0px;
            border-top: 1px solid #ddd;
            width: 100%;
            color: #333;
        }
        &.taskCur {
            .bind-tip {
                background: #f00;
            }
        }
        &:hover {
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.6);
            transform: scale(1.02);
        }
        &.current {
            border: 1px solid #aadef7;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.6);
            transform: scale(1.02);
            span {
                background: #aadef7;
            }
        }
    }
}
.swipe-info {
    flex: 1;
    height: 0px;
    .swipe-bottom-t {
        color: #fff;
        justify-content: space-between;
        align-items: center;
        padding: 0px 10px;
        height: 40px;
        background: #fb8136;
        margin-bottom: 5px;
        .swipe-bottom-t-l,
        .swipe-bottom-t-m,
        .swipe-bottom-t-r {
            flex: 1;
        }
        .swipe-bottom-t-r {
            text-align: right;
        }
    }
    .swipe-bottom-b {
        flex: 1;
        background: #fff;
        height: 0px;
        overflow-y: scroll;
        padding-top: 10px;
        border: 0px;
    }
    .swipe-bottom-t-m {
        text-align: left !important;
        color: #333;
        font-weight: bold;
    }
}
.swipe-info-l {
    flex: 1;
    width: 0px;
    height: 100%;
    padding: 0 5px;
}
.swipe-info-r {
    width: 400px;
}
.welding-info {
    color: #fff;
    padding: 0px 10px;
    margin-bottom: 5px;
    background: #34588c;
    .el-row {
        padding: 10px 0;
    }
}
.el-message-box__message{
    white-space: pre-wrap;
}
</style>
