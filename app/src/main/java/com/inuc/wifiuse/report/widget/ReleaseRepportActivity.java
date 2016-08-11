package com.inuc.wifiuse.report.widget;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inuc.wifiuse.R;
import com.inuc.wifiuse.commons.Urls;
import com.inuc.wifiuse.utils.ActivityCollector;
import com.inuc.wifiuse.utils.DoubleDatePickerDialog;
import com.inuc.wifiuse.utils.GetTimesAndCode;
import com.inuc.wifiuse.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReleaseRepportActivity extends AppCompatActivity {
    private TextView barTitle, barRight;
    private ImageView backIV;
    private EditText titleET, contentET;
    private Spinner typeSP;
    private Button submitBtn;
    private String myPostReportURL;

    private SharedPreferences pref;
    private long applicationid = 1;
    private String times;
    private String code;
    private String username;
private int flag;
    private TextView taketime_v, familyType, familyName, familySex, familyCarid;
    private String taketime, familyN, familyT, familyS, familyC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        flag=Integer.parseInt(getIntent().getStringExtra("flag"));
        setContentView(R.layout.activity_public_repport);
        pref = getSharedPreferences("data", MODE_PRIVATE);
        times = GetTimesAndCode.getTimes();
        code = GetTimesAndCode.getCode(times);
        applicationid = pref.getLong("applicationID", 1);
        username = pref.getString("username", "");
        initView();
        initEvent();
    }


    private void initView() {
        barTitle = (TextView) findViewById(R.id.id_bar_title);
        barRight = (TextView) findViewById(R.id.bar_right_tv);
        backIV = (ImageView) findViewById(R.id.id_back_arrow_image);
        titleET = (EditText) findViewById(R.id.report_release_title_et);
        typeSP = (Spinner) findViewById(R.id.Spinnerreport);
        contentET = (EditText) findViewById(R.id.report_releasw_content_et);
        submitBtn = (Button) findViewById(R.id.report_release_button);
        barTitle.setText("报告");
        barRight.setText("");
        if (flag==2){
            findViewById(R.id.report_type_layout).setVisibility(View.GONE);
            findViewById(R.id.apply_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.apply_type_layout).setVisibility(View.VISIBLE);
        }
        taketime_v = (TextView) findViewById(R.id.taketime_tv);
        familyType = (TextView) findViewById(R.id.familyType_tv);
        familyName = (TextView) findViewById(R.id.familyName_tv);
        familySex = (TextView) findViewById(R.id.familySex_tv);
        familyCarid = (TextView) findViewById(R.id.familyCarid_tv);
    }

    private void initEvent() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleET.getText().toString();
                String content = titleET.getText().toString();
                String type = (String) typeSP.getSelectedItem();

                taketime = taketime_v.getText().toString();
                final String[] taketimeArray = taketime.split("至");
                familyN = familyName.getText().toString();
                familyT = familyType.getText().toString();
                familyS = familySex.getText().toString();
                familyC = familyCarid.getText().toString();
                String members = familyN + "," + familyS + "," + familyT + "," + familyC;
                if (flag==2){
                    type="来队申请";
                }else {
                    members="";
                }

                if (content.equals("") || title.equals("")|| taketime.equals("")) {
                    Snackbar.make(contentET, "标题、内容和时间不能为空！", Snackbar.LENGTH_LONG).show();
                } else {
                    myPostReportURL = Urls.PostReportURL + "times=" + times + "&code=" + code + "&applicationID=" + applicationid;
                    List<OkHttpUtils.Param> params = new ArrayList<OkHttpUtils.Param>();
                    OkHttpUtils.Param param1 = new OkHttpUtils.Param("title", title);
                    OkHttpUtils.Param param2 = new OkHttpUtils.Param("username", username);
                    OkHttpUtils.Param param3 = new OkHttpUtils.Param("contents", content);
                    OkHttpUtils.Param param4 = new OkHttpUtils.Param("reportType", type);
                    OkHttpUtils.Param param5 = new OkHttpUtils.Param("StartTime", taketimeArray[0]);
                    OkHttpUtils.Param param6 = new OkHttpUtils.Param("EndTime",taketimeArray[1]);
                    OkHttpUtils.Param param7 = new OkHttpUtils.Param("Members",members);
                    params.add(param1);
                    params.add(param2);
                    params.add(param3);
                    params.add(param4);
                    params.add(param5);
                    params.add(param6);
                    params.add(param7);
                    OkHttpUtils.ResultCallback<String> postReportCallback = new OkHttpUtils.ResultCallback<String>() {
                        @Override
                        public void onSuccess(String response) {
                            int flag = Integer.parseInt(response);
                            if (flag > 0) {
                                Toast.makeText(ReleaseRepportActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                titleET.setText("");
                                contentET.setText("");
                                finish();
                            } else {
                                Snackbar.make(contentET, "提交失败", Snackbar.LENGTH_SHORT).show();
                                submitBtn.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            e.printStackTrace();
                            Snackbar.make(contentET, "网络连接失败", Snackbar.LENGTH_SHORT).show();

                        }
                    };
                    OkHttpUtils.post(myPostReportURL, postReportCallback, params);//请假

                }
            }
        });
        //时间选择器
        taketime_v.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                new DoubleDatePickerDialog(ReleaseRepportActivity.this, 0,
                        new DoubleDatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker startDatePicker,
                                                  int startYear, int startMonthOfYear,
                                                  int startDayOfMonth,
                                                  DatePicker endDatePicker, int endYear,
                                                  int endMonthOfYear, int endDayOfMonth) {
                                String textString = String.format(
                                        "%d-%d-%d至%d-%d-%d", startYear,
                                        startMonthOfYear + 1, startDayOfMonth,
                                        endYear, endMonthOfYear + 1,
                                        endDayOfMonth);
                                taketime_v.setText(textString);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DATE), false).show();
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.removeActivity(ReleaseRepportActivity.this);
            }
        });
    }

}
