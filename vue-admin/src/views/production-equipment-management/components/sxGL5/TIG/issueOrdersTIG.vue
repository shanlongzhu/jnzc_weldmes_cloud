<!--
 * @Descripttion: TIG工艺库下发
 * @version:
 * @Author: zhanganpeng
 * @Date: 2021-07-08 10:01:29
 * @LastEditors: zhanganpeng
 * @LastEditTime: 2021-09-15 13:25:46
-->

<template>
    <div>
        <vxe-modal
            title="选择工艺TIG"
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
                        title="序号TIG"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="channelNo"
                        title="通道号"
                        min-width="60"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="initialEleMax"
                        title="初期电流上限"
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
                        field="initialVolMax"
                        title="初期电压上限"
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
                        field="firstWeldEleMax"
                        title="第一焊接电流上限"
                        min-width="120"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="firstWeldEleMin"
                        title="第一焊接电流下限"
                        min-width="120"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="firstWeldVolMax"
                        title="第一焊接电压上限"
                        min-width="120"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="firstWeldVolMin"
                        title="第一焊接电压下限"
                        min-width="120"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="secondWeldEleMax"
                        title="第二焊接电流上限"
                        min-width="120"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="secondWeldEleMin"
                        title="第二焊接电流下限"
                        min-width="120"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="secondWeldVolMax"
                        title="第二焊接电压上限"
                        min-width="120"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="secondWeldVolMin"
                        title="第二焊接电压下限"
                        min-width="120"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcEleMax"
                        title="收弧电流上限"
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
                        field="arcVolMax"
                        title="收弧电压上限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcVolMin"
                        title="收弧电压下限"
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
                <!-- <div class="top-con flex-n">
                    <div class="con-w">
                        <span>班组：</span>
                        <el-cascader
                            v-model="searchObj.grade"
                            size="small"
                            style="width:180px"
                            clearable
                            :options="teamArr"
                            :props="defalutProps"
                            :show-all-levels="false"
                            @change="search"
                        />
                    </div>
                </div> -->
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
                        field="channelNo"
                        title="下发通道号"
                        min-width="100"
                    >
                        <template #default="{row}">
                            {{row.weldInfo.map(item => item.channelNo).join('，')}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="channelNo"
                        title="下发成功通道号"
                        min-width="100"
                    >
                        <template #default="{row}">
                            <span style="color:#13ce66">
                                {{row.weldInfo.filter(item => item.isSuccessStatus==1).map(item=>item.channelNo).join('，')}}
                            </span>
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="channelNo"
                        title="下发失败通道号"
                        min-width="100"
                    >
                        <template #default="{row}">
                            <span style="color:#f00">
                                {{row.weldInfo.filter(item => item.isSuccessStatus==2).map(item => item.channelNo).join('，')}}
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
import { getSxTIGTechList, delProcesLibraryChild, getTeam } from '_api/productionProcess/process'
import { getWelderList, getSxWelderList } from '_api/productionEquipment/production'
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
                cleanSession: true,
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
                // grade: '',
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
          messageObj:''


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
                if (topic == 'jnSxGL5ProcessIssueReturn') {
                    clearTimeout(this.timeout);
                    var datajson = JSON.parse(`${message}`);
                    this.backMqttNum++;
                    this.newEqu.forEach(item => {
                        item.weldInfo.forEach(v => {
                            if (parseInt(v.weldCid) === parseInt(datajson.weldCid) && parseInt(v.channelNo) === parseInt(datajson.channelNo)) {
                                v.isSuccessStatus = 1;
                            }
                        })
                    });
                    this.issueTimeOut();
                }
            })
        },

        //订阅主题
        doSubscribe () {
            this.client.subscribe('jnSxGL5ProcessIssueReturn', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            })
        },

        doPublish (msg) {
            this.client.publish('jnSxGl5TigProcessIssue', msg, 0)
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
            let { data, code } = await getSxTIGTechList(req);
            this.loading = false;
            if (code == 200) {
                this.tableData = (data.list || []).sort((a, b) => {
                    return a.channelNo - b.channelNo
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
            // req.model = this.libray.weldModel;
            // req.grade = this.searchObj.grade && this.searchObj.grade.length > 0 ? this.searchObj.grade.slice(-1).join('') : ''
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
              this.messageObj = this.$message({
                message:'下发中...',
                duration:0,
                type:'warning'
              });
                //选择的工艺数据
                this.newEqu = []
                //选择的工艺数据
                let techArr = this.formatTechnoloay(this.selectTechnology);
                for (let i = 0, len = iPNoArr.length; i < len; i++) {
                    this.newEqu.push({ 'weldIp': iPNoArr[i], 'weldInfo': [] });
                    clearTimeout(this.timeout);
                    let msgData = techArr.map(v => {
                        let objItem = { ...v };
                        objItem['weldIp'] = iPNoArr[i];
                        objItem['weldCid'] = equipmentArr[i].weldCid;
                        objItem['isSuccessStatus'] = 0;//记录发送状态
                        this.reqMqttNum++;//记录发送总条数

                        const msg = JSON.stringify(objItem);
                        this.doPublish(msg);

                        return objItem;
                    })
                    this.newEqu[i].weldInfo = msgData;
                    this.issueTimeOut();
                }
                this.model = false;
                this.model2 = false;
                this.model3 = true;
            }).catch(() => { })

        },

        //下发超时
        issueTimeOut () {
            this.timeout = setTimeout(() => {
                this.client.unsubscribe('jnSxGL5ProcessIssueReturn', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                })
                // this.client.end();
              this.messageObj.close();
              this.$message.info('下发完成');
                //将没有匹配到返回成功的 改为失败
                this.newEqu.forEach(item => {
                    item.weldInfo.forEach(v => {
                        if (v.isSuccessStatus != 1) {
                            v.isSuccessStatus = 2;
                        }
                    })
                });
                clearTimeout(this.timeout)
            }, 5000)
        },

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
