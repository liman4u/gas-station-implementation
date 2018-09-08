package net.bigpoint.assessment.gasstation.implementation;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
