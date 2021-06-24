<template>
  <div>
    <div class="roleTree">
      <el-tree :data="data" :props="defaultProps" float="left" @node-click="handleNodeClick" />
    </div>
    <div class="roleTable">
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
        <el-table-column prop="taskNo" label="用户名" align="center" width="100" />
        <el-table-column prop="gradeIdToStr" label="登录名" align="center" width="100" />
        <el-table-column prop="deptName" label="电话" align="center" width="110" />
        <el-table-column prop="welderName" label="邮箱" align="center" width="160" />
        <el-table-column prop="planStarttime" label="岗位" align="center" width="100" />
        <el-table-column prop="realityStarttime" label="部门" align="center" width="100" />
        <el-table-column prop="planEndtime" label="状态" align="center" width="100" />
        <el-table-column prop="planEnd" label="角色" align="center" width="110">
          <el-button size="mini" type="success" plain>
            角色列表
          </el-button>
        </el-table-column>
        <el-table-column label="操 作" align="center" class-name="small-padding fixed-width">
          <template>
            <el-button size="mini" type="primary" plain>
              修改
            </el-button>
            <el-button size="mini" type="danger" plain>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
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
  name: 'RoleManageTable',
  data() {
    return {
      role: null,
      showButton: false,
      list: [{ taskNo: '管理员', gradeIdToStr: 'admin', deptName: '18536334521', welderName: '851949634@qq.com', planStarttime: '网络管理员', realityStarttime: '集团', planEndtime: '启用' },
        { taskNo: '李小鹏', gradeIdToStr: '10009488', deptName: '18536334523', welderName: '851949534@qq.com', planStarttime: '技术', realityStarttime: '装焊一区', planEndtime: '启用' },
        { taskNo: '张大龙', gradeIdToStr: '10009487', deptName: '18536334526', welderName: '851959634@qq.com', planStarttime: '人事', realityStarttime: '装焊二区', planEndtime: '启用' },
        { taskNo: '陈二狗', gradeIdToStr: '10009486', deptName: '18436334521', welderName: '851949634@qq.com', planStarttime: '物料', realityStarttime: '装焊三区', planEndtime: '启用' },
        { taskNo: '燕小六', gradeIdToStr: '10009485', deptName: '18566334521', welderName: '851969634@qq.com', planStarttime: '后勤', realityStarttime: '装焊四区', planEndtime: '启用' },
        { taskNo: '袁天罡', gradeIdToStr: '10009484', deptName: '18336334521', welderName: '852349634@qq.com', planStarttime: '销售', realityStarttime: '装焊五区', planEndtime: '启用' },
        { taskNo: '白展堂', gradeIdToStr: '10009483', deptName: '18936334521', welderName: '855649634@qq.com', planStarttime: '实施', realityStarttime: '装焊六区', planEndtime: '启用' },
        { taskNo: '王洪祥', gradeIdToStr: '10009482', deptName: '18736334521', welderName: '823949634@qq.com', planStarttime: '测试', realityStarttime: '装焊七区', planEndtime: '启用' },
        { taskNo: '至尊宝', gradeIdToStr: '10009481', deptName: '18136334521', welderName: '859649634@qq.com', planStarttime: 'DBA', realityStarttime: '装焊八区', planEndtime: '启用' },
        { taskNo: '芈月', gradeIdToStr: '10009480', deptName: '13636334521', welderName: '851979634@qq.com', planStarttime: '经理', realityStarttime: '装焊九区', planEndtime: '启用' }],
      total: 10,
      data: [{
        label: '集团',
        children: [{
          label: '制造部',
          children: [{
            label: '装焊一区',
            children: [{
              label: '装一电焊一组'
            },
            {
              label: '装一电焊二组'
            },
            {
              label: '装一电焊三组'
            },
            {
              label: '装一装配一组'
            },
            {
              label: '装一装配二组'
            }]
          },
          {
            label: '装焊二区',
            children: [{
              label: '装二电焊一组'
            },
            {
              label: '装二电焊二组'
            },
            {
              label: '装二电焊三组'
            },
            {
              label: '装二装配四组'
            },
            {
              label: '装二装配五组'
            }]
          },
          {
            label: '装焊三区',
            children: [{
              label: '装三电焊一组'
            },
            {
              label: '装三电焊二组'
            },
            {
              label: '装三电焊三组'
            },
            {
              label: '装三装配一组'
            },
            {
              label: '装三装配二组'
            }]
          },
          {
            label: '装焊四区',
            children: [{
              label: '装四电焊一组'
            },
            {
              label: '装四电焊二组'
            },
            {
              label: '装四电焊三组'
            },
            {
              label: '装四装配一组'
            },
            {
              label: '装四装配二组'
            }]
          },
          {
            label: '装焊五区',
            children: [{
              label: '装五电焊一组'
            },
            {
              label: '装五电焊二组'
            },
            {
              label: '装五电焊三组'
            },
            {
              label: '装五装配一组'
            },
            {
              label: '装五电焊二组'
            }]
          }]
        }]
      }],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
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

<style>
  .roleTree{
    float: left;
    width: 15%;
  }
  .roleTable{
    float: right;
    width: 85%;
  }
</style>
