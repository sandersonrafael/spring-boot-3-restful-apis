package com.spring3.firstproject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring3.firstproject.exceptions.UnsupportedMathOperationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

    @GetMapping(value="/sub/{numberOne}/{numberTwo}")
    public Double sub(@PathVariable String numberOne, @PathVariable String numberTwo) {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please only enter numerical values");
        }

        return convertToDouble(numberOne) - convertToDouble(numberTwo);
    }

    @GetMapping(value="/mult/{numberOne}/{numberTwo}")
    public Double mult(@PathVariable String numberOne, @PathVariable String numberTwo) {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please only enter numerical values");
        }

        return convertToDouble(numberOne) * convertToDouble(numberTwo);
    }

    @GetMapping(value="/div/{numberOne}/{numberTwo}")
    public Double div(@PathVariable String numberOne, @PathVariable String numberTwo) {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please only enter numerical values");
        }

        return convertToDouble(numberOne) / convertToDouble(numberTwo);
    }

    @GetMapping(value="/average/{numberOne}/{numberTwo}")
    public Double average(@PathVariable String numberOne, @PathVariable String numberTwo) {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please only enter numerical values");
        }

        return (convertToDouble(numberOne) + convertToDouble(numberTwo)) / 2;
    }

    @GetMapping(value="/sqrt/{number}")
    public Double sqrt(@PathVariable String number) {
        if (!isNumeric(number)) {
            throw new UnsupportedMathOperationException("Please only enter numerical values");
        }

        return Math.sqrt(convertToDouble(number));
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
