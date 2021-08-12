<template>
    <!-- 区间绑定 -->
    <el-dialog
        title="区间绑定"
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
                @change="changeArea"
            >
                <el-option
                    v-for="item in areaArr"
                    :key="item.id"
                    :label="item.valueName"
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
                label="序号"
                align="left"
                min-width="100"
                type="index"
                fixed="left"
            />
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
import {findByIdArea,saveAreaBind} from '_api/productionEquipment/production'
export default {
    name:'equipmentBind',
    components: {},
    props: {},
    data () {
        return {
            loading: false,
            machvisable: false,
            areaId: '',
            selectData:[],
            areaArr:[],
            straddleArr2:[]
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
            this.machvisable = true;
        },

        //切换区间
        async changeArea (id) {
            let { data, code } = await findByIdArea({ id });
            if (code == 200) {
                this.$refs.modeTable.clearSelection();
                this.straddleArr2.forEach(item => {
                    data.forEach(v => {
                        if (item.valueName == v.valueName) {
                            this.$refs.modeTable.toggleRowSelection(item);
                        }
                    })
                })
            }
        },

        modelListChange (v) {
            this.selectData = v;
        },

        async submitBind(){
            if (!this.areaId) {
                return this.$message.error("请先选择区域");
            }
            let req = {
                areaBayInfos:this.selectData.length>0?this.selectData.map(item => {
                    let obj = {}
                    obj.areaId = this.areaId;
                    obj.bayId = item.id;
                    return obj
                }):[{areaId:this.areaId}]
            }
            let {code} = await saveAreaBind(req);
            if(code==200){
                this.$message.success("绑定成功");
                this.areaId = "";
                this.machvisable = false;
            }
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

