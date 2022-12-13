package com.efsauto.erste_schritte.executors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CronJobConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CronJobConfig.class);
    private final CronJobService cronJobService;

    public CronJobConfig(CronJobService cronJobService) {
        this.cronJobService = cronJobService;
    }

    @Scheduled(fixedDelay = 60000)
    public void indexingInElasticsearch(){
        LOG.debug("CronJobConfig: start indexing in elasticsearch");
        this.cronJobService.documentIndexingInElasticSearch();
    }

}
