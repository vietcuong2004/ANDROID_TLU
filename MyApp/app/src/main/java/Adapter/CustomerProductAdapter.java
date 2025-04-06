package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import Model.Product;
import vn.edu.tlu.cse.vvc.myapp.DBHelper;
import vn.edu.tlu.cse.vvc.myapp.R;

public class CustomerProductAdapter extends RecyclerView.Adapter<CustomerProductAdapter.ViewHolder> {

    Context context;
    List<Product> productList;
    String userEmail;
    DBHelper dbHelper;

    public CustomerProductAdapter(Context context, List<Product> productList, String userEmail) {
        this.context = context;
        this.productList = productList;
        this.userEmail = userEmail;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtName.setText(product.getName());
        holder.txtBrand.setText("Hãng: " + product.getBrand());
        holder.txtPrice.setText("Giá: " + product.getPrice() + " VNĐ");
        holder.txtStock.setText("Tồn kho: " + product.getStock());

        holder.btnAddToCart.setOnClickListener(v -> {
            if (product.getStock() > 0) {
                // Truyền đúng tham số: productName là String
                dbHelper.addToCart(userEmail, product.getId(), product.getName(), product.getPrice());
                Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Sản phẩm hết hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtBrand, txtPrice, txtStock;
        Button btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtBrand = itemView.findViewById(R.id.txtProductBrand);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtStock = itemView.findViewById(R.id.txtProductStock);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
