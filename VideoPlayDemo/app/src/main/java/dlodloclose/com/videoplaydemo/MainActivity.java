package dlodloclose.com.videoplaydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //在这里设置视频的网络地址
    String fileUrl = "http://192.168.2.2:8080/download/file/video/201702/086e28b3-e832-4419-a634-9fd43829d3ee.mp4";
    @BindView(R.id.tv_hello)
    TextView tvHello;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_hello)
    public void onClick() {
        Intent intent = new Intent(getApplicationContext(), VideoPlayActivity.class);
        intent.putExtra("file_url", fileUrl);
        startActivity(intent);
    }
}
