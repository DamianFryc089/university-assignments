package pl.edu.pwr.student.damian_fryc.lab4_client;

import java.util.Objects;

public class DataFilter {
    public static boolean applyFilter(Object value, String filter, String comparisonValue) {
        if (value == null || filter == null || comparisonValue == null) {
            return false;
        }

        // Integers
        if (value instanceof Double) {
            double floatValue = (Double) value;
            double compareValue;
            try{
                compareValue = Double.parseDouble(comparisonValue);
            }catch (NumberFormatException | NullPointerException ignored) {return false;}

            switch (filter) {
                case "==": return floatValue == compareValue;
                case "!=": return floatValue != compareValue;
                case ">": return floatValue > compareValue;
                case "<": return floatValue < compareValue;
                case ">=": return floatValue >= compareValue;
                case "<=": return floatValue <= compareValue;
                default: return false;
            }
        }

        // Integers
        if (value instanceof Integer intValue) {
            Integer compareValue;
            try{
                compareValue = Integer.valueOf(comparisonValue);
            }catch (NumberFormatException | NullPointerException ignored) {return false;}

            switch (filter) {
                case "==": return Objects.equals(intValue, compareValue);
                case "!=": return !Objects.equals(intValue, compareValue);
                case ">": return intValue > compareValue;
                case "<": return intValue < compareValue;
                case ">=": return intValue >= compareValue;
                case "<=": return intValue <= compareValue;
                default: return false;
            }
        }

        // Strings
        if (value instanceof String strValue) {
            boolean caseInsensitive = filter.contains("*");
            switch (filter) {
                case "==","==*":
                    return caseInsensitive ? strValue.equalsIgnoreCase(comparisonValue) : strValue.equals(comparisonValue);
                case "!=", "!=*":
                    return caseInsensitive ? !strValue.equalsIgnoreCase(comparisonValue) : !strValue.equals(comparisonValue);
                case ">", "<", ">=", "<=":
                    // no string comparisons
                    return false;
                case "@=", "@=*":
                    return caseInsensitive ? strValue.toLowerCase().contains(comparisonValue.toLowerCase()) : strValue.contains(comparisonValue);
                case "_=", "_=*":
                    return caseInsensitive ? strValue.toLowerCase().startsWith(comparisonValue.toLowerCase()) : strValue.startsWith(comparisonValue);
                case "!@=", "!@=*":
                    return caseInsensitive ? !strValue.toLowerCase().contains(comparisonValue.toLowerCase()) : !strValue.contains(comparisonValue);
                case "!_=", "!_=*":
                    return caseInsensitive ? !strValue.toLowerCase().startsWith(comparisonValue.toLowerCase()) : !strValue.startsWith(comparisonValue);
                default:
                    return false;
            }
        }

        return false;
    }
}
