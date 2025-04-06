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
import vn.edu.tlu.cse.vvc.cuahangtienloi.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<Product> cartItems;
    OnCartChangeListener listener;

    public CartAdapter(Context context, List<Product> cartItems, OnCartChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product p = cartItems.get(position);

        holder.txtName.setText(p.getName());
        holder.txtPrice.setText("Đơn giá: " + p.getPrice() + " VNĐ");
        holder.txtCategory.setText("Danh mục: " + p.getCategory());
        holder.txtQuantity.setText(String.valueOf(p.getQuantity()));
        holder.txtItemTotal.setText("Thành tiền: " + (p.getPrice() * p.getQuantity()) + " VNĐ");

        holder.btnPlus.setOnClickListener(v -> {
            p.setQuantity(p.getQuantity() + 1);
            notifyItemChanged(position);
            listener.onCartChanged(); // cập nhật tổng tiền
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (p.getQuantity() > 1) {
                p.setQuantity(p.getQuantity() - 1);
                notifyItemChanged(position);
                listener.onCartChanged(); // cập nhật tổng tiền
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public interface OnCartChangeListener {
        void onCartChanged(); // để CartActivity cập nhật tổng tiền
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtCategory, txtQuantity, txtItemTotal;
        ImageButton btnPlus, btnMinus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtCategory = itemView.findViewById(R.id.txtProductCategory);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtItemTotal = itemView.findViewById(R.id.txtItemTotal);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}
