package com.aizhixin.cloud.dataanalysis.alertinformation.respository;



import org.springframework.data.repository.PagingAndSortingRepository;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.RegistrationAlertInformation;

public interface RegistrationAlertInforRespository extends PagingAndSortingRepository<RegistrationAlertInformation, Long> {
	
}
