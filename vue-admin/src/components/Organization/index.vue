<!--
 * @Descripttion: 
 * @version: 
 * @Author: zhanganpeng
 * @Date: 2021-07-31 14:26:52
 * @LastEditors: zhanganpeng
 * @LastEditTime: 2021-07-31 14:53:11
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
import { getUserTree } from '_api/system/systemApi'
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
                label: 'name'
            },
        }
    },
    watch: {},
    computed: {},
    methods: {
        //获取部门tree
        async getUserTreeFun (id) {
            this.treeLoading = true;
            let { data, code } = await getUserTree();
            this.treeLoading = false;
            if (code == 200) {
                this.treeData = [data] || [];
                this.$nextTick(() => {
                    this.$refs.treeDom.setCurrentKey(id||1)
                })
            }
        },
        currentChangeTree(v){
            this.$emit('currentChangeTree',v);
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