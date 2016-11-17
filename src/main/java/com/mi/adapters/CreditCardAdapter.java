package com.mi.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mi.test.mypiece.R;

public class CreditCardAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<String> cardsArray;

        public CreditCardAdapter(Context context, ArrayList<String> cardsArray) {
            this.context = context;
            this.cardsArray = cardsArray;

        }

        @Override
        public int getCount() {
            return cardsArray.size();

        }

        @Override
        public Object getItem(int location) {
            return cardsArray.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (inflater == null)
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.row_myaccount_cardlist, null);

            TextView cardName = (TextView) convertView.findViewById(R.id.row_text_name);

            cardName.setText(cardsArray.get(position));

            return convertView;
        }

    }
