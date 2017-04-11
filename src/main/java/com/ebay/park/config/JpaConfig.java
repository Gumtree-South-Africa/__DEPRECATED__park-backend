/*
 * Copyright eBay, 2014
 */
package com.ebay.park.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configures needed classes for jpa data access.
 * 
 * @author marcos.lambolay
 */

@Configuration
@EnableJpaRepositories(basePackages = "com.ebay.park.db.dao")
@EnableTransactionManagement
public class JpaConfig {

	private static final boolean YES = true;
	private static final boolean NO = false;
	private static final String KEEP_ALIVE_QUERY = "SELECT 1 as test_keep_alive";

	// connection
	@Value("${db.driver}")
	private String driverClassName;
	@Value("${db.url}")
	private String url;
	@Value("${db.user}")
	private String user;
	@Value("${db.pass}")
	private String pass;
	@Value("${db.pkgs_scan}")
	private String packagesToScan;

	// pool
	@Value("${db.min_evictable_idle_time}")
	private Long minEvictableIdleTimeMillis;
	@Value("${db.time_between_eviction}")
	private Long timeBetweenEvictionRunsMillis;
	@Value("${db.initial_size}")
	private Integer initialSize;
	@Value("${db.max_active}")
	private Integer maxActive;
	@Value("${db.max_idle}")
	private Integer maxIdle;
	@Value("${db.min_idle}")
	private Integer minIdle;
	@Value("${db.max_wait}")
	private Long maxWait;

	@Autowired
	ApplicationContext context;

	@Bean
	public DataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUrl(url);
		ds.setUsername(user);
		ds.setPassword(pass);

		ds.setTestWhileIdle(YES);
		ds.setTestOnReturn(YES);
		ds.setTestOnBorrow(YES);
		ds.setValidationQuery(KEEP_ALIVE_QUERY);

		ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		ds.setMaxIdle(maxIdle);
		ds.setMaxWait(maxWait);
		ds.setMaxActive(maxActive);
		ds.setInitialSize(initialSize);
		ds.setMinIdle(minIdle);

		return ds;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
			JpaVendorAdapter jpaVendorAdapter) throws IOException {
		LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
		lef.setDataSource(dataSource);
		lef.setJpaVendorAdapter(jpaVendorAdapter);
		lef.setPackagesToScan(packagesToScan);

		return lef;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(NO);
		hibernateJpaVendorAdapter.setGenerateDdl(NO);
		hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
		return hibernateJpaVendorAdapter;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager();

	}

}
