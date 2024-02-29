package com.example.myapplication.ui.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.HTTP.Retrofit;
import com.example.myapplication.HTTP.model.RegisterRequest;
import com.example.myapplication.HTTP.service.AuthService;
import com.example.myapplication.HTTP.service.ImageService;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private ImageView imageViewSelected;
    private EditText StudentId;
    private EditText Name;
    private EditText Password;
    private EditText Email;

    private EditText Code;
    private Button buttonRegister;
    private Button ButtonCode;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;

    private boolean registerOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
        imageViewSelected = findViewById(R.id.imageViewSelected);
        StudentId = findViewById(R.id.editTextStudentId);
        Name = findViewById(R.id.editTextNickname);
        Password = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.setComment);
        Email = findViewById(R.id.editTextEmail);
        Code = findViewById(R.id.editTextCode);
        ButtonCode = findViewById(R.id.NeedCodeImage);

        // 初始化相机权限请求
        requestCameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 相机权限已经被授予，可以执行相机操作
                showImagePickerDialog();
            } else {
                // 用户拒绝了相机权限，可以显示一个提示或执行其他适当的操作
                Toast.makeText(RegisterActivity.this, "无法打开相机", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSelectImage.setOnClickListener(v -> {
            // 检查相机权限
            checkCameraPermission();
        });
        ButtonCode.setOnClickListener(v->getCode());
        buttonRegister.setOnClickListener(v -> register());
    }

    private boolean isEmail(String email) {
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        return pattern.matcher(email).matches();
    }


    private void getCode(){
        // 确保邮箱正确
        String email = Email.getText().toString();
        String[] suffix = email.split("@");
        if(email.equals("")){
            Toast.makeText(RegisterActivity.this,"请输入邮箱",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isEmail(email)){
            Toast.makeText(RegisterActivity.this,"请输入正确邮箱",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StudentId.getText().toString().equals("")){
            Toast.makeText(RegisterActivity.this,"请输入学号",Toast.LENGTH_LONG).show();
            return;
        }
        String[] suffix2 = suffix[1].split("\\.");

        if((!suffix[0].equals(StudentId.getText().toString())) || (!suffix2[0].equals("nju") && !suffix2[1].equals("nju"))){
            Toast.makeText(RegisterActivity.this,"请输入正确的南大邮箱",Toast.LENGTH_LONG).show();
            return;
        }
        AuthService authService = Retrofit.getRetrofitInstance().create(AuthService.class);

        Call<ResponseBody> call = authService.getCode(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"获取验证码成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterActivity.this,"后端错误",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"请求错误",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 如果权限没有被授予，请求权限
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        } else {
            // 相机权限已经被授予，可以执行相机操作
            showImagePickerDialog();
        }
    }

    private File saveBitmapToFile(Bitmap bitmap) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, "image.jpg");

        try (OutputStream outputStream = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile;
    }


    private void register() {
        if(imageViewSelected.getDrawable() == null){
            Toast.makeText(RegisterActivity.this, "请上传头像", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) imageViewSelected.getDrawable()).getBitmap();
        File imageFile = saveBitmapToFile(bitmap);

        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequestBody);
        List<MultipartBody.Part> files = new ArrayList<>();

        files.add(imagePart);
        // 获取 Retrofit 实例

        ImageService imageService = Retrofit.getRetrofitInstance2().create(ImageService.class);

        Call<ResponseBody> call2 = imageService.uploadImage2(files);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        Type type1 = new TypeToken<String[]>(){}.getType();
                        String[] data1 = gson.fromJson(result,type1);

//                        photos = data1.getData();
                        postToBackEnd(data1[0]);
//                        Log.d("avatar: ", photos[0]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
//                    Toast.makeText(RegisterActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "图片过大", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });

        if(registerOk) {
            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void postToBackEnd(String photo) {
        String studentId = StudentId.getText().toString();
        String name = Name.getText().toString();
        String password = Password.getText().toString();
        String email = Email.getText().toString();
        String code = Code.getText().toString();
        if(code.equals("")){
            Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(studentId.equals("") || name.equals("") || password.equals("")){
            Toast.makeText(RegisterActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
            return;
        }
        // 构建注册请求数据
        RegisterRequest registerRequest = new RegisterRequest(name, studentId, password, email, photo);
        AuthService authService = Retrofit.getRetrofitInstance().create(AuthService.class);
        Call<ResponseBody> call = authService.register(registerRequest, code);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 处理登录成功的情况
                    registerOk = true;
                } else {
                    // 处理登录失败的情况
                    Toast.makeText(RegisterActivity.this, "已注册/验证码错误", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理请求失败的情况
                Toast.makeText(RegisterActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择图片");
        builder.setItems(new CharSequence[]{"相册", "相机"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // 选择相册
                        pickImageFromGallery();
                        break;
                    case 1:
                        // 选择相机
                        captureImageFromCamera();
                        break;
                }
            }
        });
        builder.show();
    }

    private void pickImageFromGallery() {
        // 创建Intent，用于选择图片
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // 开启相册应用
        startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST);
    }

    private void captureImageFromCamera() {
        // 创建Intent，用于拍摄图片
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 确保有相机应用能够处理该Intent
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // 开启相机应用
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    // 处理从相册选择的图片和拍照得到的图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // 处理从相册选择的图片
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imageViewSelected.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            // 处理拍照得到的图片
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageViewSelected.setImageBitmap(imageBitmap);
            }
        }
    }
}
