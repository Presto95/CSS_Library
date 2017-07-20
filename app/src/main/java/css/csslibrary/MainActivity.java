package css.csslibrary;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private CheckInFragment checkInFragment;
    private CheckOutFragment checkOutFragment;
    private CheckInIngFragment checkInIngFragment;
    private MainListFragment mainListFragment;
    private InputMethodManager imm;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            BottomNavigationView view=(BottomNavigationView)findViewById(R.id.navigation);

            switch (item.getItemId()) {
                //각 메뉴 클릭했을 때 뷰가 보이게 해야 함
                //fragment 스택 초기화
                case R.id.navigation_checkin:
                    getSupportFragmentManager().popBackStackImmediate(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    //getSupportFragmentManager().beginTransaction().replace(R.id.content,checkInFragment).commit();
                    FragmentTransaction checkinTransaction=getSupportFragmentManager().beginTransaction();
                    checkinTransaction.replace(R.id.content,checkInFragment);
                    checkinTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    checkinTransaction.commit();
                    return true;
                case R.id.navigation_checkout:
                    getSupportFragmentManager().popBackStackImmediate(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    //getSupportFragmentManager().beginTransaction().replace(R.id.content,checkOutFragment).commit();
                    FragmentTransaction checkoutTransaction=getSupportFragmentManager().beginTransaction();
                    checkoutTransaction.replace(R.id.content,checkOutFragment);
                    checkoutTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    checkoutTransaction.commit();
                    return true;
                case R.id.navigation_checkining:
                    getSupportFragmentManager().popBackStackImmediate(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    //getSupportFragmentManager().beginTransaction().replace(R.id.content,checkInIngFragment).commit();
                    FragmentTransaction checkiningTransaction=getSupportFragmentManager().beginTransaction();
                    checkiningTransaction.replace(R.id.content,checkInIngFragment);
                    checkiningTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    checkiningTransaction.commit();
                    return true;
                case R.id.navigation_another:
                    getSupportFragmentManager().popBackStackImmediate(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    //여긴 그냥 기본리스트뷰로 나머지 것들 나타내자
                    FragmentTransaction anotherTransaction=getSupportFragmentManager().beginTransaction();
                    anotherTransaction.replace(R.id.content,mainListFragment);
                    anotherTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    anotherTransaction.commit();
                    //getSupportFragmentManager().beginTransaction().replace(R.id.content,mainListFragment).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        checkInFragment=new CheckInFragment();
        checkOutFragment=new CheckOutFragment();
        checkInIngFragment=new CheckInIngFragment();
        mainListFragment=new MainListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.content,checkInFragment).commit();




    }

    /*@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode()== KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_UP){
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            if(getSupportFragmentManager().getBackStackEntryCount()==0){
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
            else{
                getSupportFragmentManager().popBackStack();
            }
        }
        return true;
    }*/

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_UP){


            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            if(getSupportFragmentManager().getBackStackEntryCount()==0){
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
            else{
                getSupportFragmentManager().popBackStack();
            }
        }
        return true;
    }

}
