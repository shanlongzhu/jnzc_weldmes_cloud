<template>
    <!-- 设备绑定 -->
        <el-dialog
            title="设备绑定"
            :visible.sync="machvisable"
            width="600px"
            :close-on-click-modal="false"
        >
            <div class="p10">
                <span>厂商：</span>
                <el-select
                    v-model="bindfirm"
                    size="small"
                    class="w120"
                    placeholder="请选择"
                    @change="changeBindFirm"
                >
                    <el-option
                        v-for="item in manufactorArr"
                        :key="item.id"
                        :label="item.valueName"
                        :value="item.id"
                    />
                </el-select>
            </div>
            <el-table
                id="out-table"
                :data="modelArr2"
                stripe
                style="width: 100%"
                align="center"
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
                    label="序号"
                    align="left"
                    min-width="100"
                    type="index"
                    fixed="left"
                />
                <el-table-column
                    prop="valueName"
                    label="设备型号"
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
import { getWeldingModel,saveModelBind} from '_api/productionEquipment/production'
export default {
    name:'areaBind',
    components: {},
    props: {},
    data () {
        return {
            loading: false,
           //设备绑定
           manufactorArr:[],
           modelArr2:[],
            machvisable: false,
            bindfirm:'',
            bindModelArr: []
        }
    },
    watch: {},
    computed: {},
    methods: {
        //获取数据字典
        async getDicFun () {
            this.loading = true;
            let { data, code } = await getDictionaries({ "types": ["5", "6"] });
            this.loading = false;
            if (code == 200) {
                this.manufactorArr = data['5'] || [];
                this.modelArr2 = data['6'] || [];
            }
        },
        init () {
            this.getDicFun();
            this.machvisable = true;
        },
        //选择的型号
        modelListChange (v) {
            this.bindModelArr = v;
        },

        //切换厂商
        async changeBindFirm (id) {
            let { data, code } = await getWeldingModel({ id });
            if (code == 200) {
                this.$refs.modeTable.clearSelection();
                this.modelArr2.forEach(item => {
                    data.forEach(v => {
                        if (item.valueName == v.valueName) {
                            this.$refs.modeTable.toggleRowSelection(item);
                        }
                    })
                })
            }
        },

        //提交绑定
        async submitBind () {
            if (!this.bindfirm) {
                return this.$message.error("请先选择厂商");
            }
            let req = {
                firmMachineInfos:this.bindModelArr.map(item => {
                    let obj = {};
                    obj.firmId = this.bindfirm;
                    obj.machineId = item.id;
                    return obj
                })
            }
            let {code} = await saveModelBind(req);
            if(code==200){
                this.$message.success("绑定成功");
                this.bindfirm = "";
                this.machvisable = false;
            }
        },

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



