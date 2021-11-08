CREATE TABLE `demo_freemarker_template` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `code` varchar(4) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '模版名称',
    `value` text COLLATE utf8_unicode_ci COMMENT '模版样式Html',
    PRIMARY KEY (`id`)
)  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

# 模拟数据
INSERT INTO demo_freemarker_template(`code`, `value`) VALUES ('T001', '<html><head><title>Welcome!</title></head><body style=\"margin:0px\"><h1>Welcome ${user}!</h1><img src=\"${info.url}\"></body></html>');