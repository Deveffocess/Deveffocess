package com.livo.nuo.view.message.adapters;

import static com.livo.nuo.view.message.util.ChatItem.TYPE_OWN_END;
import static com.livo.nuo.view.message.util.ChatItem.TYPE_OWN_HEADER_FULL;
import static com.livo.nuo.view.message.util.ChatItem.TYPE_OWN_HEADER_SERIES;
import static com.livo.nuo.view.message.util.ChatItem.TYPE_OWN_MIDDLE;
import static com.livo.nuo.view.message.util.ChatItem.TYPE_REC_END;
import static com.livo.nuo.view.message.util.ChatItem.TYPE_REC_HEADER_FULL;
import static com.livo.nuo.view.message.util.ChatItem.TYPE_REC_HEADER_SERIES;
import static com.livo.nuo.view.message.util.ChatItem.TYPE_REC_MIDDLE;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import com.livo.nuo.BuildConfig;
import com.livo.nuo.R;
import com.livo.nuo.lib.loadingloader.DotProgressBar;
import com.livo.nuo.view.message.prefs.Prefs;
import com.livo.nuo.view.message.pubnub.Message;
import com.livo.nuo.view.message.util.AndroidUtils;
import com.livo.nuo.view.message.util.Helper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private final String mChannel;

    // tag::BIND-2[]
    private List<Message> mItems;
    // end::BIND-2[]

    public ChatAdapter(String channel) {
        mChannel = channel;
        mItems = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    // tag::BIND-1[]
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_OWN_HEADER_FULL:
            case TYPE_OWN_HEADER_SERIES:
            case TYPE_OWN_MIDDLE:
            case TYPE_OWN_END:
                View sentMessageView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                return new MessageViewHolder(sentMessageView, viewType);
            case TYPE_REC_HEADER_FULL:
            case TYPE_REC_HEADER_SERIES:
            case TYPE_REC_MIDDLE:
            case TYPE_REC_END:
                View receivedMessageView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent, false);
                return new MessageViewHolder(receivedMessageView, viewType);
        }
        throw new IllegalStateException("No applicable viewtype found.");
    }
    // end::BIND-1[]

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bindData(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

        public void removeAt(int position) {
            mItems.remove(position);
            notifyItemRemoved(position);
            //notifyItemChanged(position);
           // notifyItemRangeChanged(position, mItems.size());

    }

    public void addAt(Message ms) {
        mItems.add(ms);
        notifyItemInserted(mItems.size());
        //notifyDataSetChanged();
        //notifyItemRangeChanged(getItemCount(), mItems.size());
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getTimetoken();
    }

    // tag::BIND-4[]
    public void update(List<Message> newData) {
        //DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(newData, mItems));
        //diffResult.dispatchUpdatesTo(this);
        mItems.clear();
        mItems.addAll(newData);
        notifyDataSetChanged();
    }

    // end::BIND-4[]

    class DateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_date)
        TextView mDateTextView;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(Long key) {
            mDateTextView.setText(Helper.parseDateTime(key));
        }
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private int mType;

        @BindView(R.id.root)
        RelativeLayout mRoot;

        @BindView(R.id.message_avatar)
        ImageView mAvatar;

        @BindView(R.id.message_sender)
        TextView mSender;

        @BindView(R.id.message_bubble)
        TextView mBubble;

        @BindView(R.id.message_timestamp)
        TextView mTimestamp;

        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.frame)
        FrameLayout frame;

        @BindView(R.id.llTyping)
        LinearLayout llTyping;

        @OnClick(R.id.root)
        void rootClick(View v) {
            if (BuildConfig.DEBUG) {
                showMessageInfoDialog(v.getContext(), mMessage);
            }
        }

        Message mMessage;

        MessageViewHolder(View itemView, int type) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            this.mType = type;

        }

        void bindData(Message message) {
            this.mMessage = message;
            llTyping.setVisibility(View.GONE);
            handleType();



            if (mMessage.getText().equals("typing_on"))
            {
                if (mMessage.getSenderId().equals(Prefs.get().uuid())) {
                    llMain.setVisibility(View.GONE);
                    frame.setVisibility(View.GONE);
                    llTyping.setVisibility(View.GONE);
                }

                else {
                    llMain.setVisibility(View.GONE);
                    frame.setVisibility(View.VISIBLE);
                    llTyping.setVisibility(View.VISIBLE);

                    DotProgressBar dotProgressBar = new DotProgressBar.Builder()
                            .setMargin(7)
                            .setAnimationDuration(3000)
                            .setMaxScale(2.0f)
                            .setMinScale(1.2f)
                            .setNumberOfDots(3)
                            .setdotRadius(4)
                            .build(frame.getContext());
                    frame.addView(dotProgressBar);
                    dotProgressBar.startAnimation();
                }
                mTimestamp.setVisibility(View.GONE);
            }
            else {
                mBubble.setText(mMessage.getText());
                llMain.setVisibility(View.VISIBLE);
            }


            mSender.setText(mMessage.getUser().getDisplayName());

            mTimestamp.setText(mMessage.getTimestamp());

            Glide.with(this.itemView)
                    .load(mMessage.getUser().getProfilePictureUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mAvatar);

            if (this.mMessage.isSent()) {
                mBubble.setAlpha(1.0f);
            } else {
                mBubble.setAlpha(0.5f);
            }

        }

        private void handleType() {
            switch (mType) {
                case TYPE_OWN_HEADER_FULL:
                case TYPE_REC_HEADER_FULL:
                    //mAvatar.setVisibility(View.VISIBLE);
                   // mSender.setVisibility(View.VISIBLE);
                    mTimestamp.setVisibility(View.VISIBLE);
                    break;
                case TYPE_OWN_HEADER_SERIES:
                case TYPE_REC_HEADER_SERIES:
                    //mAvatar.setVisibility(View.VISIBLE);
                   // mSender.setVisibility(View.VISIBLE);
                    mTimestamp.setVisibility(View.VISIBLE);
                    break;
                case TYPE_OWN_MIDDLE:
                case TYPE_REC_MIDDLE:
                   // mAvatar.setVisibility(View.INVISIBLE);
                   // mSender.setVisibility(View.GONE);
                    mTimestamp.setVisibility(View.VISIBLE);
                    break;
                case TYPE_OWN_END:

                case TYPE_REC_END:
                   // mAvatar.setVisibility(View.INVISIBLE);
                   // mSender.setVisibility(View.GONE);
                    mTimestamp.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    class DiffCallback extends DiffUtil.Callback {

        List<Message> newMessages;
        List<Message> oldMessages;

        DiffCallback(List<Message> newMessages, List<Message> oldMessages) {
            this.newMessages = newMessages;
            this.oldMessages = oldMessages;
        }

        @Override
        public int getOldListSize() {
            return oldMessages.size();
        }

        @Override
        public int getNewListSize() {
            return newMessages.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return oldMessages.get(i).getTimetoken() == newMessages.get(i1).getTimetoken();
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            boolean type = oldMessages.get(i).getType() == newMessages.get(i1).getType();
            boolean sent = oldMessages.get(i).isSent() == newMessages.get(i1).isSent();
            return type && sent;
        }

    }

    private void showMessageInfoDialog(Context context, Message message) {

        StringBuilder contentBuilder = new StringBuilder("");
        contentBuilder.append(AndroidUtils.emphasizeText("Sender: "));
        contentBuilder.append(message.getSenderId());
        contentBuilder.append(AndroidUtils.newLine());
        contentBuilder.append(AndroidUtils.emphasizeText("Date time: "));
        contentBuilder.append(Helper.parseDateTime(message.getTimetoken() / 10_000L));
        contentBuilder.append(AndroidUtils.newLine());
        contentBuilder.append(AndroidUtils.emphasizeText("Relative: "));
        contentBuilder.append(Helper.getRelativeTime(message.getTimetoken() / 10_000L));
        contentBuilder.append(AndroidUtils.newLine());
        contentBuilder.append(AndroidUtils.emphasizeText("Own message: "));
        contentBuilder.append(message.isOwnMessage());
        contentBuilder.append(AndroidUtils.newLine());
        contentBuilder.append(AndroidUtils.emphasizeText("Type: "));
        contentBuilder.append(message.getType());
        contentBuilder.append(AndroidUtils.newLine());
        contentBuilder.append(AndroidUtils.emphasizeText("Is sent: "));
        contentBuilder.append(message.isSent());

        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title("Message Info")
                .content(Html.fromHtml(contentBuilder.toString()))
                .positiveText(android.R.string.ok)
                .build();
        materialDialog.show();
    }

}
