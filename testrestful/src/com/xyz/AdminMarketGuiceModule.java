package com.xyz;

import com.nxp.appstore.commons.guice.AbstractGuiceModuleWithPropertyBinding;
import com.nxp.appstore.commons.web.exception.ExceptionMapperGuiceModule;
import com.nxp.appstore.dao.AdministratorDao;
import com.nxp.appstore.dao.ApplicationInstallationDao;
import com.nxp.appstore.dao.DedicatedSlotDao;
import com.nxp.appstore.dao.KeyReferenceDao;
import com.nxp.appstore.dao.ReportLogDao;
import com.nxp.appstore.dao.SendEmailQueueEntryDao;
import com.nxp.appstore.market.commons.email.EmailSenderJob;
import com.nxp.appstore.market.commons.email.EmailSenderJobProvider;
import com.nxp.appstore.market.commons.rest.aop.PostPutRestMethodInterceptorGuiceModule;
import com.nxp.appstore.market.commons.rest.aop.pathid.PathCheckerGuiceModule;
import com.nxp.appstore.market.model.config.MarketModelGuiceModule;
import com.nxp.appstore.repository.AdministratorRepository;
import com.nxp.appstore.repository.ApplicationInstallationRepository;
import com.nxp.appstore.repository.DedicatedSlotRepository;
import com.nxp.appstore.repository.KeyReferenceRepository;
import com.nxp.appstore.repository.ReportLogRepository;
import com.nxp.appstore.repository.SendEmailQueueEntryRepository;

/**
 * Defines the Guice bindings for the admin market deployment.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public class AdminMarketGuiceModule extends AbstractGuiceModuleWithPropertyBinding {

  @Override
  protected void configure() {
    super.configure();

    bindResources();
    bindRepositories();

    install(new MarketModelGuiceModule());
    install(new ExceptionMapperGuiceModule());
    install(new PathCheckerGuiceModule());
    install(new PostPutRestMethodInterceptorGuiceModule());

//    bind(AdminReportGeneratorJob.class).toProvider(AdminReportGeneratorJobProvider.class);
    bind(EmailSenderJob.class).toProvider(EmailSenderJobProvider.class);
  }

  private void bindResources() {
//    bind(CardIssuerResource.class);
//    bind(ApplicationProviderResource.class);
//    bind(UserDataResource.class);
//    bind(MonitoringResource.class);
//    bind(ApplicationResource.class);
  }

  private void bindRepositories() {
    bind(AdministratorRepository.class).to(AdministratorDao.class);
    bind(ApplicationInstallationRepository.class).to(ApplicationInstallationDao.class);
    bind(ReportLogRepository.class).to(ReportLogDao.class);
    bind(KeyReferenceRepository.class).to(KeyReferenceDao.class);
    bind(DedicatedSlotRepository.class).to(DedicatedSlotDao.class);
    bind(SendEmailQueueEntryRepository.class).to(SendEmailQueueEntryDao.class);
  }
}
