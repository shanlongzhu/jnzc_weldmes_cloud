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
                ></vxe-table-column>
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
                                label="收弧"
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
                                        :value="item.id"
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
                                <el-checkbox v-model="ruleForm2.softArcSchema">柔软电弧模式</el-checkbox>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="电弧特性"
                                prop="arcCharacter"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.arcCharacter"
                                ></el-input>
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
                                >
                                    <el-option
                                        v-for="item in weldingStickTextureArr"
                                        :key="item.id"
                                        :label="item.valueName"
                                        :value="item.id"
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
                                    :min="0.1"
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
                                        :value="item.id"
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
                                    :controls="false"
                                    :precision="1"
                                    size="mini"
                                    style="width:100px"
                                    :min="0.1"
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
                                        :value="item.id"
                                    />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="滞后送气"
                                prop="hysteresisAspirated"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.hysteresisAspirated"
                                ></el-input>
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
                                        :value="item.id"
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
                                label="初期电流"
                                prop="initialEle"
                                label-width="120px"
                            >
                                <el-input-number
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
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电流"
                                prop="weldingEle"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.weldingEle"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电流"
                                prop="arcEle"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.arcEle"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="初期电压"
                                prop="initialVol"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.initialVol"
                                ></el-input>
                                <span> (V)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电压"
                                prop="weldingVol"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.weldingVol"
                                ></el-input>
                                <span> (V)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电压"
                                prop="arcVol"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.arcVol"
                                ></el-input>
                                <span> (V)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电压（一元）"
                                prop="weldingVolUnitary"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.weldingVolUnitary"
                                ></el-input>
                                <span> (±1)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电压（一元）"
                                prop="arcVolUnitary"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.arcVolUnitary"
                                ></el-input>
                                <span> (±1)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="初期电压（一元）"
                                prop="initialVolUnitary"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.initialVolUnitary"
                                ></el-input>
                                <span> (±1)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电流微调"
                                prop="weldingEleAdjust"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.weldingEleAdjust"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电流微调"
                                prop="arcEleAdjust"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.arcEleAdjust"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电压微调"
                                prop="weldingVolAdjust"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.weldingVolAdjust"
                                ></el-input>
                                <span> (V/%)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电压微调"
                                prop="arcVolAdjust"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                    v-model="ruleForm2.arcVolAdjust"
                                ></el-input>
                                <span> (V/%)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="报警电流上限"
                                prop="alarmsEleMax"
                                label-width="120px"
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
                                label-width="120px"
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
                                label-width="120px"
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
                                label-width="120px"
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
                        @click="submitForm2('ruleForm2')"
                    >保存</el-button>
                    <el-button @click="visable2 = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import { getTeam, getDictionaries, getProcesLibraryList, getProcesLibraryDetail, delProcesLibrary, editProcesLibrary, addProcesLibrary, getProcesLibraryChildDetail, getChannNos, addProcesLibraryChild, editProcesLibraryChild } from '_api/productionProcess/process'
import { getWeldingModel } from '_api/productionEquipment/production'
import expandTable from './components/expandTable.vue';
export default {
    components: { expandTable },
    data () {
        return {
            visable1: false,
            title: '新增工艺',
            ruleFormObj: {
                id: '',
                wpsName: '',//工艺库名称
                weldModel: '',//设备型号
                firmId: ''
            },
            ruleForm: {
                id: '',
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


            //工艺层
            visable2: false,
            title2: '新增工艺',
            ruleFormObj2: {
                wpsLibraryId: '',//工艺库id
                id: '',
                channelNo: '',//通道号
                initialCondition: '',//初期条件
                fusionControl: '',//熔深控制
                unitarySeveral: '',//一元/个别
                controlArc: '',//收弧方式
                softArcSchema: '',//柔软电弧模式
                arcCharacter: '',//电弧特性
                spotWeldingTime: 30,//点焊时间
                weldingStickTexture: '',//焊丝材质
                weldingStickDiameter: '',//焊丝直径
                inAdvanceAspirated: '',//提前送气
                hysteresisAspirated: '',//滞后送气
                gases: '',//气体
                weldingProcess: '',//焊接过程
                initialEle: '',//初期电流
                initialVol: '',//初期电压
                weldingEle: '',//焊接电流
                weldingVol: '',//焊接电压
                arcEle: '',//收弧电流
                arcVol: '',//收弧电压
                initialVolUnitary: '',//初期电压一元
                weldingVolUnitary: '',//焊接电压一元
                arcVolUnitary: '',//收弧电压一元
                weldingEleAdjust: '',//焊接电流微调
                weldingVolAdjust: '',//焊接电压微调
                arcEleAdjust: '',//收弧电流微调
                arcVolAdjust: '',//收弧电压微调
                alarmsEleMax: '',//报警电流上限
                alarmsEleMin: '',//报警电流下限
                alarmsVolMax: '',//报警电压上限
                alarmsVolMin: '',//报警电压下限
            },
            ruleForm2: {
                wpsLibraryId: '',//工艺库id
                id: '',
                channelNo: '',//通道号
                initialCondition: '',//初期条件
                fusionControl: '',//熔深控制
                unitarySeveral: '',//一元/个别
                controlArc: '',//收弧方式
                softArcSchema: '',//柔软电弧模式
                arcCharacter: '',//电弧特性
                spotWeldingTime: 3,//点焊时间
                weldingStickTexture: '',//焊丝材质
                weldingStickDiameter: '',//焊丝直径
                inAdvanceAspirated: 0.1,//提前送气
                hysteresisAspirated: '',//滞后送气
                gases: '',//气体
                weldingProcess: '',//焊接过程
                initialEle: 100,//初期电流
                initialVol: '',//初期电压
                weldingEle: '',//焊接电流
                weldingVol: '',//焊接电压
                arcEle: '',//收弧电流
                arcVol: '',//收弧电压
                initialVolUnitary: '',//初期电压一元
                weldingVolUnitary: '',//焊接电压一元
                arcVolUnitary: '',//收弧电压一元
                weldingEleAdjust: '',//焊接电流微调
                weldingVolAdjust: '',//焊接电压微调
                arcEleAdjust: '',//收弧电流微调
                arcVolAdjust: '',//收弧电压微调
                alarmsEleMax: '',//报警电流上限
                alarmsEleMin: '',//报警电流下限
                alarmsVolMax: '',//报警电压上限
                alarmsVolMin: '',//报警电压下限
            },
            rules2: {
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

            //收弧方式下拉
            controlArcArr: [],
            //焊丝材质
            weldingStickTextureArr: [],
            //气体
            gasesArr: [],
            //焊丝直径
            weldingStickDiameterArr: [],
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
            ],
            channelNoArr: [

            ],
        }
    },

    created () {
        this.getDicFun();
        this.getList();
    },
    methods: {
        //获取数据字典
        async getDicFun () {
            let { data, code } = await getDictionaries({ "types": ["7", "8", "9", "10", "11"] });
            if (code == 200) {
                this.controlArcArr = data['7'] || [];
                this.weldingStickTextureArr = data['8'] || [];
                this.gasesArr = data['9'] || [];
                this.weldingStickDiameterArr = data['10'] || [];
                this.weldingProcessArr = data['11'] || [];
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
        async changeFirm (id) {
            let { data, code } = await getWeldingModel({ id });
            if (code == 200) {
                this.modelArr = data || [];
            }
        },

        async editFun (id) {
            this.title = "修改工艺库"
            this.ruleForm = { ...this.ruleFormObj };
            let { data, code } = await getProcesLibraryDetail(id);
            if (code == 200) {
                this.visable1 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm.resetFields();
                    this.ruleForm = data[0] || {};
                    this.changeFirm(this.ruleForm.firmId);
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
                    Reflect.deleteProperty(this.ruleForm2, "id");
                })
            }
        },

        //子组件调用修改
        async editDetailFun (obj) {
            this.title2 = "修改工艺"
            this.ruleForm2 = { ...this.ruleFormObj2 };
             //获取已使用的通道
            let res = await getChannNos({ id:obj.parentId });           
            let { data, code } = await getProcesLibraryChildDetail(obj.id);
            if (code == 200) {
                this.visable2 = true;                
                this.$nextTick(() => {
                    this.$refs.ruleForm2.resetFields();
                    this.ruleForm2 = data[0] || {};
                     this.channelNoArr = this.channelNoSourceArr.filter(item => !res.data.includes(item.id)||item.id==this.ruleForm2.channelNo);
                    
                })
            }
        },

        // 新增/编辑提交工艺
        submitForm2 (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm2.hasOwnProperty('id')) {
                        const req = { ...this.ruleForm2 }
                        req.initialCondition = req.initialCondition ? 1 : 0;
                        req.fusionControl = req.fusionControl ? 1 : 0;
                        req.softArcSchema = req.softArcSchema ? 1 : 0;
                        const { data, code } = await editProcesLibraryChild(req)
                        if (code == 200) {
                            this.$message.success('修改成功')
                            this.visable1 = false
                            this.getList()
                        }
                    } else {
                        const req = { ...this.ruleForm2 }
                        req.initialCondition = req.initialCondition ? 1 : 0;
                        req.fusionControl = req.fusionControl ? 1 : 0;
                        req.softArcSchema = req.softArcSchema ? 1 : 0;
                        const { data, code } = await addProcesLibraryChild(req);
                        if (code == 200) {
                            this.$message.success('新增成功')
                            this.visable1 = false
                            this.getList()
                        }
                    }
                } else {
                    console.log('error submit!!')
                    return false
                }
            })
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
