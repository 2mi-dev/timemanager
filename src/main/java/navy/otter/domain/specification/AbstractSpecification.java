package navy.otter.domain.specification;

import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.lowerCase;

import org.springframework.data.jpa.domain.Specification;

public abstract class AbstractSpecification<T> implements Specification<T> {

  public static final String WILDCARD = "%";

  public String wildcards(String str) {
    return join(WILDCARD, str, WILDCARD);
  }

  public String wildcardEnd(String str) {
    return join(str, WILDCARD);
  }

  public String wildcardsAndLower(String str) {
    return wildcards(lowerCase(str));
  }

  public String wildcardEndAndLower(String str) {
    return wildcardEnd(lowerCase(str));
  }

}

