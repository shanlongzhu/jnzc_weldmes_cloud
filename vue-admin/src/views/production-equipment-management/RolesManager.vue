<template>
    <div>
        <!-- <div />
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
    </div> -->
        <el-tree
            :data="data"
            show-checkbox
            node-key="id"
            ref="tree"
            default-expand-all
            :expand-on-click-node="false"
        >
        </el-tree>
        <div class="buttons">
            <el-button @click="getCheckedNodes">通过 node 获取</el-button>
            <el-button @click="getCheckedKeys">通过 key 获取</el-button>
            <el-button @click="setCheckedNodes">通过 node 设置</el-button>
            <el-button @click="setCheckedKeys">通过 key 设置</el-button>
            <el-button @click="resetChecked">清空</el-button>
        </div>
    </div>
</template>

<script>
let id = 1000;

export default {
    data () {
        return {
            data: [{
                id: 1,
                label: '生产设备管理',
                type: "menu",
                mark: "1",
                children: [{
                    id: 101,
                    label: '采集模块管理',
                    mark: "101",
                    type: "menu",
                    parentId: 1,
                    children: [{
                        id: 1011,
                        parentId: 101,
                        mark: "101_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 1012,
                        parentId: 101,
                        mark: "101_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 102,
                    label: '生产设备管理',
                    mark: "102",
                    type: "menu",
                    parentId: 1,
                    children: [{
                        id: 1021,
                        parentId: 102,
                        mark: "102_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 1022,
                        parentId: 102,
                        mark: "102_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 103,
                    label: '设备厂商及型号绑定',
                    mark: "103",
                    type: "menu",
                    parentId: 1,
                    children: [{
                        id: 1031,
                        parentId: 103,
                        mark: "103_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 1032,
                        parentId: 103,
                        mark: "103_del",
                        type: "btn",
                        label: '删除'
                    }]
                }]
            },
            {
                id: 2,
                label: '生产工艺管理',
                type: "menu",
                mark: "2",
                children: [{
                    id: 201,
                    label: '工艺管理',
                    mark: "201",
                    type: "menu",
                    parentId: 2,
                    children: [{
                        id: 2011,
                        parentId: 201,
                        mark: "201_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 2012,
                        parentId: 201,
                        mark: "201_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 202,
                    label: '焊机参数管理',
                    mark: "202",
                    type: "menu",
                    parentId: 2,
                    children: [{
                        id: 2021,
                        parentId: 202,
                        mark: "202_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 2022,
                        parentId: 202,
                        mark: "202_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 203,
                    label: '模拟设备参数设置',
                    mark: "203",
                    type: "menu",
                    parentId: 2,
                    children: [{
                        id: 2031,
                        parentId: 203,
                        mark: "203_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 2032,
                        parentId: 203,
                        mark: "203_del",
                        type: "btn",
                        label: '删除'
                    }]
                }]
            },
            {
                id: 3,
                label: '生产过程管理',
                type: "menu",
                mark: "3",
                children: [{
                    id: 301,
                    label: '焊工管理',
                    mark: "301",
                    type: "menu",
                    parentId: 3,
                    children: [{
                        id: 3011,
                        parentId: 301,
                        mark: "301_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 3012,
                        parentId: 301,
                        mark: "301_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 302,
                    label: '任务工单管理',
                    mark: "302",
                    type: "menu",
                    parentId: 3,
                    children: [{
                        id: 3021,
                        parentId: 302,
                        mark: "302_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 3022,
                        parentId: 302,
                        mark: "302_del",
                        type: "btn",
                        label: '删除'
                    }]
                }]
            },
            {
                id: 4,
                label: '生产过程记录',
                type: "menu",
                mark: "4",
                children: [{
                    id: 401,
                    label: '生产任务详情',
                    mark: "401",
                    type: "menu",
                    parentId: 4,
                    children: [{
                        id: 4011,
                        parentId: 401,
                        mark: "401_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 4012,
                        parentId: 401,
                        mark: "401_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 402,
                    label: '故障表',
                    mark: "402",
                    type: "menu",
                    parentId: 4,
                    children: [{
                        id: 4021,
                        parentId: 402,
                        mark: "402_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 4022,
                        parentId: 402,
                        mark: "402_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 403,
                    label: '历史回溯',
                    mark: "403",
                    type: "menu",
                    parentId: 4,
                    children: [{
                        id: 4031,
                        parentId: 403,
                        mark: "403_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 4032,
                        parentId: 403,
                        mark: "403_del",
                        type: "btn",
                        label: '删除'
                    }]
                }]
            },
            {
                id: 5,
                label: '生产数据统计',
                type: "menu",
                mark: "5",
                children: [{
                    id: 501,
                    label: '人员生产数据统计',
                    mark: "501",
                    type: "menu",
                    parentId: 5,
                    children: [{
                        id: 5011,
                        parentId: 501,
                        mark: "501_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 5012,
                        parentId: 501,
                        mark: "501_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 502,
                    label: '工件生产数据统计',
                    mark: "502",
                    type: "menu",
                    parentId: 5,
                    children: [{
                        id: 5021,
                        parentId: 502,
                        mark: "502_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 5022,
                        parentId: 502,
                        mark: "502_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 503,
                    label: '设备生产数据统计',
                    mark: "503",
                    type: "menu",
                    parentId: 5,
                    children: [{
                        id: 5031,
                        parentId: 503,
                        mark: "503_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 5032,
                        parentId: 503,
                        mark: "503_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 504,
                    label: '班组生产数据统计',
                    mark: "504",
                    type: "menu",
                    parentId: 5,
                    children: [{
                        id: 5041,
                        parentId: 504,
                        mark: "504_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 5042,
                        parentId: 504,
                        mark: "504_del",
                        type: "btn",
                        label: '删除'
                    }]
                }]
            },
            {
                id: 6,
                label: '生产数据详情分析',
                type: "menu",
                mark: "6",
                children: [{
                    id: 601,
                    label: '焊工',
                    mark: "601",
                    type: "menu",
                    parentId: 6,
                    children: [{
                        id: 6011,
                        parentId: 601,
                        mark: "6011",
                        type: "menu",
                        label: '焊工工效',
                        children: [{
                            id: 601101,
                            parentId: 6011,
                            mark: "6011_add",
                            type: "btn",
                            label: '新增'
                        }, {
                            id: 601102,
                            parentId: 6011,
                            mark: "6011_del",
                            type: "btn",
                            label: '删除'
                        }]
                    }]
                }, {
                    id: 602,
                    label: '任务/工件',
                    mark: "602",
                    type: "menu",
                    parentId: 6,
                    children: [{
                        id: 6021,
                        parentId: 602,
                        mark: "6021",
                        type: "menu",
                        label: '任务焊接工程',
                        children: [{
                            id: 602101,
                            parentId: 6021,
                            mark: "6021_add",
                            type: "btn",
                            label: '新增'
                        }, {
                            id: 602102,
                            parentId: 6021,
                            mark: "6021_del",
                            type: "btn",
                            label: '删除'
                        }]
                    }]
                }, {
                    id: 603,
                    label: '焊机',
                    mark: "603",
                    type: "menu",
                    parentId: 6,
                    children: [{
                        id: 6031,
                        parentId: 603,
                        mark: "6031",
                        type: "menu",
                        label: '负载率',
                        children: [{
                            id: 603101,
                            parentId: 6031,
                            mark: "6031_add",
                            type: "btn",
                            label: '新增'
                        }, {
                            id: 603102,
                            parentId: 6031,
                            mark: "6031_del",
                            type: "btn",
                            label: '删除'
                        }]
                    }]
                }]
            },
            {
                id: 7,
                label: '系统设置',
                type: "menu",
                mark: "7",
                children: [{
                    id: 701,
                    label: '用户管理',
                    mark: "701",
                    type: "menu",
                    parentId: 7,
                    children: [{
                        id: 7011,
                        parentId: 701,
                        mark: "701_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 7012,
                        parentId: 701,
                        mark: "701_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 702,
                    label: '角色管理',
                    mark: "702",
                    type: "menu",
                    parentId: 7,
                    children: [{
                        id: 7021,
                        parentId: 702,
                        mark: "702_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 7022,
                        parentId: 702,
                        mark: "702_del",
                        type: "btn",
                        label: '删除'
                    }]

                }, {
                    id: 703,
                    label: '菜单管理',
                    mark: "703",
                    type: "menu",
                    parentId: 7,
                    children: [{
                        id: 7031,
                        parentId: 703,
                        mark: "703_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 7032,
                        parentId: 703,
                        mark: "703_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 704,
                    label: '字典管理',
                    mark: "704",
                    type: "menu",
                    parentId: 7,
                    children: [{
                        id: 7041,
                        parentId: 704,
                        mark: "704_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 7042,
                        parentId: 704,
                        mark: "704_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 705,
                    label: '组织机构管理',
                    mark: "705",
                    type: "menu",
                    parentId: 7,
                    children: [{
                        id: 7051,
                        parentId: 705,
                        mark: "705_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 7052,
                        parentId: 705,
                        mark: "705_del",
                        type: "btn",
                        label: '删除'
                    }]
                }, {
                    id: 706,
                    label: '邮件下发管理',
                    mark: "706",
                    type: "menu",
                    parentId: 7,
                    children: [{
                        id: 7061,
                        parentId: 706,
                        mark: "706_add",
                        type: "btn",
                        label: '新增'
                    }, {
                        id: 7062,
                        parentId: 706,
                        mark: "706_del",
                        type: "btn",
                        label: '删除'
                    }]
                }]
            }
            ]
        }
    },

    created () {
    },
    methods: {
        append (data) {
            const newChild = { id: id++, label: 'testtest', children: [] };
            if (!data.children) {
                this.$set(data, 'children', []);
            }
            data.children.push(newChild);
        },

        remove (node, data) {
            const parent = node.parent;
            const children = parent.data.children || parent.data;
            const index = children.findIndex(d => d.id === data.id);
            children.splice(index, 1);
        },

        getCheckedNodes () {
            console.log(this.$refs.tree.getCheckedNodes(false,true));
        },
        getCheckedKeys () {
            console.log(this.$refs.tree.getCheckedKeys());
        },
        setCheckedNodes () {
            this.$refs.tree.setCheckedNodes([{
                id: 5,
                label: '二级 2-1'
            }, {
                id: 9,
                label: '三级 1-1-1'
            }]);
        },
        setCheckedKeys () {
            this.$refs.tree.setCheckedKeys([3]);
        },
        resetChecked () {
            this.$refs.tree.setCheckedKeys([]);
        }
    }
}
</script>

<style>
.custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
}
</style>
