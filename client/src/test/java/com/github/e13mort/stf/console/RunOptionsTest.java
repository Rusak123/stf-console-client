package com.github.e13mort.stf.console;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static com.github.e13mort.stf.console.RunOptions.Operation.*;
import static org.junit.jupiter.api.Assertions.*;

public class RunOptionsTest {

    private Options options;

    @BeforeEach
    public void setUp() throws Exception {
        options = RunOptions.createOptions();
    }

    @Test
    public void testDeviceParamsAreNotNullInEmptyString() throws Exception {
        RunOptions options = getRunOptions("");
        assertNotNull(options.getDeviceParams());
    }

    @Test
    public void testReadDefaultPropertiesPathFromEmptyString() throws Exception {
        RunOptions runOptions = getRunOptions("");
        assertEquals("farm.properties", runOptions.getFarmPropertiesFileName());
    }

    @Test
    public void testReadPropertiesPath() throws Exception {
        RunOptions runOptions = getRunOptions("-p path");
        assertEquals("path", runOptions.getFarmPropertiesFileName());
    }

    @Test
    public void testUnknownCommandIsDefault() throws Exception {
        RunOptions options = getRunOptions("");
        assertEquals(RunOptions.Operation.UNKNOWN ,options.getOperation());
    }

    @Test
    public void testReadListCommandFromNormalString() throws Exception {
        RunOptions options = getRunOptions("-l");
        assertEquals(RunOptions.Operation.LIST ,options.getOperation());
    }

    @Test
    public void testReadAbiFromValidString() throws Exception {
        RunOptions options = getRunOptions("-abi test_abi");
        assertEquals("test_abi", options.getDeviceParams().getAbi());
    }

    @Test
    public void testAbiIsNullInUnrelatedString() throws Exception {
        RunOptions options = getRunOptions("-api 21");
        assertNull(options.getDeviceParams().getAbi());
    }

    @Test
    public void testAllDevicesIsFalseInEmptyString() throws Exception {
        RunOptions options = getRunOptions("");
        assertFalse(options.getDeviceParams().isAllDevices());
    }

    @Test
    public void testAllDevicesEnabledByFlag() throws Exception {
        RunOptions options = getRunOptions("-l -all");
        assertTrue(options.getDeviceParams().isAllDevices());
    }

    @Test
    public void testApiVersionIsZeroInUnrelatedString() throws Exception {
        RunOptions options = getRunOptions("-l");
        assertEquals(0, options.getDeviceParams().getApiVersion());
    }

    @Test
    public void testApiVersionIsValidInParameter() throws Exception {
        RunOptions options = getRunOptions("-l -api 19");
        assertEquals(19, options.getDeviceParams().getApiVersion());
    }

    @Test
    public void testMinApiVersionIsValidInParameter() throws Exception {
        RunOptions options = getRunOptions("-l -minApi 19");
        assertEquals(19, options.getDeviceParams().getMinApiVersion());
    }

    @Test
    public void testMaxApiVersionIsValidInParameter() throws Exception {
        RunOptions options = getRunOptions("-l -maxApi 19");
        assertEquals(19, options.getDeviceParams().getMaxApiVersion());
    }

    @Test
    public void testExceptionIsThrownWhenApiIsInvalid() throws Exception {
        assertThrows(NumberFormatException.class, test("-api not_a_number"));
    }

    @Test
    public void testUnknownPropertyWillBreakHandling() throws Exception {
        assertThrows(Exception.class, test("-api 21 -!!! -abi test_abi"));
    }

    @Test
    public void testCountPropertyIsValid() throws Exception {
        RunOptions options = getRunOptions("-count 10");
        assertEquals(10, options.getDeviceParams().getCount());
    }

    @Test
    public void testInvalidCountPropertyThrowsAnException() throws Exception {
        assertThrows(NumberFormatException.class, test("-count not_a_number"));
    }

    @Test
    public void testVoidCountPropertyThrowsAnException() throws Exception {
        assertThrows(Exception.class, test("-count -l"));
    }

    @Test
    public void testNamePropertyIsValid() throws Exception {
        assertEquals("name", getRunOptions("-n name").getDeviceParams().getName());
    }

    @Test
    public void testVoidNameThrowsAnException() throws Exception {
        assertThrows(Exception.class, test("-n -l"));
    }

    @Test
    public void testReadConnectOperationFromNormalString() throws Exception {
        assertEquals(CONNECT, getRunOptions("-c").getOperation());
    }

    @Test
    public void testReadDisconnectOperationFromNormalString() throws Exception {
        assertEquals(DISCONNECT, getRunOptions("-d").getOperation());
    }

    @Disabled
    @Test
    public void testFewCommandParamsReturnsUnknownOperation() throws Exception {
        assertEquals(UNKNOWN, getRunOptions("-l -c").getOperation());
    }

    @Test
    public void testProviderDescriptionNotNullWithParameter() throws Exception {
        RunOptions options = getRunOptions("-l -provider p1");
        assertNotNull(options.getDeviceParams().getProviderDescription());
    }

    @Test
    public void testProviderDescriptionNullWithoutParameter() throws Exception {
        RunOptions options = getRunOptions("-l");
        assertNull(options.getDeviceParams().getProviderDescription());
    }

    private Executable test(final String str) {
        return new Executable() {
            @Override
            public void execute() throws Throwable {
                getRunOptions(str);
            }
        };
    }

    private RunOptions getRunOptions(String str) throws ParseException {
        return RunOptions.create(options, str.split(" "));
    }
}