package com.sampledashboard1.service.serviceImpl;

import com.sampledashboard1.model.Charts;
import com.sampledashboard1.payload.response.ChartResponse;
import com.sampledashboard1.repository.CaptchaRepository;
import com.sampledashboard1.repository.ChartsRepository;
import com.sampledashboard1.service.ChartsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ChartsServiceImpl implements ChartsService {

    private final ChartsRepository  chartsRepository;

    @Override
    public ChartResponse getAllCharts() {
        ChartResponse chartResponse=new ChartResponse();
        List<Charts> all = chartsRepository.findAll();
        chartResponse.setChartData(all);
        chartResponse.setTotalCustomer(2420);
        chartResponse.setActiveNow(2420);
        chartResponse.setMyBalance(2420);
        chartResponse.setTotalMember(50);
        chartResponse.setActiveNowPer(20.00);
        chartResponse.setMyBalancePer(30.00);
        chartResponse.setTotalMemberPer(40.00);
        chartResponse.setTotalCustomerPer(50.00);
        return chartResponse;
    }
}
