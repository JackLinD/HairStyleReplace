package com.example.hairstylereplace;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {
	private Button btn1, btn2;
	private ImageView img;
	private static int RESULT_LOAD_IMAGE = 1;
	private Bitmap mBitmap, nBitmap, kBitmap, resizeBmp, replaceBmp;
	private double i, j, l,ll,scale,ii,jj,scaleI,scaleJ;
	private int bmpWidth, bmpHeight;
	DisplayMetrics dm = new DisplayMetrics();
	int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn1 = (Button) this.findViewById(R.id.button1);
		btn2 = (Button) this.findViewById(R.id.button2);
		img = (ImageView) this.findViewById(R.id.imageView1);
		img.setVisibility(0x00000004);
		btn2.setClickable(false);

		dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;

		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// kBitmap = BitmapFactory.decodeResource(getResources(),
				// R.drawable.b).copy(Bitmap.Config.ARGB_8888, true);
				// 加载图片
				mBitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.a).copy(Bitmap.Config.ARGB_8888, true);
				float a = (float) mBitmap.getWidth()
						/ (float) mBitmap.getHeight();
				nBitmap = Bitmap.createScaledBitmap(mBitmap, screenWidth / 2,
						(int) ((float) screenWidth / 2 / a), true);
				img.setVisibility(0x00000000);
				img.setImageBitmap(nBitmap);
//				 Intent i = new Intent(
//				 Intent.ACTION_PICK,
//				 android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//				 startActivityForResult(i, RESULT_LOAD_IMAGE);
				btn2.setClickable(true);
			}
		});

		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//发型默认眼间距离
				replaceBmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.gg).copy(Bitmap.Config.ARGB_8888, true);
				FaceDetector.Face[] mFacesI = new FaceDetector.Face[1];
				Bitmap tmpBmpI = replaceBmp.copy(Bitmap.Config.RGB_565, true);
				FaceDetector detectorI = new FaceDetector(tmpBmpI.getWidth(),
						tmpBmpI.getHeight(), mFacesI.length);
				int mNumFacesI = detectorI.findFaces(tmpBmpI, mFacesI);
				for (int k = 0; k < mNumFacesI; k++) {
					PointF midPointI = new PointF();
					mFacesI[k].getMidPoint(midPointI);
					ii = midPointI.x;
					scaleI=ii/(double)tmpBmpI.getWidth();
					jj = midPointI.y;
					scaleJ=jj/(double)tmpBmpI.getHeight();
					ll = mFacesI[k].eyesDistance();
					System.out.println(ll);
				}
				
				// 脸部位置识别
				FaceDetector.Face[] mFaces = new FaceDetector.Face[1];
				Bitmap tmpBmp = nBitmap.copy(Bitmap.Config.RGB_565, true);
				FaceDetector detector = new FaceDetector(tmpBmp.getWidth(),
						tmpBmp.getHeight(), mFaces.length);
				int mNumFaces = detector.findFaces(tmpBmp, mFaces);
				for (int k = 0; k < mNumFaces; k++) {
					PointF midPoint = new PointF();
					mFaces[k].getMidPoint(midPoint);
					i = midPoint.x;
					j = midPoint.y;
					l = mFaces[k].eyesDistance();
					System.out.println(l);
					System.out.println(i);
					System.out.println(j);
				}

				// 发型调整
				kBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ii)
						.copy(Bitmap.Config.ARGB_8888, true);
				bmpWidth = kBitmap.getWidth();
				bmpHeight = kBitmap.getHeight();
				scale = l / ll;
				System.out.println(scale);
				resizeBmp = Bitmap.createScaledBitmap(kBitmap,
						(int) (scale * bmpWidth), (int) (scale * bmpHeight),
						true);
				// float scaleWidth=1;
				// float scaleHeight=1;
				// scaleWidth=(float) (scaleWidth*scale);
				// scaleHeight=(float) (scaleHeight*scale);
				// Matrix matrix = new Matrix();
				// matrix.postScale(scaleWidth, scaleHeight);
				// System.out.println(bmpWidth);
				// resizeBmp =
				// Bitmap.createBitmap(kBitmap,0,0,bmpWidth,bmpHeight,matrix,true);
				// resizeBmp=Bitmap.createScaledBitmap(kBitmap,screenWidth/2,
				// (int) ((float)screenWidth/2/b), true);
				
				// 发型绘制
				Canvas c = new Canvas(nBitmap);
				Paint p = new Paint();
				c.drawBitmap(resizeBmp, (int) (i - resizeBmp.getWidth()*scaleI),
						(int) (j - resizeBmp.getHeight()*scaleJ), p);
				
				// 图片融合

				img.setImageBitmap(nBitmap);
				btn2.setClickable(false);
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			// Bitmap mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth,
			// mScreenHeight, true);
			mBitmap = BitmapFactory.decodeFile(picturePath).copy(
					Bitmap.Config.ARGB_8888, true);
			// mBitmap = BitmapFactory.decodeResource(getResources(),
			// R.drawable.a).copy(Bitmap.Config.ARGB_8888, true);
			float a = (float) mBitmap.getWidth() / (float) mBitmap.getHeight();
			nBitmap = Bitmap.createScaledBitmap(mBitmap, screenWidth / 2,
					(int) ((float) screenWidth / 2 / a), true);
			img.setVisibility(0x00000000);
			img.setImageBitmap(nBitmap);
			// img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
