package com.sampledashboard1.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sampledashboard1.config.audit.Auditable;
import com.sampledashboard1.utils.VariableUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(name = VariableUtils.TABLE_NAME_FOR_CHARTS)
public class Charts extends Auditable<Long> {

    private String chartType;
    private String value;

    /***
     *  others
     */
    @Transient
    private Map<String, Object> otherData;
    @JsonAnyGetter
    public Map<String, Object> getOtherData() {
        return otherData;
    }

     /*"totalCustomer": 2420,
             "activeNow": 2420,
             "totalMember": 2420,
             "myBalance": 50*/
}
