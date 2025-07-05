package utils;

public class PasswordValidator {
    private static final int MIN_LENGTH = 8;
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public static boolean isValid(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            return false;
        }

        boolean hasDigit = false;
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isUpperCase(c)) hasUpper = true;
            if ("@#$%^&+=".indexOf(c) >= 0) hasSpecial = true;
        }

        return hasDigit && hasLower && hasUpper && hasSpecial;
    }

    public static String getPasswordRequirements() {
        return "Password must:\n" +
               "- Be at least 8 characters long\n" +
               "- Contain at least one digit\n" +
               "- Contain at least one lowercase letter\n" +
               "- Contain at least one uppercase letter\n" +
               "- Contain at least one special character (@#$%^&+=)";
    }
}
