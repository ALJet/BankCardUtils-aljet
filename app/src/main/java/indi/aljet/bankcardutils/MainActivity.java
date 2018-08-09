package indi.aljet.bankcardutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import indi.aljet.bankcardutils.util.BankInfoUtil;


public class MainActivity extends AppCompatActivity {

    private TextView tvCard;
    private EditText etCard;
    private Button btn_find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCard = (TextView)findViewById(R.id.tv_info);
        etCard = (EditText)findViewById(R.id.et_cardNo);
        btn_find = (Button)findViewById(R.id.btn_find);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardNum = etCard.getText().toString()
                        .replace(" ","");
                BankInfoUtil mInfoUtil = new BankInfoUtil(cardNum);

                String bankName = mInfoUtil.getBankName();
                String bankId = mInfoUtil.getBankId();
                String bankCardType = mInfoUtil.getCardType();

                StringBuffer sb = new StringBuffer("银行卡类型： +    "
                        + bankCardType + "       银行卡名称  ： +"
                        + bankName + "     银行卡ID ：   " + bankId);

                tvCard.setText(sb);

            }
        });

    }




    /**
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }


    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }



}
