package navy.otter.domain.specification;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import navy.otter.domain.User;
import navy.otter.domain.User_;
import navy.otter.domain.criteria.UserCriteria;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification extends AbstractSpecification<User> {

  private final UserCriteria criteria;

  public UserSpecification(UserCriteria criteria) {
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    Predicate predicate = cb.conjunction();
    List<Expression<Boolean>> expressions = predicate.getExpressions();

    if (isNotBlank(criteria.getFilter())) {
      expressions.add(cb.or(
        cb.like(cb.lower(root.get(User_.username)),
          wildcardsAndLower(criteria.getFilter())),
        cb.like(cb.lower(root.get(User_.nickname)),
          wildcardsAndLower(criteria.getFilter())),
        cb.like(cb.lower(root.get(User_.name)),
          wildcardsAndLower(criteria.getFilter())),
        cb.like(cb.lower(root.get(User_.email)), wildcardsAndLower(criteria.getFilter())),
        cb.like(cb.lower(root.get(User_.phone)),
          wildcardsAndLower(criteria.getFilter()))));
    }

    if (isNotBlank(criteria.getUsername())) {
      expressions.add(cb.like(cb.lower(root.get(User_.username)),
        wildcardsAndLower(criteria.getUsername())));
    }
    if (isNotBlank(criteria.getNickname())) {
      expressions.add(cb.like(cb.lower(root.get(User_.nickname)),
        wildcardsAndLower(criteria.getNickname())));
    }
    if (isNotBlank(criteria.getName())) {
      expressions.add(cb.like(cb.lower(root.get(User_.name)),
        wildcardsAndLower(criteria.getName())));
    }
    if (isNotBlank(criteria.getEmail())) {
      expressions.add(
        cb.like(cb.lower(root.get(User_.email)), wildcardsAndLower(criteria.getEmail())));
    }
    if (isNotBlank(criteria.getAuthority())) {
      expressions.add(cb.isMember(criteria.getAuthority(), root.get(User_.authorities)));
    }
    if (null != criteria.getActivated()) {
      expressions.add(cb.equal(root.get(User_.activated), criteria.getActivated()));
    }
    if (null != criteria.getEnabled()) {
      expressions.add(cb.equal(root.get(User_.enabled), criteria.getEnabled()));
    }
    return predicate;

  }

}
