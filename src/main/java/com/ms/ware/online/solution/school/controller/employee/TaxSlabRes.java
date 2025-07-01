package com.ms.ware.online.solution.school.controller.employee;

public class TaxSlabRes {

    double percentage;
    double couple;
    double individual;

    public TaxSlabRes() {
    }

    public TaxSlabRes(double percentage, double couple, double individual) {
        this.percentage = percentage;
        this.couple = couple;
        this.individual = individual;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getCouple() {
        return couple;
    }

    public void setCouple(double couple) {
        this.couple = couple;
    }

    public double getIndividual() {
        return individual;
    }

    public void setIndividual(double individual) {
        this.individual = individual;
    }

    @Override
    public String toString() {
        return "{percentage=" + percentage + ", couple=" + couple + ", individual=" + individual + "}";
    }
}
