package vn.edu.tlu.cse.vvc.menuapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<Food> menuList;
    private boolean isLoggedIn;

    public MenuAdapter(Context context, List<Food> menuList) {
        this.context = context;
        this.menuList = menuList;

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        isLoggedIn = prefs.getBoolean("logged_in", false);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Food item = menuList.get(position);

        holder.txtFoodName.setText(item.getName());
        holder.txtPrice.setText("Giá: " + item.getPrice() + " VND");
        holder.txtDescription.setText(item.getDescription());

        // Hiển thị ảnh
        int imageResId = context.getResources().getIdentifier(
                item.getImage(), "drawable", context.getPackageName());

        if (imageResId != 0) {
            holder.imgFood.setImageResource(imageResId);
        } else {
            holder.imgFood.setImageResource(R.drawable.im1); // fallback
        }

        // Ẩn/hiện nút theo trạng thái đăng nhập
        if (isLoggedIn) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }

        // Xử lý nút XÓA
        holder.btnDelete.setOnClickListener(v -> {
            Database database = new Database(context);
            boolean deleted = database.deleteMenuItem(item.getId());
            if (deleted) {
                menuList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Đã xóa món ăn", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý nút SỬA
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditFoodActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("name", item.getName());
            intent.putExtra("price", item.getPrice());
            intent.putExtra("description", item.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView txtFoodName, txtPrice, txtDescription;
        Button btnEdit, btnDelete;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
