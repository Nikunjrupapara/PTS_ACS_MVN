package com.yesmarketing.ptsacs.services.configuration;

import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmarketing.ptsacs.services.authentication.FormUserDetails;
import com.yesmarketing.ptsacs.services.authentication.ServicesFormConfigAuthentication;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.util.FormConfigTestHelper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockFormConfigSecurityContextFactory implements WithSecurityContextFactory<WithMockFormConfig> {
    @Override
    public SecurityContext createSecurityContext(WithMockFormConfig withMockFormConfig) {
        String company = withMockFormConfig.company();
        String code = withMockFormConfig.code();
        FormConfig formConfig = FormConfigTestHelper.getByCompanyAndCode(company, code)
                .orElseThrow(() -> new ObjectNotFoundException(company, code));
        FormUserDetails formUserDetails = new FormUserDetails(formConfig);
        ServicesFormConfigAuthentication auth = new ServicesFormConfigAuthentication(formUserDetails);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}
