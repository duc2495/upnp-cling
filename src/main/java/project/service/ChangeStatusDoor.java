package project.service;

import org.fourthline.cling.binding.annotations.*;

//Services
@UpnpService(serviceId = @UpnpServiceId("ChangeStatusDoor"), serviceType = @UpnpServiceType(value = "ChangeStatusDoor", version = 1))
public class ChangeStatusDoor {
	@UpnpStateVariable(defaultValue = "0", sendEvents = false)
	private boolean status = false;

	@UpnpAction
	public void setStatus(@UpnpInputArgument(name = "NewStatusDoor") boolean status) {
		this.status = status;
		System.out.println("Set status Door is: " + this.status);
	}

	@UpnpAction(out = @UpnpOutputArgument(name = "StatusDoor"))
	public boolean getStatus() {
		System.out.println("Get status Door is: " + status);
		return status;
	}
}