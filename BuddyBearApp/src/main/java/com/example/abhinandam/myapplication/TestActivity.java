package com.example.abhinandam.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ArrayList<ListItem> listItems = new ArrayList<ListItem>();
        ListItem joy = new ListItem(R.drawable.joy, "JOY","symbolized by raising of the mouth corners (an obvious smile) and tightening of the eyelids");
        ListItem surprise = new ListItem(R.drawable.surprise, "Surprise","symbolized by eyebrows arching, eyes opening wide and exposing more white, with the jaw dropping slightly");
        ListItem sadness = new ListItem(R.drawable.sadness, "SADNESS","symbolized by lowering of the mouth corners, the eyebrows descending to the inner corners and the eyelids drooping");
        ListItem anger = new ListItem(R.drawable.anger, "ANGER","symbolized by eyebrows lowering, lips pressing firmly and eyes bulging");
        ListItem disgust = new ListItem(R.drawable.disgust, "DISGUST","symbolized by the upper lip raising, nose bridge wrinkling and cheeks raising");
        ListItem fear = new ListItem(R.drawable.fear, "FEAR","symbolized by the upper eyelids raising, eyes opening and the lips stretching horizontally");

        listItems.add(joy);
        listItems.add(surprise);
        listItems.add(sadness);
        listItems.add(anger);
        listItems.add(disgust);
        listItems.add(fear);

        ArrayList<Card> cards = new ArrayList<Card>();

        for (int i = 0; i < listItems.size(); i++) {
            // Create a Card
            Card card = new Card(this);
            // Create a CardHeader
            CardHeader header = new CardHeader(this);
            // Add Header to card
            header.setTitle(listItems.get(i).header);
            card.setTitle(listItems.get(i).description);
            card.addCardHeader(header);

            CardThumbnail thumb = new CardThumbnail(this);
            thumb.setDrawableResource(listItems.get(i).image);
            card.addCardThumbnail(thumb);

            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);

        CardListView listView = (CardListView) this.findViewById(R.id.myList);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }
}

class ListItem {
    int image;
    String header;
    String description;

    ListItem(int image, String header, String description) {
        this.image = image;
        this.header = header;
        this.description = description;
    }
}
