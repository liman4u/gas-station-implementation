package net.bigpoint.assessment.gasstation.implementation;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;
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
    
    /**
     * Test for buying gas with 3 simulated customers
     * 
     * @throws InterruptedException 
     */
    @Test
    public void testGasStation() throws InterruptedException{
        
        executorService.execute(new Customer(GasType.REGULAR, 100, REGULAR_FUEL_PRICE,1));
        executorService.execute(new Customer(GasType.SUPER, 100, SUPER_FUEL_PRICE,2));
        executorService.execute(new Customer(GasType.DIESEL, 100, DIESEL_FUEL_PRICE,3));
        
        executorService.shutdown();
        
        executorService.awaitTermination(MAXIMUM_WAITING_TIME, TimeUnit.SECONDS);
        
        assertEquals(stationManager.getNumberOfSales(), 3);
        
    }
    
    /**
     * Test for not enough gas exception
     * 
     * @throws InterruptedException 
     */
    @Test(expected = NotEnoughGasException.class)
    public void testNotEnoughGasException() throws InterruptedException{
        executorService.execute(new Customer(GasType.REGULAR, SUPER_FUEL_LITRES, REGULAR_FUEL_PRICE,1));
        
        executorService.shutdown();
        
        executorService.awaitTermination(MAXIMUM_WAITING_TIME, TimeUnit.SECONDS);
        
    }
    
    /**
     * Test for gas too expensive exception
     * 
     * @throws InterruptedException 
     */
    @Test(expected = GasTooExpensiveException.class)
    public void testGasTooExpensiveException() throws InterruptedException{
        executorService.execute(new Customer(GasType.REGULAR, SUPER_FUEL_LITRES, 0,1));
        
        executorService.shutdown();
        
        executorService.awaitTermination(MAXIMUM_WAITING_TIME, TimeUnit.SECONDS);
        
    }
    
     /**
     * Test for invalid gas type
     * 
     * @throws InterruptedException 
     */
    @Test(expected = InvalidParameterException.class)
    public void testInvalidGasType() throws InterruptedException{
        executorService.execute(new Customer(null, REGULAR_FUEL_LITRES, REGULAR_FUEL_PRICE,1));
        
        executorService.shutdown();
        
        executorService.awaitTermination(MAXIMUM_WAITING_TIME, TimeUnit.SECONDS);
        
    }
    
    
     /**
     * Test for invalid amount in litres
     * 
     * @throws InterruptedException 
     */
    @Test(expected = InvalidParameterException.class)
    public void testInvalidAmountInLitres() throws InterruptedException{
        executorService.execute(new Customer(GasType.REGULAR, 0, REGULAR_FUEL_PRICE,1));
        
        executorService.shutdown();
        
        executorService.awaitTermination(MAXIMUM_WAITING_TIME, TimeUnit.SECONDS);
        
    }
    
    
}
