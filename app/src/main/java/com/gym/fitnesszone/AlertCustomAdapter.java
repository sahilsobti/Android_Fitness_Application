package com.gym.fitnesszone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sahil on 25/06/2015.
 */
public class AlertCustomAdapter extends ArrayAdapter<String> {
    String[] sexx,pay_date,idd;
    Context c;
    View CustomView;
    static Context mcontext;
    customButtonListener customListner;
    private static int[] listview_images =
            {
                    R.drawable.mann,R.drawable.womann};

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
    }
    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }


    public AlertCustomAdapter(Context context,String[] id, String[] sex, String[] name, String[] pdated) {
        super(context, R.layout.alert_custom_view, name);
        c= context;
        sexx= sex;
        idd =id;
        pay_date= pdated;
    }


    static class ViewHolder {
        Button receive;
        TextView name,payed;
        ImageView iv;
    }
    @Override
    public View getView(final int position, View cV, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        try {
            if (cV == null) {
                LayoutInflater li = LayoutInflater.from(getContext());
                cV = li.inflate(R.layout.alert_custom_view, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) cV.findViewById(R.id.PNam);
                holder.payed = (TextView) cV.findViewById(R.id.PayDate);
                holder.receive = (Button) cV.findViewById(R.id.breceived);
                holder.iv = (ImageView) cV.findViewById(R.id.Pim);
                cV.setTag(holder);
            }
            else{
                holder = (ViewHolder) cV.getTag();
            }
            String member = getItem(position);
            holder.name.setText(member);
            holder.payed.setText(pay_date[position].trim());
            if ((sexx[position].trim().toLowerCase()).equals("male")) {
               // holder.iv.setImageResource(R.drawable.mann);
                holder.iv.setImageBitmap(getRoundedShape(decodeFile(c, listview_images[0]),200));
            } else if ((sexx[position].trim().toLowerCase()).equals("female")) {
               // holder.iv.setImageResource(R.drawable.womann);
                holder.iv.setImageBitmap(getRoundedShape(decodeFile(c, listview_images[1]),200));
            }
            holder.receive.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (customListner != null) {
                        customListner.onButtonClickListner(position, idd[position]);
                    }

                }
            });
        }catch(Exception e){

        }
        return cV;
    }
    public static Bitmap decodeFile(Context context,int resId) {
        try {
// decode image size
            mcontext=context;
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(mcontext.getResources(), resId, o);
// Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 200;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true)
            {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
// decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeResource(mcontext.getResources(), resId, o2);
        } catch (Exception e) {
        }
        return null;
    }
    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage,int width) {
        // TODO Auto-generated method stub
        int targetWidth = width;
        int targetHeight = width;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }


}
