<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-plus"
                    @click="addFun"
                >新增工艺库</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-document-add"
                >控制命令下发</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-document-remove"
                >历史下发查询</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-printer"
                >松下焊机通道锁定</el-button>
            </div>
            <div class="con-w">
                <el-button
                    size="small"
                    type="success"
                    plain
                >松下焊机通道解锁</el-button>
            </div>
        </div>
        <div
            class="table-con"
            style="flex:1;height:0px"
        >

            <vxe-table
                border
                :data="list"
                size="mini"
                height="auto"
                :loading="loading"
                auto-resize
                :expand-config="{toggleMethod: expandChange}"
            >
                <vxe-table-column
                    type="expand"
                    width="60"
                >
                    <template #content="{ row }">
                        <expand-table
                            :id="row.id"
                            @editDetail="editDetailFun"
                            @reload="getList"
                        ></expand-table>
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="wpsName"
                    title="工艺名称"
                ></vxe-table-column>
                <vxe-table-column
                    field="weldModel"
                    title="焊机型号"
                >
                <template #default={row}>
                    {{row.sysDictionary.valueName}}
                </template>
                </vxe-table-column>
                <vxe-table-column
                    field="createTime"
                    title="创建日期"
                ></vxe-table-column>
                <vxe-table-column
                    field="createTime"
                    title="操作"
                    width="350"
                >
                    <template #default="{row}">
                        <el-button
                            size="mini"
                            type="warning"
                            plain
                            @click="issueOrders(row)"
                        >
                            工艺库下发
                        </el-button>
                        <el-button
                            size="mini"
                            type="success"
                            plain
                            @click="addLibraryFun(row.id)"
                        >
                            新增工艺
                        </el-button>
                        <el-button
                            size="mini"
                            type="primary"
                            plain
                            @click="editFun(row.id)"
                        >
                            修改
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            plain
                            @click="delFun(row.id)"
                        >
                            删除
                        </el-button>
                    </template>
                </vxe-table-column>
            </vxe-table>
        </div>
        <el-pagination
            class="p10"
            :current-page.sync="page"
            :page-size="10"
            align="right"
            background
            layout="total, prev, pager, next"
            :total="total"
            @current-change="handleCurrentChange"
        />

        <!-- 新增/修改 工艺库-->
        <el-dialog
            :title="title"
            :visible.sync="visable1"
            :close-on-click-modal="false"
            width="600px"
        >
            <el-form
                ref="ruleForm"
                :model="ruleForm"
                :rules="rules"
                label-width="120px"
                class="demo-ruleForm"
            >
                <el-form-item
                    label="工艺库名称"
                    prop="wpsName"
                >
                    <el-input
                        style="width:250px"
                        v-model="ruleForm.wpsName"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="厂家"
                    prop="firmId"
                >
                    <el-select
                        v-model="ruleForm.firmId"
                        placeholder="请选择"
                        style="width:250px"
                        @change="changeFirm"
                    >
                        <el-option
                            v-for="item in manufactorArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="设备型号"
                    prop="weldModel"
                >
                    <el-select
                        v-model="ruleForm.weldModel"
                        placeholder="请选择"
                        style="width:250px"
                    >
                        <el-option
                            v-for="item in modelArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>

                <el-form-item
                    class="mt10 tc"
                    label-width="0"
                >
                    <el-button
                        type="primary"
                        @click="submitForm('ruleForm')"
                    >保存</el-button>
                    <el-button @click="visable1 = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <!-- 工艺明细 -->
        <add-tech
            ref="addTech"
            @throwData="submitLibary"
        ></add-tech>
        <!-- 工艺库下发 -->
        <issue-orders ref="issueOrdersRef"></issue-orders>
    </div>
</template>

<script>
import PahoMQTT from 'paho-mqtt'
import { getTeam, getDictionaries, getProcesLibraryList, getProcesLibraryDetail, delProcesLibrary, editProcesLibrary, addProcesLibrary, getProcesLibraryChildDetail, getChannNos, addProcesLibraryChild, editProcesLibraryChild, getProcesLibraryChild } from '_api/productionProcess/process'
import { getWeldingModel } from '_api/productionEquipment/production'
import expandTable from './components/expandTable.vue';
import AddTech from './components/addTech.vue';
import IssueOrders from './components/issueOrders.vue';

export default {
    components: { expandTable, AddTech, IssueOrders },
    data () {
        return {
            visable1: false,
            title: '新增工艺',
            ruleFormObj: {
            },
            ruleForm: {
                wpsName: '',//工艺库名称
                weldModel: '',//设备型号
                firmId: ''
            },
            rules: {
                welderNo: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                firmId: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                weldModel: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ]
            },
            //设备型号
            modelArr: [],
            //厂家
            manufactorArr: [],
            list: [],
            page: 1,
            total: 0,
            loading: false,

            //
            mqttClient: {},
            messageObj: ''
        }
    },

    created () {
        this.ruleFormObj = {...this.ruleForm};
        this.getDicFun();
        this.getList();
    },
    mounted () {
        // this.PahoMQTTFun();
    },
    methods: {
        //建立连接
        PahoMQTTFun () {
            const time = new Date().getTime();
            const clientId = "adminTest" + time;
            this.mqttClient = new PahoMQTT.Client(process.env.VUE_APP_MQTT_API, 8083, clientId);
            let options = {
                timeout: 50,
                keepAliveInterval: 60,
                cleanSession: false,
                useSSL: false,
                onSuccess: this.onConnect,
                onFailure: function (e) {
                    console.log(e);
                },
                reconnect: true
            }
            this.mqttClient.onConnectionLost = this.onConnectionLost;
            this.mqttClient.connect(options);
        },

        //连接成功
        onConnect () {
            console.log("mqttClient onConnect Success");
        },

        //连接失败
        onConnectionLost (responseObject) {
            console.log("onConnectionLost:" + responseObject.errorMessage);
        },
        async issueOrders (row) {
            this.$refs.issueOrdersRef.init(row);
            return
            this.$confirm('确定要下发吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let { data, code } = await getProcesLibraryChild({ id:row.id });
                if (code == 200) {
                    if (data.list && data.list.length > 0) {
                        this.messageObj = this.$message({
                            message: '下发中....',
                            type: 'warning',
                            duration: 0
                        });
                        this.testProcessIssue(data.list)
                    } else {
                        this.$message.error("该工艺库没有工艺可下发")
                    }

                }
            }).catch(() => { })

        },

        testProcessIssue (arr) {
            let dataArray = (arr || []).map(item => {
                let obj = {};
                obj['gatherNo'] = "0011";//采集模块编号
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
            //集合转字符串
            const message = JSON.stringify(dataArray);
            console.log(message)
            //工艺参数下发测试
            // const topic = new PahoMQTT.Message(message);
            // topic.destinationName = "processIssue";
            // mqttClient.send(topic);

            console.log(this.mqttClient)
            this.mqttClient.publish('processIssue', message, 0)

            this.testIssueReturn();
        },

        //测试下发返回
        testIssueReturn () {
            //主题订阅
            this.mqttClient.subscribe("processIssueReturn", {
                qos: 0,
                onSuccess: function (e) {
                    console.log("下发返回主题订阅成功：processIssueReturn");
                },
                onFailure: function (e) {
                    console.log(e);
                }
            });
            //5秒后执行下发超时显示
            var onTaskTimeout = window.setTimeout(function () {
                this.mqttClient.unsubscribe("processIssueReturn", {
                    onSuccess: function (e) {
                        console.log("下发返回主题取消订阅成功");
                    },
                    onFailure: function (e) {
                        console.log(e);
                    }
                });
                alert("下发超时");
            }, 5000);
            this.mqttClient.onMessageArrived = function (message) {
                console.log(message)
                if ("processIssueReturn" === message.destinationName) {
                    var datajson = JSON.parse(message.payloadString);
                    if (datajson.flag === 0) {
                        alert("采集编号：" + datajson.gatherNo + "--通道：" + datajson.channelNo + "-->下发成功");
                    } else {
                        alert("采集编号：" + datajson.gatherNo + "--通道：" + datajson.channelNo + "-->下发失败");
                    }
                    this.mqttClient.unsubscribe("processIssueReturn", {
                        onSuccess: function (e) {
                            console.log("下发返回主题取消订阅成功");
                        },
                        onFailure: function (e) {
                            console.log(e);
                        }
                    });
                    //收到消息后清除定时器
                    window.clearTimeout(onTaskTimeout);
                }
            }
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



        //获取数据字典
        async getDicFun () {
            let { data, code } = await getDictionaries({ "types": ["5"] });
            if (code == 200) {
                this.manufactorArr = data['5'] || [];
            }
        },

        search () {
            this.page = 1;
            this.getList();
        },
        //获取列表
        async getList () {
            let req = {
                pn: this.page
            }
            this.loading = true;
            let { data, code } = await getProcesLibraryList(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list || []
                this.total = data.total
            }
        },
        //新增
        addFun () {
            this.title = "新增工艺库"
            this.visable1 = true;
            this.modelArr = [];
            this.$nextTick(() => {
                this.$refs.ruleForm.resetFields();
                this.ruleForm = { ...this.ruleFormObj };
                Reflect.deleteProperty(this.ruleForm, "id");
            })
        },

        //根据厂家获取设备型号
        changeFirm (id) {
            this.ruleForm.weldModel = "";
            this.getWeldingModel(id);
        },
        async getWeldingModel (id) {
            let { data, code } = await getWeldingModel({ id });
            if (code == 200) {
                this.modelArr = data || [];
            }
        },
        //获取工艺库明细
        async editFun (id) {
            this.title = "修改工艺库"
            this.ruleForm = { ...this.ruleFormObj };
            let { data, code } = await getProcesLibraryDetail(id);
            if (code == 200) {
                this.visable1 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm.resetFields();
                    this.ruleForm = data[0] || {};
                    this.getWeldingModel(this.ruleForm.firmId);
                })
            }
        },

        //删除
        delFun (id) {
            this.$confirm('确定要删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let { code, data } = await delProcesLibrary({ id })
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.getList()
                }
            }).catch(() => { })

        },

        //展开表格行
        expandChange ({ row, expanded }) {
            return true
        },


        handleCurrentChange (pn) {
            this.page = pn;
            this.getList();
        },

        // 新增/编辑提交工艺库
        submitForm (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty('id')) {
                        const req = { ...this.ruleForm }
                        const { data, code } = await editProcesLibrary(req)
                        if (code == 200) {
                            this.$message.success('修改成功')
                            this.visable2 = false
                            this.getList()
                        }
                    } else {
                        const req = { ...this.ruleForm }
                        const { data, code } = await addProcesLibrary(req);
                        if (code == 200) {
                            this.$message.success('新增成功')
                            this.visable2 = false
                            this.getList()
                        }
                    }
                } else {
                    console.log('error submit!!')
                    return false
                }
            })
        },

        //新增工艺
        addLibraryFun (id) {
            this.$refs.addTech.addLibraryFun(id);
        },


        //子组件调用修改
        async editDetailFun (obj) {
            this.$refs.addTech.editDetailFun(obj);
        },

        // 新增/编辑提交工艺
        async submitLibary (vData) {
            const req = { ...vData }
            req.initialCondition = req.initialCondition ? 1 : 0;
            req.fusionControl = req.fusionControl ? 1 : 0;
            req.softArcSchema = req.softArcSchema ? 1 : 0;
            if (req.hasOwnProperty('id')) {
                const { data, code } = await editProcesLibraryChild(req)
                if (code == 200) {
                    this.$message.success('修改成功')
                    console.log(this.$refs.addTech)
                    this.$refs.addTech.visable2 = false
                    this.getList()
                }
            } else {
                const { data, code } = await addProcesLibraryChild(req);
                if (code == 200) {
                    this.$message.success('新增成功')
                    this.$refs.addTech.visable2 = false
                    this.getList()
                }
            }
        },




    }
}
</script>

<style>
.procces-wrap .el-input-number .el-input__inner {
    text-align: left;
}
.procces-wrap .el-form-item {
    margin-bottom: 0px;
}
.procces-wrap .el-form-item * {
    font-size: 12px;
}
</style>
