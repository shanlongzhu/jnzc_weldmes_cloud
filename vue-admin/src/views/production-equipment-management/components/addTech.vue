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
                label-width="100px"
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
                <div
                    style="border:1px solid #aaa"
                    class="pt10 pb10 mt10"
                >
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧方式"
                                prop="controlArc"
                            >
                                <el-select
                                    v-model="ruleForm2.controlArc"
                                    placeholder="请选择"
                                    style="width:120px"
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
                                label="一元/个别"
                                prop="unitarySeveral"
                            >
                                <el-select
                                    v-model="ruleForm2.unitarySeveral"
                                    placeholder="请选择"
                                    style="width:120px"
                                    size="mini"
                                    @change="changeWelderType"
                                >
                                    <el-option
                                        v-for="item in unitarySeveralArr"
                                        :key="item.id"
                                        :label="item.valueName"
                                        :value="item.id"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item
                                label=""
                                label-width="40px"
                            >
                                <el-checkbox v-model="ruleForm2.initialCondition">初期条件</el-checkbox>
                                <el-checkbox v-model="ruleForm2.fusionControl">熔深控制</el-checkbox>
                                <el-checkbox
                                    @change="changeSoftArcSchema"
                                    v-model="ruleForm2.softArcSchema"
                                >柔软电弧模式</el-checkbox>
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
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="-99"
                                    :max="99"
                                    v-model="ruleForm2.arcCharacter"
                                ></el-input-number>
                                <span> (±1)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊丝材质"
                                prop="weldingStickTexture"
                            >
                                <el-select
                                    v-model="ruleForm2.weldingStickTexture"
                                    placeholder="请选择"
                                    style="width:120px"
                                    size="mini"
                                    @change="changekDiameter"
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
                                label="提前送气"
                                prop="inAdvanceAspirated"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="1"
                                    size="mini"
                                    style="width:100px"
                                    :min="0"
                                    :max="10"
                                    v-model="ruleForm2.inAdvanceAspirated"
                                ></el-input-number>
                                <span> (0.1s)</span>
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
                                    style="width:120px"
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
                                label="点焊时间"
                                prop="spotWeldingTime"
                            >
                                <el-input-number
                                    :disabled="spotWeldingTimeDisabled"
                                    :controls="false"
                                    :precision="1"
                                    size="mini"
                                    style="width:100px"
                                    :min="0"
                                    :max="10"
                                    v-model="ruleForm2.spotWeldingTime"
                                ></el-input-number>
                                <span> (0.1s)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊丝直径"
                                prop="weldingStickDiameter"
                            >
                                <el-select
                                    v-model="ruleForm2.weldingStickDiameter"
                                    placeholder="请选择"
                                    style="width:120px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in weldingStickDiameterArr"
                                        :key="item.id"
                                        :label="'Φ'+item.valueName"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="滞后送气"
                                prop="hysteresisAspirated"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="1"
                                    size="mini"
                                    style="width:100px"
                                    :min="0"
                                    :max="10"
                                    v-model="ruleForm2.hysteresisAspirated"
                                ></el-input-number>
                                <span> (0.1s)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接过程"
                                prop="weldingProcess"
                            >
                                <el-select
                                    v-model="ruleForm2.weldingProcess"
                                    placeholder="请选择"
                                    style="width:120px"
                                    size="mini"
                                >
                                    <el-option
                                        v-for="item in weldingProcessArr"
                                        :key="item.id"
                                        :label="item.valueName"
                                        :value="item.value"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
                <div
                    style="border:1px solid #aaa"
                    class="pt10 pb10 mt10"
                >
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电流"
                                prop="weldingEle"
                                label-width="130px"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.weldingEle"
                                ></el-input-number>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电流"
                                prop="arcEle"
                                label-width="130px"
                            >
                                <el-input-number
                                    :disabled="arcDisabled"
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.arcEle"
                                ></el-input-number>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="初期电流"
                                prop="initialEle"
                                label-width="130px"
                            >
                                <el-input-number
                                    :disabled="initialEleDisabled"
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="30"
                                    :max="550"
                                    v-model="ruleForm2.initialEle"
                                ></el-input-number>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row v-if="ruleForm2.unitarySeveral==1">
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电压"
                                prop="weldingVol"
                                label-width="130px"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="1"
                                    size="mini"
                                    style="width:100px"
                                    :min="12"
                                    :max="50"
                                    v-model="ruleForm2.weldingVol"
                                ></el-input-number>
                                <span> (V)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电压"
                                prop="arcVol"
                                label-width="130px"
                            >
                                <el-input-number
                                    :disabled="arcDisabled"
                                    :controls="false"
                                    :precision="1"
                                    size="mini"
                                    style="width:100px"
                                    :min="12"
                                    :max="50"
                                    v-model="ruleForm2.arcVol"
                                ></el-input-number>
                                <span> (V)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="初期电压"
                                prop="initialVol"
                                label-width="130px"
                            >
                                <el-input-number
                                    :disabled="initialEleDisabled"
                                    :controls="false"
                                    :precision="1"
                                    size="mini"
                                    style="width:100px"
                                    :min="12"
                                    :max="50"
                                    v-model="ruleForm2.initialVol"
                                ></el-input-number>
                                <span> (V)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row v-if="ruleForm2.unitarySeveral==0">
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电压(一元)"
                                prop="weldingVolUnitary"
                                label-width="130px"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="-30"
                                    :max="30"
                                    v-model="ruleForm2.weldingVolUnitary"
                                ></el-input-number>
                                <span> (±1)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电压（一元）"
                                prop="arcVolUnitary"
                                label-width="130px"
                            >
                                <el-input-number
                                    :disabled="arcDisabled"
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="-30"
                                    :max="30"
                                    v-model="ruleForm2.arcVolUnitary"
                                ></el-input-number>
                                <span> (±1)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="初期电压（一元）"
                                prop="initialVolUnitary"
                                label-width="130px"
                            >
                                <el-input-number
                                    :disabled="initialEleDisabled"
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="-30"
                                    :max="30"
                                    v-model="ruleForm2.initialVolUnitary"
                                ></el-input-number>
                                <span> (±1)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电流微调"
                                prop="weldingEleAdjust"
                                label-width="130px"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="0"
                                    :max="50"
                                    v-model="ruleForm2.weldingEleAdjust"
                                ></el-input-number>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电流微调"
                                prop="arcEleAdjust"
                                label-width="130px"
                            >
                                <el-input-number
                                    :controls="false"
                                    :disabled="arcDisabled"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="0"
                                    :max="50"
                                    v-model="ruleForm2.arcEleAdjust"
                                ></el-input-number>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="电流补偿值"
                                prop="eleCompensate"
                                label-width="130px"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="0"
                                    :max="50"
                                    v-model="ruleForm2.eleCompensate"
                                ></el-input-number>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                v-if="ruleForm2.unitarySeveral==1"
                                label="焊接电压微调"
                                prop="weldingVolAdjust"
                                label-width="130px"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="1"
                                    size="mini"
                                    style="width:100px"
                                    :min="0"
                                    :max="5"
                                    v-model="ruleForm2.weldingVolAdjust"
                                ></el-input-number>
                                <span> (V)</span>
                            </el-form-item>
                            <el-form-item
                                v-show="ruleForm2.unitarySeveral==0"
                                label="焊接电压微调(一元)"
                                prop="weldingVolAdjust"
                                label-width="130px"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="0"
                                    :max="20"
                                    v-model="ruleForm2.weldingVolAdjust"
                                ></el-input-number>
                                <span> (%)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                v-show="ruleForm2.unitarySeveral==1"
                                label="收弧电压微调"
                                prop="arcVolAdjust"
                                label-width="130px"
                            >
                                <el-input-number
                                    :disabled="arcDisabled"
                                    :controls="false"
                                    :precision="1"
                                    size="mini"
                                    style="width:100px"
                                    :min="0"
                                    :max="5"
                                    v-model="ruleForm2.arcVolAdjust"
                                ></el-input-number>
                                <span> (V)</span>
                            </el-form-item>
                            <el-form-item
                                v-show="ruleForm2.unitarySeveral==0"
                                label="收弧电压微调(一元)"
                                prop="arcVolAdjust"
                                label-width="130px"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="0"
                                    size="mini"
                                    style="width:100px"
                                    :min="0"
                                    :max="20"
                                    v-model="ruleForm2.arcVolAdjust"
                                ></el-input-number>
                                <span> (%)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="电压补偿值"
                                prop="volCompensate"
                                label-width="130px"
                            >
                                <el-input-number
                                    :controls="false"
                                    :precision="1"
                                    size="mini"
                                    style="width:100px"
                                    :min="0"
                                    :max="5"
                                    v-model="ruleForm2.volCompensate"
                                ></el-input-number>
                                <span> (V)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="报警电流上限"
                                prop="alarmsEleMax"
                                label-width="130px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.alarmsEleMax"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="报警电流下限"
                                prop="alarmsEleMin"
                                label-width="130px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.alarmsEleMin"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="报警电压上限"
                                prop="alarmsVolMax"
                                label-width="130px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.alarmsVolMax"
                                ></el-input>
                                <span> (V/%)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="报警电压下限"
                                prop="alarmsVolMin"
                                label-width="130px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.alarmsVolMin"
                                ></el-input>
                                <span> (V/%)</span>
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

        <!-- 选择设备 -->

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
    name:'addTech',
    components: {},
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
                wpsLibraryId: '',//工艺库id
                id: '',
                channelNo: '',//通道号
                initialCondition: false,//初期条件
                fusionControl: false,//熔深控制
                unitarySeveral: 1,//一元/个别
                controlArc: '000',//收弧方式
                controlArcType: '7',//收弧方式
                softArcSchema: false,//柔软电弧模式
                arcCharacter: 0,//电弧特性
                spotWeldingTime: 3,//点焊时间
                weldingStickTexture: '0',//焊丝材质
                weldingStickTextureType: '8',//焊丝材质Type
                weldingStickDiameter: '12',//焊丝直径
                weldingStickDiameterType: '10',//焊丝直径Type
                inAdvanceAspirated: 0.1,//提前送气
                hysteresisAspirated: 0.1,//滞后送气
                gases: '0',//气体
                gasesType: '9',//气体Typez
                weldingProcess: '1',//焊接过程
                weldingProcessType: '11',//焊接过程Type
                initialEle: 100,//初期电流
                initialVol: 19,//初期电压
                weldingEle: 100,//焊接电流
                weldingVol: 19,//焊接电压
                arcEle: 100,//收弧电流
                arcVol: 19,//收弧电压
                initialVolUnitary: 0,//初期电压一元
                weldingVolUnitary: 0,//焊接电压一元
                arcVolUnitary: 0,//收弧电压一元
                weldingEleAdjust: 0,//焊接电流微调
                weldingVolAdjust: 0,//焊接电压微调
                arcEleAdjust: 0,//收弧电流微调
                arcVolAdjust: 0,//收弧电压微调
                alarmsEleMax: 0,//报警电流上限
                alarmsEleMin: 0,//报警电流下限
                alarmsVolMax: 0,//报警电压上限
                alarmsVolMin: 0,//报警电压下限

                eleCompensate: 0,//电流补偿值
                volCompensate: 0,//电压补偿值
            },
            rules2: {
                channelNo:[
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
                if (topic == 'jnOtcV1ProcessClaimReturn') {
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
            this.ruleForm2.initialCondition = arrStr[7]?false:true;//初期条件
            this.ruleForm2.controlArc = arrStr[6] + arrStr[5] + arrStr[4];//收弧

            this.ruleForm2.unitarySeveral = parseInt(arrStr[2]);//一元/个别
            this.ruleForm2.fusionControl = arrStr[1]?false:true;//熔深控制
            this.ruleForm2.softArcSchema = arrStr[0]?false:true;//柔软电弧模式
            console.log(this.ruleForm2)
        },

        //订阅主题
        doSubscribe () {
            this.client.subscribe('jnOtcV1ProcessClaimReturn', 0, (error, res) => {
                if (error) {
                    console.log('Subscribe to topics error', error)
                    return
                }
            })
        },

        doPublish (msg) {
            this.client.publish('jnOtcV1ProcessClaim', msg, 0)
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
                    if(this.channelNoArr.length>0){
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
                    const req = { ...this.ruleForm2 };
                    req.weldingEle += req.eleCompensate;
                    req.weldingVol += req.volCompensate;
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
            this.doSubscribe();
            if (JSON.stringify(this.selectModel) == "{}") {
                return this.$message.error("请选择设备!!");
            } else if (this.selectModel.machineGatherInfo && this.selectModel.machineGatherInfo.gatherNo) {
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
                this.client.unsubscribe('jnOtcV1ProcessClaimReturn', error => {
                    console.log("取消订阅")
                    if (error) {
                        console.log('取消订阅失败', error)
                    }
                    setTimeout(() => {
                        this.client.end();
                    },500)
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
        this.createConnection();
    },
    mounted () { }
}
</script>
<style>
.teamList {
    z-index: 9999 !important;
}
</style>
