/**
 * ImpoundLotTest ensures the correctness of ImpoundLot.
 */
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.eviltowtruck.CantParkException;
import com.eviltowtruck.CarNotFoundException;
import com.eviltowtruck.ImpoundLot;
import junit.framework.TestCase;

public class ImpoundLotTest extends TestCase {

    protected final double PARKING_FEE = 100.0;
    protected ImpoundLot smallLot = new ImpoundLot(2, PARKING_FEE);
    protected ImpoundLot mediumLot = new ImpoundLot(6, PARKING_FEE);

    protected Object car() {
        return new Object();
    }

    public void testTowToLotWithAvailableSpace() throws Exception {
        assertTrue(smallLot.ableToPark());
        smallLot.impound(car());
        assertTrue(smallLot.ableToPark());
    }

    public void testShouldNotParkInFullLot() throws Exception {
        smallLot.impound(car());
        smallLot.impound(car());
        assertFalse(smallLot.ableToPark());

        try {
            smallLot.impound(car());
            fail("Expected CantParkException.");
        }
        catch (CantParkException expected) {}
    }

    public void testRetrieveParkedCar() throws Exception {
        smallLot.impound(car());
        Object car = car();
        smallLot.impound(car);
        smallLot.retrieve(car);
        assertTrue(smallLot.ableToPark());
    }

    public void testShouldNotRetrieveCarNotInLot() throws Exception {
        smallLot.impound(car());
        smallLot.impound(car());
        try {
            smallLot.retrieve(car());
            fail("Expected CarNotFoundException.");
        }
        catch (CarNotFoundException expected) {}
    }

    public void testComparatorShouldReturnLeastPercentFull() {

        Comparator UTILIZATION = new Comparator() {
            public int compare(Object left, Object right) {
                return ((ImpoundLot)left).howFull() - ((ImpoundLot)right).howFull();
            }
        };

        List lots = Arrays.asList(new ImpoundLot[] {smallLot, mediumLot});
        smallLot.impound(car());
        mediumLot.impound(car());
        ImpoundLot smaller = (ImpoundLot)Collections.min(lots, UTILIZATION);
        assertEquals(50, smallLot.howFull());
        assertEquals(16, mediumLot.howFull());
        assertEquals(mediumLot, smaller);
    }
}
