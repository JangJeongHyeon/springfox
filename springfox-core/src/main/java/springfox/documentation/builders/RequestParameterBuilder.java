package springfox.documentation.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import springfox.documentation.schema.ElementFacet;
import springfox.documentation.schema.Example;
import springfox.documentation.service.ParameterSpecification;
import springfox.documentation.service.ParameterStyle;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.VendorExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RequestParameterBuilder {
  private static final Logger LOGGER = LoggerFactory.getLogger(RequestParameterBuilder.class);
  private String name;
  private ParameterType in;
  private String description;
  private Boolean required = false;
  private Boolean deprecated;
  private Boolean hidden = false;
  private Example scalarExample;
  private final List<Example> examples = new ArrayList<>();
  private final List<VendorExtension> extensions = new ArrayList<>();
  private ParameterSpecificationProvider parameterSpecificationProvider = new RootParameterSpecificationProvider();
  private SimpleParameterSpecificationBuilder simpleParameterBuilder;
  private ContentSpecificationBuilder contentSpecificationBuilder;
  private int order;
  private List<MediaType> accepts = new ArrayList<>();

  public RequestParameterBuilder name(String name) {
    this.name = name;
    return this;
  }

  public RequestParameterBuilder in(ParameterType in) {
    this.in = in;
    if (in == ParameterType.QUERY || in == ParameterType.COOKIE) {
      this.simpleParameterBuilder()
          .style(ParameterStyle.FORM)
          .allowReserved(in == ParameterType.QUERY);
    } else if (in == ParameterType.HEADER || in == ParameterType.PATH) {
      this.simpleParameterBuilder()
          .style(ParameterStyle.SIMPLE)
          .allowReserved(false);
    }
    return this;
  }

  public RequestParameterBuilder in(String in) {
    this.in(ParameterType.from(in));
    return this;
  }

  public RequestParameterBuilder description(String description) {
    this.description = description;
    return this;
  }

  public RequestParameterBuilder required(Boolean required) {
    this.required = required;
    return this;
  }

  public RequestParameterBuilder deprecated(Boolean deprecated) {
    this.deprecated = deprecated;
    return this;
  }

  public SimpleParameterSpecificationBuilder simpleParameterBuilder() {
    if (simpleParameterBuilder == null) {
      simpleParameterBuilder = new SimpleParameterSpecificationBuilder(this);
    }
    return simpleParameterBuilder;
  }

  public ContentSpecificationBuilder contentSpecificationBuilder() {
    if (contentSpecificationBuilder == null) {
      contentSpecificationBuilder = new ContentSpecificationBuilder(this);
    }
    return contentSpecificationBuilder;
  }

  public RequestParameterBuilder extensions(List<VendorExtension> extensions) {
    this.extensions.addAll(extensions);
    return this;
  }

  public RequestParameterBuilder hidden(boolean hidden) {
    this.hidden = hidden;
    return this;
  }

  public RequestParameterBuilder order(int order) {
    this.order = order;
    return this;
  }


  public RequestParameterBuilder example(Example scalarExample) {
    this.scalarExample = scalarExample;
    return this;
  }

  public RequestParameterBuilder order(Collection<Example> examples) {
    this.examples.addAll(examples);
    return this;
  }

  public RequestParameterBuilder parameterSpecificationProvider(ParameterSpecificationProvider specificationProvider) {
    this.parameterSpecificationProvider = specificationProvider;
    return this;
  }

  public RequestParameterBuilder accepts(Set<? extends MediaType> accepts) {
    this.accepts.addAll(accepts);
    return this;
  }

  public RequestParameter build() {
    if (simpleParameterBuilder != null && contentSpecificationBuilder != null) {
      throw new IllegalStateException("Parameter can be either a simple parameter or content, but not both");
    }
//    if (simpleParameterBuilder == null && contentSpecificationBuilder != null) {
//      parameter = new ParameterSpecification(null, contentSpecificationBuilder.build());
//    } else if (simpleParameterBuilder != null) {
//      parameter = new ParameterSpecification(simpleParameterBuilder.build(), null);
//    } else {
//      LOGGER.warn("Parameter has not been initialized to be either simple nor a content parameter");
//    }
    ParameterSpecification parameter = parameterSpecificationProvider.create(
        new ParameterSpecificationContext(
            in,
            accepts,
            simpleParameterBuilder != null ? simpleParameterBuilder.build() : null,
            contentSpecificationBuilder != null ? contentSpecificationBuilder.build() : null,
            new SimpleParameterSpecificationBuilder(this),
            new ContentSpecificationBuilder(this)));

    return new RequestParameter(
        name,
        in,
        description,
        required,
        deprecated,
        hidden,
        parameter,
        scalarExample,
        examples,
        order,
        extensions);
  }

  public RequestParameterBuilder from(RequestParameter source) {
    source.getParameterSpecification()
        .getQuery()
        .map(simple -> {
          for (ElementFacet each :
              simple.getFacets()) {
            this.simpleParameterBuilder()
                .facetBuilder(each.facetBuilder())
                .copyOf(each);
          }
          this.simpleParameterBuilder()
              .collectionFormat(simple.getCollectionFormat())
              .allowEmptyValue(simple.getAllowEmptyValue())
              .allowReserved(simple.getAllowReserved())
              .defaultValue(simple.getDefaultValue())
              .explode(simple.getExplode())
              .model(simple.getModel())
              .style(simple.getStyle());
          return simple;
        });
    source.getParameterSpecification()
        .getBody()
        .map(content -> this.contentSpecificationBuilder()
            .mediaTypes(content.getMediaTypes()));
    return this.in(source.getIn())
        .required(source.getRequired())
        .hidden(source.getHidden())
        .deprecated(source.getDeprecated())
        .extensions(source.getExtensions())
        .name(source.getName())
        .description(source.getDescription())
        .order(source.getOrder());
  }
}