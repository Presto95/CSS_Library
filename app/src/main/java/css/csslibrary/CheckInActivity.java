package css.csslibrary;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

import css.csslibrary.Adapter.Adapter;
import css.csslibrary.model.Book;

public class CheckInActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private String browse_type;
    private String browse_keyword;
    private ListView listView;
    private Adapter adapter;
    private CustomTask1 task1;
    private CustomTask2 task2;
    private CustomTask3 task3;
    private EditText editKeyword;
    private List<Book> items;
    private String type_checkout;
    private String title_checkout;
    private String author_checkout;
    private String amount_checkout;
    private TextView textView;
    private InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        Spinner spinner1=(Spinner)findViewById(R.id.spin_type);
        Spinner spinner2=(Spinner)findViewById(R.id.spin_keyword);
        editKeyword=(EditText)findViewById(R.id.edit_keyword);

        listView=(ListView)findViewById(R.id.list_view);
        items=setDefault();
        adapter=new Adapter(this,R.layout.list_checkin,items);
        textView=(TextView)findViewById(R.id.text_checkin);
        textView.setText("검색하세요.");

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
                Button btn=(Button)findViewById(R.id.btn_browse);
                imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btn.getWindowToken(),0);
                //여기에 서버 통신 코드
                textView.setVisibility(View.GONE);
                try{
                    task2=new CustomTask2();
                    if(browse_type.equals("분류")){

                    }
                    else{
                        editKeyword=(EditText)findViewById(R.id.edit_keyword);
                        browse_keyword=editKeyword.getText().toString();
                    }
                    //Toast.makeText(this,browse_type+browse_keyword,Toast.LENGTH_SHORT).show();

                    String result=task2.execute(browse_type,browse_keyword).get();
                    if(result.equals("")){
                        textView.setText("검색 결과가 없습니다.");
                        textView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else{
                        textView.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        items=setList(result);
                        adapter=new Adapter(this,R.layout.list_checkin,items);
                        listView.setAdapter(adapter);
                    }
                }catch(Exception e){}
                break;
        }
    }

    private List<Book> setList(String result){
        List<Book> list=new ArrayList<>();
        String data=result;
        String[] array1;
        String[] array2;
        array1=data.split(":");
        for(int i=0;i<array1.length;++i){
            array2=array1[i].split(";");
            list.add(new Book(0,array2[0],array2[1],array2[2],array2[3]+"권"));
        }
        return list;
    }
    private List<Book> setDefault(){
        List<Book> list=new ArrayList<>();
        list.add(new Book(0,"","검색하세요.","",""));
        return list;

    }
    /*
     여기만 해결하면 되는데...
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        type_checkout=((TextView)view.findViewById(R.id.text_type)).getText().toString();
        title_checkout=((TextView)view.findViewById(R.id.text_title)).getText().toString();
        author_checkout=((TextView)view.findViewById(R.id.text_author)).getText().toString();
        amount_checkout=((TextView)view.findViewById(R.id.text_isCheckin)).getText().toString();
        if(amount_checkout.charAt(0)=='0'){
            Toast.makeText(this,"보유하고 있지 않아 대출할 수 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("안내");
            builder.setMessage(title_checkout+"\n\n"+"이 도서를 대출합니다.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //서버 통신
                    task1=new CustomTask1();
                    task3=new CustomTask3();
                    SharedPreferences sp=getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                    String id=sp.getString("id",null);
                    String name=sp.getString("name",null);
                    try{
                        task1.execute(id,type_checkout,title_checkout,author_checkout,name);
                        task3.execute(title_checkout,amount_checkout);
                        //목록 업데이트 코드 들어가야함
                        task2=new CustomTask2();
                        if(browse_type.equals("분류")){

                        }
                        else{
                            editKeyword=(EditText)findViewById(R.id.edit_keyword);
                            browse_keyword=editKeyword.getText().toString();
                        }
                        //Toast.makeText(this,browse_type+browse_keyword,Toast.LENGTH_SHORT).show();

                        String result=task2.execute(browse_type,browse_keyword).get();
                        if(result.equals("")){
                            textView.setText("검색 결과가 없습니다.");
                            textView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }
                        else{
                            textView.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            items=setList(result);
                            adapter=new Adapter(CheckInActivity.this,R.layout.list_checkin,items);
                            listView.setAdapter(adapter);
                        }
                        Toast.makeText(CheckInActivity.this,"대출하였습니다.",Toast.LENGTH_SHORT).show();
                    }catch(Exception e){}
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create();
            builder.show();
        }
    }
    /*@Override
    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int a=3;
        if(a==3)
            return false;
        else
            return true;
    }*/
    class CustomTask1 extends AsyncTask<String, Void, String> { //대출
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                //URL url = new URL("http://192.168.0.6:8080/CSSLibrary/insert.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                URL url = new URL("http://192.168.226.1:8080/CSSLibrary/insert.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&type="+strings[1]+"&title="+strings[2]+"&author="+strings[3]+"&name="+strings[4];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
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
    class CustomTask2 extends AsyncTask<String, Void, String> { //검색
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                //URL url = new URL("http://192.168.0.6:8080/CSSLibrary/checkin.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                URL url = new URL("http://192.168.226.1:8080/CSSLibrary/checkin.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "type="+strings[0]+"&keyword="+strings[1];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
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
    class CustomTask3 extends AsyncTask<String, Void, String> { //수량 업데이트
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                //URL url = new URL("http://192.168.0.6:8080/CSSLibrary/update.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                URL url = new URL("http://192.168.226.1:8080/CSSLibrary/update.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "title="+strings[0]+"&amount="+strings[1];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
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
