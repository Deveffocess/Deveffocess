package com.livo.nuo.view.message.pubnub;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;

import org.jetbrains.annotations.NotNull;

public class PubNubListener extends SubscribeCallback {

    @Override
    public void status(PubNub pubnub, PNStatus status) {

    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {

    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }

    @Override
    public void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult pnSignalResult) {

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
}
