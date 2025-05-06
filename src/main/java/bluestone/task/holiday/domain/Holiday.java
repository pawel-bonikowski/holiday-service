package bluestone.task.holiday.domain;

import java.time.LocalDate;

public record Holiday(String name, LocalDate date, CountryCode countryCode) {
}
