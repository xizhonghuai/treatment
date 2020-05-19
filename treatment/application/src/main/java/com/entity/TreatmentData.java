package com.entity;

import com.model.AccountInfoDo;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class TreatmentData {

    private String deviceId;
    private String orderId;
    private List<Float> np = new LinkedList<>();
    private List<Float> ec = new LinkedList<>();
    private List<Float> tp= new LinkedList<>();
    private AccountInfoDo accountInfoDo;

}
