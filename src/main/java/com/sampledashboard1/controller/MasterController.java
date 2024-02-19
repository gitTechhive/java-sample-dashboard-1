package com.sampledashboard1.controller;

import com.sampledashboard1.filter.ResponseWrapperDTO;
import com.sampledashboard1.service.MasterService;
import com.sampledashboard1.utils.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("master")
public class MasterController {

    private final MasterService masterService;

    @GetMapping("/countriesDropdown")
    public ResponseWrapperDTO getCountriesDropdown(HttpServletRequest httpServletRequest) {

        return ResponseWrapperDTO.successResponse(MessageUtils.get("master.controller.countriesDropdown"), masterService.getCountriesDropdown(), httpServletRequest);
    }
    @GetMapping("/CountriesDropdownFlag")
    public ResponseWrapperDTO getCountriesDropdownFlag(HttpServletRequest httpServletRequest) {

        return ResponseWrapperDTO.successResponse(MessageUtils.get("master.controller.countriesDropdown"), masterService.getCountriesDropdownFlag(), httpServletRequest);
    }
    @GetMapping("/StatesDropdown")
    public ResponseWrapperDTO getStatesDropdown(@RequestParam Long country_id, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("master.controller.statesDropdown"), masterService.getStatesDropdown(country_id), httpServletRequest);
    }
    @GetMapping("/citiesDropdown")
    public ResponseWrapperDTO getCitiesDropdown(@RequestParam Long state_id, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("master.controller.citiesDropdown"), masterService.getCitiesDropdown(state_id), httpServletRequest);
    }
}
