package com.example.loteria;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    Button btn_login, btn_clear;
    EditText edtxt_user, edtxt_password;
    String url = "http://www.ramiro174.com/oauth/token";
    private SharedPreferences credentials;
    String token;
    RequestQueue queue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        btn_login = view.findViewById(R.id.btn_login);
        btn_clear = view.findViewById(R.id.btn_clearForm);
        edtxt_user = view.findViewById(R.id.edtxt_user);
        edtxt_password = view.findViewById(R.id.edtxt_password);

        queue = Volley.newRequestQueue(getContext());

        btnClick();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        return view;
    }

    private void login(){

        boolean filledInputs = false;



        if(!edtxt_user.getText().toString().equals("") && !edtxt_password.getText().toString().equals(""))
        {
            filledInputs = true;
        }else{
            Toast.makeText(getContext(),"Llena los campos correctamente", Toast.LENGTH_SHORT).show();
        }

        if(filledInputs){

            final String user = edtxt_user.getText().toString();
            final String password = edtxt_password.getText().toString();

            final  UserCredentials credential = new UserCredentials();

            JSONObject data = new JSONObject();
            try {
                data.put("client_id", 6);
                data.put("client_secret", "otuoWd9Zyukpu8WRItXxicoLlpfXbOYAX0zv4tEu");
                data.put("grant_type", "password");
                data.put("username", user);
                data.put("password", password);
            }catch (JSONException e){
                e.printStackTrace();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                    credentials = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = credentials.edit();

                                    editor.putString("token", response.getString("access_token"));
                                    editor.apply();

                                    pass();

                                }
                            catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(),"Verifica los datos e intenta de nuevo",Toast.LENGTH_SHORT).show();
                        }
                    });

            queue.add(jsonObjectRequest);

        } // if (filledInputs) END

    }

    private void pass(){

        credentials = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        if(credentials.getString("token",null) != null) {
            //Toast.makeText(getContext(), credentials.getString("token",null), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void  btnClick(){
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtxt_user.setText(null);
                edtxt_password.setText(null);
            }
        });
    }

}