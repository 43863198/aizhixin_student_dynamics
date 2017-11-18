package com.aizhixin.cloud.dataanalysis.setup.respository;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-13
 */

public interface alarmSettingsRepository extends JpaRepository<AlarmSettings, Long> {

}