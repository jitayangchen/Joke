package com.pepoc.joke.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pepoc.joke.R;
import com.pepoc.joke.data.user.UserManager;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestAddJoke;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PublishJokeActivity extends BaseSwipeBackActivity {

    @Bind(R.id.et_joke_content)
    EditText etJokeContent;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_joke);
        ButterKnife.bind(this);

        init();
    }

    @Override
    public void init() {
        super.init();

        toolbar.setTitle(R.string.menu_write_joke);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addJoke();
            }
        });
    }

    private void addJoke() {
        if (null == UserManager.getCurrentUser()) {
            Toast.makeText(context, "not login", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = etJokeContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(context, "content null", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = UserManager.getCurrentUser().getUserId();
        RequestAddJoke requestAddJoke = new RequestAddJoke(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                String status = (String) result;
                if ("1".equals(status)) {
                    Toast.makeText(context, "send success", Toast.LENGTH_SHORT).show();
                    etJokeContent.setText("");
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        requestAddJoke.putParam("content", content);
        requestAddJoke.putParam("user_id", uid);

        HttpRequestManager.getInstance().sendRequest(requestAddJoke);
    }
}
