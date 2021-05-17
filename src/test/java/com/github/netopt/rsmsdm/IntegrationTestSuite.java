package com.github.netopt.rsmsdm;

import com.github.netopt.rsmsdm.experiment.ExperimentPropertiesParserIT;
import com.github.netopt.rsmsdm.experiment.ScenarioIT;
import com.github.netopt.rsmsdm.network.CandidatePathSlotsIT;
import com.github.netopt.rsmsdm.network.DemandsFactoryIT;
import com.github.netopt.rsmsdm.network.GraphIT;
import com.github.netopt.rsmsdm.network.NetworkCandidatePathsIT;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author plechowicz
 * created on 23.02.19.
 */
@Suite.SuiteClasses({
		GraphIT.class,
		CandidatePathSlotsIT.class,
		NetworkCandidatePathsIT.class,
		DemandsFactoryIT.class,
		ExperimentPropertiesParserIT.class,
		ScenarioIT.class
})
@RunWith(Suite.class)
public class IntegrationTestSuite {
}
