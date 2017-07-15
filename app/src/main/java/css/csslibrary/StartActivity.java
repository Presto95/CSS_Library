package css.csslibrary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.microsoft.azure.mobile.MobileCenter;
import com.microsoft.azure.mobile.analytics.Analytics;
import com.microsoft.azure.mobile.crashes.Crashes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_id;
    private EditText et_pw;
    private CheckBox checkBox;
    private String input_id;
    private String input_pw;
    private CustomTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        MobileCenter.start(getApplication(), "bf80e11b-7593-4e19-8f36-44eb3a4cea25",
                Analytics.class, Crashes.class);
        findViewById(R.id.btn_login).setOnClickListener(this);
        //findViewById(R.id.btn_develop).setOnClickListener(this);

        et_id=(EditText)findViewById(R.id.edit_id);
        et_pw=(EditText)findViewById(R.id.edit_pw);
        checkBox=(CheckBox)findViewById(R.id.checkBox);


        SharedPreferences sp=getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String id=sp.getString("id",null);
        Boolean isChecked=sp.getBoolean("isChecked",false);

        if(id!=null)
            et_id.setText(id);
        if(isChecked==true)
            checkBox.setChecked(true);
        else {
            checkBox.setChecked(false);
            et_id.setText(null);
        }
        et_pw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        logIn();
                        break;
                }
                return true;
            }
        });




    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                //프론티어
                URL url = new URL("http://192.168.0.6:8080/CSSLibrary/login.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                //URL url = new URL("http://192.168.226.1:8080/CSSLibrary/login.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "loginId="+strings[0]+"&loginPassword="+strings[1];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
                //회원가입처럼 보낼 데이터가 여러 개일 경우 &로 구분하여 작성합니다.
                osw.write(sendMsg);//OutputStreamWriter에 담아 전송합니다.
                osw.flush();
                //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    //jsp에서 보낸 값을 받겠죠?
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                    // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //jsp로부터 받은 리턴 값입니다.
            return receiveMsg;
        }
    }



    /*
         로그인 버튼 눌렀을 때 관련 DB 조회 후 사용자 정보가 맞는 지 확인해야 함
         맞으면 다음 페이지로 넘어가고 틀리면 넘어갈 수 없음
         */
    public void logIn(){
        try{
            task=new CustomTask();
            input_id=et_id.getText().toString();
            input_pw=et_pw.getText().toString();
            String result=task.execute(input_id,input_pw).get();
            /*if(result.equals("true")){
                Toast.makeText(StartActivity.this,"로그인!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainListActivity.class));
                finish();
            }
            else if(result.equals("false") || result.equals("noId")){
                Toast.makeText(StartActivity.this,"로그인 실패",Toast.LENGTH_SHORT).show();
                input_id="";
                input_pw="";
            }
            else{
                Toast.makeText(StartActivity.this,"??????",Toast.LENGTH_SHORT).show();
                input_id="";
                input_pw="";
            }*/
            if(!result.equals(null)){
                SharedPreferences sp=getSharedPreferences("LOGIN",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("name",result);
                editor.commit();

                Toast.makeText(StartActivity.this,"로그인!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }
            else{
                Toast.makeText(StartActivity.this,"로그인 실패",Toast.LENGTH_SHORT).show();
                input_id="";
                input_pw="";
            }

        }catch(Exception e){}


        String id=et_id.getText().toString();
        SharedPreferences sp=getSharedPreferences("LOGIN",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        if(checkBox.isChecked()){
            editor.putBoolean("isChecked",true);
            editor.putString("id",id);
            editor.commit();
        }
        else{
            editor.putBoolean("isChecked",false);
            editor.putString("id",id);
            editor.commit();
        }
        //startActivity(new Intent(this,MainListActivity.class));
        //finish();

    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_DOWN){
            if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                logIn();
            }
        }
        return super.onKeyDown(keyCode,event);
    }*/

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_login:
                logIn();
                break;
            /*case R.id.btn_develop:
                startActivity(new Intent(this,ScheduleActivity.class));
                break;*/
        }
    }




}
