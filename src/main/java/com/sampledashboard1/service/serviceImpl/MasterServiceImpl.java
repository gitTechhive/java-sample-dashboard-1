package com.sampledashboard1.service.serviceImpl;

import com.sampledashboard1.exception.UserDefineException;
import com.sampledashboard1.filter.DropdownResponse;
import com.sampledashboard1.repository.CitiesRepository;
import com.sampledashboard1.repository.CountriesRepository;
import com.sampledashboard1.repository.StatesRepository;
import com.sampledashboard1.service.MasterService;
import com.sampledashboard1.utils.MethodUtils;
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
        if(MethodUtils.isObjectisNullOrEmpty(id)) {
            throw new UserDefineException("countries not found ! ");
        }
        return statesRepository.getStatesDropdown(id);
    }

    @Override
    public List<DropdownResponse> getCitiesDropdown(Long id) {
        if(MethodUtils.isObjectisNullOrEmpty(id)) {
            throw new UserDefineException("state not found ! ");
        }
        return citiesRepository.getCitiesDropdown(id);
    }


}
