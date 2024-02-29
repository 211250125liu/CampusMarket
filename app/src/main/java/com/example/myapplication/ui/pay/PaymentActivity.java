package com.example.myapplication.ui.pay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.HTTP.service.OrderService;
import com.example.myapplication.HTTP.Retrofit;
import com.example.myapplication.ui.data.Token;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    long goodsNumber = 0;
    long orderNumber = 0;
    double price = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // 获取布局中的视图
        TextView textOrderNumber = findViewById(R.id.textOrderNumber);
        TextView textPrice = findViewById(R.id.textPrice);
        Button btnPay = findViewById(R.id.btnPay);
        Button btnCancel = findViewById(R.id.btnCancel);

        //直接从之前一个页面拿数据，这个继续发送数据到后端吧
        Intent thisIntent = getIntent();
        if (thisIntent != null) {
            goodsNumber = thisIntent.getLongExtra("GoodsId", 0);
            price = thisIntent.getDoubleExtra("price", 0.00);
            orderNumber = thisIntent.getLongExtra("OrderId", 0);
        }

        // 设置订单信息
        textOrderNumber.setText("商品编号: " + goodsNumber);
        textPrice.setText("价格: " + price);

        // 设置支付按钮点击事件
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 在这里处理支付逻辑
                // 可以跳转到支付页面、调用支付接口等
                // 这里只是一个简单的示例

                Call<ResponseBody> orderCall = Retrofit.getRetrofitInstance().create(OrderService.class).payOrder(Token.getInstance().getToken(), orderNumber);
                orderCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e("Order", "success");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Order", "fail");
                    }
                });

                showToast("支付成功");
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(view -> {
            this.finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Call<ResponseBody> orderCall = Retrofit.getRetrofitInstance().create(OrderService.class).cancelOrder(Token.getInstance().getToken(), orderNumber);

        orderCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Order", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Order", "fail");
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
