package bluestone.task.holiday.api;

import java.time.LocalDate;

public record CommonHolidayResponse(LocalDate date, String name1, String name2) {
}
