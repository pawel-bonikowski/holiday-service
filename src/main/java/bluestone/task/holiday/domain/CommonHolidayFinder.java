package bluestone.task.holiday.domain;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CommonHolidayFinder {

    private final HolidayService holidayService;


    public CommonHolidayFinder(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    public Optional<CommonHoliday> findFirstCommonHoliday(LocalDate startDate, CountryCode country1, CountryCode country2) {
        validateCountries(country1, country2);
        LocalDate dayAfterProvidedData = startDate.plusDays(1);
        var holidaysFromCountry1 = CompletableFuture.supplyAsync(() -> holidayService.getHolidays(dayAfterProvidedData, country1));
        var holidaysFromCountry2 = CompletableFuture.supplyAsync(() -> holidayService.getHolidays(dayAfterProvidedData, country2));

        CompletableFuture.allOf(holidaysFromCountry1, holidaysFromCountry2).join();
        Map<LocalDate, List<Holiday>> groupedByDate = holidaysFromCountry2.join().stream().collect(Collectors.groupingBy(Holiday::date));


        return holidaysFromCountry1.join().stream().sorted(Comparator.comparing(Holiday::date))
                .filter(i -> groupedByDate.containsKey(i.date()))
                .findFirst()
                .map(i -> buildCommonHoliday(i, groupedByDate));
    }

    private void validateCountries(CountryCode country1, CountryCode country2) {
        Set<CountryCode> supportedCountries = holidayService.getSupportedCountries();
        if (!supportedCountries.contains(country1)) {
            throw new UnsupportedCountryException(country1);
        }
        if (!supportedCountries.contains(country2)) {
            throw new UnsupportedCountryException(country2);
        }
    }

    private CommonHoliday buildCommonHoliday(Holiday i, Map<LocalDate, List<Holiday>> grouped) {
        Holiday holiday = grouped.get(i.date()).stream().findFirst().orElseThrow();
        return new CommonHoliday(i.date(), i.name(), holiday.name());
    }
}
