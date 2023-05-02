package uz.sr.mylibrary.utility;

import java.util.regex.Matcher;

import uz.sr.mylibrary.ValidationHolder;

public interface ValidationCallback {

    void execute(ValidationHolder validationHolder, Matcher matcher);

}