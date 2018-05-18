package org.ironrhino.core.metrics;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.ironrhino.core.spring.configuration.AddressAvailabilityCondition;
import org.ironrhino.core.spring.configuration.ClassPresentConditional;
import org.ironrhino.core.util.AppInfo;
import org.ironrhino.core.util.NameableThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import com.zaxxer.hikari.HikariDataSource;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ClassPresentConditional("io.micrometer.core.instrument.Metrics")
@Slf4j
public class MetricsConfiguration {

	@Autowired
	private Environment environment;

	@Autowired(required = false)
	private DataSource dataSource;

	@Autowired(required = false)
	private ServletContext servletContext;

	@PostConstruct
	public void init() {
		if (ClassUtils.isPresent("io.micrometer.influx.InfluxMeterRegistry", MeterRegistry.class.getClassLoader())) {
			io.micrometer.influx.InfluxConfig config = key -> environment.getProperty(key, (String) null);
			if (AddressAvailabilityCondition.check(config.uri(), 2000)) {
				Metrics.addRegistry(new io.micrometer.influx.InfluxMeterRegistry(config, Clock.SYSTEM,
						new NameableThreadFactory("metrics")));
				log.info("Add influx metrics {}", config.uri());
			} else {
				log.warn("Skip register influx metrics {}", config.uri());
			}
		}
		if (Metrics.globalRegistry.getRegistries().isEmpty())
			Metrics.addRegistry(new SimpleMeterRegistry());
		String instanceId = AppInfo.getInstanceId(true);
		Metrics.globalRegistry.config().commonTags("app", AppInfo.getAppName(), "instance",
				instanceId.substring(instanceId.indexOf('@') + 1));
		instrument();
	}

	@PreDestroy
	public void destroy() {
		Metrics.globalRegistry.getRegistries().forEach(MeterRegistry::close);
	}

	protected void instrument() {
		if (dataSource instanceof HikariDataSource) {
			((HikariDataSource) dataSource).setMetricRegistry(Metrics.globalRegistry);
		}
		String className = servletContext.getClass().getName();
		if (className.startsWith("org.apache.catalina.")) {
			TomcatMetrics.monitor(Metrics.globalRegistry);
		}
	}

}