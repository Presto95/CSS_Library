package css.csslibrary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ScheduleFragment extends Fragment {

    private TextView textDate;
    private TextView textContent;
    private TextView textExtra;
    private CalendarView calendarView;
    private CustomTask customTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        calendarView=(CalendarView)getView().findViewById(R.id.calendarView);
        textDate=(TextView)getView().findViewById(R.id.text_date);
        textContent=(TextView)getView().findViewById(R.id.text_content);
        textContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        textContent.setText("날짜를 선택하세요.");


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                textContent.setText("");
                customTask=new CustomTask();
                try {
                    String result=customTask.execute(Integer.toString(year),Integer.toString(month+1),Integer.toString(dayOfMonth)).get();
                    textDate.setText(year+"년 "+(month+1)+"월 "+dayOfMonth+"일\n");
                    if(!result.equals("")){
                        String[] array1=result.split(":");
                        for(int i=0;i<array1.length;++i){
                            String[] array2=array1[i].split(";");
                            textContent.setText(textContent.getText()+array2[0]+"\n"+array2[1]+"\n\n");
                        }
                    }
                    else{
                        textContent.setText("데이터 없음");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    class CustomTask extends AsyncTask<String, Void, String> { //대출
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://220.149.124.129:8080/CSSLibrary/schedule.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "year="+strings[0]+"&month="+strings[1]+"&day="+strings[2];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
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
