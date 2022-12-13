package com.efsauto.erste_schritte.executors;


import com.efsauto.erste_schritte.models.Weather.WeatherInformationModel;
import com.efsauto.erste_schritte.service.ElasticSearchService;
import com.efsauto.erste_schritte.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service()
public class CronJobService {
    private static final Logger LOG = LoggerFactory.getLogger(CronJobService.class);

    private final String elasticSearchUrl;
    private final String weatherInfoIndexName;
    private final ElasticSearchService elasticSearchService;
    private final WeatherService weatherService;

    public CronJobService(@Value("${elasticsearch.weather.url}") String elasticSearchUrl,
                          @Value("${elasticsearch.weather.indexname}") String weatherInfoIndexName,
                          WeatherService weatherService,
                          ElasticSearchService elasticSearchService) {
        this.elasticSearchUrl = elasticSearchUrl;
        this.weatherInfoIndexName = weatherInfoIndexName;
        this.elasticSearchService = elasticSearchService;
        this.weatherService = weatherService;
    }

    public void documentIndexingInElasticSearch() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // get weather info from OpenWeather API
            WeatherInformationModel weatherInfo = this.weatherService.getWeatherInformation();
            this.elasticSearchService.writeDataInElasticsearch(this.weatherInfoIndexName, this.elasticSearchUrl, objectMapper.writeValueAsString(weatherInfo));

        } catch (Exception exception) {
            LOG.error("Error while indexing into Elasticsearch", exception);
        }
    }

}
