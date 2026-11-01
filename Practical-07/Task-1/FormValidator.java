import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FormValidator {

    static List<String> validate(Object obj) throws IllegalAccessException {
        List<String> errors = new ArrayList<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (field.isAnnotationPresent(NotBlank.class)) {
                if (value == null || value.toString().trim().isEmpty()) {
                    errors.add(field.getName() + " must not be blank");
                }
            }
            if (field.isAnnotationPresent(MaxLength.class) && value != null) {
                int max = field.getAnnotation(MaxLength.class).value();
                if (value.toString().length() > max) {
                    errors.add(field.getName() + " must be at most "
                            + max + " characters");
                }
            }
        }
        return errors;
    }

    public static void main(String[] args) throws IllegalAccessException {
        SignupForm good = new SignupForm("riya", "riya@mail.com", "Anand");
        SignupForm bad = new SignupForm("  ", "", "Ahmedabad City");
        SignupForm longName = new SignupForm("verylongusername", "a@b.com", "Surat");

        System.out.println("Good form errors      : " + validate(good));
        System.out.println("Bad form errors       : " + validate(bad));
        System.out.println("Long username errors  : " + validate(longName));
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {
    @NotBlank
    @MaxLength(8)
    String username;

    @NotBlank
    String email;

    @MaxLength(10)
    String city;

    SignupForm(String username, String email, String city) {
        this.username = username;
        this.email = email;
        this.city = city;
    }
}
