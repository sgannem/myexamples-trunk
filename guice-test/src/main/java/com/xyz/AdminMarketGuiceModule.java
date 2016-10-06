package com.xyz;

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

		// install(new MarketModelGuiceModule());
		// install(new ExceptionMapperGuiceModule());
		// install(new PathCheckerGuiceModule());
		// install(new PostPutRestMethodInterceptorGuiceModule());

		// bind(AdminReportGeneratorJob.class).toProvider(AdminReportGeneratorJobProvider.class);
		// bind(EmailSenderJob.class).toProvider(EmailSenderJobProvider.class);
		bind(MessageService.class).to(FacebookService.class);
	}

	private void bindResources() {
		// bind(CardIssuerResource.class);
		// bind(ApplicationProviderResource.class);
		// bind(UserDataResource.class);
		// bind(MonitoringResource.class);
		// bind(ApplicationResource.class);
	}

	private void bindRepositories() {
		// bind(AdministratorRepository.class).to(AdministratorDao.class);
		// bind(ApplicationInstallationRepository.class).to(ApplicationInstallationDao.class);
		// bind(ReportLogRepository.class).to(ReportLogDao.class);
		// bind(KeyReferenceRepository.class).to(KeyReferenceDao.class);
		// bind(DedicatedSlotRepository.class).to(DedicatedSlotDao.class);
		// bind(SendEmailQueueEntryRepository.class).to(SendEmailQueueEntryDao.class);
	}
}
