-- ============================================================
-- 酒店搜索及价格系统 - Mock 测试数据
-- 生成日期: 2026-03-29
-- 说明: 数据覆盖全部业务表，按业务场景组织
--       入住日期使用 2026-04-05 ~ 2026-04-10 区间
--       酒店数据贴合参考截图中的上海酒店场景
-- ============================================================

-- 使用说明:
-- 1. 先执行 DDL 建表语句（系统设计文档 2.5.1 章节）
-- 2. 再执行本文件导入测试数据
-- 3. t_snapshot_freshness_config 已在 DDL 中含初始化数据，本文件不重复插入

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ============================================================
-- 一、基础数据表
-- ============================================================

-- ------------------------------------------------------------
-- 1.1 城市信息表 t_city (12个城市, 含6个热门)
-- ------------------------------------------------------------
TRUNCATE TABLE t_city;
INSERT INTO t_city (city_code, city_name, city_name_en, city_pinyin, city_pinyin_abbr, province_name, country_name, country_code, is_domestic, timezone, utc_offset, city_tier, is_hot, hot_sort, latitude, longitude) VALUES
('SHA', '上海', 'Shanghai',   'shanghai',   'sh',  '上海市',   '中国', 'CN', 1, 'Asia/Shanghai', 8, 'TOP50',     1, 1,  31.230416, 121.473701),
('BJS', '北京', 'Beijing',    'beijing',    'bj',  '北京市',   '中国', 'CN', 1, 'Asia/Shanghai', 8, 'TOP50',     1, 2,  39.904200, 116.407396),
('KMG', '昆明', 'Kunming',    'kunming',    'km',  '云南省',   '中国', 'CN', 1, 'Asia/Shanghai', 8, 'TOP50',     1, 3,  25.040000, 102.710000),
('SYX', '三亚', 'Sanya',      'sanya',      'sy',  '海南省',   '中国', 'CN', 1, 'Asia/Shanghai', 8, 'TOP50',     1, 4,  18.252847, 109.511909),
('XIY', '西安', 'Xi''an',     'xian',       'xa',  '陕西省',   '中国', 'CN', 1, 'Asia/Shanghai', 8, 'TOP50',     1, 5,  34.341568, 108.939774),
('CAN', '广州', 'Guangzhou',  'guangzhou',  'gz',  '广东省',   '中国', 'CN', 1, 'Asia/Shanghai', 8, 'TOP50',     1, 6,  23.129163, 113.264435),
('HGH', '杭州', 'Hangzhou',   'hangzhou',   'hz',  '浙江省',   '中国', 'CN', 1, 'Asia/Shanghai', 8, 'TOP50',     0, NULL, 30.274084, 120.155070),
('CTU', '成都', 'Chengdu',    'chengdu',    'cd',  '四川省',   '中国', 'CN', 1, 'Asia/Shanghai', 8, 'TOP50',     0, NULL, 30.572816, 104.066801),
('DLC', '大连', 'Dalian',     'dalian',     'dl',  '辽宁省',   '中国', 'CN', 1, 'Asia/Shanghai', 8, 'TOP50_100', 0, NULL, 38.914003, 121.614682),
('LJG', '丽江', 'Lijiang',    'lijiang',    'lj',  '云南省',   '中国', 'CN', 1, 'Asia/Shanghai', 8, 'TOP50_100', 0, NULL, 26.872108, 100.225730),
('TYO', '东京', 'Tokyo',      'dongjing',   'dj',  NULL,       '日本', 'JP', 0, 'Asia/Tokyo',    9, 'TOP50',     0, NULL, 35.689487, 139.691711),
('BKK', '曼谷', 'Bangkok',    'mangu',      'mg',  NULL,       '泰国', 'TH', 0, 'Asia/Bangkok',  7, 'TOP50_100', 0, NULL, 13.756331, 100.501762);


-- ------------------------------------------------------------
-- 1.2 币种配置表 t_currency
-- ------------------------------------------------------------
TRUNCATE TABLE t_currency;
INSERT INTO t_currency (currency_code, currency_name, currency_symbol, exchange_rate) VALUES
('CNY', '人民币',   '¥',  1.000000),
('USD', '美元',     '$',  7.240000),
('EUR', '欧元',     '€',  7.860000),
('JPY', '日元',     '¥',  0.048200),
('GBP', '英镑',     '£',  9.150000),
('THB', '泰铢',     '฿',  0.200000),
('HKD', '港币',     'HK$', 0.928000),
('SGD', '新加坡元', 'S$', 5.380000);


-- ------------------------------------------------------------
-- 1.3 缓存有效期配置表 t_cache_ttl_config
-- ------------------------------------------------------------
TRUNCATE TABLE t_cache_ttl_config;

-- 有报价缓存策略 (HAS_PRICE)
INSERT INTO t_cache_ttl_config (city_tier, days_range_min, days_range_max, ttl_hours, config_type) VALUES
('TOP50',     0,   7,   2,  'HAS_PRICE'),
('TOP50',     8,  14,   3,  'HAS_PRICE'),
('TOP50',    15,  30,   6,  'HAS_PRICE'),
('TOP50',    31,  60,  12,  'HAS_PRICE'),
('TOP50',    61, 180,  72,  'HAS_PRICE'),
('TOP50',   181, 365, 240,  'HAS_PRICE'),
('TOP50_100', 0,   7,   4,  'HAS_PRICE'),
('TOP50_100', 8,  14,   5,  'HAS_PRICE'),
('TOP50_100',15,  30,   8,  'HAS_PRICE'),
('TOP50_100',31,  60,  18,  'HAS_PRICE'),
('TOP50_100',61, 180, 120,  'HAS_PRICE'),
('TOP50_100',181,365, 240,  'HAS_PRICE'),
('OTHER',     0,   7,  10,  'HAS_PRICE'),
('OTHER',     8,  14,  12,  'HAS_PRICE'),
('OTHER',    15,  30,  15,  'HAS_PRICE'),
('OTHER',    31,  60,  24,  'HAS_PRICE'),
('OTHER',    61, 180, 120,  'HAS_PRICE'),
('OTHER',   181, 365, 240,  'HAS_PRICE'),

-- 无报价缓存策略 (NO_PRICE)
('TOP50',     0, 999,   4,  'NO_PRICE'),   -- 第1次无报价
('TOP50_100', 0, 999,   4,  'NO_PRICE'),
('OTHER',     0, 999,   4,  'NO_PRICE'),

-- 已售完缓存策略 (SOLD_OUT)
('TOP50',     0, 999,  12,  'SOLD_OUT'),
('TOP50_100', 0, 999,  12,  'SOLD_OUT'),
('OTHER',     0, 999,  12,  'SOLD_OUT');


-- ============================================================
-- 二、供应商相关表
-- ============================================================

-- ------------------------------------------------------------
-- 2.1 供应商表 t_supplier (4个供应商)
-- ------------------------------------------------------------
TRUNCATE TABLE t_supplier;
INSERT INTO t_supplier (id, supplier_code, supplier_name, brand_name, status, channels, quote_mode, business_types, quote_type, settlement_currency, settlement_cycle, prepay_settle_mode, prepay_commission_rate, payat_settle_mode, payat_commission_rate, data_source, platform, api_urls, ip_whitelist) VALUES
(1, 'SUP_BOOKING',  'Booking.com',     'Booking',  1, '["APP","H5","WEB"]', 'DOMESTIC', '["PREPAY"]',              'SELL_PRICE', 'CNY', 'MONTHLY',  '月结',    8.00,  NULL,   NULL, 'API', 'Booking.com', '{"search":"https://api.booking.com/v1/search","detail":"https://api.booking.com/v1/detail"}', '10.0.0.0/8'),
(2, 'SUP_XIAKE',    '侠客酒店',         '侠客',     1, '["APP","H5"]',       'DOMESTIC', '["PREPAY","PAY_AT_HOTEL"]','BASE_PRICE', 'CNY', 'BIWEEKLY', '半月结',  6.50,  '预授权', 5.00, 'API', '侠客平台',    '{"search":"https://api.xiake.com/hotel/search","detail":"https://api.xiake.com/hotel/detail"}', NULL),
(3, 'SUP_MEITUAN',  '美团酒店供应',     '美团',     1, '["APP","H5","WEB"]', 'DOMESTIC', '["PREPAY","PAY_AT_HOTEL"]','BASE_PRICE', 'CNY', 'WEEKLY',   '周结',    7.00,  '预授权', 4.50, 'API', '美团开放平台', '{"search":"https://openapi.meituan.com/hotel/v2/search"}', '172.16.0.0/12'),
(4, 'SUP_HUAZHU',   '华住集团直连',     '华住',     1, '["APP","H5"]',       'DOMESTIC', '["PREPAY"]',              'SELL_PRICE', 'CNY', 'MONTHLY',  '月结',    5.50,  NULL,   NULL, 'API', '华住CRS',     '{"search":"https://crs.huazhu.com/api/search","detail":"https://crs.huazhu.com/api/detail"}', '192.168.0.0/16'),
(5, 'SUP_EXPEDIA',  'Expedia集团',      'Expedia',  0, '["APP","H5","WEB"]', 'INTERNATIONAL', '["PREPAY"]',         'SELL_PRICE', 'USD', 'MONTHLY',  '月结(美元)',10.00, NULL,   NULL, 'API', 'Expedia EPS', '{"search":"https://eps.expedia.com/v3/search"}', NULL);


-- ------------------------------------------------------------
-- 2.2 供应商工作时间 t_supplier_work_schedule
-- ------------------------------------------------------------
TRUNCATE TABLE t_supplier_work_schedule;
INSERT INTO t_supplier_work_schedule (supplier_id, schedule_type, is_24x7, day_of_week, start_time, end_time) VALUES
-- Booking.com: 7x24
(1, 'WORK',          1, NULL, NULL, NULL),
(1, 'ORDER_CONFIRM', 1, NULL, NULL, NULL),
-- 侠客: 周一~周五 9:00-21:00, 周末 10:00-18:00
(2, 'WORK', 0, 1, '09:00:00', '21:00:00'),
(2, 'WORK', 0, 2, '09:00:00', '21:00:00'),
(2, 'WORK', 0, 3, '09:00:00', '21:00:00'),
(2, 'WORK', 0, 4, '09:00:00', '21:00:00'),
(2, 'WORK', 0, 5, '09:00:00', '21:00:00'),
(2, 'WORK', 0, 6, '10:00:00', '18:00:00'),
(2, 'WORK', 0, 7, '10:00:00', '18:00:00'),
(2, 'ORDER_CONFIRM', 0, 1, '09:00:00', '18:00:00'),
(2, 'ORDER_CONFIRM', 0, 2, '09:00:00', '18:00:00'),
(2, 'ORDER_CONFIRM', 0, 3, '09:00:00', '18:00:00'),
(2, 'ORDER_CONFIRM', 0, 4, '09:00:00', '18:00:00'),
(2, 'ORDER_CONFIRM', 0, 5, '09:00:00', '18:00:00'),
-- 美团: 7x24
(3, 'WORK',          1, NULL, NULL, NULL),
(3, 'ORDER_CONFIRM', 1, NULL, NULL, NULL),
-- 华住: 7x24
(4, 'WORK',          1, NULL, NULL, NULL),
(4, 'ORDER_CONFIRM', 1, NULL, NULL, NULL);


-- ------------------------------------------------------------
-- 2.3 供应商联系信息 t_supplier_contact
-- ------------------------------------------------------------
TRUNCATE TABLE t_supplier_contact;
INSERT INTO t_supplier_contact (supplier_id, business_contact, business_phone, business_email, supplier_contact, supplier_phone, supplier_email) VALUES
(1, '张伟',   '13800001001', 'zhangwei@ceair.com',   'John Smith',     '+31-20-1234567',  'partner@booking.com'),
(2, '李娜',   '13800001002', 'lina@ceair.com',       '王磊',           '021-62345678',    'support@xiake.com'),
(3, '陈明',   '13800001003', 'chenming@ceair.com',   '赵丽',           '010-87654321',    'hotel-api@meituan.com'),
(4, '刘洋',   '13800001004', 'liuyang@ceair.com',    '孙涛',           '021-33445566',    'crs-support@huazhu.com'),
(5, '王芳',   '13800001005', 'wangfang@ceair.com',   'Sarah Johnson',  '+1-206-555-0100', 'eps@expedia.com');


-- ------------------------------------------------------------
-- 2.4 供应商缓存策略 t_supplier_cache_strategy
-- ------------------------------------------------------------
TRUNCATE TABLE t_supplier_cache_strategy;
INSERT INTO t_supplier_cache_strategy (supplier_id, participate_list_price, detail_price_source) VALUES
(1, 1, 'CACHE_FIRST'),     -- Booking.com: 参与列表报价, 详情优先缓存
(2, 1, 'CACHE_FIRST'),     -- 侠客: 参与列表报价, 详情优先缓存
(3, 1, 'REALTIME'),         -- 美团: 参与列表报价, 详情实时查询
(4, 1, 'REALTIME'),         -- 华住: 参与列表报价, 详情实时查询
(5, 0, 'CACHE_FIRST');      -- Expedia: 不参与列表报价(已下线), 详情优先缓存


-- ============================================================
-- 三、价格策略表
-- ============================================================

-- ------------------------------------------------------------
-- 3.1 全局价格策略 t_price_strategy_global
-- ------------------------------------------------------------
TRUNCATE TABLE t_price_strategy_global;
INSERT INTO t_price_strategy_global (supplier_id, markup_rate, markup_amount) VALUES
(1, 0.00,  0.00),   -- Booking.com: 售卖价模式, 不额外加价
(2, 8.00,  0.00),   -- 侠客: 底价模式, 全局加价 8%
(3, 6.50, 10.00),   -- 美团: 底价模式, 加价 6.5% + 10元/间夜
(4, 0.00,  0.00),   -- 华住: 售卖价模式, 不额外加价
(5, 0.00,  0.00);   -- Expedia: 售卖价模式


-- ------------------------------------------------------------
-- 3.2 特殊价格策略 t_price_strategy_special
-- ------------------------------------------------------------
TRUNCATE TABLE t_price_strategy_special;
INSERT INTO t_price_strategy_special (id, supplier_id, strategy_no, channel, channel_configs) VALUES
-- 侠客: APP渠道减价2%, H5渠道加价1%
(1, 2, 'SP-XIAKE-001', '["APP","H5"]',
 '[{"channel":"APP","markupRate":-2.0,"markupAmount":0},{"channel":"H5","markupRate":1.0,"markupAmount":0}]'),
-- 美团: APP渠道减5元/间夜
(2, 3, 'SP-MEITUAN-001', '["APP"]',
 '[{"channel":"APP","markupRate":0,"markupAmount":-5}]'),
-- 华住: H5渠道加价3%（直连利润较薄，H5端补贴少）
(3, 4, 'SP-HUAZHU-001', '["H5"]',
 '[{"channel":"H5","markupRate":3.0,"markupAmount":0}]');


-- ------------------------------------------------------------
-- 3.3 价格策略操作日志 t_price_strategy_log (示例)
-- ------------------------------------------------------------
TRUNCATE TABLE t_price_strategy_log;
INSERT INTO t_price_strategy_log (supplier_id, strategy_id, strategy_type, log_type, operator, operate_time, description) VALUES
(2, NULL, 'GLOBAL',  'UPDATE', 'admin',   '2026-03-15 10:30:00', '全局加价比例从 10% 调整为 8%，原因：市场竞争力优化'),
(2, 1,    'SPECIAL', 'CREATE', 'admin',   '2026-03-16 14:00:00', '新增APP渠道减价策略：APP减2%，提升移动端竞争力'),
(3, NULL, 'GLOBAL',  'UPDATE', 'admin',   '2026-03-20 09:15:00', '全局加价金额从 15元 调整为 10元/间夜'),
(3, 2,    'SPECIAL', 'CREATE', 'admin',   '2026-03-20 09:30:00', '新增APP渠道减5元策略'),
(4, 3,    'SPECIAL', 'CREATE', 'liuyang', '2026-03-25 16:00:00', '新增华住H5渠道加价3%策略');


-- ============================================================
-- 四、推荐酒店与关键词热度
-- ============================================================

-- ------------------------------------------------------------
-- 4.1 推荐酒店 t_recommended_hotel
-- 酒店ID与截图中的上海酒店对应
-- ------------------------------------------------------------
TRUNCATE TABLE t_recommended_hotel;
INSERT INTO t_recommended_hotel (destination_code, destination_name, destination_type, hotel_id, hotel_name, sort_order) VALUES
-- 上海推荐酒店 (截图中出现的酒店)
('SHA', '上海', 'CITY', 'HTL_SHA_001', '上海龙柏饭店',                 1),
('SHA', '上海', 'CITY', 'HTL_SHA_002', '上海华凯华美达广场酒店',        2),
('SHA', '上海', 'CITY', 'HTL_SHA_003', '上海和平饭店',                 3),
('SHA', '上海', 'CITY', 'HTL_SHA_004', '上海外滩W酒店',                4),
('SHA', '上海', 'CITY', 'HTL_SHA_005', '上海浦东丽思卡尔顿酒店',       5),
('SHA', '上海', 'CITY', 'HTL_SHA_006', '上海迪士尼乐园酒店',           6),
('SHA', '上海', 'CITY', 'HTL_SHA_007', '上海虹桥雅高美爵酒店',         7),
('SHA', '上海', 'CITY', 'HTL_SHA_008', '上海佘山世茂洲际酒店',         8),
-- 北京推荐酒店
('BJS', '北京', 'CITY', 'HTL_BJS_001', '北京王府井文华东方酒店',       1),
('BJS', '北京', 'CITY', 'HTL_BJS_002', '北京国贸大酒店',               2),
('BJS', '北京', 'CITY', 'HTL_BJS_003', '北京嘉里大酒店',               3),
-- 三亚推荐酒店
('SYX', '三亚', 'CITY', 'HTL_SYX_001', '三亚亚特兰蒂斯酒店',          1),
('SYX', '三亚', 'CITY', 'HTL_SYX_002', '三亚海棠湾万丽度假酒店',      2),
-- 昆明推荐酒店
('KMG', '昆明', 'CITY', 'HTL_KMG_001', '昆明洲际酒店',                 1),
-- 西安推荐酒店
('XIY', '西安', 'CITY', 'HTL_XIY_001', '西安W酒店',                    1);


-- ------------------------------------------------------------
-- 4.2 Suggest关键词热度 t_suggest_keyword_heat
-- ------------------------------------------------------------
TRUNCATE TABLE t_suggest_keyword_heat;
INSERT INTO t_suggest_keyword_heat (keyword, keyword_type, ref_id, heat_score, manual_heat) VALUES
-- 酒店名
('上海龙柏饭店',              'HOTEL',       'HTL_SHA_001', 85200,  0),
('上海华凯华美达广场酒店',     'HOTEL',       'HTL_SHA_002', 62300,  0),
('上海和平饭店',              'HOTEL',       'HTL_SHA_003', 95800, 10000),
('上海外滩W酒店',             'HOTEL',       'HTL_SHA_004', 78500,  5000),
('上海浦东丽思卡尔顿酒店',    'HOTEL',       'HTL_SHA_005', 71200,  0),
('上海迪士尼乐园酒店',        'HOTEL',       'HTL_SHA_006', 120500, 20000),
('三亚亚特兰蒂斯酒店',        'HOTEL',       'HTL_SYX_001', 110300, 15000),
-- 品牌
('华美达',                    'BRAND',       'BRAND_RAMADA', 45600, 0),
('万豪',                      'BRAND',       'BRAND_MARRIOTT', 88700, 5000),
('希尔顿',                    'BRAND',       'BRAND_HILTON',  92100, 5000),
('丽思卡尔顿',                'BRAND',       'BRAND_RITZ',    55300, 0),
('全季',                      'BRAND',       'BRAND_JHOTEL',  67800, 0),
-- POI/地标
('南京路步行街',              'POI',          'POI_SHA_NJR',  76300, 0),
('上海迪士尼',                'POI',          'POI_SHA_DSN',  135000, 30000),
('外滩',                      'POI',          'POI_SHA_WTN',  105600, 10000),
('虹桥国际机场',              'POI',          'POI_SHA_HQA',  89200, 0),
('中山公园',                  'POI',          'POI_SHA_ZSG',  34500, 0),
-- 目的地
('上海',                      'DESTINATION',  'SHA',          500000, 100000),
('北京',                      'DESTINATION',  'BJS',          480000, 100000),
('三亚',                      'DESTINATION',  'SYX',          320000, 50000),
('昆明',                      'DESTINATION',  'KMG',          180000, 0),
('西安',                      'DESTINATION',  'XIY',          210000, 0);


-- ============================================================
-- 五、报价快照 (降级兜底测试数据)
-- ============================================================

-- ------------------------------------------------------------
-- 5.1 报价快照 t_price_snapshot
-- 不同快照年龄用于测试三种降级展示模式:
--   < 24h  → NORMAL_REF  (参考价)
--   24-72h → APPROX_REF  (约¥xxx起)
--   > 72h  → NO_PRICE    (不展示价格)
-- ------------------------------------------------------------
TRUNCATE TABLE t_price_snapshot;
INSERT INTO t_price_snapshot (hotel_id, room_type_id, room_type_name, sell_room_id, sell_room_name, supplier_id, check_in_date, check_out_date, sell_price, cost_price, currency, breakfast_count, cancel_rule_desc, guarantee_desc, payment_type, promotion_tags, snapshot_time, data_source) VALUES
-- === 上海龙柏饭店: 快照 6小时前 → NORMAL_REF 测试 ===
('HTL_SHA_001', 'RT_001_01', '豪华大床房',   'SR_001_01_01', '大床 商旅',         '2', '2026-04-05', '2026-04-06', 385.00,  356.48, 'CNY', 1, '2026-04-05 00:00前免费取消', NULL, 1, '["酒店优享","支持积分抵扣"]', DATE_SUB(NOW(), INTERVAL 6 HOUR), 1),
('HTL_SHA_001', 'RT_001_01', '豪华大床房',   'SR_001_01_02', '大床 直营保障',      '2', '2026-04-05', '2026-04-06', 410.00,  379.63, 'CNY', 1, '2026-04-02 00:00前免费取消', NULL, 1, '["酒店优享","支持积分抵扣"]', DATE_SUB(NOW(), INTERVAL 6 HOUR), 1),
('HTL_SHA_001', 'RT_001_02', '高级双床房',   'SR_001_02_01', '双床 含早',          '2', '2026-04-05', '2026-04-06', 420.00,  388.89, 'CNY', 2, '2026-04-04 18:00前免费取消', NULL, 1, '["支持积分抵扣"]',           DATE_SUB(NOW(), INTERVAL 6 HOUR), 1),
('HTL_SHA_001', 'RT_001_03', '行政套房',     'SR_001_03_01', '行政套房 尊享',      '1', '2026-04-05', '2026-04-06', 880.00,  880.00, 'CNY', 2, '不可取消',                   '需信用卡担保', 1, '[]',              DATE_SUB(NOW(), INTERVAL 6 HOUR), 1),

-- === 上海华凯华美达广场酒店: 快照 36小时前 → APPROX_REF 测试 ===
('HTL_SHA_002', 'RT_002_01', '豪华大床房',   'SR_002_01_01', '大床 商旅',          '2', '2026-04-05', '2026-04-06', 598.00,  553.70, 'CNY', 1, '2026-04-05 00:00前免费取消', NULL, 1, '["酒店优享","支持积分抵扣"]', DATE_SUB(NOW(), INTERVAL 36 HOUR), 1),
('HTL_SHA_002', 'RT_002_01', '豪华大床房',   'SR_002_01_02', '大床 直营保障',      '3', '2026-04-05', '2026-04-06', 630.00,  580.00, 'CNY', 1, '2026-04-02 00:00前免费取消', NULL, 1, '["酒店优享","支持积分抵扣"]', DATE_SUB(NOW(), INTERVAL 36 HOUR), 3),
('HTL_SHA_002', 'RT_002_02', '高级双床房',   'SR_002_02_01', '双床 含早',          '2', '2026-04-05', '2026-04-06', 650.00,  601.85, 'CNY', 2, '2026-04-04 00:00前免费取消', NULL, 1, '["支持积分抵扣"]',           DATE_SUB(NOW(), INTERVAL 36 HOUR), 1),
('HTL_SHA_002', 'RT_002_03', '豪华套房',     'SR_002_03_01', '套房 商务',          '1', '2026-04-05', '2026-04-06', 1280.00, 1280.00,'CNY', 2, '不可取消',                   '需信用卡担保', 1, '[]',              DATE_SUB(NOW(), INTERVAL 36 HOUR), 1),

-- === 上海和平饭店: 快照 96小时前 → NO_PRICE 测试 ===
('HTL_SHA_003', 'RT_003_01', '经典大床房',   'SR_003_01_01', '大床 含早',          '1', '2026-04-05', '2026-04-06', 3382.00, 3382.00,'CNY', 2, '入住前3天可免费取消',         NULL, 1, '[]',                        DATE_SUB(NOW(), INTERVAL 96 HOUR), 2),
('HTL_SHA_003', 'RT_003_02', '豪华江景套房', 'SR_003_02_01', '江景套房 尊享',      '1', '2026-04-05', '2026-04-06', 6800.00, 6800.00,'CNY', 2, '不可取消',                   '需信用卡担保', 1, '[]',              DATE_SUB(NOW(), INTERVAL 96 HOUR), 2),

-- === 上海外滩W酒店: 快照 12小时前 → NORMAL_REF, 多日期 ===
('HTL_SHA_004', 'RT_004_01', '标准特大床房', 'SR_004_01_01', '特大床 城景',        '3', '2026-04-05', '2026-04-06', 1580.00, 1456.00,'CNY', 0, '2026-04-03 18:00前免费取消', NULL, 1, '["支持积分抵扣"]',           DATE_SUB(NOW(), INTERVAL 12 HOUR), 1),
('HTL_SHA_004', 'RT_004_01', '标准特大床房', 'SR_004_01_02', '特大床 含早',        '1', '2026-04-05', '2026-04-06', 1680.00, 1680.00,'CNY', 2, '2026-04-04 00:00前免费取消', NULL, 1, '[]',                        DATE_SUB(NOW(), INTERVAL 12 HOUR), 1),
('HTL_SHA_004', 'RT_004_02', 'W套房',        'SR_004_02_01', '套房 江景',          '1', '2026-04-05', '2026-04-06', 3500.00, 3500.00,'CNY', 2, '不可取消',                   '需信用卡担保', 1, '[]',              DATE_SUB(NOW(), INTERVAL 12 HOUR), 1),

-- === 不同入住日期的快照 (04-07 ~ 04-08) ===
('HTL_SHA_001', 'RT_001_01', '豪华大床房',   'SR_001_01_03', '大床 特惠',          '3', '2026-04-07', '2026-04-08', 358.00,  325.00, 'CNY', 1, '2026-04-06 00:00前免费取消', NULL, 1, '["限时特惠","支持积分抵扣"]', DATE_SUB(NOW(), INTERVAL 3 HOUR), 1),
('HTL_SHA_002', 'RT_002_01', '豪华大床房',   'SR_002_01_03', '大床 周末特价',      '2', '2026-04-07', '2026-04-08', 558.00,  516.67, 'CNY', 1, '2026-04-06 18:00前免费取消', NULL, 1, '["周末特惠"]',               DATE_SUB(NOW(), INTERVAL 3 HOUR), 1),

-- === 上海迪士尼乐园酒店: 现付房型, 快照 18小时前 ===
('HTL_SHA_006', 'RT_006_01', '乐园景观房',   'SR_006_01_01', '乐园景观 大床',      '4', '2026-04-05', '2026-04-06', 2680.00, 2680.00,'CNY', 2, '入住前1天可取消',             NULL, 2, '["迪士尼专享"]',             DATE_SUB(NOW(), INTERVAL 18 HOUR), 1),
('HTL_SHA_006', 'RT_006_02', '城堡景观房',   'SR_006_02_01', '城堡景观 双床',      '4', '2026-04-05', '2026-04-06', 3280.00, 3280.00,'CNY', 2, '不可取消',                   '需信用卡担保', 2, '["迪士尼专享"]',  DATE_SUB(NOW(), INTERVAL 18 HOUR), 1),

-- === 北京酒店快照 ===
('HTL_BJS_001', 'RT_BJS_01', '文华套房',     'SR_BJS_01_01', '文华套房 含双早',    '1', '2026-04-05', '2026-04-06', 4200.00, 4200.00,'CNY', 2, '入住前7天可免费取消',         NULL, 1, '[]',                        DATE_SUB(NOW(), INTERVAL 8 HOUR), 1),
('HTL_BJS_002', 'RT_BJS_02', '豪华大床房',   'SR_BJS_02_01', '大床 商务',          '3', '2026-04-05', '2026-04-06', 920.00,  848.00, 'CNY', 1, '2026-04-04 00:00前免费取消', NULL, 1, '["支持积分抵扣"]',           DATE_SUB(NOW(), INTERVAL 8 HOUR), 1),

-- === 三亚酒店快照 ===
('HTL_SYX_001', 'RT_SYX_01', '海景大床房',   'SR_SYX_01_01', '海景 含双早',        '2', '2026-04-05', '2026-04-06', 2580.00, 2388.89,'CNY', 2, '入住前3天可免费取消',         NULL, 1, '["支持积分抵扣"]',           DATE_SUB(NOW(), INTERVAL 10 HOUR), 1),
('HTL_SYX_001', 'RT_SYX_02', '水底套房',     'SR_SYX_02_01', '水族馆景观套房',     '2', '2026-04-05', '2026-04-06', 8800.00, 8148.15,'CNY', 2, '不可取消',                   '需信用卡担保', 1, '[]',              DATE_SUB(NOW(), INTERVAL 10 HOUR), 1);


-- ============================================================
-- 六、无报价/售完缓存记录
-- ============================================================

-- ------------------------------------------------------------
-- 6.1 无报价缓存追踪 t_cache_no_price_record
-- ------------------------------------------------------------
TRUNCATE TABLE t_cache_no_price_record;
INSERT INTO t_cache_no_price_record (supplier_id, hotel_id, check_in_date, check_out_date, no_price_count, sold_out_count, last_query_time, cache_expire_time) VALUES
-- Expedia已下线, 部分酒店无报价
(5, 'HTL_SHA_001', '2026-04-05', '2026-04-06', 3, 0, '2026-03-28 14:00:00', '2026-04-07 14:00:00'),
(5, 'HTL_SHA_002', '2026-04-05', '2026-04-06', 3, 0, '2026-03-28 14:00:00', '2026-04-07 14:00:00'),
-- 华住某房型售完
(4, 'HTL_SHA_007', '2026-04-05', '2026-04-06', 0, 2, '2026-03-29 08:00:00', '2026-03-29 20:00:00'),
-- 侠客某酒店首次无报价
(2, 'HTL_BJS_003', '2026-04-05', '2026-04-06', 1, 0, '2026-03-29 10:00:00', '2026-03-29 14:00:00'),
-- 美团某酒店连续2次无报价
(3, 'HTL_KMG_001', '2026-04-05', '2026-04-06', 2, 0, '2026-03-29 06:00:00', '2026-03-29 18:00:00');


-- ============================================================
-- 七、搜索与行为日志
-- ============================================================

-- ------------------------------------------------------------
-- 7.1 用户搜索日志 t_search_log (近3天的搜索记录)
-- ------------------------------------------------------------
TRUNCATE TABLE t_search_log;
INSERT INTO t_search_log (session_id, search_time, destination_code, destination_name, current_location, keyword, check_in_date, check_out_date, star_level, price_range, room_count, adult_count, child_count, child_ages) VALUES
('sess_20260327_001', '2026-03-27 09:15:32', 'SHA', '上海', '上海市浦东新区', NULL,        '2026-04-05', '2026-04-06', NULL,      NULL,       1, 2, 0, NULL),
('sess_20260327_002', '2026-03-27 10:22:18', 'SHA', '上海', '上海市静安区',   '迪士尼',    '2026-04-05', '2026-04-07', NULL,      NULL,       1, 2, 1, '[5]'),
('sess_20260327_003', '2026-03-27 11:45:00', 'BJS', '北京', '北京市朝阳区',   '国贸',      '2026-04-08', '2026-04-10', '["5"]',   '500-1000', 1, 2, 0, NULL),
('sess_20260327_004', '2026-03-27 14:30:55', 'SHA', '上海', NULL,             '龙柏饭店',  '2026-04-05', '2026-04-06', NULL,      NULL,       1, 2, 0, NULL),
('sess_20260328_001', '2026-03-28 08:05:12', 'SYX', '三亚', '上海市徐汇区',   NULL,        '2026-04-10', '2026-04-13', '["4","5"]','0-2000',  2, 4, 2, '[3,7]'),
('sess_20260328_002', '2026-03-28 09:18:44', 'SHA', '上海', '上海市长宁区',   '虹桥',      '2026-04-05', '2026-04-06', NULL,      '0-500',    1, 2, 0, NULL),
('sess_20260328_003', '2026-03-28 10:33:21', 'KMG', '昆明', '昆明市官渡区',   NULL,        '2026-04-06', '2026-04-08', NULL,      NULL,       1, 2, 0, NULL),
('sess_20260328_004', '2026-03-28 15:47:09', 'SHA', '上海', '上海市黄浦区',   '外滩',      '2026-04-05', '2026-04-06', '["5"]',   NULL,       1, 2, 0, NULL),
('sess_20260329_001', '2026-03-29 07:20:33', 'SHA', '上海', '上海市浦东新区', '和平饭店',  '2026-04-05', '2026-04-06', NULL,      NULL,       1, 2, 0, NULL),
('sess_20260329_002', '2026-03-29 08:55:16', 'XIY', '西安', '西安市碑林区',   NULL,        '2026-04-12', '2026-04-14', NULL,      '300-800',  1, 2, 0, NULL),
('sess_20260329_003', '2026-03-29 10:12:48', 'SHA', '上海', '上海市闵行区',   '华美达',    '2026-04-05', '2026-04-06', '["4"]',   NULL,       1, 2, 0, NULL),
('sess_20260329_004', '2026-03-29 11:30:00', 'BJS', '北京', NULL,             '王府井',    '2026-04-05', '2026-04-07', '["5"]',   NULL,       1, 2, 0, NULL);


-- ------------------------------------------------------------
-- 7.2 酒店点击日志 t_hotel_click_log
-- ------------------------------------------------------------
TRUNCATE TABLE t_hotel_click_log;
INSERT INTO t_hotel_click_log (click_time, session_id, hotel_id, hotel_name, hotel_rank, check_in_date, check_out_date, star_level, rating, review_count, has_image, has_price, is_available, starting_price, room_count, adult_count, child_count, child_ages) VALUES
('2026-03-27 09:16:15', 'sess_20260327_001', 'HTL_SHA_001', '上海龙柏饭店',             1, '2026-04-05', '2026-04-06', '3', 4.55, 2836, 1, 1, 1,  385.00, 1, 2, 0, NULL),
('2026-03-27 09:18:42', 'sess_20260327_001', 'HTL_SHA_002', '上海华凯华美达广场酒店',    2, '2026-04-05', '2026-04-06', '4', 4.35, 1520, 1, 1, 1,  598.00, 1, 2, 0, NULL),
('2026-03-27 10:25:00', 'sess_20260327_002', 'HTL_SHA_006', '上海迪士尼乐园酒店',        1, '2026-04-05', '2026-04-07', '5', 4.70, 5620, 1, 1, 1, 2680.00, 1, 2, 1, '[5]'),
('2026-03-27 14:32:10', 'sess_20260327_004', 'HTL_SHA_001', '上海龙柏饭店',              1, '2026-04-05', '2026-04-06', '3', 4.55, 2836, 1, 1, 1,  385.00, 1, 2, 0, NULL),
('2026-03-28 08:10:30', 'sess_20260328_001', 'HTL_SYX_001', '三亚亚特兰蒂斯酒店',       1, '2026-04-10', '2026-04-13', '5', 4.80, 8920, 1, 1, 1, 2580.00, 2, 4, 2, '[3,7]'),
('2026-03-28 15:48:55', 'sess_20260328_004', 'HTL_SHA_003', '上海和平饭店',              1, '2026-04-05', '2026-04-06', '5', 4.70, 3200, 1, 1, 1, 3382.00, 1, 2, 0, NULL),
('2026-03-28 15:50:20', 'sess_20260328_004', 'HTL_SHA_004', '上海外滩W酒店',             2, '2026-04-05', '2026-04-06', '5', 4.60, 2100, 1, 1, 1, 1580.00, 1, 2, 0, NULL),
('2026-03-29 07:22:18', 'sess_20260329_001', 'HTL_SHA_003', '上海和平饭店',              1, '2026-04-05', '2026-04-06', '5', 4.70, 3200, 1, 1, 1, 3382.00, 1, 2, 0, NULL),
('2026-03-29 10:14:30', 'sess_20260329_003', 'HTL_SHA_002', '上海华凯华美达广场酒店',    1, '2026-04-05', '2026-04-06', '4', 4.35, 1520, 1, 1, 1,  598.00, 1, 2, 0, NULL);


-- ------------------------------------------------------------
-- 7.3 预订点击日志 t_booking_click_log
-- ------------------------------------------------------------
TRUNCATE TABLE t_booking_click_log;
INSERT INTO t_booking_click_log (click_time, session_id, hotel_id, hotel_name, total_room_types, room_type_rank, total_quotes, quote_rank, price, is_room_lowest, is_hotel_lowest, room_name, sell_room_name, breakfast) VALUES
('2026-03-27 09:20:35', 'sess_20260327_001', 'HTL_SHA_001', '上海龙柏饭店',          3, 1, 2, 1, 385.00,  1, 1, '豪华大床房', '大床 商旅',      '含早'),
('2026-03-27 10:28:15', 'sess_20260327_002', 'HTL_SHA_006', '上海迪士尼乐园酒店',    2, 1, 1, 1, 2680.00, 1, 1, '乐园景观房', '乐园景观 大床',  '含双早'),
('2026-03-28 08:15:50', 'sess_20260328_001', 'HTL_SYX_001', '三亚亚特兰蒂斯酒店',   2, 1, 1, 1, 2580.00, 1, 1, '海景大床房', '海景 含双早',    '含双早'),
('2026-03-28 15:52:40', 'sess_20260328_004', 'HTL_SHA_003', '上海和平饭店',          2, 1, 1, 1, 3382.00, 1, 1, '经典大床房', '大床 含早',      '含双早'),
('2026-03-29 07:25:00', 'sess_20260329_001', 'HTL_SHA_003', '上海和平饭店',          2, 1, 1, 1, 3382.00, 1, 1, '经典大床房', '大床 含早',      '含双早'),
('2026-03-29 10:18:22', 'sess_20260329_003', 'HTL_SHA_002', '上海华凯华美达广场酒店', 3, 1, 2, 1, 598.00,  1, 1, '豪华大床房', '大床 商旅',      '含早');


-- ============================================================
-- 八、统计数据
-- ============================================================

-- ------------------------------------------------------------
-- 8.1 页面埋点统计 t_page_statistics (近7天)
-- ------------------------------------------------------------
TRUNCATE TABLE t_page_statistics;
INSERT INTO t_page_statistics (stat_date, page_type, dimension_type, dimension_value, pv, uv) VALUES
-- 首页
('2026-03-23', 'HOME',    'TOTAL', 'ALL', 15230, 8920),
('2026-03-24', 'HOME',    'TOTAL', 'ALL', 14850, 8650),
('2026-03-25', 'HOME',    'TOTAL', 'ALL', 16100, 9200),
('2026-03-26', 'HOME',    'TOTAL', 'ALL', 18500, 10300),
('2026-03-27', 'HOME',    'TOTAL', 'ALL', 17200, 9800),
('2026-03-28', 'HOME',    'TOTAL', 'ALL', 19800, 11500),
('2026-03-29', 'HOME',    'TOTAL', 'ALL', 12300, 7100),
-- 列表页
('2026-03-23', 'LIST',    'TOTAL', 'ALL', 9850,  5600),
('2026-03-24', 'LIST',    'TOTAL', 'ALL', 9520,  5430),
('2026-03-25', 'LIST',    'TOTAL', 'ALL', 10300, 5950),
('2026-03-26', 'LIST',    'TOTAL', 'ALL', 12100, 6800),
('2026-03-27', 'LIST',    'TOTAL', 'ALL', 11200, 6350),
('2026-03-28', 'LIST',    'TOTAL', 'ALL', 13500, 7800),
('2026-03-29', 'LIST',    'TOTAL', 'ALL', 8200,  4650),
-- 详情页
('2026-03-23', 'DETAIL',  'TOTAL', 'ALL', 6200,  3800),
('2026-03-24', 'DETAIL',  'TOTAL', 'ALL', 5980,  3650),
('2026-03-25', 'DETAIL',  'TOTAL', 'ALL', 6500,  4020),
('2026-03-26', 'DETAIL',  'TOTAL', 'ALL', 7800,  4580),
('2026-03-27', 'DETAIL',  'TOTAL', 'ALL', 7100,  4200),
('2026-03-28', 'DETAIL',  'TOTAL', 'ALL', 8600,  5100),
('2026-03-29', 'DETAIL',  'TOTAL', 'ALL', 5200,  3050),
-- 预订页
('2026-03-23', 'BOOKING', 'TOTAL', 'ALL', 1850,  1200),
('2026-03-24', 'BOOKING', 'TOTAL', 'ALL', 1780,  1150),
('2026-03-25', 'BOOKING', 'TOTAL', 'ALL', 2050,  1350),
('2026-03-26', 'BOOKING', 'TOTAL', 'ALL', 2400,  1580),
('2026-03-27', 'BOOKING', 'TOTAL', 'ALL', 2200,  1420),
('2026-03-28', 'BOOKING', 'TOTAL', 'ALL', 2800,  1850),
('2026-03-29', 'BOOKING', 'TOTAL', 'ALL', 1500,  980),
-- 按城市维度 (最近1天示例)
('2026-03-28', 'LIST', 'CITY', '上海', 5400, 3120),
('2026-03-28', 'LIST', 'CITY', '北京', 2800, 1650),
('2026-03-28', 'LIST', 'CITY', '三亚', 2100, 1230),
('2026-03-28', 'LIST', 'CITY', '昆明',  850,  480),
('2026-03-28', 'LIST', 'CITY', '西安',  750,  420),
('2026-03-28', 'LIST', 'CITY', '广州',  600,  350);


-- ------------------------------------------------------------
-- 8.2 预订拦截统计 t_booking_intercept_stat (近3天)
-- ------------------------------------------------------------
TRUNCATE TABLE t_booking_intercept_stat;
INSERT INTO t_booking_intercept_stat (stat_date, hotel_id, hotel_name, supplier_id, supplier_name, click_count, success_count, fail_count, price_change_count) VALUES
-- 2026-03-27
('2026-03-27', 'HTL_SHA_001', '上海龙柏饭店',           2, '侠客酒店',     45, 38, 3, 4),
('2026-03-27', 'HTL_SHA_002', '上海华凯华美达广场酒店',  2, '侠客酒店',     32, 27, 2, 3),
('2026-03-27', 'HTL_SHA_002', '上海华凯华美达广场酒店',  3, '美团酒店供应', 28, 25, 1, 2),
('2026-03-27', 'HTL_SHA_003', '上海和平饭店',           1, 'Booking.com',  18, 16, 1, 1),
('2026-03-27', 'HTL_SHA_006', '上海迪士尼乐园酒店',     4, '华住集团直连', 55, 48, 2, 5),
('2026-03-27', 'HTL_SYX_001', '三亚亚特兰蒂斯酒店',    2, '侠客酒店',     22, 19, 1, 2),
-- 2026-03-28
('2026-03-28', 'HTL_SHA_001', '上海龙柏饭店',           2, '侠客酒店',     52, 44, 4, 4),
('2026-03-28', 'HTL_SHA_002', '上海华凯华美达广场酒店',  2, '侠客酒店',     38, 33, 2, 3),
('2026-03-28', 'HTL_SHA_002', '上海华凯华美达广场酒店',  3, '美团酒店供应', 35, 31, 1, 3),
('2026-03-28', 'HTL_SHA_003', '上海和平饭店',           1, 'Booking.com',  21, 18, 2, 1),
('2026-03-28', 'HTL_SHA_004', '上海外滩W酒店',          1, 'Booking.com',  15, 13, 1, 1),
('2026-03-28', 'HTL_SHA_004', '上海外滩W酒店',          3, '美团酒店供应', 12, 10, 1, 1),
('2026-03-28', 'HTL_SHA_006', '上海迪士尼乐园酒店',     4, '华住集团直连', 68, 60, 3, 5),
('2026-03-28', 'HTL_SYX_001', '三亚亚特兰蒂斯酒店',    2, '侠客酒店',     28, 24, 2, 2),
-- 2026-03-29 (截至中午)
('2026-03-29', 'HTL_SHA_001', '上海龙柏饭店',           2, '侠客酒店',     25, 21, 2, 2),
('2026-03-29', 'HTL_SHA_002', '上海华凯华美达广场酒店',  2, '侠客酒店',     18, 15, 1, 2),
('2026-03-29', 'HTL_SHA_003', '上海和平饭店',           1, 'Booking.com',  10,  8, 1, 1),
('2026-03-29', 'HTL_SHA_006', '上海迪士尼乐园酒店',     4, '华住集团直连', 32, 28, 2, 2);


-- ============================================================
-- 九、供应商操作日志
-- ============================================================

TRUNCATE TABLE t_supplier_operation_log;
INSERT INTO t_supplier_operation_log (supplier_id, supplier_name, operator, operate_time, operate_type, operate_target, operate_content) VALUES
(1, 'Booking.com',     'admin',   '2026-01-15 10:00:00', 'CREATE', '供应商信息',     '创建Booking.com供应商, 渠道: APP/H5/WEB, 报价类型: SELL_PRICE'),
(2, '侠客酒店',         'admin',   '2026-01-20 14:30:00', 'CREATE', '供应商信息',     '创建侠客酒店供应商, 渠道: APP/H5, 报价类型: BASE_PRICE, 加价8%'),
(3, '美团酒店供应',     'admin',   '2026-02-01 09:00:00', 'CREATE', '供应商信息',     '创建美团酒店供应商, 渠道: APP/H5/WEB'),
(4, '华住集团直连',     'liuyang', '2026-02-10 11:15:00', 'CREATE', '供应商信息',     '创建华住集团直连供应商, CRS直连接入'),
(5, 'Expedia集团',      'admin',   '2026-02-15 16:00:00', 'CREATE', '供应商信息',     '创建Expedia供应商, 国际酒店源'),
(5, 'Expedia集团',      'admin',   '2026-03-20 09:30:00', 'OFFLINE','供应商状态',     '下线Expedia供应商, 原因: 国际业务暂缓, 合同到期未续签'),
(2, '侠客酒店',         'admin',   '2026-03-15 10:30:00', 'EDIT',   '价格策略-全局', '全局加价比例从10%调整为8%, 原因: 市场竞争力优化'),
(3, '美团酒店供应',     'admin',   '2026-03-20 09:15:00', 'EDIT',   '价格策略-全局', '全局加价金额从15元调整为10元/间夜'),
(1, 'Booking.com',     'zhangwei','2026-03-25 14:00:00', 'EDIT',   '缓存策略',       '详情页报价源切换为CACHE_FIRST, 减少API调用频率'),
(4, '华住集团直连',     'liuyang', '2026-03-28 10:00:00', 'EDIT',   '供应商信息',     '更新华住API接口地址, CRS v2.0升级');


-- ============================================================
-- 完成
-- ============================================================

SET FOREIGN_KEY_CHECKS = 1;

-- 数据统计概览 (执行以下查询验证数据完整性)
/*
SELECT 't_city' AS table_name, COUNT(*) AS row_count FROM t_city
UNION ALL SELECT 't_currency', COUNT(*) FROM t_currency
UNION ALL SELECT 't_cache_ttl_config', COUNT(*) FROM t_cache_ttl_config
UNION ALL SELECT 't_supplier', COUNT(*) FROM t_supplier
UNION ALL SELECT 't_supplier_work_schedule', COUNT(*) FROM t_supplier_work_schedule
UNION ALL SELECT 't_supplier_contact', COUNT(*) FROM t_supplier_contact
UNION ALL SELECT 't_supplier_cache_strategy', COUNT(*) FROM t_supplier_cache_strategy
UNION ALL SELECT 't_price_strategy_global', COUNT(*) FROM t_price_strategy_global
UNION ALL SELECT 't_price_strategy_special', COUNT(*) FROM t_price_strategy_special
UNION ALL SELECT 't_price_strategy_log', COUNT(*) FROM t_price_strategy_log
UNION ALL SELECT 't_recommended_hotel', COUNT(*) FROM t_recommended_hotel
UNION ALL SELECT 't_suggest_keyword_heat', COUNT(*) FROM t_suggest_keyword_heat
UNION ALL SELECT 't_price_snapshot', COUNT(*) FROM t_price_snapshot
UNION ALL SELECT 't_cache_no_price_record', COUNT(*) FROM t_cache_no_price_record
UNION ALL SELECT 't_search_log', COUNT(*) FROM t_search_log
UNION ALL SELECT 't_hotel_click_log', COUNT(*) FROM t_hotel_click_log
UNION ALL SELECT 't_booking_click_log', COUNT(*) FROM t_booking_click_log
UNION ALL SELECT 't_page_statistics', COUNT(*) FROM t_page_statistics
UNION ALL SELECT 't_booking_intercept_stat', COUNT(*) FROM t_booking_intercept_stat
UNION ALL SELECT 't_supplier_operation_log', COUNT(*) FROM t_supplier_operation_log
ORDER BY table_name;
*/
