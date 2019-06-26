package com.affluencesystems.insurancetelematics.common.Utils;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.affluencesystems.insurancetelematics.common.Models.Map_history;
import com.affluencesystems.insurancetelematics.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class MapUtils {
    private static final String TAG = "MapUtils";
    final static int min = 0;
    final static int max = 360;
    public static LatLng updated_latlong;
    public static float zoom_level = 17f;
    ValueAnimator valueAnimator;
    ProgressDialog progressDoalog;
    private static PreferenceUtils preferenceUtils;
    private float v;
    private double lat, lng;
    public static boolean is_poligon_on = false;
    private static boolean isMarkerRotating = false;
//    public static Circle radiusCircle;

    public static List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static void fitZoomWithScreen(Location source, Location distance, GoogleMap googleMap, Context context) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //the include method will calculate the min and max bound.
        builder.include(new LatLng(source.getLatitude(), source.getLongitude()));
        builder.include(new LatLng(distance.getLatitude(), distance.getLongitude()));

        //        builder.include(marker3.getPosition());
        //        builder.include(marker4.getPosition());
        LatLngBounds bounds = builder.build();

        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels - 250;
        int padding = (int) (width * 0.20);
        // offset from edges of the map 12% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        googleMap.animateCamera(cu);

    }

    public static CameraUpdate moveToBounds(Context context, Polyline p) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        List<LatLng> arr = p.getPoints();
        for (int i = 0; i < arr.size(); i++) {
            builder.include(arr.get(i));
        }
        LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels

        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels / 2;

//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);


        return cu;
//        mMap.animateCamera(cu);
    }


    public static void rotateMarker(final Marker marker, final float toRotation) {
        if (!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    //  marker.setRotation(toRotation);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }


    // https://stackoverflow.com/questions/24812483/how-to-create-bounds-of-a-android-polyline-in-order-to-fit-the-screen
    /*
     *       takes previous latlng and current latlng based on the angle change the vehicle facing correct side.
     * */
    public static float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }


    /*
     *       move the camera based on the vehicle movement.
     * */
    public static GoogleMap move_Camera(LatLng latLng, GoogleMap mMap, Context context) {
        if (preferenceUtils == null)
            preferenceUtils = new PreferenceUtils(context);

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(latLng)
                .zoom(preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17))
                //  .bearing(bearing)
                //   .tilt(30)
                .build()));
        return mMap;
    }

    //for 2D and 3D animation for google map
    public static GoogleMap move_3D_Camera(LatLng latLng, final GoogleMap mMap, float bearing, Context context) {
        if (preferenceUtils == null)
            preferenceUtils = new PreferenceUtils(context);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17))
                .bearing(bearing)
                .tilt(90)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
        return mMap;
    }


    /*
     *       convert latitude and longitude to latlng.
     * */
    public static LatLng latlong(double lat, double lang) {
        LatLng latlong = new LatLng(lat, lang);
        return latlong;
    }


    /*
     *       Used to get the role ids exists or not
     *       If exists it will sho other wise hide the role for this user.
     * */
    public static boolean isContainsId(int value, Context context) {
        String value1 = value+"";
        boolean found = false;
        if (preferenceUtils == null)
            preferenceUtils = new PreferenceUtils(context);
        try {
            JSONArray privilegeArray = new JSONArray(preferenceUtils.getStringFromPreference(PreferenceUtils.PERSON_PRIVILEGES, ""));
            for (int i = 0; i < privilegeArray.length(); i++) {
                try {
                    if (privilegeArray.getString(i).equals(value1))
                        found = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return found;
    }


    /*
     *       animate camera just like move camera.
     * */
    public static GoogleMap animate_camera(LatLng latlong, GoogleMap mMap, Context context) {
        if (preferenceUtils == null)
            preferenceUtils = new PreferenceUtils(context);
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latlong, preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17));
        mMap.animateCamera(location);
        return mMap;
    }


    /*
     *       Take the all the latlngs and draw a line as path.
     * */
    public static PolylineOptions polylineOptions(ArrayList<LatLng> latlongs, ArrayList<Float> fuel_consumption_array_list, Context context) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(latlongs);
        for (int i = 0; i < fuel_consumption_array_list.size(); i++) {
            if (fuel_consumption_array_list.get(i) >= 0f && fuel_consumption_array_list.get(i) <= 10f) {
                polylineOptions.color(context.getResources().getColor(R.color.green));
            } else if (fuel_consumption_array_list.get(i) >= 10f && fuel_consumption_array_list.get(i) <= 20f) {
                polylineOptions.color(context.getResources().getColor(R.color.light_green));
            } else if (fuel_consumption_array_list.get(i) >= 20f && fuel_consumption_array_list.get(i) <= 30f) {
                polylineOptions.color(context.getResources().getColor(R.color.orenge));
            } else if (fuel_consumption_array_list.get(i) >= 30f && fuel_consumption_array_list.get(i) <= 40f) {
                polylineOptions.color(context.getResources().getColor(R.color.red));
            }
        }

        polylineOptions.width(5);
        polylineOptions.startCap(new RoundCap());
        polylineOptions.endCap(new RoundCap());
        polylineOptions.jointType(ROUND);
        return polylineOptions;
    }


    public static float randum_angle() {
        final float random = new Random().nextInt((max - min) + 1) + min;
        return random;
    }

    /*
     *       Add a marker on google map and returns it.
     * */
    public static Marker marker(GoogleMap mMap, LatLng position, int drawableresource) {
        Marker mark_position = mMap.addMarker(new MarkerOptions().position(position)
//                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(drawableresource)));
        return mark_position;
    }


    /*
     *       Add a car as marker with radius on google map and returns it.
     * */
    public static Marker carMarker(GoogleMap mMap, LatLng position, Context context, int vectorImage) {
        Marker mark_position = mMap.addMarker(new MarkerOptions().position(position)
                .icon(bitmapDescriptorFromVector(context, vectorImage)));
        return mark_position;
    }


    /*
     *       convert drawable to BitmapDescriptor for car marker on google map.
     * */
    private static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        try {
            Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        } catch (Exception e) {
            Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.white_car_with_radius);
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
    }

    // // TODO: 1/22/2017 please check
    void fixZoomProblem(LatLng source, LatLng distance, List<LatLng> points, GoogleMap googleMap) {

        LatLngBounds.Builder bc = new LatLngBounds.Builder();

        bc.include(source);
        bc.include(distance);

    /*  for (LatLng item : points) {
            bc.include(item);
        }
*/
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
    }

   /* public ProgressDialog progressDialog(Context context) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return progressDoalog;
    }*/


    /*
     *        Create and show progress dialog.
     * */
    public void showProgressDialog(Context context) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage(context.getString(R.string.loading));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
    }


    /*
     *       dismiss the visible progress dialog.
     * */
    public void hideProgressDialog(Context context) {
//        if(progressDoalog != null)
        progressDoalog.dismiss();
    }

    public ProgressDialog progressDialog_dismiss() {
        return progressDoalog;
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }


    /*
     *       Check the vehicle is in the geo fence or not.
     *       true : for int geo fence.
     *       false : for not in geo fence.
     * */
    public static boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }


    /*
     *       Find the distance between two latlngs.
     * */
    public static double getDistanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        Location startPoint = new Location(LiveConstants.LOCATION_KEY);
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(lon1);
        Location endPoint = new Location(LiveConstants.LOCATION_KEY);
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(lon2);
        double distance = startPoint.distanceTo(endPoint);
        Log.d(TAG, "distance in km --> " + distance);
        return distance;
    }


    /*
     *       Check the vehicle is in the geo fence or not.
     * */
    private static boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {
        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }

    public static ArrayList<LatLng> LatLngFilter(ArrayList<LatLng> latLngs, double distanceInMeter) {
        ArrayList<LatLng> latLngs1 = new ArrayList<>();
        Log.d(TAG, "Array  size is before " + latLngs1.size());
        Log.d(TAG, "Array  size is total before " + latLngs.size());
        for (int i = 1; i < latLngs.size(); i++) {
            for (int j = i; j < latLngs.size(); j++) {
                double distance = getDistanceInMeters(latLngs.get(i - 1).latitude, latLngs.get(i - 1).longitude, latLngs.get(j).latitude, latLngs.get(j).longitude);
                if (distance >= distanceInMeter) {
                    Log.d(TAG, " compare with : " + String.valueOf(i - 1));
                    latLngs1.add(latLngs.get(j));
                    Log.d(TAG, "Added position : " + j);
                    i = j;
                    break;
                }
            }
        }
        Log.d(TAG, "Array  size is after " + latLngs1.size());
        return latLngs1;
    }


    /*
     *       Eliminate the latlngs, which the distance is less than distanceInMeter.
     *       It takes all the latlngs and returns new arraylist of latlngs.
     * */
    public static ArrayList MapHistoryFilter(ArrayList<Map_history> map_histories, double distanceInMeter) {
        ArrayList<Map_history> latLngs1 = new ArrayList<>();
        Log.d(TAG, "Array  size is before " + latLngs1.size());
        Log.d(TAG, "Array  size is total before " + map_histories.size());
        for (int i = 1; i < map_histories.size(); i++) {
            for (int j = i; j < map_histories.size(); j++) {
                double distance = getDistanceInMeters(Double.parseDouble(map_histories.get(i - 1).getLatitude()), Double.parseDouble(map_histories.get(i - 1).getLongitude()),
                        Double.parseDouble(map_histories.get(j).getLatitude()), Double.parseDouble(map_histories.get(j).getLongitude()));
                if (distance >= distanceInMeter) {
                    Log.d(TAG, " compare with : " + String.valueOf(i - 1));
                    latLngs1.add(map_histories.get(j));
                    Log.d(TAG, "Added position : " + j);
                    i = j;
                    break;
                }
            }
        }
        Log.d(TAG, "Array  size is after " + latLngs1.size());
        return latLngs1;
    }


    public static String encryptDecrypt(String input) {
        char xorKey = 'P';
        String output = "";
        int length = input.length();
        for (int i = 0; i < length; i++) {
            output = output + Character.toString((char) (input.charAt(i) ^ xorKey));
        }
        return output;
    }

}
