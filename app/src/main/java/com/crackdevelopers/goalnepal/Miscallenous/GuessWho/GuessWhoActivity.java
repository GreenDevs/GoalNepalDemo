package com.crackdevelopers.goalnepal.Miscallenous.GuessWho;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.CacheRequest;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class GuessWhoActivity extends AppCompatActivity implements View.OnClickListener
{

    private static final String URL="http://www.goalnepal.com/json_guess_who_2015.php";
    private static final String QUESTION_KEY="questions";
    private static final String IMAGE_URL_KEY="img";
    private static final String QUESTION_ID_KEY="question_id";
    private static final String ANS_ID="ans_id";
    private static final String ANS_OPTIONS="ans_options";
    private static final String GUESS="guess";
    private static String IMAGE_URL="";
    private static long QUESTION_ID=-1;
    private static String QUESTION="";


    private Context mContext;
    private  EditText UserName,UserPhone;
    private TextView titleQuestion;
    private ImageView guessImage;
    private RadioGroup radioGroup;
    private RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
    private Button reset,send;
    private ImageLoader mImageLoader;
    RequestQueue mQueue;
    private String userName,userPhone;
    private static ArrayList<IdentitySingleRow> information=null;

 private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_who);
        mQueue= VolleySingleton.getInstance().getQueue();
        mImageLoader= VolleySingleton.getInstance().getmImageLoader();
        initToolBar();
        initXmlAttributes();
        sendIdentifyRequest();
    }


    private void initToolBar()
    {
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mContext=this;
    }


    private void initXmlAttributes()
    {

        titleQuestion= (TextView) findViewById(R.id.title);
        UserName= (EditText) findViewById(R.id.guesswho_name);
        UserPhone= (EditText) findViewById(R.id.guesswho_phone);

        guessImage= (ImageView) findViewById(R.id.player_image);


        radioButton1= (RadioButton) findViewById(R.id.rbutton_p1);
        radioButton2= (RadioButton) findViewById(R.id.rbutton_p2);
        radioButton3= (RadioButton) findViewById(R.id.rbutton_p3);
        radioButton4= (RadioButton) findViewById(R.id.rbutton_p4);

        radioGroup=(RadioGroup)findViewById(R.id.radio_group);


        reset= (Button) findViewById(R.id.guesswhobtn_reset);
        send= (Button) findViewById(R.id.guesswhobtn_send);

    }





    private void sendIdentifyRequest()
    {

        CacheRequest cacheRequest=new CacheRequest(Request.Method.GET, URL, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response)
            {
                String jsonStringResponse= null;
                try
                {
                    jsonStringResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JSONObject responseJson=new JSONObject(jsonStringResponse);
                    information=parseIdentity(responseJson);

                    setGuessWhoFields(information);


                }
                catch (UnsupportedEncodingException |JSONException e)
                {
                    e.printStackTrace();
                }


            }
        }
                ,
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });

        cacheRequest.setTag(mContext);
        mQueue.add(cacheRequest);

    }

    private void setGuessWhoFields(final ArrayList<IdentitySingleRow> tmp)
    {
        String imageUrl = IMAGE_URL;

        if (imageUrl != null)
        {
            mImageLoader.get(imageUrl, new ImageLoader.ImageListener()
            {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                {
                    guessImage.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(mContext, "image resource not available", Toast.LENGTH_SHORT).show();
                }
            });

            // set Guesswho title to textview.
            titleQuestion.setText(QUESTION);

            // set GuessWho available options.
            try {
                radioButton1.setText(tmp.get(0).ansoption);
                radioButton2.setText(tmp.get(1).ansoption);
                radioButton3.setText(tmp.get(2).ansoption);
                radioButton4.setText(tmp.get(3).ansoption);
            }
            catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }

            send.setOnClickListener(this);
            reset.setOnClickListener(this);

        }


    }


    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.guesswhobtn_send:
                if(information!=null)               sendEmailToGoalNepal(information);
                break;

            case R.id.guesswhobtn_reset:
                if(radioButton==null)               radioButton= (RadioButton) findViewById(R.id.rbutton_p1);
                clearInputFields();
                break;

            default:
                Toast.makeText(mContext, "please select valid option only", Toast.LENGTH_SHORT).show();
        }
    }



    private void clearInputFields()
    {
        radioGroup.clearCheck();
        radioButton.setChecked(true);
        UserName.setText("");
        UserPhone.setText("");
    }



    private void sendEmailToGoalNepal(ArrayList<IdentitySingleRow> tmp)
    {

        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radio button by the previously returned id
        radioButton = (RadioButton) findViewById(selectedId);
        String ans_option="";

        if(radioButton!=null)
        {
            ans_option=radioButton.getText().toString();
        }

        long ans_id=-1;


        for(IdentitySingleRow list:tmp)
        {

            if(list.ansoption==ans_option)
            {
                ans_id=list.ansId;
            }
        }

        // collect user specific details from edit text.
        userName=UserName.getText().toString();
        userPhone=UserPhone.getText().toString();


        StringBuffer buffer =new StringBuffer();

        if(!userName.equals("") && userName!=null && !userPhone.equals("") && userPhone!=null)
        {

            if(userPhone.length()>7 && userPhone.length()<=15)
            {

                try
                {
                    buffer.append("User Name=" + userName + "\n" + "User phone=" + userPhone + "\n" + "Question id=" + QUESTION_ID + "\n" + "answer id=" + ans_id + "\n" + "answer option=" + ans_option);
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "goalnepalwhoishe@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, " Quiz Response");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, buffer.toString());
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }

                catch (Exception e)
                {
                    Toast.makeText(this, "no email app found", Toast.LENGTH_SHORT).show();
                }
            }

            else
            {
                Toast.makeText(GuessWhoActivity.this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(GuessWhoActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
        }

    }

    private ArrayList<IdentitySingleRow> parseIdentity(JSONObject rootObj)
    {
        ArrayList<IdentitySingleRow>tmpList=new ArrayList<>();

        if(rootObj!=null)
        {
            if(rootObj.length()!=0)
            {
                try
                {
                    JSONArray answers=rootObj.getJSONArray(GUESS);

                    JSONObject zeroIndex=answers.getJSONObject(0);

                    if(zeroIndex.has(QUESTION_KEY)) QUESTION=zeroIndex.getString(QUESTION_KEY);
                    if(zeroIndex.has(QUESTION_ID_KEY)) QUESTION_ID=zeroIndex.getLong(QUESTION_ID_KEY);
                    if(zeroIndex.has(IMAGE_URL_KEY))  IMAGE_URL=zeroIndex.getString(IMAGE_URL_KEY);

                    for(int i=1;i<answers.length();i++)
                    {
                        String option=""; long id=-1;
                        JSONObject answer=answers.getJSONObject(i);

                        if(answer.has(ANS_OPTIONS)) option=answer.getString(ANS_OPTIONS);
                        if(answer.has(ANS_ID)) id=answer.getLong(ANS_ID);

                        tmpList.add(new IdentitySingleRow(id, option));
                    }

                } catch (JSONException | IndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }

            }
        }

        return tmpList;
    }



    @Override
    protected void onStop()
    {
        super.onStop();
        mQueue.cancelAll(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
