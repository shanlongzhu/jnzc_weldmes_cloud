<!--
 * @Descripttion: Co2工艺库下发
 * @version: 
 * @Author: zhanganpeng
 * @Date: 2021-07-08 10:01:29
 * @LastEditors: zhanganpeng
 * @LastEditTime: 2021-09-10 11:33:19
-->

<template>
    <div>
        <vxe-modal
            title="选择工艺FR2"
            v-model="model"
            width="1000"
        >
            <template #default>
                <vxe-table
                    border
                    show-overflow
                    auto-resize
                    size="mini"
                    height="300"
                    :loading="loading"
                    highlight-hover-row
                    resizable
                    stripe
                    :data="tableData"
                    :checkbox-config="{'highlight':true}"
                    @checkbox-all="selectTable1Fun"
                    @checkbox-change="selectTable1Fun"
                >
                    <vxe-table-column
                        type="checkbox"
                        width="60"
                    ></vxe-table-column>
                    <vxe-table-column
                        type="seq"
                        width="70"
                        title="序号"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="channel"
                        title="通道号"
                        min-width="60"
                    >
                        <template #default={row}>
                            {{getChanneName(row.channel)}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="channelFlag"
                        title="通道标志"
                        min-width="70"
                    >
                    </vxe-table-column>
                    <!-- <vxe-table-column
                        field="initialEleMin"
                        title="控制参数"
                        min-width="100"
                    >
                        <template #default={row}>
                            {{row.command==1?'查询':row.command==2?'下载':'删除'}}
                        </template>
                    </vxe-table-column> -->
                    <vxe-table-column
                        field="presetEleMax"
                        title="预置电流上限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="presetVolMax"
                        title="预置电压上限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="presetEleMin"
                        title="预置电流下限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="presetVolMin"
                        title="预置电压下限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="initialEleMax"
                        title="初期电流上限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="initialVolMax"
                        title="初期电压上限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="initialEleMin"
                        title="初期电流下限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="initialVolMin"
                        title="初期电压下限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcEleMax"
                        title="收弧电流上限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcVolMax"
                        title="收弧电压上限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcEleMin"
                        title="收弧电流下限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcVolMin"
                        title="收弧电压下限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="texture"
                        title="材质"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="wireDiameter"
                        title="丝径"
                        min-width="100"
                    >
                        <template #default={row}>
                            {{getWireDiameterArrFun(row.wireDiameter)}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="gases"
                        title="气体"
                        min-width="100"
                    >
                        <template #default={row}>
                            {{getGasesArrFun(row.gases)}}
                        </template>

                    </vxe-table-column>
                    <vxe-table-column
                        field="weldingControl"
                        title="焊接控制"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="pulseHaveNot"
                        title="脉冲有无"
                        min-width="100"
                    >
                    </vxe-table-column>
                </vxe-table>
                <div class="p10 tr">
                    <el-button
                        size="small"
                        type="primary"
                        @click="nextFun"
                    >下一步</el-button>
                    <el-button
                        size="small"
                        @click="model=false"
                    >取消</el-button>
                </div>
            </template>
        </vxe-modal>
        <!-- 选择设备 -->
        <vxe-modal
            title="选择设备"
            v-model="model2"
            width="700"
        >
            <template #default>
                <vxe-table
                    ref="vxeTable"
                    border
                    show-overflow
                    auto-resize
                    size="mini"
                    height="300"
                    :loading="loading2"
                    highlight-hover-row
                    resizable
                    stripe
                    :data="list"
                    row-id="id"
                    :checkbox-config="{'highlight':true,'reserve':true}"
                    @checkbox-all="selectTable2Fun"
                    @checkbox-change="selectTable2Fun"
                >
                    <vxe-table-column
                        type="checkbox"
                        width="60"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="weldNo"
                        title="设备序号"
                        width="100"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="weldCid"
                        title="设备CID"
                        width="100"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="weldCode"
                        title="设备编码"
                        width="100"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="weldIp"
                        title="IP地址"
                        width="100"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="weldStatus"
                        title="状态"
                        width="100"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="weldModel"
                        title="设备机型"
                        width="100"
                    ></vxe-table-column>
                </vxe-table>
                <div
                    class="p10 flex"
                    style="justify-content: space-between;"
                >
                    <el-pagination
                        :current-page.sync="page"
                        :page-size="10"
                        align="right"
                        background
                        small
                        layout="total, prev, pager, next"
                        :total="total2"
                        @current-change="handleCurrentChange"
                    />
                    <div>
                        <el-button
                            size="small"
                            type="primary"
                            @click="submitIssue"
                        >确定</el-button>
                        <el-button
                            size="small"
                            @click="model2=false"
                        >取消</el-button>
                    </div>

                </div>
            </template>
        </vxe-modal>

        <!-- 下发结果显示 -->
        <vxe-modal
            title="下发结果"
            v-model="model3"
            width="700"
        >
            <template #default>
                <vxe-table
                    border
                    show-overflow
                    auto-resize
                    size="mini"
                    height="300"
                    highlight-hover-row
                    resizable
                    stripe
                    :data="newEqu"
                    row-id="id"
                >
                    <vxe-table-column
                        field="weldIp"
                        title="ip"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="channel"
                        title="下发通道号"
                        min-width="100"
                    >
                        <template #default="{row}">
                            {{row.weldInfo.map(item => item.channel).join('，')}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="channel"
                        title="下发成功通道号"
                        min-width="100"
                    >
                        <template #default="{row}">
                            <span style="color:#13ce66">
                                {{row.weldInfo.filter(item => item.isSuccessStatus==1).map(item=>item.channel).join('，')}}
                            </span>
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="channel"
                        title="下发失败通道号"
                        min-width="100"
                    >
                        <template #default="{row}">
                            <span style="color:#f00">
                                {{row.weldInfo.filter(item => item.isSuccessStatus==2).map(item => item.channel).join('，')}}
                            </span>
                        </template>
                    </vxe-table-column>
                </vxe-table>
                <div class="p10 tr">
                    <el-button
                        size="small"
                        type="primary"
                        @click="model3=false"
                    >确定</el-button>
                </div>
            </template>
        </vxe-modal>
    </div>
</template>

<script>
import mqtt from 'mqtt'
import { getSxFR2TechList, delProcesLibraryChild, getTeam } from '_api/productionProcess/process'
import { getWelderList, getSxWelderList } from '_api/productionEquipment/production'
import { getChannelNoSourceArr, getWireDiameterArr, getGasesArr } from './common'
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
            //工艺
            model: false,
            tableData: [],
            total: 0,
            loading: false,

            //搜索条件设备型号
            //搜索条件
            searchObj: {
                equipType: ''
            },
            model2: false,
            loading2: false,
            modelArr2: [],
            list: [],
            //分页
            page: 1,
            total2: 0,
            // 级联下拉配置
            defalutProps: {
                label: 'name',
                value: 'id',
                children: 'list'
            },
            //机构数据
            teamArr: [],
            //记录父组件传入工艺库数据
            libray: {},

            //工艺选中数据
            selectTechnology: [],
            //设备选中数据
            selectEquipment: {},
            //发送mqtt数据
            newEqu: [],
            //记录发送mqtt总条数
            reqMqttNum: 0,
            //mqtt返回数据总条数
            backMqttNum: 0,
            //下发结果显示层
            model3: false,
            //订阅超时
            timeout: '',


        }
    },
    watch: {},
    computed: {},
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
                this.doSubscribe();

            })
            this.client.on('error', error => {
                console.log('连接失败', error)
            })
            this.client.on('message', (topic, message) => {
                if (topic == 'sxChannelParamReply') {
                    clearTimeout(this.timeout);
                    console.log(`${message}`)
                    var datajson = JSON.parse(`${message}`);
                    this.backMqttNum++;
                    this.newEqu.forEach(item => {
                        item.forEach(v => {
                            if (parseInt(v.gatherNo) === parseInt(datajson.gatherNo) && parseInt(v.channel) === parseInt(datajson.channel)) {
                                v.isSuccessStatus = datajson.flag === 0 ? 1 : 2;
                            }
                        })
                    });
                    this.issueTimeOut();
                }
            })
        },

        //订阅主题
        doSubscribe () {
            this.client.subscribe('sxChannelParamReply', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            })
        },

        doPublish (msg) {
            this.client.publish('sxFr2ChannelParamDownload', msg, 0)
        },


        init (row) {
            this.model = true;
            this.selectTechnology = [];
            this.selectEquipment = {};
            this.libray = { ...row };
            this.getExpandDetail(row.id);
        },
        //获取工艺列表
        async getExpandDetail (id) {
            this.loading = true;
            this.tableData = [];
            let req = {
                pn: 1,
                pageSize: 100,
                wpsLibraryId: id
            }
            let { data, code } = await getSxFR2TechList(req);
            this.loading = false;
            if (code == 200) {
                this.tableData = (data.list || []).sort((a, b) => {
                    return a.channel - b.channel
                });
                this.total = data.total || 0;
            }
        },
        //选择工艺后下一步
        nextFun () {
            if (this.selectTechnology.length > 0) {
                this.model2 = true;
                this.getList();
                this.$nextTick(() => {
                    this.$refs.vxeTable.clearCheckboxReserve();
                })
            } else {
                this.$message.error("请选择工艺")
            }
        },
        //获取设备
        async getList () {
            let req = {
                pn: this.page,
                ...this.searchObj
            }
            this.loading2 = true;
            let { data, code } = await getSxWelderList(req);
            this.loading2 = false;
            if (code == 200) {
                this.list = data.list
                this.total2 = data.total
            }
        },
        // 获取班组
        async getTeamList () {
            const { data, code } = await getTeam()
            this.teamArr = data.workArea || []
        },
        search () {
            this.page = 1;
            this.getList();
        },
        //分页
        handleCurrentChange (p) {
            this.page = p;
            this.getList();
        },
        //工艺选中数据
        selectTable1Fun ({ records }) {
            this.selectTechnology = records;
        },
        //组合工艺数据
        formatTechnoloay (arr) {
            let dataArray = (arr || []).map(item => {
                let obj = { ...item };
                // obj['gatherNo'] = "";//采集模块编号
                // obj['channelNo'] = item.channelNo;//通道号
                // obj['spotWeldTime'] = item.spotWeldingTime * 10;//点焊时间
                // obj['preflowTime'] = item.inAdvanceAspirated * 10;//提前送气
                // obj['initialEle'] = item.initialEle;//初期电流
                // obj['initialVol'] = item.initialVol * 10;//初期电压
                // obj['initialVolUnitary'] = item.initialVolUnitary;//初期电压一元
                // obj['weldElectricity'] = item.weldingEle;//焊接电流
                // obj['weldVoltage'] = item.weldingVol * 10;//焊接电压
                // obj['weldVoltageUnitary'] = item.weldingVolUnitary;//焊接电压一元
                // obj['extinguishArcEle'] = item.arcEle;//收弧电流
                // obj['extinguishArcVol'] = item.arcVol * 10;//收弧电压
                // obj['extinguishArcVolUnitary'] = item.arcVolUnitary;//收弧电压一元
                // obj['hysteresisAspirated'] = item.hysteresisAspirated * 10;//滞后送气
                // obj['arcPeculiarity'] = item.arcCharacter;//电弧特性
                // obj['gases'] = item.gases;//气体
                // obj['wireDiameter'] = item.weldingStickDiameter;//直径
                // obj['wireMaterials'] = item.weldingStickTexture;//材质
                // obj['weldProcess'] = item.weldingProcess;//焊接过程
                // obj['controlInfo'] = this.issueOrdersStr(item);//控制信息
                // obj['weldEleAdjust'] = item.weldingEleAdjust;//焊接电流微调
                // obj['weldVolAdjust'] = item.weldingVolAdjust * 10;//焊接电压微调
                // obj['extinguishArcEleAdjust'] = item.arcEleAdjust;//收弧电流微调
                // obj['extinguishArcVolAdjust'] = item.arcVolAdjust * 10;//收弧电压微调
                // obj['alarmsElectricityMax'] = item.alarmsEleMax;//报警电流上限
                // obj['alarmsElectricityMin'] = item.alarmsEleMin;//报警电流下限
                // obj['alarmsVoltageMax'] = item.alarmsVolMax;//报警电压上限
                // obj['alarmsVoltageMin'] = item.alarmsVolMin;//报警电压下限
                return obj;
            })
            return dataArray;
        },

        issueOrdersStr (item) {
            let bit0 = item.initialCondition;//初期条件
            let bit1 = item.controlArc;//收弧
            let bit4 = '0';//焊枪
            let bit5 = item.unitarySeveral;//一元/个别
            let bit6 = item.fusionControl;//熔深控制
            let bit7 = item.softArcSchema;//柔软电弧模式
            let str = bit7 + '' + bit6 + '' + bit5 + '' + bit4 + '' + bit1 + '' + bit0;
            return parseInt(str, 2);
        },

        //设备选中数据
        selectTable2Fun ({ records }) {
            this.selectEquipment['page' + this.page] = records;
        },
        //命令下发
        submitIssue () {
            this.doSubscribe();
            let equipmentArr = [];//选中的设备
            let iPNoArr = [];//选中设备的所有IP
            this.reqMqttNum = 0;//发送数量
            this.backMqttNum = 0;//返回数量
            //把分页中存储的选中机型取出
            for (let item in this.selectEquipment) {
                if (this.selectEquipment[item] && this.selectEquipment[item].length > 0) {
                    equipmentArr = [...equipmentArr, ...this.selectEquipment[item]]
                }
            }
            if (equipmentArr.length == 0) {
                return this.$message.error("请选择设备");
            }
            //检查选择的设备IP是否存在空值
            if (equipmentArr.filter(item => !item.weldIp || item.weldIp == '').length > 0) {
                return this.$message.error("选择的设备存在IP为空");
            }
            //取出选中设备所有IP
            equipmentArr.filter(item => item.weldIp).forEach(item => {
                iPNoArr = [...iPNoArr, ...item.weldIp.split(',')]
            });
            this.$confirm('确定要下发吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                //选择的工艺数据
                this.newEqu = []
                //选择的工艺数据
                let techArr = this.formatTechnoloay(this.selectTechnology);
                for (let i = 0, len = iPNoArr.length; i < len; i++) {
                    ((i) => {
                        this.newEqu.push({ 'weldIp': iPNoArr[i], 'weldInfo': [] })
                        setTimeout(() => {
                            clearTimeout(this.timeout);
                            let msgData = techArr.map(v => {
                                let objItem = { ...v };
                                objItem['weldIp'] = iPNoArr[i];
                                objItem['weldCid'] = equipmentArr[i].weldCid;
                                objItem['command'] = 2;
                                objItem['isSuccessStatus'] = 0;//记录发送状态
                                this.reqMqttNum++;//记录发送总条数

                                const msg = JSON.stringify(objItem);
                                this.doPublish(msg);
                                console.log(msg)

                                return objItem;
                            })
                            this.newEqu[i].weldInfo = msgData;
                            this.issueTimeOut();
                        }, (i + 1) * 300);
                    })(i)
                }
                this.model = false;
                this.model2 = false;
                this.model3 = true;
            }).catch(() => { })

        },

        //下发超时
        issueTimeOut () {
            this.timeout = setTimeout(() => {
                this.client.unsubscribe('sxChannelParamReply', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                })
                // this.client.end();
                if (this.backMqttNum !== this.reqMqttNum) {
                    this.$message.error("下发超时")
                    this.newEqu.forEach(item => {
                        item.weldInfo.forEach(v => {
                            v.isSuccessStatus = 2;
                        })
                    });
                    clearTimeout(this.timeout)
                }
            }, 5000)
        },

        //通道
        getChanneName (v) {
            return getChannelNoSourceArr(v)
        },
        //丝径
        getWireDiameterArrFun (v) {
            return getWireDiameterArr(v)
        },
        //气体
        getGasesArrFun (v) {
            return getGasesArr(v);
        }

    },
    created () {
        this.getTeamList();
        this.createConnection();
    },
    mounted () {

    }
}
</script>
<style>
.vxe-table--render-default .vxe-body--row.row--checked,
.vxe-table--render-default .vxe-body--row.row--radio {
    background-color: #aadef7;
}

.vxe-table--render-default .vxe-body--row.row--hover.row--checked,
.vxe-table--render-default .vxe-body--row.row--hover.row--radio {
    background-color: #54c2f5;
}
</style>
