package com.baodanyun.websocket.springConfig;

import com.alibaba.druid.pool.DruidDataSource;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.PropertiesUtil;
import com.mongodb.Mongo;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jms.Destination;
import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by liaowuhen on 2016/11/2.
 */
@Component
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages =
        {"com.baodanyun.websocket.service",
                "com.baodanyun.cc",
                "com.baodanyun.websocket.alarm",
                "com.baodanyun.websocket.controller",
                "com.baodanyun.websocket.node",
                "com.baodanyun.websocket.listener",
                "com.baodanyun.robot"
        })
//加载资源文件
@PropertySource({"classpath:config.properties"})
public class SpringConfig {
    protected static Logger logger = LoggerFactory.getLogger(SpringConfig.class);
    Map<String, String> map = PropertiesUtil.get(this.getClass().getClassLoader(), "config.properties");

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public DataSource dataSource() {
        logger.info("DataSource");
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(map.get("jdbc_driverClassName"));
        dataSource.setUrl(map.get("jdbc_url"));
        dataSource.setUsername(map.get("jdbc_username"));
        dataSource.setPassword(map.get("jdbc_password"));
        dataSource.setMaxActive(50);
        dataSource.setInitialSize(5);
        dataSource.setMaxWait(6000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeoutMillis(1800);
        dataSource.setLogAbandoned(true);
        //dataSource.setFilters("mergeStat");
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager txManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        logger.info("sqlSessionFactory");
        TransactionFactory transactionFactory = new
                SpringManagedTransactionFactory();

        Environment environment =
                new Environment("development", transactionFactory, dataSource);

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration(environment);

        SqlSessionFactory sessionFactory =
                new SqlSessionFactoryBuilder().build(configuration);
        return sessionFactory;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactory(sqlSessionFactory);
        mapperScannerConfigurer.setBasePackage("com/baodanyun/websocket/dao");
        return mapperScannerConfigurer;
    }


    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory aMQF = new ActiveMQConnectionFactory();
        aMQF.setBrokerURL(map.get("broker.url"));
        return aMQF;
    }

    @Bean
    public PooledConnectionFactory pooledConnectionFactory(@Qualifier("activeMQConnectionFactory") ActiveMQConnectionFactory activeMQConnectionFactory) {
        PooledConnectionFactory pCF = new PooledConnectionFactory();
        pCF.setConnectionFactory(activeMQConnectionFactory);
        return pCF;
    }

    @Bean
    public JmsTemplate jmsTemplate(@Qualifier("pooledConnectionFactory") PooledConnectionFactory pooledConnectionFactory) {

        JmsTemplate jtl = new JmsTemplate();
        jtl.setConnectionFactory(pooledConnectionFactory);
        return jtl;
    }

    @Bean
    public Destination destination() {
        Destination de = new ActiveMQQueue(map.get("env.queue.eventCenterDestination"));

        return de;
    }

    @Bean
    public SockIOPool getSockIOPool() {
        String[] servers = {Config.cacheurl + ":" + Config.cacheport};
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(servers);
        pool.setFailover(false);
        pool.setInitConn(10);
        pool.setMinConn(5);
        pool.setMaxConn(250);
        pool.setMaintSleep(30);
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setAliveCheck(true);
        pool.initialize();
        return pool;
    }

    @Bean
    public MemCachedClient getMemCachedClient() {
        MemCachedClient memCachedClient = new MemCachedClient();
        return memCachedClient;
    }

    /**
     * mongoDB配置
     *
     * @return
     * @throws Exception
     */
    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        String host = map.get("mongodb.host");
        String port = map.get("mongodb.port");

        String databaseName = map.get("mongodb.dbname"); //数据库名
        String username = map.get("mongodb.username"); //用户名
        char[] password = map.get("mongodb.password").toCharArray(); //密码


        Mongo mongo = new Mongo(host, Integer.valueOf(port));
        UserCredentials credentials = new UserCredentials(username, map.get("mongodb.password"));
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo, databaseName, credentials);

        return mongoDbFactory;
    }

    @Bean
    public MongoTemplate mongoTemplate(@Qualifier("mongoDbFactory") MongoDbFactory mongoDbFactory) {
        MongoTemplate template = new MongoTemplate(mongoDbFactory);
        return template;
    }

}
