package com.zxy.wtlauncher.widget;

import com.zxy.wtlauncher.util.Util;
import com.zxy.wtlauncher.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 
* @ClassName: ReflectItemView 
* @author zhaoyufei
* @date 2016-7-1
 */
public class ReflectItemView extends FrameLayout {

	private static final String TAG = "ReflectItemView";
	private static final int DEFUALT_REFHEIGHT = 80;
	private static final int DEFUALT_RADIUS = 12;
	private Paint mClearPaint = null;
	private Paint mShapePaint = null;
	private Paint mRefPaint = null;
	private int mRefHeight = DEFUALT_REFHEIGHT;
	private boolean mIsDrawShape = false;
	private boolean mIsReflection = false;

	private float mRadius = DEFUALT_RADIUS;
	private RadiusRect mRadiusRect = new RadiusRect(mRadius, mRadius, mRadius,
			mRadius);

	private BitmapMemoryCache mBitmapMemoryCache = BitmapMemoryCache
			.getInstance();
	private static int sViewIDNum = 0;
	private int viewIDNum = 0;

	public ReflectItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public ReflectItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public ReflectItemView(Context context) {
		super(context);
		init(context, null);
	}

	private void init(Context context, AttributeSet attrs) {
		setClipChildren(false);
		setClipToPadding(false);
		setWillNotDraw(false);
		// 鍒濆鍖栧睘锟�??.
		if (attrs != null) {
			TypedArray tArray = context.obtainStyledAttributes(attrs,
					R.styleable.reflectItemView);// 鑾峰彇閰嶇疆灞烇拷??
			mIsReflection = tArray.getBoolean(
					R.styleable.reflectItemView_isReflect, false);
			mRefHeight = (int) tArray.getDimension(
					R.styleable.reflectItemView_reflect_height,
					DEFUALT_REFHEIGHT);
			mIsDrawShape = tArray.getBoolean(
					R.styleable.reflectItemView_isShape, false);
			mRadius = tArray.getDimension(R.styleable.reflectItemView_radius,
					DEFUALT_RADIUS);
			setRadius(mRadius);
		}
		initShapePaint();
		initRefPaint();
	}

	private void initShapePaint() {
		mShapePaint = new Paint();
		mShapePaint.setAntiAlias(true);
		mShapePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
	}

	private void initRefPaint() {
		if (mRefPaint == null) {
			mRefPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mRefPaint
					.setShader(new LinearGradient(0, 0, 0, mRefHeight,
							new int[] { 0x77000000, 0x66AAAAAA, 0x0500000,
									0x00000000 }, new float[] { 0.0f, 0.1f,
									0.9f, 1.0f }, Shader.TileMode.CLAMP));
			mRefPaint.setXfermode(new PorterDuffXfermode(
					PorterDuff.Mode.MULTIPLY));
		}
		if (mClearPaint == null) {
			mClearPaint = new Paint();
			mClearPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		}
	}

	public void setReflection(boolean ref) {
		mIsReflection = ref;
		invalidate();
	}

	public boolean isReflection() {
		return this.mIsReflection;
	}

	public void setDrawShape(boolean isDrawShape) {
		mIsDrawShape = isDrawShape;
		invalidate();
	}

	public boolean isDrawShape() {
		return this.mIsDrawShape;
	}

	public void setRefHeight(int height) {
		this.mRefHeight = height;
	}

	public int getRefHeight() {
		return this.mRefHeight;
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	private boolean isDrawShapeRadiusRect(RadiusRect radiusRect) {
		if (radiusRect.bottomLeftRadius <= 0
				&& radiusRect.bottomRightRadius <= 0
				&& radiusRect.topLeftRadius <= 0
				&& radiusRect.topRightRadius <= 0)
			return false;
		return true;
	}

	private int getViewCacheID() {
		if (viewIDNum == 0) {
			sViewIDNum++;
			viewIDNum = sViewIDNum;
		}
		return viewIDNum;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (viewIDNum != 0) {
			mBitmapMemoryCache.removeImageCache(viewIDNum + "");
		}
	}

	public Path getShapePath(int width, int height, float radius) {
		return DrawUtils.addRoundPath3(getWidth(), getHeight(), radius);
	}

	@Override
	public void draw(Canvas canvas) {
		try {
			if (canvas != null) {
				if (mIsDrawShape && isDrawShapeRadiusRect(mRadiusRect)) {
					drawShapePathCanvas(canvas);
				} else {
					super.draw(canvas);
				}
				if (Util.getSDKVersion() == 18) {
					drawRefleCanvas4_3_18(canvas);
				} else if (Util.getSDKVersion() == 17) {
					// 4.2 涓嶉渶瑕侊拷?锟藉奖锛岀粯鍒舵湁闂锛屾殏鏃跺睆锟�??.
					drawRefleCanvas(canvas);
				} else { // 鎬ц兘楂橈拷??-鍊掑奖(4.3鏈夐棶锟�??).
					drawRefleCanvas(canvas);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void drawShapePathCanvas(Canvas shapeCanvas) {
		if (shapeCanvas != null) {
			int width = getWidth();
			int height = getHeight();
			int count = shapeCanvas.save();
			int count2 = shapeCanvas.saveLayer(0, 0, width, height, null,
					Canvas.ALL_SAVE_FLAG);
			//
			Path path = getShapePath(width, height, mRadius);
			super.draw(shapeCanvas);
			shapeCanvas.drawPath(path, mShapePaint);
			//
			shapeCanvas.restoreToCount(count2);
			shapeCanvas.restoreToCount(count);
		}
	}

	private void drawRefleCanvas(Canvas refleCanvas) {
		if (mIsReflection) {
			refleCanvas.save();
			int dy = getHeight();
			int dx = 0;
			refleCanvas.translate(dx, dy);
			drawReflection(refleCanvas);
			refleCanvas.restore();
		}
	}

	private void drawRefleCanvas4_3_18(Canvas canvas) {
		if (mIsReflection) {
			String cacheID = getViewCacheID() + "";
			//
			Bitmap reflectBitmap = mBitmapMemoryCache
					.getBitmapFromMemCache(cacheID);
			if (reflectBitmap == null) {
				reflectBitmap = Bitmap.createBitmap(getWidth(), mRefHeight,
						Bitmap.Config.ARGB_8888);
				mBitmapMemoryCache.addBitmapToMemoryCache(cacheID,
						reflectBitmap);
			}
			Canvas reflectCanvas = new Canvas(reflectBitmap);
			reflectCanvas.drawPaint(mClearPaint); // 娓呯┖鐢诲竷.
			int width = reflectCanvas.getWidth();
			int height = reflectCanvas.getHeight();
			RectF outerRect = new RectF(0, 0, width, height);
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			if (mIsDrawShape) {
				reflectCanvas.drawPath(
						getShapePath(width, height + 50, mRadius), paint);
				paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			}
			reflectCanvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
			drawReflection4_3_18(reflectCanvas);
			reflectCanvas.restore();
			canvas.save();
			int dy = getHeight();
			int dx = 0;
			canvas.translate(dx, dy);
			canvas.drawBitmap(reflectBitmap, 0, 0, null);
			canvas.restore();
		}
	}

	public void drawReflection4_3_18(Canvas canvas) {
		canvas.save();
		canvas.clipRect(0, 0, getWidth(), mRefHeight);
		canvas.save();
		canvas.scale(1, -1);
		canvas.translate(0, -getHeight());
		super.draw(canvas);
		canvas.restore();
		canvas.drawRect(0, 0, getWidth(), mRefHeight, mRefPaint);
		canvas.restore();
	}

	public void drawReflection(Canvas reflectionCanvas) {
		int width = getWidth();
		int height = getHeight();
		int count = reflectionCanvas.save();
		int count2 = reflectionCanvas.saveLayer(0, 0, width, mRefHeight, null,
				Canvas.ALL_SAVE_FLAG);
		//
		reflectionCanvas.save();
		reflectionCanvas.clipRect(0, 0, getWidth(), mRefHeight);
		reflectionCanvas.save();
		reflectionCanvas.scale(1, -1);
		reflectionCanvas.translate(0, -getHeight());
		super.draw(reflectionCanvas);
		if (mIsDrawShape) {
			Path path = getShapePath(width, height, mRadius);
			reflectionCanvas.drawPath(path, mShapePaint);
		}
		reflectionCanvas.restore();
		reflectionCanvas.drawRect(0, 0, getWidth(), mRefHeight, mRefPaint);
		reflectionCanvas.restore();
		//
		reflectionCanvas.restoreToCount(count2);
		reflectionCanvas.restoreToCount(count);
	}


	public void setRadius(float radius) {
		setRadius(radius, radius, radius, radius);
	}

	public void setRadius(float tlRadius, float trRaius, float blRadius,
			float brRaius) {
		setRadiusRect(new RadiusRect(tlRadius, trRaius, blRadius, brRaius));
	}

	public void setRadiusRect(RadiusRect rect) {
		mRadiusRect = rect;
		invalidate();
	}

	public RadiusRect getRadiusRect() {
		return this.mRadiusRect;
	}

	public class RadiusRect {
		public float topLeftRadius;
		public float topRightRadius;
		public float bottomLeftRadius;
		public float bottomRightRadius;

		public RadiusRect() {
		}

		public RadiusRect(float tlRadius, float trRaius, float blRadius,
				float brRaius) {
			topLeftRadius = tlRadius;
			topRightRadius = trRaius;
			bottomLeftRadius = blRadius;
			bottomRightRadius = brRaius;
		}

		public void set(float tlRadius, float trRaius, float blRadius,
				float brRaius) {
			topLeftRadius = tlRadius;
			topRightRadius = trRaius;
			bottomLeftRadius = blRadius;
			bottomRightRadius = brRaius;
		}

		public RadiusRect get() {
			return new RadiusRect(topLeftRadius, topRightRadius,
					bottomLeftRadius, bottomRightRadius);
		}
	}

}
