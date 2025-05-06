package bluestone.task.holiday.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

class CommonHolidayFinderTest {

    private final CommonHolidayFinder systemUnderTest = new CommonHolidayFinder(new TestHolidayService());

    @Test
    @DisplayName("Unsupported County 1 provided (DE). UnsupportedCountryException should be thrown")
    void findHolidays_2025_1_1_DE_PL__exceptionThrown() {
        //given
        LocalDate providedDate = LocalDate.of(2025, 1, 1);
        CountryCode countryCode1 = CountryCode.of("DE");
        CountryCode countryCode2 = CountryCode.of("PL");

        //when
        //then
        UnsupportedCountryException unsupportedCountryException = Assertions.assertThrows(UnsupportedCountryException.class, () -> systemUnderTest.findFirstCommonHoliday(providedDate, countryCode1, countryCode2));
        Assertions.assertEquals("Unsupported country code: DE", unsupportedCountryException.getMessage());

    }


    @Test
    @DisplayName("Unsupported County 2 provided (DE). UnsupportedCountryException should be thrown")
    void findHolidays_2025_1_1_PL_DE__exceptionThrown() {
        //given
        LocalDate providedDate = LocalDate.of(2025, 1, 1);
        CountryCode countryCode1 = CountryCode.of("PL");
        CountryCode countryCode2 = CountryCode.of("DE");

        //when
        //then
        UnsupportedCountryException unsupportedCountryException = Assertions.assertThrows(UnsupportedCountryException.class, () -> systemUnderTest.findFirstCommonHoliday(providedDate, countryCode1, countryCode2));
        Assertions.assertEquals("Unsupported country code: DE", unsupportedCountryException.getMessage());

    }

    @Test
    @DisplayName("2025-1-1 PL IT - New Year Match")
    void findHolidays_2025_1_1_PL_IT___NewYar() {
        //given
        LocalDate providedDate = LocalDate.of(2025, 1, 1);
        CountryCode countryCode1 = CountryCode.of("PL");
        CountryCode countryCode2 = CountryCode.of("IT");

        //when
        Optional<CommonHoliday> result = systemUnderTest.findFirstCommonHoliday(providedDate, countryCode1, countryCode2);

        //then
        Assertions.assertTrue(result.isPresent());
        CommonHoliday commonHoliday = result.get();
        Assertions.assertEquals(LocalDate.of(2025, 1, 1), commonHoliday.date());
        Assertions.assertEquals("Nowy Rok", commonHoliday.name1());
        Assertions.assertEquals("Capodanno", commonHoliday.name2());
    }

    @Test
    @DisplayName("2025-5-1 PL ES - Chrismas Match")
    void findHolidays_2025_5_1_PL_ES___Christmas() {
        //given
        LocalDate providedDate = LocalDate.of(2025, 5, 1);
        CountryCode countryCode1 = CountryCode.of("PL");
        CountryCode countryCode2 = CountryCode.of("ES");

        //when
        Optional<CommonHoliday> result = systemUnderTest.findFirstCommonHoliday(providedDate, countryCode1, countryCode2);

        //then
        Assertions.assertTrue(result.isPresent());
        CommonHoliday commonHoliday = result.get();
        Assertions.assertEquals(LocalDate.of(2025, 12, 25), commonHoliday.date());
        Assertions.assertEquals("Boże Narodzenie", commonHoliday.name1());
        Assertions.assertEquals("Navidad", commonHoliday.name2());
    }


    @Test
    @DisplayName("2025-12-26 PL IT - No Match")
    void findHolidays_2025_12_16_PL_IT_No_Match() {
        //given
        LocalDate providedDate = LocalDate.of(2025, 12, 26);
        CountryCode countryCode1 = CountryCode.of("PL");
        CountryCode countryCode2 = CountryCode.of("IT");

        //when
        Optional<CommonHoliday> result = systemUnderTest.findFirstCommonHoliday(providedDate, countryCode1, countryCode2);

        //then
        Assertions.assertTrue(result.isEmpty());
    }
}