<template>
    <div>
        <vxe-table
                border
                :data="list"
                :loading="loading"
                size="mini"
                max-height="200px"
            >
                <vxe-table-column
                    type="seq"
                    width="50"
                    title="序号"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="channelNo"
                    title="通道号"
                    min-width="60"
                ></vxe-table-column>
                <vxe-table-column
                    field="initialCondition"
                    title="初期条件"
                    min-width="70"
                >
                    <template #default="{row}">
                        {{row.initialCondition?'是':'否'}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="fusionControl"
                    title="熔深控制"
                    min-width="70"
                >
                <template #default="{row}">
                        {{row.fusionControl?'是':'否'}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="unitarySeveral"
                    title="一元/个别"
                    min-width="80"
                >
                <template #default="{row}">
                        {{row.unitarySeveral?'个别':'一元'}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="controlArc"
                    title="收弧"
                    min-width="70"
                >
                <template #default="{row}">
                         {{row.sysDictionary.valueName}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="arcCharacter"
                    title="电弧特性"
                    min-width="70"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="softArcSchema"
                    title="柔软电弧模式"
                    min-width="100"
                >
                <template #default="{row}">
                          {{row.softArcSchema?'是':'否'}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="weldingStickTexture"
                    title="焊丝材质"
                    min-width="90"
                >
                <template #default="{row}">
                         {{row.sysDictionary.valueNames}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="gases"
                    title="气体"
                    min-width="60"
                >
                <template #default="{row}">
                         {{row.sysDictionary.valueNamess}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="weldingStickDiameter"
                    title="焊丝直径"
                    min-width="70"
                >
                <template #default="{row}">
                         {{row.sysDictionary.valueNamesss}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="weldingProcess"
                    title="焊接过程"
                    min-width="70"
                >
                <template #default="{row}">
                         {{row.sysDictionary.valueNamessss}}
                    </template>
                </vxe-table-column>
                <vxe-table-column
                    field="spotWeldingTime"
                    title="点焊时间"
                    min-width="70"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="inAdvanceAspirated"
                    title="提前送气"
                    min-width="70"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="hysteresisAspirated"
                    title="滞后送气"
                    min-width="70"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="initialEle"
                    title="初期电流"
                    min-width="70"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="initialVol"
                    title="初期电压"
                    min-width="70"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="initialVolUnitary"
                    title="初期电压一元"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="weldingEle"
                    title="焊接电流"
                    min-width="70"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="weldingVol"
                    title="焊接电压"
                    min-width="70"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="weldingVolUnitary"
                    title="焊接电压一元"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="arcEle"
                    title="收弧电流"
                    min-width="70"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="arcVol"
                    title="收弧电压"
                    min-width="70"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="arcVolUnitary"
                    title="收弧电压一元"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="weldingEleAdjust"
                    title="焊接电流微调"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="weldingVolAdjust"
                    title="焊接电压微调"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="arcEleAdjust"
                    title="收弧电流微调"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="arcVolAdjust"
                    title="收弧电压微调"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="alarmsEleMax"
                    title="报警电流上限"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="alarmsEleMin"
                    title="报警电流下限"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="alarmsVolMax"
                    title="报警电压上限"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="alarmsVolMin"
                    title="报警电压下限"
                    min-width="100"
                >                
                </vxe-table-column>

                <vxe-table-column
                    field="createTime"
                    title="操作"
                    width="150"
                    fixed="right"
                >
                    <template #default="{row}">
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
        <el-pagination
            class="p10"
            :current-page.sync="page"
            :page-size="10"
            align="right"
            small
            background
            layout="total, prev, pager, next"
            :total="total"
            @current-change="handleCurrentChange"
        />
    </div>
</template>

<script>
import { getProcesLibraryChild,delProcesLibraryChild } from '_api/productionProcess/process'
export default {
    props:{
        id:''
    },
    data () {
        return {
            page: 1,
            total: 0,
            list: [],
            loading: false
        }
    },
    mounted(){
        this.getExpandDetail()
    },
    methods: {
        //获取展开行详细数据
        async getExpandDetail () {
            this.loading = true;
            let req = {
                id:this.id,
                pn:this.page
            }
            let { data, code } = await getProcesLibraryChild(req);
            this.loading = false;
            if(code==200){   
                this.list = data.list||[];
                this.total = data.total||0;               
            }
        },

        //修改
        editFun(id){
            let objData = {
                id:id,
                parentId:this.id
            }
            this.$emit('editDetail',objData);
        },

        delFun(id){
            this.$confirm('确定要删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let {data,code} = await delProcesLibraryChild({id});
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.$emit('reload')
                }
            }).catch(() => { })           

        },

        handleCurrentChange(p){
            this.page = p;
            this.getExpandDetail();
        }
    }
}
</script>

