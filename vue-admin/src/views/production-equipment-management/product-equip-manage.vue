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
          <el-table-column prop="taskNo" label="固 定 资 产 编 号" align="center" width="100" />
          <el-table-column prop="gradeIdToStr" label="设 备 类 型" align="center" width="100" />
          <el-table-column prop="deptName" label="入 厂 时 间" align="center" width="100" />
          <el-table-column prop="welderName" label="所 属 项 目" align="center" width="100" />
          <el-table-column prop="planStarttime" label="状 态" align="center" width="120" />
          <el-table-column prop="realityStarttime" label="厂 家" align="center" width="120" />
          <el-table-column prop="planEndtime" label="是 否 在 网" align="center" width="120" />
          <el-table-column prop="planEnd" label="采 集 序 号" align="center" width="120" />
          <el-table-column prop="planEn" label="位 置" align="center" width="120" />
          <el-table-column prop="planE" label="ip 地 址" align="center" width="120" />
          <el-table-column prop="plan" label="设备型号" align="center" width="120" />
          <el-table-column label="操 作" align="center" class-name="small-padding fixed-width">
            <template>
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
              <el-button size="mini" type="success" plain>
                维修记录
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
      list: [{ taskNo: '0001', gradeIdToStr: '气保焊', deptName: '2021-05-29 11:46:58', welderName: '七区电焊一组', planStarttime: '启用', realityStarttime: 'OTC', planEndtime: '是', planEnd: '0001', planEn: '上海', planE: 'www.baidu.com', plan: 'CPVE-500' },
        { taskNo: '0002', gradeIdToStr: '气保焊', deptName: '2021-05-29 11:36:58', welderName: '七区电焊一组', planStarttime: '启用', realityStarttime: 'OTC', planEndtime: '是', planEnd: '0011', planEn: '上海', planE: 'www.baidu.com', plan: 'CPVE-500' },
        { taskNo: '0003', gradeIdToStr: '气保焊', deptName: '2021-05-29 11:26:58', welderName: '七区电焊二组', planStarttime: '启用', realityStarttime: 'OTC', planEndtime: '是', planEnd: '0002', planEn: '上海', planE: 'www.baidu.com', plan: 'YD-500GR3' },
        { taskNo: '0004', gradeIdToStr: '气保焊', deptName: '2021-05-29 11:16:58', welderName: '七区电焊三组', planStarttime: '启用', realityStarttime: 'OTC', planEndtime: '是', planEnd: '0003', planEn: '上海', planE: 'www.baidu.com', plan: 'YD-500GR3' },
        { taskNo: '0005', gradeIdToStr: '气保焊', deptName: '2021-05-29 11:06:58', welderName: '七区电焊四组', planStarttime: '启用', realityStarttime: 'OTC', planEndtime: '是', planEnd: '0004', planEn: '上海', planE: 'www.baidu.com', plan: 'CPVE-500' },
        { taskNo: '0006', gradeIdToStr: '气保焊', deptName: '2021-05-29 11:56:58', welderName: '七区电焊五组', planStarttime: '启用', realityStarttime: 'OTC', planEndtime: '是', planEnd: '0005', planEn: '上海', planE: 'www.baidu.com', plan: 'CPVE-500' },
        { taskNo: '0007', gradeIdToStr: '气保焊', deptName: '2021-05-29 10:56:58', welderName: '七区电焊六组', planStarttime: '启用', realityStarttime: 'OTC', planEndtime: '是', planEnd: '0006', planEn: '上海', planE: 'www.baidu.com', plan: 'YD-500GR3' },
        { taskNo: '0008', gradeIdToStr: '气保焊', deptName: '2021-05-29 10:46:58', welderName: '七区电焊七组', planStarttime: '启用', realityStarttime: 'OTC', planEndtime: '是', planEnd: '0007', planEn: '上海', planE: 'www.baidu.com', plan: 'CPVE-500' },
        { taskNo: '0009', gradeIdToStr: '气保焊', deptName: '2021-05-29 10:36:58', welderName: '七区电焊八组', planStarttime: '启用', realityStarttime: 'OTC', planEndtime: '是', planEnd: '0008', planEn: '上海', planE: 'www.baidu.com', plan: 'YD-500GR3' }],
      total: 9,
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
