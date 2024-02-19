package com.sampledashboard1.service;

import com.sampledashboard1.filter.DropdownResponse;

import java.util.List;

public interface MasterService {
    List<DropdownResponse> getCountriesDropdown();

    List<DropdownResponse> getCountriesDropdownFlag();

    List<DropdownResponse> getStatesDropdown(Long id);

    List<DropdownResponse> getCitiesDropdown(Long id);
}
