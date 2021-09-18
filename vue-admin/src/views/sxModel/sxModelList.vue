<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
            <!-- <div class="con-w">
                <span>编号：</span>
                <el-input
                    size="small"
                    class="w150"
                    v-model="searchObj.machineNo"
                ></el-input>
            </div>
            <div class="con-w">
                <span>类型：</span>
                <el-select
                    v-model="searchObj.type"
                    size="small"
                    class="w120"
                    clearable
                    placeholder="请选择"
                >
                    <el-option
                        v-for="item in typeArr"
                        :key="item.id"
                        :label="item.valueName"
                        :value="item.id"
                    />
                </el-select>
            </div>
            <div class="con-w">
                <span>所属项目：</span>
                <el-cascader
                    v-model="searchObj.grade"
                    size="small"
                    class="w150"
                    clearable
                    :options="teamArr"
                    :props="defalutProps"
                    :show-all-levels="false"
                />
            </div>
            <div class="con-w">
                <span>状态：</span>
                <el-select
                    v-model="searchObj.status"
                    size="small"
                    class="w100"
                    clearable
                    placeholder="请选择"
                >
                    <el-option
                        v-for="item in statusArr"
                        :key="item.id"
                        :label="item.valueName"
                        :value="item.id"
                    />
                </el-select>
            </div>

            <div class="con-w">
                <span>IP：</span>
                <el-input
                    size="small"
                    class="w150"
                    v-model="searchObj.ipPath"
                ></el-input>
            </div> -->
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
            <div class="con-w">
                <el-button
                    size="small"
                    icon="el-icon-plus"
                    @click="addFun"
                    v-has="'add'"
                >新增</el-button>
            </div>
            <!-- <div class="con-w">
                <el-upload
                    v-has="'import'"
                    :action="importUrl"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :headers="headers"
                    accept=".xlsx"
                >
                    <el-button
                        size="small"
                        icon="el-icon-document-add"
                    >导入</el-button>
                </el-upload>

            </div>
            <div class="con-w">
                <el-button
                    v-has="'exprot'"
                    size="small"
                    icon="el-icon-document-remove"
                    @click="exportExcelFun"
                >导出</el-button>
            </div> -->
            <!-- <div class="con-w">
                <el-button
                    v-has="'exprot'"
                    size="small"
                    icon="el-icon-document-remove"
                    @click="bindMichFun"
                >设备绑定</el-button>
            </div>
            <div class="con-w">
                <el-button
                    v-has="'exprot'"
                    size="small"
                    icon="el-icon-document-remove"
                    @click="bindAreaFun"
                >区间绑定</el-button>
            </div> -->

        </div>
        <div
            class="table-con"
            style="flex:1;height:0px"
        >
            <el-table
                id="out-table"
                :data="list"
                stripe
                style="width: 100%"
                align="center"
                border
                :loading="loading"
                size="mini"
                height='100%'
                :header-cell-style="{background:'#eef1f6',color:'#606266'}"
            >
                <el-table-column
                    label=""
                    align="left"
                    min-width="100"
                    type="index"
                    fixed="left"
                />
                <el-table-column
                    prop="weldNo"
                    label="设备序号"
                    align="left"
                    min-width="100"
                    fixed="left"
                >
                    <template slot="header">
                        <span>设备序号</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="weldCid"
                    label="设备CID"
                    align="left"
                    min-width="80"
                >
                    <template slot="header">
                        <span>设备CID</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="weldCode"
                    label="设备编码"
                    align="left"
                    min-width="150"
                >
                </el-table-column>
                <el-table-column
                    prop="weldIp"
                    label="IP地址"
                    align="left"
                    min-width="120"
                >
                    <template slot="header">
                        <span>IP地址</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="weldStatusStr"
                    label="状态"
                    align="left"
                    min-width="60"
                >
                </el-table-column>

                <el-table-column
                    prop="weldModel"
                    label="设备机型"
                    align="left"
                    min-width="120"
                >
                    <template slot="header">
                        <span>设备机型</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="powerSupplyStr"
                    label="电源类型"
                    align="left"
                    min-width="100"
                >
                </el-table-column>
                <el-table-column
                    prop="wireFeederModelStr"
                    label="送丝机类型"
                    align="left"
                    min-width="100"
                >
                </el-table-column>
                <el-table-column
                    prop="weldKindStr"
                    label="设备种类"
                    align="left"
                    min-width="100"
                >
                </el-table-column>

                <el-table-column
                    prop="weldCpuNum"
                    label="焊机CPU个数"
                    align="left"
                    min-width="100"
                >
                </el-table-column>
                <el-table-column
                    prop="cpu1No"
                    label="cpu1编号"
                    align="left"
                    min-width="100"
                >
                </el-table-column>
                <el-table-column
                    prop="cpu1ModelStr"
                    label="cpu1类型"
                    align="left"
                    min-width="120"
                >
                </el-table-column>
                <el-table-column
                    prop="cpu1Version"
                    label="cpu1软件版本"
                    align="left"
                    min-width="120"
                >
                </el-table-column>
                <el-table-column
                    prop="cpu2No"
                    label="cpu2编号"
                    align="left"
                    min-width="100"
                >
                </el-table-column>
                <el-table-column
                    prop="cpu2ModelStr"
                    label="cpu2类型"
                    align="left"
                    min-width="120"
                >
                </el-table-column>
                <el-table-column
                    prop="cpu2Version"
                    label="cpu2软件版本"
                    align="left"
                    min-width="120"
                >
                </el-table-column>
                <el-table-column
                    prop="cpu3No"
                    label="cpu3编号"
                    align="left"
                    min-width="100"
                >
                </el-table-column>
                <el-table-column
                    prop="cpu3ModelStr"
                    label="cpu3类型"
                    align="left"
                    min-width="120"
                >
                </el-table-column>
                <el-table-column
                    prop="cpu3Version"
                    label="cpu3软件版本"
                    align="left"
                    min-width="120"
                >
                </el-table-column>
                <el-table-column
                    prop="deptIdStr"
                    label="所属项目"
                    align="left"
                    min-width="100"
                >
                </el-table-column>
                <el-table-column
                    prop="createTime"
                    label="创建时间"
                    align="left"
                    min-width="150"
                >
                </el-table-column>
                <el-table-column
                    prop="weldFirmStr"
                    label="生产厂商"
                    align="left"
                    min-width="100"
                >
                </el-table-column>

                <el-table-column
                    label="操作"
                    align="left"
                    class-name="small-padding fixed-width"
                    width="250"
                    fixed="right"
                >
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="primary"
                            plain
                            v-has="'edit'"
                            @click="editFun(scope.row.id)"
                        >
                            修改
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            plain
                            v-has="'del'"
                            @click="delFun(scope.row.id)"
                        >
                            删除
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            plain
                            v-has="'record'"
                        >
                            维修记录
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-pagination
            class="p10"
            :current-page.sync="page"
            :page-size="pageSize"
            align="right"
            background
            :page-sizes="[10, 50, 100, 150, 200]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            :total="total"
            @current-change="handleCurrentChange"
        />

        <!-- 新增/修改 -->
        <el-dialog
            :title="title"
            :visible.sync="visable1"
            width="1000px"
            :close-on-click-modal="false"
        >
            <el-form
                ref="ruleForm"
                :model="ruleForm"
                :rules="rules"
                label-width="150px"
                class="demo-ruleForm"
            >
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="设备序号"
                            prop="weldNo"
                        >
                            <el-input
                                v-model="ruleForm.weldNo"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="设备CID"
                            prop="weldCid"
                        >
                            <el-input
                                v-model="ruleForm.weldCid"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="设备编码"
                            prop="weldCode"
                        >
                            <el-input
                                v-model="ruleForm.weldCode"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="IP地址"
                            prop="weldIp"
                        >
                            <el-input
                                v-model="ruleForm.weldIp"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="设备机型"
                            prop="weldModel"
                        >
                            <el-input
                                v-model="ruleForm.weldModel"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="电源类型"
                            prop="powerSupply"
                        >
                            <el-select
                                v-model="ruleForm.powerSupply"
                                placeholder="电源类型"
                                style="width:250px"
                            >
                                <el-option
                                    v-for="item in powerSupplyArr"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="送丝机类型"
                            prop="wireFeederModel"
                        >
                            <el-select
                                v-model="ruleForm.wireFeederModel"
                                placeholder="送丝速度"
                                style="width:250px"
                            >
                                <el-option
                                    v-for="item in wireFeederModelArr"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="设备种类"
                            prop="weldKind"
                        >
                            <el-select
                                v-model="ruleForm.weldKind"
                                placeholder="设备种类"
                                style="width:250px"
                            >
                                <el-option
                                    v-for="item in weldKindArr"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="焊机CPU个数"
                            prop="weldCpuNum"
                        >
                            <el-input
                                v-model="ruleForm.weldCpuNum"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="cpu1编号"
                            prop="cpu1No"
                        >
                            <el-input
                                v-model="ruleForm.cpu1No"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="cpu1类型"
                            prop="cpu1Model"
                        >
                            <el-select
                                v-model="ruleForm.cpu1Model"
                                placeholder="cpu1"
                                style="width:250px"
                            >
                                <el-option
                                    v-for="item in cpu1Model"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="cpu1软件版本"
                            prop="cpu1Version"
                        >
                            <el-input
                                v-model="ruleForm.cpu1Version"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="cpu2编号"
                            prop="cpu2No"
                        >
                            <el-input
                                v-model="ruleForm.cpu2No"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="cpu2类型"
                            prop="cpu2Model"
                        >
                            <el-select
                                v-model="ruleForm.cpu2Model"
                                placeholder="cpu2"
                                style="width:250px"
                            >
                                <el-option
                                    v-for="item in cpu2Model"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="cpu2软件版本"
                            prop="cpu2Version"
                        >
                            <el-input
                                v-model="ruleForm.cpu2Version"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="cpu3编号"
                            prop="cpu3No"
                        >
                            <el-input
                                v-model="ruleForm.cpu3No"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="cpu3类型"
                            prop="cpu3Model"
                        >
                            <el-select
                                v-model="ruleForm.cpu3Model"
                                placeholder="cpu3"
                                style="width:250px"
                            >
                                <el-option
                                    v-for="item in cpu3Model"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="cpu3软件版本"
                            prop="cpu3Version"
                        >
                            <el-input
                                v-model="ruleForm.cpu3Version"
                                style="width:250px"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="状态"
                            prop="weldStatus"
                        >
                            <el-radio-group v-model="ruleForm.weldStatus">
                                <el-radio
                                    :label="item.id"
                                    v-for="item in statusArr"
                                    :key="item.id"
                                >{{item.valueName}}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="所属项目"
                            prop="deptId"
                        >
                            <el-cascader
                                v-model="ruleForm.deptId"
                                size="small"
                                style="width:250px"
                                clearable
                                :options="teamArr"
                                :props="defalutProps"
                                :show-all-levels="false"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="区域"
                            prop="area"
                        >
                            <el-select
                                v-model="ruleForm.area"
                                placeholder="请选择"
                                style="width:250px"
                                @change="changeArea"
                            >
                                <el-option
                                    v-for="item in areaArr"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="跨间"
                            prop="bay"
                        >
                            <el-select
                                v-model="ruleForm.bay"
                                placeholder="请选择"
                                style="width:250px"
                            >
                                <el-option
                                    v-for="item in straddleArr"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item
                            label="创建时间"
                            prop="createTime"
                        >
                            <el-date-picker
                                v-model="ruleForm.createTime"
                                style="width:250px"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                type="datetime"
                                placeholder="选择日期时间"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item
                            label="生产厂商"
                            prop="weldFirm"
                        >
                            <el-select
                                v-model="ruleForm.weldFirm"
                                placeholder="请选择"
                                style="width:250px"
                            >
                                <el-option
                                    v-for="item in manufactorArr"
                                    :key="item.id"
                                    :label="item.valueName"
                                    :value="item.id"
                                />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item>
                    <el-button
                        type="primary"
                        @click="submitForm('ruleForm')"
                    >保存</el-button>
                    <el-button @click="visable1 = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import { getWelderList, getWelderDetail, delWelder, exportWelderExcel, getAllGatherNos, addWelder, editWelder, getWeldingModel, findByIdArea, getSxWelderList, addSxWelder, getSxWelderDetail, delSxWelder, editSxWelder } from '_api/productionEquipment/production'
import { getTeam, getDictionaries } from '_api/productionProcess/process'
import { getToken } from '@/utils/auth'

export default {
    components: {},
    name: 'sxModelList',
    data () {
        return {
            //搜索条件
            searchObj: {
                equipType: ''
            },
            //搜索条件设备型号
            modelArr2: [],
            list: [],
            //分页
            page: 1,
            total: 0,
            pageSize: 10,

            visable1: false,
            ruleFormObj: {
            },
            ruleForm: {
                weldNo: "",
                weldCid: "",
                weldCode: "",
                weldIp: "",
                weldModel: "",
                powerSupply: "",
                powerSupplyStr: "",
                wireFeederModel: "",
                wireFeederModelStr: "",
                weldKind: "",
                weldKindStr: "",
                weldCpuNum: "",
                cpu1No: "",
                cpu1Model: "",
                cpu1ModelStr: "",
                cpu1Version: "",
                cpu2No: "",
                cpu2Model: "",
                cpu2ModelStr: "",
                cpu2Version: "",
                cpu3No: "",
                cpu3Model: "",
                cpu3ModelStr: "",
                cpu3Version: "",
                weldStatus: "",
                weldStatusStr: "",
                weldFirm: 21,
                weldFirmStr: "",
                deptId: "",
                deptIdStr: "",
                createTime: "",
                area: '',//区域
                bay: ''//跨间
            },
            rules: {
                weldNo: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                weldCid: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                weldIp: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                weldCode: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],                
                area: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                bay: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
            },
            title: '修改任务',
            dateTime: '',

            //机构数据
            teamArr: [],
            //设备型号
            modelArr: [],
            //状态
            statusArr: [],
            //设备类型
            typeArr: [],
            //厂家
            manufactorArr: [],
            //采集序号下拉
            gatherNos: [],
            //区域
            areaArr: [],
            //跨间
            straddleArr: [],
            straddleArr2: [],
            // 级联下拉配置
            defalutProps: {
                label: 'name',
                value: 'id',
                children: 'list'
            },
            importUrl: `${process.env.VUE_APP_BASE_API}/welder/importExcel`,
            headers: {
                'Authorization': getToken()
            },
            loading: false,
            //电源类型列表
            powerSupplyArr: [],
            //送丝速度列表
            wireFeederModelArr: [],
            //设备种类列表
            weldKindArr: [],
            //cpu1类型列表
            cpu1Model: [],
            //cpu2类型列表
            cpu2Model: [],
            //cpu3类型
            cpu3Model: []
        }
    },

    created () {
        this.ruleFormObj = { ...this.ruleForm };
        // 获取班组
        this.getTeamList();
        this.getList();
        this.getDicFun();
    },
    methods: {
        //获取数据字典
        async getDicFun () {
            let { data, code } = await getDictionaries({ "types": ["3", "4", "5", "6", "16", "17", "18", "19", "20", "21", "22", "23"] });
            if (code == 200) {
                this.statusArr = data['4'] || [];
                this.manufactorArr = data['5'] || [];
                this.typeArr = data['3'] || [];
                this.areaArr = data['16'] || [];
                this.straddleArr2 = data['17'] || [];
                this.modelArr2 = data['6'] || [];


                //电源类型
                this.powerSupplyArr = data['18'] || [];
                //送丝速度
                this.wireFeederModelArr = data['19'] || [];
                //设备种类
                this.weldKindArr = data['23'] || [];
                //cpu1类型列表
                this.cpu1Model = data['20'] || [];
                //cpu2类型列表
                this.cpu2Model = data['21'] || [];
                //cpu3类型列表
                this.cpu3Model = data['22'] || [];
            }
        },
        //获取全部采集序号
        async getAllGatherNosFun () {
            let { data, code } = await getAllGatherNos();
            if (code == 200) {
                this.gatherNos = data || []
            }
        },
        search () {
            this.page = 1;
            this.getList();
        },
        async getList () {
            let req = {
                pn: this.page,
                size: this.pageSize,
                ...this.searchObj
            }
            // req.grade = this.searchObj.grade && this.searchObj.grade.length > 0 ? this.searchObj.grade.slice(-1).join('') : ''
            this.loading = true;
            let { data, code } = await getSxWelderList(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list
                this.total = data.total
            }
        },

        //根据厂家获取关联设备型号
        async changeFirm (id) {
            let { data, code } = await getWeldingModel({ id });
            if (code == 200) {
                this.modelArr = data || [];
            }
        },


        //新增
        addFun () {
            this.title = "新增焊机设备"
            this.visable1 = true;
            this.modelArr = [];
            this.getAllGatherNosFun();
            this.$nextTick(() => {
                this.$refs.ruleForm.resetFields();
                this.ruleForm = { ...this.ruleFormObj };
                Reflect.deleteProperty(this.ruleForm, "id");
            })
        },
        //修改
        async editFun (id) {
            this.title = "修改焊机设备"
            this.getAllGatherNosFun();
            this.ruleForm = { ...this.ruleFormObj };
            let { data, code } = await getSxWelderDetail({ id });
            if (code == 200) {
                this.visable1 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm.resetFields();
                    this.ruleForm = data || {};
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
                let { code, data } = await delSxWelder({ id })
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.getList()
                }
            }).catch(() => { })

        },

        // 获取班组
        async getTeamList () {
            const { data, code } = await getTeam()
            this.teamArr = data.workArea || []
        },
        //分页切换
        handleCurrentChange (p) {
            this.page = p;
            this.getList();
        },
        handleSizeChange (s) {
            this.pageSize = s;
            this.getList();
        },



        //导出
        exportExcelFun () {
            this.$message({
                showClose: true,
                message: '导出中...',
                type: 'success',
                duration: 1000
            });
            let req = {
                ...this.searchObj
            }
            req.grade = this.searchObj.grade && this.searchObj.grade.length > 0 ? this.searchObj.grade.slice(-1).join('') : ''
            location.href = exportWelderExcel(req)
        },
        handleAvatarSuccess (res, file) {
            if (res.code == 200) {
                this.$message.success("导入成功");
                this.search();
            }
        },
        // 新增/编辑提交
        submitForm (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty('id')) {
                        const req = { ...this.ruleForm }
                        req.deptId = req.deptId && req.deptId.length > 0 ? req.deptId.slice(-1).join('') : req.deptId
                        const { data, code } = await editSxWelder(req)
                        if (code == 200) {
                            this.$message.success('修改成功')
                            this.visable1 = false
                            this.getList()
                        }
                    } else {
                        const req = { ...this.ruleForm }
                        req.deptId = req.deptId && req.deptId.length > 0 ? req.deptId.slice(-1).join('') : req.deptId
                        const { data, code } = await addSxWelder(req);
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

        async changeArea (id) {
            let { code, data } = await findByIdArea({ id });
            if (code == 200) {
                this.straddleArr = data || []
            }
        },

        //设备绑定
        bindMichFun () {
            this.$refs.equipmentBind.init();
        },
        //区间绑定
        bindAreaFun () {
            this.$refs.areaBind.init();
        },
    }
}
</script>

<style scoped>
.cell span.green {
    color: rgb(0, 190, 73);
}
.cell span.waring {
    color: rgb(224, 183, 0);
}
.cell span.error {
    color: #f00;
}
</style>
