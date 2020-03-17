package com.hurry.custom.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hurry.custom.R;
import com.hurry.custom.common.Constants;
import com.hurry.custom.common.db.PreferenceUtils;
import com.hurry.custom.controller.GetCity;
import com.hurry.custom.view.BaseActivity;
import com.hurry.custom.view.activity.HomeActivity;
import com.hurry.custom.view.adapter.CityAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 3/18/2017.
 */

public class LocationActivity extends BaseActivity {


    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);

        showProgressDialog();
        new GetCity(this,"").execute();
    }


    public void setupRecyclerView() {
        if(Constants.cityModels!= null && Constants.cityModels.size() > 0){
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(new CityAdapter(this,
                    Constants.cityModels, recyclerView));
        }else{
            recyclerView.setAdapter(null);
        }
    }

    public void goToMain(){
        PreferenceUtils.setFirstStart(LocationActivity.this, false);
        Intent intent = new Intent(LocationActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
