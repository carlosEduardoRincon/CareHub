package com.care.hub.scheduling.entities;

import com.care.hub.scheduling.enums.Speciality;

import java.time.LocalTime;

public class Doctor {
    private String name;
    private String cpf;
    private String crm;
    private LocalTime startWorkTime;
    private LocalTime endWorkTime;
    private Speciality speciality;
}
