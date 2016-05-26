package lte1800ConfigGenerator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestInputReader.class, TestLteSite.class, TestXmlCreator.class, TestAllLteSites.class,
		TestAllConfigFiles.class })

public class TestMaster {

}
