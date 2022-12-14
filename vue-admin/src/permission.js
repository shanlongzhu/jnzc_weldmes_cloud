import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken,removePublicToken,getWelderInfo,removeWelderInfo,removeToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect'] // no redirect whitelist
const authIde = process.env.VUE_APP_AUTH_IDE;
router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()
  // set page title
  document.title = getPageTitle(to.meta.title)
  // determine whether the user has logged in

  //根据项目标识判断是否是访问同一个项目
  if(sessionStorage.getItem('authIde')){
    if(sessionStorage.getItem('authIde')!=authIde){
      removeToken();
    }
  }else{
    sessionStorage.setItem('authIde',authIde)
  }

  const hasToken = getToken()
  //进入焊工刷卡界面
  if(to.path==='/swipeCard'){
    removeWelderInfo();
    NProgress.done()
    next();
  }else if(to.path==='/swipeInfo'){
    if(getWelderInfo()){
      NProgress.done()
      next();
    }else{
      NProgress.done()
      next(`/swipeCard`)
    }
  }else if (hasToken) {
    removePublicToken();
    removeWelderInfo();
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done() // hack: https://github.com/PanJiaChen/vue-element-admin/pull/2939
    } else {
      // determine whether the user has obtained his permission roles through getInfo
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      if (hasRoles) {
        next()
      } else {
        try {
          // get user info
          // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
          const { roles,menus } = await store.dispatch('user/getInfo')
          let objData = {
            menusArr:menus.menus||[],
            roles:roles||[]
          }

          // generate accessible routes map based on roles
          const accessRoutes = await store.dispatch('permission/generateRoutes', objData)
          // dynamically add accessible routes
          router.addRoutes(accessRoutes)
          // hack method to ensure that addRoutes is complete
          // set the replace: true, so the navigation will not leave a history record
          if(accessRoutes.length==0){
            next();
          }else{
            next({ ...to, replace: true })
          }
        } catch (error) {
          sessionStorage.removeItem('authIde')
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    removePublicToken();
    removeWelderInfo();    
    /* has no token*/
    sessionStorage.removeItem('authIde')

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
