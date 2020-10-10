package es.iessaladillo.pedrojoya.pr02_greetimproved;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import es.iessaladillo.pedrojoya.pr02_greetimproved.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private TextWatcher lblNameTextWatcher;
    private TextWatcher lblSurnameTextWatcher;
    String gender;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupViews();
    }

    protected void onStart() {
        super.onStart();
        binding.edtName.setOnFocusChangeListener(
                (v, hasFocus) -> changeColor(binding.lblCharsName, hasFocus));
        binding.edtSurname.setOnFocusChangeListener(
                (v, hasFocus) -> changeColor(binding.lblCharsSurname, hasFocus));
        lblNameTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateName(s.toString());
            }
        };
        binding.edtName.addTextChangedListener(lblNameTextWatcher);
        lblSurnameTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateSurname(s.toString());
            }
        };
        binding.edtSurname.addTextChangedListener(lblSurnameTextWatcher);
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.edtName.setOnFocusChangeListener(null);
        binding.edtSurname.setOnFocusChangeListener(null);
        binding.edtName.removeTextChangedListener(lblNameTextWatcher);
        binding.edtSurname.removeTextChangedListener(lblSurnameTextWatcher);
    }

    private boolean validateName(String name) {
        if (isValidName(name)) {
            binding.edtName.setError(null);
            return true;
        } else {
            binding.edtName.setError(getString(R.string.main_required));
            return false;
        }
    }

    private boolean validateSurname(String surname) {
        if (isValidSurname(surname)) {
            binding.edtSurname.setError(null);
            return true;
        } else {
            binding.edtSurname.setError(getString(R.string.main_required));
            return false;
        }
    }

    private boolean isValidName(String name) {
        return !TextUtils.isEmpty(name);
    }

    private boolean isValidSurname(String surname) {
        return !TextUtils.isEmpty(surname);
    }

    private boolean isValidForm(String name, String surname) {
        if (!validateName(name)) {
            binding.edtName.requestFocus();
            return false;
        }
        if (!validateSurname(surname)) {
            binding.edtName.requestFocus();
            return false;
        }
        return true;
    }

    private void clickGender(){
        if (binding.rdbMr.isChecked()) {
            binding.imgGender.setImageResource(R.drawable.ic_mr);
            gender = "Mr.";
        } else if (binding.rdbMrs.isChecked()) {
            binding.imgGender.setImageResource(R.drawable.ic_mrs);
            gender = "Mrs.";
        } else if (binding.rdbMs.isChecked()) {
            binding.imgGender.setImageResource(R.drawable.ic_ms);
            gender = "Ms.";
        }
    }

    private void btnGreetOnClick() {
        String name = binding.edtName.getText().toString();
        String surname = binding.edtSurname.getText().toString();
        if (isValidForm(name, surname)) {
            SoftInputUtils.hideSoftKeyboard(binding.lblSurname);
            clickGreet(name, surname);
        }
    }

    private void clickGreet(String name, String surname) {
        if (binding.chkPolitely.isChecked()) {
            Toast.makeText(this,
                    getString(R.string.txt_politely_checked, name, surname),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,
                    getString(R.string.txt_politely_notChecked, name, surname),
                    Toast.LENGTH_SHORT).show();
        }
        binding.prBar.setProgress(i++);
        binding.lblPrBar.setText(i + " of 10");

        if (i > 10) {
            binding.lblPrBar.setText("10 of 10");
            Toast.makeText(this, getString(R.string.txt_buyPremium), Toast.LENGTH_SHORT).show();
        }

    }

    private void isPremium() {
        if (binding.swtPremium.isChecked()) {
            binding.prBar.setVisibility(View.INVISIBLE);
            binding.lblPrBar.setVisibility(View.INVISIBLE);
            i = 0;
            reset();
        } else {
            binding.prBar.setVisibility(View.VISIBLE);
            binding.lblPrBar.setVisibility(View.VISIBLE);
            binding.prBar.setProgress(0);
            binding.lblPrBar.setText("0 of 10");
        }
    }

    private void reset() {
        btnGreetOnClick();
    }

    private boolean lblSurnameOnEditorAction() {
        btnGreetOnClick();
        return true;
    }

    private void changeColor(TextView textView, boolean hasFocus) {
        int colorResId = hasFocus ? R.color.colorAccent : R.color.textPrimary;
        textView.setTextColor(ContextCompat.getColor(this, colorResId));
    }

    private void setupViews() {

        changeColor(binding.lblCharsName, true);
        binding.rdbMr.setOnCheckedChangeListener((x, y) -> clickGender());
        binding.rdbMrs.setOnCheckedChangeListener((x, y) -> clickGender());
        binding.rdbMs.setOnCheckedChangeListener((x, y) -> clickGender());

        binding.lblSurname.setOnEditorActionListener((v, actionId, event) ->
                lblSurnameOnEditorAction());
        binding.btnGreet.setOnClickListener(v -> btnGreetOnClick());

        binding.swtPremium.setOnClickListener(v -> isPremium());
    }

}