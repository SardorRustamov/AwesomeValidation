package uz.sr.awesome.validators;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;

import uz.sr.awesome.R;
import uz.sr.awesome.ValidationHolder;
import uz.sr.awesome.utility.ValidationCallback;

public class TextInputLayoutValidator extends Validator {

    private int mErrorTextAppearance = R.style.AwesomeValidation_TextInputLayout;

    public void setErrorTextAppearance(int styleId) {
        mErrorTextAppearance = styleId;
    }

    private ValidationCallback mValidationCallback = new ValidationCallback() {
        @Override
        public void execute(ValidationHolder validationHolder, Matcher matcher) {
            TextInputLayout textInputLayout = validationHolder.getTextInputLayout();
            textInputLayout.setErrorTextAppearance(mErrorTextAppearance);
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(validationHolder.getErrMsg());
        }
    };

    @Override
    public boolean trigger() {
        halt();
        return checkFields(mValidationCallback);
    }

    @Override
    public void halt() {
        for (ValidationHolder validationHolder : mValidationHolderList) {
            if (validationHolder.isSomeSortOfView()) {
                validationHolder.resetCustomError();
            } else {
                TextInputLayout textInputLayout = validationHolder.getTextInputLayout();
                textInputLayout.setErrorEnabled(false);
                textInputLayout.setError(null);
            }
        }
    }

}
