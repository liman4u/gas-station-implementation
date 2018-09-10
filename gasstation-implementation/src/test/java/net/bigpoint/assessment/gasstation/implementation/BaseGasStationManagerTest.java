package net.bigpoint.assessment.gasstation.implementation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;
import org.junit.Test;

/**
 *
 * @author limanadamu
 * 
 * Base class for GasStationManagerTest
 */
public class BaseGasStationManagerTest  extends TestCase{
    
    protected GasStationManager stationManager ;
    
    //Gas Pump litres
    protected final static double REGULAR_FUEL_LITRES = 1000;
    protected final static double SUPER_FUEL_LITRES = 2000;
    protected final static double DIESEL_FUEL_LITRES = 3000;
    
    // Fuel prices per litre
    protected final static double REGULAR_FUEL_PRICE = 0.50;
    protected final static double SUPER_FUEL_PRICE = 1.70;
    protected final static double DIESEL_FUEL_PRICE = 3.20;
    
    //Service for running tasks in asynchronous mode
    protected ExecutorService executorService;
    
    //Maximum waiting time in queue
    protected final static int MAXIMUM_WAITING_TIME = 300;
    
    //Maximum number of executors
    protected final static int MAXIMUM_EXECUTORS = 10;
    
     /**
     * Util Logger instances - for logging to console
     */
    private static Logger LOG = Logger.getLogger(GasStationManagerTest.class.getName());
   
    
    @Override
    public void setUp(){
        
        //Gas Pumps - Regular
        GasPump gasPumpRegular1 = new GasPump(GasType.REGULAR, REGULAR_FUEL_LITRES);
        GasPump gasPumpRegular2 = new GasPump(GasType.REGULAR, REGULAR_FUEL_LITRES);
        
        //Gas Pumps - Super
        GasPump gasPumpSuper1 = new GasPump(GasType.SUPER, SUPER_FUEL_LITRES);
        GasPump gasPumpSuper2 = new GasPump(GasType.SUPER, SUPER_FUEL_LITRES);
        
        //Gas Pumps - Diesel
        GasPump gasPumpDiesel1 = new GasPump(GasType.DIESEL, DIESEL_FUEL_LITRES);
        GasPump gasPumpDiesel2 = new GasPump(GasType.DIESEL, DIESEL_FUEL_LITRES);
        
        
        stationManager = new GasStationManager();
        
        //Prices to be added
        stationManager.setPrice(GasType.REGULAR, REGULAR_FUEL_PRICE);
        stationManager.setPrice(GasType.SUPER, SUPER_FUEL_PRICE);
        stationManager.setPrice(GasType.DIESEL, DIESEL_FUEL_PRICE);
        
        //Pumps to be added
        stationManager.addGasPump(gasPumpRegular1);
        stationManager.addGasPump(gasPumpRegular2);
        
        stationManager.addGasPump(gasPumpSuper1);
        stationManager.addGasPump(gasPumpSuper2);
        
        stationManager.addGasPump(gasPumpDiesel1);
        stationManager.addGasPump(gasPumpDiesel2);
        
        executorService = Executors.newFixedThreadPool(MAXIMUM_EXECUTORS);
        
    }
    
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
    
    @Override
    public void tearDown(){
        stationManager = null;
        
        executorService.shutdown();
    }
    
    /**
     * Customer as gas station client who buys gas
     * 
     * @throws NotEnoughGasException , GasTooExpensiveException 
     */
    public class Customer implements Runnable {
        
        private final GasType gasType;
        private final double amountInLitres;
        private final double maxPricePerLitre;
        private final int customerId;
        

        public Customer(GasType gasType, double amountInLitres, double maxPricePerLitre,int customerId) {
            this.gasType = gasType;
            this.amountInLitres = amountInLitres;
            this.maxPricePerLitre = maxPricePerLitre;
            this.customerId = customerId;
        }
        
        

        public void run() {
            try {
                
                LOG.log(Level.INFO, "Customer - {0} buying gas with gas type - {1} , amount in litres - {2} and max price - {3}",new Object[]{ customerId,amountInLitres,maxPricePerLitre});
                stationManager.buyGas(gasType, amountInLitres, maxPricePerLitre);
            } catch (NotEnoughGasException ex) {
                
                LOG.log(Level.SEVERE, "Not enough gas for customer - {0}", customerId);
            } catch (GasTooExpensiveException ex) {
                LOG.log(Level.SEVERE, "Gas too expensive for customer - {0}", customerId);
            }
        }
        
    }
            
}
