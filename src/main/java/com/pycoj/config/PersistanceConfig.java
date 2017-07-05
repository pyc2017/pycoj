package com.pycoj.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Heyman on 2017/5/1.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.pycoj.dao"})
@MapperScan(basePackages = {"com.pycoj.dao"})
public class PersistanceConfig {
    @Bean("jdbcProperties")
    public Properties properties() {
        Properties p=new Properties();
        try {
            p.load(PersistanceConfig.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (IOException e) {
        }
        return p;
    }

    @Bean
    public BasicDataSource dataSource(){
        BasicDataSource dataSource=new BasicDataSource();
        dataSource.setUsername(properties().getProperty("username"));
        dataSource.setPassword(properties().getProperty("password"));
        dataSource.setDriverClassName(properties().getProperty("driver"));
        dataSource.setUrl(properties().getProperty("url"));
        return dataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        bean.setMapperLocations(new
                PathMatchingResourcePatternResolver().getResources("classpath:com/pycoj/dao/*.xml"));
        bean.setDataSource(dataSource());
//        bean.setTypeAliasesPackage("com.bit.projectmanagement.entity");
        bean.setConfigLocation(new
                PathMatchingResourcePatternResolver().getResource("classpath:Mybatis-config.xml"));
        return bean;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     *
     * @return 存储用户上传的代码的文件夹
     */
    @Bean("filePrefix")
    public String filePrefix(){
        return properties().getProperty("question");
    }

    /**
     * 题目的输入输出用例所在的文件夹，使用时还需要添加题目的id才能到达特定题目的输入输出用例所在的文件夹
     * @return
     */
    @Bean("inAndOut")
    public String inAndOut(){
        return properties().getProperty("submit");
    }
}
