package css.csslibrary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChangeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textUnite;
    private EditText editCurrent;
    private EditText editChange;
    private EditText editChangeCheck;
    private CustomTask task;
    private String currentPassword;
    private String changePassword;
    private String confirmPassword;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        textUnite=(TextView)findViewById(R.id.text_unite);
        editCurrent=(EditText)findViewById(R.id.edit_current);
        editChange=(EditText)findViewById(R.id.edit_change);
        editChangeCheck=(EditText)findViewById(R.id.edit_change_check);
        SharedPreferences sp=getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        id=sp.getString("id",null);
        editChangeCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textUnite.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textUnite.setVisibility(View.VISIBLE);
                textUnite.setText("입력중...");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editChange.getText().toString().equals(editChangeCheck.getText().toString())){
                    textUnite.setText("일치");
                    textUnite.setTextColor(Color.BLUE);
                }
                else{
                    textUnite.setText("불일치");
                    textUnite.setTextColor(Color.RED);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_submit:
                String current=editCurrent.getText().toString();
                String change=editChange.getText().toString();
                String change_check=editChangeCheck.getText().toString();
                /*
                - 셋 중 하나라도 값이 입력되지 않은 경우 : 해결
                - 현재 비밀번호가 일치하지 않는 경우 - DB검색 필요
                - 새 비밀번호와 새 비밀번호 확인이 같지 않는 경우 : 해결
                - 세 조건이 만족되면 DB 연동하여 필드값 변경
                 */
                if(current.equals("") || change.equals("") || change_check.equals("")){
                    Toast.makeText(this,"값을 입력해 주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(!change.equals(change_check)){
                    Toast.makeText(this,"새 비밀번호와 비밀번호 확인이 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    /*
                    이 쪽에 DB 연동 코드 작성
                    현재 비밀번호가 일치하지 않아도 아무 일 없게 해야 함.
                     */
                    try{
                        currentPassword=editCurrent.getText().toString();
                        changePassword=editChange.getText().toString();
                        confirmPassword=editChangeCheck.getText().toString();
                        task=new CustomTask();
                        String result=task.execute(current,change,change_check,id).get();
                        if(result.equals("true")){
                            Toast.makeText(ChangeActivity.this,"비밀번호를 변경하였습니다.",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else if(result.equals("false")||result.equals("noId") ){
                            Toast.makeText(ChangeActivity.this,"현재 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ChangeActivity.this,"??????",Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){}
                }
                break;
        }
    }
    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {

                String str;
                //URL url = new URL("http://192.168.0.6:8080/CSSLibrary/change.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                URL url = new URL("http://192.168.226.1:8080/CSSLibrary/change.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "currentPassword="+strings[0]+"&changePassword="+strings[1]+"&confirmPassword="+strings[2]+"&id="+strings[3];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
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
}
