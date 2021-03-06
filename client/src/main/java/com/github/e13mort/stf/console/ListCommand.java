package com.github.e13mort.stf.console;

import com.github.e13mort.stf.client.DevicesParams;
import com.github.e13mort.stf.client.FarmClient;
import com.github.e13mort.stf.model.device.Device;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

class ListCommand implements Commands.Command {
    public static final String UNKNOWN_DEVICE_NAME = "<Unknown>";
    private final FarmClient client;

    ListCommand(FarmClient client) {
        this.client = client;
    }

    @Override
    public void execute(RunOptions options) {
        DevicesParams params = options.getDeviceParams();
        client.getDevices(params).subscribe(new Consumer<Device>() {
            @Override
            public void accept(@NonNull Device device) throws Exception {
                print(device);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                print(throwable);
            }
        });
    }

    private void print(Device device) {
        String name = device.getName();
        name = name != null ? name : UNKNOWN_DEVICE_NAME;
        System.out.println(String.format(
                "%10s abi: %7s serial: %s, sdk: %s",
                name, device.getAbi(), device.getSerial(), device.getSdk()));
    }

    private void print(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }
}
