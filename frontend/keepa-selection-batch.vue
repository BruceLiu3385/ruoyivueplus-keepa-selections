<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="批次名称" prop="batchName">
        <el-input
          v-model="queryParams.batchName"
          placeholder="请输入批次名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option label="初始" value="0" />
          <el-option label="配置中" value="1" />
          <el-option label="抓取ASIN中" value="2" />
          <el-option label="抓取详情中" value="3" />
          <el-option label="分析中" value="4" />
          <el-option label="完成" value="5" />
          <el-option label="失败" value="9" />
        </el-select>
      </el-form-item>
      <el-form-item label="亚马逊站点" prop="amazonDomain">
        <el-select v-model="queryParams.amazonDomain" placeholder="请选择亚马逊站点" clearable size="small">
          <el-option
            v-for="domain in domainList"
            :key="domain.domainCode"
            :label="domain.domainName"
            :value="domain.domainCode"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['keepa:batch:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['keepa:batch:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['keepa:batch:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['keepa:batch:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格数据 -->
    <el-table v-loading="loading" :data="batchList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="批次ID" align="center" prop="id" width="80" />
      <el-table-column label="批次名称" align="center" prop="batchName" :show-overflow-tooltip="true" />
      <el-table-column label="批次编码" align="center" prop="batchCode" width="180" />
      <el-table-column label="亚马逊站点" align="center" prop="amazonDomainName" width="120" />
      <el-table-column label="状态" align="center" prop="statusName" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">{{ scope.row.statusName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="进度" align="center" width="120">
        <template slot-scope="scope">
          <el-progress :percentage="scope.row.progressPercentage" :stroke-width="8" />
        </template>
      </el-table-column>
      <el-table-column label="ASIN数量" align="center" width="100">
        <template slot-scope="scope">
          {{ scope.row.currentAsinCount }} / {{ scope.row.maxAsinCount }}
        </template>
      </el-table-column>
      <el-table-column label="详情数量" align="center" prop="currentDetailCount" width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['keepa:batch:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['keepa:batch:edit']"
          >修改</el-button>
          <el-button
            v-if="scope.row.canStartFetchAsin"
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleFetchAsin(scope.row)"
            v-hasPermi="['keepa:batch:fetchAsin']"
          >抓取ASIN</el-button>
          <el-button
            v-if="scope.row.canStartFetchDetail"
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleFetchDetail(scope.row)"
            v-hasPermi="['keepa:batch:fetchDetail']"
          >抓取详情</el-button>
          <el-button
            v-if="scope.row.canStartAnalyze"
            size="mini"
            type="text"
            icon="el-icon-s-data"
            @click="handleAnalyze(scope.row)"
            v-hasPermi="['keepa:batch:analyze']"
          >分析数据</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['keepa:batch:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改选品批次对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="批次名称" prop="batchName">
          <el-input v-model="form.batchName" placeholder="请输入批次名称" />
        </el-form-item>
        <el-form-item label="批次描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入批次描述" />
        </el-form-item>
        <el-form-item label="亚马逊站点" prop="amazonDomain">
          <el-select v-model="form.amazonDomain" placeholder="请选择亚马逊站点">
            <el-option
              v-for="domain in domainList"
              :key="domain.domainCode"
              :label="domain.domainName"
              :value="domain.domainCode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="类目ID" prop="categoryIds">
          <el-input v-model="form.categoryIds" placeholder="请输入类目ID，多个用逗号分隔" />
        </el-form-item>
        <el-form-item label="最大ASIN数量" prop="maxAsinCount">
          <el-input-number v-model="form.maxAsinCount" :min="100" :max="500000" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 批次详情对话框 -->
    <el-dialog title="批次详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="批次名称">{{ batchDetail.batchName }}</el-descriptions-item>
        <el-descriptions-item label="批次编码">{{ batchDetail.batchCode }}</el-descriptions-item>
        <el-descriptions-item label="亚马逊站点">{{ batchDetail.amazonDomainName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTagType(batchDetail.status)">{{ batchDetail.statusName }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="ASIN数量">
          {{ batchDetail.currentAsinCount }} / {{ batchDetail.maxAsinCount }}
        </el-descriptions-item>
        <el-descriptions-item label="详情数量">{{ batchDetail.currentDetailCount }}</el-descriptions-item>
        <el-descriptions-item label="进度">
          <el-progress :percentage="batchDetail.progressPercentage" :stroke-width="10" />
        </el-descriptions-item>
        <el-descriptions-item label="类目ID">{{ batchDetail.categoryIds }}</el-descriptions-item>
        <el-descriptions-item label="开始时间" :span="2">
          {{ parseTime(batchDetail.startTime, '{y}-{m}-{d} {h}:{i}:{s}') || '未开始' }}
        </el-descriptions-item>
        <el-descriptions-item label="结束时间" :span="2">
          {{ parseTime(batchDetail.endTime, '{y}-{m}-{d} {h}:{i}:{s}') || '未结束' }}
        </el-descriptions-item>
        <el-descriptions-item label="批次描述" :span="2">{{ batchDetail.description }}</el-descriptions-item>
        <el-descriptions-item v-if="batchDetail.errorMessage" label="错误信息" :span="2">
          <el-alert :title="batchDetail.errorMessage" type="error" :closable="false" />
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listBatch, getBatch, delBatch, addBatch, updateBatch, exportBatch, createBatch, startFetchAsin, startFetchDetail, startAnalyze, getBatchProgress } from "@/api/keepa/batch";
import { listDomain } from "@/api/keepa/domain";

export default {
  name: "KsSelectionBatch",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 选品批次表格数据
      batchList: [],
      // 亚马逊站点数据
      domainList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      detailOpen: false,
      // 批次详情
      batchDetail: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        batchName: null,
        status: null,
        amazonDomain: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        batchName: [
          { required: true, message: "批次名称不能为空", trigger: "blur" }
        ],
        amazonDomain: [
          { required: true, message: "亚马逊站点不能为空", trigger: "change" }
        ],
        categoryIds: [
          { required: true, message: "类目ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDomainList();
  },
  methods: {
    /** 查询选品批次列表 */
    getList() {
      this.loading = true;
      listBatch(this.queryParams).then(response => {
        this.batchList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询亚马逊站点列表 */
    getDomainList() {
      listDomain().then(response => {
        this.domainList = response.data;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        batchName: null,
        description: null,
        amazonDomain: null,
        categoryIds: null,
        maxAsinCount: 5000,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加选品批次";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBatch(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改选品批次";
      });
    },
    /** 查看详情 */
    handleView(row) {
      getBatch(row.id).then(response => {
        this.batchDetail = response.data;
        this.detailOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBatch(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            createBatch(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除选品批次编号为"' + ids + '"的数据项？').then(function() {
        return delBatch(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('keepa/batch/export', {
        ...this.queryParams
      }, `batch_${new Date().getTime()}.xlsx`)
    },
    /** 抓取ASIN */
    handleFetchAsin(row) {
      this.$modal.confirm('确认开始抓取ASIN列表？').then(function() {
        return startFetchAsin(row.id);
      }).then(() => {
        this.$modal.msgSuccess("已开始抓取ASIN列表");
        this.getList();
      }).catch(() => {});
    },
    /** 抓取商品详情 */
    handleFetchDetail(row) {
      this.$modal.confirm('确认开始抓取商品详情？').then(function() {
        return startFetchDetail(row.id);
      }).then(() => {
        this.$modal.msgSuccess("已开始抓取商品详情");
        this.getList();
      }).catch(() => {});
    },
    /** 分析商品数据 */
    handleAnalyze(row) {
      this.$modal.confirm('确认开始分析商品数据？').then(function() {
        return startAnalyze(row.id);
      }).then(() => {
        this.$modal.msgSuccess("已开始分析商品数据");
        this.getList();
      }).catch(() => {});
    },
    /** 获取状态标签类型 */
    getStatusTagType(status) {
      const statusMap = {
        '0': '',
        '1': 'warning',
        '2': 'warning',
        '3': 'warning',
        '4': 'warning',
        '5': 'success',
        '9': 'danger'
      };
      return statusMap[status] || '';
    }
  }
};
</script>
