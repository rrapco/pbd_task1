package sk.upjs.gursky.pdb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UnclusteredBPTreeSalaryTest {


    private UnclusteredBPTreeSalary bpTreeSalary;

    @Before
    public void setUp() throws Exception {
        bpTreeSalary = UnclusteredBPTreeSalary.createByBulkLoading();
    }

    @After
    public void tearDown() throws Exception {
        bpTreeSalary.close();
        UnclusteredBPTreeSalary.INDEX_FILE.delete();
    }

//    @Test
//    public void test() throws Exception {
//        long time = System.nanoTime();
//        List<SalaryOffsetEntry> result = bpTreeSalary.intervalQuery(new SalaryKey(800), new SalaryKey(1000));
//        time = System.nanoTime() - time;
//
//        System.out.println("Interval unclusetered: " + time/1_000_000.0 +" ms");
//        for (int i = 0; i < 20; i++) {
//            System.out.println(result.get(i));
//        }
//    }


    @Test
    public void test2() throws Exception {
        long time = System.nanoTime();
        List<PersonEntry> result = bpTreeSalary.unclusteredIntervalQuerySalary(new SalaryKey(500), new SalaryKey(1000)); //b999999999
        time = System.nanoTime() - time;

        System.out.println("Interval unclusetered: " + time/1_000_000.0 +" ms");
        for (int i = 0; i < 500; i++) {
            System.out.println(result.get(i));
        }
    }
}
