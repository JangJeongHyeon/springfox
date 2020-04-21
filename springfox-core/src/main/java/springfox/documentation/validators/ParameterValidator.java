package springfox.documentation.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springfox.documentation.builders.ParameterSpecificationContext;
import springfox.documentation.service.ParameterType;

public class ParameterValidator implements Validator {
  @Override
  public boolean supports(Class<?> clazz) {
    return ParameterSpecificationContext.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ParameterSpecificationContext context = (ParameterSpecificationContext) target;

  }
}
