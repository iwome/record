package com.vcredit.linechartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhaoguangyou on 2015/6/25.
 */
public class LineChartView extends View {

    private Context context;
    public int ViewWidth = 0;
    public int ViewHeight = 0;

    int xAxiesPadding = 88; //x轴坐标paddingright
    int yAxiesPadding = 130;//y轴坐标的paddingtop
    int xTextPadding = 20;

    public int XPoint = 0; // 原点的X坐标
    public int YPoint = 0; // 原点的Y坐标
    public int XScale = 0; // X的刻度长度
    public int YScale = 0; // Y的刻度长度
    public int XLength = 0; // X轴的长度
    public int YLength = 0; // Y轴的长度

    public int textInfoSize = 0;// 要显示弹出框字allData体长度
    public int FrameHigh = 0;// 显示字体矩形的高
    public int FrameWight = 0;// 显示字体矩形的高

    public int DirectXScale = 0;// 要显示提示信息弹出框点的X坐标
    public float DirectYScale = 0;// 要显示提示信息弹出框点的Y坐标
    public int DirectDataIndex = 0;// 要显示提示信息弹出框的点

    public String[] XLabel = new String[20];// X的刻度
    public int[] YLabel = new int[20];// Y的刻度
    public String[] allData = new String[20];// 数据
    public int dataLength = 0;// 所传数据的个数
    // public String Title = ""; // 显示的标题
    public String contextShow = "";// 节点显示的内容

    private int touchDownX = 0;// 触摸时刚刚点击点的横坐标值
    private int touchTempX = 0;// 手指滑动过程中横坐标值
    private int touchTotalMoveX = 0;// 最后手指离开屏幕与开始点击时的横坐标差值
    private int dotColor;// 虚线的颜色

    private boolean isVoid = false;//如果没有数据不绘制曲线

    private float down_x = 0;
    private float down_y = 0;

    public LineChartView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public LineChartView(Context ct, AttributeSet attrs) {
        super(ct, attrs);
        this.context = ct;
        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.DottedLine);
        dotColor = attributes.getColor(R.styleable.DottedLine_dotColor,
                getResources().getColor(R.color.dot_line));
    }

    public LineChartView(Context ct, AttributeSet attrs, int defStyle) {
        super(ct, attrs, defStyle);
        this.context = ct;
        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.DottedLine);
        dotColor = attributes.getColor(R.styleable.DottedLine_dotColor,
                getResources().getColor(R.color.dot_line));
    }

    public void SetInfo(String[] XLabels, int[] YLabels, String[] AllData,
                        int indexData, boolean isVoid) {
        this.XLabel = XLabels;
        this.YLabel = YLabels;
        this.allData = AllData;
        this.contextShow = AllData[indexData];
        this.textInfoSize = AllData[indexData].length();
        this.DirectDataIndex = indexData;
        if (allData != null)
            dataLength = allData.length;
        this.isVoid = isVoid;
        initData();
    }

    private void initData() {

        XPoint = ViewWidth / 10;
        // 原点的X坐标
        YPoint = ViewHeight * 5 / 6;

        XLength = ViewWidth * 9 / 10; // X轴的长度
        YLength = ViewHeight * 5 / 6; // Y轴的长度


        // Y的刻度长度
        YScale = (YLength - CommonUtils.Px2Dp(context, yAxiesPadding)) / (YLabel.length - 1);
        XScale = (XLength - CommonUtils.Px2Dp(context, xAxiesPadding)) / (XLabel.length - 1);

        XLength = XLength - CommonUtils.Px2Dp(context, xAxiesPadding) * 2 / 3;
        YLength = YLength - CommonUtils.Px2Dp(context, yAxiesPadding) * 2 / 3;

        FrameHigh = ViewHeight / 10;// CommonUtils.Px2Dp(context, ViewHeight/4);// 显示字体矩形的高
        FrameWight = ViewWidth / 10;// CommonUtils.Px2Dp(context, ViewWidth/4);// 显示字体矩形的宽
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        ViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        initData();
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);

    }

    class XYCoord {
        float x = 0;
        float y = 0;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

    }

    private List<XYCoord> listXYCord = new ArrayList<XYCoord>();

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);// 重写onDraw方法

        // canvas.drawColor(Color.WHITE);//设置背景颜色
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);// 去锯齿
        paint.setStrokeWidth(CommonUtils.Dp2Px(context, 1));
        paint.setColor(getResources().getColor(R.color.chart_dot_line));// 颜色
//        paint.setColor(getResources().getColor(R.color.light_gray_1));// 颜色
        paint.setAlpha(255);

        Paint paintText = new Paint();
        paintText.setStyle(Paint.Style.STROKE);
        paintText.setAntiAlias(true);// 去锯齿
        paintText.setColor(getResources().getColor(R.color.font_light_gray2));// 颜色
//        paintText.setColor(getResources().getColor(R.color.light_gray_1));// 颜色
        paintText.setAlpha(255);
        paintText.setTextAlign(Paint.Align.RIGHT);//文本从右边绘制
        paintText.setTextSize(getResources().getDimension(R.dimen.fontSize_10));

        // Point[] points = new Point[allData.length];
        if (!listXYCord.isEmpty())
            listXYCord.clear();
        //记录数据坐标点
        for (int i = 0; i < allData.length; i++) {
            // points[i] = new Point(XPoint + (i) * XScale, YCoord(allData[i]));
            XYCoord temp = new XYCoord();
            temp.setX(XPoint + (i) * XScale);
            temp.setY(YCoord(allData[i]));
            listXYCord.add(temp);
        }
        // // 画曲线及阴影
        // for (int i = 0; i < XLabel.length; i++) {
        // try {
        // if (i != 0)
        // drawCurveLine(paint, canvas, i, points);
        // } catch (Exception e) {
        // }
        // }

        // 设置Y轴
        canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint); // 轴线

        for (int i = 0; i < YLabel.length; i++) {
            // canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint -
            // i
            // * YScale, paint); // 刻度
            try {
                if (i != 0)
                    drawHorizontalDottedLine(YPoint - i * YScale, canvas);
                paint.setColor(getResources().getColor(R.color.font_light_gray2));// 颜色

                canvas.drawText(YLabel[i] + "",
                        XPoint - CommonUtils.Px2Dp(context, xTextPadding), YPoint - i
                                * YScale + 5, paintText); // 文字
                paint.setColor(getResources().getColor(R.color.light_blue_1));// 颜色

            } catch (Exception e) {
            }
        }
        // canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint -
        // YLength
        // + 6, paint); // 箭头
        // canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint -
        // YLength
        // + 6, paint);

        paint.setColor(getResources().getColor(R.color.chart_dot_line));// 颜色

        // 设置X轴
        canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint); // 轴线
        for (int i = 0; i < XLabel.length; i++) {
            // canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale,
            // YPoint - 5, paint); // 刻度
            if (i != 0)
                drawVerticalDottedLine(XPoint + i * XScale, canvas);

            try {
//                paint.setColor(getResources().getColor(R.color.light_gray_1));// 颜色
                paint.setColor(getResources().getColor(R.color.font_light_gray2));// 颜色
                paint.setAntiAlias(true);// 去锯齿
                paint.setAlpha(168);
                canvas.drawText(XLabel[i], XPoint + i * XScale + CommonUtils.Dp2Px(context, 13), YPoint
                        + CommonUtils.Dp2Px(context, 20), paintText); // 文字
                paint.setColor(getResources().getColor(R.color.light_blue_1));// 颜色

                if (i != 0 && !isVoid) {
                    drawCurveLine(paint, canvas, i, listXYCord);
                }
                // drawDottedAll(paint, canvas, i);
            } catch (Exception e) {
            }
        }

        // 画坐标点
        if (!isVoid) {

            for (int i = 0; i < XLabel.length; i++) {
                try {
                    drawDottedAll(paint, canvas, i);
                } catch (Exception e) {
                }
            }
        }
        // canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
        // YPoint - 3, paint); // 箭头
        // canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
        // YPoint + 3, paint);

        DirectXScale = XPoint + DirectDataIndex * XScale;
        DirectYScale = YCoord(allData[DirectDataIndex])
                - CommonUtils.Px2Dp(context, 24);

        // 绘制信息提示框
        if (!isVoid) {
            drawInfoShow(paint, canvas);

        }
        DirectYScale = YCoord(allData[DirectDataIndex]);

        // 绘制选中坐标点的圆点
        if (!isVoid) {

            drawDottedSelect(paint, canvas);
        }
    }

    // 绘制竖直虚线
    private void drawVerticalDottedLine(int xPoint, Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(dotColor);
        paint.setStrokeWidth(CommonUtils.Dp2Px(context, 1));
        Path path = new Path();
        path.moveTo(xPoint, YPoint - YLength);
        path.lineTo(xPoint, YPoint);
        PathEffect effects = new DashPathEffect(new float[]{2, 2, 2, 2}, 1);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }

    // 绘制水平虚线
    private void drawHorizontalDottedLine(int yPoint, Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(dotColor);
        paint.setStrokeWidth(CommonUtils.Dp2Px(context, 1));
        Path path = new Path();
        path.moveTo(XPoint, yPoint);
        path.lineTo(XPoint + XLength, yPoint);
        PathEffect effects = new DashPathEffect(new float[]{2, 2, 2, 2}, 1);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }

    // 绘制信息提示框
    private void drawInfoShow(Paint paint, Canvas canvas) {
        FrameWight = ViewHeight / 10 + CommonUtils.Dp2Px(context, 4) * textInfoSize;

        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.light_blue_1));
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL);

        RectF oval3 = new RectF(DirectXScale - FrameWight / 2,
                DirectYScale - 10 - FrameHigh,
                DirectXScale + FrameWight / 2,
                DirectYScale - 10);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 15, 15, paint);// 第二个参数是x半径，第三个参数是y半径

        Path path2 = new Path();
        path2.moveTo(DirectXScale - CommonUtils.Px2Dp(context, 8),
                DirectYScale - 10);

        path2.lineTo(DirectXScale, DirectYScale);

        path2.lineTo(DirectXScale + CommonUtils.Px2Dp(context, 8),
                DirectYScale - 10);
        canvas.drawPath(path2, paint);

        Paint paint2 = new Paint();
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setAntiAlias(true);// 去锯齿
        paint2.setColor(getResources().getColor(R.color.font_white));// 颜色
        paint2.setAlpha(168);
        paint2.setTextSize(getResources().getDimension(R.dimen.fontSize_10));
//        canvas.drawText(contextShow,
//                DirectXScale - CommonUtils.Px2Dp(context, textInfoSize * 20),
//                DirectYScale - FrameHigh * 2 / 5, paint2);
        int tempAdd = 0;
        if (textInfoSize > 5) {
            tempAdd = textInfoSize + CommonUtils.Dp2Px(context, 5);
        } else {
            tempAdd = textInfoSize + CommonUtils.Dp2Px(context, 2);
        }
        canvas.drawText(contextShow,
                DirectXScale - FrameWight / 5 - tempAdd,
                DirectYScale - FrameHigh / 2 - 1, paint2);
    }

    // 绘制曲线
    private void drawCurveLine(Paint paint, Canvas canvas, int index,
                               List<XYCoord> listXYCord) {

        float wt = (listXYCord.get(index - 1).getX() + listXYCord.get(index)
                .getX()) / 2;
        XYCoord p3 = new XYCoord();
        XYCoord p4 = new XYCoord();
        p3.setY(listXYCord.get(index - 1).getY());
        p3.setX(wt);
        p4.setY(listXYCord.get(index).getY());
        p4.setX(wt);


        Path path2 = new Path();
        path2.moveTo(listXYCord.get(index - 1).getX(), listXYCord.get(index - 1)
                .getY());
        path2.cubicTo(p3.getX(), p3.getY(), p4.getX(), p4.getY(), listXYCord
                .get(index).getX(), listXYCord.get(index).getY());

        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(125);
        path2.lineTo(listXYCord.get(index).getX(), YPoint);
        path2.lineTo(listXYCord.get(index - 1).getX(), YPoint);
        canvas.drawPath(path2, paint);


        Path path1 = new Path();
        path1.moveTo(listXYCord.get(index - 1).getX(), listXYCord.get(index - 1)
                .getY());
        paint.setStrokeWidth(CommonUtils.Px2Dp(context, 8));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(255);
        path1.cubicTo(p3.getX(), p3.getY(), p4.getX(), p4.getY(), listXYCord
                .get(index).getX(), listXYCord.get(index).getY());
        canvas.drawPath(path1, paint);

    }

    // 绘制坐标点
    private void drawDottedAll(Paint paint, Canvas canvas, int index) {
        // if (i > 0 && YCoord(Data[i - 1]) != -999
        // && YCoord(Data[i]) != -999) // 保证有效数据
        // canvas.drawLine(XPoint + (i - 1) * XScale,
        // YCoord(Data[i - 1]), XPoint + i * XScale,
        // YCoord(Data[i]), paint);

        // canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
        // R.mipmap.dotblue), XPoint + index * XScale, YCoord(Data[index]),
        // paint);

        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.font_white));
        paint.setAlpha(170);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(XPoint + index * XScale, YCoord(allData[index]), 12,
                paint);

        paint.setColor(getResources().getColor(R.color.light_blue_1));
        paint.setAlpha(255);
        paint.setStrokeWidth(3);
        canvas.drawCircle(XPoint + index * XScale, YCoord(allData[index]), 8,
                paint);
    }

    // 绘制选中坐标点的圆点
    private void drawDottedSelect(Paint paint, Canvas canvas) {

        // 去锯齿
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.font_white));
        paint.setAlpha(170);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(DirectXScale, DirectYScale, 12, paint);

        paint.setColor(getResources().getColor(R.color.light_blue_1));
        paint.setAlpha(255);
        paint.setStrokeWidth(3);
        canvas.drawCircle(DirectXScale, DirectYScale, 8, paint);
    }

    private float YCoord(String y0) // 计算绘制时的Y坐标，无数据时返回-999
    {
        float y;
        try {
            y = Float.parseFloat(y0);
        } catch (Exception e) {
            return 0; // 出错则返回-999
        }
        try {
            return YPoint - y * YScale / Float.parseFloat(YLabel[1] + "");
        } catch (Exception e) {
        }
        return y;
    }

    private Double changerStringToDouble(String y0) // 计算绘制时的Y坐标，无数据时返回-999
    {
        Double y;
        try {
            y = Double.parseDouble(y0);
        } catch (Exception e) {
            return -0.0; // 出错则返回-999
        }
        // try {
        // return YPoint - y * YScale / Integer.parseInt(YLabel[1]+"");
        // } catch (Exception e) {
        // }
        return y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = touchTempX = (int) event.getRawX();
                down_x = event.getX();
                down_y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                touchTempX = (int) event.getRawX();
                touchTotalMoveX = touchTempX - touchDownX;
                break;
            case MotionEvent.ACTION_UP:
//                if (Math.abs(touchTotalMoveX) >= 10) {
//                    if (touchTotalMoveX > 0) {
//                        changePointPosition(false);
//                    } else if (touchTotalMoveX < 0) {
//                        changePointPosition(true);
//                    }
//                    invalidate();
//                }
//                touchTotalMoveX = 0;
                float up_x = event.getX();
                float up_y = event.getY();
                if (Math.abs(down_x - up_x) <= CommonUtils.Dp2Px(context, 10)
                        && Math.abs(down_y - up_y) <= CommonUtils.Dp2Px(context, 10)) {
                    changePointPosition(up_x, up_y);
                }
                break;
        }

        return true;
    }

    /**
     * 改变提示信息的点的坐标位置
     *
     * @param isLeft 左滑为true 否则为false
     */
    public void changePointPosition(boolean isLeft) {
        if (isLeft && DirectDataIndex > 0) {
            DirectDataIndex--;
        } else if (!isLeft && DirectDataIndex < dataLength - 1) {
            DirectDataIndex++;
        }
        contextShow = allData[DirectDataIndex];
        textInfoSize = allData[DirectDataIndex].length();
    }

    /**
     * 改变提示信息的点的坐标位置
     */
    private void changePointPosition(float x, float y) {
        int len = listXYCord.size();
        for (int i = 0; i < len; i++) {
            XYCoord xyCoord = listXYCord.get(i);
            if (Math.abs(xyCoord.getX() - x) <= CommonUtils.Dp2Px(context, 10)
                    && Math.abs(xyCoord.getY() - y) <= CommonUtils.Dp2Px(context, 10)) {
                DirectDataIndex = i;
                contextShow = allData[DirectDataIndex];
                textInfoSize = allData[DirectDataIndex].length();
                postInvalidate();
                break;
            }
        }
    }
}
