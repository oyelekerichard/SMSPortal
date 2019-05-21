package com.crowninteractive.smsportal.ussd;

import com.crowninteractive.smsportal.dto.Data;
import com.crowninteractive.smsportal.dto.MtechRequest;
import com.crowninteractive.smsportal.dto.MtechResponse;

public interface ShortCodeInterface {

    public String process(Data data);

    public MtechResponse process(MtechRequest request);

    @Override
    public String toString();
}
