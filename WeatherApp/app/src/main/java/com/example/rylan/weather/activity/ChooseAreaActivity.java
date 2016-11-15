package com.example.rylan.weather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rylan.weather.R;
import com.example.rylan.weather.module.City;
import com.example.rylan.weather.module.District;
import com.example.rylan.weather.module.Province;
import com.example.rylan.weather.module.WeatherDB;
import com.example.rylan.weather.util.HttpCallbackListener;
import com.example.rylan.weather.util.HttpUtil;
import com.example.rylan.weather.util.LogUtil;
import com.example.rylan.weather.util.Utility;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends Activity {

    private static final String CITY_INFO = "http://v.juhe.cn/weather/citys";

    public static final String API_KEY = "9508aa89e1a3c57692f29136e9fd6333";

    public static final String CITY_SELECTED = "city_selected";
    public static final String EXTRA_DISTRACT_NAME = "district_name";
    private static final String KEY_CURRENT_LEVEL = "current_level";
    private static final String KEY_SELECTED_PROVINCE_ID = "selected_province_id";
    private static final String KEY_SELECTED_CITY_ID = "selected_city_id";
    private static final String KEY_SELECTED_PROVINCE_NAME = "selected_province_name";
    private static final String KEY_SELECTED_CITY_NAME = "selected_city_name";


    private static final int LEVEL_PROVINCE = 1;
    private static final int LEVEL_CITY = 2;
    private static final int LEVEL_DISTRICT = 3;

    private ProgressDialog mProgressDialog;
    private TextView mTitleText;
    private ListView mListView;
    // Province City District 共用 mDataList，是传入 listView adapter 的第三个参数
    private List<String> mDataList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private WeatherDB mWeatherDB;

    /**
     * 省列表
     */
    private List<Province> mProvinceList;
    /**
     * 市列表
     */
    private List<City> mCityList;
    /**
     * 县列表
     */
    private List<District> mDistrictList;
    /**
     * 选中的省份
     */
    private Province mSelectedProvince;
    private int mSelectedProvinceId;
    private String mSelectedProvinceName;
    /**
     * 选中的城市
     */
    private City mSelectedCity;
    private int mSelectedCityId;
    private String mSelectedCityName;
    /**
     * 当前选中的级别
     */
    private int mCurrentLevel = 0;

    /**
     *  是否从WeatherActivity 中跳转过来。
     */
    private boolean mIsFromWeatherActivity;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_LEVEL, mCurrentLevel);

        outState.putInt(KEY_SELECTED_PROVINCE_ID, mSelectedProvinceId);
        outState.putString(KEY_SELECTED_PROVINCE_NAME, mSelectedProvinceName);

        outState.putInt(KEY_SELECTED_CITY_ID, mSelectedCityId);
        outState.putString(KEY_SELECTED_CITY_NAME, mSelectedCityName);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.log("onCreate", "onCreate", LogUtil.DEBUG);
        super.onCreate(savedInstanceState);


        mIsFromWeatherActivity = getIntent()
                .getBooleanExtra(WeatherActivity.EXTRA_FROM_WEATHER_ACTIVITY, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean citySelected = preferences.getBoolean(CITY_SELECTED, false);
        //  已经选择了城市且不是从WeatherActivity 跳转过来，才会直接跳转到WeatherActivity
        if (!mIsFromWeatherActivity && citySelected) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);

        mListView = (ListView) findViewById(R.id.list_view);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDataList);
        mListView.setAdapter(mAdapter);

        mWeatherDB = WeatherDB.getInstance(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.log("onItemClick", "onItemClick", LogUtil.DEBUG);
                if (mCurrentLevel == LEVEL_PROVINCE) {
                    // 当前选择的省被保存下来用来 查询城市信息
                    mSelectedProvince = mProvinceList.get(position);
                    mSelectedProvinceId = mSelectedProvince.getId();
                    mSelectedProvinceName = mSelectedProvince.getProvinceName();
                    LogUtil.log("OnItemClickListener", "mSelectedProvince = " + mSelectedProvince.getProvinceName(), LogUtil.DEBUG);
                    queryCities();
                } else if (mCurrentLevel == LEVEL_CITY) {
                    // 当前选择的城市被保存下来用来 查询地区信息
                    mSelectedCity = mCityList.get(position);
                    mSelectedCityId = mSelectedCity.getId();
                    mSelectedCityName = mSelectedCity.getCityName();
                    LogUtil.log("OnItemClickListener", "mSelectedCity = " + mSelectedCity.getCityName(), LogUtil.DEBUG);
                    queryDistricts();
                } else if (mCurrentLevel == LEVEL_DISTRICT) {
                    String districtName = mDistrictList.get(position).getDistrictName();
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra(EXTRA_DISTRACT_NAME, districtName);
                    startActivity(intent);
                    finish();
                }
            }
        });

        if (savedInstanceState != null) {
            mCurrentLevel = savedInstanceState.getInt(KEY_CURRENT_LEVEL, 0);
            if (mCurrentLevel == LEVEL_CITY) {
                mSelectedProvinceId = savedInstanceState.getInt(KEY_SELECTED_PROVINCE_ID);
                mSelectedProvinceName = savedInstanceState.getString(KEY_SELECTED_PROVINCE_NAME);
                queryCities();
            } else if (mCurrentLevel == LEVEL_DISTRICT) {
                mSelectedCityId = savedInstanceState.getInt(KEY_SELECTED_CITY_ID);
                mSelectedCityName = savedInstanceState.getString(KEY_SELECTED_CITY_NAME);
                queryDistricts();
            }

        }
        if (mCurrentLevel == 0) {
            queryProvinces(); // 加载省级数据
        }
    }

    /**
     * 查询全国所有的省，并把省份显示在 listView 上
     * 优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryProvinces() {
        LogUtil.log("queryProvinces", "queryProvinces", LogUtil.DEBUG);
        mProvinceList = mWeatherDB.loadProvince();
        if (mProvinceList.size() > 0) {
            // Province City District 共用 mDataList 所以要清空
            mDataList.clear();
            for (Province province : mProvinceList) {
                mDataList.add(province.getProvinceName());
            }

            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTitleText.setText("中国");
            mCurrentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer("Province");
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCities() {
        LogUtil.log("queryCities", "mSelectedProvince.getId =" + mSelectedProvinceId, LogUtil.DEBUG);
        mCityList = mWeatherDB.loadCity(mSelectedProvinceId);
        if (mCityList.size() > 0) {
            mDataList.clear();
            for (City city : mCityList) {
                mDataList.add(city.getCityName());
            }

            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            //mTitleText.setText(mSelectedProvince.getProvinceName());
            mTitleText.setText(mSelectedProvinceName);
            mCurrentLevel = LEVEL_CITY;
        } else {
            queryFromServer("City");
            LogUtil.log("queryCities", "queryFromServer", LogUtil.DEBUG);
        }
    }

    private void queryDistricts() {
        mDistrictList = mWeatherDB.loadDistrict(mSelectedCityId);
        if (mDistrictList.size() > 0) {
            mDataList.clear();
            for (District district : mDistrictList) {
                mDataList.add(district.getDistrictName());
            }

            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTitleText.setText(mSelectedCityName);
            mCurrentLevel = LEVEL_DISTRICT;
        } else {
            queryFromServer("District");
        }
    }

    /**
     * 根据传入的类型从服务器上查询省市县数据
     */
    private void queryFromServer(final String type) {
        showProgressDialog();

        String address = CITY_INFO + "?key=" + API_KEY;
        // 发起连接
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(InputStream in) {
                // 调用 Utility.handleResponse() 方法处理数据并保存到数据库（三张表），成功返回 true
                boolean result = Utility.handleResponse(mWeatherDB, in);
                if (result) {
                    //通过runOnUiThread()方法回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            switch (type) {
                                case "Province":
                                    // 此时本地数据库中已有数据，再次调用了 queryProvinces()方法来重新加载省级数据
                                    // queryProvinces()方法牵扯到了 UI 操作，因此必须要在主线程中调用
                                    // 这里借助了 runOnUiThread()方法来实现从子线程切换到主线程
                                    queryProvinces();
                                    break;
                                case "City":
                                    queryCities();
                                    break;
                                case "District":
                                    queryDistricts();
                                    break;
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentLevel == LEVEL_DISTRICT) {
            queryCities();
        } else if (mCurrentLevel == LEVEL_CITY) {
            queryProvinces();
        } else {
            if (mIsFromWeatherActivity) {
                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }
}