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
                    <el-col>
                        <el-form-item
                            label="通道号"
                            prop="channelNo"
                        >
                            <el-select
                                v-model="ruleForm2.channelNo"
                                placeholder="请选择"
                                style="width:100px"
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
                </el-row>
                <!--普通气保焊工艺  -->
                <sxCO2Tech1></sxCO2Tech1>
                <!-- GL4工艺 -->
                <sxCO2GL4></sxCO2GL4>
                <!-- 手工焊 -->
                <sxCO2Manual></sxCO2Manual>
                <div class="border-tip">
                    <span class="border-tip-txt">报警设置</span>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电流上限(A)"
                                prop="initialEle"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.initialEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电流下限(A)"
                                prop="weldingEle"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.weldingEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电压上限(V)"
                                prop="arcEle"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.arcEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电压下限(V)"
                                prop="arcEle"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.arcEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item prop="initialEle">
                                <template slot="label">
                                    报警模式
                                    <el-tooltip
                                        class="item"
                                        effect="light"
                                        placement="top"
                                    >
                                        <template slot="content">
                                            <span style="color:#f00">请确认焊机是否支持此功能</span>
                                        </template>
                                        <i class="el-icon-info"></i>
                                    </el-tooltip>
                                </template>
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.initialEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="起弧延时时间"
                                prop="weldingEle"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.weldingEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="报警延时(S)"
                                prop="arcEle"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.arcEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="停机冻结(S)"
                                prop="arcEle"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.arcEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                prop="initialEle"
                                label="停机延时(S)"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.initialEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
                <div class="border-tip">
                    <span class="border-tip-txt">限流设置</span>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接限流上限(A)"
                                prop="initialEle"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.initialEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="超限时间(S)"
                                prop="weldingEle"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.weldingEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接限流下限(A)"
                                prop="initialEle"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.initialEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="修正周期(MS)"
                                prop="weldingEle"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.weldingEle"
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
                        popper-class="teamList"
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
                highlight-current-row
                resizable
                stripe
                :data="list"
                row-id="id"
                :radio-config="{ trigger: 'row',highlight: true}"
                @cell-click="radioChangeEvent"
            >
                <vxe-table-column
                    type="radio"
                    title="请选择"
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
        </el-dialog>
    </div>
</template>

<script>
import mqtt from 'mqtt'
import { getTeam, getDictionaries, getProcesLibraryChildDetail, getChannNos, addProcesLibraryChild, editProcesLibraryChild } from '_api/productionProcess/process'

import { getWelderList } from '_api/productionEquipment/production'
import sxCO2Tech1 from './SxCO2Tech1.vue'
import sxCO2GL4 from './SxCO2GL4.vue'
import sxCO2Manual from './SxCO2Manual.vue'
export default {
    name: 'SxCO2',
    components: { sxCO2Tech1, sxCO2GL4,sxCO2Manual },
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
            title2: '新增工艺',
            ruleFormObj2: {},
            ruleForm2: {
                // wpsLibraryId: '',//工艺库id
                // id: '',
                // channelNo: '',//通道号
                // initialCondition: false,//初期条件
                // fusionControl: false,//熔深控制
                // unitarySeveral: 1,//一元/个别
                // controlArc: '000',//收弧方式
                // controlArcType: '7',//收弧方式
                // softArcSchema: false,//柔软电弧模式
                // arcCharacter: 0,//电弧特性
                // spotWeldingTime: 3,//点焊时间
                // weldingStickTexture: '0',//焊丝材质
                // weldingStickTextureType: '8',//焊丝材质Type
                // weldingStickDiameter: '12',//焊丝直径
                // weldingStickDiameterType: '10',//焊丝直径Type
                // inAdvanceAspirated: 0.1,//提前送气
                // hysteresisAspirated: 0.1,//滞后送气
                // gases: '0',//气体
                // gasesType: '9',//气体Typez
                // weldingProcess: '1',//焊接过程
                // weldingProcessType: '11',//焊接过程Type
                // initialEle: 100,//初期电流
                // initialVol: 19,//初期电压
                // weldingEle: 100,//焊接电流
                // weldingVol: 19,//焊接电压
                // arcEle: 100,//收弧电流
                // arcVol: 19,//收弧电压
                // initialVolUnitary: 0,//初期电压一元
                // weldingVolUnitary: 0,//焊接电压一元
                // arcVolUnitary: 0,//收弧电压一元
                // weldingEleAdjust: 0,//焊接电流微调
                // weldingVolAdjust: 0,//焊接电压微调
                // arcEleAdjust: 0,//收弧电流微调
                // arcVolAdjust: 0,//收弧电压微调
                // alarmsEleMax: 0,//报警电流上限
                // alarmsEleMin: 0,//报警电流下限
                // alarmsVolMax: 0,//报警电压上限
                // alarmsVolMin: 0,//报警电压下限

                //松下co2工艺
                channelNo: '',//通道编号
                weldIp: '',//下发的焊机IP

                //*** 预置参数 */
                initialEleMax: 0,//初期电流上限
                initialEleMin: 0,//初期电流下限
                initialVolMax: 0,//初期电压上限
                initialWireSpeedMax: '',//初期送丝速度上限
                initialWireSpeedMin: '',//初期送丝速度下限
                weldEleMax: '',//焊接电流上限
                weldEleMin: '',//焊接电流下限
                weldVolMax: '',//焊接电压上限
                weldVolMin: '',//焊接电压下限
                weldWireSpeedMax: '',//焊接送丝速度上限
                weldWireSpeedMin: '',//焊接送丝速度下限,
                arcEleMax: '',//收弧电流上限
                arcEleMin: '',//收弧电流下限
                arcVolMax: '',//收弧电压上限
                arcVolMin: '',//收弧电压下限
                arcWireSpeedMax: '',//收弧送丝速度上限
                arcWireSpeedMin: '',//收弧送丝速度下限

                //*** 焊接条件 */
                modeSelect: '',//模式选择
                weldingControl: '',//焊接控制
                weldingManner: '',//焊接方式
                texture: '',//材质
                wireDiameter: '',//丝径
                gases: '',//气体
                wireFeed: '', //手动送丝
                checkGasStatus: '',//检气状态
                cutStatus: '',//切换状态
                lockStatus: '',//锁定状态
                eleShowSelect: '',//电流显示选择
                rev: '',//转速
                boardThickness: '',//板厚
                spotWeldingTime: '',//点焊时间
                volShowSelect: '',//电压显示选择
                arcLength: '',//弧长
                arcCharacter: '',//电弧特性
                penetrationControl: '',//熔深控制
                beforeAspiratedTime: '',//提前送气时间
                afterStopGasTime: '',//滞后停气时间
                unitaryDifference: '',//一元/分别
                nowChannel: '',//当前通道

                //*** 动态限流参数 */
                dclInitialEleMax: '',//初期电流上限
                dclInitialEleMin: '',//初期电流下限
                dclWeldEleMax: '',//焊接电流上限
                dclWeldEleMin: '',//焊接电流下限
                dclArcEleMax: '',//收弧电流上限
                dclArcEleMin: '',//收弧电流下限
                startDelayTime: '',//启动延时时间
                clAmendPeriod: '',//限流修正周期

                //*** 超限报警参数 */
                oaInitialEleMax: '',//初期电流上限
                oaInitialEleMin: '',//初期电流下限
                oaInitialVolMax: '',//初期电压上限
                oaInitialVolMin: '',//初期电压下限
                oaWeldEleMax: '',//焊接电流上限
                oaWeldEleMin: '',//焊接电流下限
                oaWeldVolMax: '',//焊接电压上限
                oaWeldVolMin: '',//焊接电压下限
                oaArcEleMax: '',//收弧电流上限
                oaArcEleMin: '',//收弧电流下限
                oaArcVolMax: '',//收弧电压上限
                oaArcVolMin: '',//收弧电压下限
                arcDelayTime: '',//起弧延时时间
                alarmDelayTime: '',//报警延时时间
                haltDelayTime: '',//停机延时时间
                haltFreezeTime: '',//停机冻结时间
            },
            rules2: {
                channelNo: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                weldingStickTexture: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                weldingProcess: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                weldingStickDiameter: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                gases: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                unitarySeveral: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                weldingEle: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                weldingVol: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                weldingEleAdjust: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                weldingVolAdjust: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                inAdvanceAspirated: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                hysteresisAspirated: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                initialEle: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                initialVol: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                controlArc: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                spotWeldingTime: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                arcEle: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                arcVol: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                arcEleAdjust: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                arcVolAdjust: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                arcCharacter: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                weldingVolUnitary: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                arcVolUnitary: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                initialVolUnitary: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
            },

            //收弧方式下拉
            controlArcArr: [],
            //焊丝材质
            weldingStickTextureArr: [],
            //气体
            gasesArr: [],
            gasesArrSource: [],
            //焊丝直径
            weldingStickDiameterArr: [],
            weldingStickDiameterArrSource: [],
            //焊接过程
            weldingProcessArr: [],
            //一元/个别下拉
            unitarySeveralArr: [
                {
                    id: 0,
                    valueName: '一元'
                },
                {
                    id: 1,
                    valueName: '个别'
                },
            ],
            //通道号下拉
            channelNoSourceArr: [
                {
                    id: 1,
                    valueName: '通道1'
                },
                {
                    id: 2,
                    valueName: '通道2'
                },
                {
                    id: 3,
                    valueName: '通道3'
                },
                {
                    id: 4,
                    valueName: '通道4'
                },
                {
                    id: 5,
                    valueName: '通道5'
                },
                {
                    id: 6,
                    valueName: '通道6'
                },
                {
                    id: 7,
                    valueName: '通道7'
                },
                {
                    id: 8,
                    valueName: '通道8'
                },
                {
                    id: 9,
                    valueName: '通道9'
                },
                {
                    id: 10,
                    valueName: '通道10'
                },
                {
                    id: 11,
                    valueName: '通道11'
                },
                {
                    id: 12,
                    valueName: '通道12'
                },
                {
                    id: 13,
                    valueName: '通道13'
                },
                {
                    id: 14,
                    valueName: '通道14'
                },
                {
                    id: 15,
                    valueName: '通道15'
                },
                {
                    id: 16,
                    valueName: '通道16'
                },
                {
                    id: 17,
                    valueName: '通道17'
                },
                {
                    id: 18,
                    valueName: '通道18'
                },
                {
                    id: 19,
                    valueName: '通道19'
                },
                {
                    id: 20,
                    valueName: '通道20'
                },
                {
                    id: 21,
                    valueName: '通道21'
                },
                {
                    id: 22,
                    valueName: '通道22'
                },
                {
                    id: 23,
                    valueName: '通道23'
                },
                {
                    id: 24,
                    valueName: '通道24'
                },
                {
                    id: 25,
                    valueName: '通道25'
                },
                {
                    id: 26,
                    valueName: '通道26'
                },
                {
                    id: 27,
                    valueName: '通道27'
                },
                {
                    id: 28,
                    valueName: '通道28'
                },
                {
                    id: 29,
                    valueName: '通道29'
                },
                {
                    id: 30,
                    valueName: '通道30'
                },
            ],
            channelNoArr: [],

            //选择设备
            model2: false,
            searchObj: {
                grade: '',
                model: ''
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
            selectModel: {}
        }
    },
    watch: {},
    computed: {
        //电焊时间是否禁用
        spotWeldingTimeDisabled () {
            //收弧方式选择点焊可填
            return this.ruleForm2.controlArc !== '100'
        },
        //初期电流是否禁用
        initialEleDisabled () {
            //初期条件且收弧方式有或反复
            return !this.ruleForm2.initialCondition || this.ruleForm2.controlArc !== '001' && this.ruleForm2.controlArc !== '011'
        },
        //收弧是否可填
        arcDisabled () {
            return this.ruleForm2.controlArc !== '001' && this.ruleForm2.controlArc !== '011'
        },

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
                if (topic == 'processClaimReturn') {
                    clearTimeout(this.timeout);
                    console.log(`${message}`)
                    var datajson = JSON.parse(`${message}`);
                    this.ruleForm2.spotWeldingTime = datajson['spotWeldTime'] / 10;//点焊时间
                    this.ruleForm2.inAdvanceAspirated = datajson['preflowTime'] / 10;//提前送气
                    this.ruleForm2.initialEle = datajson['initialEle'];//初期电流
                    this.ruleForm2.initialVol = datajson['initialVol'] / 10;//初期电压
                    this.ruleForm2.initialVolUnitary = datajson['initialVolUnitary'];//初期电压一元
                    this.ruleForm2.weldingEle = datajson['weldElectricity'];//焊接电流
                    this.ruleForm2.weldingVol = datajson['weldVoltage'] / 10;//焊接电压
                    this.ruleForm2.weldingVolUnitary = datajson['weldVoltageUnitary'];//焊接电压一元
                    this.ruleForm2.arcEle = datajson['extinguishArcEle'];//收弧电流
                    this.ruleForm2.arcVol = datajson['extinguishArcVol'] / 10;//收弧电压
                    this.ruleForm2.arcVolUnitary = datajson['extinguishArcVolUnitary'];//收弧电压一元
                    this.ruleForm2.hysteresisAspirated = datajson['hysteresisAspirated'] / 10;//滞后送气
                    this.ruleForm2.arcCharacter = datajson['arcPeculiarity'];//电弧特性
                    this.ruleForm2.gases = datajson['gases'];//气体
                    this.ruleForm2.weldingStickDiameter = datajson['wireDiameter'];//直径
                    this.ruleForm2.weldingStickTexture = datajson['wireMaterials'];//材质
                    this.ruleForm2.weldingProcess = datajson['weldProcess'];//焊接过程
                    this.ruleForm2.weldingEleAdjust = datajson['weldEleAdjust'];//焊接电流微调
                    this.ruleForm2.weldingVolAdjust = datajson['weldVolAdjust'] / 10;//焊接电压微调
                    this.ruleForm2.arcEleAdjust = datajson['extinguishArcEleAdjust'];//收弧电流微调
                    this.ruleForm2.arcVolAdjust = datajson['extinguishArcVolAdjust'] / 10;//收弧电压微调
                    this.ruleForm2.alarmsEleMax = datajson['alarmsElectricityMax'];//报警电流上限
                    this.ruleForm2.alarmsEleMin = datajson['alarmsElectricityMin'];//报警电流下限
                    this.ruleForm2.alarmsVolMax = datajson['alarmsVoltageMax'];//报警电压上限
                    this.ruleForm2.alarmsVolMin = datajson['alarmsVoltageMin'];//报警电压下限
                    this.deconstruction(datajson['controlInfo']);
                    this.$message.success("索取成功！！！");
                    this.model2 = false;
                    this.issueTimeOut();
                }
            })
        },

        deconstruction (str) {
            const binaryStr = parseFloat(str).toString(2);
            let arrStr = binaryStr.split('');
            while (arrStr.length < 8) {
                arrStr.unshift('0')
            }
            this.ruleForm2.initialCondition = arrStr[7] ? false : true;//初期条件
            this.ruleForm2.controlArc = arrStr[6] + arrStr[5] + arrStr[4];//收弧

            this.ruleForm2.unitarySeveral = parseInt(arrStr[2]);//一元/个别
            this.ruleForm2.fusionControl = arrStr[1] ? false : true;//熔深控制
            this.ruleForm2.softArcSchema = arrStr[0] ? false : true;//柔软电弧模式
            console.log(this.ruleForm2)
        },

        //订阅主题
        doSubscribe () {
            this.client.subscribe('processClaimReturn', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            })
        },

        doPublish (msg) {
            this.client.publish('processClaim', msg, 0)
        },





        //获取数据字典
        async getDicFun () {
            let { data, code } = await getDictionaries({ "types": ["7", "8", "9", "10", "11"] });
            if (code == 200) {
                //收弧方式
                this.controlArcArr = data['7'] || [];
                //焊丝材质
                this.weldingStickTextureArr = data['8'] || [];
                //气体
                this.gasesArrSource = data['9'] || [];
                this.gasesArr = (data['9'] || []).filter(item => item.value == '0' || item.value == '1');
                //焊丝直径
                this.weldingStickDiameterArrSource = data['10'] || [];
                this.weldingStickDiameterArr = data['10'] || [];
                //焊接过程
                this.weldingProcessArr = data['11'] || [];

            }
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
            this.title2 = "修改工艺"
            this.ruleForm2 = { ...this.ruleFormObj2 };
            //获取已使用的通道
            let res = await getChannNos({ id: obj.parentId });
            let { data, code } = await getProcesLibraryChildDetail(obj.id);
            if (code == 200) {
                this.visable2 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm2.resetFields();
                    this.ruleForm2 = data[0] || {};
                    this.ruleForm2.initialCondition = this.ruleForm2.initialCondition ? true : false;
                    this.ruleForm2.fusionControl = this.ruleForm2.fusionControl ? true : false;
                    this.ruleForm2.softArcSchema = this.ruleForm2.softArcSchema ? true : false;
                    this.channelNoArr = this.channelNoSourceArr.filter(item => !res.data.includes(item.id) || item.id == this.ruleForm2.channelNo);
                })
            }
        },

        //新增工艺
        async addLibraryFun (id) {
            this.title2 = "新建工艺"
            let { code, data } = await getChannNos({ id });
            if (code == 200) {
                this.visable2 = true;
                this.channelNoArr = this.channelNoSourceArr.filter(item => !data.includes(item.id));
                this.$nextTick(() => {
                    this.$refs.ruleForm2.resetFields();
                    this.ruleForm2 = { ...this.ruleFormObj2 };
                    this.ruleForm2.wpsLibraryId = id;
                    if (this.channelNoArr.length > 0) {
                        this.ruleForm2.channelNo = this.channelNoArr[0].id;
                    }

                    Reflect.deleteProperty(this.ruleForm2, "id");
                })
            }
        },

        //切换一元/个别
        changeWelderType (v) {
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
            req.initialCondition = req.initialCondition ? 1 : 0;
            req.fusionControl = req.fusionControl ? 1 : 0;
            req.softArcSchema = req.softArcSchema ? 1 : 0;
            if (req.hasOwnProperty('id')) {
                const { data, code } = await editProcesLibraryChild(req)
                if (code == 200) {
                    this.$message.success('修改成功')
                    console.log(this.$refs.addTech)
                    this.visable2 = false
                    this.$parent.getList()
                }
            } else {
                const { data, code } = await addProcesLibraryChild(req);
                if (code == 200) {
                    this.$message.success('新增成功')
                    this.visable2 = false
                    this.$parent.getList()
                }
            }
        },




        //切换材质过滤直径和气体
        changekDiameter (v) {
            switch (v) {
                case '0'://低碳钢实芯焊丝
                    this.weldingStickDiameterArr = this.weldingStickDiameterArrSource.filter(item => ['8', '9', '10', '12', '14', '16'].includes(item.value));
                    this.gasesArr = this.gasesArrSource.map(item => ['0', '1'].includes(item.value))
                    break;
                case '2'://低碳钢药芯焊丝
                    this.weldingStickDiameterArr = this.weldingStickDiameterArrSource.filter(item => ['12', '14', '16'].includes(item.value));
                    this.gasesArr = this.gasesArrSource.map(item => ['0'].includes(item.value))
                    break;
                case '1'://不锈钢实芯焊丝
                    this.weldingStickDiameterArr = this.weldingStickDiameterArrSource.filter(item => ['8', '9', '10', '12', '14', '16'].includes(item.value));
                    this.gasesArr = this.gasesArrSource.map(item => ['2'].includes(item.value))
                    break;
                case '3'://不锈钢药芯焊丝
                    this.weldingStickDiameterArr = this.weldingStickDiameterArrSource.filter(item => ['8', '9', '10', '12', '14', '16'].includes(item.value));
                    this.gasesArr = this.gasesArrSource.map(item => ['0'].includes(item.value))
                    break;
            }
        },

        //索取规范
        requestSpec () {
            if (this.ruleForm2.channelNo && this.ruleForm2.channelNo != '') {
                this.model2 = true;
                this.getList();
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
            // req.model = this.libray.weldModel;
            req.grade = this.searchObj.grade && this.searchObj.grade.length > 0 ? this.searchObj.grade.slice(-1).join('') : ''
            this.loading2 = true;
            let { data, code } = await getWelderList(req);
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
            if (JSON.stringify(this.selectModel) == "{}") {
                return this.$message.error("请选择设备!!");
            } else if (this.selectModel.machineGatherInfo && this.selectModel.machineGatherInfo.gatherNo) {
                this.createConnection();
                setTimeout(() => {
                    let msg = {}
                    msg['gatherNo'] = this.selectModel.machineGatherInfo.gatherNo;
                    msg['channelNo'] = this.ruleForm2.channelNo;
                    this.doPublish(JSON.stringify(msg));
                    console.log(msg)
                    //记时触发下发失败
                    this.issueTimeOut(1);
                }, 500);
            } else {
                return this.$message.error("选择的设备请先绑定采集编号!!");
            }
        },

        //下发超时
        issueTimeOut (n) {
            this.timeout = setTimeout(() => {
                this.client.unsubscribe('processClaimReturn', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                    setTimeout(() => {
                        this.client.end();
                    }, 500)
                });

                if (n) {
                    this.$message.error("下发超时")
                }
                clearTimeout(this.timeout)
            }, 5000)
        },

    },
    created () {
        this.ruleFormObj2 = { ...this.ruleForm2 }
        this.getDicFun();
        this.getTeamList();
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
