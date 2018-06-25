package com.jetictors.futures.yake.widgets.verticalTextView

import android.content.Context
import android.graphics.*
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.view.View
import com.futures.jetictors.androidwidgets.R

/**
 * 描述    : 竖向显示的TextView
 * author  : Jetictors
 * time    : 2018/1/17 11:27
 * Email   : zhengxcfutures@gmail.com
 * version : v-1.0.1
 */
class VerticalTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : View(context, attrs, defStyleAttr) {

    companion object {
        /*
           开始方向
           START_ORIENTATION_LEFT ：文字从左至右显示
           START_ORIENTATION_RIGHT ：文字从右至左显示
        */
        @IntDef(START_ORIENTATION_LEFT, START_ORIENTATION_RIGHT)
        @Retention(AnnotationRetention.SOURCE)
        annotation class StartOrientation
        const val START_ORIENTATION_LEFT = 1
        const val START_ORIENTATION_RIGHT = 2

        /*
              分割线类型
              LINE_TYPE_NONE : 无分割线
              LINE_TYPE_REAL : 实线
              LINE_TYPE_DOTTED : 虚线
         */
        @IntDef(LINE_TYPE_NONE, LINE_TYPE_REAL, LINE_TYPE_DOTTED)
        @Retention(AnnotationRetention.SOURCE)
        annotation class LineType
        const val LINE_TYPE_NONE = 0
        const val LINE_TYPE_REAL = 1
        const val LINE_TYPE_DOTTED = 2

        /*
            字体风格
            STYLE_NORMAL : 正常
            STYLE_BOLD : 粗体
            STYLE_ITALIC : 斜体
            STYLE_BOLD_ITALIC : 粗斜体
         */
        @IntDef(STYLE_NORMAL, STYLE_BOLD, STYLE_ITALIC, STYLE_BOLD_ITALIC)
        @Retention(AnnotationRetention.SOURCE)
        annotation class TextStyle
        const val STYLE_NORMAL = 0
        const val STYLE_BOLD = 1
        const val STYLE_ITALIC = 2
        const val STYLE_BOLD_ITALIC = 3

        /*
              文本出现类型
              SHOW_TYPE_NONE : 无动画
              SHOW_TYPE_ONCE : 逐字显示
              SHOW_TYPE_COLUMN : 逐列显示
              SHOW_TYPE_ROW : 逐行显示
              SHOW_TYPE_ALL : 全部显示
         */
        @IntDef(SHOW_TYPE_NONE, SHOW_TYPE_ONCE, SHOW_TYPE_COLUMN, SHOW_TYPE_ROW, SHOW_TYPE_ALL)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ShowType
        const val SHOW_TYPE_NONE = 0
        const val SHOW_TYPE_ONCE = 1
        const val SHOW_TYPE_COLUMN = 2
        const val SHOW_TYPE_ROW = 3
        const val SHOW_TYPE_ALL = 4
    }

    // 字体大小
    private var textSize = 0f

    // 字体颜色
    private var textColor = 0

    // 显示文本
    private var text: String = ""

    // 文字排版方向
    @StartOrientation
    private var startOrientation = START_ORIENTATION_LEFT

    // 分割线宽度
    private var lineWidth = 1f

    // 分割线风格
    @LineType
    private var lineType = LINE_TYPE_REAL

    // 当分割线为虚线时，虚线中每一段小线的高度
    private var dottedLineHeight = 12f

    // 当分割线为虚线时，虚线中每一段小线与小线之间的间隔
    private var dottedLineMargin = 6f

    // 分割线颜色
    private var lineColor = 0

    // 列与列之间的间距
    private var textHorizontalMargin = 8f

    // 一列中字与字的间距
    private var textVerticalMargin = 4f

    // 设置文字风格（加粗/斜线等）
    @TextStyle
    private var textStyle = STYLE_NORMAL

    // assets下的字体路径
    private var textFont: String? = null

    // 文本出现的动画类型
    @ShowType
    private var showAnimType = SHOW_TYPE_COLUMN

    // 文本出现动画执行时长
    private var showTimeCount = 5000L

    // 是否支持对某一些文本的标注
    private var isLabel = false

    // 标注文本的下标字符串
    private var labelIndex: String? = null

    // 标注文本的真实下标
    private lateinit var realLabelIndex: IntArray

    // 标注的颜色
    private var labelColor = 0

    // 组件本身是否能支持滑动
    private var isMove = true

    // 画笔
    private lateinit var paint: Paint

    // 组件的高度和宽度
    private var realWidth: Int = 0
    private var realHeight: Int = 0

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalTextView)

        this.startOrientation = typedArray.getInt(R.styleable.VerticalTextView_start_orientation, 1)
        this.text = typedArray.getString(R.styleable.VerticalTextView_text)
        this.textColor = typedArray.getColor(R.styleable.VerticalTextView_textColor, Color.BLACK)
        this.textSize = typedArray.getDimension(R.styleable.VerticalTextView_textSize, 14f)
        this.textStyle = typedArray.getInt(R.styleable.VerticalTextView_text_style, 0)
        this.textFont = typedArray.getString(R.styleable.VerticalTextView_text_font)
        this.textVerticalMargin = typedArray.getDimension(R.styleable.VerticalTextView_textVerticalMargin, textVerticalMargin)
        this.textHorizontalMargin = typedArray.getDimension(R.styleable.VerticalTextView_textHorizontalMargin, textHorizontalMargin)
        this.lineWidth = typedArray.getDimension(R.styleable.VerticalTextView_lineWidth, lineWidth)
        this.lineColor = typedArray.getColor(R.styleable.VerticalTextView_lineColor, Color.GRAY)
        this.lineType = typedArray.getInt(R.styleable.VerticalTextView_line_type, 2)
        this.showAnimType = typedArray.getInt(R.styleable.VerticalTextView_line_type, 0)
        this.showTimeCount = typedArray.getInt(R.styleable.VerticalTextView_showTimeCount, 0).toLong()
        this.dottedLineHeight = typedArray.getDimension(R.styleable.VerticalTextView_dotted_line_height, dottedLineHeight)
        this.dottedLineMargin = typedArray.getDimension(R.styleable.VerticalTextView_dotted_line_margin, dottedLineMargin)
        this.isLabel = typedArray.getBoolean(R.styleable.VerticalTextView_isLabel, isLabel)
        this.labelIndex = typedArray.getString(R.styleable.VerticalTextView_labelIndex)
        this.labelColor = typedArray.getColor(R.styleable.VerticalTextView_labelColor, Color.RED)
        this.isMove = typedArray.getBoolean(R.styleable.VerticalTextView_isMove, isMove)

        typedArray.recycle()
        initPaint()
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        paint = Paint()
        paint.apply {
            this.textSize = textSize
            this.color = textColor
            this.isAntiAlias = true
            this.typeface = Typeface.DEFAULT
            this.textAlign = Paint.Align.CENTER
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        realWidth = measureWidth(widthMeasureSpec)

        val h = measureHeight(heightMeasureSpec)
        realHeight = if (h > height){
            height
        }else{
            h
        }

        setMeasuredDimension(realWidth, realHeight)
    }

    /**
     * 计算宽度
     */
    private fun measureWidth(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        return if(specMode == MeasureSpec.EXACTLY){
            specSize
        }else{
            if (getColNum() == 1){
                (getOneWordWidth() + paddingLeft + paddingRight).toInt()
            }else{
                (getOneWordWidth() * getColNum() + textHorizontalMargin * (getColNum() -1) + paddingLeft + paddingRight).toInt()
            }
        }
    }

    /**
     * 计算高度
     */
    private fun measureHeight(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        return if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else {
            (getOneWordHeight() * text.length + textVerticalMargin * (text.length - 1) + paddingTop + paddingBottom).toInt()
        }
    }

    /**
     * 获取文本的BaseLine
     */
    private fun getTextBaseLine(rect: RectF): Float {
        val metricsInt = paint.fontMetricsInt
        return (rect.top + rect.bottom - metricsInt.top - metricsInt.bottom) / 2
    }

    /**
     * 根据文本长度确定列数
     * @return 列数
     */
    private fun getColNum(): Int {
        var colNum = 0



        return colNum
    }

    /**
     * 获取第一个字的宽度
     * @return
     */
    private fun getOneWordWidth(): Float {
        return paint.measureText("雅", 0, 1)
    }

    /**
     * 获取第一个字的高度
     */
    private fun getOneWordHeight(): Float {
        val rect = Rect()
        paint.getTextBounds("雅", 0, 1, rect)
        return rect.height().toFloat()
    }

    /**
     * 获取一列占有最大字符个数
     */
    private fun getColWordCount(): Int {
        val result = (realHeight / getOneWordHeight()).toDouble()
        // 向上取整
        return Math.ceil(result).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)



//        drawLabel(canvas)
//
        val oneLineWordCount = getColWordCount()
        val w = getOneWordWidth()
        val h = getOneWordHeight()
//
//        val colNum = getColNum()
//        val cutCharArray = spiltChars?.split("|".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
//        if (cutCharArray != null) {
//            val textArray = text.split(spiltChars!!.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//            var stepCol = 0
//            for (n in textArray.indices) {
//                val text = textArray[n]
//                var currentCol = 0
//                for (i in 0 until text.length) {
//                    val str = text[i].toString()
//                    var currentRow = i % oneLineWordCount
//                    if (colNum == 1) {
//                        currentRow = i
//                    }
//                    if (colNum > 1) {
//                        currentCol = stepCol + i / oneLineWordCount
//                    }
//                    drawText(getOneTextRectF(w, h, currentCol, currentRow), str, canvas)
//                    drawLine(getOneTextRectF(w, h, currentCol, currentRow), currentCol, canvas)
//                    if (i + 1 == text.length) {
//                        stepCol = currentCol + 1
//                    }
//                }
//            }
//        } else {
//            var currentCol = 0
//            for (i in 0 until text.length) {
//                val str = text[i].toString()
//                var currentRow = i % oneLineWordCount
//                if (colNum == 1) {
//                    currentRow = i
//                }
//                if (colNum > 1) {
//                    currentCol = i / oneLineWordCount
//                }
//                drawText(getOneTextRectF(w, h, currentCol, currentRow), str, canvas)
//                drawLine(getOneTextRectF(w, h, currentCol, currentRow), currentCol, canvas)
//            }
//        }
    }

    /**
     * 获取每一个字符占有的矩形
     * @param w 每一列的宽度
     * @param h 每一列的高度
     * @param currentCol 当前的列
     * @param currentRow 当前的行
     */
    private fun getOneTextRectF(w: Float, h: Float, currentCol: Int, currentRow: Int): RectF {

        return if (startOrientation == START_ORIENTATION_LEFT) {
            RectF(currentCol * w, currentRow * h, currentCol * w + w, currentRow * h + h)
        } else {
            RectF(width - (currentCol + 1) * w, currentRow * h, width - (currentCol + 1) * w + w,
                    currentRow * h + h)
        }
    }

    /**
     * 画文本

     * @param str 文本
     * @param canvas 画布
     */
    private fun drawText(rect: RectF, str: String, canvas: Canvas) {
        val baseline = getTextBaseLine(rect)
        paint.typeface = getTextTypeface()
        paint.color = textColor
        paint.style = Paint.Style.FILL
        canvas.drawText(str, rect.centerX(), baseline, paint)
    }

    /**
     * 画分割线
     */
    private fun drawLine(rect: RectF, currentCol: Int, canvas: Canvas) {

        // 是否需要分割线
        if (lineType == LINE_TYPE_NONE) {
            return
        }

        paint.color = lineColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = lineWidth

        // 画虚线还是实线
        if (lineType == LINE_TYPE_DOTTED) {
            val dotted = FloatArray(2)
            dotted[0] = dottedLineHeight
            dotted[1] = dottedLineMargin
            paint.pathEffect = DashPathEffect(dotted, 0f)
        }
    }

    /**
     * 画标注
     */
    private fun drawLabel(canvas: Canvas) {

        if (!isLabel) {
            return
        }

        // 处理下标
        val indexArray: List<String> = labelIndex?.split(",") ?: return

        // String 转 Int , 不能安全转换的返回null, 最后去除数组中的null
        realLabelIndex = indexArray.mapNotNull {
            it.toIntOrNull()
        }.toIntArray()

        // 画标注
        paint.color = labelColor
        paint.style = Paint.Style.FILL
        val radius = getOneWordHeight() / 2 + dip2px(context, 1f)
        var circleX: Float
        var circleY: Float
        realLabelIndex.map {
            if (it >= 0 && it <= text.length) {
                if (it <= getColWordCount()) {
                    if (startOrientation == START_ORIENTATION_LEFT) {
                        circleX = getOneWordWidth() / 2
                        circleY = getOneWordHeight() * it + getOneWordHeight() / 2
                    } else {
                        circleX = realWidth - getOneWordWidth() / 2
                        circleY = getOneWordHeight() * it + getOneWordHeight() / 2
                    }
                } else {
                    if (startOrientation == START_ORIENTATION_LEFT) {
                        circleX = getOneWordWidth() * (it / getColWordCount()) + getOneWordWidth() / 2
                        circleY = getOneWordHeight() * (it % getColWordCount()) + getOneWordHeight() / 2
                    } else {
                        circleX = realWidth - (getOneWordWidth() * (it / getColWordCount()) + getOneWordWidth() / 2)
                        circleY = getOneWordHeight() * (it % getColWordCount()) + getOneWordHeight() / 2
                    }
                }
                canvas.drawCircle(circleX, circleY, radius, paint)
            }
        }
    }

    /**
     * 获取字体风格
     */
    private fun getTextTypeface(): Typeface {
        if (textFont.isNullOrBlank()) {
            return when (textStyle) {
                STYLE_NORMAL -> Typeface.DEFAULT
                STYLE_BOLD -> Typeface.DEFAULT_BOLD
                STYLE_ITALIC -> Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
                STYLE_BOLD_ITALIC -> Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
                else -> Typeface.DEFAULT
            }
        } else {
            return when (textStyle) {
                STYLE_BOLD -> Typeface.create(Typeface.createFromAsset(context.assets, textFont), Typeface.BOLD)
                STYLE_ITALIC -> Typeface.create(Typeface.createFromAsset(context.assets, textFont), Typeface.ITALIC)
                STYLE_BOLD_ITALIC -> Typeface.create(Typeface.createFromAsset(context.assets, textFont), Typeface.BOLD_ITALIC)
                else -> Typeface.create(Typeface.createFromAsset(context.assets, textFont), Typeface.NORMAL)
            }
        }
    }

    /**
     * 设置文字起始方向
     * @param startOrientation
     */
    fun setStartOrientation(@StartOrientation startOrientation: Int) {
        this.startOrientation = startOrientation
        invalidate()
    }

    /**
     * 设置分割线宽度
     * @param lineWidth
     */
    fun setLineWidth(lineWidth: Float) {
        this.lineWidth = lineWidth
        invalidate()
    }

    /**
     * 设置分割线宽度
     * @param resId  资源id
     */
    fun setLineWidth(resId: Int) {
        this.lineWidth = resources.getDimension(resId)
        invalidate()
    }

    /**
     * 设置分割线颜色
     * @param lineColor
     */
    fun setLineColor(lineColor: Int) {
        this.lineColor = lineColor
        invalidate()
    }

    /**
     * 设置列与列之间的间隔
     * @param resId 资源id
     */
    fun setTextHorizontalMargin(resId: Int) {
        this.textHorizontalMargin = resources.getDimension(resId)
        invalidate()
    }

    /**
     * 设置列与列之间的间隔
     * @param textHorizontalMargin
     */
    fun setTextHorizontalMargin(textHorizontalMargin: Float) {
        this.textHorizontalMargin = textHorizontalMargin
        invalidate()
    }

    /**
     * 设置每一列中字与字的间隔
     * @param resId 资源id
     */
    fun setTextVerticalMargin(resId: Int) {
        this.textVerticalMargin = resources.getDimension(resId)
        invalidate()
    }

    /**
     * 设置每一列中字与字的间隔
     * @param textVerticalMargin
     */
    fun setTextVerticalMargin(textVerticalMargin: Float) {
        this.textVerticalMargin = textVerticalMargin
        invalidate()
    }

    /**
     * 设置风格线类型
     * @param lineType
     */
    fun setLineType(@LineType lineType: Int) {
        this.lineType = lineType
        invalidate()
    }

    /**
     * 设置分割线为虚线时，每一条虚线的小线的高度
     * @param resId 资源id
     */
    fun setDottedLineHeight(resId: Int) {
        this.dottedLineHeight = resources.getDimension(resId)
        invalidate()
    }

    /**
     * 设置分割线为虚线时，每一条虚线的小线的高度
     * @param dottedLineHeight
     */
    fun setDottedLineHeight(dottedLineHeight: Float) {
        this.dottedLineHeight = dottedLineHeight
        invalidate()
    }

    /**
     * 设置分割线为虚线时，每一条虚线的小线之间的间隔
     * @param resId 资源id
     */
    fun setDottedLineMargin(resId: Int) {
        this.dottedLineMargin = resources.getDimension(resId)
        invalidate()
    }

    /**
     * 设置分割线为虚线时，每一条虚线的小线之间的间隔
     * @param dottedLineMargin
     */
    fun setDottedLineMargin(dottedLineMargin: Float) {
        this.dottedLineMargin = dottedLineMargin
        invalidate()
    }

    /**
     * 设置文字尺寸
     * @param resId 资源id
     */
    fun setTextSize(resId: Int) {
        this.textSize = resources.getDimension(resId)
        invalidate()
    }

    /**
     * 设置文字尺寸
     * @param textSize
     */
    fun setTextSize(textSize: Float) {
        this.textSize = textSize
        invalidate()
    }

    /**
     * 设置文字颜色
     * @param textColor
     */
    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        invalidate()
    }

    /**
     * 设置文本
     * @param text 文本
     */
    fun setText(text: String) {
        this.text = text
        invalidate()
    }

    /**
     * 设置文本样式
     * @param textStyle
     */
    fun setTextStyle(@TextStyle textStyle: Int) {
        this.textStyle = textStyle
        invalidate()
    }

    /**
     * 设置文本字体的路径
     * assets/fonts目录下
     * @param textFont
     */
    fun setTextStyle(textFont: String) {
        this.textFont = textFont
        invalidate()
    }

    /**
     * 设置文本出现类型
     * @param showType
     */
    fun setShowType(@ShowType showType: Long) {
        this.showAnimType = showAnimType
        invalidate()
    }

    /**
     * 设置标注的颜色
     * @param labelColor
     */
    fun setLabelColor(labelColor: Int) {
        this.labelColor = labelColor
        invalidate()
    }

    /**
     * 设置标注文本的下标
     * @param labelIndex
     */
    fun setLabelIndex(labelIndex: String) {
        this.labelIndex = labelIndex
        invalidate()
    }

    /**
     * 设置标注文本的下标
     * @param labelIndex
     */
    fun setLabelIndex(vararg labelIndex: Int) {
        this.realLabelIndex = labelIndex
        invalidate()
    }

    /**
     * 是否支持标注
     * @param isLabel
     */
    fun isLable(isLabel: Boolean) {
        this.isLabel = isLabel
        invalidate()
    }

    /**
     * 是否支持滑动
     * @param isMove
     */
    fun isMove(isMove: Boolean) {
        this.isMove = isMove
        invalidate()
    }

    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}
