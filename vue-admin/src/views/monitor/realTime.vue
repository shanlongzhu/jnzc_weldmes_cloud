<template>
    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div
            class="table-con flex"
            style="flex:1; height:0px"
        >
            <div
                class="user-l"
                style='height:100%;width:300px;border:1px solid #ddd;margin-right:10px'
            >
                <div class="organizational-tit">组织机构菜单</div>
                <div style="height:calc(100% - 34px);overflow-y:auto">
                    <el-tree
                        :data="treeData"
                        ref="treeDom"
                        :expand-on-click-node="false"
                        v-loading="treeLoading"
                        :props="defaultProps"
                        default-expand-all
                        @current-change="currentChangeTree"
                        highlight-current
                        node-key="id"
                    ></el-tree>
                </div>
            </div>
            <div
                class="user-r flex-c real-tit"
                style='height:100%;flex:1; width:0px'
            >
                <div class="organizational-tit fs14">焊机实时状态监测</div>
                <div class="real-con">
                    <div class="real-con-box flex-n">
                        <div class="real-con-item flex-n" v-for="item in list" :key="item.id">
                            <span class="real-con-item-img">
                                <img src="/swipes/AT.png" />
                            </span>
                            <div class="real-con-item-txt">
                                <p><span>设备编号：</span>{{item.machineNo||'--'}}</p>
                                <p><span>任务编号：</span>{{item.machineNo||'--'}}</p>
                                <p><span>操作人员：</span>{{item.machineNo||'--'}}</p>
                                <p><span>焊接电流：</span>{{item.machineNo||'--'}}A</p>
                                <p><span>焊接电压：</span>{{item.machineNo||'--'}}V</p>
                                <p><span>焊机状态：</span><strong>{{item.statusStr||'--'}}</strong></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- <el-pagination
            class="p10"
            :current-page.sync="page"
            :page-size="10"
            align="right"
            background
            layout="total, prev, pager, next"
            :total="total"
            @current-change="handleCurrentChange"
        /> -->

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
                    label="名称"
                    prop="name"
                >
                    <el-input
                        v-model="ruleForm.name"
                        style="width:250px"
                    ></el-input>
                </el-form-item>
                             
                <el-form-item
                    label="上级部门"
                    prop="parentId"
                >
                    <el-cascader
                        v-model="ruleForm.parentId"
                        style="width:250px"
                        clearable
                        :options="teamArr"
                        :props="defalutProps"
                        :show-all-levels="false"
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
import { getTeam } from '_api/productionProcess/process'
import { getUserTree,getTreeDeptInfo,addDept,findIdDeptInfo,editDept,delDept } from '_api/system/systemApi'
import {  getModelFindId } from '_api/productionEquipment/production'
export default {
    name: 'organizational',
    data () {
        
        return {
            list: [],
            //分页
            page: 1,
            total: 0,

            //搜索条件
            searchObj: {
                id:''
            },

            visable1: false,
            ruleFormObj: {

            },
            ruleForm: {
                name: '',//机构名称
                parentId: '',//父级机构id
            },
            rules: {
                name: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],
                parentId: [
                    { required: true, message: '不能为空', trigger: 'blur' }
                ],                
            },
            title: '新建机构',

            //机构数据
            teamArr: [],

            // 级联下拉配置
            defalutProps: {
                label: 'name',
                value: 'id',
                children: 'list',
                checkStrictly: true
            },
            loading: false,

            //部门tree数据
            treeLoading:false,
            treeData: [],
            defaultProps: {
                children: 'list',
                label: 'name'
            },
            //角色下拉数据
            rolesArr: []
        }
    },

    created () {
        this.ruleFormObj = { ...this.ruleForm };
        this.getList();
        this.getUserTreeFun();
        this.getTeamList();
    },
    methods: {
        search () {
            this.page = 1;
            this.getList();
        },
        //获取部门tree
        async getUserTreeFun () {
            this.treeLoading = true;
            let { data, code } = await getUserTree();
            this.treeLoading = false;
            if (code == 200) {
                this.treeData = [data] || [];
                this.$nextTick(()=>{
                    this.$refs.treeDom.setCurrentKey(this.searchObj.id)
                })
            }
        },

        //根据部门id获取用户列表
        async getList (id) {
            let req = {
                pn: this.page,
                ...this.searchObj
            }
            this.loading = true;
            let { code, data } = await getModelFindId(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list||[];
                this.total = data.total;
            }
        },
        //新增
        addFun () {
            this.title = "新建机构"
            this.visable1 = true;
            this.$nextTick(() => {
                this.$refs.ruleForm.resetFields();
                this.ruleForm = { ...this.ruleFormObj };
                Reflect.deleteProperty(this.ruleForm, "id");
            })

        },
        //修改
        async editFun (id) {
            this.title = "编辑机构"
            this.ruleForm = { ...this.ruleFormObj };
            let { data, code } = await findIdDeptInfo({ id });
            if (code == 200) {
                this.visable1 = true;
                this.$nextTick(() => {
                    this.$refs.ruleForm.resetFields();
                    this.ruleForm = data || {}
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
                let { code, data } = await delDept({ id })
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.getList();
                    this.getUserTreeFun();
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

        // 新增/编辑提交
        submitForm (formName) {
            console.log(this.ruleForm)
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty('id')) {
                        const req = { ...this.ruleForm }
                        req.parentId = req.parentId && req.parentId.length > 0 ? req.parentId.slice(-1).join('') : req.parentId
                        const { data, code } = await editDept(req)
                        if (code == 200) {
                            this.$message.success('修改成功')
                            this.visable1 = false
                            this.getList();
                            this.getUserTreeFun();
                        }
                    } else {
                        const req = { ...this.ruleForm }
                        req.parentId = req.parentId && req.parentId.length > 0 ? req.parentId.slice(-1).join('') : req.parentId
                        const { data, code } = await addDept(req);
                        if (code == 200) {
                            this.$message.success('新增成功')
                            this.visable1 = false
                            this.getList();
                            this.getUserTreeFun();
                        }
                    }
                } else {
                    console.log('error submit!!')
                    return false
                }
            })
        },
        currentChangeTree (v) {
            this.searchObj.id = v.id;
            this.search();
        },

    }
}
</script>

<style>
.user-l * {
    font-size: 12px;
}
.organizational-tit {
    height: 34px;
    padding: 0 10px;
    line-height: 34px;
    background: #f8f8f8;
    font-weight: bold;
    border-bottom: 1px solid #ddd;
    border-left: 1px solid #ddd;
}
.real-tit{
    border-top:1px solid #ddd;
}
.real-con{
    flex:1;
    height:0;
    overflow-y:scroll;
    border:1px solid #ddd;
    border-top:none;
}
.real-con-item{
    line-height: 20px;
    font-size: 12px;
    align-items: center;
    padding: 10px;
    width: 230px;
}
.real-con-item .real-con-item-img{
    margin-right: 6px;
}
.real-con-item .real-con-item-txt p{
    margin: 0px;
}
.real-con-item .real-con-item-txt p span{
    color: #666;
}
</style>
