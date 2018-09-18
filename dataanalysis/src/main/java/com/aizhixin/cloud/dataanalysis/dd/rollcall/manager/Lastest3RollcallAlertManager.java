package com.aizhixin.cloud.dataanalysis.dd.rollcall.manager;

import com.aizhixin.cloud.dataanalysis.dd.rollcall.respository.Lastest3RollcallAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Lastest3RollcallAlertManager {
    @Autowired
    private Lastest3RollcallAlertRepository lastest3RollcallAlertRepository;
}
