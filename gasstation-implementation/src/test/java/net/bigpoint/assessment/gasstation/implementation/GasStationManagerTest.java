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
     * Test for buying gas with 3 simulated customers
     * 
     * @throws InterruptedException 
     */
    @Test
    public void testGasStation() throws InterruptedException{
        
        double amountInLitres = 100.0;
        
        executorService.execute(new Customer(GasType.REGULAR, amountInLitres, REGULAR_FUEL_PRICE,1));
        executorService.execute(new Customer(GasType.SUPER, amountInLitres, SUPER_FUEL_PRICE,2));
        executorService.execute(new Customer(GasType.DIESEL, amountInLitres, DIESEL_FUEL_PRICE,3));
        
        executorService.shutdown();
        
        executorService.awaitTermination(MAXIMUM_WAITING_TIME, TimeUnit.SECONDS);
        
        assertEquals(stationManager.getNumberOfSales(), 3);
        
        assertEquals(stationManager.getRevenue(), (amountInLitres * REGULAR_FUEL_PRICE) + (amountInLitres * SUPER_FUEL_PRICE) + (amountInLitres * DIESEL_FUEL_PRICE));
        
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
    
    /**
     * Test for invalid maximum price per litre
     * 
     * @throws InterruptedException 
     */
    @Test(expected = InvalidParameterException.class)
    public void testInvalidMaximumPricePerLitre() throws InterruptedException{
        executorService.execute(new Customer(GasType.REGULAR, REGULAR_FUEL_LITRES, -1,1));
        
        executorService.shutdown();
        
        executorService.awaitTermination(MAXIMUM_WAITING_TIME, TimeUnit.SECONDS);
        
    }
    
    /**
     * Test for buying gas concurrently on multiple gas pump
     * 
     * @throws InterruptedException 
     */
    @Test
    public void testConcurrentSaleofMultipleGasPump() throws InterruptedException{
        
        double amountInLitres = 100.0;
         
        executorService.execute(new Customer(GasType.REGULAR, amountInLitres, REGULAR_FUEL_PRICE,1));
        executorService.execute(new Customer(GasType.REGULAR, amountInLitres, REGULAR_FUEL_PRICE,2));
        executorService.execute(new Customer(GasType.REGULAR, amountInLitres, REGULAR_FUEL_PRICE,3));
        
        
        executorService.shutdown();
        
        executorService.awaitTermination(MAXIMUM_WAITING_TIME, TimeUnit.SECONDS);
        
        assertEquals(stationManager.getNumberOfSales(), 3);
        
        assertEquals(stationManager.getRevenue(), 3*(amountInLitres * REGULAR_FUEL_PRICE) );
        
    }
    
    
    
    /**
     * Test for non whole number amount in litres
     * 
     * @throws InterruptedException 
     */
    @Test
    public void testNonWholeNumberAmountInLitres() throws InterruptedException{
        executorService.execute(new Customer(GasType.REGULAR, 1.33, REGULAR_FUEL_PRICE,1));
        
        executorService.shutdown();
        
        executorService.awaitTermination(MAXIMUM_WAITING_TIME, TimeUnit.SECONDS);
        
    }
    
    
    /**
     * Test for negative amount in litres
     * 
     * @throws InterruptedException 
     */
    @Test(expected = InvalidParameterException.class)
    public void testNegativerAmountInLitres() throws InterruptedException{
        executorService.execute(new Customer(GasType.REGULAR, -1, REGULAR_FUEL_PRICE,1));
        
        executorService.shutdown();
        
        executorService.awaitTermination(MAXIMUM_WAITING_TIME, TimeUnit.SECONDS);
        
    }
    
}
