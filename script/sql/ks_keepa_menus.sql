-- Keepa 模块菜单与权限(适配 RuoYi-Vue-Plus sys_menu 结构)

-- 目录：选品工具
insert into sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values (6000, '选品工具', 0, 20, 'keepa', null, '', 1, 0, 'M', '0', '0', '', 'shopping', 103, 1, sysdate(), null, null, 'Keepa选品目录');

-- 菜单：批次管理
insert into sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values (6001, '批次管理', 6000, 1, 'batch', 'keepa/batch/index', '', 1, 0, 'C', '0', '0', 'keepa:batch:list', 'list', 103, 1, sysdate(), null, null, 'Keepa 批次管理');

-- 菜单：站点管理
insert into sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values (6002, '站点管理', 6000, 2, 'domain', 'keepa/domain/index', '', 1, 0, 'C', '0', '0', 'keepa:domain:list', 'international', 103, 1, sysdate(), null, null, 'Keepa 站点管理');

-- 按钮权限：批次管理
insert into sys_menu values (600101, '批次导出', 6001, 5, '', '', '', 1, 0, 'F', '0', '0', 'keepa:batch:export', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600102, '批次查询', 6001, 1, '', '', '', 1, 0, 'F', '0', '0', 'keepa:batch:query',  '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600103, '批次新增', 6001, 2, '', '', '', 1, 0, 'F', '0', '0', 'keepa:batch:add',    '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600104, '批次修改', 6001, 3, '', '', '', 1, 0, 'F', '0', '0', 'keepa:batch:edit',   '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600105, '批次删除', 6001, 4, '', '', '', 1, 0, 'F', '0', '0', 'keepa:batch:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600106, '创建批次', 6001, 6, '', '', '', 1, 0, 'F', '0', '0', 'keepa:batch:create', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600107, '抓取ASIN', 6001, 7, '', '', '', 1, 0, 'F', '0', '0', 'keepa:batch:fetchAsin', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600108, '抓取详情', 6001, 8, '', '', '', 1, 0, 'F', '0', '0', 'keepa:batch:fetchDetail', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600109, '分析数据', 6001, 9, '', '', '', 1, 0, 'F', '0', '0', 'keepa:batch:analyze', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600110, '查看进度', 6001, 10, '', '', '', 1, 0, 'F', '0', '0', 'keepa:batch:progress', '#', 103, 1, sysdate(), null, null, '');

-- 按钮权限：站点管理
insert into sys_menu values (600201, '站点可用列表', 6002, 1, '', '', '', 1, 0, 'F', '0', '0', 'keepa:domain:list', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600202, '站点全量列表', 6002, 2, '', '', '', 1, 0, 'F', '0', '0', 'keepa:domain:all',  '#', 103, 1, sysdate(), null, null, '');

-- 菜单：API工具（可选，便于测试）
insert into sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values (6003, 'API工具', 6000, 3, 'apiTool', 'keepa/api/index', '', 1, 0, 'C', '0', '0', 'keepa:api:category', 'api', 103, 1, sysdate(), null, null, 'Keepa API工具');

-- API权限按钮
insert into sys_menu values (600301, '获取类目',     6003, 1, '', '', '', 1, 0, 'F', '0', '0', 'keepa:api:category',    '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600302, '搜索商品',     6003, 2, '', '', '', 1, 0, 'F', '0', '0', 'keepa:api:search',      '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600303, '畅销榜',       6003, 3, '', '', '', 1, 0, 'F', '0', '0', 'keepa:api:bestsellers', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600304, '商品详情',     6003, 4, '', '', '', 1, 0, 'F', '0', '0', 'keepa:api:details',     '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600305, 'Token状态',    6003, 5, '', '', '', 1, 0, 'F', '0', '0', 'keepa:api:token',       '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600306, '密钥校验',     6003, 6, '', '', '', 1, 0, 'F', '0', '0', 'keepa:api:validate',    '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values (600307, '域名信息',     6003, 7, '', '', '', 1, 0, 'F', '0', '0', 'keepa:api:domain',      '#', 103, 1, sysdate(), null, null, '');

-- 赋权：将Keepa目录及其全部子菜单、按钮分配给角色ID=1(超级管理员)
insert into sys_role_menu select 1 as role_id, menu_id from sys_menu where menu_id between 6000 and 600399;
