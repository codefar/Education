package com.education.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.education.R;

/**
 * Created by su on 2014/9/22.
 */
public class ClearableEditText extends RelativeLayout {
    private final ImageView mClearImageView;
    private final ImageView mPasswordImageView;
    private Resources mResources;
    private final EditText mInputEditText;

    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClearableEditText);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources = getResources();
        inflater.inflate(R.layout.clearable_edittext, this, true);
        TextView unitTextView = (TextView) findViewById(R.id.unit);
        mInputEditText = (EditText) findViewById(R.id.input);
        mInputEditText.setText(a.getString(R.styleable.ClearableEditText_android_text));
        mInputEditText.setTextColor(a.getColor(R.styleable.ClearableEditText_android_textColor, mResources.getColor(R.color.first_text)));
        mInputEditText.setHintTextColor(a.getColor(R.styleable.ClearableEditText_android_textColorHint, mResources.getColor(R.color.disable_text)));
        String unit = a.getString(R.styleable.ClearableEditText_unit);
        if (unit == null || "".equals(unit)) {
            unitTextView.setVisibility(View.GONE);
        } else {
            unitTextView.setVisibility(View.VISIBLE);
            unitTextView.setTextColor(a.getColor(R.styleable.ClearableEditText_unitColor, mResources.getColor(R.color.disable_text)));
            unitTextView.setText(unit);
        }
        int textSize = a.getDimensionPixelSize(R.styleable.ClearableEditText_android_textSize, 0);
        if (textSize > 0) {
            unitTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mInputEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        mInputEditText.setHint(a.getString(R.styleable.ClearableEditText_android_hint));
        mInputEditText.setIncludeFontPadding(false);
        mInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeBackground((ClearableEditText) v.getParent());
            }
        });

        mClearImageView = (ImageView) findViewById(R.id.clear);
        mClearImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputEditText.setText("");
            }
        });

        mInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (mClearImageView.getVisibility() != View.VISIBLE) {
                        mClearImageView.setVisibility(View.VISIBLE);
                        if (mPasswordImageView.getVisibility() == View.VISIBLE) {
                            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                            lp.addRule(RelativeLayout.LEFT_OF, mPasswordImageView.getId());
                            mClearImageView.setLayoutParams(lp);
                        }
                    }
                } else {
                    mClearImageView.setVisibility(View.GONE);
                }
            }
        });

        mPasswordImageView = (ImageView) findViewById(R.id.password_img);
        int inputType = a.getInt(R.styleable.ClearableEditText_android_inputType, EditorInfo.TYPE_NULL);
        if (inputType == EditorInfo.TYPE_NULL) {
            inputType = EditorInfo.TYPE_CLASS_TEXT;
        }

        mPasswordImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType = getInputType();
                if (isPasswordInputType(inputType)) {
                    setInputType(EditorInfo.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        setClearButton();
        setInputType(inputType);
        changeBackground(this);
        boolean enable = a.getBoolean(R.styleable.ClearableEditText_android_enabled, true);
        setEnabled(enable);
        a.recycle();
    }

    private void setClearButton() {
        TextView unitTextView = (TextView) findViewById(R.id.unit);
        LayoutParams lp = (LayoutParams) mClearImageView.getLayoutParams();
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        if (unitTextView.getVisibility() == View.VISIBLE) {
            lp.addRule(RelativeLayout.LEFT_OF, unitTextView.getId());
        } else if (mPasswordImageView.getVisibility() == View.VISIBLE) {
            lp.addRule(RelativeLayout.LEFT_OF, mPasswordImageView.getId());
        } else {
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        mClearImageView.setLayoutParams(lp);
    }

    public void setInputType(int inputType) {
        if (isPasswordInputType(inputType)) {
            mClearImageView.setVisibility(View.GONE);
            mPasswordImageView.setImageBitmap(BitmapFactory.decodeResource(mResources, R.drawable.password_invisible));
            mPasswordImageView.setVisibility(View.VISIBLE);
        } else if (isVisiblePasswordInputType(inputType)) {
            mClearImageView.setVisibility(View.GONE);
            mPasswordImageView.setImageBitmap(BitmapFactory.decodeResource(mResources, R.drawable.password_visible));
            mPasswordImageView.setVisibility(View.VISIBLE);
        } else {
            mClearImageView.setVisibility(View.VISIBLE);
            mPasswordImageView.setVisibility(View.GONE);
        }
        mInputEditText.setInputType(inputType);

        if (mInputEditText.getText().length() == 0) {
            mClearImageView.setVisibility(View.GONE);
        }
    }

    private static boolean isPasswordInputType(int inputType) {
        final int variation =
                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private static boolean isVisiblePasswordInputType(int inputType) {
        final int variation =
                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    public int getInputType() {
        return mInputEditText.getInputType();
    }

    public EditText getInputEditText() {
        return mInputEditText;
    }

    public Editable getText() {
        return mInputEditText.getText();
    }

    public void setText(CharSequence text) {
        mInputEditText.setText(text);
    }

    public final void setText(int resid) {
        mInputEditText.setText(mResources.getText(resid));
    }

    public void setTextColor(int color) {
        mInputEditText.setTextColor(color);
    }

    public void setTextHint(String hint) {
        mInputEditText.setHint(hint);
    }

    public void setEnabled(boolean enable) {
        mInputEditText.setEnabled(enable);
        super.setEnabled(enable);
    }

    public void setEditTextOnFocusChangeListener(OnFocusChangeListener listener) {
        mInputEditText.setOnFocusChangeListener(listener);
    }

    public View.OnFocusChangeListener getEditTextOnFocusChangeListener() {
        return mInputEditText.getOnFocusChangeListener();
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        mInputEditText.setOnEditorActionListener(listener);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        mInputEditText.addTextChangedListener(textWatcher);
    }

    public void removeTextChangedListener(TextWatcher textWatcher) {
        mInputEditText.removeTextChangedListener(textWatcher);
    }

    private static void changeBackground(ClearableEditText clearableEditText) {
        Context context = clearableEditText.getContext();
        Resources resources = context.getResources();
        if (clearableEditText.mInputEditText.hasFocus()) {
            clearableEditText.setBackgroundDrawable(resources.getDrawable(R.drawable.edittext_bg_focused));
        } else if (clearableEditText.isEnabled()) {
            clearableEditText.setBackgroundDrawable(resources.getDrawable(R.drawable.edittext_bg_default));
        } else {
            clearableEditText.setBackgroundDrawable(resources.getDrawable(R.drawable.edittext_bg_disable));
        }
    }

    public abstract static class OnFocusChangeListener implements View.OnFocusChangeListener {
        public void onFocusChange(View v) {
            changeBackground((ClearableEditText) v.getParent());
            onFocusChange(((ClearableEditText) v.getParent()).hasFocus());
        }

        public abstract void onFocusChange(boolean hasFocus);
    }
}
