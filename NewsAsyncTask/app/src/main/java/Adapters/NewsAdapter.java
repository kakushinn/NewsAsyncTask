package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guochen.newsasynctask.R;

import java.util.List;

import Entities.NewsBean;
import Logic.ImageLoader;

/**
 * Created by guochen on 2016/09/13.
 */
public class NewsAdapter extends BaseAdapter {
    private Context context;
    private List<NewsBean> newsBeanList;
    private LayoutInflater inflater;

    public NewsAdapter(Context context, List<NewsBean> newsBeanList) {
        this.context = context;
        this.newsBeanList = newsBeanList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return newsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_lv,null);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView)convertView.findViewById(R.id.item_icon);
            viewHolder.tvContent =(TextView)convertView.findViewById(R.id.item_content);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.item_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        String url = newsBeanList.get(position).getIconUrl();
        viewHolder.ivIcon.setTag(url);
        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        new ImageLoader().showImageByThread(viewHolder.ivIcon,url);
        viewHolder.tvTitle.setText(newsBeanList.get(position).getTitle());
        viewHolder.tvContent.setText(newsBeanList.get(position).getContent());
        return convertView;
    }

    class ViewHolder{
         public TextView tvTitle , tvContent;
         public ImageView ivIcon;
    }
}
