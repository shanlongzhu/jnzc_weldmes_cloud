<template>
  <div>
    <div>
      <el-table
        id="out-table"
        :data="list"
        stripe
        style="width: 100%"
        align="center"
        border
        size="medium"
        :header-cell-style="{background:'#eef1f6',color:'#606266'}"
      >
        <el-table-column prop="taskNo" label="任 务 编 号" align="center" />
        <el-table-column prop="gradeIdToStr" label="任 务 等 级" align="center" />
        <el-table-column prop="deptName" label="所 属 班 组" align="center" />
        <el-table-column prop="welderName" label="焊 工 姓 名" align="center" />
        <el-table-column prop="planStarttime" label="计划开始时间" align="center" />
        <el-table-column prop="realityStarttime" label="实际开始时间" align="center" />
        <el-table-column prop="planEndtime" label="计划结束时间" align="center" />
        <el-table-column prop="realityEndtime" label="实际结束时间" align="center" />
        <el-table-column prop="evaluateContent" label="任 务 评 价" align="center" />
        <el-table-column prop="evaluateStarsIdToStr" label="评 价 等 级" align="center" />
        <el-table-column label="操 作" align="center" class-name="small-padding fixed-width">
          <template>
            <el-button size="mini" type="primary" plain>
              修改
            </el-button>
            <el-button size="mini" type="danger" plain>
              删除
            </el-button>
            <el-button size="mini" type="success" plain>
              评价
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="currentPage1"
        :page-size="5"
        align="center"
        layout="total, prev, pager, next"
        :total=total>
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { getTaskInfos,queryCurrentPageTaskListController} from '../../api/user'
export default {
  methods: {
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(pn) {
      queryCurrentPageTaskListController(pn).then(res => {
        this.list = res.data.list
      })
    }
  },
  data() {
    return {
      list: {},
      total:0,
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
    getTaskInfos().then(res => {
      this.list = res.data.list
      this.total = res.data.total
    })
  }
}
</script>

<style scoped>

</style>
