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
                <div class="border-tip">
                    <span class="border-tip-txt">预置参数</span>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电流上限"
                                prop="initialEleMax"
                            >
                                <el-input-number :precision="0"
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
                                <el-input-number :precision="0"
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
                                <el-input-number :precision="0"
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
                                <el-input-number :precision="0"
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
                                label="第一焊接电流上限"
                                prop="firstWeldEleMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.firstWeldEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="第一焊接电流下限"
                                prop="firstWeldEleMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.firstWeldEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="第一焊接电压上限"
                                prop="firstWeldVolMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.firstWeldVolMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="第一焊接电压下限"
                                prop="firstWeldVolMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.firstWeldVolMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="第二焊接电流上限"
                                prop="secondWeldEleMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.secondWeldEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="第二焊接电流下限"
                                prop="secondWeldEleMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.secondWeldEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="第二焊接电压上限"
                                prop="secondWeldVolMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.secondWeldVolMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="第二焊接电压下限"
                                prop="secondWeldVolMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.secondWeldVolMin"
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
                                <el-input-number :precision="0"
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
                                <el-input-number :precision="0"
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
                                <el-input-number :precision="0"
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
                                <el-input-number :precision="0"
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
                                label="峰值焊接电流上限"
                                prop="peakWeldEleMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.peakWeldEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="峰值焊接电流下限"
                                prop="peakWeldEleMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.peakWeldEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="峰值焊接电压上限"
                                prop="peakWeldVolMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.peakWeldVolMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="峰值焊接电压下限"
                                prop="peakWeldVolMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.peakWeldVolMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
                <div class="border-tip">
                    <span class="border-tip-txt">焊接条件</span>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接方法"
                                prop="weldMethod"
                            >
                                <el-select
                                    v-model="ruleForm2.weldMethod"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in modeArr"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧有无"
                                prop="arcHaveNot"
                            >
                                <el-select
                                    v-model="ruleForm2.arcHaveNot"
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
                                label="交流波形"
                                prop="acWaveform"
                            >
                                <el-select
                                    v-model="ruleForm2.acWaveform"
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
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="脉冲比率"
                                prop="pulseRate"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="5"
                                    :max="95"
                                    v-model="ruleForm2.pulseRate"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="脉冲频率"
                                prop="pulseFrequency"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="1"
                                    :max="5000"
                                    v-model="ruleForm2.pulseFrequency"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="清洁宽度"
                                prop="cleanWidth"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="10"
                                    :max="50"
                                    v-model="ruleForm2.cleanWidth"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="AC频率"
                                prop="acFrequency"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="30"
                                    :max="400"
                                    v-model="ruleForm2.acFrequency"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="MIX频率"
                                prop="mixFrequency"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="1"
                                    :max="200"
                                    v-model="ruleForm2.mixFrequency"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="MIX（AC）比率"
                                prop="mixAcRate"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="10"
                                    :max="90"
                                    v-model="ruleForm2.mixAcRate"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="脉冲弧度"
                                prop="pulseRadian"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="1"
                                    :max="100"
                                    v-model="ruleForm2.pulseRadian"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="电弧挺度（预留）"
                                prop="arcStiffness"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="10"
                                    v-model="ruleForm2.arcStiffness"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="手工焊推力"
                                prop="handWeldThrust"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="100"
                                    v-model="ruleForm2.handWeldThrust"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="提前送气时间"
                                prop="beforeAspiratedTime"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="300"
                                    v-model="ruleForm2.beforeAspiratedTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="滞后停气时间"
                                prop="afterStopGasTime"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="300"
                                    v-model="ruleForm2.afterStopGasTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="主焊上升时间"
                                prop="mainWeldRiseTime"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="10"
                                    v-model="ruleForm2.mainWeldRiseTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="主焊下降时间"
                                prop="mainWeldDeclineTime"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="10"
                                    v-model="ruleForm2.mainWeldDeclineTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="主焊上升弧度"
                                prop="mainWeldRiseRadian"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="100"
                                    v-model="ruleForm2.mainWeldRiseRadian"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="主焊下降弧度"
                                prop="mainWeldDeclineRadian"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="100"
                                    v-model="ruleForm2.mainWeldDeclineRadian"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="点焊时间"
                                prop="spotWeldingTime"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.spotWeldingTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="点焊间隔时间"
                                prop="spotWeldIntervalTime"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.spotWeldIntervalTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="点焊上升时间"
                                prop="spotWeldRiseTime"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="10"
                                    v-model="ruleForm2.spotWeldRiseTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="点焊下降时间"
                                prop="spotWeldDeclineTime"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="10"
                                    v-model="ruleForm2.spotWeldDeclineTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="点焊上升弧度"
                                prop="spotWeldRiseRadian"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="50"
                                    v-model="ruleForm2.spotWeldRiseRadian"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>                        
                    </el-row> 

                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="点焊下降弧度"
                                prop="spotWeldDeclineRadian"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="50"
                                    v-model="ruleForm2.spotWeldDeclineRadian"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="最大通道"
                                prop="maxChannel"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.maxChannel"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="当前通道"
                                prop="nowChannel"
                            >
                                <el-select
                                    v-model="ruleForm2.nowChannel"
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
                        <el-col :span="6">
                            <el-form-item
                                label="手工焊焊接电流"
                                prop="handWeldWeldEle"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    v-model="ruleForm2.handWeldWeldEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>                        
                    </el-row>

                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="手工焊引弧电流"
                                prop="handWeldArcEle"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    v-model="ruleForm2.handWeldArcEle"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>                                          
                    </el-row>
                </div>
                <div class="border-tip">
                    <span class="border-tip-txt">动态限流参数</span>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电流上限"
                                prop="dclInitialEleMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.dclInitialEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电流下限"
                                prop="dclInitialEleMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.dclInitialEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电流上限"
                                prop="dclWeldEleMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.dclWeldEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电流下限"
                                prop="dclWeldEleMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.dclWeldEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="第二焊接电流上限"
                                prop="dclSecondWeldEleMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.dclSecondWeldEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="第二焊接电流下限"
                                prop="dclSecondWeldEleMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.dclSecondWeldEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电流上限"
                                prop="dclArcEleMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.dclArcEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电流下限"
                                prop="dclArcEleMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.dclArcEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>                        
                    </el-row>
                    <el-row>
                      <el-col :span="6">
                            <el-form-item
                                label="启动延时时间"
                                prop="startDelayTime"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.startDelayTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="限流修正周期"
                                prop="clAmendPeriod"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.clAmendPeriod"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
                <div class="border-tip">
                    <span class="border-tip-txt">超限报警参数</span>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电流上限"
                                prop="oaInitialEleMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaInitialEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电流下限"
                                prop="oaInitialEleMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaInitialEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电压上限"
                                prop="oaInitialVolMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaInitialVolMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期电压下限"
                                prop="oaInitialVolMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaInitialVolMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电流上限"
                                prop="oaWeldEleMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaWeldEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电流下限"
                                prop="oaWeldEleMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaWeldEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电压上限"
                                prop="oaWeldVolMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaWeldVolMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电压下限"
                                prop="oaWeldVolMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaWeldVolMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="第二焊接电流上限"
                                prop="oaSecondWeldEleMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaSecondWeldEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="第二焊接电流下限"
                                prop="oaSecondWeldEleMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaSecondWeldEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="第二焊接电压上限"
                                prop="oaSecondWeldVolMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaSecondWeldVolMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="第二焊接电压下限"
                                prop="oaSecondWeldVolMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaSecondWeldVolMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电流上限"
                                prop="oaArcEleMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaArcEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电流下限"
                                prop="oaArcEleMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaArcEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电压上限"
                                prop="oaArcVolMax"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaArcVolMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧电压下限"
                                prop="oaArcVolMin"
                            >
                                <el-input-number :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.oaArcVolMin"
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
                                <el-input-number :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0.1"
                                    :max="3"
                                    v-model="ruleForm2.arcDelayTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="报警延时时间"
                                prop="alarmDelayTime"
                            >
                                <el-input-number :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0.1"
                                    :max="25"
                                    v-model="ruleForm2.alarmDelayTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="停机延时时间"
                                prop="haltDelayTime"
                            >
                                <el-input-number :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0.1"
                                    :max="25"
                                    v-model="ruleForm2.haltDelayTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="停机冻结时间"
                                prop="haltFreezeTime"
                            >
                                <el-input-number :precision="1"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0.1"
                                    :max="25"
                                    v-model="ruleForm2.haltFreezeTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>

                </div>
                <el-form-item
                    class="mt10 tc"
                    label-width="0"
                >
                    <!-- <el-button
                        type="primary"
                        @click="requestSpec"
                    >索取规范</el-button> -->
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
export default {
    name: 'SxTIG',
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
                //松下TIG工艺
                channelNo: '',//通道编号
                weldIp: '',//下发的焊机IP

                //*** 预置参数 */
                initialEleMax: 0,//初期电流上限
                initialEleMin: 0,//初期电流下限
                initialVolMax: 0,//初期电压上限
                initialVolMin: 0,//初期电压下限
                firstWeldEleMax: '',//第一焊接电流上限
                firstWeldEleMin: '',//第一焊接电流下限
                firstWeldVolMax: '',//第一焊接电压上限
                firstWeldVolMin: '',//第一焊接电压下限
                secondWeldEleMax: '',//第二焊接电流上限
                secondWeldEleMin: '',//第二焊接电流下限,
                secondWeldVolMax: '',//第二焊接电压上限,
                secondWeldVolMin: '',//第二焊接电压下限,
                arcEleMax: '',//收弧电流上限
                arcEleMin: '',//收弧电流下限
                arcVolMax: '',//收弧电压上限
                arcVolMin: '',//收弧电压下限
                peakWeldEleMax: '',//峰值焊接电流上限
                peakWeldEleMin: '',//峰值焊接电流下限
                peakWeldVolMax: '',//峰值焊接电压上限
                peakWeldVolMin: '',//峰值焊接电压下限

                //*** 焊接条件 */
                weldMethod: '',//焊接方法
                arcHaveNot: '',//收弧有无
                pulseHaveNot: '',//脉冲有无
                acWaveform: '',//交流波形
                pulseRate: '',//脉冲比率(5-95)
                pulseFrequency: '',//脉冲频率
                cleanWidth: '', //清洁宽度
                acFrequency: '',//AC频率
                mixFrequency: '',//MIX频率
                mixAcRate: '',//MIX（AC）比率
                pulseRadian: '',//脉冲弧度
                arcStiffness: '',//电弧挺度
                handWeldThrust: '',//手工焊推力
                beforeAspiratedTime: '',//提前送气时间
                afterStopGasTime: '',//滞后停气时间
                mainWeldRiseTime: '',//主焊上升时间
                mainWeldDeclineTime: '',//主焊下降时间
                mainWeldRiseRadian: '',//主焊上升弧度
                mainWeldDeclineRadian: '',//主焊下降弧度
                spotWeldingTime: '',//点焊时间                
                spotWeldIntervalTime: '',//点焊间隔时间
                spotWeldRiseTime: '',//点焊上升时间
                spotWeldDeclineTime: '',//点焊下降时间
                spotWeldRiseRadian: '',//点焊上升弧度
                spotWeldDeclineRadian: '',//点焊下降弧度
                maxChannel: '',//最大通道
                nowChannel: '',//当前通道
                handWeldWeldEle: '',//手工焊焊接电流
                handWeldArcEle: '',//手工焊引弧电流

                //*** 动态限流参数 */
                dclInitialEleMax: '',//初期电流上限
                dclInitialEleMin: '',//初期电流下限
                dclWeldEleMax: '',//焊接电流上限
                dclWeldEleMin: '',//焊接电流下限
                dclSecondWeldEleMax: '',//第二焊接电流上限
                dclSecondWeldEleMin: '',//第二焊接电流下限
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

                oaSecondWeldEleMax: '',//第二焊接电流上限
                oaSecondWeldEleMin: '',//第二焊接电流下限
                oaSecondWeldVolMax: '',//第二焊接电压上限
                oaSecondWeldVolMin: '',//第二焊接电压下限

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
            //焊接方法
            modeArr:[
                {
                    label:'直流TIG',
                    value:0
                },
                {
                    label:'交流TIG',
                    value:1
                },
                {
                    label:'MIX TIG',
                    value:2
                },
                {
                    label:'手工焊',
                    value:3
                }
            ],
            //收弧有无
            weldingControlArr:[
                {
                    label:'收弧无',
                    value:0
                },
                {
                    label:'收弧有',
                    value:1
                },
                {
                    label:'重复收弧有',
                    value:2
                },
                {
                    label:'收弧点焊',
                    value:3
                },
                {
                    label:'连续点焊',
                    value:4
                }
            ],
            //脉冲有无
            weldingMannerArr:[
                {
                    label:'脉冲无',
                    value:0
                },
                {
                    label:'脉冲有',
                    value:1
                },
            ],
            //交流波形
            textureArr:[
                {
                    label:'标准',
                    value:0
                },
                {
                    label:'硬',
                    value:1
                },
                {
                    label:'软',
                    value:2
                },                
            ],
            //脉冲比率
            wireDiameterArr:[
                {
                    label:'0.8',
                    value:0
                },
                {
                    label:'0.9',
                    value:1
                },
                {
                    label:'1.0',
                    value:2
                },
                {
                    label:'1.2',
                    value:3
                },
                {
                    label:'1.4',
                    value:4
                },
                {
                    label:'1.6',
                    value:5
                },
                {
                    label:'OP1',
                    value:6
                },
            ],
            //气体
            gasesArr: [
                {
                    label:'CO2',
                    value:0
                },
                {
                    label:'MAG',
                    value:1
                },
                {
                    label:'MIG',
                    value:2
                },
                {
                    label:'OP1',
                    value:3
                },
                {
                    label:'OP2',
                    value:4
                },
                {
                    label:'OP3',
                    value:5
                },
                {
                    label:'OP4',
                    value:6
                },
            ],
            //手动送丝
            wireFeedArr: [
                {
                    label:'送丝停止',
                    value:0
                },
                {
                    label:'送丝启动',
                    value:1
                },                
            ],
            //检气状态
            checkGasStatusArr: [
                {
                    label:'检气停止',
                    value:0
                },
                {
                    label:'检气启动',
                    value:1
                },                
            ],
            //切换状态
            cutStatusArr:[
                {
                    label:'初期',
                    value:0
                },
                {
                    label:'焊接',
                    value:1
                },   
                {
                    label:'收弧',
                    value:2
                },                
            ],
            //锁定状态
            lockStatusArr:[
                {
                    label:'锁定无',
                    value:0
                },
                {
                    label:'锁定有',
                    value:1
                },                 
            ],

            //电流显示选择
            eleShowSelectArr:[
                {
                    label:'电流',
                    value:0
                },
                {
                    label:'速度',
                    value:1
                },  
                {
                    label:'板厚',
                    value:2
                },             
                {
                    label:'电焊时间',
                    value:3
                },      
            ],
            //电压显示选择
            volShowArr:[
                {
                    label:'电压',
                    value:0
                },
                {
                    label:'弧长',
                    value:1
                },  
                {
                    label:'电弧特性',
                    value:2
                },             
                {
                    label:'熔深控制',
                    value:3
                }, 
            ],
            
            //一元/分别
            unitaryDifferenceArr:[
                {
                    label:'一元化',
                    value:1
                },
                {
                    label:'分别',
                    value:2
                },   
            ],
            //通道号下拉            
            channelNoArr: [
                {
                    id: 0,
                    valueName: '通道1'
                },
                {
                    id: 1,
                    valueName: '通道2'
                },
                {
                    id: 2,
                    valueName: '通道3'
                },
                {
                    id: 3,
                    valueName: '通道4'
                },
                {
                    id: 4,
                    valueName: '通道5'
                },
                {
                    id: 5,
                    valueName: '通道6'
                },
                {
                    id: 6,
                    valueName: '通道7'
                },
                {
                    id: 7,
                    valueName: '通道8'
                },
                {
                    id: 8,
                    valueName: '通道9'
                },
                {
                    id: 9,
                    valueName: '通道10'
                },
            ],


            
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