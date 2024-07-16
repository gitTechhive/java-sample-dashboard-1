package com.sampledashboard1.payload.response;

import com.sampledashboard1.model.Charts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class ChartResponse {

    private Integer totalCustomer;
    private Integer activeNow;
    private Integer totalMember;
    private Integer myBalance;
    private Double totalCustomerPer;
    private Double activeNowPer;
    private Double totalMemberPer;
    private Double myBalancePer;
    private List<Charts> chartData;

}
