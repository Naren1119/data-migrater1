package io.mosip.packet.core.dto.dbimport;

import io.mosip.packet.core.constant.FieldCategory;
import io.mosip.packet.core.constant.QuerySelection;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class TableRequestDto implements Comparable<TableRequestDto> {
    private QuerySelection queryType;
    private Integer executionOrderSequence;
    private String tableName;
    private String sqlQuery;
    private String[] nonIdSchemaFields;
    private List<QueryFilter> filters;
    private String tableNamewithOutSchema;

    @Override
    public int compareTo(TableRequestDto o) {
        return this.executionOrderSequence.compareTo(o.executionOrderSequence);
    }

    public String getTableNameWithOutSchema() {
        if(tableNamewithOutSchema == null) {
            if(tableName.contains("."))
                tableNamewithOutSchema=tableName.split("\\.")[1];
            else
                tableNamewithOutSchema=tableName;
        }
        return tableNamewithOutSchema;
    }
}