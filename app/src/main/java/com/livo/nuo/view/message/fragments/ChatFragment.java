package com.livo.nuo.view.message.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.livo.nuo.BuildConfig;
import com.livo.nuo.R;
import com.livo.nuo.utility.AppUtils;
import com.livo.nuo.view.message.adapters.ChatAdapter;
import com.livo.nuo.view.message.pubnub.History;
import com.livo.nuo.view.message.services.ConnectivityListener;
import com.livo.nuo.view.message.util.Helper;
import com.livo.nuo.view.message.view.EmptyView;
import com.livo.nuo.view.message.view.MessageComposer;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import com.livo.nuo.view.message.pubnub.Message;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import butterknife.BindView;

public class ChatFragment extends ParentFragment implements MessageComposer.Listener, ConnectivityListener {

    private static final String ARGS_CHANNEL = "whiteboard1";
    private static String statusfrag;
    //ARGS_CHANNEL
    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.chat_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.chat_recycler_view)
    // tag::HIS-4.1[]
    RecyclerView mChatsRecyclerView;
    // end::HIS-4.1[]

    @BindView(R.id.chats_message_composer)
    MessageComposer mMessageComposer;

    @BindView(R.id.emptview)
    View emptview;

    @BindView(R.id.view)
    View view;

    @BindView(R.id.chat_empty_view)
    EmptyView mEmptyView;

    // tag::HIS-4.2[]
    private ChatAdapter mChatAdapter;
    private List<Message> mMessages = new ArrayList<>();
    List<Message> othermMessages=new ArrayList<>();
    // end::HIS-4.2[]

    Integer typeing_off_count=0;
    private String mChannel;
    private SubscribeCallback mPubNubListener;

    private RecyclerView.OnScrollListener mOnScrollListener;

    public static ChatFragment newInstance(String channel,String status) {
        statusfrag= status;
        Bundle args = new Bundle();
        args.putString(ARGS_CHANNEL, channel);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int provideLayoutResourceId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void setViewBehaviour(boolean viewFromCache) {
        if (!viewFromCache) {
            setHasOptionsMenu(true);
            initializeScrollListener();
            prepareRecyclerView();
            mSwipeRefreshLayout.setRefreshing(true);
            subscribe();

            if (!statusfrag.isEmpty()){
                mMessageComposer.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
                emptview.setVisibility(View.VISIBLE);
            }
            else{
                mMessageComposer.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                emptview.setVisibility(View.GONE);
            }
        }
    }

    private void prepareRecyclerView() {

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this::fetchHistory);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(fragmentContext);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(true);
        mChatsRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(fragmentContext, RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.chats_divider));
        mChatsRecyclerView.addItemDecoration(dividerItemDecoration);

        mChatsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mChatAdapter = new ChatAdapter(mChannel);
        mChatsRecyclerView.setAdapter(mChatAdapter);

        mMessageComposer.setListener(this);



        // tag::HIS-5.2[]

        mChatsRecyclerView.addOnScrollListener(mOnScrollListener);
        // end::HIS-5.2[]
    }

    // tag::HIS-5.1[]
    private void initializeScrollListener() {
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstCompletelyVisibleItemPosition =
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                if (firstCompletelyVisibleItemPosition == History.TOP_ITEM_OFFSET && dy < 0) {
                    fetchHistory();
                }
            }
        };
    }
    // end::HIS-5.1[]

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chat_info:
                hostActivity.addFragment(ChatInfoFragment.newInstance(mChannel));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public String setScreenTitle() {
        hostActivity.enableBackButton(false);
        scrollChatToBottom();
        loadCurrentOccupancy();
        return mChannel;
    }

    @Override
    public void extractArguments() {
        super.extractArguments();
        mChannel = getArguments().getString(ARGS_CHANNEL);
    }

    @Override
    public void onReady() {
        initListener();



        // tag::FRG-2[]
        // tag::ignore[]
        /*
        // end::ignore[]
        hostActivity.getPubNub();
        // tag::ignore[]
        */
        // end::ignore[]
        // end::FRG-2[]
    }

    // tag::SUB-2[]
    private void initListener() {



        mPubNubListener = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getOperation() == PNOperationType.PNSubscribeOperation && status.getAffectedChannels()
                        .contains(mChannel)) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    fetchHistory();
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                runOnUiThread(() ->{
                AppUtils.Companion.hideKeyboard(getActivity());
                loadAdapter();
                handleNewMessage(message);


                if (typeing_off_count==1){}
                else {
                    typeing_off_count = 1;
                    sendSignal("typing_off");
                }
                });
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                if (presence.getChannel().equals(mChannel)) {
                    int members = presence.getOccupancy();
                    runOnUiThread(() -> //hostActivity.setSubtitle(fragmentContext.getResources().getQuantityString(R.plurals.members_online, members, members))
                    {});
                }
            }

            @Override
            public void signal(@NotNull PubNub pubnub,  PNSignalResult pnSignalResult) {

                Log.e("signal", String.valueOf(pnSignalResult.getMessage()));

                runOnUiThread(() -> {

                String text="";
                try {
                    JSONObject jsonObj = new JSONObject(String.valueOf(pnSignalResult.getMessage()));
                        text = jsonObj.getString("text");
                    } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (text.equals("typing_off")){
                    if (typeing_off_count==0){
                        loadAdapter();
                    typeing_off_count=1;}
                    else{ }
                    }
                    else if(text.equals("typing_on")){
                    typeing_off_count=0;
                        handleNewSignal(pnSignalResult);
                    }

                });

                Log.e("typing",typeing_off_count.toString());
            }

            @Override
            public void uuid(@NotNull PubNub pubnub, @NotNull PNUUIDMetadataResult pnUUIDMetadataResult) {

            }

            @Override
            public void channel(@NotNull PubNub pubnub, @NotNull PNChannelMetadataResult pnChannelMetadataResult) {

            }

            @Override
            public void membership(@NotNull PubNub pubnub, @NotNull PNMembershipResult pnMembershipResult) {

            }

            @Override
            public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnMessageActionResult) {

            }

            @Override
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {

            }
        };
    }
    // end::SUB-2[]

    private void loadCurrentOccupancy() {
        hostActivity.getPubNub()
                .hereNow()
                .channels(Arrays.asList(mChannel))
                .async(new PNCallback<PNHereNowResult>() {
                    @Override
                    public void onResponse(PNHereNowResult result, PNStatus status) {
                        if (!status.isError()) {
                            int members = result.getTotalOccupancy();
                           // hostActivity.setSubtitle(fragmentContext.getResources().getQuantityString(R.plurals.members_online, members, members));
                        }
                    }
                });
    }

    // tag::MSG-1[]
    private void handleNewMessage(PNMessageResult message) {
        if (message.getChannel().equals(mChannel)) {
            Message msg = Message.serialize(message);
            mMessages.add(msg);
            History.chainMessages(mMessages, mMessages.size());
            runOnUiThread(() -> {
                if (mEmptyView.getVisibility() == View.VISIBLE) {
                    mEmptyView.setVisibility(View.GONE);
                }
                mChatAdapter.update(mMessages);
                scrollChatToBottom();
            });
        }
    }
    // end::MSG-1[]

    // tag::SUB-1[]
    private void subscribe() {
        hostActivity.getPubNub()
                .subscribe()
                .channels(Collections.singletonList(mChannel))
                .withPresence()
                .execute();

    }
    // end::SUB-1[]

    // tag::HIS-1[]
    private void fetchHistory() {
        if (History.isLoading()) {
            return;
        }
        History.setLoading(true);
        mSwipeRefreshLayout.setRefreshing(true);
        History.getAllMessages(hostActivity.getPubNub(), mChannel, getEarliestTimestamp(),
                new History.CallbackSkeleton() {
                    @Override
                    public void handleResponse(List<Message> newMessages) {
                        if (!newMessages.isEmpty()) {
                            mMessages.addAll(0, newMessages);
                            History.chainMessages(mMessages, mMessages.size());
                            runOnUiThread(() -> mChatAdapter.update(mMessages));
                        } else if (mMessages.isEmpty()) {
                            runOnUiThread(() -> mEmptyView.setVisibility(View.VISIBLE));
                        } else {
                           // runOnUiThread(() -> Toast.makeText(fragmentContext,"No More Messages",Toast.LENGTH_SHORT).show());
                        }
                        runOnUiThread(() -> {
                            mSwipeRefreshLayout.setRefreshing(false);
                            Log.d("new_arrival", "size: " + mMessages.size());
                        });
                        History.setLoading(false);
                    }
                });
    }
    // end::HIS-1[]

    private Long getEarliestTimestamp() {
        if (mMessages != null && !mMessages.isEmpty()) {
            return mMessages.get(0).getTimetoken();
        }
        return null;
    }

    @Override
    public void onDestroy() {
        hostActivity.getPubNub().removeListener(mPubNubListener);
        super.onDestroy();
    }

    // tag::SEND-2[]
    @Override
    public void onSentClick(String message) {

        AppUtils.Companion.hideKeyboard(getActivity());
        //loadAdapter();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
          String message=msg;*/
        // tag::ignore[]
        if (TextUtils.isEmpty(message)) {
            if (BuildConfig.DEBUG) {
                StringBuilder messageBuilder = new StringBuilder("");
                messageBuilder.append(UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.US));
                messageBuilder.append("\n");
                messageBuilder.append(Helper.parseDateTime(System.currentTimeMillis()));
                message = messageBuilder.toString();
            } else {
                return;
            }
        }
        // end::ignore[]
        String finalMessage = message;

        hostActivity.getPubNub()
                .publish()
                .channel(mChannel)
                .shouldStore(true)
                .message(Message.newBuilder().text(message).build())
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (!status.isError()) {
                            long newMessageTimetoken = result.getTimetoken();
                        } else {
                            Message msg = Message.createUnsentMessage(Message.newBuilder().text(finalMessage).build());
                            mMessages.add(msg);
                            History.chainMessages(mMessages, mMessages.size());
                            runOnUiThread(() -> {
                                if (mEmptyView.getVisibility() == View.VISIBLE) {
                                    mEmptyView.setVisibility(View.GONE);
                                }
                                mChatAdapter.update(mMessages);
                                scrollChatToBottom();

                                Toast.makeText(fragmentContext, "Message Not Sent", Toast.LENGTH_SHORT).show();

                            });
                        }
                    }
                });

           /* }
        }, 1000);*/
    }
    // end::SEND-2[]

    // tag::MSG-3[]
    private void scrollChatToBottom() {
        mChatsRecyclerView.scrollToPosition(mMessages.size() - 1);
    }
    // end::MSG-3[]

    @Override
    public SubscribeCallback provideListener() {
        return mPubNubListener;
    }

    @Override
    public void onConnected() {

    }

    public void showSoftKeyboard(View view) {


        if(view.requestFocus()){
            InputMethodManager imm =(InputMethodManager)fragmentContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void sendSignal(String value){

        if (TextUtils.isEmpty(value)) {
            if (BuildConfig.DEBUG) {
                StringBuilder messageBuilder = new StringBuilder("");
                messageBuilder.append(UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.US));
                messageBuilder.append("\n");
                messageBuilder.append(Helper.parseDateTime(System.currentTimeMillis()));
                value = messageBuilder.toString();
            } else {
                return;
            }
        }

        String finalMessage = value;

        hostActivity.getPubNub()
                .signal()
                .message(Message.newBuilder().text(value).build())
                .channel(mChannel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult pnPublishResult, PNStatus pnStatus) {
                        if (pnStatus.isError()) {
                            Long timetoken = pnPublishResult.getTimetoken(); // signal message timetoken
                        } else {
                            //Log.e()
                        }
                    }
                });
    }


    private void handleNewSignal(PNSignalResult message) {

        if (message.getChannel().equals(mChannel)) {
            Message msg = Message.serialize(message);
            //othermMessages.clear();
            //othermMessages.addAll(mMessages);
            Log.e("jui",String.valueOf(othermMessages));
            mMessages.add(msg);
                mChatAdapter.addAt(msg);
                scrollChatToBottom();

        }
    }

    public void loadAdapter()
    {

            mMessages.remove(mMessages.size()-1);
            mChatAdapter.removeAt(mMessages.size());
           // mChatAdapter.update(mMessages);
            scrollChatToBottom();

    }
}
