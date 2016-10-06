package com.xyz;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * The Guice servlet context listener. It creates the {@link Injector}, starts and stops the {@link PersistService}.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public class AdminServletContextListener extends GuiceServletContextListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(AdminServletContextListener.class);
//  private static final String MBEAN_OBJECT_NAME_ADMIN_REPORT_GENERATOR = "com.nxp.appstore:type=AdminReportGenerator";
//  private static final String MBEAN_OBJECT_NAME_EMAIL_SENDER = "com.nxp.appstore:type=EmailSender";
//  private static final JobKey ADMIN_REPORT_GENERATOR_JOB_KEY = new JobKey(AdminReportGeneratorJob.class.getSimpleName());

  private Injector injector = null;
//  private Scheduler scheduler;

  @Override
  protected Injector getInjector() {
    return injector;
  }

  @Override
  public void contextInitialized(final ServletContextEvent servletContextEvent) {
    injector = Guice.createInjector(new AdminServletModule());

    LOGGER.info("Start PersistService");
    System.out.println("###Start PersistService");
    injector.getInstance(PersistService.class).start();
    LOGGER.info("PersistService started");
    System.out.println("###PersistService started");

//    final String disableEmailSending = servletContextEvent.getServletContext().getInitParameter("disableEmailSending");
//    final boolean isEmailSendingDisabled = equalsIgnoreCase("true", disableEmailSending);

//    configureObjectMapper();
//    schedulePeriodicJobs(isEmailSendingDisabled);
//    registerAdminReportGeneratorMBean();
//    registerEmailSenderMBean();

    super.contextInitialized(servletContextEvent);
  }

  @Override
  public void contextDestroyed(final ServletContextEvent servletContextEvent) {
    super.contextDestroyed(servletContextEvent);

//    unregisterAdminReportGeneratorMBean();
//    unregisterEmailSenderMBean();

//    if (scheduler != null) {
//      try {
//        scheduler.shutdown(true);
//        LOGGER.info("successful shutdown of scheduler of {}", AdminReportGeneratorJob.class.getSimpleName());
//      } catch (final SchedulerException e) {
//        LOGGER.error("failed shutdown of scheduler of {}", AdminReportGeneratorJob.class.getSimpleName(), e);
//      }
//    }
  }

  private void configureObjectMapper() {
    final ObjectMapper objectMapper = injector.getInstance(ObjectMapper.class);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(DeserializationFeature.WRAP_EXCEPTIONS, false);

    objectMapper.registerModule(new JacksonModule());
  }

//  private void schedulePeriodicJobs(final boolean isEmailSendingDisabled) {
//    try {
//      scheduler = StdSchedulerFactory.getDefaultScheduler();
//      scheduler.setJobFactory(GuiceQuartzJobFactory.newInstance(injector));
//
//      scheduleReportGeneratorJob();
//
//      if (!isEmailSendingDisabled) {
//        scheduleEmailSenderJob();
//      }
//
//      scheduler.start();
//
//      LOGGER.info("started scheduler, jobs={},{}", AdminReportGeneratorJob.class.getSimpleName(), EmailSenderJob.class.getSimpleName());
//
//      scheduler.triggerJob(ADMIN_REPORT_GENERATOR_JOB_KEY);
//    } catch (final SchedulerException e) {
//      LOGGER.error("failed to create quartz scheduler for {}", AdminReportGeneratorJob.class.getSimpleName());
//    }
//  }

//  private void scheduleReportGeneratorJob() throws SchedulerException {
//    final JobDetail jobDetail = newJob(AdminReportGeneratorJob.class).withIdentity(ADMIN_REPORT_GENERATOR_JOB_KEY).build();
//    final CronScheduleBuilder cronScheduleBuilder = monthlyOnDayAndHourAndMinute(1, 0, 0);
//    final Trigger trigger = newTrigger().startNow().withSchedule(cronScheduleBuilder).build();
//    scheduler.scheduleJob(jobDetail, trigger);
//  }

//  private void scheduleEmailSenderJob() throws SchedulerException {
//    final JobDetail jobDetail = newJob(EmailSenderJob.class).build();
//    final SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatMinutelyForever(EMAIL_SENDER_JOB_INTERVAL_IN_MINUTES);
//    final Trigger trigger = newTrigger().startNow().withSchedule(scheduleBuilder).build();
//    this.scheduler.scheduleJob(jobDetail, trigger);
//  }

//  private void registerAdminReportGeneratorMBean() {
//    final AdminReportGenerator adminReportGenerator = injector.getInstance(AdminReportGenerator.class);
//    registerBean(MBEAN_OBJECT_NAME_ADMIN_REPORT_GENERATOR, adminReportGenerator);
//  }

//  private void registerEmailSenderMBean() {
//    final AdminEmailSender adminEmailSender = injector.getInstance(AdminEmailSender.class);
//    registerBean(MBEAN_OBJECT_NAME_EMAIL_SENDER, adminEmailSender);
//  }

//  private void registerBean(final String objectNameAsString, final Object mbean) {
//    try {
//      final ObjectName objectName = new ObjectName(objectNameAsString);
//      ManagementFactory.getPlatformMBeanServer().registerMBean(mbean, objectName);
//      LOGGER.info("registered MBean {}", objectNameAsString);
//    } catch (final MalformedObjectNameException | MBeanRegistrationException | NotCompliantMBeanException |
//        InstanceAlreadyExistsException e) {
//      LOGGER.error("failed to register MBean {}", objectNameAsString, e);
//    }
//  }

//  private void unregisterAdminReportGeneratorMBean() {
//    unregisterMBean(MBEAN_OBJECT_NAME_ADMIN_REPORT_GENERATOR);
//  }

//  private void unregisterEmailSenderMBean() {
//    unregisterMBean(MBEAN_OBJECT_NAME_EMAIL_SENDER);
//  }

//  private void unregisterMBean(final String objectNameAsString) {
//    try {
//      final ObjectName objectName = new ObjectName(objectNameAsString);
//      ManagementFactory.getPlatformMBeanServer().unregisterMBean(objectName);
//      LOGGER.info("unregistered MBean {}", objectNameAsString);
//    } catch (final MalformedObjectNameException | MBeanRegistrationException | InstanceNotFoundException e) {
//      LOGGER.error("failed to unregister MBean {}", objectNameAsString, e);
//    }
//  }
}
