package com.mschabow.flyrecipeserver.repository;

import com.mschabow.flyrecipeserver.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <User, String> {
}
