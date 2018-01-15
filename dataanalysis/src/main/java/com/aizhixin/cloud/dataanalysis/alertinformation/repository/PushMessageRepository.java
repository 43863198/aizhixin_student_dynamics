package com.aizhixin.cloud.dataanalysis.alertinformation.repository;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.PushMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PushMessageRepository extends
        JpaRepository<PushMessage, Long> {

    List<PushMessage> findAllByModuleAndFunctionAndUserIdAndHaveRead(
            String module, String function, Long userId, Boolean false1);

    Page<PushMessage> findAllByModuleAndFunctionAndUserIdAndDeleteFlag(
            String module, String function, Long userId, Integer deleteFlag,
            Pageable pageable);

    @Modifying
    @Query("update PushMessage p set p.deleteFlag = ?2 where p.id = ?1 ")
    int deleteMessage(long id, Integer deleteFlag);

}
