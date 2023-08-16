package io.mosip.packet.extractor.validator.impl;

import io.mosip.packet.core.constant.FieldCategory;
import io.mosip.packet.core.dto.dbimport.DBImportRequest;
import io.mosip.packet.core.dto.dbimport.FieldFormatRequest;
import io.mosip.packet.core.dto.dbimport.TableRequestDto;
import io.mosip.packet.core.exception.ApisResourceAccessException;
import io.mosip.packet.core.util.CommonUtil;
import io.mosip.packet.extractor.util.PacketCreator;
import io.mosip.packet.extractor.validator.Validator;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class IdSchemaFieldValidator implements Validator {

    @Autowired
    private CommonUtil commonUtil;


    List<String> idFields;

    List<String> nonIdSchemaFieldsMap;

    private List<String> getIdFields() throws ApisResourceAccessException, IOException, ParseException {
        if (idFields == null) {
            idFields = new ArrayList<>();
            LinkedHashMap<String, Object> idSchema = commonUtil.getLatestIdSchema();

            for(Object obj : (List)idSchema.get("schema")) {
                Map<String, Object> map = (Map<String, Object>) obj;
                String id = map.get("id").toString();
                String type = map.get("type").toString();

                if (type.equals("biometricsType")) {
                    List<String> bioAttributes = (List<String>) map.get("bioAttributes");
                    for(String attribute : bioAttributes)
                        idFields.add(id + "_" + attribute);
                } else {
                    idFields.add(id);
                }
            }
        }
        return idFields;
    }

    public void populateNonIdSchemaFields(DBImportRequest dbImportRequest) {
        nonIdSchemaFieldsMap = new ArrayList<>();

        for (TableRequestDto tableRequestDto: dbImportRequest.getTableDetails()) {
            if(tableRequestDto.getNonIdSchemaFields() != null) {
                List<String> nonIdSchemaFieldList = Arrays.asList(tableRequestDto.getNonIdSchemaFields());
                nonIdSchemaFieldsMap.addAll(nonIdSchemaFieldList);
            }
        }
    }
    @Override
    public Boolean validate(DBImportRequest dbImportRequest) throws Exception {
        List<String> idFieldsList = getIdFields();
        populateNonIdSchemaFields(dbImportRequest);

        for(FieldFormatRequest fieldFormatRequest : dbImportRequest.getColumnDetails()) {
            if(nonIdSchemaFieldsMap == null || (nonIdSchemaFieldsMap != null && nonIdSchemaFieldsMap.size() > 0 && !nonIdSchemaFieldsMap.contains(fieldFormatRequest.getFieldToMap())))
                for(String fieldName : fieldFormatRequest.getFieldToMap().split(","))
                    if(!idFieldsList.contains(fieldName))
                        throw new Exception(fieldFormatRequest.getFieldToMap() + " is not found in Id Schema.");
        }
        return true;
    }
}
