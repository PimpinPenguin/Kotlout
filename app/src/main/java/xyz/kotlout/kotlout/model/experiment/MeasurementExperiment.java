package xyz.kotlout.kotlout.model.experiment;

import java.util.List;

import xyz.kotlout.kotlout.model.experiment.trial.MeasurementTrial;

/**
 * A specialization of the Experiment class for experiments involving floating point measurements.
 */
public class MeasurementExperiment extends Experiment {

  private List<MeasurementTrial> trials;

  /**
   * Creates a new BinomialExperiment with basic fields passed on to the base Experiment
   * constructor.
   */
  public MeasurementExperiment(String description, String region, int minimumTrials) {
    super(description, region, minimumTrials);
  }
}
