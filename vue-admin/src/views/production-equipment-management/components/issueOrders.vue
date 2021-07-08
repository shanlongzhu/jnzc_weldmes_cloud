<!--
 * @Descripttion: 工艺库下发
 * @version: 
 * @Author: zhanganpeng
 * @Date: 2021-07-08 10:01:29
 * @LastEditors: zhanganpeng
 * @LastEditTime: 2021-07-08 16:47:09
-->

<template>
    <div>
        <vxe-modal
            title="选择工艺"
            v-model="model"
            width="700"
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
                        field="channelNo"
                        title="通道号"
                        min-width="60"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="initialCondition"
                        title="初期条件"
                        min-width="70"
                    >
                        <template #default="{row}">
                            {{row.initialCondition?'是':'否'}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="fusionControl"
                        title="熔深控制"
                        min-width="70"
                    >
                        <template #default="{row}">
                            {{row.fusionControl?'是':'否'}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="unitarySeveral"
                        title="一元/个别"
                        min-width="80"
                    >
                        <template #default="{row}">
                            {{row.unitarySeveral?'个别':'一元'}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="controlArc"
                        title="收弧"
                        min-width="70"
                    >
                        <template #default="{row}">
                            {{row.sysDictionary.valueName}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcCharacter"
                        title="电弧特性"
                        min-width="70"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="softArcSchema"
                        title="柔软电弧模式"
                        min-width="100"
                    >
                        <template #default="{row}">
                            {{row.softArcSchema?'是':'否'}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="weldingStickTexture"
                        title="焊丝材质"
                        min-width="90"
                    >
                        <template #default="{row}">
                            {{row.sysDictionary.valueNames}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="gases"
                        title="气体"
                        min-width="60"
                    >
                        <template #default="{row}">
                            {{row.sysDictionary.valueNamess}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="weldingStickDiameter"
                        title="焊丝直径"
                        min-width="70"
                    >
                        <template #default="{row}">
                            {{row.sysDictionary.valueNamesss}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="weldingProcess"
                        title="焊接过程"
                        min-width="70"
                    >
                        <template #default="{row}">
                            {{row.sysDictionary.valueNamessss}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="spotWeldingTime"
                        title="点焊时间"
                        min-width="70"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="inAdvanceAspirated"
                        title="提前送气"
                        min-width="70"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="hysteresisAspirated"
                        title="滞后送气"
                        min-width="70"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="initialEle"
                        title="初期电流"
                        min-width="70"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="initialVol"
                        title="初期电压"
                        min-width="70"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="initialVolUnitary"
                        title="初期电压一元"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="weldingEle"
                        title="焊接电流"
                        min-width="70"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="weldingVol"
                        title="焊接电压"
                        min-width="70"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="weldingVolUnitary"
                        title="焊接电压一元"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcEle"
                        title="收弧电流"
                        min-width="70"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcVol"
                        title="收弧电压"
                        min-width="70"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcVolUnitary"
                        title="收弧电压一元"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="weldingEleAdjust"
                        title="焊接电流微调"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="weldingVolAdjust"
                        title="焊接电压微调"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcEleAdjust"
                        title="收弧电流微调"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="arcVolAdjust"
                        title="收弧电压微调"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="alarmsEleMax"
                        title="报警电流上限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="alarmsEleMin"
                        title="报警电流下限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="alarmsVolMax"
                        title="报警电压上限"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="alarmsVolMin"
                        title="报警电压下限"
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
                <div class="top-con flex-n">
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
                </div>
                <vxe-table
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
                        field="machineNo"
                        title="固定资产编号"
                        width="100"
                    ></vxe-table-column>
                    <vxe-table-column
                        field="deptName"
                        title="设备类型"
                        width="100"
                    >
                        <template #default="{row}">
                            {{row.sysDictionary.valueName}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="welderName"
                        title="所属项目"
                        width="100"
                    >
                        <template #default="{row}">
                            {{row.sysDept.name}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="status"
                        title="状态"
                        width="60"
                    >
                        <template #default="{row}">
                            {{row.sysDictionary.valueNames}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="macPath"
                        title="厂家"
                        width="100"
                    >
                        <template #default="{row}">
                            {{row.sysDictionary.valueNamess}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="gatherNo"
                        title="采集序号"
                        min-width="100"
                    >
                        <template #default="{row}">
                            {{row.machineGatherInfo.gatherNo}}
                        </template>
                    </vxe-table-column>
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
    </div>
</template>

<script>
import mqtt from 'mqtt'
import { getProcesLibraryChild, delProcesLibraryChild, getTeam } from '_api/productionProcess/process'
import { getWelderList } from '_api/productionEquipment/production'
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
            subscribeSuccess: false,
            messageObj: '',
            //工艺
            model: false,
            tableData: [],
            total: 0,
            loading: false,

            //搜索条件设备型号
            //搜索条件
            searchObj: {
                grade: '',
                model: ''
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
            //接受mqtt返回
            backMqttData: [],

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
                if (topic == 'processIssueReturn') {
                    console.log(`${message}`)
                    this.backMqttData.push(`${message}`);
                    if (this.backMqttData.length == this.newEqu.length) {
                        //取消订阅
                        this.client.unsubscribe('processIssueReturn', error => {
                            if (error) {
                                console.log('取消订阅失败', error)
                            }
                        })
                    }
                    // console.log(this.backMqttData)
                }
            })
        },

        //订阅主题
        doSubscribe () {
            this.client.subscribe('processIssueReturn', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
                this.subscribeSuccess = true
            })
        },

        doPublish (msg) {
            console.log(msg)
            this.client.publish('processIssue', msg, 0)
        },


        init (row) {
            this.model = true;
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
                id: id
            }
            let { data, code } = await getProcesLibraryChild(req);
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
            req.grade = this.searchObj.grade && this.searchObj.grade.length > 0 ? this.searchObj.grade.slice(-1).join('') : ''
            this.loading2 = true;
            let { data, code } = await getWelderList(req);
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
                let obj = {};
                obj['gatherNo'] = "";//采集模块编号
                obj['channelNo'] = item.channelNo;//通道号
                obj['spotWeldTime'] = item.spotWeldingTime * 10;//点焊时间
                obj['preflowTime'] = item.inAdvanceAspirated * 10;//提前送气
                obj['initialEle'] = item.initialEle;//初期电流
                obj['initialVol'] = item.initialVol * 10;//初期电压
                obj['initialVolUnitary'] = item.initialVolUnitary;//初期电压一元
                obj['weldElectricity'] = item.weldingEle;//焊接电流
                obj['weldVoltage'] = item.weldingVol * 10;//焊接电压
                obj['weldVoltageUnitary'] = item.weldingVolUnitary;//焊接电压一元
                obj['extinguishArcEle'] = item.arcEle;//收弧电流
                obj['extinguishArcVol'] = item.arcVol * 10;//收弧电压
                obj['extinguishArcVolUnitary'] = item.arcVolUnitary;//收弧电压一元
                obj['hysteresisAspirated'] = item.hysteresisAspirated * 10;//滞后送气
                obj['arcPeculiarity'] = item.arcCharacter;//电弧特性
                obj['gases'] = item.gases;//气体
                obj['wireDiameter'] = item.weldingStickDiameter;//直径
                obj['wireMaterials'] = item.weldingStickTexture;//材质
                obj['weldProcess'] = item.weldingProcess;//焊接过程
                obj['controlInfo'] = this.issueOrdersStr(item);//控制信息
                obj['weldEleAdjust'] = item.weldingEleAdjust;//焊接电流微调
                obj['weldVolAdjust'] = item.weldingVolAdjust * 10;//焊接电压微调
                obj['extinguishArcEleAdjust'] = item.arcEleAdjust;//收弧电流微调
                obj['extinguishArcVolAdjust'] = item.arcVolAdjust * 10;//收弧电压微调
                obj['alarmsElectricityMax'] = item.alarmsEleMax;//报警电流上限
                obj['alarmsElectricityMin'] = item.alarmsEleMin;//报警电流下限
                obj['alarmsVoltageMax'] = item.alarmsVolMax;//报警电压上限
                obj['alarmsVoltageMin'] = item.alarmsVolMin;//报警电压下限
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
            let equipmentArr = []
            for (let item in this.selectEquipment) {
                if (this.selectEquipment[item] && this.selectEquipment[item].length > 0) {
                    equipmentArr = [...equipmentArr, ...this.selectEquipment[item]]
                }
            }
            //检查选择的设备采集编号是否存在空值
            if (equipmentArr.filter(item => !item.machineGatherInfo.gatherNo || item.machineGatherInfo.gatherNo == '').length > 0) {
                return this.$message.error("选择的设备存在采集序号为空");
            }

            //选择的工艺数据
            let techArr = this.formatTechnoloay(this.selectTechnology);
            this.newEqu = [];
            equipmentArr.forEach(item => {
                techArr.forEach(v => {
                    let objItem = { ...v };
                    objItem['gatherNo'] = item.machineGatherInfo.gatherNo;
                    this.newEqu.push(objItem);
                })
            });
            const msg = JSON.stringify(this.newEqu);
            this.$confirm('确定要下发吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                this.createConnection();
                setTimeout(() => {
                    this.doPublish(msg);
                }, 500)

            }).catch(() => { })

        }
    },
    created () {
        this.getTeamList();
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
