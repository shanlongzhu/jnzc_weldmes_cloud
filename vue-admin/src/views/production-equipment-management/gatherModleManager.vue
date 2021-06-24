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
          <el-table-column prop="taskNo" label="采 集 模 块 编 号" align="center" width="100" />
          <el-table-column prop="gradeIdToStr" label="所 属 项 目" align="center" width="100" />
          <el-table-column prop="deptName" label="采 集 模 块 状 态" align="center" width="100" />
          <el-table-column prop="welderName" label="采 集 模 块 通 讯 协 议" align="center" width="100" />
          <el-table-column prop="planStarttime" label="采 集 模 块 IP 地 址" align="center" width="120" />
          <el-table-column prop="realityStarttime" label="采 集 模 块 MAC 地 址" align="center" width="120" />
          <el-table-column prop="planEndtime" label="采 集 模 块 出 厂 时 间" align="center" width="120" />
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
      list: [{ taskNo: '7853', gradeIdToStr: '部件MAG焊机', deptName: '正常', welderName: '串口', planStarttime: 'wwww.baidu.com', realityStarttime: 'www.baidu.com', planEndtime: '2021-05-29 11:46:58' },
        { taskNo: '7854', gradeIdToStr: '部件MAG焊机', deptName: '正常', welderName: '串口', planStarttime: 'wwww.baidu.com', realityStarttime: 'www.baidu.com', planEndtime: '2021-05-29 11:46:58' },
        { taskNo: '7855', gradeIdToStr: '部件MAG焊机', deptName: '正常', welderName: '串口', planStarttime: 'wwww.baidu.com', realityStarttime: 'www.baidu.com', planEndtime: '2021-05-29 11:46:58' },
        { taskNo: '7856', gradeIdToStr: '部件MAG焊机', deptName: '正常', welderName: '串口', planStarttime: 'wwww.baidu.com', realityStarttime: 'www.baidu.com', planEndtime: '2021-05-29 11:46:58' },
        { taskNo: '7857', gradeIdToStr: '部件MAG焊机', deptName: '正常', welderName: '串口', planStarttime: 'wwww.baidu.com', realityStarttime: 'www.baidu.com', planEndtime: '2021-05-29 11:46:58' },
        { taskNo: '7858', gradeIdToStr: '部件MAG焊机', deptName: '正常', welderName: '串口', planStarttime: 'wwww.baidu.com', realityStarttime: 'www.baidu.com', planEndtime: '2021-05-29 11:46:58' },
        { taskNo: '7860', gradeIdToStr: '部件MAG焊机', deptName: '正常', welderName: '串口', planStarttime: 'wwww.baidu.com', realityStarttime: 'www.baidu.com', planEndtime: '2021-05-29 11:46:58' },
        { taskNo: '7861', gradeIdToStr: '部件MAG焊机', deptName: '正常', welderName: '串口', planStarttime: 'wwww.baidu.com', realityStarttime: 'www.baidu.com', planEndtime: '2021-05-29 11:46:58' },
        { taskNo: '7862', gradeIdToStr: '部件MAG焊机', deptName: '正常', welderName: '串口', planStarttime: 'wwww.baidu.com', realityStarttime: 'www.baidu.com', planEndtime: '2021-05-29 11:46:58' },
        { taskNo: '7863', gradeIdToStr: '部件MAG焊机', deptName: '正常', welderName: '串口', planStarttime: 'wwww.baidu.com', realityStarttime: 'www.baidu.com', planEndtime: '2021-05-29 11:46:58' }],
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
