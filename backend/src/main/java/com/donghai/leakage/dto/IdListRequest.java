package com.donghai.leakage.dto;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class IdListRequest {
    private List<Long> menuIds = new ArrayList<>();
    private List<Long> roleIds = new ArrayList<>();
}
