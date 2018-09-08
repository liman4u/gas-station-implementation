package net.bigpoint.assessment.gasstation.implementation;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasStation;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

/**
 *
 * @author limanadamu
 * 
 * Gas Station Manager class implements the Gas Station having collection of pumps with various prices of fuel types
 */
public class GasStationManager implements GasStation {
    
    /**
     * Collection of gas pumps
     */
    private CopyOnWriteArrayList<GasPump> gasPumps;
    
    /**
     * Collection of gas types and their prices
     */
    private ConcurrentHashMap<GasType,Double> gasTypePrices;
    
    /**
     * Total number of cancellations because of no gas
     */
    private AtomicInteger noOfCancellationsNoGas;
    
     /**
     * Total number of cancellations because of gas too expensive
     */
    private AtomicInteger noOfCancellationsTooExpensive;
    
    /**
     * Total number of sales
     */
    private AtomicInteger noOfSales;
    
    /**
     * Total revenue made
     */
    private AtomicLong revenue;
    
    /**
     * Util Logger instances - for logging to console
     */
    private static Logger LOG = Logger.getLogger(GasStationManager.class.getName());
    
    public GasStationManager(){
        
        // Initializations
        gasPumps = new CopyOnWriteArrayList<GasPump>();
        gasTypePrices = new ConcurrentHashMap<GasType, Double>();
        
        noOfCancellationsNoGas = new AtomicInteger(0);
        noOfCancellationsTooExpensive = new AtomicInteger(0);
        noOfSales = new AtomicInteger(0);
        revenue = new AtomicLong(0);
        
        
    }

    /**
     * Add a new pump to the collection of gas pumps
     * @param pump 
     */
    public void addGasPump(GasPump pump) {
      this.gasPumps.add(pump);
    }

    /**
     * Get collection of all gas pumps
     * @return gasPumps
     */
    public Collection<GasPump> getGasPumps() {
      return this.gasPumps;
    }

    public double buyGas(GasType type, double amountInLiters, double maxPricePerLiter) throws NotEnoughGasException, GasTooExpensiveException {
      
        
        //Checks for validity of params
        validateParameters(type, amountInLiters, maxPricePerLiter);
        
        //Checks for gas too expensive
        checkGasTooExpensive(type, maxPricePerLiter);
        
        //Price that customer have to pay
        double priceToPay = 0.0d;
        
        //Check for if the gas pump
        boolean gasPumpFound = false;
        
        LOG.log(Level.INFO, "Requested for gas pump type {0} with amount {1}", new Object[]{type, amountInLiters});
        
        //Loops through all gas pumps to get right gas pump
         for(GasPump gasPump : gasPumps){
             
             //Checks for right gas pump per type request
             if(gasPump.getGasType().equals(type)){
                 
                 LOG.log(Level.INFO, "Found the right gas pump : {0}", gasPump.getGasType().name());
                 
                 //Price of gas type
                 double gasTypePrice = gasTypePrices.get(gasPump.getGasType());
                 
                 //Lock gas pump for thread safety , one gas pump operation at a time
                 synchronized(gasPump){
                     
                     //Checks if gas pump has enough fuel
                     if(gasPump.getRemainingAmount() >= amountInLiters){
                         
                        //Serves gas with amountInLiters value
                         gasPump.pumpGas(amountInLiters);
                         
                         priceToPay = amountInLiters * gasTypePrice;
                         
                         noOfSales.incrementAndGet();
                         revenue.addAndGet((long) priceToPay);
                         
                         gasPumpFound = true;
                         
                         LOG.log(Level.INFO, "{0} gas pump remaining amount of {1}", new Object[]{gasPump.getGasType().name(), gasPump.getRemainingAmount()});
                         
                         break;
                     }
                     
                 }
             }
         }
         
         
         //Check if no gas pump was found
         if(!gasPumpFound){
             
             noOfCancellationsNoGas.incrementAndGet();
             throw new NotEnoughGasException();
         }
         
         LOG.log(Level.INFO, "Price to pay {0}", priceToPay);
         
         return priceToPay;
    }
    
    /**
     * Validate Parameters passed
     * 
     * @param type
     * @param amountInLiters
     * @param maxPricePerLiter
     * @throws IllegalArgumentException 
     */
    private void validateParameters(GasType type, double amountInLiters, double maxPricePerLiter) throws IllegalArgumentException {
        if(type == null || !gasTypePrices.containsKey(type) ||  amountInLiters <= 0 || maxPricePerLiter <= 0){
            throw new InvalidParameterException();
        }
    }
    
    /**
     * Check for gas too expensive
     * 
     * @param type
     * @param maxPricePerLiter 
     */
    private void checkGasTooExpensive(GasType type,double maxPricePerLiter) throws GasTooExpensiveException{
        
         //Checks if price of the gas type requested is greater than  - gas too expenisve
        if(maxPricePerLiter < gasTypePrices.get(type)){
            
            //increase number of cancellations for too expensive
            noOfCancellationsTooExpensive.incrementAndGet();
            throw new GasTooExpensiveException();
        }
    }
    
    
    /**
     * Get current revenue
     * 
     * @return (double) revenue
     */
    public double getRevenue() {
        return this.revenue.doubleValue();
    }

    /**
     * Get number of sales
     * 
     * @return (int) noOfSales
     */
    public int getNumberOfSales() {
       return this.noOfSales.get();
    }

    /**
     * Get number of cancellations because of no gas
     * 
     * @return (int) noOfCancellationsNoGas
     */
    public int getNumberOfCancellationsNoGas() {
        return this.noOfCancellationsNoGas.get();
    }

    /**
     * Get number of cancellations because of gas is too expensive
     * 
     * @return (int) noOfCancellationsTooExpensive
     */
    public int getNumberOfCancellationsTooExpensive() {
        return this.noOfCancellationsTooExpensive.get();
    }

    /**
     * Get price for a particular gas type
     * 
     * @param type
     * @return (double) gasTypePrice
     */
    public double getPrice(GasType type) {
        return this.gasTypePrices.get(type);
    }

    /**
     * Set price a particular gas type
     * 
     * @param type
     * @param price 
     */
    public void setPrice(GasType type, double price) {
        this.gasTypePrices.put(type, price);
    }
    
}
