package com.project.bank1.service.interfaces;

import com.project.bank1.dto.AcquirerDto;
import com.project.bank1.dto.OperationResponse;
import com.project.bank1.model.Acquirer;

public interface AcquirerService {
    AcquirerDto register(AcquirerDto dto);

    Acquirer findByMerchantId(String merchantId);

    AcquirerDto registerQrCode(AcquirerDto dto);

    OperationResponse removeQrCode(String shopId);

    OperationResponse removeBankPayment(String shopId);
}
