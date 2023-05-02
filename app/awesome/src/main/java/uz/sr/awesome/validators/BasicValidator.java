package uz.sr.awesome.validators;

import java.util.regex.Matcher;

import uz.sr.awesome.ValidationHolder;
import uz.sr.awesome.utility.ValidationCallback;

public class BasicValidator extends Validator {

    private ValidationCallback mValidationCallback = new ValidationCallback() {
        @Override
        public void execute(ValidationHolder validationHolder, Matcher matcher) {
            validationHolder.getEditText().setError(validationHolder.getErrMsg());
        }
    };

    @Override
    public boolean trigger() {
        return checkFields(mValidationCallback);
    }

    @Override
    public void halt() {
        for (ValidationHolder validationHolder : mValidationHolderList) {
            if (validationHolder.isSomeSortOfView()) {
                validationHolder.resetCustomError();
            } else {
                validationHolder.getEditText().setError(null);
            }
        }
    }

}