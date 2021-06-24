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
          <el-table-column prop="taskNo" label="班组" align="center" width="110" />
          <el-table-column prop="gradeIdToStr" label="设备总数" align="center" width="100" />
          <el-table-column prop="deptName" label="开机设备数" align="center" width="100" />
          <el-table-column prop="welderName" label="实焊设备数" align="center" width="100" />
          <el-table-column prop="planStarttime" label="未绑定设备数" align="center" width="120" />
          <el-table-column prop="realityStarttime" label="设备利用率(%)" align="center" width="120" />
          <el-table-column prop="planEndtime" label="焊接任务数" align="center" width="120" />
          <el-table-column prop="planEnd" label="工作时间" align="center" width="120" />
          <el-table-column prop="planEn" label="焊接时间" align="center" width="120" />
          <el-table-column prop="planE" label="焊接效率(%)" align="center" width="120" />
          <el-table-column prop="plan" label="设备型号" align="center" width="120" />
          <el-table-column label="操 作" align="center" class-name="small-padding fixed-width">
            <template>
              <el-button size="mini" type="success" plain>
                导出
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
      list: [{ taskNo: '七区电焊一组', gradeIdToStr: '80', deptName: '60', welderName: '30', planStarttime: '20', realityStarttime: '75', planEndtime: '2', planEnd: '7:45:58', planEn: '4:36:58', planE: '59.4', plan: 'CPVE-500' },
        { taskNo: '七区电焊二组', gradeIdToStr: '75', deptName: '50', welderName: '45', planStarttime: '25', realityStarttime: '66.7', planEndtime: '3', planEnd: '6:25:45', planEn: '5:40:25', planE: '88.3', plan: 'CPVE-500' },
        { taskNo: '七区电焊三组', gradeIdToStr: '73', deptName: '66', welderName: '65', planStarttime: '7', realityStarttime: '98.5', planEndtime: '2', planEnd: '5:23:56', planEn: '4:58:30', planE: '92.1', plan: 'CPVE-500' },
        { taskNo: '七区电焊四组', gradeIdToStr: '80', deptName: '60', welderName: '30', planStarttime: '20', realityStarttime: '75', planEndtime: '2', planEnd: '7:45:58', planEn: '4:36:58', planE: '59.4', plan: 'CPVE-500' },
        { taskNo: '七区电焊五组', gradeIdToStr: '75', deptName: '50', welderName: '45', planStarttime: '25', realityStarttime: '66.7', planEndtime: '3', planEnd: '6:25:45', planEn: '5:40:25', planE: '88.3', plan: 'CPVE-500' },
        { taskNo: '七区电焊六组', gradeIdToStr: '73', deptName: '66', welderName: '65', planStarttime: '7', realityStarttime: '98.5', planEndtime: '2', planEnd: '5:23:56', planEn: '4:58:30', planE: '92.1', plan: 'CPVE-500' },
        { taskNo: '七区电焊七组', gradeIdToStr: '80', deptName: '60', welderName: '30', planStarttime: '20', realityStarttime: '75', planEndtime: '2', planEnd: '7:45:58', planEn: '4:36:58', planE: '59.4', plan: 'CPVE-500' },
        { taskNo: '七区电焊八组', gradeIdToStr: '73', deptName: '66', welderName: '65', planStarttime: '7', realityStarttime: '98.5', planEndtime: '2', planEnd: '5:23:56', planEn: '4:58:30', planE: '92.1', plan: 'CPVE-500' },
        { taskNo: '七区电焊九组', gradeIdToStr: '80', deptName: '60', welderName: '30', planStarttime: '20', realityStarttime: '75', planEndtime: '2', planEnd: '7:45:58', planEn: '4:36:58', planE: '59.4', plan: 'CPVE-500' },
        { taskNo: '七区电焊十组', gradeIdToStr: '75', deptName: '50', welderName: '45', planStarttime: '25', realityStarttime: '66.7', planEndtime: '3', planEnd: '6:25:45', planEn: '5:40:25', planE: '88.3', plan: 'CPVE-500' }],
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
