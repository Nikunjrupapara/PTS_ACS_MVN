package com.yesmarketing.ptsacs.services.dto;

import com.yesmarketing.acsapi.model.Email;

public class EmailDto extends Email {

    public String eventId;

    @Override
    public String getEmail(){
        return (email==null)?null:email.toLowerCase();
    }

}
