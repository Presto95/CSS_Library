package css.csslibrary;

import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import css.csslibrary.Adapter.Adapter;
import css.csslibrary.model.Book;

public class CheckInActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private String browse_type;
    private String browse_keyword;
    private ListView listView;
    private Adapter adapter;
    private CustomTask task;
    private EditText editKeyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        Spinner spinner1=(Spinner)findViewById(R.id.spin_type);
        Spinner spinner2=(Spinner)findViewById(R.id.spin_keyword);
        editKeyword=(EditText)findViewById(R.id.edit_keyword);

        listView=(ListView)findViewById(R.id.list_view);
        List<Book> items=findList();
        adapter=new Adapter(this,R.layout.list_checkin,items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        findViewById(R.id.btn_browse).setOnClickListener(this);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                browse_type=parent.getItemAtPosition(position).toString();
                if(browse_type.equals("분류")) {
                    browse_keyword="";
                    findViewById(R.id.btn_browse).setEnabled(true);

                    findViewById(R.id.edit_keyword).setVisibility(View.INVISIBLE);
                    findViewById(R.id.spin_keyword).setVisibility(View.VISIBLE);
                }
                else if(browse_type.equals("선택")){
                    findViewById(R.id.btn_browse).setEnabled(false);
                    findViewById(R.id.edit_keyword).setVisibility(View.INVISIBLE);
                    findViewById(R.id.spin_keyword).setVisibility(View.INVISIBLE);
                }
                else{
                    findViewById(R.id.edit_keyword).setVisibility(View.VISIBLE);
                    findViewById(R.id.spin_keyword).setVisibility(View.INVISIBLE);
                    browse_keyword="";
                    editKeyword.setText(null);
                    findViewById(R.id.btn_browse).setEnabled(true);
                    ((Spinner) findViewById(R.id.spin_keyword)).setSelection(0);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                browse_keyword=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_browse:
                //여기에 서버 통신 코드
                try{
                    task=new CustomTask();
                    if(browse_type.equals("분류")){

                    }
                    else{
                        editKeyword=(EditText)findViewById(R.id.edit_keyword);
                        browse_keyword=editKeyword.getText().toString();
                    }
                    Toast.makeText(this,browse_type+browse_keyword,Toast.LENGTH_SHORT).show();

                    String result=task.execute(browse_keyword,browse_type).get();
                    if(result.equals("true")){
                        Toast.makeText(CheckInActivity.this,"로그인!",Toast.LENGTH_SHORT).show();

                    }
                    else if(result.equals("false") || result.equals("noId")){
                        Toast.makeText(CheckInActivity.this,"로그인 실패",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(CheckInActivity.this,"??????",Toast.LENGTH_SHORT).show();

                    }
                }catch(Exception e){}
                break;
        }
    }

    private List<Book> findList(){
        List<Book> list=new ArrayList<>();
        for(int i=0;i<40;++i)
            list.add(new Book(0,"type","title","author",0));
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    /*@Override
    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int a=3;
        if(a==3)
            return false;
        else
            return true;
    }*/
    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.0.6:8080/CSSLibrary/checkin.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "keyword="+strings[0]+"&spinner="+strings[1];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
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
