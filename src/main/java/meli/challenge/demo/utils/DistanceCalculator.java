package meli.challenge.demo.utils;

public class DistanceCalculator {

    private static final double LAT_BS_AS = -34;
    private static final double LON_BS_AS = -64;

    public static double distance(double lat2, double lon2) {
        if ((LAT_BS_AS == lat2) && (LON_BS_AS == lon2)) {
            return 0;
        } else {
            double theta = LON_BS_AS - lon2;
            double dist = Math.sin(Math.toRadians(LAT_BS_AS)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(LAT_BS_AS)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.85316;

            return (dist);
        }
    }
}
