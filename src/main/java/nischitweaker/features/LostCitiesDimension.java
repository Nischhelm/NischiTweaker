package nischitweaker.features;

import mcjty.lostcities.config.LostCityConfiguration;

public class LostCitiesDimension {
    public static boolean isLostCities(int dimension) {
        return LostCityConfiguration.DIMENSION_ID != -1 && dimension == LostCityConfiguration.DIMENSION_ID;
    }
}
