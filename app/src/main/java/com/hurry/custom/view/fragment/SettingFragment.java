/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hurry.custom.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hurry.custom.R;
import com.hurry.custom.common.Constants;
import com.hurry.custom.common.db.PreferenceUtils;
import com.hurry.custom.controller.GetCity;
import com.hurry.custom.view.activity.HomeActivity;
import com.hurry.custom.view.activity.MainActivity;
import com.hurry.custom.view.activity.login.LoginActivity;
import com.hurry.custom.view.activity.setting.AboutUsActivity;
import com.hurry.custom.view.activity.setting.ContactUsActivity;
import com.hurry.custom.view.activity.setting.PrivacyPolicyActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.txt_about_us)
    TextView txtAboutUs;
    @BindView(R.id.txt_contact_us)
    TextView txtContactUs;
    @BindView(R.id.txt_privacy_policy)
    TextView txtPrivacyPolicy;
    @BindView(R.id.txt_rate)
    TextView txtRate;
    @BindView(R.id.txt_share)
    TextView txtShare;
    @BindView(R.id.text_location)
    TextView txtLocation;

    @BindView(R.id.txt_sign_out)
    TextView txtSignOut;
    @BindView(R.id.img_sign_out)
    ImageView imgSignOut;

    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = (View) inflater.inflate(
                R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        position = 3;
        initView();
        return view;

    }

    private void initView() {
        txtAboutUs.setOnClickListener(this);
        txtContactUs.setOnClickListener(this);
        txtPrivacyPolicy.setOnClickListener(this);
        txtSignOut.setOnClickListener(this);
        imgSignOut.setOnClickListener(this);
        txtRate.setOnClickListener(this);
        txtShare.setOnClickListener(this);
        txtLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.text_location:
                if (Constants.cityModels.size() == 0) {
                    new GetCity(mContext, "show").execute();
                } else {
                    ((HomeActivity) mContext).showConfirmDialog();
                }
                break;


            case R.id.txt_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                String shareBody = "http://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.txt_rate:
                Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
                }

                break;

            case R.id.txt_about_us:
                Intent about = new Intent(mContext, AboutUsActivity.class);
                startActivity(about);
                ((HomeActivity) mContext).setIndex(3); // setting page
                break;

            case R.id.txt_contact_us:
                Intent contact = new Intent(mContext, ContactUsActivity.class);
                startActivity(contact);
                ((HomeActivity) mContext).setIndex(3); // setting page
                break;

            case R.id.txt_privacy_policy:
                Intent privacy = new Intent(mContext, PrivacyPolicyActivity.class);
                startActivity(privacy);
                ((HomeActivity) mContext).setIndex(3); // setting page
                break;

            case R.id.img_sign_out:
            case R.id.txt_sign_out:

                new AlertDialog.Builder(mContext)
                        .setMessage("Do you want to logout ?")
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Constants.clearData();
                                PreferenceUtils.setLogOut(mContext);
                                if (Constants.MODE == Constants.PERSONAL) {
                                    PreferenceUtils.setEmail(mContext, "");
                                    PreferenceUtils.setPassword(mContext, "");
                                } else if (Constants.MODE == Constants.CORPERATION) {
                                    PreferenceUtils.setCorEmail(mContext, "");
                                    PreferenceUtils.setCorPassword(mContext, "");
                                }
                                Constants.MODE = Constants.PERSONAL;
                                PreferenceUtils.setMode(mContext, Constants.PERSONAL);
                                PreferenceUtils.setCityId(mContext, -1);

                                Intent login = new Intent(mContext, LoginActivity.class);
                                login.putExtra("type", "close");
                                startActivity(login);


                                ((HomeActivity)mContext).finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                break;
        }
    }

}