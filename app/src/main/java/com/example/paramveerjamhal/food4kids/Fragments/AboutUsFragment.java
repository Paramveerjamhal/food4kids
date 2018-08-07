package com.example.paramveerjamhal.food4kids.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.paramveerjamhal.food4kids.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsFragment extends Fragment {
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
        View rootView = inflater.inflate(R.layout.about, container, false);
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
        myData = "Food4Kids Waterloo Wellington was conceived in September 2016. \n" +
                "\n" +
                "The Food4Kids Waterloo Wellington Founding Board of Directors was installed, and a pilot project with our partner school boards was successfully completed to meet the needs of children living with severe food insecurity within Waterloo Region. \n" +
                "\n" +
                "In 2017-18 over 340 children living with severe food insecurity will receive weekend support leaving a waiting list of 2,160 children.\n" +
                "\n" +
                "\n" +
                "Most people in our communities would say we live in a fairly affluent community.  Statistics show however that “15.6 percent of all children less than 18 years of age are part of a family living with low income.” That represents over 17,000 children and youth throughout Waterloo Region, and almost 13,000 children and youth in Guelph, Wellington, Dufferin. Over 2,500 and 1,500 children within our distinct communities are living with severe food insecurity. That is why we exist. \n"+
                "\n" +
                "Children are the building blocks of the country, we considered ourselves fortunate of being a part of such organization who aims that no child should be hungry in our society.\n" +
                "\n" +
                "we believe that if we are doing good for others, it provides a natural sense of accomplishment and more likely we intend to think a positive view of our life and future goals.  \n" +
                "\n" +
                "We have learned that devoting our few time from busy lives in volunteering, even though we are doing smallest tasks, it gives much more satisfaction to us. \n" +
                "\n" +
                "For a while, watching smiling faces of the kids gives a lot more happiness to us, moreover it will counteract all the stress, anxiety we had.";

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
