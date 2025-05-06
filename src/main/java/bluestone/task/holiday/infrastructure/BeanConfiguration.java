package bluestone.task.holiday.infrastructure;

import bluestone.task.holiday.domain.CommonHolidayFinder;
import bluestone.task.holiday.domain.HolidayService;
import bluestone.task.holiday.openholiday.ApiClient;
import bluestone.task.holiday.openholiday.client.HolidaysApi;
import bluestone.task.holiday.openholiday.client.RegionalApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BeanConfiguration {

    @Bean
    HolidayService holidayService(HolidaysApi holidaysApi, RegionalApi regionalApi) {
        return new HolidayServiceImpl(holidaysApi, regionalApi);
    }

    @Bean
    CommonHolidayFinder commonHolidayFinder(HolidayService holidayService) {
        return new CommonHolidayFinder(holidayService);
    }

    @Bean
    HolidaysApi getHolidaysApi(ApiClient apiClient) {
        return new HolidaysApi(apiClient);
    }

    @Bean
    RegionalApi getRegionalApi(ApiClient apiClient) {
        return new RegionalApi(apiClient);
    }

    @Bean
    ApiClient getApiClient(@Value("${openholiday.client.baseUrl}") String apiUrl) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(apiUrl);
        return apiClient;
    }

}
