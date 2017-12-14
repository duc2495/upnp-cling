package project.service;

import org.fourthline.cling.binding.annotations.*;

//Services
@UpnpService(serviceId = @UpnpServiceId("ChangeStatusLight"), serviceType = @UpnpServiceType(value = "ChangeStatusLight", version = 1))
public class ChangeStatusLight {
	@UpnpStateVariable(defaultValue = "0", sendEvents = false)
	private boolean status = false;

	@UpnpAction
	public void setStatus(@UpnpInputArgument(name = "NewStatusLight") boolean status) {
		this.status = status;
		System.out.println("Set status Light is: " + this.status);
	}

	@UpnpAction(out = @UpnpOutputArgument(name = "StatusLight"))
	public boolean getStatus() {
		System.out.println("Get status Light is: " + status);
		return status;
	}
}
