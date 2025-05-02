package com.coop.global.repository;

import com.coop.domain.room.entity.RoomInfo;
import org.springframework.data.repository.CrudRepository;

public interface RoomRedisRepository extends CrudRepository<RoomInfo, Long> {
}
