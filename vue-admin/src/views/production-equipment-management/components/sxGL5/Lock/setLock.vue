<!--
 * @Descripttion: 密码下发
 * @version: 
 * @Author: zhanganpeng
 * @Date: 2021-07-08 10:01:29
 * @LastEditors: zhanganpeng
 * @LastEditTime: 2021-09-15 13:18:06
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
                        <span>设备型号：</span>
                        <el-select
                            v-model="searchObj.equipType"
                            size="small"
                            class="w150"
                            clearable
                            placeholder="请选择"
                        >
                            <el-option
                                v-for="item in modelArr2"
                                :key="item.id"
                                :label="item.valueName"
                                :value="item.id"
                            />
                        </el-select>
                    </div>
                    <div class="con-w">
                        <el-button
                            size="small"
                            icon="el-icon-search"
                            type="primary"
                            @click="search"
                        >搜索</el-button>
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

        <!-- 选择通道 -->
        <vxe-modal
            :title="lockTxt+' - 通道选择'"
            v-model="model1"
            width="300"
        >
            <template #default>
                <div class="p10">
                    <el-radio-group v-model="channelSelect">
                        <el-radio :label="0">焊接通道</el-radio>
                        <el-radio :label="1">PC通道</el-radio>
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
                        field="weldCid"
                        title="CID"
                        min-width="100"
                    >
                    </vxe-table-column>
                    <vxe-table-column
                        field="weldType"
                        title="下发命令"
                        min-width="100"
                    >
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
import { getDictionaries } from '_api/productionProcess/process'
import { getSxWelderList } from '_api/productionEquipment/production'
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
            //通道
            model1: false,
            channelSelect: 0,
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

            //设备选中数据
            selectEquipment: {},
            //选中设备的所有采集编号
            weldCidArr: [],
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

            //解锁/锁定 0解锁 1锁定
            lockStatus: 0,
            lockTxt: '',
            //搜索条件设备型号
            modelArr2: [],


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
                if (topic == 'jnSxGL5WeldChannelSetOrReadReturn') {
                    clearTimeout(this.timeout);
                    var datajson = JSON.parse(`${message}`);
                    this.backMqttNum++;
                    this.newEqu.forEach(item => {
                        if (parseInt(item.weldCid) === parseInt(datajson.weldCid)) {
                            item.isSuccessStatus =  0;
                        }
                    });
                    this.issueTimeOut();
                }
            })
        },

        //订阅主题
        doSubscribe () {
            this.client.subscribe('jnSxGL5WeldChannelSetOrReadReturn', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            })
        },

        doPublish (msg) {
            this.client.publish('jnSxGl5WeldChannelSet', msg, 0)
        },


        init (v) {
            //v 1锁定 0解锁
            this.lockTxt = v ? '锁定' : '解锁';
            this.model2 = true;
            this.lockStatus = v;
            this.selectEquipment = {};
            this.getDicFun();
            this.getList();
            this.$nextTick(() => {
                this.$refs.vxeTable.clearCheckboxReserve();
            })
        },

        //获取数据字典
        async getDicFun () {
            let { data, code } = await getDictionaries({ "types": ["6"] });
            if (code == 200) {
                this.modelArr2 = data['6'] || [];
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
            this.weldCidArr = [];//选中设备的所有CID
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
            //检查选择的设备CID是否存在空值
            if (equipmentArr.filter(item => !item.weldCid || item.weldCid == '').length > 0) {
                return this.$message.error("选择的设备存在CID为空");
            }
            //取出选中设备所有CID
            equipmentArr.filter(item => item.weldCid).forEach(item => {
                this.weldCidArr = [...this.weldCidArr, ...item.weldCid.split(',')]
            });

            this.model1 = true;

        },
        //密码下发
        passwordIssueFun () {
            this.$confirm('确定要下发吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                //选择的工艺数据
                this.newEqu = []
                for (let i = 0, len = this.weldCidArr.length; i < len; i++) {
                    this.newEqu.push({ 'weldCid': this.weldCidArr[i], 'isSuccessStatus': 2, 'weldType': this.lockTxt })
                    clearTimeout(this.timeout);
                    let objItem = {};
                    objItem['weldCid'] = this.weldCidArr[i];
                    objItem['readWriteFlag'] = 2;
                    objItem['function'] = this.lockStatus;
                    objItem['channelSelect'] = this.channelSelect;
                    this.reqMqttNum++;//记录发送总条数
                    const msg = JSON.stringify(objItem);
                    this.doPublish(msg);
                    this.issueTimeOut();
                }
                this.model1 = false;
                this.model2 = false;
                this.model3 = true;
            }).catch(() => { })
        },

        //下发超时
        issueTimeOut () {
            this.timeout = setTimeout(() => {
                this.client.unsubscribe('jnSxGL5WeldChannelSetOrReadReturn', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                })
                // this.client.end();
                this.newEqu.forEach(item => {
                    if (item.isSuccessStatus !== 0) {
                        item.isSuccessStatus = 1;
                    }
                });
                clearTimeout(this.timeout)
            }, 5000)
        },

    },
    created () {
    },
    mounted () {
        this.createConnection();
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
