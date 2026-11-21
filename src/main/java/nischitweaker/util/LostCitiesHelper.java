package nischitweaker.util;

import mcjty.lostcities.config.LostCityConfiguration;

public class LostCitiesHelper {
    public static boolean isLostCities(int dimension) {
        return LostCityConfiguration.DIMENSION_ID != -1 && dimension == LostCityConfiguration.DIMENSION_ID;
    }
}
