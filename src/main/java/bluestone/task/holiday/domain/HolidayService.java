package bluestone.task.holiday.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface HolidayService {

    List<Holiday> getHolidays(LocalDate startDate, CountryCode countryCode) throws ExternalServiceError;

    Set<CountryCode> getSupportedCountries() throws ExternalServiceError;
}
