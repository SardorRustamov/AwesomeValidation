package uz.sr.awesome.utility;

import java.util.regex.Matcher;

import uz.sr.awesome.ValidationHolder;

public interface ValidationCallback {

    void execute(ValidationHolder validationHolder, Matcher matcher);

}