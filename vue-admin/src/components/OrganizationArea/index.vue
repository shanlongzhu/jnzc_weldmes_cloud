<!--
 * @Descripttion: 
 * @version: 
 * @Author: zhanganpeng
 * @Date: 2021-07-31 14:26:52
 * @LastEditors: zhanganpeng
 * @LastEditTime: 2021-08-27 16:29:04
-->
<template>
    <el-tree
        :data="treeData"
        ref="treeDom"
        :expand-on-click-node="false"
        v-loading="treeLoading"
        :props="defaultProps"
        default-expand-all
        @current-change="currentChangeTree"
        highlight-current
        node-key="id"
    ></el-tree>
</template>

<script>
import { getAreaG,getAreaOrgG } from '_api/system/systemApi'

export default {
    components: {},
    props: {},
    data () {
        return {
            //部门tree数据
            treeLoading: false,
            treeData: [],
            defaultProps: {
                children: 'list',
                label: 'areaBayName'
            },
            once: 1,
            index_d: 1
        }
    },
    watch: {},
    computed: {},
    methods: {
        //获取部门tree
        async getUserTreeFun (id) {
            this.treeLoading = true;
            let { data, code } = await getAreaOrgG();
            this.treeLoading = false;
            if (code == 200) {
                this.treeData = this.repentData(data || []);
                this.$nextTick(() => {
                    this.$refs.treeDom.setCurrentKey(id || this.treeData[0].id);
                    //只加载第一次
                    if (this.once === 1) {
                        this.once++;
                        this.currentChangeTree(this.treeData[0])
                    }
                })
            }
        },

        repentData (arr) {            
            let newArr = arr.map(item => {
                this.index_d++;
                let objItem = { ...item }
                objItem.id = this.index_d;
                if (item.list && item.list.length > 0) {
                    objItem.list = this.repentData(item.list)
                } 
                return objItem                
            });
            return newArr;
        },


        currentChangeTree (v) {
            this.$emit('currentChangeTree', v);
        }
    },
    created () {
        this.getUserTreeFun();
    },
    mounted () { }
}
</script>
<style lang="scss" scoped>
.wrapper {
}
</style>