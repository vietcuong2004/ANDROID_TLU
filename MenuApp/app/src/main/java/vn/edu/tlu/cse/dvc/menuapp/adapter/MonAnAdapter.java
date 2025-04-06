package vn.edu.tlu.cse.dvc.menuapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.edu.tlu.cse.dvc.menuapp.R;
import vn.edu.tlu.cse.dvc.menuapp.model.MonAn;

public class MonAnAdapter extends ArrayAdapter<MonAn> {
    private Context context;
    private List<MonAn> monAnList;

    public MonAnAdapter(Context context, List<MonAn> monAnList) {
        super(context, 0, monAnList);
        this.context = context;
        this.monAnList = monAnList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_monan, parent, false);
        }

        MonAn monAn = monAnList.get(position);

        ImageView imgMonAn = convertView.findViewById(R.id.imgMonAn);
        TextView tvTenMon = convertView.findViewById(R.id.tvTenMon);
        TextView tvGia = convertView.findViewById(R.id.tvGia);
        TextView tvMoTa = convertView.findViewById(R.id.tvMoTa);

        tvTenMon.setText(monAn.getTenMon());
        tvGia.setText("Giá: " + monAn.getGia() + " VNĐ");
        tvMoTa.setText("Mô tả: " + monAn.getMoTa());

        // Hiển thị ảnh từ ID tài nguyên drawable
        imgMonAn.setImageResource(monAn.getAnhMinhHoa());

        return convertView;
    }
}