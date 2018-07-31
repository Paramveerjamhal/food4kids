package com.example.paramveerjamhal.food4kids.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.example.paramveerjamhal.food4kids.R;

/**
 * @author AppStudioz Custom image view class use to display image in circular
 *         form with whight border
 */
public class CircularImageView extends android.support.v7.widget.AppCompatImageView
{
	private int borderWidth;
	private int canvasSize;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;

	public CircularImageView(final Context context)
	{
		this(context, null);
	}

	public CircularImageView(Context context, AttributeSet attrs)
	{
		this(context, attrs, R.attr.circularImageViewStyle);
	}

	@SuppressLint("Recycle")
	public CircularImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		// init paint
		paint = new Paint();
		paint.setAntiAlias(true);

		paintBorder = new Paint();
		paintBorder.setAntiAlias(true);

		// load the styled attributes and set their properties
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView, defStyle, 0);

		if (attributes.getBoolean(R.styleable.CircularImageView_border, true))
		{
			/*int defaultBorderSize = (int) (2 * getContext().getResources().getDisplayMetrics().density + 0.5f);
			setBorderWidth(attributes.getDimensionPixelOffset(R.styleable.CircularImageView_border_width, defaultBorderSize));
			setBorderColor(attributes.getColor(R.styleable.CircularImageView_border_color, Color.DKGRAY));*/
		}

		/*if (attributes.getBoolean(R.styleable.CircularImageView_shadow, false))
			addShadow();*/
	}

	public void setBorderWidth(int borderWidth)
	{
		this.borderWidth = borderWidth;
		this.requestLayout();
		this.invalidate();
	}

	public void setBorderColor(int borderColor)
	{
		if (paintBorder != null)
			paintBorder.setColor(borderColor);
		this.invalidate();
	}

	@SuppressLint("NewApi")
	public void addShadow()
	{
		setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
		paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas)
	{
		// load the bitmap
		image = drawableToBitmap(getDrawable());

		// init shader
		if (image != null)
		{

			canvasSize = canvas.getWidth();
			if (canvas.getHeight() < canvasSize)
				canvasSize = canvas.getHeight();

			BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(image, canvasSize, canvasSize, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			paint.setShader(shader);

			// circleCenter is the x or y of the view's center
			// radius is the radius in pixels of the cirle to be drawn
			// paint contains the shader that will texture the shape
			int circleCenter = (canvasSize - (borderWidth * 2)) / 2;
			canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, ((canvasSize - (borderWidth * 2)) / 2) + borderWidth - 4.0f, paintBorder);
			canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, ((canvasSize - (borderWidth * 2)) / 2) - 4.0f, paint);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int size = Math.min(widthSize, heightSize);
		setMeasuredDimension(size, size);
	}

	public Bitmap drawableToBitmap(Drawable drawable)
	{
		if (drawable == null)
		{
			return null;
		} else if (drawable instanceof BitmapDrawable)
		{
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}
}
