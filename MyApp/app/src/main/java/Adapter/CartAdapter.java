package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Model.Product;
import vn.edu.tlu.cse.vvc.myapp.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<Product> cartItems;
    OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

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
        holder.txtBrand.setText("Hãng: " + p.getBrand());
        holder.txtPrice.setText("Đơn giá: " + p.getPrice() + " VNĐ");
        holder.txtQuantity.setText(String.valueOf(p.getQuantity()));
        holder.txtItemTotal.setText("Thành tiền: " + p.getTotal() + " VNĐ");

        holder.btnPlus.setOnClickListener(v -> {
            p.setQuantity(p.getQuantity() + 1);
            notifyItemChanged(position);
            listener.onCartChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (p.getQuantity() > 1) {
                p.setQuantity(p.getQuantity() - 1);
                notifyItemChanged(position);
                listener.onCartChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtBrand, txtPrice, txtQuantity, txtItemTotal;
        ImageButton btnPlus, btnMinus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtBrand = itemView.findViewById(R.id.txtProductBrand);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtItemTotal = itemView.findViewById(R.id.txtItemTotal);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}
