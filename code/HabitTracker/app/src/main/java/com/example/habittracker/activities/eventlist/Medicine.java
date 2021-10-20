/*
 * Medicine
 *
 * Version 1.0.0
 *
 * Date: 2021-09-25
 *
 * Copyright (c) 2021 Yongquan Zhang.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.example.habittracker.activities.eventlist;

import java.io.Serializable;

/**
 * The Medicine class that contains 5 attributes: name, date, dose amount,
 * dose unit and daily frequency.
 * @author Yongquan Zhang
 */
public class Medicine implements Serializable {
    private String date;
    private String name;
    private Double dose;
    private String doseUnit;
    private int frequency;

    /**
     * The constructor
     * @param date
     * @param name
     * @param dose
     * @param doseUnit
     * @param frequency
     */
    public Medicine(String date, String name, Double dose, String doseUnit, int frequency){
        this.date = date;
        this.name = name;
        this.dose = dose;
        this.doseUnit = doseUnit;
        this.frequency = frequency;
    }

    /**
     * Setter for medicine name
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Getter for medicine name
     * @return
     */
    public String getName(){
        return this.name;
    }

    /**
     * Setter for date
     * @param date
     */
    public void setDate(String date){
        this.date = date;
    }

    /**
     * Getter for date
     * @return
     */
    public String getDate(){
        return this.date;
    }

    /**
     * Setter for dose amount
     * @param dose
     */
    public void setDose(double dose){
        this.dose = dose;
    }

    /**
     * Getter for dose amount
     * @return
     */
    public double getDose(){
        return this.dose;
    }

    /**
     * Setter for dose unit
     * @param unit
     */
    public void setDoseUnit(String unit){
        this.doseUnit = unit;
    }

    /**
     * Getter for dose unit
     * @return
     */
    public String getDoseUnit(){
        return this.doseUnit;
    }

    /**
     * Setter for daily frequency
     * @param freq
     */
    public void setFreq(int freq){
        this.frequency = freq;
    }

    /**
     * Getter for daily frequency
     * @return
     */
    public int getFrequency(){
        return this.frequency;
    }
}