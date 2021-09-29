<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
            <div class="con-w">
                <span>设备编号：</span>
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
                <span>厂家：</span>
                <el-select
                    v-model="searchObj.firm"
                    size="small"
                    class="w120"
                    clearable
                    placeholder="请选择"
                >
                    <el-option
                        v-for="item in manufactorArr"
                        :key="item.id"
                        :label="item.valueName"
                        :value="item.id"
                    />
                </el-select>
            </div>
            <div class="con-w">
                <span>是否在网：</span>
                <el-select
                    v-model="searchObj.isNetwork"
                    size="small"
                    class="w100"
                    clearable
                    placeholder="请选择"
                >
                    <el-option
                        label="是"
                        value="0"
                    />
                    <el-option
                        label="否"
                        value="1"
                    />
                </el-select>
            </div>
            <div class="con-w">
                <span>采集编号：</span>
                <el-input
                    size="small"
                    class="w150"
                    v-model="searchObj.gatherNo"
                ></el-input>
            </div>
            <div class="con-w">
                <span>IP：</span>
                <el-input
                    size="small"
                    class="w150"
                    v-model="searchObj.ipPath"
                ></el-input>
            </div>
            <div class="con-w">
                <span>设备型号：</span>
                <el-select
                    v-model="searchObj.model"
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
            <div class="con-w">
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
            </div>
            <div class="con-w">
                <el-button
                    v-has="'bind'"
                    size="small"
                    icon="el-icon-document-remove"
                    @click="bindMichFun"
                >设备绑定</el-button>
            </div>
            <div class="con-w">
                <el-button
                    v-has="'bind'"
                    size="small"
                    icon="el-icon-document-remove"
                    @click="bindAreaFun"
                >区间绑定</el-button>
            </div>
            <div class="con-w">
                <el-button
                    v-has="'bind'"
                    size="small"
                    icon="el-icon-document-remove"
                    @click="bindAreaFun2"
                >区间绑定2</el-button>
            </div>

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
                    prop="machineNo"
                    label="设备编号"
                    align="left"
                    min-width="120"
                    fixed="left"
                >
                <template
                        slot="header"
                    >
                        <span>设备编号</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="modelStr"
                    label="设备类型"
                    align="left"
                    min-width="120"
                >
                <template
                        slot="header"
                    >
                        <span>设备类型</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="createTime"
                    label="入厂时间"
                    align="left"
                    min-width="150"
                >
                </el-table-column>
                <el-table-column
                    prop="deptName"
                    label="所属项目"
                    align="left"
                    min-width="120"
                >
                 <template
                        slot="header"
                    >
                        <span>所属项目</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="ipPath"
                    label="状态"
                    align="left"
                    min-width="60"
                >
                    <template slot-scope="scope">
                        <span :class="{'green':scope.row.statusStr=='启用','waring':scope.row.statusStr=='维修','error':scope.row.statusStr=='报废'}">{{scope.row.statusStr}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="macPath"
                    label="厂家"
                    align="left"
                    min-width="100"
                >
                 <template
                        slot="header"
                    >
                        <span>厂家</span><span class="red-star">*</span>
                    </template>
                    <template slot-scope="scope">
                        {{scope.row.firmStr}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="createTime"
                    label="是否在网"
                    align="left"
                    min-width="70"
                >
                    <template slot-scope="scope">
                        {{scope.row.isNetwork?'否':'是'}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="gatherNo"
                    label="采集编号"
                    align="left"
                    min-width="100"
                    show-overflow-tooltip
                >
                    <template
                        slot="header"
                    >
                        <span>采集编号</span><span class="red-star">*</span>
                    </template>
                    <template slot-scope="scope">
                        {{scope.row.gatherNo}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="areaStr"
                    label="区域"
                    align="left"
                    min-width="100"
                >
                     <template
                        slot="header"
                    >
                        <span>区域</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="bayStr"
                    label="跨间"
                    align="left"
                    min-width="100"
                >
                 <template
                        slot="header"
                    >
                        <span>跨间</span><span class="red-star">*</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="ipPath"
                    label="IP地址"
                    align="left"
                    min-width="120"
                >

                </el-table-column>
                <el-table-column
                    prop="modelStr"
                    label="设备型号"
                    align="left"
                    min-width="120"
                >
                     <template
                        slot="header"
                    >
                        <span>设备型号</span><span class="red-star">*</span>
                    </template>
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
            width="600px"
            :close-on-click-modal="false"
        >
            <el-form
                ref="ruleForm"
                :model="ruleForm"
                :rules="rules"
                label-width="150px"
                class="demo-ruleForm"
            >
                <el-form-item
                    label="设备编号"
                    prop="machineNo"
                >
                    <el-input
                        v-model="ruleForm.machineNo"
                        style="width:250px"
                    />
                </el-form-item>
                <el-form-item
                    label="设备类型"
                    prop="type"
                >
                    <el-select
                        v-model="ruleForm.type"
                        placeholder="请选择"
                        style="width:250px"
                    >
                        <el-option
                            v-for="item in typeArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="入厂时间"
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
                <el-form-item
                    label="生产厂商"
                    prop="firm"
                >
                    <el-select
                        v-model="ruleForm.firm"
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
                    prop="model"
                >
                    <el-select
                        v-model="ruleForm.model"
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
                    label="采集编号"
                    prop="gid"
                >
                    <el-select
                        v-model="ruleForm.gid"
                        multiple
                        :multiple-limit="1"
                        filterable
                        placeholder="请选择"
                        style="width:250px"
                    >
                        <el-option
                            v-for="item in gatherNos"
                            :key="item.id"
                            :label="item.gatherNo"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
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
                <el-form-item
                    label="IP地址"
                    prop="ipPath"
                >
                    <el-input
                        v-model="ruleForm.ipPath"
                        style="width:250px"
                    />
                </el-form-item>
                <el-form-item
                    label="状态"
                    prop="status"
                >
                    <el-radio-group v-model="ruleForm.status">
                        <el-radio
                            :label="item.id"
                            v-for="item in statusArr"
                            :key="item.id"
                        >{{item.valueName}}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item
                    label="是否联网"
                    prop="isNetwork"
                >
                    <el-radio-group v-model="ruleForm.isNetwork">
                        <el-radio :label="0">是</el-radio>
                        <el-radio :label="1">否</el-radio>
                    </el-radio-group>
                </el-form-item>

                <el-form-item>
                    <el-button
                        type="primary"
                        @click="submitForm('ruleForm')"
                    >保存</el-button>
                    <el-button @click="visable1 = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <area-bind ref="areaBind"></area-bind>
        <equipment-bind ref="equipmentBind"></equipment-bind>
        <area-org-bind ref="areaBind2"></area-org-bind>
    </div>
</template>

<script>
import { getWelderList, getWelderDetail, delWelder, exportWelderExcel, getAllGatherNos, addWelder, editWelder, getWeldingModel, findByIdArea } from '_api/productionEquipment/production'
import { getTeam, getDictionaries } from '_api/productionProcess/process'
import { getToken } from '@/utils/auth'
import areaBind from './components/areaBind.vue'
import EquipmentBind from './components/equipmentBind.vue'
import AreaOrgBind from './components/areaOrgBind.vue'
export default {
    components: { areaBind, EquipmentBind, AreaOrgBind },
    name: 'product-equip-manage',
    data () {
        return {
            //搜索条件
            searchObj: {
                machineNo: '',
                type: '',
                grade: '',
                status: '',
                firm: '',
                isNetwork: '',
                gatherNo: '',
                ipPath: '',
                model: ''
            },
            //搜索条件设备型号
            modelArr2: [],
            list: [],
            //分页
            page: 1,
            total: 0,
            pageSize:10,

            visable1: false,
            ruleFormObj: {
            },
            ruleForm: {
                id: '',
                machineNo: '',//编号
                status: 17,//状态
                type: '',//设备类型
                firm: '',//厂商
                model: '',//设备型号
                deptId: '',//项目（机构id）
                isNetwork: 0,//是否联网
                gid: [],//采集id
                protocol: '',//通讯协议
                createTime: '',//入厂时间
                ipPath: '',//ip
                area: '',//区域
                bay: ''//跨间
            },
            rules: {
                machineNo: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                type: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                deptId: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                firm: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                gid: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                model: [
                    { required: true, message: '不能为空', trigger: 'change' }
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
            loading: false
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
            let { data, code } = await getDictionaries({ "types": ["3", "4", "5", "6", "16", "17"] });
            if (code == 200) {
                this.statusArr = data['4'] || [];
                this.manufactorArr = data['5'] || [];
                this.typeArr = data['3'] || [];
                this.areaArr = data['16'] || [];
                this.straddleArr2 = data['17'] || [];
                this.modelArr2 = data['6'] || [];
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
                size:this.pageSize,
                ...this.searchObj
            }
            req.grade = this.searchObj.grade && this.searchObj.grade.length > 0 ? this.searchObj.grade.slice(-1).join('') : ''
            this.loading = true;
            let { data, code } = await getWelderList(req);
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
            let { data, code } = await getWelderDetail(id);
            if (code == 200) {
                this.visable1 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm.resetFields();
                    this.ruleForm = data[0] || {};
                    this.ruleForm.gid = this.ruleForm.gid.split(',').map(Number);
                    this.gatherNos.unshift({ id: parseInt(this.ruleForm.gid), gatherNo: this.ruleForm.machineGatherInfo.gatherNo });
                    if (this.ruleForm.firm) {
                        this.changeFirm(this.ruleForm.firm);
                    }
                    if (this.ruleForm.area) {
                        this.changeArea(this.ruleForm.area);
                    }
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
                let { code, data } = await delWelder({ id })
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
                        req.gid = req.gid.join(',');
                        const { data, code } = await editWelder(req)
                        if (code == 200) {
                            this.$message.success('修改成功')
                            this.visable1 = false
                            this.getList()
                        }
                    } else {
                        const req = { ...this.ruleForm }
                        req.deptId = req.deptId && req.deptId.length > 0 ? req.deptId.slice(-1).join('') : req.deptId
                        req.gid = req.gid.join(',');
                        const { data, code } = await addWelder(req);
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

        bindAreaFun2 () {
            this.$refs.areaBind2.init();
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
