package kusurin.custom_ir_signal_sender;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText carrier_frequency_input = null;
    private EditText pattern_input = null;
    private ImageButton send_but = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.carrier_frequency_input = (EditText) super.findViewById(R.id.carrier_frequency_input);
        this.pattern_input = (EditText) super.findViewById(R.id.pattern_input);
        this.send_but = (ImageButton) super.findViewById(R.id.send_but);

        send_but.setOnClickListener(new ShowListener());
    }

    private class ShowListener implements View.OnClickListener {
        public void onClick(View view) {
            int cf = Integer.parseInt(carrier_frequency_input.getText().toString());

            String pattern_s = pattern_input.getText().toString();

            ArrayList<Integer> numList = new ArrayList<>();
            Stack<Integer> digits = new Stack<>();
            Pattern isNum = Pattern.compile("[0-9]");
            int num;
            int base;
            int i;

            for (i = 0; i < pattern_s.length(); i++) {
                try {
                    if (pattern_s.charAt(i) == ',') {
                        num = 0;
                        base = 1;
                        while (!digits.empty()) {
                            num += (Integer) digits.pop() * base;
                            base *= 10;
                        }//i will consider creating a new class
                        numList.add(num);
                    } else if (isNum.matcher(String.valueOf(pattern_s.charAt(i))).matches()) {
                        digits.push(Integer.parseInt(String.valueOf(pattern_s.charAt(i))));
                    } else throw new IllegalArgumentException("wrong pattern format");
                } catch (IllegalArgumentException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            try {
                num = 0;
                base = 1;
                while (!digits.empty()) {
                    num += (Integer) digits.pop() * base;
                    base *= 10;
                }//i will consider creating a new class
                numList.add(num);
                if (numList.size() % 2 == 1 || i < pattern_s.length() || numList.isEmpty())
                    throw new IllegalArgumentException("incorrect number of arguments");
                int[] pattern = new int[numList.size()];
                for (i = 0; i < numList.size(); i++) {
                    pattern[i] = numList.get(i);
                }
                IrApi sender = IrApi.getIr(MainActivity.this);
                sender.transmit(cf, pattern);
            } catch (IllegalArgumentException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            numList.clear();
            digits.clear();

        }
    }
}
