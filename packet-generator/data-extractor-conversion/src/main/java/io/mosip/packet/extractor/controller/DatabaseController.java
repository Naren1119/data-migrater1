package io.mosip.packet.extractor.controller;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.packet.core.constant.DatabaseQueries;
import io.mosip.packet.core.constant.QuerySelection;
import io.mosip.packet.core.dto.RequestWrapper;
import io.mosip.packet.core.dto.ResponseWrapper;
import io.mosip.packet.core.dto.dbimport.DBImportRequest;
import io.mosip.packet.core.dto.dbimport.DBImportResponse;
import io.mosip.packet.core.dto.dbimport.TableRequestDto;
import io.mosip.packet.core.logger.DataProcessLogger;
import io.mosip.packet.core.util.DataBaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/databaseController")
public class DatabaseController {

    Logger LOGGER = DataProcessLogger.getLogger(DatabaseController.class);

    @Autowired
    private DataBaseUtil dataBaseUtil;

    @PostMapping(value = "/fetchTableList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> fetchTableDetails(@RequestBody RequestWrapper<DBImportRequest> request) {
        ResponseWrapper<List<Map<String, Object>>> responseWrapper = new ResponseWrapper();
        try {
            dataBaseUtil.closeConnection();
            dataBaseUtil.connectDatabase(request.getRequest(), false);

            TableRequestDto tableRequestDto = new TableRequestDto();
            tableRequestDto.setQueryType(QuerySelection.SQL_QUERY);
            tableRequestDto.setSqlQuery(String.format(DatabaseQueries.valueOf(request.getRequest().getDbType().toString()+"_TABLE_LIST").getQuery(), request.getRequest().getDatabaseName()));
            ResultSet resultSet = dataBaseUtil.readDataFromDatabase(tableRequestDto, null, null);
            List<Map<String, Object>> resultData = dataBaseUtil.extractResultSet(resultSet);
            responseWrapper.setResponse(resultData);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<ResponseWrapper>(responseWrapper, HttpStatus.OK);
    }

    @PostMapping(value = "/fetchColumnList/{tableName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> fetchColumnDetails(@RequestBody RequestWrapper<DBImportRequest> request, @PathVariable String tableName) {
        ResponseWrapper<List<Map<String, Object>>> responseWrapper = new ResponseWrapper();
        try {
            dataBaseUtil.closeConnection();
            dataBaseUtil.connectDatabase(request.getRequest(), false);

            TableRequestDto tableRequestDto = new TableRequestDto();
            tableRequestDto.setQueryType(QuerySelection.SQL_QUERY);
            tableRequestDto.setSqlQuery(String.format(DatabaseQueries.valueOf(request.getRequest().getDbType().toString()+"_COLUMN_LIST").getQuery(), tableName));
            ResultSet resultSet = dataBaseUtil.readDataFromDatabase(tableRequestDto, null, null);
            List<Map<String, Object>> resultData = dataBaseUtil.extractResultSet(resultSet);
            responseWrapper.setResponse(resultData);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<ResponseWrapper>(responseWrapper, HttpStatus.OK);
    }
}
