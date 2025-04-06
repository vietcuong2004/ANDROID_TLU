package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Map;

import model.CartItem;
import model.Phone;
import vn.edu.tlu.cse.vvc.mycartman.R;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneViewHolder> {

    private Context context;
    private List<Phone> phoneList;
    private Map<String, CartItem> cartMap;

    public PhoneAdapter(Context context, List<Phone> phoneList, Map<String, CartItem> cartMap) {
        this.context = context;
        this.phoneList = phoneList;
        this.cartMap = cartMap;
    }

    @NonNull
    @Override
    public PhoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phone, parent, false);
        return new PhoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHolder holder, int position) {
        Phone phone = phoneList.get(position);
        holder.tvName.setText(phone.getName());
        holder.tvPrice.setText("฿ " + phone.getPrice());

        Glide.with(context).load(phone.getImage()).into(holder.imgPhone);

        holder.btnAdd.setOnClickListener(v -> {
            if (cartMap.containsKey(phone.getId())) {
                CartItem item = cartMap.get(phone.getId());
                item.setQuantity(item.getQuantity() + 1);
            } else {
                cartMap.put(phone.getId(), new CartItem(phone, 1));
            }

            Snackbar.make(v, "Đã thêm: " + phone.getName(), Snackbar.LENGTH_SHORT)
                    .setAction("XEM GIỎ", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    public static class PhoneViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhone;
        TextView tvName, tvPrice;
        ImageButton btnAdd;

        public PhoneViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhone = itemView.findViewById(R.id.imgPhone);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
