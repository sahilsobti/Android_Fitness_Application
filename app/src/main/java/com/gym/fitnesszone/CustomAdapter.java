package com.gym.fitnesszone;

/**
 * Created by sahil on 23/06/2015.
 */

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
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {

    String[] sexx;
    Context c;
    static Context mcontext;
    View CustomView;
    private static int[] listview_images =
            {
                    R.drawable.mann,R.drawable.womann};
    public CustomAdapter(Context context, String[] sex, String[] s) {
        super(context, R.layout.custom_view, s);
        // TODO Auto-generated constructor stub
        c=context;
        sexx = sex;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        try {
            LayoutInflater li = LayoutInflater.from(getContext());
            CustomView = li.inflate(R.layout.custom_view, parent, false);
            String member = getItem(position);
            TextView m = (TextView) CustomView.findViewById(R.id.PNamee);
            final ImageView iv = (ImageView) CustomView.findViewById(R.id.Pimage);
            m.setText(member);
            if ((sexx[position].trim().toLowerCase()).equals("male")) {
             //  iv.setImageResource(R.drawable.mann);
                iv.setImageBitmap(getRoundedShape(decodeFile(c, listview_images[0]),200));
            } else if ((sexx[position].trim().toLowerCase()).equals("female")) {
               // iv.setImageResource(R.drawable.womann);
                iv.setImageBitmap(getRoundedShape(decodeFile(c, listview_images[1]),200));
            }
        }catch(Exception e){

        }
        return CustomView;
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
