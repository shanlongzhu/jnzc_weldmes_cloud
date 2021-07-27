<template>
    <div>
        <div class="border-tip">
            <span class="border-tip-txt">焊接参数</span>
            <!-- 手工焊工艺 -->
            <el-row>
                <el-col :span="6">
                    <el-form-item
                        label="工艺类型"
                        prop="controlArc"
                    >
                        <el-select
                            v-model="ruleForm2.controlArc"
                            placeholder="请选择"
                            style="width:110px"
                            size="mini"
                        >
                            <el-option
                                v-for="item in controlArcArr"
                                :key="item.id"
                                :label="item.valueName"
                                :value="item.value"
                            />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item
                        label="工艺编码"
                        prop="unitarySeveral"
                    >
                        <el-input
                            size="mini"
                            v-model="ruleForm2.unitarySeveral"
                            placeholder=""
                            style="width:110px"
                        ></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item
                        label="电流补偿值"
                        prop="unitarySeveral"
                    >
                        <el-input
                            size="mini"
                            v-model="ruleForm2.unitarySeveral"
                            placeholder=""
                            style="width:110px"
                        ></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item
                        label="电压补偿值"
                        prop="unitarySeveral"
                    >
                        <el-input
                            size="mini"
                            v-model="ruleForm2.unitarySeveral"
                            placeholder=""
                            style="width:110px"
                        ></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="6">
                    <el-form-item
                        label="预置电流上限(A)"
                        prop="arcCharacter"
                    >
                        <el-input-number
                            :controls="false"
                            :precision="0"
                            size="mini"
                            style="width:110px"
                            :min="-99"
                            :max="99"
                            v-model="ruleForm2.arcCharacter"
                        ></el-input-number>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item
                        label="预置电流下限(A)"
                        prop="weldingStickTexture"
                    >
                        <el-select
                            v-model="ruleForm2.weldingStickTexture"
                            placeholder="请选择"
                            style="width:110px"
                            size="mini"
                        >
                            <el-option
                                v-for="item in weldingStickTextureArr"
                                :key="item.id"
                                :label="item.valueName"
                                :value="item.value"
                            />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item
                        label="预置电压上限(V)"
                        prop="inAdvanceAspirated"
                    >
                        <el-input-number
                            :controls="false"
                            :precision="1"
                            size="mini"
                            style="width:110px"
                            :min="0"
                            :max="10"
                            v-model="ruleForm2.inAdvanceAspirated"
                        ></el-input-number>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item
                        label="预置电压下限(V)"
                        prop="gases"
                    >
                        <el-select
                            v-model="ruleForm2.gases"
                            placeholder="请选择"
                            style="width:110px"
                            size="mini"
                        >
                            <el-option
                                v-for="item in gasesArr"
                                :key="item.id"
                                :label="item.valueName"
                                :value="item.value"
                            />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="6">
                    <el-form-item
                        label="工艺说明"
                        prop="spotWeldingTime"
                    >
                        <el-input-number
                            :controls="false"
                            :precision="1"
                            size="mini"
                            style="width:110px"
                            :min="0"
                            :max="10"
                            v-model="ruleForm2.spotWeldingTime"
                        ></el-input-number>
                    </el-form-item>
                </el-col>                
            </el-row>
        </div>        
    </div>
</template>

<script>
export default {
    components: {},
    props: {},
    data () {
        return {
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
        }
    },
    watch: {},
    computed: {},
    methods: {},
    created () { },
    mounted () { }
}
</script>
<style lang="scss" scoped>
.wrapper {
}
</style>