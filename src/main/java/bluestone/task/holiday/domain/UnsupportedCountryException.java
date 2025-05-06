package bluestone.task.holiday.domain;

public class UnsupportedCountryException extends RuntimeException {

    private static final String ERROR_TEMPLATE = "Unsupported country code: %s";

    public UnsupportedCountryException(CountryCode unsupportedCountry) {
        super(String.format(ERROR_TEMPLATE, unsupportedCountry.code()));
    }
}
