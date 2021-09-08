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
                    title="序号"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="channel"
                    title="通道号"
                    min-width="60"
                ></vxe-table-column>
                <vxe-table-column
                    field="channelFlag"
                    title="通道标志"
                    min-width="70"
                >
                </vxe-table-column>
                <!-- <vxe-table-column
                    field="initialEleMin"
                    title="控制参数"
                    min-width="100"
                >
                <template #default={row}>
                    {{row.command==1?'查询':row.command==2?'下载':'删除'}}
                </template> -->
                </vxe-table-column>
                <vxe-table-column
                    field="presetEleMax"
                    title="预置电流上限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="presetVolMax"
                    title="预置电压上限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="presetEleMin"
                    title="预置电流下限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="presetVolMin"
                    title="预置电压下限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="eleAlarmMax"
                    title="电流报警上限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="volAlarmMax"
                    title="电压报警上限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="eleAlarmMin"
                    title="电流报警下限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="volAlarmMin"
                    title="电压报警下限"
                    min-width="100"
                >
                </vxe-table-column>
                <vxe-table-column
                    field="alarmDelayTime"
                    title="报警延时时间"
                    min-width="100"
                >                
                </vxe-table-column>
                <vxe-table-column
                    field="alarmHaltTime"
                    title="报警停机时间"
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
import { getSxAT3TechList,delSxAT3TechDetail } from '_api/productionProcess/process'
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
            let { data, code } = await getSxAT3TechList(req);
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
                let {data,code} = await delSxAT3TechDetail({id});
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

