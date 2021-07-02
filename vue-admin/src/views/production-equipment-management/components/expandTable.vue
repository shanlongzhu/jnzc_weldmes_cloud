<template>
    <div>
        <el-table
            :data="childTable.list"
            stripe
            style="width: 100%"
            align="center"
            v-loading='loading'
            border
            size="mini"
            height='120px'
            :header-cell-style="{background:'#eef1f6',color:'#606266'}"
        >
            <el-table-column
                label="序号"
                align="left"
                width="50"
                type="index"
            />
            <el-table-column
                prop="channelNo"
                label="通道号"
                align="left"
                min-width="100"
            />
            <el-table-column
                prop="initialCondition"
                label="初期条件"
                align="left"
                min-width="120"
            >
                <template slot-scope="scope">
                    {{scope.row.initialCondition?'是':'否'}}
                </template>
            </el-table-column>
            <el-table-column
                prop="fusionControl"
                label="熔深控制"
                align="left"
                min-width="120"
            >
            <template slot-scope="scope">
                    {{scope.row.fusionControl?'是':'否'}}
                </template>
            </el-table-column>
            <el-table-column
                prop="unitarySeveral"
                label="一元/个别"
                align="left"
                min-width="120"
            >
                <template slot-scope="scope">
                    {{scope.row.unitarySeveral?'个别':'一元'}}
                </template>
            </el-table-column>
            <el-table-column
                prop="controlArc"
                label="收弧"
                align="left"
                min-width="120"
            />
            <el-table-column
                prop="arcCharacter"
                label="电弧特性"
                align="left"
                min-width="120"
            >
                <template slot-scope="scope">
                    {{}}
                </template>
            </el-table-column>
            <el-table-column
                prop="softArcSchema"
                label="柔软电弧模式"
                align="left"
                min-width="170"
            >
                <template slot-scope="scope">
                    {{}}
                </template>
            </el-table-column>
            <el-table-column
                prop="weldingStickTexture"
                label="焊丝材质"
                align="left"
                min-width="170"
            >
            <template slot-scope="scope">
                {{scope.row.sysDictionary.valueNames}}
            </template>
            </el-table-column>
            <el-table-column
                prop="gases"
                label="气体"
                align="left"
                min-width="170"
            >
            <template slot-scope="scope">
                {{scope.row.sysDictionary.valueNamess}}
            </template>
            </el-table-column>
            <el-table-column
                prop="weldingStickDiameter"
                label="焊丝直径"
                align="left"
                min-width="170"
            >
            <template slot-scope="scope">
                {{scope.row.sysDictionary.valueNamesss}}
            </template>
            </el-table-column>
            <el-table-column
                prop="weldingProcess"
                label="焊接过程"
                align="left"
                min-width="170"
            >
            <template slot-scope="scope">
                {{scope.row.sysDictionary.valueNamessss}}
            </template>
            </el-table-column>
            <el-table-column
                prop="spotWeldingTime"
                label="点焊时间"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="inAdvanceAspirated"
                label="提前送气"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="hysteresisAspirated"
                label="滞后送气"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="initialEle"
                label="初期电流"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="initialVol"
                label="初期电压"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="initialVolUnitary"
                label="初期电压一元"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="weldingEle"
                label="焊接电流"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="weldingVol"
                label="焊接电压"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="weldingVolUnitary"
                label="焊接电压一元"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="arcEle"
                label="收弧电流"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="arcVol"
                label="收弧电压"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="arcVolUnitary"
                label="收弧电压一元"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="weldingEleAdjust"
                label="焊接电流微调"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="weldingVolAdjust"
                label="焊接电压微调"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="arcEleAdjust"
                label="收弧电流微调"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="arcVolAdjust"
                label="收弧电压微调"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="alarmsEleMax"
                label="报警电流上限"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="alarmsEleMin"
                label="报警电流下限"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="alarmsVolMax"
                label="报警电压上限"
                align="left"
                min-width="170"
            />
            <el-table-column
                prop="alarmsVolMin"
                label="报警电压下限"
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
        <el-pagination
            class="p10"
            :current-page.sync="page"
            :page-size="10"
            align="right"
            small
            background
            layout="total, prev, pager, next"
            :total="childTable.total"
            @current-change="handleCurrentChange"
        />
    </div>
</template>

<script>
import { getProcesLibraryChild } from '_api/productionProcess/process'
export default {
    props:{
        childTable:{}
    },
    data () {
        return {
            id:'',
            page: 1,
            total: 0,
            list: [],
            loading: false
        }
    },
    methods: {
        init (row) {
            this.getExpandDetail(row.id);
            this.id = row.id;
        },
        //获取展开行详细数据
        async getExpandDetail (id) {
            this.loading = true;
            let { data, code } = await getProcesLibraryChild({ id});
            this.loading = false;
            if(code==200){   
                let objData = {
                    id:id,
                    tableList : data
                }
                this.$emit('throwData',objData);          
                
            }
        },

        //修改
        editFun(id){
            this.$emit('editDetail',id);
        },


        handleCurrentChange(p){
            this.page = p;
        }
    }
}
</script>

