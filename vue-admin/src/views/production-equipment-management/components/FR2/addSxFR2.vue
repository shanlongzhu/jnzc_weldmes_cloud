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
                                label="初期电流上限"
                                prop="initialEleMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电流下限"
                                prop="initialEleMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电压上限"
                                prop="initialVolMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialVolMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电压下限"
                                prop="initialVolMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialVolMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电流上限"
                                prop="arcEleMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电流下限"
                                prop="arcEleMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电压上限"
                                prop="arcVolMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcVolMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电压下限"
                                prop="arcVolMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcVolMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="材质"
                                prop="texture"
                            >
                                <el-select
                                    v-model="ruleForm2.texture"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in textureArr"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="丝径"
                                prop="wireDiameter"
                            >
                                <el-select
                                    v-model="ruleForm2.wireDiameter"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in wireDiameterArr"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="气体"
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
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接控制"
                                prop="weldingControl"
                            >
                                <el-select
                                    v-model="ruleForm2.weldingControl"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in weldingControlArr"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="脉冲有无"
                                prop="pulseHaveNot"
                            >
                                <el-select
                                    v-model="ruleForm2.pulseHaveNot"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in weldingMannerArr"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="电焊时间"
                                prop="spotWeldingTime"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.spotWeldingTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="一元/分别"
                                prop="unitaryDifference"
                            >
                                <el-select
                                    v-model="ruleForm2.unitaryDifference"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in unitaryDifferenceArr"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="干伸长度"
                                prop="dryExtendLength"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.dryExtendLength"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接上限"
                                prop="weldMax"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.weldMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接下限"
                                prop="weldMin"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.weldMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期上限"
                                prop="initialMax"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期下限"
                                prop="initialMin"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧上限"
                                prop="arcMax"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧下限"
                                prop="arcMin"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="延时时间"
                                prop="delayTime"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.delayTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="修正周期"
                                prop="amendPeriod"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.amendPeriod"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="预置电流报警上限"
                                prop="presetEleAlarmMax"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.presetEleAlarmMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="预置电压报警上限"
                                prop="presetVolAlarmMax"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.presetVolAlarmMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="预置电流报警下限"
                                prop="presetEleAlarmMin"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.presetEleAlarmMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="预置电压报警下限"
                                prop="presetVolAlarmMin"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.presetVolAlarmMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电流报警上限"
                                prop="initialEleAlarmMax"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialEleAlarmMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电压报警上限"
                                prop="initialVolAlarmMax"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialVolAlarmMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电流报警下限"
                                prop="initialEleAlarmMin"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialEleAlarmMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电压报警下限"
                                prop="initialVolAlarmMin"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialVolAlarmMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电流报警上限"
                                prop="arcEleAlarmMax"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcEleAlarmMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电压报警上限"
                                prop="arcVolAlarmMax"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcVolAlarmMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电流报警下限"
                                prop="arcEleAlarmMin"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcEleAlarmMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电压报警下限"
                                prop="arcVolAlarmMin"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcVolAlarmMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="起弧延时时间"
                                prop="arcDelayTime"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcDelayTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="报警延时时间"
                                prop="alarmDelayTime"
                            >
                                <el-input-number
                                    :precision="1"
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
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.alarmHaltTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="流量上限"
                                prop="flowMax"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.flowMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="标志（下载参数）"
                                prop="alarmFlag"
                            >
                                <el-input-number
                                    :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.alarmFlag"
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
import { getTeam, getFR2ChannaNoIsUse, addSxFR2Tech, getSxFR2TechDetail, editSxFR2TechDetail } from '_api/productionProcess/process'
import { getWelderList, getSxWelderList } from '_api/productionEquipment/production'
import { getChannelNoSourceArr, getWireDiameterArr, getGasesArr } from './common'
export default {
    name: 'addSxFR2',
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
            title2: '新增工艺FR2',
            ruleFormObj2: {},
            ruleForm2: {
                channel: '0',//通道编号
                command: '',//控制参数
                channelFlag: 0,//通道标志（0~9）

                //*** 预置参数 */
                presetEleMax: 0,//预置电流上限
                presetVolMax: 0,//预置电压上限
                presetEleMin: 0,//预置电流下限
                presetVolMin: 0,//预置电压下限

                initialEleMax: 0,//初期电流上限
                initialVolMax: 0,//初期电压上限
                initialEleMin: 0,//初期电流下限
                initialVolMin: 0,//初期电压下限

                arcEleMax: 0,//收弧电流上限
                arcVolMax: 0,//收弧电压上限
                arcEleMin: 0,//收弧电流下限
                arcVolMin: 0,//收弧电压下限

                texture: '',//材质
                wireDiameter: '',//丝径
                gases: '',//气体
                weldingControl: '',//焊接控制
                pulseHaveNot: '',//脉冲有无
                spotWeldingTime: 0,//点焊时间
                unitaryDifference: '',//一元/分别
                dryExtendLength: 0,//干伸长度
                weldMax: 0,//焊接上限
                weldMin: 0,//焊接下限
                initialMax: 0,//初期上限
                initialMin: 0,//初期下限
                arcMax: 0,//收弧上限
                arcMin: 0,//收弧下限
                delayTime: 0,//延时时间
                amendPeriod: 0,//修正周期

                presetEleAlarmMax: 0,//预置电流报警上限
                presetVolAlarmMax: 0,//预置电压报警上限
                presetEleAlarmMin: 0,//预置电流报警下限
                presetVolAlarmMin: 0,//预置电压报警下限

                initialEleAlarmMax: 0,//初期电流报警上限
                initialVolAlarmMax: 0,//初期电压报警上限
                initialEleAlarmMin: 0,//初期电流报警下限
                initialVolAlarmMin: 0,//初期电压报警下限

                arcEleAlarmMax: 0,//收弧电流报警上限
                arcVolAlarmMax: 0,//收弧电压报警上限
                arcEleAlarmMin: 0,//收弧电流报警下限
                arcVolAlarmMin: 0,//收弧电压报警下限

                arcDelayTime: 0,//起弧延时时间
                alarmDelayTime: 0,//报警延时时间
                alarmHaltTime: 0,//报警停机时间
                flowMax: 0,//流量上限(查询回复)
                alarmFlag: 0,//标志（下载参数）                
            },
            rules2: {
                channel: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                // weldingStickTexture: [
                //     { required: true, message: '不能为空', trigger: 'change' }
                // ],
                // weldingProcess: [
                //     { required: true, message: '不能为空', trigger: 'change' }
                // ],
                // weldingStickDiameter: [
                //     { required: true, message: '不能为空', trigger: 'change' }
                // ],
                // gases: [
                //     { required: true, message: '不能为空', trigger: 'change' }
                // ],
                // unitarySeveral: [
                //     { required: true, message: '不能为空', trigger: 'change' }
                // ],
                // weldingEle: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // weldingVol: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // weldingEleAdjust: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // weldingVolAdjust: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // inAdvanceAspirated: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // hysteresisAspirated: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // initialEle: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // initialVol: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // controlArc: [
                //     { required: true, message: '不能为空', trigger: 'change' }
                // ],
                // spotWeldingTime: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // arcEle: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // arcVol: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // arcEleAdjust: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // arcVolAdjust: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // arcCharacter: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // weldingVolUnitary: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // arcVolUnitary: [
                //     { required: true, message: '不能为空', trigger: 'blur' }
                // ],
                // initialVolUnitary: [
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
            //模式选择
            modeArr: [
                {
                    label: '焊接',
                    value: 0
                },
                {
                    label: '详细',
                    value: 1
                },
                {
                    label: '调用',
                    value: 2
                },
                {
                    label: '存储',
                    value: 3
                }
            ],
            //焊接控制
            weldingControlArr: [
                {
                    label: '收弧无',
                    value: 0
                },
                {
                    label: '收弧有',
                    value: 1
                },
                {
                    label: '初期有',
                    value: 2
                },
                {
                    label: '电焊',
                    value: 3
                }
            ],
            //焊接方式
            weldingMannerArr: [
                {
                    label: '脉冲无',
                    value: 0
                },
                {
                    label: '脉冲有',
                    value: 1
                },
                {
                    label: '根焊',
                    value: 2
                },
                {
                    label: '大熔深',
                    value: 3
                }
            ],
            //材质
            textureArr: [
                {
                    label: '碳钢',
                    value: 0
                },
                {
                    label: '药芯碳钢',
                    value: 1
                },
                {
                    label: '不锈钢3系',
                    value: 2
                },
                {
                    label: '不锈钢4系',
                    value: 3
                },
                {
                    label: '药芯不锈钢',
                    value: 4
                },
                {
                    label: 'OP1',
                    value: 5
                },
                {
                    label: 'OP2',
                    value: 6
                },
            ],
            //丝径
            wireDiameterArr: getWireDiameterArr(),
            //气体
            gasesArr: getGasesArr(),
            //手动送丝
            wireFeedArr: [
                {
                    label: '送丝停止',
                    value: 0
                },
                {
                    label: '送丝启动',
                    value: 1
                },
            ],
            //检气状态
            checkGasStatusArr: [
                {
                    label: '检气停止',
                    value: 0
                },
                {
                    label: '检气启动',
                    value: 1
                },
            ],
            //切换状态
            cutStatusArr: [
                {
                    label: '初期',
                    value: 0
                },
                {
                    label: '焊接',
                    value: 1
                },
                {
                    label: '收弧',
                    value: 2
                },
            ],
            //锁定状态
            lockStatusArr: [
                {
                    label: '锁定无',
                    value: 0
                },
                {
                    label: '锁定有',
                    value: 1
                },
            ],

            //电流显示选择
            eleShowSelectArr: [
                {
                    label: '电流',
                    value: 0
                },
                {
                    label: '速度',
                    value: 1
                },
                {
                    label: '板厚',
                    value: 2
                },
                {
                    label: '电焊时间',
                    value: 3
                },
            ],
            //电压显示选择
            volShowArr: [
                {
                    label: '电压',
                    value: 0
                },
                {
                    label: '弧长',
                    value: 1
                },
                {
                    label: '电弧特性',
                    value: 2
                },
                {
                    label: '熔深控制',
                    value: 3
                },
            ],

            //一元/分别
            unitaryDifferenceArr: [
                {
                    label: '一元化',
                    value: 1
                },
                {
                    label: '分别',
                    value: 2
                },
            ],
            //通道号下拉            
            channelNoSourceArr: getChannelNoSourceArr(),
            channelNoArr: [],



            //收弧方式下拉
            controlArcArr: [],
            //焊丝材质
            weldingStickTextureArr: [],

            gasesArrSource: [],
            //焊丝直径
            weldingStickDiameterArr: [],
            weldingStickDiameterArrSource: [],
            //焊接过程
            weldingProcessArr: [],



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
            selectModel: {}
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
                if (topic == 'sxChannelParamReplyHave') {
                    clearTimeout(this.timeout);
                    console.log(`${message}`)
                    var datajson = JSON.parse(`${message}`);
                    // ****预置参数**/
                    this.ruleForm2.presetEleMax = datajson['presetEleMax'];//预置电流上限
                    this.ruleForm2.presetVolMax = datajson['presetVolMax'];//预置电压上限
                    this.ruleForm2.presetEleMin = datajson['presetEleMin'];//预置电流下限
                    this.ruleForm2.presetVolMin = datajson['presetVolMin'];//预置电压下限

                    this.ruleForm2.initialEleMax = datajson['initialEleMax'];//初期电流上限
                    this.ruleForm2.initialVolMax = datajson['initialVolMax'];//初期电压上限
                    this.ruleForm2.initialEleMin = datajson['initialEleMin'];//初期电流下限
                    this.ruleForm2.initialVolMin = datajson['initialVolMin'];//初期电压下限

                    this.ruleForm2.arcEleMax = datajson['arcEleMax'];//收弧电流上限
                    this.ruleForm2.arcVolMax = datajson['arcVolMax'];//收弧电压上限
                    this.ruleForm2.arcEleMin = datajson['arcEleMin'];//收弧电流下限
                    this.ruleForm2.arcVolMin = datajson['arcVolMin'];//收弧电压下限

                    this.ruleForm2.texture = datajson['texture'];//材质
                    this.ruleForm2.wireDiameter = datajson['wireDiameter'];//丝径
                    this.ruleForm2.gases = datajson['gases'];//气体
                    this.ruleForm2.weldingControl = datajson['weldingControl'];//焊接控制
                    this.ruleForm2.pulseHaveNot = datajson['pulseHaveNot'];//脉冲有无
                    this.ruleForm2.spotWeldingTime = datajson['spotWeldingTime'];//点焊时间
                    this.ruleForm2.unitaryDifference = datajson['unitaryDifference'];//一元/分别
                    this.ruleForm2.dryExtendLength = datajson['dryExtendLength'];//干伸长度
                    this.ruleForm2.weldMax = datajson['weldMax'];//焊接上限
                    this.ruleForm2.weldMin = datajson['weldMin'];//焊接下限
                    this.ruleForm2.initialMax = datajson['initialMax'];//初期上限
                    this.ruleForm2.initialMin = datajson['initialMin'];//初期下限
                    this.ruleForm2.arcMax = datajson['arcMax'];//收弧上限
                    this.ruleForm2.arcMin = datajson['arcMin'];//收弧下限
                    this.ruleForm2.delayTime = datajson['delayTime'];//延时时间
                    this.ruleForm2.amendPeriod = datajson['amendPeriod'];//修正周期

                    this.ruleForm2.presetEleAlarmMax = datajson['presetEleAlarmMax'];//预置电流报警上限
                    this.ruleForm2.presetVolAlarmMax = datajson['presetVolAlarmMax'];//预置电压报警上限
                    this.ruleForm2.presetEleAlarmMin = datajson['presetEleAlarmMin'];//预置电流报警下限
                    this.ruleForm2.presetVolAlarmMin = datajson['presetVolAlarmMin'];//预置电压报警下限

                    this.ruleForm2.initialEleAlarmMax = datajson['initialEleAlarmMax'];//初期电流报警上限
                    this.ruleForm2.initialVolAlarmMax = datajson['initialVolAlarmMax'];//初期电压报警上限
                    this.ruleForm2.initialEleAlarmMin = datajson['initialEleAlarmMin'];//初期电流报警下限
                    this.ruleForm2.initialVolAlarmMin = datajson['initialVolAlarmMin'];//初期电压报警下限

                    this.ruleForm2.arcEleAlarmMax = datajson['arcEleAlarmMax'];//收弧电流报警上限
                    this.ruleForm2.arcVolAlarmMax = datajson['arcVolAlarmMax'];//收弧电压报警上限
                    this.ruleForm2.arcEleAlarmMin = datajson['arcEleAlarmMin'];//收弧电流报警下限
                    this.ruleForm2.arcVolAlarmMin = datajson['arcVolAlarmMin'];//收弧电压报警下限

                    this.ruleForm2.arcDelayTime = datajson['arcDelayTime'];//起弧延时时间
                    this.ruleForm2.alarmDelayTime = datajson['alarmDelayTime'];//报警延时时间
                    this.ruleForm2.alarmHaltTime = datajson['alarmHaltTime'];//报警停机时间

                    this.ruleForm2.flowMax = datajson['flowMax'];//流量上限(查询回复)
                    this.ruleForm2.alarmFlag = datajson['alarmFlag'];//标志（下载参数）

                    this.$message.success("索取成功！！！");
                    this.model2 = false;
                    this.issueTimeOut();
                }
                if (topic == 'sxChannelParamReply') {
                    clearTimeout(this.timeout);
                    console.log(`${message}`)
                    var datajson = JSON.parse(`${message}`);
                    this.$message.warning("无参数！！！");
                    this.model2 = false;
                    this.issueTimeOut();
                }
            })
        },


        //订阅主题
        doSubscribe () {
            //有参数
            this.client.subscribe('sxChannelParamReplyHave', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            })
            //无参数
            this.client.subscribe('sxChannelParamReply', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            })
        },

        doPublish (msg) {
            this.client.publish('sxFr2ChannelParamQuery', msg, 0)
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
            this.title2 = "修改工艺FR2"
            this.ruleForm2 = { ...this.ruleFormObj2 };
            //获取已使用的通道
            let res = await getFR2ChannaNoIsUse({ id: obj.parentId });
            let { data, code } = await getSxFR2TechDetail({ id: obj.id });
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
            this.title2 = "新增工艺FR2"
            let { code, data } = await getFR2ChannaNoIsUse({ id });
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
                const { data, code } = await editSxFR2TechDetail(req)
                if (code == 200) {
                    this.$message.success('修改成功')
                    this.visable2 = false
                    this.$parent.getList()
                }
            } else {
                const { data, code } = await addSxFR2Tech(req);
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
            if (JSON.stringify(this.selectModel) == "{}") {
                return this.$message.error("请选择设备!!");
            } else if (this.selectModel.weldIp) {
                this.createConnection();
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
                this.client.unsubscribe('sxChannelParamReplyHave', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                });
                this.client.unsubscribe('sxChannelParamReply', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
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
