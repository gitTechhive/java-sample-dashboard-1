package com.sampledashboard1.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sampledashboard1.utils.MethodUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DropdownResponse {
    private String label;
    private Object value;
    private Object other;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private List<DropdownResponse> sub;

    public DropdownResponse(String label, Object value) {
        this.label = label;
        this.value = value;
    }

    public DropdownResponse(String label, Object value, Object other) {
        super();
        this.label = label;
        this.value = value;
        this.other = other;
    }

    public void setSub(List<DropdownResponse> sub) {
        this.sub = (!MethodUtils.isListIsNullOrEmpty(sub) && !MethodUtils.isObjectisNullOrEmpty(sub.get(0).getLabel()))
                ? sub
                : new ArrayList<>();
    }

}
