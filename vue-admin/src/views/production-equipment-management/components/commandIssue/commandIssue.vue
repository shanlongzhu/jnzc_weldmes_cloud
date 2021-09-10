<!--
 * @Descripttion: 密码下发
 * @version: 
 * @Author: zhanganpeng
 * @Date: 2021-07-08 10:01:29
 * @LastEditors: zhanganpeng
 * @LastEditTime: 2021-09-09 15:43:12
-->

<template>
    <div>
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

        <!-- 密码层 -->
        <vxe-modal
            title="选择命令"
            v-model="model1"
            width="400"
        >
            <template #default>
                <div class="p10">
                    <el-radio-group v-model="commandNo">
                        <el-radio :label="0">工作自由调节</el-radio>
                        <el-radio :label="1">工作不可自由调节</el-radio>
                    </el-radio-group>
                </div>
                <div class="p10 tc">
                    <el-button
                        size="small"
                        type="primary"
                        @click="passwordIssueFun"
                    >确定</el-button>
                    <el-button
                        size="small"
                        @click="model1=false"
                    >取消</el-button>
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
                        field="gatherNo"
                        title="采集序号"
                        min-width="100"
                    >
                        <template #default="{row}">
                            {{row.gatherNo}}
                        </template>
                    </vxe-table-column>
                    <vxe-table-column
                        field="isSuccessStatus"
                        title="下发状态"
                        min-width="100"
                    >
                        <template #default="{row}">
                            <span
                                style="color:#13ce66"
                                v-if='row.isSuccessStatus===0'
                            >
                                成功
                            </span>
                            <span
                                style="color:#f00"
                                v-else-if='row.isSuccessStatus===1'
                            >
                                失败
                            </span>
                            <span v-else>
                                发送等待中...
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
            //密码
            model1: false,
            commandNo: 0,
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
            //设备选中数据
            selectEquipment: {},
            //选中设备的所有采集编号
            gatherNoArr: [],
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
                if (topic == 'commandReturn') {
                    clearTimeout(this.timeout);
                    var datajson = JSON.parse(`${message}`);
                    this.backMqttNum++;
                    this.newEqu.forEach(item => {
                        if (parseInt(item.gatherNo) === parseInt(datajson.gatherNo)) {
                            item.isSuccessStatus = datajson.flag === 0 ? 0 : 1;
                        }
                    });
                    this.issueTimeOut();
                }
            })
        },

        //订阅主题
        doSubscribe () {
            this.client.subscribe('commandReturn', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            })
        },

        doPublish (msg) {
            this.client.publish('commandIssue', msg, 0)
        },


        init () {
            this.model2 = true;
            this.selectEquipment = {};
            this.getList();
            this.$nextTick(() => {
                this.$refs.vxeTable.clearCheckboxReserve();
            })
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


        //设备选中数据
        selectTable2Fun ({ records }) {
            this.selectEquipment['page' + this.page] = records;
        },
        //命令下发
        submitIssue () {
            this.doSubscribe();
            let equipmentArr = [];//选中的设备
            this.gatherNoArr = [];//选中设备的所有采集编号
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
            //检查选择的设备采集编号是否存在空值
            if (equipmentArr.filter(item => !item.machineGatherInfo.gatherNo || item.machineGatherInfo.gatherNo == '').length > 0) {
                return this.$message.error("选择的设备存在采集序号为空");
            }
            //取出选中设备所有采集编号
            equipmentArr.filter(item => item.machineGatherInfo.gatherNo).forEach(item => {
                this.gatherNoArr = [...this.gatherNoArr, ...item.machineGatherInfo.gatherNo.split(',')]
            });

            this.model1 = true;
            this.commandNo = 0;

        },
        //控制命令下发
        passwordIssueFun () {
            this.$confirm('确定要下发吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                //选择的工艺数据
                this.newEqu = []
                for (let i = 0, len = this.gatherNoArr.length; i < len; i++) {
                    ((i) => {
                        this.newEqu.push({ 'gatherNo': this.gatherNoArr[i], 'isSuccessStatus': 2 })
                        setTimeout(() => {
                            clearTimeout(this.timeout);
                            let objItem = {};
                            objItem['gatherNo'] = this.gatherNoArr[i];
                            objItem['command'] = this.commandNo;
                            this.reqMqttNum++;//记录发送总条数
                            const msg = JSON.stringify(objItem);
                            this.doPublish(msg);
                            console.log(msg)
                            this.issueTimeOut();
                        }, (i + 1) * 300);
                    })(i)
                }
                this.model1 = false;
                this.model2 = false;
                this.model3 = true;
            }).catch(() => { })
        },

        //下发超时
        issueTimeOut () {
            this.timeout = setTimeout(() => {
                this.client.unsubscribe('commandReturn', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                })
                // this.client.end();
                if (this.backMqttNum !== this.reqMqttNum) {
                    this.$message.error("下发超时")
                    this.newEqu.forEach(item => {
                        item.isSuccessStatus = 1;
                    });
                    clearTimeout(this.timeout)
                }
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
