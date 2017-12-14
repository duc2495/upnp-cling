package project.service;

import org.fourthline.cling.binding.annotations.*;

//Services
@UpnpService(serviceId = @UpnpServiceId("SwitchPower"), serviceType = @UpnpServiceType(value = "SwitchPower", version = 1))
public class SwitchPower {
	@UpnpStateVariable
	private String target;

	@UpnpStateVariable(defaultValue = "0", sendEvents = false)
	private boolean status = false;

	@UpnpAction
	public void setTarget(@UpnpInputArgument(name = "NewTarget") String target) {
		this.target = target;
	}

	@UpnpAction
	public void setStatus(@UpnpInputArgument(name = "NewStatus") boolean status) {
		this.status = status;
		System.out.println("Set status is: " + this.status);
	}

	@UpnpAction(out = @UpnpOutputArgument(name = "Status"))
	public boolean getStatus() {
		System.out.println("Get status is: " + status);
		return status;
	}
}
