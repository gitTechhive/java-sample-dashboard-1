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

    /**
     * This API Used For User Register Help with add Countries
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/countriesDropdown")
    public ResponseWrapperDTO getCountriesDropdown(HttpServletRequest httpServletRequest) {

        return ResponseWrapperDTO.successResponse(MessageUtils.get("master.controller.countriesDropdown"), masterService.getCountriesDropdown(), httpServletRequest);
    }

    /**
     * This API Used For User Countries Flag add
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/countryCodes")
    public ResponseWrapperDTO getCountriesDropdownFlag(HttpServletRequest httpServletRequest) {

        return ResponseWrapperDTO.successResponse(MessageUtils.get("master.controller.countriesDropdown"), masterService.getCountriesDropdownFlag(), httpServletRequest);
    }

    /**
     * This API Used For User Register Help with add States
     * @param country_id
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/StatesDropdown")
    public ResponseWrapperDTO getStatesDropdown(@RequestParam Long country_id, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("master.controller.statesDropdown"), masterService.getStatesDropdown(country_id), httpServletRequest);
    }

    /**
     * This API Used For User Register Help with add Cities
     * @param state_id
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/citiesDropdown")
    public ResponseWrapperDTO getCitiesDropdown(@RequestParam Long state_id, HttpServletRequest httpServletRequest) {
        return ResponseWrapperDTO.successResponse(MessageUtils.get("master.controller.citiesDropdown"), masterService.getCitiesDropdown(state_id), httpServletRequest);
    }
}
