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
import com.ingic.waterapp.entities.CompanyEnt;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.SimpleDividerItemDecoration;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.CompaniesListAdapter;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;
import com.ingic.waterapp.ui.views.TitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompaniesListFragment extends BaseFragment implements OnViewHolderClick {
    @BindView(R.id.rv_companies)
    RecyclerView rvNotifcations;
    Unbinder unbinder;
    RecyclerViewListAdapter adapter;

    List<CompanyEnt> companyEnts;
    int companyId = -1;

    public CompaniesListFragment() {
        // Required empty public constructor
    }

    public static CompaniesListFragment newInstance() {
        return new CompaniesListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_companies_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (prefHelper.getLoginTYpe() == AppConstants.NOT_REGISTERED_USER)
//            llNoNotifications.setVisibility(View.VISIBLE);
//        else
        serviceHelper.enqueueCall(webService.getCompany(),
                WebServiceConstants.getCompanies);
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new CompaniesListAdapter(getDockActivity(), this);
        rvNotifcations.setLayoutManager(new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false));
        rvNotifcations.addItemDecoration(new SimpleDividerItemDecoration(getDockActivity()));
        rvNotifcations.setAdapter(adapter);
//        ArrayList<String> list = new ArrayList<>();

  /*      if (companyEnts != null) {
            final CharSequence[] items = new CharSequence[companyEnts.size()];
            for (int i = 0; i < companyEnts.size(); i++) {
                items[i] = companyEnts.get(i).getFullName();
            }
        }
        list.add("The Oasis Water Company");
        list.add("Masafi Co.");
        list.add("Falcon Spring Drinking Water");
        list.add("Nestle Waters");
        list.add("Awafi Mineral Water");
        list.add("The Oasis Water Company");
        list.add("Masafi Co.");
        list.add("Falcon Spring Drinking Water");
        list.add("Nestle Waters");
        list.add("Awafi Mineral Water");*/
        adapter.addAll(companyEnts);

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
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.companies));
//        titleBar.setTitleBarTextColor(getResources().getColor(R.color.white));
//        titleBar.setTitleBarBackgroundColor(getResources().getColor(R.color.theme_green));
        titleBar.showBackButton();
    }

    @Override
    public void onItemClick(View view, int position) {

//        notImplemented();
    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {
            case WebServiceConstants.getCompanies:
                companyId = -1;
                companyEnts = (List<CompanyEnt>) result;
                adapter.addAll(companyEnts);

                break;
        }
    }
    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
