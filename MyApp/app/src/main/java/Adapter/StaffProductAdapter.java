package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Model.Product;
import vn.edu.tlu.cse.vvc.myapp.DBHelper;
import vn.edu.tlu.cse.vvc.myapp.R;

public class StaffProductAdapter extends RecyclerView.Adapter<StaffProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private DBHelper dbHelper;
    private Runnable reloadCallback;

    public StaffProductAdapter(Context context, List<Product> productList, DBHelper dbHelper, Runnable reloadCallback) {
        this.context = context;
        this.productList = productList;
        this.dbHelper = dbHelper;
        this.reloadCallback = reloadCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtName.setText(product.getName());
        holder.txtBrand.setText("Hãng: " + product.getBrand());
        holder.txtPrice.setText("Giá: " + product.getPrice() + " VNĐ");
        holder.txtStock.setText("Tồn kho: " + product.getStock());

        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deleteProduct(product.getId());
            Toast.makeText(context, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            if (reloadCallback != null) reloadCallback.run();
        });

        holder.btnEdit.setOnClickListener(v -> showEditDialog(product));
    }

    private void showEditDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_product, null);
        builder.setView(view);

        EditText edtName = view.findViewById(R.id.edtProductName);
        EditText edtBrand = view.findViewById(R.id.edtProductBrand);
        EditText edtPrice = view.findViewById(R.id.edtProductPrice);
        EditText edtStock = view.findViewById(R.id.edtProductStock);
        EditText edtNote = view.findViewById(R.id.edtProductNote);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        edtName.setText(product.getName());
        edtBrand.setText(product.getBrand());
        edtPrice.setText(String.valueOf(product.getPrice()));
        edtStock.setText(String.valueOf(product.getStock()));
        edtNote.setText(product.getNote());

        AlertDialog dialog = builder.create();
        dialog.show();

        //Sửa sản phẩm
        btnConfirm.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String brand = edtBrand.getText().toString().trim();
            int price = Integer.parseInt(edtPrice.getText().toString().trim());
            int stock = Integer.parseInt(edtStock.getText().toString().trim());
            String note = edtNote.getText().toString().trim();

            dbHelper.updateProduct(product.getId(), name, brand, price, stock, note);
            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            if (reloadCallback != null) reloadCallback.run();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtBrand, txtPrice, txtStock;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtBrand = itemView.findViewById(R.id.txtProductBrand);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtStock = itemView.findViewById(R.id.txtProductStock);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
