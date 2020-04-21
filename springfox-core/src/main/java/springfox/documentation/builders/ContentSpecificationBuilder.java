package springfox.documentation.builders;

import springfox.documentation.service.ContentSpecification;
import springfox.documentation.service.MediaType;
import springfox.documentation.service.SimpleParameterSpecification;

import java.util.Set;

public class ContentSpecificationBuilder {
  private final RequestParameterBuilder owner;
  private Set<MediaType> mediaTypes;
  private boolean requestBody = false;

  ContentSpecificationBuilder(RequestParameterBuilder owner) {
    this.owner = owner;
  }

  public ContentSpecificationBuilder mediaTypes(Set<MediaType> mediaTypes) {
    this.mediaTypes = mediaTypes;
    return this;
  }

  public ContentSpecificationBuilder requestBody(boolean requestBody) {
    this.requestBody = requestBody;
    return this;
  }

  ContentSpecification build() {
    return new ContentSpecification(requestBody, mediaTypes);
  }

  public RequestParameterBuilder yield() {
    return owner;
  }

  public SimpleParameterSpecificationBuilder copyOf(SimpleParameterSpecification simpleParameter) {
    return null;
  }
}