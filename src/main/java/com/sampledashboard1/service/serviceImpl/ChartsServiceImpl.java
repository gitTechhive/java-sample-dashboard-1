package com.sampledashboard1.service.serviceImpl;

import com.sampledashboard1.model.Charts;
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
    public List<Charts> getAllCharts() {
        List<Charts> all = chartsRepository.findAll();
        return all;
    }
}
