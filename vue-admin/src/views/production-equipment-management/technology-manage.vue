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
                    @click="changeAllStatus"
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
                size="mini"
                height='100%'
                :header-cell-style="{background:'#eef1f6',color:'#606266'}"
            >
                <el-table-column
                    type="expand"
                    fixed="left"
                >
                    <template slot-scope="props">
                        123123123
                    </template>
                </el-table-column>
                <el-table-column
                    prop="gradeIdToStr"
                    label="工艺名称"
                    align="left"
                    min-width="200"
                />
                <el-table-column
                    prop="deptName"
                    label="焊机型号"
                    align="left"
                    min-width="200"
                />
                <el-table-column
                    prop="welderName"
                    label="创建日期"
                    align="left"
                    min-width="160"
                />
                <el-table-column
                    prop="welderName"
                    label="状态"
                    align="left"
                    min-width="80"
                />
                <el-table-column
                    label="操作"
                    align="left"
                    class-name="small-padding fixed-width"
                    min-width="330"
                >
                    <template>
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
                        >
                            新增工艺
                        </el-button>
                        <el-button
                            size="mini"
                            type="primary"
                            plain
                        >
                            修改
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            plain
                        >
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-pagination
            class="p10"
            :current-page.sync="currentPage1"
            :page-size="10"
            align="right"
            background
            layout="total, prev, pager, next"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
    </div>
</template>

<script>
import { getInfo, getTaskInfos, queryCurrentPageTaskListController } from '../../api/user'
export default {
    data () {
        return {
            role: null,
            showButton: false,
            list: [{ taskNo: 'CPVE', gradeIdToStr: 'YD-500GR3', deptName: '2021-06-05 10:05:54', welderName: '启用' },
            { taskNo: 'test', gradeIdToStr: 'YD-500GR3', deptName: '2021-06-05 10:05:54', welderName: '启用' },
            { taskNo: 'CPVE', gradeIdToStr: 'YD-500GR3', deptName: '2021-06-05 10:05:54', welderName: '启用' },
            { taskNo: 'test1', gradeIdToStr: 'CPVE-500', deptName: '2021-06-05 10:05:54', welderName: '启用' },
            { taskNo: 'test2', gradeIdToStr: 'YD-500GR3', deptName: '2021-06-05 10:05:54', welderName: '启用' },
            { taskNo: 'test3', gradeIdToStr: 'CPVE-500', deptName: '2021-06-05 10:05:54', welderName: '启用' },
            { taskNo: 'test4', gradeIdToStr: 'YD-500GR3', deptName: '2021-06-05 10:05:54', welderName: '启用' },
            { taskNo: 'test5', gradeIdToStr: 'YD-500GR3', deptName: '2021-06-05 10:05:54', welderName: '启用' },
            { taskNo: 'test6', gradeIdToStr: 'YD-500GR3', deptName: '2021-06-05 10:05:54', welderName: '启用' },
            { taskNo: 'test7', gradeIdToStr: 'CPVE-500', deptName: '2021-06-05 10:05:54', welderName: '启用' }],
            total: 10,
            listLoading: true,
            formData: {
                taskNo: '',
                gradeIdToStr: '',
                deptName: '',
                planStarttime: '',
                planEndtime: '',
                realityStarttime: '',
                realityEndtime: '',
                evaluateContent: '',
                evaluateStarsIdToStr: ''
            }
        }
    },

    created () {
        this.getUserRoles()
    },
    methods: {
        getList () {
            getTaskInfos().then(res => {
                this.list = res.data.list
                this.total = res.data.total
            })
        },
        getUserRoles () {
            getInfo().then(res => {
                this.role = res.data.role
            })
        },
        handleSizeChange (val) {
            console.log(`每页 ${val} 条`)
        },
        handleCurrentChange (pn) {
            queryCurrentPageTaskListController(pn).then(res => {
                this.list = res.data.list
            })
        }
    }
}
</script>

<style scoped>
</style>
