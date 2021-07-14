package com.gw.entities;


import lombok.Data;

import java.io.Serializable;

@Data
public class TaskClaim implements Serializable {

    private long id;

    private Long welderId;

    private Long taskId;

    private Long weldId;

    private String claimTime;


}
