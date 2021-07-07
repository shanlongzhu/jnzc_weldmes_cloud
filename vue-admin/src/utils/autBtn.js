import Vue from 'vue'
import store from '@/store'
import router from '@/router'



Vue.directive('has',{
    inserted:function(el, binding){
        const hasValue = binding.value;
        const roles = store.getters && store.getters.roles;
        const menus = store.getters && store.getters.menus;        
        const mark = router.currentRoute.meta&&router.currentRoute.meta.mark;
        const arrData = menus.filter(item => item.mark==mark); 
        if(!roles.includes('admin')){
            if(arrData.length>0){
                const btnMark = menus.filter(item => item.parentId==arrData[0].id).map(item => item.mark);                        
                if(btnMark.length==0){
                   el.parentNode.removeChild(el)
                }else{
                    let btnMarkValueArr = btnMark.map(item => item.split('_')[1]).filter(item => item==hasValue);
                    if(btnMarkValueArr.length==0){
                        el.parentNode.removeChild(el)
                    }                
                }
            }
        }        
    }
})