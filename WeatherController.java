package com.efsauto.erste_schritte.Controller;


import com.efsauto.erste_schritte.exception.ElasticSearchException;
import com.efsauto.erste_schritte.exception.ToDoExceptionHandler;
import com.efsauto.erste_schritte.models.Weather.WeatherInformationModel;
import com.efsauto.erste_schritte.service.ElasticSearchService;
import com.efsauto.erste_schritte.service.WeatherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Weather"})
@RestController
@RequestMapping(value = "/Weather")
public class WeatherController {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherController.class);
    private final ToDoExceptionHandler toDoExceptionHandler;
    private final ElasticSearchService elasticSearchService;

    public WeatherController(WeatherService weatherService,
                             ToDoExceptionHandler toDoExceptionHandler,
                             ElasticSearchService elasticSearchService) {
        this.toDoExceptionHandler = toDoExceptionHandler;
        this.elasticSearchService = elasticSearchService;
    }


    /**
     * Get weather information
     * @return
     */
    // TODO: eigenen Controller ==> DONE
    // TODO: Pfadname anpassen ==> DONE
    @Operation(summary = "Get Weather information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Weather information loaded"),
            @ApiResponse(code = 500, message = "An error occurred")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getWeatherInformation() {
        try {
            LOG.debug("Try to weather information");

            WeatherInformationModel weatherInformationModel = this.elasticSearchService.getDataFromElasticsearch();
            LOG.debug("Weather information loaded  - Done");

            return ResponseEntity.ok(weatherInformationModel);

        } catch (ElasticSearchException ex) {
            return toDoExceptionHandler.handleToDoException(ex);
        }
    }



}
