package net.bigpoint.assessment.gasstation.implementation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

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
    protected final static double REGULAR_FUEL_PRICE = 1.00;
    protected final static double SUPER_FUEL_PRICE = 2.00;
    protected final static double DIESEL_FUEL_PRICE = 3.00;
    
    protected ExecutorService executorService;
   
    
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
        
        executorService = Executors.newFixedThreadPool(10);
        
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
        

        public Customer(GasType gasType, double amountInLitres, double maxPricePerLitre) {
            this.gasType = gasType;
            this.amountInLitres = amountInLitres;
            this.maxPricePerLitre = maxPricePerLitre;
        }
        
        

        public void run() {
            try {
                stationManager.buyGas(gasType, amountInLitres, maxPricePerLitre);
            } catch (NotEnoughGasException ex) {
                Logger.getLogger(BaseGasStationManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (GasTooExpensiveException ex) {
                Logger.getLogger(BaseGasStationManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
            
}
