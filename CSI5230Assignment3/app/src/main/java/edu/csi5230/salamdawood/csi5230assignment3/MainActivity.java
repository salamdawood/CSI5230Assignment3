package edu.csi5230.salamdawood.csi5230assignment3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView = null;
    ProductAdapter productAdapter = null;
    Button addButton = null;
    ArrayList<Product> initialProducts = new ArrayList<>();

    final int PRODUCT_OK = 1;
    Intent otherIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        addButton = (Button) findViewById(R.id.addButton);

        //get reference of products
        initialProducts = ((CSI5230Assignment3Application) getApplication()).getProducts();

        productAdapter = new ProductAdapter(this, 0, initialProducts);

        //set adapter to listview
        listView.setAdapter(productAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ProductForm.class);
                startActivityForResult(i, PRODUCT_OK);
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                ViewGroup group = (ViewGroup) view;
////                TextView textview = (TextView) group.getChildAt(1);
////
////                String data = textview.getText().toString();
////
////                Toast toast = Toast.makeText(view.getContext() ,data, Toast.LENGTH_LONG);
////                toast.show();
//                productAdapter.notifyDataSetChanged();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String productNameValue, productPriceValue;

        if (requestCode == PRODUCT_OK){
            if (resultCode == Activity.RESULT_OK) {
                productNameValue = data.getStringExtra("PRODUCT_NAME");
                productPriceValue = data.getStringExtra("PRODUCT_PRICE");

                String filename = data.getStringExtra("IMAGE");

                Product product = new Product(productNameValue, productPriceValue, filename);
                productAdapter.add(product);
                productAdapter.notifyDataSetChanged();
                productAdapter.notifyDataSetInvalidated();
                listView.invalidateViews();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        otherIntent = getIntent();
        if (otherIntent.hasExtra("INDEX")){
            String productNameValue, productPriceValue;

            productNameValue = otherIntent.getStringExtra("PRODUCT_NAME");
            productPriceValue = otherIntent.getStringExtra("PRODUCT_PRICE");
            String filename = otherIntent.getStringExtra("IMAGE");
            int index = otherIntent.getIntExtra("INDEX", -1);

            productAdapter.products.get(index).setName(productNameValue);
            productAdapter.products.get(index).setPrice(productPriceValue);
            productAdapter.products.get(index).setImage(filename);
        }

        productAdapter.notifyDataSetChanged();
        productAdapter.notifyDataSetInvalidated();
        listView.invalidateViews();
    }
}