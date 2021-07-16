<template>
  <section class="main">
    <div class="crumbs">
      <el-breadcrumb separator="/"></el-breadcrumb>
    </div>
    <div class="container">
      <div class="drag-box">
        <el-row>
          <el-col :span="24">
            <div class="drag-box-item">
              <div class="item-title">已选菜单</div>
              <draggable v-model="bytesUsedMenu" @remove="removeHandle" :options="dragOptions">
                <transition-group tag="div" id="已选菜单" class="item-ul">
                  <div
                    v-for="item in bytesUsedMenu"
                    class="drag-list"
                    :key="item.id"
                    style="min-width: 40px;float: left;"
                  >{{item.content}}</div>
                  <div @click="addMenu" key="55" class="drag-list notMove">添加新菜单</div>
                </transition-group>
              </draggable>
            </div>
          </el-col>
        </el-row>
      </div>
      <br />
      <div class="drag-box">
        <el-row>
          <el-col :span="24">
            <div class="drag-box-item">
              <div class="item-title">未选菜单</div>
              <draggable v-model="unusedMenu" @remove="removeHandle" :options="dragOptions">
                <transition-group tag="div" id="未选菜单" class="item-ul">
                  <div
                    v-for="item in unusedMenu"
                    class="drag-list"
                    :key="item.id"
                    style="min-width: 40px;float: left;"
                  >{{item.content}}</div>
                </transition-group>
              </draggable>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </section>
</template>
 
<script>
import draggable from "vuedraggable";
export default {
  name: "draglist",
  data() {
    return {
      dragOptions: {
        animation: 300,
        scroll: true,
        group: "sortlist",
        ghostClass: "ghost-style",
        filter: ".notMove"
      },
      bytesUsedMenu: [
        {
          id: 1,
          content: "首页"
        },
        {
          id: 2,
          content: "简介"
        },
        {
          id: 3,
          content: "研究内容"
        },
        {
          id: 4,
          content: "团队合作"
        },
        {
          id: 5,
          content: "新闻动态"
        },
        {
          id: 6,
          content: "团队出行"
        }
      ],
      unusedMenu: []
    };
  },
  components: {
    draggable
  },
  methods: {
    addMenu() {
      this.bytesUsedMenu.push({
        id: this.bytesUsedMenu.length + 1 + "",
        content: "自定义菜单" + this.bytesUsedMenu.length + 1
      });
    },
    removeHandle(event) {
      console.log(this.bytesUsedMenu);
      console.log(this.unusedMenu);
      console.log(event);
      this.$message.success(`从 ${event.from.id} 移动到 ${event.to.id} `);
    }
  }
};
</script>
 
<style scoped>
.drag-box {
  display: flex;
  user-select: none;
}
.drag-box-item {
  width: 900px;
  min-height: 105px;
  flex: 1;
  /* max-width: 600px; */
  min-width: 400px;
  background-color: #eff1f5;
  margin-right: 16px;
  border-radius: 6px;
  border: 1px #e1e4e8 solid;
}
.item-title {
  padding: 8px 8px 8px 12px;
  font-size: 14px;
  line-height: 1.5;
  color: #24292e;
  font-weight: 600;
}
.item-ul {
  padding: 0 8px 8px;
  /* height: 500px; */
  overflow-y: scroll;
}
.item-ul::-webkit-scrollbar {
  width: 0;
}
.drag-list {
  border: 1px #e1e4e8 solid;
  padding: 10px;
  margin: 5px 0 10px;
  list-style: none;
  background-color: #fff;
  border-radius: 6px;
  cursor: pointer;
  -webkit-transition: border 0.3s ease-in;
  transition: border 0.3s ease-in;
}
.drag-list:hover {
  border: 1px solid #20a0ff;
}
.drag-title {
  font-weight: 400;
  line-height: 25px;
  margin: 10px 0;
  font-size: 22px;
  color: #1f2f3d;
}
.ghost-style {
  display: block;
  color: #ccc;
  border-style: dashed;
}
.notMove {
  color: #ffff;
  background-color: #409eff;
  min-width: 40px;
  float: left;
}
</style>