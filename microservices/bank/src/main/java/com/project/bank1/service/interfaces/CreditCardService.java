package com.project.bank1.service.interfaces;

import com.project.bank1.dto.AcquirerResponseDto;
import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.dto.RequestDto;

public interface CreditCardService {
    RequestDto validateAcquirer(OnboardingRequestDto dto) throws Exception;

    AcquirerResponseDto startPayment(OnboardingRequestDto dto) throws Exception;

}
