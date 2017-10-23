package com.twt.service.home.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.twt.service.R
import com.twt.service.base.BaseFragment
import com.twt.service.home.common.gpaItem.GpaItemViewModel
import com.twt.service.home.common.schedule.ScheduleViewModel

/**
 * Created by retrox on 22/10/2017.
 */
class CommonFragmentNew : BaseFragment() {

    lateinit var recyclerview: RecyclerView
    lateinit var tadapter: CommonPageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_commons_new, container, false)
        recyclerview = view.findViewById(R.id.recyclerView)
        val viewmodel = ViewModelProviders.of(this).get(GpaItemViewModel::class.java)
//        val scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        val scheduleViewModel = ScheduleViewModel(this.activity as RxAppCompatActivity?)
        //todo 修改课程表暴露的接口 （重载）
        val list = listOf<ViewModel>(scheduleViewModel,viewmodel)
        tadapter = CommonPageAdapter(list,this.activity,this)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this.context)
            this.adapter = tadapter
        }
        return view
    }


}