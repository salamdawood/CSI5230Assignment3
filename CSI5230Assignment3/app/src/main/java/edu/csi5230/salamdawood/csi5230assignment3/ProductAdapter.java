package edu.csi5230.salamdawood.csi5230assignment3;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by salamdawood on 10/26/17.
 */

public class ProductAdapter extends ArrayAdapter {

    ArrayList<Product> products = new ArrayList<>();
    Context context;

    public ProductAdapter(Context context,int resource, ArrayList<Product> objects) {
        super(context, resource, objects);

        this.context = context;
        this.products = objects;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.product_item, null);
        }

        final Product product = products.get(i);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView price = (TextView) view.findViewById(R.id.price);
        ImageView productImage = (ImageView) view.findViewById(R.id.productImage);

        ImageButton editButton = (ImageButton) view.findViewById(R.id.editButton);
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.trashButton);

        name.setText(product.getName());
        price.setText(product.getPrice());


        Bitmap bitmap = null;
        String filename = product.getImage();
        try {
            FileInputStream is = this.getContext().openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        productImage.setImageBitmap(bitmap);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProductForm.class);
                intent.putExtra("index", i);
                intent.putExtra("name", product.getName());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("image", product.getImage());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.setFlags(PendingIntent.FLAG_UPDATE_CURRENT);
                view.getContext().startActivity(intent);
                notifyDataSetChanged();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(product);
                notifyDataSetChanged();
                notifyDataSetInvalidated();
            }
        });
        return view;
    }
}
