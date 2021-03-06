/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spk2fr;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Libra
 */
/*
Parallel Processing
 */
public class Para {

    String format = "";

    final ExecutorService pool;
    final boolean wellTrainOnly;
    final double refracRatio;

    public Para() {
        this(false, 0.0015);
    }

    public Para(boolean wellTrain, double refracRatio) {
        int cpus = Runtime.getRuntime().availableProcessors();
        if (Runtime.getRuntime().maxMemory() / 1024 / 1024 < 8192) {
            cpus = 1;
        }
        System.out.println("cpus " + cpus);
        pool = Executors.newFixedThreadPool(cpus);
        this.wellTrainOnly = wellTrain;
        this.refracRatio = refracRatio;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    synchronized public Future<ComboReturnType> parGetSampleFR(String evtFile, String spkFile,
            String classify, String type, float binStart, float binSize, float binEnd, int[][] sampleSize, int repeats) {
        return pool.submit(new ParSpk2fr(evtFile, spkFile, classify, type, binStart, binSize, binEnd, sampleSize, repeats));
    }

    class ParSpk2fr implements Callable<ComboReturnType> {

        final String evtFile;
        final String spkFile;
        final String spkThreshold;
        final String groupBy;
        final float binStart;
        final float binSize;
        final float binEnd;
        final int[][] sampleSize;
        final int repeats;

        public ParSpk2fr(String evtFile, String spkFile,
                String spkThreshold, String groupBy, float binStart, float binSize, float binEnd, int[][] sampleSize, int repeats) {
            this.evtFile = evtFile;
            this.spkFile = spkFile;
            this.spkThreshold = spkThreshold;
            this.groupBy = groupBy;
            this.binStart = binStart;
            this.binSize = binSize;
            this.binEnd = binEnd;
            this.sampleSize = sampleSize;
            this.repeats = repeats;
        }

        @Override
        public ComboReturnType call() throws Exception {
            Spk2fr s2f = new Spk2fr();
//            s2f.setWellTrainOnly(wellTrainOnly);
            s2f.setRefracRatio(refracRatio);
            s2f.setLeastFR(spkThreshold);

            if (format.toLowerCase().equals("wj")) {
                return s2f.getSampleFringRate(s2f.buildData(
                        MatFile.wellTrainedTrials(MatFile.getFile(evtFile, "TrialInfo"), wellTrainOnly),
                        MatFile.getFile(spkFile, "Spk"),
                        format),
                        groupBy, s2f.setBin(binStart, binSize, binEnd), sampleSize, repeats);
            } else {
                return s2f.getSampleFringRate(s2f.buildData(
                        MatFile.wellTrainedTrials(MatFile.getFile(evtFile, "evts"), wellTrainOnly),
                        MatFile.getFile(spkFile, "data"),
                        format),
                        groupBy, s2f.setBin(binStart, binSize, binEnd), sampleSize, repeats);
            }
        }

    }
}
