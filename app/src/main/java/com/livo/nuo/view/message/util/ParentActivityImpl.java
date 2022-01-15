package com.livo.nuo.view.message.util;

import androidx.fragment.app.Fragment;

import com.pubnub.api.PubNub;

// tag::INIT-2[]
public interface ParentActivityImpl {

    PubNub getPubNub();

    // tag::ignore[]
    void setTitle(String title);

    void setSubtitle(String subtitle);

    void addFragment(Fragment fragment);

    void enableBackButton(boolean enable);

    void backPress();
    // end::ignore[]
}
// end::INIT-2[]
