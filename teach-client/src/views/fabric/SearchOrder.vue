<template>
  <Navi/>
  <div>
    <OrderDetail v-model:visible="flag"
    :amount="dataa.amount"
    :quantity="dataa.quantity"
    :sellerId="dataa.sellerId"
    :sellerReview="dataa.sellerReview"
    :orderId="dataa.orderId"
    :orderTime="dataa.orderTime"
    :name="dataa.name"
    :status="dataa.status"
    :buyerId="dataa.buyerId"
    :buyerReview="dataa.buyerReview"
    :logistics="dataa.logistics"/>
    <create v-model:visible="p"/>
     <his v-model:visible="v" :orderId="msg"/>
  </div>
  <div >
     <el-button type="primary" @click="dk">添加</el-button>
     <el-button v-if="isd" type="info" >批量删除</el-button>&nbsp;
     <el-button v-else type="danger" @click="mude">批量删除</el-button>&nbsp;
     <el-input v-model="input" placeholder="单号" style="width: 150px"/>&nbsp;
     <el-date-picker v-model="time" type="daterange"
     range-separator="至" start-placeholder="开始日期"
     value-format="YYYY-MM-DD"
     end-placeholder="结束日期">
     </el-date-picker> &nbsp;
     <el-input v-model="si" placeholder="卖家id" style="width: 150px"/>&nbsp;
     <el-input v-model="bi" placeholder="买家id" style="width: 150px"/>&nbsp;
     <el-input v-model="wl" placeholder="物流" style="width: 80px"/>&nbsp;
     <el-input v-model="st" placeholder="交易状态" style="width: 80px"/>&nbsp;
     <el-button type="primary" @click="SearchOrder">搜索</el-button>
  </div>
      <el-main>
      <el-table :data="tableData"  height="680px" style="width: 80%" @selection-change="handleSelectionChange">
      <el-table-column  width="80" type="selection"></el-table-column>
      <el-table-column  label="卖家id"  prop="sellerId"  width="150">
      </el-table-column>
      <el-table-column  label="交易id"  prop="orderId"  width="150">
      </el-table-column>
      <el-table-column  label="买家id"  prop="buyerId"  width="150">
      </el-table-column>
      <el-table-column  label="状态"  prop="status"  width="90" >
      </el-table-column>
      <el-table-column label="操作" width="300" >
        <template #default="{ row }">
          <div>
          <el-button size="small" type="primary" @click="open(row)">详细信息</el-button>
          <el-button size="small" type="success" @click="history(row)">历史记录</el-button>
          <el-button size="small" type="danger" @click="handleDel(row)">删除</el-button>
          </div>
      </template>
      </el-table-column>
    </el-table>
    <!-- 分页底部 -->

    </el-main>
</template>

<script>
import Navi from '@/components/Navi'
import { ref, watch } from 'vue'
import OrderDetail from "./OrderDetail.vue"
import CreateOrder from "./CreateOrder.vue"
import OrderHistory from "./OrderHistory.vue"
import { ElMessageBox } from "element-plus";
import {searchOrder,deleteOrder,multiDeleteOrder} from "@/service/fabricServ";

export default {
  components: {
    Navi,
    OrderDetail,
    create: CreateOrder,
    his: OrderHistory
  },
  data() {
    return {
      isd:true,
      input: '',
      time:"",
      si:'',
      bi:'',
      wl:'',
      st:'',
      tableData : [],
      tempData:[],
      arr:[]
    };
  },
methods: {
          SearchOrder(){
           if(!this.input&&!this.time&&!this.si&&!this.bi&&!this.wl&&!this.st) {
             this.$message({
               message: '请至少填入一项信息',
               type: 'warning',
             })
           }
           else
           {
            searchOrder({"orderId":this.input,"startDateTime":this.time[0],"endDateTime":this.time[1],"sellerId":this.si,"buyerId":this.bi,"logisticsStatus":this.wl,"orderStatus":this.st}).then(response=>{
                        this.tableData=response.data
            })
           }
          },
          handleSelectionChange(val){
             console.log(val)
              this.tempData=val
              if(this.tempData == undefined || this.tempData <= 0)
              this.isd=true
              else
              this.isd=false
          },
          handleDel(val) {
            console.log(val)
      ElMessageBox.confirm("你确定删除这个信息吗?", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.handleSure(val);
        })
        .catch(() => {
          // catch error
        });
    },
    handleSure(val) {
      const index = this.tableData.findIndex((item) => item.orderId == val.orderId);
      console.log(index)
      this.tableData.splice(index, 1);
      deleteOrder({'orderId':val.orderId}).then(response=>{
        if (response.code == '0') {
          this.$message({
            message:  '删除成功',
            type: 'success',
          })
        } else {
          this.$message({
            message: response.msg,
            type: 'warning',
          })
        }

      })
    },
    mude(){
      this.$confirm("确认删除？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(() => {
        this.arr=[]
        //遍历获得多选选中的index值
        this.tempData.forEach((value) => {
          //遍历多选框获取的数据
          this.tableData.forEach((v, i) => {
            //遍历数据表，任意一个属性值相同，则说明该数据被选中，其i则为索引值
            if (value.orderId == v.orderId) {
              this.arr.push(v.orderId)
              this.tableData.splice(i, 1);
            }
          });
        });
        console.log(this.arr)
        multiDeleteOrder({'orderIds':this.arr}).then((res)=>{
          this.$message({
            type: "success",
            message: res.data.msg,
          });
         })
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消删除",
        });
      });

    }
},
  setup() {
    const flag = ref(false)
    const p= ref(false)
    const v= ref(false)
    const msg=ref("")
     const dataa =    ref( {amount: 0,
      quantity: 0,
      orderTime: "",
      sellerId: "",
      sellerReview: "",
      orderId: "",
      name: "",
      logistics: "",
      buyerId: "",
      buyerReview: "",
      status: ""
      })
    const open = (val) => {
      flag.value = true
      dataa.value.amount=val.amount
      dataa.value.quantity=val.quantity
      dataa.value.orderTime=val.orderTime
      dataa.value.sellerId=val.sellerId
      dataa.value.sellerReview=val.sellerReview
      dataa.value.orderId= val.orderId
      dataa.value.name=val.name
      dataa.value.logistics=val.logistics
      dataa. value.buyerId=val. buyerId
      dataa.value. buyerReview=val. buyerReview
      dataa.value.status=val.status
    }
    const history= (val) =>{
        v.value=true
        msg.value=val.orderId
    }
    const dk = () => {
      p.value = true
    }

    watch(() => flag.value , (val) => {
      console.log("监听flag值得变化:", val)
    })

    return {
      flag,
      open,
      dk,
      dataa,
      p,
      msg,
      v,
      history
    }
  }
}
</script>
