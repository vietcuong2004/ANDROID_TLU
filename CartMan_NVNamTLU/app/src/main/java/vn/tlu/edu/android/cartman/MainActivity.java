package vn.tlu.edu.android.cartman;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import vn.tlu.edu.android.cartman.cart.CartActivity;
import vn.tlu.edu.android.cartman.cart.model.Cart;
import vn.tlu.edu.android.cartman.databinding.ActivityMainBinding;
import vn.tlu.edu.android.cartman.product.model.ProductRepository;
import vn.tlu.edu.android.cartman.product.adapter.ProductsAdapter;
import vn.tlu.edu.android.cartman.product.model.Product;
import android.view.Menu;
import android.view.MenuItem;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ProductRepository productRepository;
    RecyclerView rvProduct;
    private Cart cart = new Cart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        initData();

        rvProduct = binding.rvproduct;

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvProduct.setLayoutManager(mLayoutManager);

        ProductsAdapter rvAdapter = new ProductsAdapter(this, this.productRepository.getProductList());
        rvProduct.setAdapter(rvAdapter);
    }

    private void initData() {
        ArrayList<Product> alProduct = new ArrayList<>();
        Random random = new Random(); // Reuse Random instance
        for (int i = 0; i < 100; i++) {
            Product p = new Product(i, "ProductName" + i);
            int resID = getResId("ss_" + i % 9, R.drawable.class);
            if (resID != -1) {
                Uri imgUri = getUri(resID);
                p.setImage(imgUri);
            } else {
                Log.e("MainActivity", "Resource not found for ss_" + i % 9);
                p.setImage(null); // Handle missing resource
            }
            // Generate price and round to 2 decimal places
            float price = random.nextFloat() * 1000;
            price = Math.round(price * 100.0f) / 100.0f;
            p.setPrice(price);
            alProduct.add(p);
        }
        this.productRepository = new ProductRepository(alProduct);
    }

    public Uri getUri(int resId) {
        return Uri.parse("android.resource://" + this.getPackageName() + "/" + resId);
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnu_cart) {
            Log.d("MainActivity", "Cart menu clicked");
            Intent intent = new Intent(this, CartActivity.class);
            intent.putExtra("cart", (CharSequence) cart); // Pass the cart (ensure Cart implements Parcelable/Serializable)
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}