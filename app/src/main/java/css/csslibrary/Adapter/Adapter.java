package css.csslibrary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import css.csslibrary.R;
import css.csslibrary.model.Book;

/**
 * Created by yoohan95 on 2017-07-02.
 */

public class Adapter extends BaseAdapter{

    private Context context;
    private int layoutId;
    private List<Book> items;
    private LayoutInflater inflater;

    public Adapter(Context context, int layoutId, List<Book> items){
        this.context=context;
        this.layoutId=layoutId;
        this.items=items;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView=inflater.inflate(this.layoutId,null);
        TextView type=(TextView)convertView.findViewById(R.id.text_type);
        final TextView title=(TextView)convertView.findViewById(R.id.text_title);
        TextView author=(TextView)convertView.findViewById(R.id.text_author);
        TextView amount=(TextView)convertView.findViewById(R.id.text_isCheckin);
        title.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                title.setSelected(true);
            }
        });

        Book book=this.items.get(position);
        type.setText(book.getType());
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        amount.setText(String.valueOf(book.getAmount()));
        return convertView;
    }
}
