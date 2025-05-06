package bluestone.task.holiday.api;

import bluestone.task.holiday.domain.CommonHoliday;
import bluestone.task.holiday.domain.CommonHolidayFinder;
import bluestone.task.holiday.domain.CountryCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
class HolidayApi {

    private final CommonHolidayFinder finder;

    public HolidayApi(CommonHolidayFinder finder) {
        this.finder = finder;
    }

    @GetMapping("/firstCommonHoliday")
    ResponseEntity<CommonHolidayResponse> getFirstCommonHoliday(
            @RequestParam("date") @NonNull LocalDate date,
            @RequestParam("countryCode1") @NonNull String county1,
            @RequestParam("countryCode2") @NonNull String county2) {

        Optional<CommonHoliday> firstCommonHoliday = finder.findFirstCommonHoliday(date, CountryCode.of(county1), CountryCode.of(county2));
        if (firstCommonHoliday.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CommonHoliday commonHoliday = firstCommonHoliday.get();
        return ResponseEntity.ok(new CommonHolidayResponse(commonHoliday.date(), commonHoliday.name1(), commonHoliday.name2()));
    }

}
