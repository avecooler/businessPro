package com.br;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.br.shopping"} , sqlSessionTemplateRef = "serviceSqlSessionTemplate")
public class ServiceDatabase {

    @Bean(name = "serviceDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.second") // prefix值必须是application.properteis中对应属性的前缀
    public DataSource userDataSource() {
        return DruidDataSourceBuilder.create().build();
    }
    @Bean(name = "serviceSqlSessionFactory")
    public SqlSessionFactory userSqlSessionFactory(@Qualifier("serviceDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:com/br/shopping/dao/mapping/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Bean(name= "serviceSqlSessionTemplate")
    public SqlSessionTemplate userSqlSessionTemplate(@Qualifier("serviceSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory); // 使用上面配置的Factory
        return template;
    }
}
