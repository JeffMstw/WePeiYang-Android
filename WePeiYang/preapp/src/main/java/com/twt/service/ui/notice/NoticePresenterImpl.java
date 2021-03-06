package com.twt.service.ui.notice;

import com.twt.service.bean.NewsList;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.NoticeInteractor;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/16.
 */
public class NoticePresenterImpl implements NoticePresenter, OnGetNoticeCallback {

    private NoticeView view;
    private NoticeInteractor interactor;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;
    private int page;


    public NoticePresenterImpl(NoticeView view, NoticeInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void refreshNoticeItems() {
        if (isRefreshing) {
            return;
        } else {
            page = 1;
            isRefreshing = true;
            view.showProgress();
            interactor.getNotice(page, this);
        }
    }

    @Override
    public void loadMoreNoticeItems() {
        if (isLoadingMore) {
            return;
        } else {
            isLoadingMore = true;
            page += 1;
            view.useFooter();
            interactor.getNotice(page, this);
        }
    }

    @Override
    public void onSuccess(NewsList newsList) {
        view.setRefreshEnable(false);
        if (isRefreshing) {
            isRefreshing = false;
            view.hideProgress();
            view.refreshItems(newsList.data);
        } else if (isLoadingMore) {
            isLoadingMore = false;
            view.hideFooter();
            view.loadMoreItems(newsList.data);
        }
    }

    @Override
    public void onFailure(RetrofitError error) {
        view.setRefreshEnable(false);
        isRefreshing = false;
        isLoadingMore = false;
        view.hideProgress();
        switch (error.getKind()){
            case HTTP:
                RestError restError = (RestError)error.getBodyAs(RestError.class);
                if (restError!=null){
                    view.toastMessage(restError.message);
                }
                break;
            case NETWORK:
                view.toastMessage("无法连接到网络");
                break;
            case CONVERSION:
            case UNEXPECTED:
                throw error;
            default:
                throw new AssertionError("未知的错误类型：" + error.getKind());
        }
    }
}
