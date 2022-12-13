package com.efsauto.erste_schritte.service;


import com.efsauto.erste_schritte.exception.WeatherException;
import com.efsauto.erste_schritte.models.Weather.WeatherInformationModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class WeatherService {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherService.class);

    private final String openWeatherUrl;
    private final String openWeatherApiKey;
    private final String openWeatherZip;
    private final String openWeatherUnit;
    private final String openWeatherLanguage;

    public WeatherService(@Value("${openweather.url}") String openWeatherUrl,
                          @Value("${openweather.api.key}") String openWeatherApiKey,
                          @Value("${openweather.zip}") String openWeatherZip,
                          @Value("${openweather.units}") String openWeatherUnit,
                          @Value("${openweather.language}") String openWeatherLanguage) {
        this.openWeatherUrl = openWeatherUrl;
        this.openWeatherApiKey = openWeatherApiKey;
        this.openWeatherZip = openWeatherZip;
        this.openWeatherUnit = openWeatherUnit;
        this.openWeatherLanguage = openWeatherLanguage;
    }


    //    TODO: in weatherservice schieben ==> DONE
    // TODO : keine Strings zurück geben sondern aussagekräftige Objekte ==> DONE
    //
    public WeatherInformationModel getWeatherInformation() throws WeatherException {
        ObjectMapper objectMapper = new ObjectMapper();

        try (CloseableHttpClient closeHttpClient = HttpClientBuilder.create().build()) { // create HttpClient
            // build URI
            URIBuilder builtOpenWeatherURI = new URIBuilder(this.openWeatherUrl);
            builtOpenWeatherURI.setParameter("zip", this.openWeatherZip)
                    .setParameter("appid", this.openWeatherApiKey)
                    .setParameter("lang", this.openWeatherLanguage)
                    .setParameter("units", this.openWeatherUnit);

            LOG.debug("builtOpenWeatherURI URL {}", builtOpenWeatherURI);

            HttpGet request = new HttpGet(builtOpenWeatherURI.build());


            request.addHeader(HttpHeaders.ACCEPT, "application/json");
            CloseableHttpResponse response = closeHttpClient.execute(request);

            // TODO: eine  if (response.getStatusLine().getStatusCode() >= 400) ==> DONE
            if (response.getStatusLine().getStatusCode() >= 400) {
                LOG.error("something went wrong when getting weather information. StatusCode: {}, Reason: {}", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
                throw new WeatherException("SORRY....something went wrong when getting weather information : " + response.getStatusLine().getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");

            response.close();

            LOG.debug("Successfully get weather information for zip \"{}\", Result: {}", this.openWeatherZip, new JSONObject(result));

            return objectMapper.readValue(result, WeatherInformationModel.class);

        } catch (IOException | JSONException | URISyntaxException ex) {
            throw new WeatherException("SORRY....Get weather information: FAILED ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
