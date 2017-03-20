package com.example.coolweather.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.MainActivity;
import com.example.coolweather.R;
import com.example.coolweather.WeatherActivity;
import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;
import com.example.coolweather.util.GsonUtil;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.LogUtil;
import com.example.coolweather.util.SaveUtil;
import com.example.coolweather.util.UilUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AllInfo extends Fragment {

    @InjectView(R.id.show_portInfo)
    TextView showPortInfo;
    @InjectView(R.id.bn)
    Button bn;
    @InjectView(R.id.ls)
    ListView ls;
    private ArrayAdapter<String> adapter;

    private List<String> dataList;

    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    private int counterLevel;

    private Province selectProvince;

    private List<Province> provinceList;
    private ProgressDialog progressDialog;
    private List<City> cityList;
    private City selcetCity;
    private List<County> countyList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_info, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        ls.setAdapter(adapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (counterLevel == LEVEL_PROVINCE) {

                    selectProvince = provinceList.get(position);

                    queryCity();

                } else if (counterLevel == LEVEL_CITY) {
                    selcetCity = cityList.get(position);
                    queryCounty();
                } else if (counterLevel == LEVEL_COUNTY) {

                    if (getActivity() instanceof MainActivity) {

                        County county = countyList.get(position);
                        Intent intent = new Intent(getContext(), WeatherActivity.class);
                        intent.putExtra("weather_id", county.getWeatherId());
                        startActivity(intent);
                        getActivity().finish();


                    } else if (getActivity() instanceof WeatherActivity) {

                        County county = countyList.get(position);

                        WeatherActivity weatherActivity = (WeatherActivity) getActivity();

                        weatherActivity.drawerLayout.closeDrawers();

                        weatherActivity.requestInfo(county.getWeatherId());


                    }

                }
            }
        });

        queryProvince();
    }

    private void queryProvince() {

        bn.setVisibility(View.GONE);

        provinceList = DataSupport.findAll(Province.class);

        if (provinceList.size() > 0) {

            dataList.clear();

            for (Province temp : provinceList) {

                dataList.add(temp.getProvinceName());
            }

            adapter.notifyDataSetChanged();
            ls.setSelection(0);

        } else {
            String address = UilUtil.URL;
            requestData(address, "province");
        }

        counterLevel = LEVEL_PROVINCE;
    }

    private void queryCity() {

        showPortInfo.setText(selectProvince.getProvinceName());

        bn.setVisibility(View.VISIBLE);

        cityList = DataSupport.where("provinceId=?", String.valueOf(selectProvince.getProvinceCode()))
                .find(City.class);


        if (cityList.size() > 0) {

            dataList.clear();

            for (City temp : cityList) {

                dataList.add(temp.getCityName());
            }

            adapter.notifyDataSetChanged();
            ls.setSelection(0);

        } else {

            String address = UilUtil.URL + "/" + selectProvince.getProvinceCode();
            requestData(address, "city");

        }


        counterLevel = LEVEL_CITY;
    }

    private void queryCounty() {

        showPortInfo.setText(selcetCity.getCityName());
        bn.setVisibility(View.VISIBLE);

        countyList = DataSupport.where("cityId=?", String.valueOf(selcetCity.getCityCode())).find(County.class);


        if (countyList.size() > 0) {

            dataList.clear();

            for (County temp : countyList) {

                dataList.add(temp.getCountyName());
            }

            adapter.notifyDataSetChanged();
            ls.setSelection(0);

        } else {

            String address = UilUtil.URL + "/" + selectProvince.getId() + "/" + selcetCity.getCityCode();
            requestData(address, "county");

        }


        counterLevel = LEVEL_COUNTY;
    }


    @OnClick(R.id.bn)
    public void buttonOnclick() {

        if (counterLevel == LEVEL_COUNTY) {

            queryCity();

        } else if (counterLevel == LEVEL_CITY) {

            queryProvince();
        }


    }

    private void requestData(String address, final String type) {

        showDialogProgress();

        HttpUtil.sendOkHttpRequest(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                Toast.makeText(getContext(), "网络不佳", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String content = response.body().string();

                boolean results = false;

                if ("province".equals(type)) {

                    results = GsonUtil.handleProvinceResponse(content);

                } else if ("city".equals(type)) {

                    results = GsonUtil.handleCityResponse(content, selectProvince.getProvinceCode());

                } else if ("county".equals(type)) {

                    results = GsonUtil.handleCountyResponse(content, selcetCity.getCityCode());
                }

                if (results) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeDialogProgress();
                            if ("province".equals(type)) {

                                queryProvince();

                            } else if ("city".equals(type)) {

                                queryCity();

                            } else if ("county".equals(type)) {

                                queryCounty();
                            }
                        }
                    });
                }

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    private void showDialogProgress() {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeDialogProgress() {

        if (progressDialog != null) {

            progressDialog.dismiss();
        }

    }


}
