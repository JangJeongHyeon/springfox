package springfox.documentation.service;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringJoiner;
import java.util.TreeSet;

public class ContentSpecification {
  private final SortedSet<MediaType> mediaTypes = new TreeSet<>(Comparator.comparing(MediaType::getMediaType));
  private final boolean requestBody;

  public ContentSpecification(
      boolean requestBody,
      Set<MediaType> mediaTypes) {
    this.requestBody = requestBody;
    this.mediaTypes.addAll(mediaTypes);
  }

  public SortedSet<MediaType> getMediaTypes() {
    return mediaTypes;
  }

  public boolean isRequestBody() {
    return requestBody;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContentSpecification that = (ContentSpecification) o;
    return mediaTypes.equals(that.mediaTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mediaTypes);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ContentSpecification.class.getSimpleName() + "[", "]")
        .add("mediaTypes=" + mediaTypes)
        .toString();
  }
}
