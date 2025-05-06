package bluestone.task.holiday.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestHolidayService implements HolidayService {

    public static Map<CountryCode, List<Holiday>> HOLIDAYS = Map.of(
            CountryCode.of("PL"), List.of(
                    new Holiday("Nowy Rok", LocalDate.of(2025, 1, 1), CountryCode.of("PL")),
                    new Holiday("Trzech Króli", LocalDate.of(2025, 1, 6), CountryCode.of("PL")),
                    new Holiday("Konstytucja 3 Maj", LocalDate.of(2025, 5, 3), CountryCode.of("PL")),
                    new Holiday("Boże Narodzenie", LocalDate.of(2025, 12, 25), CountryCode.of("PL")),
                    new Holiday("Boże Narodzenie 2", LocalDate.of(2025, 12, 26), CountryCode.of("PL"))

            ),
            CountryCode.of("IT"), List.of(
                    new Holiday("Capodanno", LocalDate.of(2025, 1, 1), CountryCode.of("IT")),
                    new Holiday("Epifania", LocalDate.of(2025, 1, 6), CountryCode.of("IT")),
                    new Holiday("Festa del lavoro", LocalDate.of(2025, 5, 1), CountryCode.of("IT")),
                    new Holiday("Ferragosto", LocalDate.of(2025, 8, 15), CountryCode.of("IT")),
                    new Holiday("Natale di Gesù", LocalDate.of(2025, 12, 25), CountryCode.of("IT"))
            ),
            CountryCode.of("ES"), List.of(
                    new Holiday("Epifanía del Señor", LocalDate.of(2025, 1, 6), CountryCode.of("ES")),
                    new Holiday("Festa del lavoro", LocalDate.of(2025, 5, 1), CountryCode.of("ES")),
                    new Holiday("Asunción de la Virgen", LocalDate.of(2025, 8, 15), CountryCode.of("ES")),
                    new Holiday("Navidad", LocalDate.of(2025, 12, 25), CountryCode.of("ES"))
            ));


    @Override
    public List<Holiday> getHolidays(LocalDate startDate, CountryCode countryCode) {

        return HOLIDAYS.getOrDefault(countryCode, List.of())
                .stream()
                .filter(holiday -> holiday.date().isAfter(startDate) || holiday.date().equals(startDate))
                .toList();
    }

    @Override
    public Set<CountryCode> getSupportedCountries() {
        return HOLIDAYS.keySet();
    }
}
