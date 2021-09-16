import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import parentMain from '@/layout/components/parentMain'

/* Router Modules */
// eslint-disable-next-line no-unused-vars
import componentsRouter from './modules/components'

// eslint-disable-next-line no-unused-vars
import chartsRouter from './modules/charts'

// eslint-disable-next-line no-unused-vars
import tableRouter from './modules/table'

// eslint-disable-next-line no-unused-vars
import nestedRouter from './modules/nested'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */

/* 通用路由*/
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/swipeCard',
    component: () => import('@/views/swipes/swipeCard'),
    hidden: true
  },
  {
    path: '/swipeInfo',
    component: () => import('@/views/swipes/swipeInfo'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: '/',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
/* 动态路由*/
export const asyncRoutes = [
  {
    path: '/per',
    component: Layout,
    redirect: 'page',
    alwaysShow: true,
    name: 'Per',
    meta: {
      title: '生产设备管理',
      icon: 'tree',
      mark:'1'
    },
    children: [
      {
        path: 'page',
        component: () => import('@/views/production-equipment-management/gatherModleManager'),
        name: 'PagePermission',
        meta: {
          title: '采集模块管理',
          mark:'101',
          noCache:true
        }
      },
      {
        path: 'directive',
        component: () => import('@/views/production-equipment-management/product-equip-manage'),
        name: 'DirectivePermission',
        meta: {
          title: '生产设备管理',
          mark:'102'
        }
      },
      {
        path: 'sx-model-list',
        component: () => import('@/views/sxModel/sxModelList'),
        name: 'sxModelList',
        meta: {
          title: '松下生产设备',
          mark:'103',
          noCache:true
        }
      }
    ]
  },
  {
    path: '/pro',
    component: Layout,
    redirect: 'pa',
    alwaysShow: true,
    name: 'Permission',
    meta: {
      title: '生产工艺管理',
      icon: 'example',
      mark:'2'
    },
    children: [
      {
        path: 'pa',
        component: () => import('@/views/production-equipment-management/technology-manage'),
        name: 'tech',
        meta: {
          title: '工艺管理',
          mark:'201'
        }
      },
      // {
      //   path: 'directives',
      //   component: () => import('@/views/table/inline-edit-table'),
      //   name: 'DirectivePermission',
      //   meta: {
      //     title: '焊机参数管理',
      //     mark:'202'
      //   }
      // },
      // {
      //   path: 'roe',
      //   component: () => import('@/views/table/inline-edit-table'),
      //   name: 'RolePermission',
      //   meta: {
      //     title: '模拟设备参数设置',
      //     mark:'203'
      //   }
      // }
    ]
  },
  {
    path: '/cha',
    component: Layout,
    redirect: 'welderManagement',
    alwaysShow: true,
    name: 'Charts',
    meta: {
      title: '生产过程管理',
      mark:'3',
      icon: 'tree-table'
    },
    children: [
      {
        path: 'welderManagement',
        component: () => import('@/views/production-equipment-management/welderManagement.vue'),
        name: 'welderManagement',
        meta: {
          title: '焊工管理',
          mark:'301'
        }
      },
      {
        path: 'card',
        component: () => import('@/views/production-equipment-management/show-task-list'),
        name: 'KeyboardChart',
        meta: {
          title: '任务工单管理',
          mark:'302'}
      }
    ]
  },
  {
    path: '/table',
    component: Layout,
    redirect: 'welder-history-line',
    alwaysShow: true,
    name: 'Table',
    meta: {
      title: '生产过程记录',
      mark:'4',
      icon: 'table'
    },
    children: [
      // {
      //   path: 'table',
      //   component: () => import('@/views/charts/keyboard'),
      //   name: 'DynamicTable',
      //   meta: { title: '生产任务详情',mark:'401' }
      // },
      // {
      //   path: 'drag-table1',
      //   component: () => import('@/views/table/drag-table'),
      //   name: 'DragTable',
      //   meta: {
      //     title: '故障表',
      //     mark:'402'
      //   }
      // },
      {
        path: 'welder-history-line',
          component: () => import('@/views/productionDataAnalysis/historyLine'),
          name: 'welderHistoryLine',
          meta: {
            title: '历史回溯',
            mark:'401',
            noCache: true }
      }
    ]
  },
  {
    path: '/production-data-statistics',
    component: Layout,
    redirect: 'team-data-stat',
    alwaysShow: true,
    name: 'productionDataStatistics',
    meta: {
      title: '生产数据统计',
      mark:'5',
      icon: 'chart'
    },
    children: [
      {
        path: 'team-data-stat',
        component: () => import('@/views/productionDataStatistics/teamProDataStatistics'),
        name: 'teamDataStat',
        meta: {
          title: '班组生产数据统计',
          mark:'504'}
      },
      {
        path: 'personnel-production',
        component: () => import('@/views/productionDataStatistics/personnelProduction'),
        name: 'personnelProduction',
        meta: {
          title: '人员生产数据统计',
          mark:'501'
        }
      },
      {
        path: 'workpiece-production',
        component: () => import('@/views/productionDataStatistics/workpieceProduction'),
        name: 'workpieceProduction',
        meta: {
          title: '工件生产数据统计',
          mark:'502'
         }
      },
      {
        path: 'equ-product',
        component: () => import('@/views/productionDataStatistics/equProduct'),
        name: 'equProduct',
        meta: {
          title: '设备生产数据统计',
          mark:'503'
         }
      },
      {
        path: 'task-detail',
        component: () => import('@/views/productionDataStatistics/taskDetail'),
        name: 'taskDetail',
        meta: {
          title: '生产任务详情',
          mark:'505'
        }
      },
      // {
      //   path: 'welding-task',
      //   component: () => import('@/views/productionDataStatistics/weldingMachine'),
      //   name: 'weldingMachine',
      //   meta: {
      //     title: '焊机任务列表',
      //     mark:'505'
      //   }
      // },
    ]
  },
  // {
  //   path: '/product-data',
  //   component: Layout,
  //   redirect: 'welder-history-line',
  //   alwaysShow: true,
  //   name: 'productData',
  //   meta: {
  //     title: '生产数据详情分析',
  //     mark:'6',
  //     icon: 'el-icon-s-tools'
  //   },
  //   children: [
  //     {
  //       path: 'welder-history-line',
  //         component: () => import('@/views/productionDataAnalysis/historyLine'),
  //         name: 'welderHistoryLine',
  //         meta: {
  //           title: '历史曲线',
  //           mark:'601',
  //           noCache: true }
  //     }
  //   ]
  // },
  {
    path: '/ch',
    component: Layout,
    redirect: 'user-manager',
    alwaysShow: true,
    name: 'system',
    meta: {
      mark:'7',
      title: '系统管理',
      roles: ['admin'],
      icon: 'el-icon-s-tools'
    },
    children: [
      {
        path: 'user-manager',
        component: () => import('@/views/production-equipment-management/user-manage'),
        name: 'userManager',
        meta: {
          mark:'701',
          title: '用户管理',
          roles: ['admin'],
          noCache: true }
      },
      {
        path: 'roles-manager',
        component: () => import('@/views/production-equipment-management/RolesManager'),
        name: 'RolesManager',
        meta: {
          mark:'702',
          title: '角色管理',
          roles: ['admin'],
          noCache: true }
      },
      {
        path: 'menus-manager',
        component: () => import('@/views/production-equipment-management/menusManager'),
        name: 'menusManager',
        meta: {
          title: '菜单管理',
          mark:'703',
          noCache: true }
      },
      {
        path: 'dictionaries',
        component: () => import('@/views/system/dictionariesManager/dictionaries'),
        name: 'dictionariesName',
        meta: {
          title: '字典管理',
          mark:'704',
          noCache: true }
      },
      {
        path: 'organizational',
        component: () => import('@/views/system/organizationalStructure/organizational'),
        name: 'organizationalName',
        meta: {
          mark:'705',
          title: '组织机构管理',
          roles: ['admin'],
          noCache: true }
      },
      // {
      //   path: 'keyboard8',
      //   component: () => import('@/views/system/drag/dragMenu'),
      //   name: 'exEmail',
      //   meta: {
      //     title: '邮件下发管理',
      //     mark:'706',
      //     noCache: true }
      // }
    ]
  },
  {
    path: '/log-manager',
    component: Layout,
    redirect: 'log-list',
    alwaysShow: true,
    name: 'logManager',
    meta: {
      mark:'8',
      title: '日志管理',
      icon: 'el-icon-notebook-2'
    },
    children: [
      {
        path: 'log-list',
        component: () => import('@/views/logManager/logList'),
        name: 'logList',
        meta: {
          mark:'801',
          title: '日志列表' }
      }
    ]
  },
  {
    path: '/monitor-manager',
    component: Layout,
    redirect: 'real-time',
    alwaysShow: true,
    name: 'monitorManager',
    meta: {
      mark:'9',
      title: '实时监测管理',
      icon: 'el-icon-data-board'
    },
    children: [
      {
        path: 'real-time',
        component: () => import('@/views/monitor/realTime'),
        name: 'realTime',
        meta: {
          mark:'901',
          title: '实时监测' }
      },
      {
        path: 'real-time-area',
        component: () => import('@/views/monitor/realTimeArea'),
        name: 'realTimeArea',
        meta: {
          mark:'902',
          title: '实时监测2' }
      } ,
      {
        path: 'real-time-area-detail',
        component: () => import('@/views/monitor/realTimeArea2'),
        name: 'realTimeAreaDetail',
        hidden: true,
        meta: {
          mark:'903',
          title: '实时监测3',
          activeMenu: '/example/list' ,
          noCache:true
        }
      }
    ]
  },
  /* {
    path: '/icon',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/icons/index'),
        name: 'Icons',
        meta: { title: 'Icons', icon: 'icon', noCache: true }
      }
    ]
  },*/

  /** when your routing map is too long, you can split it into small modules **/
  /* componentsRouter,
  chartsRouter,
  nestedRouter,
  tableRouter,*/
  /* {
    path: '/tab',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/tab/index'),
        name: 'Tab',
        meta: { title: 'Tab', icon: 'tab' }
      }
    ]
  },*/

  /* {
    path: '/error',
    component: Layout,
    redirect: 'noRedirect',
    name: 'ErrorPages',
    meta: {
      title: 'Error Pages',
      icon: '404'
    },
    children: [
      {
        path: '401',
        component: () => import('@/views/error-page/401'),
        name: 'Page401',
        meta: { title: '401', noCache: true }
      },
      {
        path: '404',
        component: () => import('@/views/error-page/404'),
        name: 'Page404',
        meta: { title: '404', noCache: true }
      }
    ]
  },*/

  /* {
    path: '/error-log',
    component: Layout,
    children: [
      {
        path: 'log',
        component: () => import('@/views/error-log/index'),
        name: 'ErrorLog',
        meta: { title: 'Error Log', icon: 'bug' }
      }
    ]
  },*/

  /* {
    path: '',
    component: Layout,
    redirect: '/zip/download',
    alwaysShow: true,
    name: 'Zip',
    meta: { title: 'ZIP', icon: 'zip' },
    children: [
      {
        path: 'download',
        component: () => import('@/views/zip/index'),
        name: 'ExportZip',
        meta: { title: 'Export Zip' }
      }
    ]
  },*/

  /* {
    path: '/excel',
    component: Layout,
    redirect: '/excel/export-excel',
    name: 'Excel',
    meta: {
      title: 'Excel',
      icon: 'excel'
    },
    children: [
      {
        path: 'export-excel',
        component: () => import('@/views/excel/export-excel'),
        name: 'ExportExcel',
        meta: { title: 'Export Excel' }
      },
      {
        path: 'export-selected-excel',
        component: () => import('@/views/excel/select-excel'),
        name: 'SelectExcel',
        meta: { title: 'Export Selected' }
      },
      {
        path: 'export-merge-header',
        component: () => import('@/views/excel/merge-header'),
        name: 'MergeHeader',
        meta: { title: 'Merge Header' }
      },
      {
        path: 'upload-excel',
        component: () => import('@/views/excel/upload-excel'),
        name: 'UploadExcel',
        meta: { title: 'Upload Excel' }
      }
    ]
  },*/

  /* {
    path: '/pdf',
    component: Layout,
    redirect: '/pdf/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/pdf/index'),
        name: 'PDF',
        meta: { title: 'PDF', icon: 'pdf' }
      }
    ]
  },*/
  {
    path: '/pdf/download',
    component: () => import('@/views/pdf/download'),
    hidden: true
  },

  /* {
    path: '/theme',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/theme/index'),
        name: 'Theme',
        meta: { title: 'Theme', icon: 'theme' }
      }
    ]
  },*/

  /* {
    path: '/clipboard',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/clipboard/index'),
        name: 'ClipboardDemo',
        meta: { title: 'Clipboard', icon: 'clipboard' }
      }
    ]
  },*/

  /* {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://github.com/PanJiaChen/vue-element-admin',
        meta: { title: 'External Link', icon: 'link' }
      }
    ]
  },
*/
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}
export default router
