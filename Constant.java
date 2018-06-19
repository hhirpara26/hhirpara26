

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pollia.model.ModelCategories;
import com.pollia.model.ModelRegions;
import com.pollia.model.ModelUserDetail;
import com.pollia.storage.SharedPreferenceUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;



public class Constant {

    public static final String Broadcast_Response_Came="Broadcast_Response_Came";
    public static final String CURRENT_LANUGUAGE = "CURRENT_LANUGUAGE";
    public static final String ISLOGIN = "isLogin";
    public static final String NAME = "name";
    public static final String BIRTHDAY = "birthday";
    public static final String FIRSTNAME = "FirstName";
    public static final String LASTNAME = "LastName";
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";
    public static final String CONFIRM_PASSWORD = "ConfirmPassword";
    public static final String LANGUAGE = "Language";
    public static final String COUNTRY_CODE = "CountryCode";
    public static final String BIRTHDATE = "BirthDate";
    public static final String DEVICE_TOKEN = "DeviceToken";
    public static final String FACEBOOK_ID = "FacebookId";
    public static final String AcceptLanguage = "Accept-Language";

    public static final String isLogin = "isLogin";
    public static final String CategoriesList = "CategoriesList";


    public static final String English = "English";
    public static final String Arabic = "Arabic";
    public static final String Chinese = "Chinese";
    public static final String Russian =  "Russian";
    public static final String English_code = "en";
    public static final String Arabic_code = "ar";
    public static final String Chinese_code = "zh";
    public static final String Russian_code = "ru";


    public static final String access_token = "access_token";
    public static final String Authorization = "Authorization";
    public static final String LanguageId = "LanguageId";

    public static final String CurrentPassword = "CurrentPassword";
    public static final String NewPassword = "NewPassword";
    public static final String ConfirmPassword = "ConfirmPassword";


    public static final String UserDetailModel = "UserDetailModel";
    public static final String PageIndex = "PageIndex";
    public static final String RegionId = "RegionId";
    public static final String CountryId = "CountryId";
    public static final String CategoryId = "CategoryId";
    public static final String isWorldWide = "isWorldWide";
    public static final String PollAlgo = "PollAlgo";
    public static final String Skip = "Skip";

    public static final String Regions = "Regions";
    public static final String IsProfileSetUp = "IsProfileSetUp";
    public static final String PollId = "PollId";
    public static final String OptionId = "OptionId";
    public static String mObject="mObject";
    public static final String Email = "Email";
    public static String Favourite_Response_came = "Favourite_Response_came";


    public static String getBearerKey() {
        if (SharedPreferenceUtil.contains(Constant.access_token) && SharedPreferenceUtil.getString(Constant.access_token, null) != null) {
            Log.e("AuthorizationKey", "Bearer" + " " + SharedPreferenceUtil.getString(Constant.access_token, ""));
            return "Bearer" + " " + SharedPreferenceUtil.getString(Constant.access_token, "");
        }
        return "";
    }


    public static boolean isLoginUser() {
        if (SharedPreferenceUtil.contains(Constant.isLogin) && SharedPreferenceUtil.getBoolean(Constant.isLogin, false)) {
            Log.e("IsLogin", "" + SharedPreferenceUtil.getBoolean(Constant.isLogin, false));
            return SharedPreferenceUtil.getBoolean(Constant.isLogin, false);
        }
        return false;
    }



    public static String getLanguageCode() {
        if (SharedPreferenceUtil.contains(Constant.CURRENT_LANUGUAGE) && SharedPreferenceUtil.getString(Constant.CURRENT_LANUGUAGE, null) != null) {
            Log.e("languageId", "::" + SharedPreferenceUtil.getString(Constant.CURRENT_LANUGUAGE, ""));
            return SharedPreferenceUtil.getString(Constant.CURRENT_LANUGUAGE, "");
        }
        return "";
    }

    public static String getEmail() {
        if (SharedPreferenceUtil.contains(Constant.Email) && SharedPreferenceUtil.getString(Constant.Email, null) != null) {
            Log.e("email", "::" + SharedPreferenceUtil.getString(Constant.Email, ""));
            return SharedPreferenceUtil.getString(Constant.Email, "");
        }
        return "";
    }

    public static int getRegionId() {
        if (SharedPreferenceUtil.contains(Constant.RegionId)) {
            Log.e("languageId", "::" + SharedPreferenceUtil.getInt(Constant.RegionId, 0));
            return SharedPreferenceUtil.getInt(Constant.RegionId, 0);
        }
        return 0;
    }

    public static int getCountryId() {
        if (SharedPreferenceUtil.contains(Constant.CountryId)) {
            Log.e("CountryId", "::" + SharedPreferenceUtil.getInt(Constant.CountryId, 0));
            return SharedPreferenceUtil.getInt(Constant.CountryId, 0);
        }
        return 0;
    }



    public static String getCountryCode() {
        TelephonyManager tm = (TelephonyManager) getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String country = null;
        if (tm != null) {
            country = tm.getSimCountryIso();
        }
        return country;
    }


    public static int getLanguageId()
    {
        String str= SharedPreferenceUtil.getString(Constant.CURRENT_LANUGUAGE,"");
        switch (str) {
            case Constant.English_code:
                return 1;
            case Constant.Arabic_code:
                return 2;
            case Constant.Chinese_code:
                return 3;
            case Constant.Russian_code:
                return 4;
        }
        return 0;
    }

    public static String getLanguageName()
    {
        String str= SharedPreferenceUtil.getString(Constant.CURRENT_LANUGUAGE,"");
        switch (str) {
            case Constant.English_code:
                return English;
            case Constant.Arabic_code:
                return Arabic;
            case Constant.Chinese_code:
                return Chinese;
            case Constant.Russian_code:
                return Russian;
        }
        return "";
    }

    public static boolean isProfileSetUp() {
        if (SharedPreferenceUtil.contains(Constant.IsProfileSetUp) ) {
            return SharedPreferenceUtil.getBoolean(Constant.IsProfileSetUp, false);
        }
        return false;
    }

    public static List<ModelCategories> getCategoriesList() {
        List<ModelCategories> list = new ArrayList<>();
        if (SharedPreferenceUtil.contains(Constant.CategoriesList) &&
                SharedPreferenceUtil.getString(Constant.CategoriesList, null) != null) {
            String strList= SharedPreferenceUtil.getString(Constant.CategoriesList,null);
            Type type = new TypeToken<List<ModelCategories>>() {}.getType();
            list = new Gson().fromJson(strList, type);
            Log.e( "onResponse::","categoryList:"+   list.toString());
            return list;
        }
        return list;
    }

    public static List<ModelRegions> getRegionsList() {
        List<ModelRegions> list = new ArrayList<>();
        if (SharedPreferenceUtil.contains(Constant.Regions) &&
                SharedPreferenceUtil.getString(Constant.Regions, null) != null) {
            String strList= SharedPreferenceUtil.getString(Constant.Regions,null);
            Type type = new TypeToken<List<ModelRegions>>() {}.getType();
            list = new Gson().fromJson(strList, type);
            Log.e( "onResponse::","RegionList:"+   list.toString());
            return list;
        }
        return list;
    }


    public static ModelUserDetail getUserModel() {
        ModelUserDetail modelUserDetails = new ModelUserDetail();
        if (SharedPreferenceUtil.contains(Constant.UserDetailModel) && SharedPreferenceUtil.getString(Constant.UserDetailModel, null) != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ModelUserDetail>() {}.getType();
            modelUserDetails = gson.fromJson(SharedPreferenceUtil.getString(Constant.UserDetailModel, null), type);
            return modelUserDetails;
        }
        return modelUserDetails;
    }
}
