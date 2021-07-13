<template>

    <div
        style="height:calc(100vh - 86px)"
        class="flex-c"
    >
        <div
            class="buttons"
            style="border-bottom:1px solid #ddd;padding:10px"
        >
            <el-button
                size="small"
                @click="append"
            >添加顶级菜单</el-button>
        </div>
        <div class="flex tree-top-wrap">
            <span style="padding-left:30px">菜单名称</span>
            <span class="flex">
                <span style="width:200px">标识</span>
                <span style="padding-right:30px">操作</span>
            </span>
        </div>
        <div
            class="table-con"
            style="flex:1;height:0px; overflow-y:auto"
            v-loading="loading"
        >
            <el-tree
                :data="data"
                node-key="id"
                ref="tree"
                :props="defaultProps"
                default-expand-all
                :expand-on-click-node="false"
            >
                <span
                    class="custom-tree-node"
                    slot-scope="{ node, data }"
                >
                    <span>{{ node.label }}</span>
                    <span>
                        <span class="span-mark">{{ data.mark}}</span>
                        <el-button
                            type="text"
                            size="mini"
                            @click="() => editFun(data,node)"
                        >
                            <i
                                style="font-size:16px;"
                                class="el-icon-edit"
                            ></i>
                        </el-button>
                        <el-button
                            type="text"
                            size="mini"
                            v-show="data.type=='1'"
                            @click="() => append(data,node)"
                        >
                            <i
                                style="font-size:16px;"
                                class="el-icon-plus"
                            ></i>
                        </el-button>
                        <el-button
                            type="text"
                            size="mini"
                            @click="() => remove(node, data)"
                        >
                            <i
                                style="font-size:16px;color:red"
                                class="el-icon-delete"
                            ></i>
                        </el-button>
                    </span>
                </span>
            </el-tree>            
        </div>
        <el-dialog
            :title="title"
            :visible.sync="sourceVisible"
            :close-on-click-modal="false"
        >
            <el-form
                :model="ruleForm"
                :rules="rules"
                ref="ruleForm"
                label-width="100px"
                class="demo-ruleForm"
            >
                <el-form-item
                    label="上级目录"
                    prop="parentName"
                >
                    <el-input
                        disabled
                        v-model="ruleForm.parentName"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="资源类型"
                    prop="type"
                >
                    <el-select
                        v-model="ruleForm.type"
                        placeholder="请选择"
                    >
                        <el-option
                            label="菜单"
                            value="1"
                        ></el-option>
                        <el-option
                            label="按钮"
                            value="2"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="名称"
                    prop="label"
                >
                    <el-input v-model="ruleForm.label"></el-input>
                </el-form-item>
                <el-form-item
                    label="权限标识"
                    prop="mark"
                >
                    <el-input v-model="ruleForm.mark"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button
                        type="primary"
                        @click="submitForm('ruleForm')"
                    >保存</el-button>
                    <el-button @click="sourceVisible=false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>

import { getMenuList, addMenu, editMenu, getMenuDetail,delMenu } from '_api/system/systemApi'
export default {
    name:'menusManager',
    data () {
        return {
            defaultProps: {
                children: 'menus'
            },

            data: [],
            loading: false,
            //层标题
            title: '',
            //树形新增点击数据
            hanldObj: {},
            //model资源编辑层
            sourceVisible: false,
            //form
            objRuleForm: {

            },
            ruleForm: {
                parentId: '',//父级id
                parentName: '',//父级名称
                type: '1',//新增资源类型
                label: '',//新增资源名称
                mark: ''//权限标识
            },
            //
            rules: {
                label: [
                    { required: true, message: '名称不能为空', trigger: 'blur' }
                ],
                mark: [
                    { required: true, message: '表示不能为空', trigger: 'blur' }
                ],
            }
        }
    },

    created () {
        this.objRuleForm = { ...this.ruleForm };
        this.getList();
    },
    methods: {

        //获取资源列表
        async getList () {
            this.loading = true;
            let { code, data } = await getMenuList();
            this.loading = false;
            if (code == 200) {
                this.data = data || [];
            }
        },

        append (data) {
            this.title = '新增资源';
            this.hanldObj = { ...data };
            this.ruleForm = { ...this.objRuleForm };
            this.ruleForm.parentName = data.label || "顶级";
            this.ruleForm.parentId = data.id || 0;
            this.sourceVisible = true;
        },

        editFun (data) {
            this.title = '编辑资源';
            this.findIdDetail(data.id);
        },
        //根据id获取资源明细
        async findIdDetail (id) {
            let { data, code } = await getMenuDetail({ id });
            if (code == 200) {
                this.sourceVisible = true;
                this.ruleForm = data || {};
            }
        },

        remove (node, data) {
            const id = data.id||"";
            this.$confirm('确定要删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let {code} = await delMenu({id});
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.getList()
                }
            }).catch(() => { })
        },        

        submitForm (formName) {
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    if (this.ruleForm.hasOwnProperty("id")) {
                        let { data, code } = await editMenu({ ...this.ruleForm });
                        if (code == 200) {
                            this.$message.success("修改成功");
                            this.sourceVisible = false;
                            this.getList();
                        }
                    } else {
                        let { data, code } = await addMenu({ ...this.ruleForm });
                        if (code == 200) {
                            this.$message.success("保存成功");
                            this.sourceVisible = false;
                            this.getList();
                        }
                    }
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        }
    }
}
</script>

<style>
.custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
}

.span-mark {
    display: inline-block;
    width: 200px;
}

.tree-top-wrap{
    justify-content: space-between;background:#f8f8f8;
    line-height:36px;
    border-bottom:1px solid #ddd
}
</style>
