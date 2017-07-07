package css.csslibrary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private ArrayAdapter<String> adapter;   //List<> : 크기 제한 없는 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        listView=(ListView)findViewById(R.id.list_view);

        List<String> items=findList();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }
    private List<String> findList(){
        List<String> list=new ArrayList<>();
        list.add("대출");
        list.add("반납");
        list.add("대출중");
        list.add("계정 정보 변경");
        list.add("Thanks To");
        return list;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                startActivity(new Intent(this,CheckInActivity.class));
                break;
            case 1:
                startActivity(new Intent(this,CheckOutActivity.class));
                break;
            case 2:
                startActivity(new Intent(this,CheckInIngActivity.class));
                break;
            case 3:
                startActivity(new Intent(this,ChangeActivity.class));
                break;
            case 4:
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setTitle("Thanks To");
                dialog.setMessage("Dev : 9期 LEE Han-Gyeol"+"\n"+"asdf"+"\n"+"asdf"+"\n"+"adf"+"\n"+"asdf"+"\n"+"asdf"+"\n"+"adf");
                dialog.setPositiveButton("확인",null);
                dialog.create();
                dialog.show();
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode()== KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_UP){
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setTitle("안내");
            dialog.setMessage("어플리케이션을 종료합니다.");
            dialog.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setNegativeButton("취소",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){

                }
            });
            dialog.create();
            dialog.show();
        }
        return true;
    }
}
