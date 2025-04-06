package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import model.CartItem;
import model.Phone;
import vn.edu.tlu.cse.vvc.mycartman.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;

    public CartAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_cart_adapter, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        Phone phone = item.getPhone();

        holder.tvName.setText(phone.getName());
        holder.tvPrice.setText("฿ " + phone.getPrice());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvTotal.setText("฿ " + String.format("%.2f", item.getTotalPrice()));
        holder.imgPhone.setImageResource(R.drawable.ic_launcher_foreground); // Hoặc dùng Glide nếu cần
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhone;
        TextView tvName, tvPrice, tvQuantity, tvTotal;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhone = itemView.findViewById(R.id.imgPhone);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}
