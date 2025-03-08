package by.klevitov.eventpersistor.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchByFieldsRequestDTO {
    @JsonProperty("isCombinedMatch")
    private boolean isCombinedMatch;
    private Map<String, Object> fields;
    private PageRequestDTO pageRequestDTO;
}
