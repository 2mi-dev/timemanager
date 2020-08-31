package navy.otter.mapper;

import navy.otter.domain.User;
import navy.otter.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {

  UserModel entityToModel(User entity);

  User modelToEntity(UserModel model);

  void updateEntityFromModel(UserModel model, @MappingTarget User entity);

}
