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
            <el-table
                id="out-table"
                :data="list"
                stripe
                style="width: 100%"
                align="center"
                border
                v-loading="loading"
                size="mini"
                height='100%'
                :header-cell-style="{background:'#eef1f6',color:'#606266'}"
                @expand-change="expandChange"
            >
                <el-table-column
                    type="expand"
                    fixed="left"
                >
                    <template slot-scope="props">
                        <expand-table
                            :ref="'expandTable'+props.row.id"
                            :childTable="props.row.childTable"
                            @throwData="getDetailTable"
                            @editDetail="editDetailFun"
                        ></expand-table>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="wpsName"
                    label="工艺名称"
                    align="left"
                    min-width="200"
                />
                <el-table-column
                    prop="weldModel"
                    label="焊机型号"
                    align="left"
                    min-width="200"
                >
                    <template slot-scope="scope">
                        {{scope.row.sysDictionary.valueName}}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="createTime"
                    label="创建日期"
                    align="left"
                    min-width="160"
                />
                <el-table-column
                    label="操作"
                    align="left"
                    class-name="small-padding fixed-width"
                    width="330"
                >
                    <template slot-scope="scope">
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
                            @click="addFun"
                        >
                            新增工艺
                        </el-button>
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
            width="1000px"
            class="procces-wrap"
        >
            <el-form
                ref="ruleForm"
                :model="ruleForm"
                :rules="rules"
                label-width="100px"
                class="demo-ruleForm"
            >
                <el-row>
                    <el-col>
                        <el-form-item
                            label="通道号"
                            prop="firmId"
                        >
                            <el-select
                                v-model="ruleForm.firmId"
                                placeholder="请选择"
                                style="width:100px"
                                size="mini"
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
                    </el-col>
                </el-row>
                <div style="border:1px solid #aaa" class="pt10 pb10 mt10">
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="收弧"
                                prop="firmId"
                            >
                                <el-select
                                    v-model="ruleForm.firmId"
                                    placeholder="请选择"
                                    style="width:120px"
                                    size="mini"
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
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="一元/个别"
                                prop="firmId"
                            >
                                <el-select
                                    v-model="ruleForm.firmId"
                                    placeholder="请选择"
                                    style="width:120px"
                                    size="mini"
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
                        </el-col>
                        <el-col :span="12">
                            <el-form-item
                                label=""
                                label-width="40px"
                            >
                                <el-checkbox v-model="checked">初期条件</el-checkbox>
                                <el-checkbox v-model="checked">熔深控制</el-checkbox>
                                <el-checkbox v-model="checked">柔软电弧模式</el-checkbox>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="电弧特性"
                                prop="firmId"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (±1)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊丝材质"
                                prop="firmId"
                            >
                                <el-select
                                    v-model="ruleForm.firmId"
                                    placeholder="请选择"
                                    style="width:120px"
                                    size="mini"
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
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="提前送气"
                                prop="firmId"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (0.1s)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="气体"
                                prop="firmId"
                            >
                                <el-select
                                    v-model="ruleForm.firmId"
                                    placeholder="请选择"
                                    style="width:120px"
                                    size="mini"
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
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item
                                label="点焊时间"
                                prop="firmId"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (0.1s)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊丝直径"
                                prop="firmId"
                            >
                                <el-select
                                    v-model="ruleForm.firmId"
                                    placeholder="请选择"
                                    style="width:120px"
                                    size="mini"
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
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="滞后送气"
                                prop="firmId"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (0.1s)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item
                                label="焊接过程"
                                prop="firmId"
                            >
                                <el-select
                                    v-model="ruleForm.firmId"
                                    placeholder="请选择"
                                    style="width:120px"
                                    size="mini"
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
                        </el-col>
                    </el-row>
                </div>
                <div style="border:1px solid #aaa" class="pt10 pb10 mt10">
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="初期电流"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电流"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电流"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="初期电压"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (V)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电压"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (V)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电压"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (V)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电压（一元）"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (±1)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电压（一元）"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (±1)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="初期电压（一元）"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (±1)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电流微调"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电流微调"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="焊接电压微调"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (V/%)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="收弧电压微调"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (V/%)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="报警电流上限"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="报警电流下限"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (A)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item
                                label="报警电压上限"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (V/%)</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                label="报警电压下限"
                                prop="firmId"
                                label-width="120px"
                            >
                                <el-input
                                    size="mini"
                                    style="width:100px"
                                ></el-input>
                                <span> (V/%)</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>

                <el-form-item class="mt10 tc" label-width="0">
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
import { getTeam, getDictionaries, getProcesLibraryList, getProcesLibraryDetail, delProcesLibrary, editProcesLibrary, addProcesLibrary } from '_api/productionProcess/process'
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

        }
    },

    created () {
        this.getDicFun();
        this.getList();
    },
    methods: {
        //获取数据字典
        async getDicFun () {
            let { data, code } = await getDictionaries({ "types": ["5"] });
            if (code == 200) {
                this.manufactorArr = data['5'] || [];
                // this.modelArr = data['6'] || [];
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
                this.list = (data.list || []).map((item, index) => {
                    let itemObj = { ...item };
                    itemObj.childTable = {};
                    itemObj._index = index + 1;
                    return itemObj;
                })
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
        expandChange (row, expandedRows) {
            if (expandedRows.length > 0) {
                this.$nextTick(() => {
                    this.$refs[`expandTable${row.id}`].init(row);
                })
            }
        },
        //子组件回写数据
        getDetailTable (v) {
            this.list.map(item => {
                if (item.id == v.id) {
                    item.childTable = v.tableList
                }
            })
        },

        handleCurrentChange (pn) {
            this.page = pn;
            this.getList();
        },

        // 新增/编辑提交
        submitForm (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty('id')) {
                        const req = { ...this.ruleForm }
                        const { data, code } = await editProcesLibrary(req)
                        if (code == 200) {
                            this.$message.success('修改成功')
                            this.visable1 = false
                            this.getList()
                        }
                    } else {
                        const req = { ...this.ruleForm }
                        const { data, code } = await addProcesLibrary(req);
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

        //子组件调用修改
        editDetailFun(id){
            console.log(id)
        }
    }
}
</script>

<style lang="sass">
.procces-wrap .el-form-item
    margin-bottom: 0px
    *
        font-size: 12px
</style>
