<template>
  <Navi/>
  <div >
    <h2>订单管理</h2>
    <div>
      <el-form  :inline="true">
        <el-form-item label="订单编号"> <el-input v-model="searchForm.orderId" placeholder="请输入编号"></el-input></el-form-item>
        <el-form-item label="起止日期">
          <el-date-picker
              v-model="searchForm.dateTimeRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"/>
        </el-form-item>
        <el-form-item label="卖家编号"> <el-input v-model="searchForm.sellerId" placeholder="请输入编号"></el-input></el-form-item>
        <el-form-item label="买家编号"> <el-input v-model="searchForm.buyerId" placeholder="请输入编号"></el-input></el-form-item>
        <el-form-item label="物流状态">
          <el-select v-model="searchForm.logisticsStatus" :clearable="true" placeholder="选择物流状态">
            <el-option v-for="item in logisticsStatusList" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.orderStatus" clearable placeholder="选择订单状态">
            <el-option v-for="item in orderStatusList" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchOrder" v-show="hasRight('RIGHT_QUERY')">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-button type="success" @click="newOrderDialog=true">创建订单</el-button>
    <el-popconfirm title="确定删除吗？" @confirm="multiDelete" v-show="readyToDelete.length>0">
      <template #reference>
        <el-button type="danger"  v-show="readyToDelete.length>0">批量删除</el-button>
      </template>
    </el-popconfirm>


    <el-table :data="orders" style="width: 100%" height="500"  @selection-change="handleSelectionChange">
      <el-table-column label="解密">
        <template #default="scope">
          <el-button size="small" type="info" @click="decrypt(scope.row.name)">解密</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="orderTime"  label="订单日期" sortable/>
      <el-table-column prop="name"  label="订单名称" sortable/>
      <el-table-column label="订单状态" width="120">
        <template #default="scope">
          <el-tag>{{orderStatusMap.get(scope.row.status)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="buyerId"  label="买家编号" sortable width="120"/>
      <el-table-column prop="sellerId"  label="卖家编号" sortable width="120"/>
      <el-table-column prop="quantity"  label="数量" sortable width="80"/>
      <el-table-column prop="amount"  label="金额" sortable width="80"/>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" type="primary" @click="openEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="success" @click="openHistory(scope.row.orderId)">历史</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="deleteOrder(scope.row.orderId)">
            <template #reference>
              <el-button size="small" type="danger" >删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" />
    </el-table>

    <el-dialog v-model="historyDialog" title="订单历史" width="70%">
      <el-table :data="histories">
        <el-table-column prop="timeStamp" label="时间" width="250" sortable>
          <template #default="scope">
            <el-date-picker
                v-model="scope.row.timestamp"
                type="datetime"
                disabled/>
          </template>
        </el-table-column>
        <el-table-column  label="卖家评价"  prop="sellerReview" show-overflow-tooltip  />
        <el-table-column  label="买家评价"  prop="buyerReview" show-overflow-tooltip  />
        <el-table-column label="物流状态" width="80">
          <template #default="scope">
            <el-tag type="success">{{logisticStatusMap.get(scope.row.logistics)}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="订单状态" width="80">
          <template #default="scope">
            <el-tag>{{orderStatusMap.get(scope.row.status)}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog v-model="newOrderDialog" title="创建订单">
      <el-form label-position="left" label-width="80px">
        <el-form-item label="订单号" >
          <el-input v-model="newOrderForm.orderId"  />
        </el-form-item>
        <el-form-item label="名称">
        <el-form :inline="true">
          <el-form-item  >
            <el-input v-model="newOrderForm.name"  />
          </el-form-item>
          <el-form-item>
            <el-switch v-model="newOrderForm.encrypt"  style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                       active-text="加密" inactive-text="不加密"/>
          </el-form-item>
        </el-form>
        </el-form-item>
        <el-form-item label="买家编号" >
          <el-input v-model="newOrderForm.buyerId" />
        </el-form-item>
        <el-form-item label="卖家编号" >
          <el-input v-model="newOrderForm.sellerId" />
        </el-form-item>

        <el-form-item label="交易日期" >
          <el-date-picker
              v-model="newOrderForm.orderTime"
              type="date"
              placeholder="请选择交易日期"/>
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="newOrderForm.quantity" :min="0" :max="10"  />
        </el-form-item>
        <el-form-item label="金额">
          <el-input-number v-model="newOrderForm.amount" :precision="2" :min="0" :step="0.01" :max="9999999" />
        </el-form-item>

      </el-form>
      <template #footer>
            <span class="dialog-footer">
              <el-button @click="newOrderDialog = false">取消</el-button>
              <el-button type="primary" @click="submitOrder">提交</el-button>
            </span>
      </template>
    </el-dialog>

    <el-dialog v-model="editDialog" title="编辑订单">
      <el-form label-position="left" label-width="80px" >
        <el-form-item label="订单状态">
          <el-form :inline="true">
            <el-form-item >
              <el-select v-model="editForm.status" :clearable="true" placeholder="订单状态">
                <el-option v-for="item in orderStatusList" :key="item.value" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="submitOrderStatus">提交</el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
        <el-form-item label="物流状态">
          <el-form :inline="true">
            <el-form-item >
              <el-select v-model="editForm.logistics" :clearable="true" placeholder="物流状态">
                <el-option v-for="item in logisticsStatusList" :key="item.value" :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="submitOrderLogistics">提交</el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
        <el-form-item label="买家评价">
          <el-form :inline="true">
            <el-form-item >
              <el-input :rows="3" type="textarea" v-model="editForm.buyerReview" style="width:500px"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="submitOrderBuyer">提交</el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
        <el-form-item label="卖家评价" >
          <el-form :inline="true">
            <el-form-item >
              <el-input :rows="3" type="textarea" v-model="editForm.sellerReview" style="width:500px"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="submitOrderSeller">提交</el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog v-model="decryptDialog" title="解密结果" width="30%">
      <span>{{decrypted}}</span>
    </el-dialog>


  </div>

</template>

<script>
import Navi from "@/components/Navi";
import {getLogisticsList,getOrderStatusList} from "@/service/infoServ";
import {getMyRightTypes} from "@/service/rightServ";
import {searchOrder,getHistoryOrder,createOrder,deleteOrder,updateLogistics,updateTxn,addSellerReview,addBuyerReview,multiDeleteOrder,decrypt} from "@/service/fabricServ";
import {ElMessage} from "element-plus";

export default {
  name: "SearchOrder",
  components:{Navi},
  data(){
    return{
      rightTypes:[],
      logisticsStatusList:[],
      logisticStatusMap:{},
      orderStatusList:[],
      orderStatusMap:{},

      readyToDelete:[],

      searchForm:{
        orderId:"",
        dateTimeRange:[],
        sellerId:"",
        buyerId:"",
        logisticsStatus:"",
        orderStatus:""
      },

      newOrderDialog:false,
      newOrderForm:{
        orderId:"",
        name:"",
        quantity:0,
        amount:0,
        orderTime:null,
        buyerId:"",
        sellerId:"",
        encrypt:true
      },

      histories:[],
      historyDialog:false,

      editForm:{},
      editDialog:false,

      decryptDialog:false,
      decrypted:"",

      orders:[]
    }
  },
  created() {
    this.getOrderStatusEnum()
    this.getLogisticsStatusEnum()
    this.getRightTypes()
  },
  methods:{
    hasRight(right){
      return this.rightTypes.includes(right)
    },
    decrypt(name){
      decrypt({"name":name}).then(res=>{
        if(res.code==='0'){
          this.decrypted=res.data
          this.decryptDialog=true
        }else{
          ElMessage.error("不可解密")
        }
      })
    },
    multiDelete(){
      multiDeleteOrder({"orderIds":this.readyToDelete}).then(res=>{
        ElMessage.success(res.data)
        this.searchOrder()
      })
    },
    handleSelectionChange(orders){
      this.readyToDelete=[]
      orders.forEach((order)=>{
        this.readyToDelete.push(order.orderId)
      })
    },
    submitOrderStatus(){
      updateTxn(this.editForm).then(res=>{
        ElMessage.success(res.data)
      })
    },
    submitOrderLogistics(){
      updateLogistics(this.editForm).then(res=>{
        ElMessage.success(res.data)
      })
    },
    submitOrderBuyer(){
      addBuyerReview(this.editForm).then(res=>{
        ElMessage.success(res.data)
      })
    },
    submitOrderSeller(){
      addSellerReview(this.editForm).then(res=>{
        ElMessage.success(res.data)
      })
    },
    deleteOrder(orderId){
      deleteOrder({"orderId":orderId}).then(res=>{
        ElMessage.success(res.data)
        const index = this.orders.findIndex(p => p.orderId === orderId);
        this.orders.splice(index, 1);
      })
    },
    submitOrder(){
      createOrder(this.newOrderForm).then(res=>{
        ElMessage.success(res.data)
        this.newOrderDialog=false
        this.searchOrder()
      })
    },
    openEdit(order){
      this.editForm=order
      this.editDialog=true
    },
    openHistory(orderId){
      getHistoryOrder({'orderId':orderId}).then(res=>{
        if(res.code==='0'){
          this.histories=res.data[0]
          this.histories.forEach((v, i) => {
            this.histories[i] = Object.assign({'timestamp': v.timestamp}, v.order);
          })
          this.historyDialog=true
        }else{
          ElMessage.error(res.msg)
        }
      })
    },
    searchOrder(){
      this.searchForm.startDateTime=this.searchForm.dateTimeRange[0]
      this.searchForm.endDateTime=this.searchForm.dateTimeRange[1]
      searchOrder(this.searchForm).then(res=>{
        if(res.code==='0')
          this.orders=res.data
        else{
          ElMessage.error(res.msg)
        }
      })
    },
    getOrderStatusEnum(){
      getOrderStatusList().then(res=>{
        this.orderStatusList=res.data
        this.orderStatusMap=res.data.reduce((result, { value,label }) => {
          result.set(value,label);
          return result;
        }, new Map());
      })
    },
    getLogisticsStatusEnum(){
      getLogisticsList().then(res=>{
        this.logisticsStatusList=res.data
        this.logisticStatusMap=res.data.reduce((result, { value,label }) => {
          result.set(value,label);
          return result;
        }, new Map());
      })
    },
    getRightTypes(){
      getMyRightTypes().then(res=>{
        this.rightTypes=res.data
      })
    }
  }
}
</script>

<style scoped>

</style>
