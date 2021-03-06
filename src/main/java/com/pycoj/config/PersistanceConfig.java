package com.pycoj.config;

import com.pycoj.entity.program.Program;
import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.io.FileNotFoundException;
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
    @Autowired @Qualifier("cProgram") Program cProgram;
    @Autowired @Qualifier("javaProgram") Program javaProgram;
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
    @Bean("program")
    public File program(){
        return new File(properties().getProperty("program"));
    }

    @Bean("matchProgramDir")
    public File matchProgramDir(){
        return new File(properties().getProperty("matchProgram"));
    }

    /**
     * 题目的输入输出用例所在的文件夹，使用时还需要添加题目的id才能到达特定题目的输入输出用例所在的文件夹
     * @return
     */
    @Bean("questionDir")
    public File questionDir(){
        return new File(properties().getProperty("question"));
    }

    @Bean("matchQuestionDir")
    public File matchQuestionDir(){
        return new File(properties().getProperty("matchQuestion"));
    }

    @Bean("commentPic")
    public File commentPic(){
        return new File(properties().getProperty("commentPic"));
    }

    @Bean("javaRunningFile")
    public File javaRunningFile() throws FileNotFoundException {
        return new File(program(),"Main1.java");
    }

    @Bean("cRunningFile")
    public File cRunningFile() throws FileNotFoundException {
        return new File(program(),"main1.c");
    }

    @Bean("programs")
    public Program[] programs(){
        return new Program[]{cProgram,javaProgram};
    }
}
