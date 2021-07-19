<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div class="top-con flex-n">
            <div class="con-w">
                <span>姓名：</span>
                <el-input
                    size="small"
                    class="w100"
                    v-model="searchObj.welderName"
                ></el-input>
            </div>
            <div class="con-w">
                <span>编号：</span>
                <el-input
                    size="small"
                    class="w100"
                    v-model="searchObj.welderNo"
                ></el-input>
            </div>
            <div class="con-w">
                <span>级别：</span>
                <el-select
                    v-model="searchObj.rate"
                    placeholder="请选择"
                    clearable
                    style="width:120px"
                >
                    <el-option
                        v-for="item in levelArr"
                        :key="item.id"
                        :label="item.valueName"
                        :value="item.id"
                    />
                </el-select>
            </div>
            <div class="con-w">
                <span>资质：</span>
                <el-select
                    v-model="searchObj.talent"
                    placeholder="请选择"
                    clearable
                    style="width:120px"
                >
                    <el-option
                        v-for="item in qualiArr"
                        :key="item.id"
                        :label="item.valueName"
                        :value="item.id"
                    />
                </el-select>
            </div>

            <div class="con-w">
                <span>部门：</span>
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
                >新增</el-button>
            </div>
            <div class="con-w">
                <el-upload
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
                    size="small"
                    icon="el-icon-document-remove"
                    @click="exportExcelFun"
                >导出</el-button>
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
                v-loading='loading'
                border
                size="mini"
                height='100%'
                :header-cell-style="{background:'#eef1f6',color:'#606266'}"
            >
                <el-table-column
                    label=""
                    align="left"
                    width="50"
                    type="index"
                    fixed="left"
                />
                <el-table-column
                    prop="welderName"
                    label="姓名"
                    align="left"
                    min-width="100"
                    fixed="left"
                />
                <el-table-column
                    prop="welderNo"
                    label="编号"
                    align="left"
                    min-width="120"
                >
                </el-table-column>
                <el-table-column
                    prop="cellphone"
                    label="手机"
                    align="left"
                    min-width="120"
                >
                </el-table-column>
                <el-table-column
                    prop="rank"
                    label="级别"
                    align="left"
                    min-width="120"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDictionary.valueName}}
                    </template>
                </el-table-column>
                
                <el-table-column
                    prop="macPath"
                    label="资质"
                    align="left"
                    min-width="120"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDictionary.valueNames}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="createTime"
                    label="部门"
                    align="left"
                    min-width="170"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDept.name}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="remarks"
                    label="备注"
                    align="left"
                    min-width="170"
                />
                <el-table-column
                    label="操作"
                    align="left"
                    class-name="small-padding fixed-width"
                    width="150"
                    fixed="right"
                >
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="primary"
                            plain
                            @click="editFun(scope.row.id)"
                        >
                            修改
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            plain
                            @click="delFun(scope.row.id)"
                        >
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
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

        <!-- 新增/修改 -->
        <el-dialog
            :title="title"
            :visible.sync="visable1"
            :close-on-click-modal="false"
            width="500px"
        >
            <el-form
                ref="ruleForm"
                :model="ruleForm"
                :rules="rules"
                label-width="100px"
                class="demo-ruleForm"
            >
                <el-form-item
                    label="编号"
                    prop="welderNo"
                >
                    <el-input
                        v-model="ruleForm.welderNo"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="姓名"
                    prop="welderName"
                >
                    <el-input
                        v-model="ruleForm.welderName"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="手机"
                    prop="cellphone"
                >
                    <el-input
                        v-model="ruleForm.cellphone"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="卡号"
                    prop="welderNo"
                >
                    <el-input
                        v-model="ruleForm.welderNo"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="资质"
                    prop="certification"
                >
                    <el-select
                        v-model="ruleForm.certification"
                        placeholder="请选择"
                        style="width:250px"
                    >
                        <el-option
                            v-for="item in qualiArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="级别"
                    prop="rank"
                >
                    <el-select
                        v-model="ruleForm.rank"
                        placeholder="请选择"
                        style="width:250px"
                    >
                        <el-option
                            v-for="item in levelArr"
                            :key="item.id"
                            :label="item.valueName"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="部门"
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
                    label="备注"
                    prop="remarks"
                >
                    <el-input
                        v-model="ruleForm.remarks"
                        style="width:250px"
                    />
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
    </div>
</template>

<script>
import { getTeam, getDictionaries, getWelderPeopleList, addWelderPeople,editWelderPeople,getWelderPeopleDetail,exportWelderPeopleExcel } from '_api/productionProcess/process'
import { getToken } from '@/utils/auth'
export default {
    name: 'welderManagement',
    data () {
        let validatorPhone = function (rule, value, callback) {
            if (value === '') {
                callback()
            } else if (!/^1\d{10}$/.test(value)) {
                callback(new Error('手机号格式错误'))
            } else {
                callback()
            }
        }
        return {
            list: [],
            //分页
            page: 1,
            total: 0,

            //搜索条件
            searchObj:{
                welderName:'',
                welderNo:'',
                rate:'',
                talent:'',
                grade:''
            },

            visable1: false,
            ruleFormObj: {                
            },
            ruleForm: {
                welderNo: '',//编号
                welderName: '',//姓名
                deptId: '',//项目（机构id）
                cellphone: '',//手机号
                rank: '',//级别
                certification: '',//资质
                remarks: ''//备注
            },
            rules: {
                welderNo: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                deptId: [
                    { required: true, message: '不能为空', trigger: 'change' }
                ],
                cellphone: [
                    { validator: validatorPhone, trigger: 'blur' }
                ],
                welderName: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ]
            },
            title: '新建焊工',

            //机构数据
            teamArr: [],
            //级别下拉数据
            levelArr: [],
            //资质下拉数据
            qualiArr: [],
            // 级联下拉配置
            defalutProps: {
                label: 'name',
                value: 'id',
                children: 'list'
            },
            loading: false,
            importUrl: `${process.env.VUE_APP_BASE_API}/solderer/importExcel`,
            headers: {
                'Authorization': getToken()
            }
        }
    },

    created () {
        this.ruleFormObj = {...this.ruleForm};
        // 获取班组
        if (this.teamArr.length == 0) {
            this.getTeamList()
        }
        this.getList();
        this.getDicFun();
    },
    methods: {
        //获取数据字典
        async getDicFun () {
            let { data, code } = await getDictionaries({ "types": ["13", "14"] });
            if (code == 200) {
                this.levelArr = data['13'] || [];
                this.qualiArr = data['14'] || [];
            }
        },
        search () {
            this.page = 1;
            this.getList();
        },
        async getList () {
            let req = {
                pn: this.page,
                ...this.searchObj
            }
            req.grade = this.searchObj.grade && this.searchObj.grade.length > 0 ? this.searchObj.grade.slice(-1).join('') : ''
            this.loading = true;
            let { data, code } = await getWelderPeopleList(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list
                this.total = data.total
            }
        },
        //新增
        addFun () {
            this.title = "新建焊工"
            this.visable1 = true;
            this.$nextTick(() => {
                this.$refs.ruleForm.resetFields();
                this.ruleForm = { ...this.ruleFormObj };
                Reflect.deleteProperty(this.ruleForm, "id");
            })

        },
        //修改
        async editFun (id) {
            this.title = "修改焊工"
            this.ruleForm = { ...this.ruleFormObj };
            let { data, code } = await getWelderPeopleDetail(id);
            if (code == 200) {
                this.visable1 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm.resetFields();
                    this.ruleForm = data[0] || {}
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
                let { code, data } = await delEqu({ id })
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
            location.href = exportWelderPeopleExcel(req)
        },
        handleAvatarSuccess (res, file) {
            if (res.code == 200) {
                this.$message.success("导入成功");
                this.search();
            }
        },
        // 新增/编辑提交
        submitForm (formName) {
            console.log(this.ruleForm)
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty('id')) {
                        const req = { ...this.ruleForm }
                        req.deptId = req.deptId && req.deptId.length > 0 ? req.deptId.slice(-1).join('') : req.deptId
                        const { data, code } = await editWelderPeople(req)
                        if (code == 200) {
                            this.$message.success('修改成功')
                            this.visable1 = false
                            this.getList()
                        }
                    } else {
                        const req = { ...this.ruleForm }
                        req.deptId = req.deptId && req.deptId.length > 0 ? req.deptId.slice(-1).join('') : req.deptId
                        const { data, code } = await addWelderPeople(req);
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

<style scoped>
</style>


