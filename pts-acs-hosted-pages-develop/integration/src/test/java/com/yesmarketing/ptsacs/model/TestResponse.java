package com.yesmarketing.ptsacs.model;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;
import org.apache.http.Header;

@Value
@AllArgsConstructor
public class TestResponse {

    @ToString.Include(rank = 3)
    int statusCode;

    @ToString.Include(rank = 2)
    String body;

    @ToString.Include(rank = 1)
    Header[] headers;
}
