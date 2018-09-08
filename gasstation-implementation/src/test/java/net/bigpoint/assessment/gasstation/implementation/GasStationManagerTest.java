package net.bigpoint.assessment.gasstation.implementation;

import java.util.concurrent.TimeUnit;
import net.bigpoint.assessment.gasstation.GasType;
import org.junit.Test;

/**
 *
 * @author limanadamu
 * 
 * Tests for gas station manager
 */
public class GasStationManagerTest extends BaseGasStationManagerTest{
    
    /**
     * Test for setup of gas station , test for prices and number of gas pumps
     */
    @Test
    public void testGasStationSetup(){
        
        assertNotNull(stationManager);
        
        //Test if all gas type have prices 
        assertTrue(stationManager.getPrice(GasType.REGULAR) == REGULAR_FUEL_PRICE);
        assertTrue(stationManager.getPrice(GasType.SUPER) == SUPER_FUEL_PRICE);
        assertTrue(stationManager.getPrice(GasType.DIESEL) == DIESEL_FUEL_PRICE);
        
        
        assertEquals(stationManager.getGasPumps().size(),6);
        
    }
    
    
    @Test
    public void testGasStation() throws InterruptedException{
        
        executorService.execute(new Customer(GasType.REGULAR, 100, REGULAR_FUEL_PRICE,1));
        executorService.execute(new Customer(GasType.SUPER, 100, SUPER_FUEL_PRICE,2));
        executorService.execute(new Customer(GasType.DIESEL, 100, DIESEL_FUEL_PRICE,3));
        
        executorService.shutdown();
        
        executorService.awaitTermination(MAXIMUM_WAITING_TIME, TimeUnit.SECONDS);
        
        assertEquals(stationManager.getNumberOfSales(), 3);
        
    }
    
}
