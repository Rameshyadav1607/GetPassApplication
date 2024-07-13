package com.indianoil.VisitorManagement.interfaces;

import java.time.LocalTime;

public interface GetPassIn {
    String getId();
    String getVisitor();
    LocalTime getInTime();
    LocalTime getOutTime();
}