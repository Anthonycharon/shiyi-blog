package com.shiyi;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

/**
 * @author blue
 * @description:
 * @date 2021/7/19 10:51
 */
public class AutoGenerateCode {

    public static void main(String[] args) {
        // 需要构建一个 代码自动生成器 对象
        AutoGenerator mpg = new AutoGenerator();

        // 设置全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath+"/src/main/java");
        gc.setAuthor("blue");
        gc.setOpen(false);
        gc.setFileOverride(false); // 是否覆盖
        gc.setServiceName("%sService"); // 去Service的I前缀
        gc.setIdType(IdType.ID_WORKER);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://121.41.101.167:3306/zw_blog?serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("queql");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 包的配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.isblog");
        pc.setEntity("entity");
        pc.setService("service");
        mpg.setPackageInfo(pc);

        // 数据库表配置
        StrategyConfig sc = new StrategyConfig();
        sc.setInclude(new String[]{"b_feed_back"}); // 设置要映射的表名
        sc.setNaming(NamingStrategy.underline_to_camel); //设置驼峰命名
        sc.setColumnNaming(NamingStrategy.underline_to_camel);
        sc.setEntityLombokModel(true); // 自动lombok；
        sc.setLogicDeleteFieldName("deleted");
        sc.setTablePrefix("b_");
        // 自动填充配置
        TableFill gmtCreate = new TableFill("create_time", FieldFill.INSERT);
        TableFill gmtModified = new TableFill("update_time",
                FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(gmtCreate);
        tableFills.add(gmtModified);
        sc.setTableFillList(tableFills);
        // 乐观锁
        sc.setVersionFieldName("version");
        sc.setRestControllerStyle(true);
        sc.setControllerMappingHyphenStyle(true); //localhost:8080/hello_id_2
        mpg.setStrategy(sc);
        mpg.execute(); //执行

    }
}