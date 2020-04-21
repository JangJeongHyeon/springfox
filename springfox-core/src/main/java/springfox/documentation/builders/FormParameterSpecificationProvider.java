package springfox.documentation.builders;

import org.springframework.http.MediaType;
import springfox.documentation.service.CollectionFormat;
import springfox.documentation.service.ContentSpecification;
import springfox.documentation.service.ParameterSpecification;
import springfox.documentation.service.ParameterStyle;
import springfox.documentation.service.SimpleParameterSpecification;

import java.util.Arrays;
import java.util.List;

public class FormParameterSpecificationProvider implements ParameterSpecificationProvider {
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
    ContentSpecification contentParameter = context.getContentParameter();

    ContentSpecification renderedContent = null;

    for (MediaType each : context.getAccepts()) {
      if (each == MediaType.APPLICATION_FORM_URLENCODED) {
        
      } else if (each == MediaType.MULTIPART_FORM_DATA) {

      } else {

      }
      if (simpleParameter.getModel().getScalar().isPresent()) {
        context.getSimpleParameterSpecificationBuilder()
            .copyOf(simpleParameter)
            .explode(null)
            .style(ParameterStyle.FORM);
      } else if (simpleParameter.getModel().getCollection().isPresent()) {
        ParameterStyle style = VALID_COLLECTION_STYLES.contains(simpleParameter.getStyle())
            ? simpleParameter.getStyle()
            : simpleParameter.getExplode() ? ParameterStyle.FORM : ParameterStyle.PIPEDELIMITED;
        context.getSimpleParameterSpecificationBuilder()
            .copyOf(simpleParameter)
            .explode(simpleParameter.getExplode())
            .style(style)
            .collectionFormat(simpleParameter.getExplode()
                ? CollectionFormat.MULTI
                : CollectionFormat.CSV);
      } else {
        ParameterStyle style = VALID_OBJECT_STYLES.contains(simpleParameter.getStyle())
            ? simpleParameter.getStyle()
            : simpleParameter.getExplode() ? ParameterStyle.FORM : ParameterStyle.DEEPOBJECT;
        context.getSimpleParameterSpecificationBuilder()
            .copyOf(simpleParameter)
            .explode(style == ParameterStyle.DEEPOBJECT ? true : simpleParameter.getExplode())
            .style(style)
            .collectionFormat(null);
      }
    }


    return new ParameterSpecification(
        null,
        renderedContent);
  }
}
