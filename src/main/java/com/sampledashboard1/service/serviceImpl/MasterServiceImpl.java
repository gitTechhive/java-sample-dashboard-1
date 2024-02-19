package com.sampledashboard1.service.serviceImpl;

import com.sampledashboard1.filter.DropdownResponse;
import com.sampledashboard1.repository.CitiesRepository;
import com.sampledashboard1.repository.CountriesRepository;
import com.sampledashboard1.repository.StatesRepository;
import com.sampledashboard1.service.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final CountriesRepository countriesRepository;
    private final StatesRepository statesRepository;
    private final CitiesRepository citiesRepository;
    @Override
    public List<DropdownResponse> getCountriesDropdown() {
        return countriesRepository.getCountriesDropdown();
    }
    @Override
    public List<DropdownResponse> getCountriesDropdownFlag() {
        return countriesRepository.getCountriesDropdownFlag();
    }

    @Override
    public List<DropdownResponse> getStatesDropdown(Long id) {
        return statesRepository.getStatesDropdown(id);
    }

    @Override
    public List<DropdownResponse> getCitiesDropdown(Long id) {
        return citiesRepository.getCitiesDropdown(id);
    }


}
