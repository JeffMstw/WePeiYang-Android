package com.twt.service.bike.bike.bikeAuth;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.twt.service.R;
import com.twt.service.bike.common.ui.PActivity;

import butterknife.InjectView;


/**
 * Created by jcy on 2016/8/7.
 */

public class BikeAuthActivity extends PActivity<BikeAuthPresenter> implements BikeAuthController{
    @InjectView(R.id.bike_auth_button)
    Button mButton;
    @InjectView(R.id.bike_card)
    Button mCardButton;
    @InjectView(R.id.bind_card)
    Button mBindButton;
    @Override
    protected BikeAuthPresenter getPresenter() {
        return new BikeAuthPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bike_auth;
    }

    @Override
    protected void actionStart(Context context) {

    }

    @Override
    protected int getStatusbarColor() {
        return android.R.color.holo_blue_bright;
    }

    @Override
    protected void initView() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getBikeToken();
            }
        });
        mCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getBikeCard("130727199709231059");
            }
        });
        mBindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.bindBikeCard();
            }
        });
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }
}
