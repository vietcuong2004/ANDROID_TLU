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
import vn.edu.tlu.cse.vvc.cuahangtienloi.StaffActivity;

public class StaffProductAdapter extends RecyclerView.Adapter<StaffProductAdapter.ProductViewHolder> {

    Context context;
    List<Product> productList;
    DBHelper dbHelper;
    Runnable refreshCallback; // dùng để reload khi sửa/xóa

    public StaffProductAdapter(Context context, List<Product> productList, Runnable refreshCallback) {
        this.context = context;
        this.productList = productList;
        this.refreshCallback = refreshCallback;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_staff, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.txtName.setText(p.getName());
        holder.txtCategory.setText("Danh mục: " + p.getCategory());
        holder.txtPrice.setText("Giá: " + p.getPrice() + " VNĐ");
        holder.txtStock.setText("Tồn kho: " + p.getStock());

        holder.btnEdit.setOnClickListener(v -> {
            if (context instanceof StaffActivity) {
                ((StaffActivity) context).showEditProductDialog(p.getId());
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deleteProduct(p.getId());
            Toast.makeText(context, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            if (refreshCallback != null) refreshCallback.run();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCategory, txtPrice, txtStock;
        Button btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtCategory = itemView.findViewById(R.id.txtProductCategory);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtStock = itemView.findViewById(R.id.txtProductStock);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
