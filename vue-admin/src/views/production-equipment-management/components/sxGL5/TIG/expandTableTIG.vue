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
                    width="70"
                    title="序号TIG"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="channelNo"
                    title="通道号"
                    min-width="60"
                ></vxe-table-column>
                <vxe-table-column
                    field="initialEleMax"
                    title="初期电流上限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="initialEleMin"
                    title="初期电流下限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="initialVolMax"
                    title="初期电压上限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="initialVolMin"
                    title="初期电压下限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="firstWeldEleMax"
                    title="第一焊接电流上限"
                    min-width="120"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="firstWeldEleMin"
                    title="第一焊接电流下限"
                    min-width="120"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="firstWeldVolMax"
                    title="第一焊接电压上限"
                    min-width="120"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="firstWeldVolMin"
                    title="第一焊接电压下限"
                    min-width="120"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="secondWeldEleMax"
                    title="第二焊接电流上限"
                    min-width="120"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="secondWeldEleMin"
                    title="第二焊接电流下限"
                    min-width="120"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="secondWeldVolMax"
                    title="第二焊接电压上限"
                    min-width="120"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="secondWeldVolMin"
                    title="第二焊接电压下限"
                    min-width="120"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="arcEleMax"
                    title="收弧电流上限"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="arcEleMin"
                    title="收弧电流下限"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="arcVolMax"
                    title="收弧电压上限"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="arcVolMin"
                    title="收弧电压下限"
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
                        v-has="'editGY'"
                    >
                        修改
                    </el-button>
                    <el-button
                        size="mini"
                        type="danger"
                        plain
                        @click="delFun(row.id)"
                        v-has="'delGY'"
                    >
                        删除
                    </el-button>
                    </template>
                </vxe-table-column>
            </vxe-table>        
        <el-pagination
            class="p10"
            :current-page.sync="page"
            :page-size="pageSize"
            align="right"
            small
            background
            :page-sizes="[10, 50, 100, 150, 200]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            :total="total"
            @current-change="handleCurrentChange"
        />
    </div>
</template>

<script>
import { getSxTIGTechList,delSxTIGTechDetail } from '_api/productionProcess/process'
export default {
    props:{
        id:''
    },
    data () {
        return {
            page: 1,
            total: 0,
            pageSize:10,
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
                wpsLibraryId:this.id,
                pn:this.page,
                size:this.pageSize
            }
            let { data, code } = await getSxTIGTechList(req);
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
                let {data,code} = await delSxTIGTechDetail({id});
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.$emit('reload')
                }
            }).catch(() => { })           

        },

        handleCurrentChange(p){
            this.page = p;
            this.getExpandDetail();
        },
        handleSizeChange (s) {
            this.pageSize = s;
            this.getExpandDetail();
        },
    }
}
</script>

