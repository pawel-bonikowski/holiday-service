package bluestone.task.holiday.infrastructure;

import bluestone.task.holiday.domain.CountryCode;
import bluestone.task.holiday.domain.ExternalServiceError;
import bluestone.task.holiday.domain.Holiday;
import bluestone.task.holiday.domain.HolidayService;
import bluestone.task.holiday.openholiday.client.HolidaysApi;
import bluestone.task.holiday.openholiday.client.RegionalApi;
import bluestone.task.holiday.openholiday.model.HolidayResponse;
import bluestone.task.holiday.openholiday.model.LocalizedText;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class HolidayServiceImpl implements HolidayService {

    private final HolidaysApi holidaysApi;

    private final RegionalApi regionalApi;

    private final Set<CountryCode> supportedCountries;

    HolidayServiceImpl(HolidaysApi holidaysApi, RegionalApi regionalApi) {
        this.holidaysApi = holidaysApi;
        this.regionalApi = regionalApi;
        this.supportedCountries = getCountryCodes();

    }

    @Override
    public List<Holiday> getHolidays(LocalDate startDate, CountryCode countryCode) throws ExternalServiceError {

        List<HolidayResponse> holidayResponses =
                wrapApiCall(() -> holidaysApi.publicHolidaysGet(countryCode.code(), startDate, startDate.plusYears(1), null, null));
        return holidayResponses.stream()
                .filter(HolidayResponse::getNationwide)
                .map(i -> {
                    String localName = i.getName().stream()
                            .filter(text -> countryCode.code().equals(text.getLanguage()))
                            .findAny()
                            .map(LocalizedText::getText)
                            .orElse("Local Name not found");
                    return new Holiday(localName, i.getStartDate(), countryCode);
                }).toList();

    }


    @Override
    public Set<CountryCode> getSupportedCountries() throws ExternalServiceError {
        return Collections.unmodifiableSet(supportedCountries);
    }

    private Set<CountryCode> getCountryCodes() {
        return wrapApiCall(() -> regionalApi.countriesGet(null))
                .stream()
                .map(i -> CountryCode.of(i.getIsoCode()))
                .collect(Collectors.toSet());
    }

    private <T> T wrapApiCall(Supplier<T> apiCall) {
        try {
            return apiCall.get();
        } catch (HttpClientErrorException ex) {
            throw new ExternalServiceError(ex);
        }
    }
}
