package css.csslibrary;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.concurrent.ExecutionException;

import css.csslibrary.Adapter.Adapter;
import css.csslibrary.model.Book;

public class CheckOutFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    private List<Book> items;
    private Adapter adapter;
    private boolean isNoData;
    private String data;
    private TextView textView;
    private String type_checkout;
    private String title_checkout;
    private String author_checkout;
    private String amount_checkout;
    private CustomTask task;
    private CustomTask1 task1;
    //private CustomTask2 task2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_out,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

        listView=(ListView)getView().findViewById(R.id.list_view_checkout);
        textView=(TextView)getView().findViewById(R.id.text_checkout);

        try {
            SharedPreferences sp=getActivity().getSharedPreferences("LOGIN",Context.MODE_PRIVATE);
            String id=sp.getString("id",null);
            task=new CustomTask();
            String result=task.execute(id).get();
            items=setList(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        adapter=new Adapter(getActivity(),R.layout.list_checkin,items);
        if(isNoData){
            textView.setText("검색 결과가 없습니다.");
        }
        else{
            textView.setVisibility(View.GONE);
            listView.setAdapter(adapter);
        }
        listView.setOnItemClickListener(this);

    }

    private List<Book> setList(String result){
        List<Book> list=new ArrayList<>();
        data=result;
        if(result.equals("")) {
            isNoData = true;
            list.add(new Book(0, "", "", "", ""));
        }else{
            isNoData=false;
            String[] array1;
            String[] array2;
            array1=data.split(":");
            for(int i=0;i<array1.length;++i){
                array2=array1[i].split(";");
                list.add(new Book(0,array2[0],array2[1],array2[2],""));
            }
        }
        return list;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        task1 = new CustomTask1();
        task=new CustomTask();
        type_checkout=((TextView)view.findViewById(R.id.text_type)).getText().toString();
        title_checkout=((TextView)view.findViewById(R.id.text_title)).getText().toString();
        author_checkout=((TextView)view.findViewById(R.id.text_author)).getText().toString();
        amount_checkout=((TextView)view.findViewById(R.id.text_isCheckin)).getText().toString();
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("안내");
        builder.setMessage(title_checkout+"\n\n"+"이 도서를 반납합니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //서버 통신
                SharedPreferences sp = getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                String id = sp.getString("id", null);
                String name=sp.getString("name",null);
                try {
                    task1.execute(type_checkout, title_checkout, author_checkout,id,name);
                    //목록 업데이트 코드 들어가야함
                    String result=task.execute(id).get();       //여기가문제!!!!!!!!!!!!
                    if(result.equals("")){
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("검색 결과가 없습니다.");
                        listView.setVisibility(View.GONE);
                        //Toast.makeText(CheckOutActivity.this,"검색 결과가 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                    items=setList(result);
                    adapter=new Adapter(getActivity(),R.layout.list_checkin,items);
                    listView.setAdapter(adapter);
                    Toast.makeText(getActivity(),"반납하였습니다.",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {}
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



    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://220.149.124.129:8080/CSSLibrary/checkout.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id=" + strings[0];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
                //회원가입처럼 보낼 데이터가 여러 개일 경우 &로 구분하여 작성합니다.
                osw.write(sendMsg);//OutputStreamWriter에 담아 전송합니다.
                osw.flush();
                //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    //jsp에서 보낸 값을 받겠죠?
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
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



    class CustomTask1 extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://220.149.124.129:8080/CSSLibrary/update2.jsp");//보낼 jsp 주소를 ""안에 작성합니다
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "type="+strings[0]+"&title="+strings[1]+"&author="+strings[2]+"&id="+strings[3]+"&name="+strings[4];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
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