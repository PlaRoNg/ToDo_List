package com.efsauto.erste_schritte.service;


import com.efsauto.erste_schritte.exception.ElasticSearchException;
import com.efsauto.erste_schritte.exception.WeatherException;
import com.efsauto.erste_schritte.models.Weather.WeatherInformationModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ElasticSearchService {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchService.class);

    private final String elasticSearchUrl;
    private final String weatherInfoIndexName;


    public ElasticSearchService(@Value("${elasticsearch.weather.url}") String elasticSearchUrl,
                                @Value("${elasticsearch.weather.indexname}") String weatherInfoIndexName, WeatherService weatherService) {
        this.elasticSearchUrl = elasticSearchUrl;
        this.weatherInfoIndexName = weatherInfoIndexName;
    }

    // TODO: generische Funktionen schreiben ==> DONE
    // TODO: Cron Jobs als eigene Klasse : wie im Converter oder Baeldung==> DONE
    // TODO: use URIBuilder ==> DONE

    public WeatherInformationModel getDataFromElasticsearch() throws ElasticSearchException {
        ObjectMapper objectMapper = new ObjectMapper();

        try (CloseableHttpClient closeHttpClient = HttpClientBuilder.create().build()) { // create HttpClient

            URIBuilder builtElasticSearchURI = this.builtSearchURI(this.elasticSearchUrl, this.weatherInfoIndexName);

            LOG.debug("getDataFromElasticsearch: builtElasticSearchURI URL {}", builtElasticSearchURI);

            HttpPost request = new HttpPost(String.valueOf(builtElasticSearchURI));

            // build query to get the last indexed document
            Map<String, Object> elasticsearchQuery = buildElasticsearchQuery();

            // send and get request response
            request.setEntity(new StringEntity(objectMapper.writeValueAsString(elasticsearchQuery), ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = closeHttpClient.execute(request);


            if (response.getStatusLine().getStatusCode() >= 400) {
                LOG.error("getDataFromElasticsearch(): something went wrong while getting weather information from Elasticsearch. StatusCode: {}, Reason: {}", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
                throw new ElasticSearchException("SORRY... getDataFromElasticsearch(): something went wrong while getting weather information from Elasticsearch : " + response.getStatusLine().getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // get hits from the response and parse it to java object
            String jsonString = getHitsFromElasticsearchDocument(response);

            response.close();

            return objectMapper.readValue(jsonString, WeatherInformationModel.class);

        } catch (IOException | URISyntaxException | JSONException ex) {
            throw new ElasticSearchException("SORRY.... getDataFromElasticsearch(): get weather information from elasticsearch : FAILED ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> buildElasticsearchQuery() {

        Map<String, String> esDt = new HashMap<>();
        esDt.put("dt", "desc");
        Map<String, Object> elasticsearchQuery = new HashMap<>();
        elasticsearchQuery.put("sort", esDt);
        elasticsearchQuery.put("size", 1);

        return elasticsearchQuery;
    }

    private String getHitsFromElasticsearchDocument(CloseableHttpResponse response) throws IOException, JSONException {

        HttpEntity entity = response.getEntity();

        JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
        JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");

        return jsonArray.getJSONObject(0).getJSONObject("_source").toString();

    }


    public void writeDataInElasticsearch(String indexName, String baseUrl, String indexingData) throws WeatherException {
        ObjectMapper objectMapper = new ObjectMapper();

        try (CloseableHttpClient closeHttpClient = HttpClientBuilder.create().build()) {

            URIBuilder builtElasticSearchURI = this.builtCreateDocumentURI(baseUrl, indexName);

            LOG.debug("builtElasticSearchURI URL {}", builtElasticSearchURI);

            HttpPost request = new HttpPost(String.valueOf(builtElasticSearchURI));

            request.setEntity(new StringEntity(indexingData, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = closeHttpClient.execute(request);

            if (response.getStatusLine().getStatusCode() >= 400) {
                LOG.error("something went wrong when indexing . StatusCode: {}, Reason: {}", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
                throw new WeatherException("SORRY.... something went wrong when indexing in Elasticsearch : " + response.getStatusLine().getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.close();

            LOG.debug("Cron Job: Successfully indexing index with index name : {}", this.weatherInfoIndexName);

            objectMapper.readValue(indexingData, WeatherInformationModel.class);

        } catch (IOException | URISyntaxException ex) {
            throw new WeatherException("SORRY.... Cron Job:Indexing weather information in elasticsearch : FAILED ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private URIBuilder builtCreateDocumentURI(String baseUrl, String indexName) throws URISyntaxException {

        URIBuilder builtElasticSearchURI = new URIBuilder(baseUrl);
        builtElasticSearchURI.setPath(indexName + "/_doc");

        return builtElasticSearchURI;
    }


    private URIBuilder builtSearchURI(String baseUrl, String indexName) throws URISyntaxException {

        URIBuilder builtElasticSearchURI = new URIBuilder(baseUrl);
        builtElasticSearchURI.setPath(indexName + "/_search");

        return builtElasticSearchURI;
    }


}
