package com.example.myapplication.ui.sell;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.HTTP.gson.ImageListGsonData;
import com.example.myapplication.HTTP.model.GoodsRequest;
import com.example.myapplication.HTTP.service.GoodService;
import com.example.myapplication.HTTP.service.ImageService;
import com.example.myapplication.HTTP.Retrofit;
import com.example.myapplication.ui.data.Token;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.widget.ProgressBar;

public class SellFragment extends Fragment {
    private NavController navController;

    EditText commodityName;
    EditText commodityDescription;
    EditText commodityPrice;
    ImageView[] itemPic = new ImageView[3];
    int choose;
    Button buttonCommit;

    private File currentImageFile;
    List<MultipartBody.Part> files = new ArrayList<>();
    String[] photos;
    AlertDialog progressDialog;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;

    private static int fileNum = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_sell, container, false);

        navController = NavHostFragment.findNavController(this);

        commodityName = v.findViewById(R.id.commodityName);
        commodityDescription = v.findViewById(R.id.description);
        commodityPrice = v.findViewById(R.id.price);

        itemPic[0] = v.findViewById(R.id.AddItemPic);
        itemPic[1] = v.findViewById(R.id.AddItemPic2);
        itemPic[2] = v.findViewById(R.id.AddItemPic3);

        // 初始化相机权限请求
        requestCameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 相机权限已经被授予，可以执行相机操作
                showImagePickerDialog();
            } else {
                // 用户拒绝了相机权限，可以显示一个提示或执行其他适当的操作
                Toast.makeText(getContext(),"无法打开相机",Toast.LENGTH_SHORT).show();
            }
        });

        itemPic[0].setOnClickListener(view -> {
            choose = 0;
            checkCamera();
        });

        itemPic[1].setOnClickListener(view -> {
            choose = 1;
            checkCamera();
        });

        itemPic[2].setOnClickListener(view -> {
            choose = 2;
            checkCamera();
        });

        buttonCommit = v.findViewById(R.id.buttonCommit);
        buttonCommit.setOnClickListener(w-> getImageUrl());


        return v;
    }

    // 拿到图片的url之后发送所有信息
    private void commitGoods() {
        String description = commodityDescription.getText().toString();
        String name = commodityName.getText().toString();
        String priceString = commodityPrice.getText().toString();
        double price = Double.parseDouble(priceString);

        GoodService goodService = Retrofit.getRetrofitInstance().create(GoodService.class);

        // 输出发送的图片 URL
        Log.d("CommitGoods", "name: " + name + " description: " + description + " price: " + price);
        // 使用图片URL数组构建 GoodsRequest 对象
        GoodsRequest goodsRequest = new GoodsRequest(photos, name, description, price);



        Call<ResponseBody> call = goodService.postGoods(goodsRequest, Token.getInstance().getToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                // 关闭AlertDialog
                progressDialog.dismiss();

                if(response.isSuccessful()){
                    Toast.makeText(getActivity(),"提交成功",Toast.LENGTH_SHORT).show();
                    // 获取 NavController
//                    NavController navController = NavHostFragment.findNavController(this);

                    // 使用 NavController 切换到 HomeFragment
                    navController.navigate(R.id.navigation_home);
                } else {
                    Toast.makeText(getActivity(),"提交失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                // 输出请求失败的原因

                // 关闭AlertDialog
                progressDialog.dismiss();

                Log.e("CommitGoods", "Request failed: " + t.getMessage());
                Toast.makeText(getActivity(),"请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }


    //拿到图片url
    private void getImageUrl(){

        // 创建并显示带有进度条的AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提交中");
        builder.setMessage("请稍候...");
        builder.setCancelable(false);

        // 添加进度条
        ProgressBar progressBar = new ProgressBar(getActivity());
        builder.setView(progressBar);

        progressDialog = builder.create();
        progressDialog.show();

        // 使用Retrofit接口上传文件
        Log.d("fileNum",String.valueOf(files.size()));
        ImageService apiService = Retrofit.getRetrofitInstance().create(ImageService.class);
        Call<ResponseBody> call = apiService.uploadImage(files,Token.getInstance().getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    handleImageUploadResponse(response.body());
//                    Toast.makeText(getActivity(), "图片请求成功", Toast.LENGTH_SHORT).show();
                    commitGoods();
                }
                else{
                    Toast.makeText(getActivity(), "图片过大", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "连接后端失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //将图片url保存到成员变量photos
    private void handleImageUploadResponse(ResponseBody responseBody) {
        try {
            String responseString = responseBody.string();

            Log.d("responseString",responseString);
            // 使用 Gson 将 JSON 字符串解析为字符串数组
            Gson gson = new Gson();
            ImageListGsonData imageListGsonData = gson.fromJson(responseString, ImageListGsonData.class);
            photos = imageListGsonData.getData();
            Log.d("photoUrlFromBack",responseString);

            // 在这里执行你的其他逻辑
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 检查相机权限
    private void checkCamera(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 如果权限没有被授予，请求权限
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        } else {
            // 相机权限已经被授予，可以执行相机操作
            showImagePickerDialog();
        }
    }

    //选择相册还是相机
    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("选择图片");
        builder.setItems(new CharSequence[]{"相册", "相机"}, (dialog, which) -> {
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
        });
        builder.show();
    }

    //选择图片的
    private void pickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择图片"), 1);
    }

    //选择相机的
    private void captureImageFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 创建一个临时文件保存拍摄的图片
        try {
            currentImageFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 将文件URI传递给相机应用
        Uri photoURI = FileProvider.getUriForFile(requireContext(), "com.example.myapplication.fileprovider", currentImageFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cameraIntent, 2);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            // 保存选择的图片为文件
            try {
                currentImageFile = saveBitmapToFile(selectedImageUri);
                // 将文件添加到 files 列表
                files.add(prepareFilePart("image"+fileNum,currentImageFile));
                fileNum++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // 更新ImageView
            itemPic[choose].setImageBitmap(BitmapFactory.decodeFile(currentImageFile.getAbsolutePath()));
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            // 使用拍摄的照片
            if (currentImageFile != null && currentImageFile.exists()) {
                // 将文件添加到 files 列表
                files.add(prepareFilePart("image"+fileNum,currentImageFile));
                fileNum++;
                // 更新ImageView
                itemPic[choose].setImageBitmap(BitmapFactory.decodeFile(currentImageFile.getAbsolutePath()));
            }
        }
    }

    private File createImageFile() throws IOException {
        // 创建一个唯一的文件名
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // 获取应用的缓存目录
        File storageDir = requireContext().getExternalCacheDir();

        // 创建临时文件
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private File saveBitmapToFile(Uri selectedImageUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);

        // 创建一个唯一的文件名
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // 获取应用的缓存目录
        File storageDir = requireContext().getExternalCacheDir();

        // 创建临时文件
        File tempFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        // 将Bitmap保存到文件
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        }

        return tempFile;
    }

    // 在 SellFragment 类中添加准备 MultipartBody.Part 的方法
    private MultipartBody.Part prepareFilePart(String partName, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

}
