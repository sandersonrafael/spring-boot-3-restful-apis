package com.spring3.firstproject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring3.firstproject.exceptions.UnsupportedMathOperationException;

@RestController
public class MathController {

    @RequestMapping(value = "/sum/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double sum(
        @PathVariable(value = "numberOne") String numberOne,
        @PathVariable(value = "numberTwo") String numberTwo
    ) throws Exception {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please only enter numerical values");
        }

        return convertToDouble(numberOne) + convertToDouble(numberTwo);
    }

    private Double convertToDouble(String strNumber) {
        if (strNumber == null) return 0D;
        // Funcionamento com , ou .
        strNumber = strNumber.replaceAll(",", ".");

        if (isNumeric(strNumber)) return Double.parseDouble(strNumber);
        return 0D;
    }

    private boolean isNumeric(String strNumber) {
        if (strNumber == null) return false;

        strNumber = strNumber.replaceAll(",", ".");

        return strNumber.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
}
