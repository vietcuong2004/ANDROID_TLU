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
import vn.edu.tlu.cse.vvc.cuahangtienloi.DBHelper;
import vn.edu.tlu.cse.vvc.cuahangtienloi.R;

public class CustomerProductAdapter extends RecyclerView.Adapter<CustomerProductAdapter.CartViewHolder> {

    Context context;
    List<Product> productList;
    DBHelper dbHelper;
    String userEmail;

    public CustomerProductAdapter(Context context, List<Product> productList, String userEmail) {
        this.context = context;
        this.productList = productList;
        this.userEmail = userEmail;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_customer, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.txtName.setText(p.getName());
        holder.txtCategory.setText("Danh mục: " + p.getCategory());
        holder.txtPrice.setText("Giá: " + p.getPrice() + " VNĐ");
        holder.txtStock.setText("Tồn kho: " + p.getStock());

        holder.btnAddToCart.setOnClickListener(v -> {
            if (p.getStock() > 0) {
                boolean added = dbHelper.addToCart(userEmail, p.getName(), 1, p.getPrice());
                if (added) {
                    Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi khi thêm giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCategory, txtPrice, txtStock;
        Button btnAddToCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtCategory = itemView.findViewById(R.id.txtProductCategory);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtStock = itemView.findViewById(R.id.txtProductStock);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
