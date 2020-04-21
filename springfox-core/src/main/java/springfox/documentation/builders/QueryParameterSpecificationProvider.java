package springfox.documentation.builders;

import springfox.documentation.service.CollectionFormat;
import springfox.documentation.service.ContentSpecification;
import springfox.documentation.service.ParameterSpecification;
import springfox.documentation.service.ParameterStyle;
import springfox.documentation.service.SimpleParameterSpecification;

import java.util.Arrays;
import java.util.List;

class QueryParameterSpecificationProvider implements ParameterSpecificationProvider {
  public static final List<ParameterStyle> VALID_COLLECTION_STYLES =
      Arrays.asList(
          ParameterStyle.FORM,
          ParameterStyle.SPACEDELIMITED,
          ParameterStyle.PIPEDELIMITED
      );
  public static final List<ParameterStyle> VALID_OBJECT_STYLES =
      Arrays.asList(
          ParameterStyle.FORM,
          ParameterStyle.DEEPOBJECT
      );

  @Override
  public ParameterSpecification create(ParameterSpecificationContext context) {
    SimpleParameterSpecification simpleParameter = context.getSimpleParameter();
    SimpleParameterSpecification validSimpleParameter = null;
    ContentSpecification validContentEncoding = null;
    if (simpleParameter.getModel().getScalar().isPresent()) {
      validSimpleParameter = context.getSimpleParameterSpecificationBuilder()
          .copyOf(simpleParameter)
          .explode(null)
          .style(ParameterStyle.FORM)
          .build();
    } else if (simpleParameter.getModel().getCollection().isPresent()) {
      ParameterStyle style = VALID_COLLECTION_STYLES.contains(simpleParameter.getStyle())
          ? simpleParameter.getStyle()
          : simpleParameter.getExplode() ? ParameterStyle.FORM : ParameterStyle.PIPEDELIMITED;
      validSimpleParameter = context.getSimpleParameterSpecificationBuilder()
          .copyOf(simpleParameter)
          .explode(simpleParameter.getExplode())
          .style(style)
          .collectionFormat(simpleParameter.getExplode()
              ? CollectionFormat.MULTI
              : CollectionFormat.CSV)
          .build();
    } else if (context.getContentParameter() != null) {
      validContentEncoding = context.getContentSpecificationBuilder()
          .requestBody(false)
          .build();
    } else {
      ParameterStyle style = VALID_OBJECT_STYLES.contains(simpleParameter.getStyle())
          ? simpleParameter.getStyle()
          : simpleParameter.getExplode() ? ParameterStyle.FORM : ParameterStyle.DEEPOBJECT;
      validSimpleParameter = context.getSimpleParameterSpecificationBuilder()
          .copyOf(simpleParameter)
          .explode(style == ParameterStyle.DEEPOBJECT ? true : simpleParameter.getExplode())
          .style(style)
          .collectionFormat(null)
          .build();
    }
    return new ParameterSpecification(
        validSimpleParameter,
        validContentEncoding);
  }
}
