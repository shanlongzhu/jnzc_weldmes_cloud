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
                                label="初期送丝速度上限"
                                prop="initialWireSpeedMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialWireSpeedMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="初期送丝速度下限"
                                prop="initialWireSpeedMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.initialWireSpeedMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电流上限"
                                prop="weldEleMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.weldEleMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电流下限"
                                prop="weldEleMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.weldEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电压上限"
                                prop="weldVolMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.weldVolMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接电压下限"
                                prop="weldVolMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.weldVolMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接送丝速度上限"
                                prop="weldWireSpeedMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.weldWireSpeedMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接送丝速度下限"
                                prop="weldWireSpeedMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.weldWireSpeedMin"
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
                                label="收弧送丝速度上限"
                                prop="arcWireSpeedMax"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcWireSpeedMax"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧送丝速度下限"
                                prop="arcWireSpeedMin"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.arcWireSpeedMin"
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
                                label="模式选择"
                                prop="modeSelect"
                            >
                                <el-select
                                    v-model="ruleForm2.modeSelect"
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
                        <el-col :span="6">
                            <el-form-item
                                label="焊接方式"
                                prop="weldingManner"
                            >
                                <el-select
                                    v-model="ruleForm2.weldingManner"
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
                    </el-row>
                    <el-row>
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
                                label="手动送丝"
                                prop="wireFeed"
                            >
                                <el-select
                                    v-model="ruleForm2.wireFeed"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in wireFeedArr"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="检气状态"
                                prop="checkGasStatus"
                            >
                                <el-select
                                    v-model="ruleForm2.checkGasStatus"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in checkGasStatusArr"
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
                                label="切换状态"
                                prop="cutStatus"
                            >
                                <el-select
                                    v-model="ruleForm2.cutStatus"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in cutStatusArr"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="锁定状态"
                                prop="lockStatus"
                            >
                                <el-select
                                    v-model="ruleForm2.lockStatus"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in lockStatusArr"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="电流显示选择"
                                prop="eleShowSelect"
                            >
                                <el-select
                                    v-model="ruleForm2.eleShowSelect"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in eleShowSelectArr"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="转速"
                                prop="rev"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="250"
                                    v-model="ruleForm2.rev"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="板厚"
                                prop="boardThickness"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="200"
                                    v-model="ruleForm2.boardThickness"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="点焊时间"
                                prop="spotWeldingTime"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="300"
                                    v-model="ruleForm2.spotWeldingTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="电压显示选择"
                                prop="volShowSelect"
                            >
                                <el-select
                                    v-model="ruleForm2.volShowSelect"
                                    placeholder="请选择"
                                    style="width:110px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in volShowArr"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="弧长"
                                prop="arcLength"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="100"
                                    v-model="ruleForm2.arcLength"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="电弧特性"
                                prop="arcCharacter"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="20"
                                    v-model="ruleForm2.arcCharacter"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="熔深控制"
                                prop="penetrationControl"
                            >
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="3"
                                    v-model="ruleForm2.penetrationControl"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="提前送气时间"
                                prop="beforeAspiratedTime"
                            >
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    :max="300"
                                    v-model="ruleForm2.afterStopGasTime"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                label="收弧电流上限"
                                prop="dclArcEleMax"
                            >
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
                                    :controls="false"
                                    size="mini"
                                    style="width:110px"
                                    :min="0"
                                    v-model="ruleForm2.dclArcEleMin"
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="启动延时时间"
                                prop="startDelayTime"
                            >
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                label="收弧电流上限"
                                prop="oaArcEleMax"
                            >
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="0"
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
                                <el-input-number
                                    :precision="1"
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
                                <el-input-number
                                    :precision="1"
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
                                <el-input-number
                                    :precision="1"
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
                                <el-input-number
                                    :precision="1"
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
import { getTeam, getDictionaries, getProcesLibraryChildDetail, getCO2ChannaNoIsUse, addProcesLibraryChild, editProcesLibraryChild, addSxCo2Tech, getSxCo2TechDetail, editSxCo2TechDetail } from '_api/productionProcess/process'

import { getWelderList, getSxWelderList } from '_api/productionEquipment/production'
export default {
    name: 'addSxCO2',
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
            timeout: '',


            //工艺层
            visable2: false,
            title2: '新增工艺',
            ruleFormObj2: {},
            ruleForm2: {
                //松下co2工艺
                channelNo: '',//通道编号
                // weldIp: '',//下发的焊机IP
                // weldCid: "2",

                //*** 预置参数 */
                initialEleMax: 0,//初期电流上限
                initialEleMin: 0,//初期电流下限
                initialVolMax: 0,//初期电压上限
                initialVolMin: 0,//初期电压下限
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
            wireDiameterArr: [
                {
                    label: '0.8',
                    value: 0
                },
                {
                    label: '0.9',
                    value: 1
                },
                {
                    label: '1.0',
                    value: 2
                },
                {
                    label: '1.2',
                    value: 3
                },
                {
                    label: '1.4',
                    value: 4
                },
                {
                    label: '1.6',
                    value: 5
                },
                {
                    label: 'OP1',
                    value: 6
                },
            ],
            //气体
            gasesArr: [
                {
                    label: 'CO2',
                    value: 0
                },
                {
                    label: 'MAG',
                    value: 1
                },
                {
                    label: 'MIG',
                    value: 2
                },
                {
                    label: 'OP1',
                    value: 3
                },
                {
                    label: 'OP2',
                    value: 4
                },
                {
                    label: 'OP3',
                    value: 5
                },
                {
                    label: 'OP4',
                    value: 6
                },
            ],
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
                if (topic == 'jnSxGL5CO2ProcessClaimReturn') {
                    clearTimeout(this.timeout);
                    console.log(`${message}`)
                    var datajson = JSON.parse(`${message}`);
                    // ****预置参数**/
                    this.ruleForm2.initialEleMax = datajson['initialEleMax'];//初期电流上限
                    this.ruleForm2.initialEleMin = datajson['initialEleMin'];//初期电流下限
                    this.ruleForm2.initialVolMax = datajson['initialVolMax'];//初期电压上限
                    this.ruleForm2.initialVolMin = datajson['initialVolMin'];//初期电压下限
                    this.ruleForm2.initialWireSpeedMax = datajson['initialWireSpeedMax'];//初期送丝速度上限
                    this.ruleForm2.initialWireSpeedMin = datajson['initialWireSpeedMin'];//初期送丝速度下限
                    this.ruleForm2.weldEleMax = datajson['weldEleMax'];//焊接电流上限
                    this.ruleForm2.weldEleMin = datajson['weldEleMin'];//焊接电流下限
                    this.ruleForm2.weldVolMax = datajson['weldVolMax'];//焊接电压上限
                    this.ruleForm2.weldVolMin = datajson['weldVolMin'];//焊接电压下限
                    this.ruleForm2.weldWireSpeedMax = datajson['weldWireSpeedMax'];//焊接送丝速度上限
                    this.ruleForm2.weldWireSpeedMin = datajson['weldWireSpeedMin'];//焊接送丝速度下限
                    this.ruleForm2.arcEleMax = datajson['arcEleMax'];//收弧电流上限
                    this.ruleForm2.arcEleMin = datajson['arcEleMin'];//收弧电流下限
                    this.ruleForm2.arcVolMax = datajson['arcVolMax'];//收弧电压上限
                    this.ruleForm2.arcVolMin = datajson['arcVolMin'];//收弧电压下限
                    this.ruleForm2.arcWireSpeedMax = datajson['arcWireSpeedMax'];//收弧送丝速度上限
                    this.ruleForm2.arcWireSpeedMin = datajson['arcWireSpeedMin'];//收弧送丝速度下限

                    // *****焊接条件*****/
                    this.ruleForm2.modeSelect = datajson['modeSelect'];//模式选择
                    this.ruleForm2.weldingControl = datajson['weldingControl'];//焊接控制
                    this.ruleForm2.weldingManner = datajson['weldingManner'];//焊接方式
                    this.ruleForm2.texture = datajson['texture'];//材质
                    this.ruleForm2.wireDiameter = datajson['wireDiameter'];//丝径
                    this.ruleForm2.gases = datajson['gases'];//气体
                    this.ruleForm2.wireFeed = datajson['wireFeed'];//手动送丝

                    this.ruleForm2.checkGasStatus = datajson['checkGasStatus'];//检气状态
                    this.ruleForm2.cutStatus = datajson['cutStatus'];//切换状态
                    this.ruleForm2.lockStatus = datajson['lockStatus'];//锁定状态
                    this.ruleForm2.eleShowSelect = datajson['eleShowSelect'];//电流显示选择
                    this.ruleForm2.rev = datajson['rev'];//转速
                    this.ruleForm2.boardThickness = datajson['boardThickness'];//板厚
                    this.ruleForm2.spotWeldingTime = datajson['spotWeldingTime'];//点焊时间
                    this.ruleForm2.volShowSelect = datajson['volShowSelect'];//电压显示选择
                    this.ruleForm2.arcLength = datajson['arcLength'];//弧长
                    this.ruleForm2.arcCharacter = datajson['arcCharacter'];//电弧特性
                    this.ruleForm2.penetrationControl = datajson['penetrationControl'];//熔深控制
                    this.ruleForm2.beforeAspiratedTime = datajson['beforeAspiratedTime'];//提前送气时间
                    this.ruleForm2.afterStopGasTime = datajson['afterStopGasTime'];//滞后停气时间
                    this.ruleForm2.unitaryDifference = datajson['unitaryDifference'];//一元/分别
                    this.ruleForm2.nowChannel = datajson['nowChannel'];//当前通道

                    //******动态限流参数****** */
                    this.ruleForm2.dclInitialEleMax = datajson['dclInitialEleMax'];//初期电流上限
                    this.ruleForm2.dclInitialEleMin = datajson['dclInitialEleMin'];//初期电流下限
                    this.ruleForm2.dclWeldEleMax = datajson['dclWeldEleMax'];//焊接电流上限
                    this.ruleForm2.dclWeldEleMin = datajson['dclWeldEleMin'];//焊接电流下限
                    this.ruleForm2.dclArcEleMax = datajson['dclArcEleMax'];//收弧电流上限
                    this.ruleForm2.dclArcEleMin = datajson['dclArcEleMin'];//收弧电流下限
                    this.ruleForm2.startDelayTime = datajson['startDelayTime'];//启动延时时间
                    this.ruleForm2.clAmendPeriod = datajson['clAmendPeriod'];//限流修正周期

                    //*** 超限报警参数 */
                    this.ruleForm2.oaInitialEleMax = datajson['oaInitialEleMax'];//初期电流上限
                    this.ruleForm2.oaInitialEleMin = datajson['oaInitialEleMin'];//初期电流下限
                    this.ruleForm2.oaInitialVolMax = datajson['oaInitialVolMax'];//初期电压上限
                    this.ruleForm2.oaInitialVolMin = datajson['oaInitialVolMin'];//初期电压下限
                    this.ruleForm2.oaWeldEleMax = datajson['oaWeldEleMax'];//焊接电流上限
                    this.ruleForm2.oaWeldEleMin = datajson['oaWeldEleMin'];//焊接电流下限
                    this.ruleForm2.oaWeldVolMax = datajson['oaWeldVolMax'];//焊接电压上限
                    this.ruleForm2.oaWeldVolMin = datajson['oaWeldVolMin'];//焊接电压下限
                    this.ruleForm2.oaArcEleMax = datajson['oaArcEleMax'];//收弧电流上限
                    this.ruleForm2.oaArcEleMin = datajson['oaArcEleMin'];//收弧电流下限
                    this.ruleForm2.oaArcVolMax = datajson['oaArcVolMax'];//收弧电压上限
                    this.ruleForm2.oaArcVolMin = datajson['oaArcVolMin'];//收弧电压下限
                    this.ruleForm2.arcDelayTime = datajson['arcDelayTime'];//起弧延时时间
                    this.ruleForm2.alarmDelayTime = datajson['alarmDelayTime'];//报警延时时间
                    this.ruleForm2.haltDelayTime = datajson['haltDelayTime'];//停机延时时间
                    this.ruleForm2.haltFreezeTime = datajson['haltFreezeTime'];//停机冻结时间
                  this.messageObj.close();
                  this.issLoading = false;
                    this.$message.success("索取成功！！！");
                    this.model2 = false;
                    this.issueTimeOut();
                }
                if (topic == 'jnSxGL5ProcessClaimReturn') {
                    clearTimeout(this.timeout);
                    console.log(`${message}`)
                    var datajson = JSON.parse(`${message}`);
                    // if(datajson['dataFlag']==1){
                    //     this.$message.success("索取成功！！！");
                    // }
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
            //订阅有数据回复
            this.client.subscribe('jnSxGL5CO2ProcessClaimReturn', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            });
            //订阅无数据回复
            this.client.subscribe('jnSxGL5ProcessClaimReturn', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            });
        },

        doPublish (msg) {
            this.client.publish('jnSxGl5ProcessClaim', msg, 0)
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
            this.title2 = "修改工艺CO2"
            this.ruleForm2 = { ...this.ruleFormObj2 };
            //获取已使用的通道
            let res = await getCO2ChannaNoIsUse({ id: obj.parentId });
            let { data, code } = await getSxCo2TechDetail({ id: obj.id });
            if (code == 200) {
                this.visable2 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm2.resetFields();
                    this.ruleForm2 = data || {};
                    this.channelNoArr = this.channelNoSourceArr.filter(item => !res.data.includes(item.id) || item.id == this.ruleForm2.channelNo);
                })
            }
        },

        //新增工艺
        async addLibraryFun (id) {
            this.title2 = "新建工艺CO2"
            let { code, data } = await getCO2ChannaNoIsUse({ id });
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
            if (req.hasOwnProperty('id')) {
                const { data, code } = await editSxCo2TechDetail(req)
                if (code == 200) {
                    this.$message.success('修改成功')
                    console.log(this.$refs.addTech)
                    this.visable2 = false
                    this.$parent.getList()
                }
            } else {
                const { data, code } = await addSxCo2Tech(req);
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
                    msg['weldCid'] = this.selectModel.weldCid;
                    msg['weldIp'] = this.selectModel.weldIp;
                    msg['readWriteFlag'] = 1;
                    msg['channelNo'] = this.ruleForm2.channelNo;
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
                this.client.unsubscribe('jnSxGL5CO2ProcessClaimReturn', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                });

                this.client.unsubscribe('jnSxGL5ProcessClaimReturn', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                });

                if (n) {
                  this.messageObj.close();
                  this.issLoading = false;
                    this.$message.error("下发超时")
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
