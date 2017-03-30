package com.example.abhinandam.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.app.AlertDialog;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.*;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;
import java.util.Random;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class TestActivity extends AppCompatActivity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ArrayList<ListItem> listItems = new ArrayList<ListItem>();
        ListItem joy = new ListItem(R.drawable.joy, "JOY", "symbolized by raising of the mouth corners (an obvious smile) and tightening of the eyelids");
        ListItem surprise = new ListItem(R.drawable.surprise, "SURPRISE", "symbolized by eyebrows arching, eyes opening wide and exposing more white, with the jaw dropping slightly");
        ListItem sadness = new ListItem(R.drawable.sadness, "SADNESS", "symbolized by lowering of the mouth corners, the eyebrows descending to the inner corners and the eyelids drooping");
        ListItem anger = new ListItem(R.drawable.anger, "ANGER", "symbolized by eyebrows lowering, lips pressing firmly and eyes bulging");
        ListItem disgust = new ListItem(R.drawable.disgust, "DISGUST", "symbolized by the upper lip raising, nose bridge wrinkling and cheeks raising");
        ListItem fear = new ListItem(R.drawable.fear, "FEAR", "symbolized by the upper eyelids raising, eyes opening and the lips stretching horizontally");

        listItems.add(joy);
        listItems.add(surprise);
        listItems.add(sadness);
        listItems.add(anger);
       // listItems.add(disgust);
       // listItems.add(fear);

        ArrayList<Card> cards = new ArrayList<Card>();

        for (int i = 0; i < listItems.size(); i++) {
            // Create a Card
            Card card = new Card(this);
            // Create a CardHeader
            CardHeader header = new CardHeader(this);
            // Add Header to card
            header.setTitle(listItems.get(i).header);
            //card.setTitle(listItems.get(i).description);
            card.addCardHeader(header);

            CardThumbnail thumb = new CardThumbnail(this);
            thumb.setDrawableResource(listItems.get(i).image);
            card.addCardThumbnail(thumb);

            cards.add(card);
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    Random random = new Random();
                    if (random.nextBoolean()) {
                        Toast.makeText(getApplicationContext(), "Not Quite, Try Again", Toast.LENGTH_SHORT).show();
                    } else {
                        ImageView image = new ImageView(TestActivity.this);
                        image.setImageResource(R.drawable.check);
                        builder1.setView(image);
                        builder1.setCancelable(true);
                        

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent cameraPage = new Intent(TestActivity.this, CameraActivity.class);
                                TestActivity.this.startActivity(cameraPage);
                            }
                        }, 3000);

                    }
                }
            });
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);

        final CardListView listView = (CardListView) this.findViewById(R.id.myList);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Object o = listView.getItemAtPosition(position);
                String str = (String) o;//As you are using Default String Adapter
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

                /*
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("You Got it Right!");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });*/

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
