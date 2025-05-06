package bluestone.task.holiday.domain;

public record CountryCode(String code) {

    public static CountryCode of(String code) {
        return new CountryCode(code);
    }
}
