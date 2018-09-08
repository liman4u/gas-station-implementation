package net.bigpoint.assessment.gasstation.implementation;

import net.bigpoint.assessment.gasstation.GasType;
import org.junit.Test;

/**
 *
 * @author limanadamu
 * 
 * Tests for gas station manager
 */
public class GasStationManagerTest extends BaseGasStationManagerTest{
    
    @Test
    public void testGasStationSetup(){
        
        assertNotNull(stationManager);
        
        //Test if all gas type have prices 
        assertTrue(stationManager.getPrice(GasType.REGULAR) > 0);
        assertTrue(stationManager.getPrice(GasType.SUPER) > 0);
        assertTrue(stationManager.getPrice(GasType.DIESEL) > 0);
        
        
        assertEquals(stationManager.getGasPumps().size(),6);
        
    }
    
    
}
