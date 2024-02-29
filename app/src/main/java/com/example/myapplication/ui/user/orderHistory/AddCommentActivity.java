package com.example.myapplication.ui.user.orderHistory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.HTTP.model.CommentRequest;
import com.example.myapplication.HTTP.service.CommentService;
import com.example.myapplication.HTTP.Retrofit;
import com.example.myapplication.ui.data.Token;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCommentActivity extends AppCompatActivity {

    EditText comment;
    Button commit;
    RadioGroup ratingOptions;
    boolean selectedRating;
    String commentS;
    Long orderId;
    Long buyerId;
    Long sellerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        comment = findViewById(R.id.comment);
        commit = findViewById(R.id.buttonCommit);
        ratingOptions = findViewById(R.id.ratingOptions);
        buyerId = getIntent().getLongExtra("buyerId", 0);
        orderId = getIntent().getLongExtra("orderId", 0);
        sellerId = getIntent().getLongExtra("sellerId", 0);

        commit.setOnClickListener(view -> {
            sendComment();
        });

        ratingOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);

                if (selectedRadioButton != null) {
                    selectedRating = selectedRadioButton.getText().toString().equals("好评");
                }
            }
        });
    }

    private void sendComment() {
        commentS = this.comment.getText().toString();

        Call<ResponseBody> call = Retrofit.getRetrofitInstance().create(CommentService.class).addComment(Token.getInstance().getToken(), new CommentRequest(orderId, buyerId, sellerId, selectedRating, commentS));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent();
                    intent.putExtra("commentText", commentS);
                    intent.putExtra("orderId", orderId);
                    setResult(Activity.RESULT_OK, intent);
                    AddCommentActivity.this.finish();
                    Toast.makeText(AddCommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("AddCommentActivity", "Response Error Body: " + errorBody);
                    } catch (IOException e) {
                        Log.e("AddCommentActivity", "Error while getting error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddCommentActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
