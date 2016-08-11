package com.inuc.wifiuse.report.widget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inuc.wifiuse.R;
import com.inuc.wifiuse.beans.ReportBean;
import com.inuc.wifiuse.commons.Urls;
import com.inuc.wifiuse.main.widget.MainActivity;
import com.inuc.wifiuse.utils.ActivityCollector;
import com.inuc.wifiuse.utils.GetTimesAndCode;
import com.inuc.wifiuse.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class ReportItemActivity extends AppCompatActivity {
    private TextView mrtitle, mcontent, mtype, reporttime, Instruction2, Instruction2Time, Instruction1,
            Instruction1Time, Instruction0, Instruction0Time;
    private ReportBean c;
    private Button mrsubmit;
    private EditText myinstruction;
    private String reportid, Content;
    private TextView BarTitle, BarRight;
    private String postAuditReportURL;
    private SharedPreferences pref;
    private long applicationid = 1;
    private String times;
    private String code;
    private String username;
    private ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_item);
        ActivityCollector.addActivity(this);
        initview();
        pref = getSharedPreferences("data", MODE_PRIVATE);
        times = GetTimesAndCode.getTimes();
        code = GetTimesAndCode.getCode(times);
        applicationid = pref.getLong("applicationID", 1);
        username = pref.getString("username", "");

        mrsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sub = myinstruction.getText().toString();
                if (sub.equals("")) {
                    Snackbar.make(myinstruction, "提交内容不能为空", Snackbar.LENGTH_SHORT).show();
                } else {
                    postAuditReportURL = Urls.PostAuditReportURL + "times=" + times + "&code=" + code + "&applicationID=" + applicationid;
                    List<OkHttpUtils.Param> params=new ArrayList<OkHttpUtils.Param>();
                    OkHttpUtils.Param param1=new OkHttpUtils.Param("isPass","true");
                    OkHttpUtils.Param param2=new OkHttpUtils.Param("username",username);
                    OkHttpUtils.Param param3=new OkHttpUtils.Param("contents",sub);
                    OkHttpUtils.Param param4=new OkHttpUtils.Param("id",c.getID());
                    params.add(param1);params.add(param2);params.add(param3);params.add(param4);
                    OkHttpUtils.ResultCallback<String> loginCallback = new OkHttpUtils.ResultCallback<String>() {
                        @Override
                        public void onSuccess(String response) {

                            int flag =Integer.parseInt(response);
                            if (flag>0) {
                                Toast.makeText(ReportItemActivity.this,"提交成功！", Toast.LENGTH_SHORT).show();
                                myinstruction.setText("");
                                finish();
                            } else {
                                Snackbar.make(myinstruction, "提交失败！您没有审批权限...", Snackbar.LENGTH_SHORT).show();
                                myinstruction.setText("");
                               mrsubmit.setEnabled(false);
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            e.printStackTrace();
                            Snackbar.make(myinstruction, "网络连接失败", Snackbar.LENGTH_SHORT).show();

                        }
                    };
                    OkHttpUtils.post(postAuditReportURL, loginCallback,params);//提交回复
                }
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.removeActivity(ReportItemActivity.this);
            }
        });
    }

    private void initview() {
        c = (ReportBean) getIntent().getSerializableExtra("report");
        int f = Integer.parseInt(getIntent().getStringExtra("tijiao"));
        BarTitle = (TextView) findViewById(R.id.id_bar_title);
        BarRight = (TextView) findViewById(R.id.bar_right_tv);
        BarTitle.setText("详情");
        BarRight.setText("");
backIV= (ImageView) findViewById(R.id.id_back_arrow_image);

        mrtitle = (TextView) findViewById(R.id.mrtitle);
        mcontent = (TextView) findViewById(R.id.mcontent);
        reporttime = (TextView) findViewById(R.id.reportTime);
        mtype = (TextView) findViewById(R.id.mtype);
        Instruction2 = (TextView) findViewById(R.id.Instruction2);
        Instruction1 = (TextView) findViewById(R.id.Instruction1);
        Instruction0 = (TextView) findViewById(R.id.Instruction0);
        Instruction2Time = (TextView) findViewById(R.id.Instruction2Time);
        Instruction1Time = (TextView) findViewById(R.id.Instruction1Time);
        Instruction0Time = (TextView) findViewById(R.id.Instruction0Time);
        mrsubmit = (Button) findViewById(R.id.mrsubmit);
        myinstruction = (EditText) findViewById(R.id.myinstruction);
        if (f == 2) {
            myinstruction.setVisibility(View.INVISIBLE);
            mrsubmit.setVisibility(View.INVISIBLE);
        }
        reportid = c.getID();
        mrtitle.setText(c.getTitle());
        mcontent.setText(c.getContents());
        mtype.setText(c.getReportType());
        reporttime.setText(c.getSubmitTime());
        Instruction2.setText(c.getInstruction2());
        Instruction1.setText(c.getInstruction1());
        Instruction0.setText(c.getInstruction0());
        Instruction2Time.setText(c.getInstruction2Time());
        Instruction1Time.setText(c.getInstruction1Time());
        Instruction0Time.setText(c.getInstruction0Time());
    }
}
