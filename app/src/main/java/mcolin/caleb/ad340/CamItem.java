package mcolin.caleb.ad340;

import android.util.Log;

public class CamItem {
    private final String mCamId;
    private final String mCamType;
    private final String mCameAddress;
    private final String mCamImageUrl;
    private final double mCamLong;
    private final double mCamLat;

    public CamItem(String id, String type, String address, String url, double mLong, double lat) {
        mCameAddress = address;
        mCamId = id;
        mCamType = type;
        mCamLong = mLong;
        mCamLat = lat;
        mCamImageUrl = checkType(type, url);
    }

    private String checkType(String type, String url) {
        String newUrl = "";
        if (type.equals("sdot")) {
            newUrl = "https://www.seattle.gov/trafficcams/images/" + url;
        } else if (type.equals("wsdot")) {
            newUrl = "https://images.wsdot.wa.gov/nw/" + url;
        } else {
            Log.i("TAG", "incorrect type");
        }
        return newUrl;
    }

    public String getCamId() {
        return mCamId;
    }

    public String getType() {
        return mCamType;
    }

    public String getAddress() {
        return mCameAddress;
    }

    public String getUrl() {
        return mCamImageUrl;
    }

    public double getLong() {
        return mCamLong;
    }

    public double getLat() {
        return mCamLat;
    }
}
