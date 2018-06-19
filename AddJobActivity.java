

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.icsdeliverysystem.R;
import com.icsdeliverysystem.model.ModelJob;
import com.icsdeliverysystem.storage.DatabaseHelper;
import com.icsdeliverysystem.utility.Utility;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class AddJobActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup radioGroup;
    private RadioButton rbService, rbInvoice;
    private LinearLayout llJobDetail2;
    private EditText etZoneId, etAccountCode, etserviceNumber, etAmount;
    private DatabaseHelper databaseHelper;
    private Activity activity = AddJobActivity.this;
    private boolean hasUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        findViewById();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbService) {
                    llJobDetail2.setVisibility(View.GONE);
                } else if (checkedId == R.id.rbInvoice) {
                    llJobDetail2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void findViewById() {
        databaseHelper = new DatabaseHelper(activity);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Add Job");

        findViewById(R.id.btnSave).setOnClickListener(this);
        radioGroup = findViewById(R.id.radioGroup);
        rbInvoice = findViewById(R.id.rbInvoice);
        rbService = findViewById(R.id.rbService);
        llJobDetail2 = findViewById(R.id.llJobDetail2);
        etZoneId = findViewById(R.id.etZoneId);
        etAccountCode = findViewById(R.id.etAccountCode);
        etserviceNumber = findViewById(R.id.etserviceNumber);
        etserviceNumber.setOnClickListener(this);
        etAmount = findViewById(R.id.etAmount);
        llJobDetail2 = findViewById(R.id.llJobDetail2);
        ImageButton ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(this);

        etserviceNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable k1) {

                String pattern1 = "^[sSdD0-9]$";
                String pattern2 = "^[sSdD0-9]\\d+";
                String text = k1.toString();

                int length = text.length();
                if (length > 0) {
                    if (length == 1)
                    {
                        if (!Pattern.matches(pattern1, text)) {
                          String x=text.substring(0, length-1 );
                          etserviceNumber.setText(x);
                          etserviceNumber.setSelection(x.length());}
                    } else {
                        if (!Pattern.matches(pattern2, text)) {
                            String x=text.substring(0, length-1 );
                            etserviceNumber.setText(x);
                            etserviceNumber.setSelection(x.length());}
                    }
                }
                if (Pattern.matches("[0-9]+", etserviceNumber.getText().toString())) {
                    rbInvoice.setChecked(true);
                } else {
                    rbService.setChecked(true);}
            }
        });
    }

    public String customFormat(double value) {
        DecimalFormat myFormatter = new DecimalFormat("00000.00");
        String output = myFormatter.format(value);
        String part1 = output.substring(0, 5);
        String part2 = output.substring(6, 8);

        return part1 + part2;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSave:
                String ZoneId = etZoneId.getText().toString().toUpperCase();
                String AccountCode = etAccountCode.getText().toString().toUpperCase();
                String ServiceNumber = etserviceNumber.getText().toString().toUpperCase();
                Log.e( "onClick::: ",""+ServiceNumber );
                int type;

                String title = ZoneId + AccountCode + " " + ServiceNumber;
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbService)
                    type = 1;
                else
                    type = 2;

                if (validate()) {
                    String Amount = (radioGroup.getCheckedRadioButtonId() == R.id.rbService ? "0000000" : etAmount.getText().toString());
                    String formatedAmount = customFormat(Double.parseDouble(Amount));

                    ModelJob modelJob = new ModelJob();
                    modelJob.setTitle(title);
                    modelJob.setZoneId(etZoneId.getText().toString());
                    modelJob.setAccountCode(etAccountCode.getText().toString());
                    modelJob.setServiceNumber(etserviceNumber.getText().toString());
                    modelJob.setType(type);
                    modelJob.setAmount(formatedAmount);
                    modelJob.setDelivered(1);
                    modelJob.setPaymentMode(1);
                    modelJob.setSignature("");
                    modelJob.setChequeNumber("");
                    modelJob.setCollectCash("");
                    modelJob.setIsFinish(0);
                    modelJob.setComments("");
                    databaseHelper.addJob(modelJob);
                    hasUpdate = true;
                    onBackPressed();
                    break;
                }
                break;

            case R.id.ivBack:
                onBackPressed();
                break;

        }
    }


    private boolean validate() {
        if (etZoneId.getText().toString().equals("") || etZoneId.getText().toString().length() < 2) {
            Utility.showTost("Please valid Division Id");
            return false;
        } else if (etAccountCode.getText().toString().equals("")
                || etAccountCode.getText().toString().length() < 7) {
            Utility.showTost("Please valid AccountCode");
            return false;

        }else if (!(Pattern.matches("^[a-zA-Z][0-9]{6}$", etAccountCode.getText().toString()))) {
            Utility.showTost("Please valid AccountCode");
            return false;

        }
//        else if (!(Pattern.matches("^[a-zA-Z].*", etAccountCode.getText().toString()))) {
//            Utility.showTost("Please valid AccountCode");
//            return false;
//
//        }
        else if (etserviceNumber.getText().toString().equals("") || etserviceNumber.getText().toString().length() < 7) {
            return false;

        }
        /*else if(!(Pattern.matches("^[D].*", etserviceNumber.getText().toString())||
                Pattern.matches("^[d].*", etserviceNumber.getText().toString())||
                Pattern.matches("^[s].*", etserviceNumber.getText().toString()) ||
                Pattern.matches("[0-9]+", etserviceNumber.getText().toString()) ||
                Pattern.matches("^[S].*", etserviceNumber.getText().toString())))
        {
            Utility.showTost("Please insert valid Service/Invoice number");
            return false;
        }*/
        else if (etAmount.getText().toString().equals("") && radioGroup.getCheckedRadioButtonId() == R.id.rbInvoice) {
            Utility.showTost("Please insert Amount");
            return false;
        } else
            return true;
    }

    @Override
    public void onBackPressed() {
        if (hasUpdate) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        super.onBackPressed();
        Utility.goPrevious(activity);
    }
}
