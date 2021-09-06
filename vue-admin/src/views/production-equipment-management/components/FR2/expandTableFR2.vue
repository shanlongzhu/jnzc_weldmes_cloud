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
            >
                <template #default={row}>
                    {{getChanneName(row.channel)}}
                </template>
            </vxe-table-column>
            <vxe-table-column
                field="channelFlag"
                title="通道标志"
                min-width="70"
            >
            </vxe-table-column>
            <vxe-table-column
                field="initialEleMin"
                title="控制参数"
                min-width="100"
            >
                <template #default={row}>
                    {{row.command==1?'查询':row.command==2?'下载':'删除'}}
                </template>
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
                field="initialEleMax"
                title="初期电流上限"
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
                field="initialEleMin"
                title="初期电流下限"
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
                field="arcEleMax"
                title="收弧电流上限"
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
                field="arcEleMin"
                title="收弧电流下限"
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
                field="texture"
                title="材质"
                min-width="100"
            >
            </vxe-table-column>
            <vxe-table-column
                field="wireDiameter"
                title="丝径"
                min-width="100"
            >
                <template #default={row}>
                    {{getWireDiameterArrFun(row.wireDiameter)}}
                </template>
            </vxe-table-column>
            <vxe-table-column
                field="gases"
                title="气体"
                min-width="100"
            >
                <template #default={row}>
                    {{getGasesArrFun(row.gases)}}
                </template>

            </vxe-table-column>
            <vxe-table-column
                field="weldingControl"
                title="焊接控制"
                min-width="100"
            >
            </vxe-table-column>
            <vxe-table-column
                field="pulseHaveNot"
                title="脉冲有无"
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
import { getSxFR2TechList, delSxFR2TechDetail } from '_api/productionProcess/process'
import { getChannelNoSourceArr, getWireDiameterArr, getGasesArr } from './common'

export default {
    props: {
        id: ''
    },
    data () {
        return {
            page: 1,
            total: 0,
            pageSize: 10,
            list: [],
            loading: false
        }
    },
    mounted () {
        this.getExpandDetail()
    },
    methods: {
        //获取展开行详细数据
        async getExpandDetail () {
            this.loading = true;
            let req = {
                wpsLibraryId: this.id,
                pn: this.page,
                size: this.pageSize
            }
            let { data, code } = await getSxFR2TechList(req);
            this.loading = false;
            if (code == 200) {
                this.list = data.list || [];
                this.total = data.total || 0;
            }
        },

        //修改
        editFun (id) {
            let objData = {
                id: id,
                parentId: this.id
            }
            this.$emit('editDetail', objData);
        },

        delFun (id) {
            this.$confirm('确定要删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let { data, code } = await delSxFR2TechDetail({ id });
                if (code == 200) {
                    this.$message.success('操作成功')
                    this.$emit('reload')
                }
            }).catch(() => { })

        },

        handleCurrentChange (p) {
            this.page = p;
            this.getExpandDetail();
        },
        handleSizeChange (s) {
            this.pageSize = s;
            this.getExpandDetail();
        },
        //通道
        getChanneName (v) {
            return getChannelNoSourceArr(v)
        },
        //丝径
        getWireDiameterArrFun (v) {
            return getWireDiameterArr(v)
        },
        //气体
        getGasesArrFun (v) {
            return getGasesArr(v);
        }
    }
}
</script>

