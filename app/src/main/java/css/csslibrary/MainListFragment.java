package css.csslibrary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainListFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    private ArrayAdapter<String> adapter;   //List<> : 크기 제한 없는 배열

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_list,container,false);
    }
    @Override
    public void onStart() {
        super.onStart();


        listView=(ListView)getView().findViewById(R.id.list_view);

        List<String> items=setList();
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,items);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }
    private List<String> setList(){
        SharedPreferences sp=getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        List<String> list=new ArrayList<>();
        if(sp.getBoolean("autoLogin",false)==true)
            list.add("자동 로그인 해제");
        else
            list.add("자동 로그인 설정");
        list.add("비밀번호 변경");
        //list.add("도움말");
        list.add("버그 리포트");
        list.add("CSS 스케쥴러");
        list.add("CSS 싸이월드 클럽 접속");
        list.add("만든 사람들");
        list.add("종료");
        return list;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                final SharedPreferences sp=getActivity().getSharedPreferences("LOGIN",Context.MODE_PRIVATE);
                AlertDialog.Builder autoLoginDialog=new AlertDialog.Builder(getActivity());
                autoLoginDialog.setTitle("자동 로그인");
                if(sp.getBoolean("autoLogin",false)==true){
                    autoLoginDialog.setMessage("자동 로그인을 해제합니다.");
                    autoLoginDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor=sp.edit();
                            editor.putBoolean("autoLogin",false);
                            editor.commit();
                            Toast.makeText(getActivity(),"자동 로그인을 해제하였습니다.",Toast.LENGTH_SHORT).show();

                            List<String> items=setList();
                            adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,items);
                            listView.setAdapter(adapter);

                        }
                    });
                    autoLoginDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
                else{
                    autoLoginDialog.setMessage("자동 로그인을 설정합니다.");
                    autoLoginDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor=sp.edit();
                            editor.putBoolean("autoLogin",true);
                            editor.commit();
                            Toast.makeText(getActivity(),"자동 로그인을 설정하였습니다.",Toast.LENGTH_SHORT).show();

                            List<String> items=setList();
                            adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,items);
                            listView.setAdapter(adapter);
                        }
                    });
                    autoLoginDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
                autoLoginDialog.create();
                autoLoginDialog.show();

                break;

            case 1:
                //startActivity(new Intent(getActivity(),ChangeActivity.class));
                ChangeFragment changeFragment = new ChangeFragment();
                FragmentTransaction changeTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                changeTransaction.replace(R.id.content,changeFragment);
                changeTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                changeTransaction.addToBackStack(null);
                changeTransaction.commit();
                break;
            /*case 1:
                //startActivity(new Intent(getActivity(),HelpActivity.class));
                HelpFragment helpFragment = new HelpFragment();
                FragmentTransaction helpTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                helpTransaction.replace(R.id.content,helpFragment);
                helpTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                helpTransaction.addToBackStack(null);
                helpTransaction.commit();
                break;*/
            case 2:
                //startActivity(new Intent(getActivity(),BugReportActivity.class));
                BugReportFragment bugReportFragment = new BugReportFragment();
                FragmentTransaction bugreportTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                bugreportTransaction.replace(R.id.content,bugReportFragment);
                bugreportTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                bugreportTransaction.addToBackStack(null);
                bugreportTransaction.commit();
                break;
            case 3:
                ScheduleFragment scheduleFragment=new ScheduleFragment();
                FragmentTransaction scheduleTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                scheduleTransaction.replace(R.id.content,scheduleFragment);
                scheduleTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                scheduleTransaction.addToBackStack(null);
                scheduleTransaction.commit();
                break;
            case 4:
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://club.cyworld.com/ClubV1/Home.cy/53489439"));
                startActivity(intent);
                break;
            case 5:
                AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                dialog.setTitle("Project [CSS Library]");
                dialog.setMessage("Developer\n9th LEE Han Gyoel\n\nThanks To\n4th KIM Jong Hoon");
                dialog.setPositiveButton("확인",null);
                dialog.create();
                dialog.show();
                break;
            case 6:
                AlertDialog.Builder dialog2=new AlertDialog.Builder(getActivity());
                dialog2.setTitle("안내");
                dialog2.setMessage("어플리케이션을 종료합니다.");
                dialog2.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                dialog2.setNegativeButton("취소",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){

                    }
                });
                dialog2.create();
                dialog2.show();
                break;
        }
    }


}
