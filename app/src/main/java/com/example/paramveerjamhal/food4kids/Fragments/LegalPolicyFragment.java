package com.example.paramveerjamhal.food4kids.Fragments;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.paramveerjamhal.food4kids.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LegalPolicyFragment extends Fragment {
    @BindView(R.id.webView)
    WebView webView;
    String htmlText,myData;
    TextToSpeech textToSpeech;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("About Us");

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_legalpolicy, container, false);
        ButterKnife.bind(this,rootView);
        textToSpeech=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        htmlText = "<html><body style=\"text-align:justify\"> %s </body></Html>";
        myData =
                "You may be asked to provide your personal information anytime you are in contact with this organisation.\n"+
                        "\n"+
                        "its affiliates may share this personal information with each other and use it consistent with this Privacy Policy. They may also combine it with other information to provide and improve our products, services, content, and advertising.\n"+
                        "\n"+
                        "You are not required to provide the personal information that we have requested, but, if you chose not to do so, in many cases we will not be able to provide you with our products or services or respond to any queries you may have.\n" +
                        "\n" +
                        "We will provide new updations by sending you a notification message, which help you guys to get important information on time.\n"+
                        "\n" +
                        "If you have any questions or concerns aboutthis organisation's Privacy Policy or data processing, you would like to contact on the given number of the organization or if you would like to make a complaint about a possible breach of local privacy laws, please contact us. \n";

        webView.loadData(String.format(htmlText, myData), "text/html", "utf-8");
        return rootView;

    }

    @OnClick(R.id.rel_speaker)
    public void text_to_speechListener(){
        String toSpeak = myData;
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

}
