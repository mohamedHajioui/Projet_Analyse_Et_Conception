package excel.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public abstract class Expression{
    // La méthode interpret renvoie un Object
    public abstract Object interpret();

    protected String formatNumber(double number){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##", symbols);
        return df.format(number);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Expression) {
            Expression exp = (Expression) obj;
            return exp.interpret().equals(this.interpret());
        }
        return false;
    }


}

