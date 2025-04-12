package excel.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public abstract class Expression{
    // La méthode interpret renvoie un Object
    public abstract Object interpret();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Expression) {
            Expression exp = (Expression) obj;
            return exp.interpret().equals(this.interpret());
        }
        return false;
    }


}

