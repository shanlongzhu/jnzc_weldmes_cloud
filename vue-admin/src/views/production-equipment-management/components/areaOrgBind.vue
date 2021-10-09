<template>
    <!-- 区间绑定 -->
    <el-dialog
        title="区域跨间部门绑定"
        :visible.sync="machvisable"
        width="600px"
        :close-on-click-modal="false"
    >
        <div class="p10">
            <span>区域：</span>
            <el-select
                v-model="areaId"
                size="small"
                class="w120"
                placeholder="请选择"
                @change="changeDep"
            >
                <el-option
                    v-for="item in areaArr"
                    :key="item.id"
                    :label="item.valueName"
                    :value="item.id"
                />
            </el-select>
            <span style="margin-left:20px">部门：</span>
            <el-select
                v-model="departmentId"
                size="small"
                class="w120"
                placeholder="请选择"
                @change="changeDep"
            >
                <el-option
                    v-for="item in treeData"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                />
            </el-select>
        </div>
        <el-table
            id="out-table"
            :data="straddleArr2"
            stripe
            style="width: 100%"
            align="center"
            v-loading="loading"
            border
            size="mini"
            ref="modeTable"
            height='300px'
            @selection-change="modelListChange"
            :header-cell-style="{background:'#eef1f6',color:'#606266'}"
        >
            <el-table-column
                type="selection"
                width="55"
            >
            </el-table-column>
            <el-table-column
                prop="valueName"
                label="跨间"
                align="left"
                min-width="100"
                fixed="left"
            />
        </el-table>
        <div class="tc p10">
            <el-button
                type="primary"
                @click="submitBind"
            >保存</el-button>
            <el-button @click="machvisable = false">取消</el-button>
        </div>
    </el-dialog>
</template>

<script>
import { getTeam, getDictionaries } from '_api/productionProcess/process'
import { findByIdArea, saveAreaBind } from '_api/productionEquipment/production'
import { getDeptTree, saveDeptTOAreaAndBay, getBayInfo } from '_api/system/systemApi'
export default {
    name: 'equipmentBind',
    components: {},
    props: {},
    data () {
        return {
            loading: false,
            machvisable: false,
            areaId: '',
            //跨间选中数据
            selectData: [],
            areaArr: [],
            straddleArr2: [],

            //部门Id
            departmentId: '',

            treeData: [],
        }
    },
    watch: {},
    computed: {},
    methods: {
        //获取数据字典
        async getDicFun () {
            this.loading = true;
            let { data, code } = await getDictionaries({ "types": ["16", "17"] });
            this.loading = false;
            if (code == 200) {
                this.areaArr = data['16'] || [];
                this.straddleArr2 = data['17'] || [];
            }
        },
        init () {
            this.getDicFun();
            this.areaId = '';
            this.departmentId = '';
            this.machvisable = true;
            this.getUserTreeFun();
        },

        //切换区间
        async changeArea () {
            if (this.areaId && this.departmentId) {
                let { data, code } = await getBayInfo({ deptId: this.departmentId, areaId: this.areaId });
                if (code == 200) {
                    this.$refs.modeTable.clearSelection();
                    this.straddleArr2.forEach(item => {
                        data.forEach(v => {
                            if (item.valueName == v.bay) {
                                this.$refs.modeTable.toggleRowSelection(item);
                            }
                        })
                    })
                }
            }

        },
        //切换部门
        changeDep () {
            this.$nextTick(() => {
                this.changeArea();
            })
        },

        modelListChange (v) {
            this.selectData = v;
        },

        async submitBind () {
            if (!this.areaId) {
                return this.$message.error("请先选择区域");
            }
            if (!this.departmentId) {
                return this.$message.error("请选择绑定部门");
            }
            if (this.selectData.length == 0) {
                return this.$message.error("请选择跨间");
            }

            let req = {
                "deptId": this.departmentId,
                "areaId": this.areaId,
                "bayIds": this.selectData.map(item => item.id)
            }
            let { code } = await saveDeptTOAreaAndBay(req);
            if (code == 200) {
                this.$message.success("绑定成功");
                this.areaId = "";
                this.departmentId = "";
                this.machvisable = false;
            }
        },

        //获取部门tree
        async getUserTreeFun (id) {
            this.treeLoading = true;
            let { data, code } = await getDeptTree();
            this.treeLoading = false;
            if (code == 200) {
                this.treeData = data || [];
                this.$nextTick(() => {
                    // this.$refs.treeDom.setCurrentKey(id || this.treeData[0].id);                    
                })
            }

        },
        currentChangeTree (v) {
            this.$emit('currentChangeTree', v);
        }

    },
    created () {

    },
    mounted () { }
}
</script>
<style lang="scss" scoped>
.wrapper {
}
</style>

