<template>
  <div>
    <div />
    <div>
      <el-row>
        <el-table
          id="out-table"
          :data="list"
          stripe
          style="width: 100%"
          align="center"
          border
          size="medium"
          width="80%"
          :header-cell-style="{background:'#eef1f6',color:'#606266'}"
        >
          <el-table-column prop="gradeIdToStr" label="焊 机 型 号" align="center" width="100" />
          <el-table-column prop="deptName" label="创 建 日 期" align="center" width="100" />
          <el-table-column prop="welderName" label="状 态" align="center" width="100" />
          <el-table-column label="操 作" align="center" class-name="small-padding fixed-width">
            <template>
              <el-button size="mini" type="warning" plain>
                工艺库下发
              </el-button>
              <el-button size="mini" type="success" plain>
                新增工艺
              </el-button>
              <el-button
                size="mini"
                type="primary"
                plain
              >
                修改
              </el-button>
              <el-button size="mini" type="danger" plain>
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </div>
    <div class="block">
      <el-pagination
        :current-page.sync="currentPage1"
        :page-size="10"
        align="center"
        layout="total, prev, pager, next"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { getInfo, getTaskInfos, queryCurrentPageTaskListController } from '../../api/user'
export default {
  data() {
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

  created() {
    this.getUserRoles()
  },
  methods: {
    getList() {
      getTaskInfos().then(res => {
        this.list = res.data.list
        this.total = res.data.total
      })
    },
    getUserRoles() {
      getInfo().then(res => {
        this.role = res.data.role
      })
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
    },
    handleCurrentChange(pn) {
      queryCurrentPageTaskListController(pn).then(res => {
        this.list = res.data.list
      })
    }
  }
}
</script>

<style scoped>

</style>
