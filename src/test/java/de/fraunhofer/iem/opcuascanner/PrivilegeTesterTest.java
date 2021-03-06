package de.fraunhofer.iem.opcuascanner;

import de.fraunhofer.iem.opcuascanner.logic.AccessPrivileges;
import de.fraunhofer.iem.opcuascanner.logic.Authentication;
import de.fraunhofer.iem.opcuascanner.logic.Privilege;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrivilegeTesterTest {

    @Test
    public void testSetOtherPrivilegesToTestedOnlySetsIfConnectTested(){
        AccessPrivileges accessPrivileges = new AccessPrivileges();
        Authentication auth = Authentication.ANONYMOUSLY;
        PrivilegeTester.setOtherPrivilegesToTestedIfUnableToConnect(accessPrivileges, auth);
        for (Privilege privilege : Privilege.values()){
            if (!privilege.equals(Privilege.CONNECT)){
                assertFalse("Privilege was not tested yet.", accessPrivileges.wasTested(privilege, auth));
            }
        }
    }

    @Test
    public void testSetOtherPrivilegesToTestedDoesNotSetIfConnectTrue(){
        AccessPrivileges accessPrivileges = new AccessPrivileges();
        Authentication auth = Authentication.ANONYMOUSLY;
        accessPrivileges.setPrivilegeWasTested(Privilege.CONNECT, auth);
        accessPrivileges.setPrivilegePerAuthentication(Privilege.CONNECT, auth);
        PrivilegeTester.setOtherPrivilegesToTestedIfUnableToConnect(accessPrivileges, auth);
        for (Privilege privilege : Privilege.values()){
            if (!privilege.equals(Privilege.CONNECT)){
                assertFalse("Privilege was not tested yet.", accessPrivileges.wasTested(privilege, auth));
            }
        }
    }

    @Test
    public void testSetOtherPrivilegesToTestedSetsIfConnectNotTrue(){
        AccessPrivileges accessPrivileges = new AccessPrivileges();
        Authentication auth = Authentication.ANONYMOUSLY;
        accessPrivileges.setPrivilegeWasTested(Privilege.CONNECT, auth);
        PrivilegeTester.setOtherPrivilegesToTestedIfUnableToConnect(accessPrivileges, auth);
        for (Privilege privilege : Privilege.values()){
            if (!privilege.equals(Privilege.CONNECT)){
                assertTrue("Privilege was implicitly tested.", accessPrivileges.wasTested(privilege, auth));
            }
        }
    }

    //TODO Test remaining functions

}
