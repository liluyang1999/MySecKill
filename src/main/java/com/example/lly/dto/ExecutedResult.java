package com.example.lly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutedResult {

    private long seckillInfoId;

    private int state;

    private String stateInfo;


}
