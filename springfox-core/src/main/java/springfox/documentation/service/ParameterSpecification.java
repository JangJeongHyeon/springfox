package springfox.documentation.service;

import java.util.Optional;

public class ParameterSpecification {
  private final SimpleParameterSpecification query;
  private final ContentSpecification body;

  public ParameterSpecification(
      SimpleParameterSpecification query,
      ContentSpecification body) {
    this.query = query;
    this.body = body;
  }

  public Optional<SimpleParameterSpecification> getQuery() {
    return Optional.ofNullable(query);
  }

  public Optional<ContentSpecification> getBody() {
    return Optional.ofNullable(body);
  }
}
