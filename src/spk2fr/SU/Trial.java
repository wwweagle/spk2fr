/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spk2fr.SU;

import java.util.ArrayList;
import spk2fr.EventType;

/**
 *
 * @author Libra
 */
public class Trial {

    private EventType firstOdor; // 0 for Q, 1 for R
    private EventType secondOdor; //^^
    private EventType response;// 0=hit,1=miss;2=cr,3=false;
    private boolean isSet = false;
    ArrayList<Double> spks = new ArrayList<>();
    double trialLength;

    double[] fr100msBin;

    public void addSpk(double spkTimeStamp) {
        spks.add(spkTimeStamp);
    }

    public boolean isSet() {
        return isSet;
    }

    public void setTrialParameter(EventType firstOdor, EventType secondOdor, EventType response, double trialLength) {
        this.trialLength = trialLength;
        if ((firstOdor != EventType.OdorA && firstOdor != EventType.OdorB)
                || (secondOdor != EventType.OdorA && secondOdor != EventType.OdorB)) {
            System.out.println("Error Trial Parameter");
        }
        this.firstOdor = firstOdor;
        this.secondOdor = secondOdor;

        this.response = response;

        isSet = true;
    }

    public boolean firstOdorIs(EventType odor) {
        return this.firstOdor == odor;
    }

    public boolean secondOdorIs(EventType odor) {
        return this.secondOdor == odor;
    }

//    4 removal  
//    public Double[] getSpikes(){
//        return spks.toArray(new Double[spks.size()]);
//    }
    public ArrayList<Double> getSpikesList() {
        return spks;
    }

    public double getLength() {
        return trialLength;
    }

    public boolean isCorrect() {
        return response == EventType.CorrectRejection || response == EventType.Hit;
    }
    
    public boolean isMatch(){
        return firstOdor==secondOdor;
    }
}