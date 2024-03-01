package com.sampledashboard1.controller;

import com.sampledashboard1.filter.ResponseWrapperDTO;
import com.sampledashboard1.service.ChartsService;
import com.sampledashboard1.utils.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("charts")
public class ChartsController {

    private final ChartsService chartsService;

    /**
     * This API Used For Get All Charts
     * now static response
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/")
    public ResponseWrapperDTO getAllCharts(HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("charts.controller.getAll"), chartsService.getAllCharts(), httpServletRequest);
    }
}
