package com.yesmarketing.ptsacs.services.model;

import com.yesmarketing.ptsacs.services.enums.HashingFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.List;

/*
    trancore sample

        fieldPath = email, acct14, partner
        delimiter = |
        function = SHA512

    ymnewsolutions

        fieldPath = profile.email
        delimiter = NULL
        function = NULL

*/


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerIdDefinition {

    @Id
    String company;

    List<CustomerIdField> fields;

    String delimiter;

    HashingFunction function;
}
