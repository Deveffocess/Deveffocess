package com.livo.nuo.view.message.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.livo.nuo.R;


// tag::SEND-1[]
public class MessageComposer extends RelativeLayout {

    private EditText mInput;
    private ImageView mSend;
    private ImageView mAttachment;
    private ConstraintLayout contraintLayout;

    Integer keyup=0,keydown=1;

    private Listener mListener;

    public MessageComposer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View root = inflate(getContext(), R.layout.view_message_composer, this);
        mInput = root.findViewById(R.id.composer_edittext);
        mSend = root.findViewById(R.id.composer_send);
        mAttachment = root.findViewById(R.id.composer_attachment);
        contraintLayout=root.findViewById(R.id.contraintLayout);

        mSend.setOnClickListener(v -> {
            keydown++;
            keyup=0;
            mListener.onSentClick(mInput.getText().toString().trim());
            mInput.setText("");
        });


        contraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                contraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = contraintLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    Log.e("me","Keyboard shown");
                    if (keyup==0) {
                        mListener.sendSignal("typing_on");
                        keydown=0;
                        keyup++;
                    }
                } else {
                    Log.e("me","Keyboard off");
                    if (keydown==0) {
                        mListener.sendSignal("typing_off");
                        //mListener.loadAdapter();
                        keyup=0;
                        keydown++;
                    }
                }
            }
        });
    }



    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {

        void onSentClick(String message);
        void sendSignal(String value);
        void loadAdapter();
    }

}
// end::SEND-1[]
