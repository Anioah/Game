package com.example.loteria.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.loteria.Login;
import com.example.loteria.R;
import com.example.loteria.Splash;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class HomeFragment extends Fragment {

    ArrayList<Integer> cardNumbers = new ArrayList<>();
    ArrayList<Integer> board_numbers = new ArrayList<>();
    int cardCheck;
    int cardsPassed;
    CardInterface[] board = new CardInterface[16];
    CardInterface cardButton = new CardInterface();
    ColorMatrixColorFilter filter;
    TextView CardCount, instructions;
    final String url = "http://ramiro174.com/api/cartaloteria";
    RequestQueue queue;
    SharedPreferences credentials;
    String userToken;
    boolean whileLoop = false;
    Handler handler;

    public HomeFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler(getContext().getMainLooper());


        cardsPassed = 0;
        cardCheck = 0;

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        filter = new ColorMatrixColorFilter(matrix);

        for(int i = 0; i<54; i++){
            cardNumbers.add(i);
        }

        for(int i = 0; i<board.length;i++){
            board[i] = new CardInterface();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            board_numbers.add(ThreadLocalRandom.current().nextInt(0,54));
        }

        boolean repeated;

        for (int i = 0; i< board.length - 1;){
            repeated = false;
            int number = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                number = ThreadLocalRandom.current().nextInt(0,54);
            }

            for(int j = 0; j<board_numbers.size();j++){
                if(number == board_numbers.get(j)){
                    repeated = true;
                    break;
                }
            }
            if(!repeated){
                board_numbers.add(number);
                i++;
            }

        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        instructions = root.findViewById(R.id.txt_vwInstructions);

        setGame(root);
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        whileLoop = false;
    }

    public void setGame(final View view) {

        credentials = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        userToken = credentials.getString("token",null);

        CardCount = view.findViewById(R.id.txt_vwCardCount);
        CardCount.setText(String.valueOf(cardsPassed));

        cardButton.setImageView((ImageView) view.findViewById(R.id.img_vwStart));

        cardButton.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                instructions.setVisibility(View.GONE);

                Toast.makeText(getActivity(), "Game started", Toast.LENGTH_SHORT).show();

                cardButton.getImageView();

                whileLoop = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (whileLoop) {
                            try {
                                getCard();
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

        board[0].setImageView((ImageView) view.findViewById(R.id.img_vw0));
        board[1].setImageView((ImageView) view.findViewById(R.id.img_vw1));
        board[2].setImageView((ImageView) view.findViewById(R.id.img_vw2));
        board[3].setImageView((ImageView) view.findViewById(R.id.img_vw3));
        board[4].setImageView((ImageView) view.findViewById(R.id.img_vw4));
        board[5].setImageView((ImageView) view.findViewById(R.id.img_vw5));
        board[6].setImageView((ImageView) view.findViewById(R.id.img_vw6));
        board[7].setImageView((ImageView) view.findViewById(R.id.img_vw7));
        board[8].setImageView((ImageView) view.findViewById(R.id.img_vw8));
        board[9].setImageView((ImageView) view.findViewById(R.id.img_vw9));
        board[10].setImageView((ImageView) view.findViewById(R.id.img_vw10));
        board[11].setImageView((ImageView) view.findViewById(R.id.img_vw11));
        board[12].setImageView((ImageView) view.findViewById(R.id.img_vw12));
        board[13].setImageView((ImageView) view.findViewById(R.id.img_vw13));
        board[14].setImageView((ImageView) view.findViewById(R.id.img_vw14));
        board[15].setImageView((ImageView) view.findViewById(R.id.img_vw15));

        for (int i = 0; i < board.length; i++) {

            board[i].getImageView().setImageResource(CardSet.getCard(board_numbers.get(i)));
            board[i].setCard_number(board_numbers.get(i));
        }
    }

    private void getCard(){

        queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        int cardNumber = 0;
                        try {
                            cardNumber = response.getInt("numero") - 1;
                            Toast.makeText(getContext(),"A card has been released!",Toast.LENGTH_SHORT).show();
                            System.out.println(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        cardButton.getImageView().setImageResource(CardSet.getCard(cardNumber));

                        for (CardInterface card : board) {
                            if (cardNumber == card.getCard_number() && !card.isChecked()) {
                                card.getImageView().setColorFilter(filter);
                                card.checkCard();
                                cardCheck++;
                                break;
                            }
                        }

                        if (cardsPassed >= 54 || cardCheck >= 16) {

                            if(cardsPassed >= 54){
                                cardButton.getImageView().setImageResource(R.drawable.finished);
                                Toast.makeText(getContext(),"Game Over",Toast.LENGTH_SHORT).show();
                            }

                            if (cardCheck >= 16) {
                                cardButton.getImageView().setImageResource(R.drawable.loteria_win);
                                Toast.makeText(getContext(),"¡You Win!",Toast.LENGTH_SHORT).show();
                            }

                            whileLoop = false;
                            Toast.makeText(getContext(), "selecciona Loteria en el menú para reiniciar", Toast.LENGTH_SHORT).show();
                        }

                        cardsPassed++;

                        CardCount.setText(String.valueOf(cardsPassed));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), String.valueOf(error.networkResponse.statusCode), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                String token = "Bearer " + userToken;
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                params.put("Authorization", token);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        //System.out.println(userToken);
        queue.add(jsonObjectRequest);
    }


}