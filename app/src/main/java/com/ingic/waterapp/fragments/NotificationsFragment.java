package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.NotificationCountEnt;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.SimpleDividerItemDecoration;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.NotificationsListAdapter;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;
import com.ingic.waterapp.ui.views.TitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends BaseFragment implements OnViewHolderClick {
    @BindView(R.id.rv_notications)
    RecyclerView rvNotifcations;
//    @BindView(R.id.ll_noNotifications)
//    LinearLayout llNoNotifications;
    Unbinder unbinder;
    RecyclerViewListAdapter adapter;

    /*todo change its type*/
    List<NotificationCountEnt> notificationsEnts;


    public NotificationsFragment() {
        // Required empty public constructor
    }

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (prefHelper.getLoginTYpe() == AppConstants.NOT_REGISTERED_USER)
//            llNoNotifications.setVisibility(View.VISIBLE);
//        else
        serviceHelper.enqueueCall(webService.getNotifications(prefHelper.getUser().getToken()),
                WebServiceConstants.getNotifications);
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new NotificationsListAdapter(getDockActivity(), this);
        rvNotifcations.setLayoutManager(new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false));
        rvNotifcations.addItemDecoration(new SimpleDividerItemDecoration(getDockActivity()));
        rvNotifcations.setAdapter(adapter);
//        ArrayList<String> list = new ArrayList<>();
//        list.add("Lorem Ipsum is simply dummy text of the printing.");
//        list.add("Lorem Ipsum is simply dummy text of the printing.");
//        list.add("Lorem Ipsum is simply dummy text of the printing.");
//        adapter.addAll(list);
        adapter.addAll(notificationsEnts);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getDrawerLayout() != null)
            getDockActivity().lockDrawer();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.notifications));
//        titleBar.setTitleBarTextColor(getResources().getColor(R.color.white));
//        titleBar.setTitleBarBackgroundColor(getResources().getColor(R.color.theme_green));
        titleBar.showBackButton();
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {
            case WebServiceConstants.getNotifications:
                notificationsEnts = (List<NotificationCountEnt>) result;
                break;
        }
    }
}
