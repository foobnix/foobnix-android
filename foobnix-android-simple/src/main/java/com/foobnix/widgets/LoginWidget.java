package com.foobnix.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foobnix.commons.LOG;
import com.foobnix.commons.ViewBinder;
import com.foobnix.R;

public class LoginWidget extends LinearLayout {

    private ImageView image;
    private TextView loginView;
    private TextView passwordView;
    private OnSingInListener onSingInListener;

    public LoginWidget(Context context) {
        this(context, null);
    }

    public LoginWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.login_widget, this);

        image = (ImageView) layout.findViewById(R.id.imageLogo);
        loginView = (TextView) layout.findViewById(R.id.login);
        passwordView = (TextView) layout.findViewById(R.id.password);

        ViewBinder.onClick(layout, R.id.signIn, onClick);
        ViewBinder.onClick(layout, R.id.loginLabel, onLoginLabel);
        ViewBinder.onClick(layout, R.id.passwordLabel, onPassLabel);


    }

    public void setLogo(int resId) {
        image.setImageResource(resId);
    }

    public void setValues(String login, String pass) {
            loginView.setText(login);
            passwordView.setText(pass);
    }

    View.OnClickListener onLoginLabel = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            loginView.requestFocus();
        }
    };

    View.OnClickListener onPassLabel = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            passwordView.requestFocus();
        }
    };

    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (onSingInListener != null) {
                onSingInListener.onSignIn(getLogin(), getPassword());
            } else {
                LOG.d("OnSingInListener not defined");
            }
        }
    };

    public String getLogin() {
        return loginView.getText().toString();
    }

    public String getPassword() {
        return passwordView.getText().toString();
    }

    public void setOnSingInListener(OnSingInListener onSingInListener) {
        this.onSingInListener = onSingInListener;
    }

    public OnSingInListener getOnSingInListener() {
        return onSingInListener;
    }

    public interface OnSingInListener {
        public abstract void onSignIn(String login, String pass);
    }

}
