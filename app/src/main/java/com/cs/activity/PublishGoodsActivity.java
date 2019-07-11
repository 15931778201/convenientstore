package com.cs.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.entity.Goods;
import com.cs.entity.UserBean;
import com.cs.server.ServerAddress;
import com.cs.util.GsonUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class PublishGoodsActivity extends Activity implements OnClickListener,OnItemLongClickListener,AdapterView.OnItemSelectedListener{

	private GridView gridView;               	//网格显示缩略图

	private ImageView addPic;					//添加图片按钮

	private TextView selectedNum;			//显示已选择图片数量

	private Button btPublish;					//发布商品

	private ProgressBar mProBar;

	private TextView mLoading;

	private final int IMAGE_OPEN = 1;        //打开图片标记

	private final int CAMERA_TAKE = 2;

	private String pathImage;                	//选择图片路径

	private String pathPhoto;					//拍照图片路径

	private ArrayList<HashMap<String, Object>> imageItem;

	private ArrayList<HashMap<String, Object>> uploadImages;

	private SimpleAdapter simpleAdapter;     //适配器

	private PopupWindow mPopWindow;

	private String name;						// 图片名
	//相册，相册选择框
	private PopupWindow mPopupWindow;

	private View mpopview;
	//屏幕宽高
	private int scWidth,scHeight;
	/*进度条*/
	private ProgressDialog searchDialog;

	private ImageView ivBack;

	private EditText etGoodsTitle,etGoodsBuyInPrice,etGoodsSoldOutPrice,etGoodsStock;

	private Spinner spGoodsType;

	private String sGoodsType;

	private Goods goods;

	private static final String PATH = Environment
			.getExternalStorageDirectory() + "/DCIM"; // 存储路径

	private String type;

	private Goods getGoods;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_publishgoods);
		WindowManager wm = (WindowManager)this.getWindowManager();
		scWidth = wm.getDefaultDisplay().getWidth();
		scHeight = wm.getDefaultDisplay().getHeight();
		try{
			Intent intent = getIntent();
			type = intent.getStringExtra("type");
			getGoods = (Goods) intent.getSerializableExtra("goods");
		}catch (Exception e){
			e.printStackTrace();
		}
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		gridView = (GridView) findViewById(R.id.publishgoods_gridview);
		addPic = (ImageView) findViewById(R.id.iv_publishgoods_addpic);
		selectedNum = (TextView) findViewById(R.id.tv_publishgoods_selected_num);
		btPublish = (Button) findViewById(R.id.bt_publishgoods_upload);

		ivBack = (ImageView) findViewById(R.id.iv_publishgoods_back);
		etGoodsTitle = (EditText) findViewById(R.id.et_publishgoods_title);
		etGoodsBuyInPrice = (EditText) findViewById(R.id.et_publishgoods_buyinprice);
		etGoodsSoldOutPrice = (EditText) findViewById(R.id.et_publishgoods_soldoutprice);
		etGoodsStock = (EditText) findViewById(R.id.et_publishgoods_stock);

		spGoodsType = (Spinner) findViewById(R.id.sp_publishgoods_type);

		spGoodsType.setOnItemSelectedListener(this);


		uploadImages = new ArrayList<HashMap<String,Object>>();
		imageItem = new ArrayList<HashMap<String, Object>>();
		simpleAdapter = new SimpleAdapter(this,
				imageItem, R.layout.griditem_addpic,
				new String[] { "itemImage"}, new int[] { R.id.imageView1});

		gridView.setAdapter(simpleAdapter);
		gridView.setOnItemLongClickListener(this);
		addPic.setOnClickListener(this);
		btPublish.setOnClickListener(this);
		ivBack.setOnClickListener(this);

		if(type != null && type.equals("update")){
			setData();
		}
	}

	private void setData() {
		etGoodsTitle.setText(getGoods.getGoodsTitle());
		etGoodsStock.setText(""+getGoods.getGoodsStock());
		etGoodsSoldOutPrice.setText(getGoods.getGoodsSoldOutPrice());
		etGoodsBuyInPrice.setText(getGoods.getGoodsBuyInPrice());
		sGoodsType = getGoods.getGoodsType();
		setSpinnerDefaultValue(spGoodsType,sGoodsType);

		btPublish.setText("更新");
	}

	//获取图片路径 响应startActivityForResult  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//打开图片  
		if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {
			Uri uri = data.getData();
			if (!TextUtils.isEmpty(uri.getAuthority())) {
				//查询选择图片  
				Cursor cursor = getContentResolver().query(
						uri,
						new String[] { MediaStore.Images.Media.DATA },
						null,
						null,
						null);
				//返回 没找到选择图片  
				if (null == cursor) {
					return;
				}
				//光标移动至开头 获取图片路径  
				cursor.moveToFirst();
				pathImage = cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media.DATA));
			}
		}  //end if 打开图片
	}

	//刷新图片
	@Override
	protected void onResume() {
		super.onResume();

		if(!TextUtils.isEmpty(pathImage)){
			Bitmap addbmp=BitmapFactory.decodeFile(pathImage);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", addbmp);
			Log.i("test", pathImage);
			imageItem.add(map);

			HashMap<String, Object> upload = new HashMap<String, Object>();
			upload.put("uploadImage", pathImage);
			uploadImages.add(upload);

			simpleAdapter = new SimpleAdapter(this,
					imageItem, R.layout.griditem_addpic,
					new String[] { "itemImage"}, new int[] { R.id.imageView1});
			simpleAdapter.setViewBinder(new ViewBinder() {
				@Override
				public boolean setViewValue(View view, Object data,
											String textRepresentation) {
					// TODO Auto-generated method stub  
					if(view instanceof ImageView && data instanceof Bitmap){
						ImageView i = (ImageView)view;
						i.setImageBitmap((Bitmap) data);
						return true;
					}
					return false;
				}
			});
			gridView.setAdapter(simpleAdapter);
			simpleAdapter.notifyDataSetChanged();
			//刷新后释放防止手机休眠后自动添加
			pathImage = null;
			selectedNum.setText("("+String.valueOf(imageItem.size())+")");
		}else if(!TextUtils.isEmpty(pathPhoto)){
			Bitmap addbmp=BitmapFactory.decodeFile(pathPhoto);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", addbmp);
			imageItem.add(map);

			HashMap<String, Object> upload = new HashMap<String, Object>();
			upload.put("uploadImage", pathPhoto);
			uploadImages.add(upload);

			simpleAdapter = new SimpleAdapter(this,
					imageItem, R.layout.griditem_addpic,
					new String[] { "itemImage"}, new int[] { R.id.imageView1});
			simpleAdapter.setViewBinder(new ViewBinder() {
				@Override
				public boolean setViewValue(View view, Object data,
											String textRepresentation) {
					// TODO Auto-generated method stub  
					if(view instanceof ImageView && data instanceof Bitmap){
						ImageView i = (ImageView)view;
						i.setImageBitmap((Bitmap) data);
						return true;
					}
					return false;
				}
			});
			gridView.setAdapter(simpleAdapter);
			simpleAdapter.notifyDataSetChanged();
			//刷新后释放防止手机休眠后自动添加
			pathPhoto = null;
			selectedNum.setText("("+String.valueOf(imageItem.size())+")");
		}
	}

	/*
	 * Dialog对话框提示用户删除操作
	 * position为删除图片位置
	 */
	protected void dialog(final int position) {
		Builder builder = new Builder(PublishGoodsActivity.this);
		builder.setMessage("确认移除已添加图片吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				imageItem.remove(position);
				uploadImages.remove(position);
				simpleAdapter.notifyDataSetChanged();
				selectedNum.setText("("+String.valueOf(imageItem.size())+")");
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
								   long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
			case R.id.publishgoods_gridview:
				dialog(position);
				break;

			default:
				break;
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
			case R.id.iv_publishgoods_addpic:
				showPopWindow();
				break;

			case R.id.iv_publishgoods_back:
				onBackPressed();
				finish();
				break;

			case R.id.bt_publishgoods_upload:
				if(type != null && type.equals("add")){
					if(checkInput()){
						String goodsTitle = etGoodsTitle.getText().toString().trim();
						String goodsStock = etGoodsStock.getText().toString().trim();
						String goodsBuyInPrice = etGoodsBuyInPrice.getText().toString().trim();
						String goodsSoldOutPrice = etGoodsSoldOutPrice.getText().toString().trim();
						goods = new Goods();
						goods.setGoodsPublishUser(LoginActivity.tel);
						goods.setGoodsTitle(goodsTitle);
						goods.setGoodsType(sGoodsType);
						goods.setGoodsBuyInPrice(goodsBuyInPrice);
						goods.setGoodsSoldOutPrice(goodsSoldOutPrice);
						goods.setGoodsState("0");
						goods.setGoodsStock(Integer.valueOf(goodsStock));
						searchDialog = new ProgressDialog(this);
						searchDialog.setMessage("发布中...");
						searchDialog.setCancelable(false);
						searchDialog.show();
						createProgressBar();
						publishgoods(goods);
					}else{
						Toast.makeText(PublishGoodsActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
					}
				}else if(type != null && type.equals("update")){
					if(checkInput()){
						String goodsTitle = etGoodsTitle.getText().toString().trim();
						String goodsStock = etGoodsStock.getText().toString().trim();
						String goodsBuyInPrice = etGoodsBuyInPrice.getText().toString().trim();
						String goodsSoldOutPrice = etGoodsSoldOutPrice.getText().toString().trim();
						goods = new Goods();
						goods.setId(getGoods.getId());
						goods.setGoodsPublishUser(LoginActivity.tel);
						goods.setGoodsTitle(goodsTitle);
						goods.setGoodsType(sGoodsType);
						goods.setGoodsBuyInPrice(goodsBuyInPrice);
						goods.setGoodsSoldOutPrice(goodsSoldOutPrice);
						goods.setGoodsStock(Integer.valueOf(goodsStock));
						searchDialog = new ProgressDialog(this);
						searchDialog.setMessage("更新中...");
						searchDialog.setCancelable(false);
						searchDialog.show();
						createProgressBar();
						updategoods(goods);
					}else{
						Toast.makeText(PublishGoodsActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
					}
				}
				break;


			default:
				break;
		}
	}

	private void showPopWindow(){
		LayoutInflater inflater = LayoutInflater.from(this);
		mpopview = inflater.inflate(R.layout.layout_login_choose_photo,
				null);
		mPopupWindow = new PopupWindow(mpopview, scWidth*9/10, scHeight/4, true);
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tekephoto_dialog_background));

		mPopupWindow.showAtLocation(addPic, Gravity.CENTER, 0,
				0);
		TextView mTextView = (TextView) mpopview
				.findViewById(R.id.textchoice);
		TextView mbuttonTakePhoto = (TextView) mpopview
				.findViewById(R.id.button_take_photo);
		TextView mbuttonChoicePhoto = (TextView) mpopview
				.findViewById(R.id.button_choice_photo);

		// 相册上传
		mbuttonChoicePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(imageItem.size()< 1){
					Intent intent = new Intent(Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, IMAGE_OPEN);
				}else{
					Toast.makeText(PublishGoodsActivity.this, "只允许添加1张", Toast.LENGTH_SHORT).show();
				}
				mPopupWindow.dismiss();
			}
		});

		// 拍照上传
		mbuttonTakePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(imageItem.size()< 1){
					takePhoto();
				}else{
					Toast.makeText(PublishGoodsActivity.this, "只允许添加1张", Toast.LENGTH_SHORT).show();
				}
				mPopupWindow.dismiss();
			}
		});

	}

	/**
	 * 调用系统相机
	 */
	public void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用系统相机
		new DateFormat();
		name = DateFormat.format("yyyyMMdd-hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";

		Uri imageUri = Uri.fromFile(new File(PATH, name));
		pathPhoto = PATH+"/"+name;
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, CAMERA_TAKE);
	}

	public void publishgoods(Goods goods){
		RequestParams params = new RequestParams();
		params.addBodyParameter("goods", GsonUtils.createGsonString(goods));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ ServerAddress.ADD_GOODS_SERVER , params , new RequestCallBack<String>(){

			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(PublishGoodsActivity.this,"发布失败", Toast.LENGTH_SHORT).show();
				searchDialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if(arg0.result.equals("success")){
					searchDialog.dismiss();
					if(uploadImages.size() > 0){
						createProgressBar();
						uploadPics();
					}else{
						Toast.makeText(PublishGoodsActivity.this,"发布成功", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PublishGoodsActivity.this,ShowMsgActivity.class);
						startActivity(intent);
						finish();
					}
				}else{
					Toast.makeText(PublishGoodsActivity.this,"发布失败", Toast.LENGTH_SHORT).show();
					searchDialog.dismiss();
				}
			}

		});
	}

	public void updategoods(Goods goods){
		RequestParams params = new RequestParams();
		params.addBodyParameter("goods", GsonUtils.createGsonString(goods));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ ServerAddress.UPDATE_GOODS , params , new RequestCallBack<String>(){

			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(PublishGoodsActivity.this,"发布失败", Toast.LENGTH_SHORT).show();
				searchDialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if(arg0.result.equals("success")){
					searchDialog.dismiss();
					if(uploadImages.size() > 0){
						createProgressBar();
						updatePics();
					}else{
						Toast.makeText(PublishGoodsActivity.this,"更新成功", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PublishGoodsActivity.this,ShowMsgActivity.class);
						startActivity(intent);
						finish();
					}
				}else{
					Toast.makeText(PublishGoodsActivity.this,"更新失败", Toast.LENGTH_SHORT).show();
					searchDialog.dismiss();
				}
			}

		});
	}

	private void uploadPics(){
		String middleStr,uploadStr;
		String fileName;
		fileName = (String) DateFormat.format("yyyyMMddhhmmss",
				Calendar.getInstance(Locale.CHINA));
		RequestParams params = new RequestParams();
		params.addBodyParameter("fileName",fileName);
		for(int i = 0 ;i < uploadImages.size(); i++){
			middleStr = uploadImages.get(i).toString().substring(13);
			uploadStr = middleStr.substring(0, middleStr.length()-1);
			params.addBodyParameter(String.valueOf(i), new File(uploadStr));
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ServerAddress.ADD_GOODS_IMAGE_SERVER, params , new RequestCallBack<String>(){

			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(PublishGoodsActivity.this,"上传失败", Toast.LENGTH_SHORT).show();
				mProBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				super.onLoading(total, current, isUploading);
				double progess =   (double) current/(double) total;
				int prog  = (int) (progess*100);
				mLoading.setText("已上传"+prog+"%");
				if(prog == 100){
					mLoading.setVisibility(View.GONE);
					mProBar.setVisibility(View.GONE);
					imageItem.clear();
					uploadImages.clear();
					simpleAdapter.notifyDataSetChanged();
					selectedNum.setText("("+String.valueOf(imageItem.size())+")");
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Toast.makeText(PublishGoodsActivity.this,"上传成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent( PublishGoodsActivity.this,ShowMsgActivity.class);
				startActivity(intent);
				finish();
			}

		});
	}

	private void updatePics(){
		String middleStr,uploadStr;
		String fileName;
		fileName = (String) DateFormat.format("yyyyMMddhhmmss",
				Calendar.getInstance(Locale.CHINA));
		RequestParams params = new RequestParams();
		params.addBodyParameter("fileName",fileName);
		params.addBodyParameter("id",String.valueOf(getGoods.getId()));
		for(int i = 0 ;i < uploadImages.size(); i++){
			middleStr = uploadImages.get(i).toString().substring(13);
			uploadStr = middleStr.substring(0, middleStr.length()-1);
			params.addBodyParameter(String.valueOf(i), new File(uploadStr));
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ServerAddress.UPDATE_GOODS_IMAGE, params , new RequestCallBack<String>(){

			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(PublishGoodsActivity.this,"上传失败", Toast.LENGTH_SHORT).show();
				mProBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				super.onLoading(total, current, isUploading);
				double progess =   (double) current/(double) total;
				int prog  = (int) (progess*100);
				mLoading.setText("已上传"+prog+"%");
				if(prog == 100){
					mLoading.setVisibility(View.GONE);
					mProBar.setVisibility(View.GONE);
					imageItem.clear();
					uploadImages.clear();
					simpleAdapter.notifyDataSetChanged();
					selectedNum.setText("("+String.valueOf(imageItem.size())+")");
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Toast.makeText(PublishGoodsActivity.this,"上传成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent( PublishGoodsActivity.this,ShowMsgActivity.class);
				startActivity(intent);
				finish();
			}

		});
	}

	private void createProgressBar() {
		FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
		LayoutParams layoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		mProBar = new ProgressBar(this);
		mProBar.setLayoutParams(layoutParams);
		mProBar.setVisibility(View.VISIBLE);
		mLoading = new TextView(this);
		mLoading.setLayoutParams(layoutParams);
		mLoading.setVisibility(View.VISIBLE);
		layout.addView(mProBar);
		layout.addView(mLoading);

	}

	//用户输入项检查
	public boolean checkInput(){
		String goodsTitle = etGoodsTitle.getText().toString();
		if(goodsTitle.trim().length() == 0){
			etGoodsTitle.setError("商品名称不能为空");
			etGoodsTitle.requestFocus();
			return false;
		}
		String goodsBuyinPrice = etGoodsBuyInPrice.getText().toString();
		if(goodsBuyinPrice.trim().length() == 0){
			etGoodsBuyInPrice.setError("商品进价不能为空");
			etGoodsBuyInPrice.requestFocus();
			return false;
		}
		String goodsSoldOutPrice = etGoodsSoldOutPrice.getText().toString();
		if(goodsSoldOutPrice.trim().length() == 0){
			etGoodsSoldOutPrice.setError("商品售价不能为空");
			etGoodsSoldOutPrice.requestFocus();
			return false;
		}
		String goodsStock = etGoodsStock.getText().toString();
		if(goodsStock.trim().length() == 0){
			etGoodsStock.setError("商品库存不能为空");
			etGoodsStock.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()){
			case R.id.sp_publishgoods_type:
				sGoodsType = (String) spGoodsType.getSelectedItem();
				break;

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	//获得当前时间
	private String getThisMomentDate() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.fff");
		return sdf.format(date);
	}

	private void setSpinnerDefaultValue(Spinner spinner, String value) {
		SpinnerAdapter apsAdapter = spinner.getAdapter();
		int size = apsAdapter.getCount();
		for (int i = 0; i < size; i++) {
			if (TextUtils.equals(value, apsAdapter.getItem(i).toString())) {
				spinner.setSelection(i,true);
				break;
			}
		}
	}
}
