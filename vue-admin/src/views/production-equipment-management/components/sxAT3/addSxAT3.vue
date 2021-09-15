<template>
    <div>
        <!-- 新增/修改工艺 -->
        <el-dialog
            :title="title2"
            :visible.sync="visable2"
            :close-on-click-modal="false"
            width="1000px"
            class="procces-wrap"
        >
            <el-form
                ref="ruleForm2"
                :model="ruleForm2"
                :rules="rules2"
                :show-message="false"
                label-width="120px"
                class="demo-ruleForm"
            >
                <el-row>
                    <el-col :span="6">
                        <el-form-item
                            label="通道号"
                            prop="channel"
                        >
                            <el-select
                                v-model="ruleForm2.channel"
                                placeholder="请选择"
                                style="width:110px"
                                size="mini"
                            >
                                <el-option
                                    v-for="item in channelNoArr"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <!-- <el-col :span="6">
                        <el-form-item
                            label="控制"
                            prop="command"
                        >
                            <el-select
                                v-model="ruleForm2.command"
                                placeholder="请选择"
                                style="width:110px"
                                size="mini"
                            >
                                <el-option
                                    v-for="item in commandArr"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>

                        </el-form-item>
                    </el-col> -->
                    <el-col :span="6">
                        <el-form-item
                            label="通道标志"
                            prop="channelFlag"
                        >
                            <el-select
                                v-model="ruleForm2.channelFlag"
                                placeholder="请选择"
                                style="width:110px"
                                size="mini"
                            >
                                <el-option
                                    v-for="item in channelFlagArr"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value"
                                />
                            </el-select>

                        </el-form-item>
                    </el-col>
                </el-row>
                <div class="border-tip">
                    <span class="border-tip-txt">参数</span>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="预置电流上限"
                                prop="presetEleMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.presetEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="预置电流下限"
                                prop="presetEleMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.presetEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="预置电压上限"
                                prop="presetVolMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.presetVolMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="预置电压下限"
                                prop="presetVolMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.presetVolMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="电流报警上限"
                                prop="eleAlarmMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.eleAlarmMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="电流报警下限"
                                prop="eleAlarmMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.eleAlarmMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="电压报警上限"
                                prop="volAlarmMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.volAlarmMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="电压报警下限"
                                prop="volAlarmMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.volAlarmMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="报警延时时间"
                                prop="alarmDelayTime"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.alarmDelayTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="报警停机时间"
                                prop="alarmHaltTime"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.alarmHaltTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
                <el-form-item
                    class="mt10 tc"
                    label-width="0"
                >
                    <el-button
                        type="primary"
                        @click="requestSpec"
                        :loading="issLoading"
                    >索取规范</el-button>
                    <el-button
                        type="primary"
                        @click="submitForm2('ruleForm2')"
                    >保存</el-button>
                    <el-button @click="visable2 = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 索取规范 选择设备 -->
        <el-dialog
            title="选择设备"
            :visible.sync="model2"
            :close-on-click-modal="false"
            width="800px"
            class="procces-wrap"
        >
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
                        popper-class="teamList"
                    />
                </div>
            </div> -->
            <vxe-table
                border
                show-overflow
                auto-resize
                size="mini"
                height="300"
                :loading="loading2"
                highlight-hover-row
                highlight-current-row
                resizable
                stripe
                :data="list"
                row-id="id"
                :radio-config="{ trigger: 'row',highlight: true}"
                @cell-click="radioChangeEvent"
                ref="proModelTable"
            >
                <vxe-table-column
                    type="radio"
                    title="请选择"
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
        </el-dialog>
    </div>
</template>

<script>
import mqtt from 'mqtt'
import { getTeam, getAT3ChannaNoIsUse, addSxAT3Tech, getSxAT3TechDetail, editSxAT3TechDetail } from '_api/productionProcess/process'

import { getWelderList, getSxWelderList } from '_api/productionEquipment/production'
export default {
    name: 'addSxAT3',
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


            //工艺层
            visable2: false,
            title2: '新增工艺AT3',
            ruleFormObj2: {},
            ruleForm2: {
                //松下AT3工艺
                channel: '0',//通道编号
                command: '',//控制参数
                channelFlag: 0,//通道标志（0~9）

                //*** 预置参数 */
                presetEleMax: 0,//预置电流上限
                presetVolMax: 0,//预置电压上限
                presetEleMin: 0,//预置电流下限
                presetVolMin: 0,//预置电压下限
                eleAlarmMax: 0,//电流报警上限
                volAlarmMax: 0,//电压报警上限
                eleAlarmMin: 0,//电流报警下限
                volAlarmMin: 0,//电压报警下限
                alarmDelayTime: 0,//报警延时时间
                alarmHaltTime: 0,//报警停机时间
            },
            rules2: {
                channel: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                command: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                channelFlag: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                // presetEleMax: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // presetVolMax: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // presetEleMin: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // presetVolMin: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // eleAlarmMax: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // volAlarmMax: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // eleAlarmMin: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // volAlarmMin: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // alarmDelayTime: [
                //     { required: true, message: '不能为空', trigger: 'change' }
                // ],
                // alarmHaltTime: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
            },

            //控制下拉
            commandArr: [
                {
                    label: '查询',
                    value: 1
                },
                {
                    label: '下载',
                    value: 2
                },
                {
                    label: '删除',
                    value: 3
                },
            ],
            //通道标志
            channelFlagArr: [
                {
                    label: '0',
                    value: 0
                },
                {
                    label: '1',
                    value: 1
                },
                {
                    label: '2',
                    value: 2
                },
                {
                    label: '3',
                    value: 3
                },
                {
                    label: '4',
                    value: 4
                },
                {
                    label: '5',
                    value: 5
                },
                {
                    label: '6',
                    value: 6
                },
                {
                    label: '7',
                    value: 7
                },
                {
                    label: '8',
                    value: 8
                },
                {
                    label: '9',
                    value: 9
                },
            ],
            //通道号下拉
            channelNoSourceArr: [
                {
                    id: '0',
                    valueName: '通道1'
                },
                {
                    id: '1',
                    valueName: '通道2'
                },
                {
                    id: '2',
                    valueName: '通道3'
                },
                {
                    id: '3',
                    valueName: '通道4'
                },
                {
                    id: '4',
                    valueName: '通道5'
                },
                {
                    id: '5',
                    valueName: '通道6'
                },
                {
                    id: '6',
                    valueName: '通道7'
                },
                {
                    id: '7',
                    valueName: '通道8'
                },
                {
                    id: '8',
                    valueName: '通道9'
                },
                {
                    id: '9',
                    valueName: '通道10'
                },
            ],
            channelNoArr: [],



            //选择设备
            model2: false,
            searchObj: {
                equipType: ''
            },
            loading2: false,
            page: 1,
            total2: 0,
            list: [],
            teamArr: [],
            // 级联下拉配置
            defalutProps: {
                label: 'name',
                value: 'id',
                children: 'list'
            },
            //选中的设备
            selectModel: {},
            messageObj:'',
          issLoading:false
        }
    },
    watch: {},
    computed: {

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
                this.doSubscribe();

            })
            this.client.on('error', error => {
                console.log('连接失败', error)
            })
            this.client.on('message', (topic, message) => {
                if (topic == 'jnSxAT3ParamQueryReturn') {
                    clearTimeout(this.timeout);
                    console.log(`${message}`)
                    var datajson = JSON.parse(`${message}`);
                    // ****预置参数**/
                    this.ruleForm2.presetEleMax = datajson['presetEleMax'];//预置电流上限
                    this.ruleForm2.presetVolMax = datajson['presetVolMax'];//预置电压上限
                    this.ruleForm2.presetEleMin = datajson['presetEleMin'];//预置电流下限
                    this.ruleForm2.presetVolMin = datajson['presetVolMin'];//预置电压下限
                    this.ruleForm2.eleAlarmMax = datajson['eleAlarmMax'];//电流报警上限
                    this.ruleForm2.volAlarmMax = datajson['volAlarmMax'];//电压报警上限
                    this.ruleForm2.eleAlarmMin = datajson['eleAlarmMin'];//电流报警下限
                    this.ruleForm2.volAlarmMin = datajson['volAlarmMin'];//电压报警下限
                    this.ruleForm2.alarmDelayTime = datajson['alarmDelayTime'];//报警延时时间
                    this.ruleForm2.alarmHaltTime = datajson['alarmHaltTime'];//报警停机时间

                    this.$message.success("索取成功！！！");
                    this.messageObj.close();
                  this.issLoading = false;
                    this.model2 = false;
                    this.issueTimeOut();
                }
                if (topic == 'jnSxFR2OrAT3ChannelParamReply') {
                    clearTimeout(this.timeout);
                    console.log(`${message}`)
                    var datajson = JSON.parse(`${message}`);
                    this.messageObj.close();
                  this.issLoading = false;
                    this.$message.warning("无参数！！！");
                    this.model2 = false;
                    this.issueTimeOut();
                }
            })
        },


        //订阅主题
        doSubscribe () {
            //订阅有参数
            this.client.subscribe('jnSxAT3ParamQueryReturn', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            })
            //订阅无参数
            this.client.subscribe('jnSxFR2OrAT3ChannelParamReply', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            })
        },

        doPublish (msg) {
            this.client.publish('jnSxFr2ChannelParamQuery', msg, 0)
        },

        //选择柔软电弧模式
        changeSoftArcSchema (v) {
            if (v) {
                this.ruleForm2.weldingStickTexture = '0';
                this.ruleForm2.gases = '0';
                this.ruleForm2.weldingStickDiameter = '9';
            } else {
                this.ruleForm2.weldingStickTexture = '0';
                this.ruleForm2.gases = '0';
                this.ruleForm2.weldingStickDiameter = '12';
            }
        },

        //子组件调用修改
        async editDetailFun (obj) {
            this.title2 = "修改工艺AT3"
            this.ruleForm2 = { ...this.ruleFormObj2 };
            //获取已使用的通道
            let res = await getAT3ChannaNoIsUse({ id: obj.parentId });
            let { data, code } = await getSxAT3TechDetail({ id: obj.id });
            if (code == 200) {
                this.visable2 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm2.resetFields();
                    this.ruleForm2 = data || {};
                    this.channelNoArr = this.channelNoSourceArr.filter(item => !res.data.includes(item.id) || item.id == this.ruleForm2.channel);
                })
            }
        },

        //新增工艺
        async addLibraryFun (id) {
            this.title2 = "新增工艺AT3"
            let { code, data } = await getAT3ChannaNoIsUse({ id });
            if (code == 200) {
                this.visable2 = true;
                this.channelNoArr = this.channelNoSourceArr.filter(item => !data.includes(item.id));
                this.$nextTick(() => {
                    this.$refs.ruleForm2.resetFields();
                    this.ruleForm2 = { ...this.ruleFormObj2 };
                    this.ruleForm2.wpsLibraryId = id;
                    if (this.channelNoArr.length > 0) {
                        this.ruleForm2.channel = this.channelNoArr[0].id;
                    }
                    Reflect.deleteProperty(this.ruleForm2, "id");
                })
            }
        },

        submitForm2 (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    const req = { ...this.ruleForm2 }
                    this.submitLibary(req)
                } else {
                    console.log('error submit!!')
                    return false
                }
            })
        },

        // 新增/编辑提交工艺
        async submitLibary (vData) {
            const req = { ...vData }
            if (req.hasOwnProperty('id')) {
                const { data, code } = await editSxAT3TechDetail(req)
                if (code == 200) {
                    this.$message.success('修改成功')
                    console.log(this.$refs.addTech)
                    this.visable2 = false
                    this.$parent.getList()
                }
            } else {
                const { data, code } = await addSxAT3Tech(req);
                if (code == 200) {
                    this.$message.success('新增成功')
                    this.visable2 = false
                    this.$parent.getList()
                }
            }
        },


        //索取规范
        requestSpec () {
            if (this.ruleForm2.channel && this.ruleForm2.channel != '') {
                this.model2 = true;
                this.getList();
              this.$nextTick(()=>{
                this.issLoading = true;
                this.selectModel = {}
                this.$refs.proModelTable.clearRadioRow();
                this.$refs.proModelTable.clearCurrentRow();
              })
            } else {
                return this.$message.error("请先选择通道号！！！");
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
                this.list = data.list || [];
                this.total2 = data.total;
            }
        },

        // 获取班组
        async getTeamList () {
            const { data, code } = await getTeam()
            this.teamArr = data.workArea || [];
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
        //设备选中
        radioChangeEvent ({ row }) {
            this.selectModel = { ...row };
        },

        submitIssue () {
            this.doSubscribe();
            if (JSON.stringify(this.selectModel) == "{}") {
                return this.$message.error("请选择设备!!");
            } else if (this.selectModel.weldIp) {
               this.messageObj = this.$message({
                  message:'索取中...',
                  duration:0,
                  type:'warning'
                });
                //关闭索取层
                this.model2 = false;
                setTimeout(() => {
                    let msg = {}
                    msg['weldIp'] = this.selectModel.weldIp;
                    msg['weldCid'] = this.selectModel.weldCid;
                    msg['channel'] = this.ruleForm2.channel;
                    msg['command'] = 1
                    this.doPublish(JSON.stringify(msg));
                    console.log(msg)
                    //记时触发下发失败
                    this.issueTimeOut(1);
                }, 500);
            } else {
                return this.$message.error("选择的设备请先绑定IP!!");
            }
        },

        //下发超时
        issueTimeOut (n) {
            this.timeout = setTimeout(() => {
                this.client.unsubscribe('jnSxAT3ParamQueryReturn', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                });

                this.client.unsubscribe('jnSxFR2OrAT3ChannelParamReply', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                });

                if (n) {
                  this.messageObj.close();
                  this.issLoading = false;
                    this.$message.error("下发超时");
                }
                clearTimeout(this.timeout)
            }, 5000)
        },

    },
    created () {
        this.ruleFormObj2 = { ...this.ruleForm2 }
        this.getTeamList();
        this.createConnection();
    },
    mounted () { }
}
</script>
<style>
.border-tip {
    border: 1px solid #ccc;
    position: relative;
    padding-top: 10px;
    padding-bottom: 10px;
    margin-top: 10px;
}
.border-tip-txt {
    position: absolute;
    background: #fff;
    left: 10px;
    top: -7px;
    font-size: 12px;
    line-height: 14px;
    padding: 0 10px;
}
.teamList {
    z-index: 9999 !important;
}
</style>
