/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.ui;

import java.util.HashMap;
import javax.swing.ImageIcon;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.UDAServiceId;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

/**
 *
 * @author MyPC
 */
public class Door extends javax.swing.JFrame {

	/**
	 * Creates new form Door
	 */
	private HashMap<String, RemoteDevice> remoteDevices = new HashMap<String, RemoteDevice>();
	private UpnpService upnpService = new UpnpServiceImpl();
	private AirConditional airConditional;
	private Controller ctrl;
	private Light light;
	private boolean hasUser = false;
	private boolean isOn = false;
	private boolean isOnLight = false;

	public Door(Controller ctrl, Light light, AirConditional airConditional) {
		this.ctrl = ctrl;
		this.light = light;
		this.airConditional = airConditional;
		initComponents();
		setResizable(false);
		setTitle("Door");
		ClassLoader classLoader = getClass().getClassLoader();
		setIconImage(new ImageIcon(classLoader.getResource("icon/temperature_sensor.png")).getImage());
		setLocation(500, 0);
		sensorUserCheckbox.setSelected(hasUser);
		upnpService.getRegistry().addListener(createRegistryListener());
		upnpService.getControlPoint().search(new STAllHeader());
	}

	// Services
	public RegistryListener createRegistryListener() {
		return new DefaultRegistryListener() {
			@Override
			public void remoteDeviceAdded(Registry registry, RemoteDevice remoteDevice) {
				addRemoteDevice(remoteDevice);
			}

			@Override
			public void remoteDeviceRemoved(Registry registry, RemoteDevice remoteDevice) {
				removeRemoteDevice(remoteDevice);
			}
		};
	}

	public void callService(String deviceName, String UDAServiceId, String actionType, String attribute, Object value) {
		String actionName = actionType + attribute;
		ActionInvocation<RemoteService> actionInvocation = getActionInvocation(deviceName, UDAServiceId, actionName);
		ActionCallback actionCallback = null;
		if (actionInvocation != null) {
			if (actionType.equals("Set")) {
				actionInvocation.setInput("New" + attribute, value);
				actionCallback = setCallback(actionInvocation);
			} else {
				actionCallback = getCallback(actionInvocation);
			}
			upnpService.getControlPoint().execute(actionCallback);
		}
	}

	public ActionInvocation<RemoteService> getActionInvocation(String deviceName, String UDAServiceId,
			String actionName) {
		try {
			RemoteDevice remoteDevice = remoteDevices.get(deviceName);
			if (remoteDevice != null) {
				RemoteService remoteService = remoteDevice.findService(new UDAServiceId(UDAServiceId));
				if (remoteService != null) {
					Action<RemoteService> actionService = remoteService.getAction(actionName);
					return new ActionInvocation<RemoteService>(actionService);
				}
			} else {
				System.out.println("Remote device not find!");
			}
		} catch (Exception e) {
			System.out.println("System err: " + e);
		}
		return null;
	}

	public ActionCallback setCallback(ActionInvocation<RemoteService> setInvocation) {
		return new ActionCallback(setInvocation) {
			@Override
			public void success(@SuppressWarnings("rawtypes") ActionInvocation invocation) {
				@SuppressWarnings("rawtypes")
				ActionArgumentValue[] args = invocation.getInput();
				for (int i = 0; i < args.length; i++) {
					String argument = args[i].getArgument().getName();
					Object value = args[i].getValue();
					if (argument.equals("NewStatus")) {
						isOn = (Boolean) value;
						ctrl.setControllerStateCheckbox(isOn);
						airConditional.setAirStatusIndexLabel(isOn);
						light.setLightStatus(isOn);
					}
				}

			}

			@Override
			public void failure(@SuppressWarnings("rawtypes") ActionInvocation invocation, UpnpResponse operation,
					String defaultMsg) {
				System.err.println(defaultMsg);
			}
		};
	}

	public ActionCallback getCallback(ActionInvocation<RemoteService> invocation) {
		return new ActionCallback(invocation) {
			@Override
			public void success(@SuppressWarnings("rawtypes") ActionInvocation invocation) {
				@SuppressWarnings("rawtypes")
				ActionArgumentValue[] args = invocation.getOutput();
				for (int i = 0; i < args.length; i++) {
					if (args[i].getArgument().getName().equals("Status")) {
						isOn = (Boolean) args[i].getValue();
					} else if (args[i].getArgument().getName().equals("StatusLight")) {
						isOnLight = (Boolean) args[i].getValue();
					}
				}
			}

			@Override
			public void failure(@SuppressWarnings("rawtypes") ActionInvocation invocation, UpnpResponse operation,
					String defaultMsg) {
				System.err.println(defaultMsg);
			}
		};
	}

	public void addRemoteDevice(RemoteDevice remoteDevice) {
		System.out.println("ADD DEVICE: " + remoteDevice.getType().getDisplayString());
		if (!remoteDevices.containsKey(remoteDevice.getType().getDisplayString()))
			remoteDevices.put(remoteDevice.getType().getDisplayString(), remoteDevice);
	}

	public void removeRemoteDevice(RemoteDevice remoteDevice) {
		System.out.println("REMOVE DEVICE: " + remoteDevice.getType().getDisplayString());
		remoteDevices.remove(remoteDevice.getType().getDisplayString());
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		buttonGroup1 = new javax.swing.ButtonGroup();
		openDoor = new javax.swing.JRadioButton();
		closeDoor = new javax.swing.JRadioButton();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		sensorUserCheckbox = new javax.swing.JCheckBox();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		buttonGroup1.add(openDoor);
		openDoor.setText("ON");

		buttonGroup1.add(closeDoor);
		closeDoor.setText("OFF");
		closeDoor.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				closeDoorStateChanged(evt);
			}
		});

		jLabel1.setText("Door");

		jLabel2.setText("Status");

		jLabel3.setText("User");

		sensorUserCheckbox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sensorUserCheckboxActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(73, 73, 73)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2)
						.addComponent(jLabel3))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
						.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(openDoor).addComponent(jLabel1))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
						.addComponent(closeDoor).addGap(106, 106, 106))
						.addGroup(layout.createSequentialGroup().addComponent(sensorUserCheckbox)
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(83, 83, 83)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(openDoor).addComponent(closeDoor).addComponent(jLabel2))
						.addGap(54, 54, 54)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jLabel3).addComponent(sensorUserCheckbox))
						.addContainerGap(94, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void closeDoorStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_closeDoorStateChanged
		if (hasUser == false && closeDoor.isSelected()) {
			this.callService("Controller", "SwitchPower", "Set", "Status", hasUser);
			if (isOnLight) {
				this.callService("Light", "ChangeStatusLight", "Set", "Status", hasUser);
			}
			this.callService("AirConditional", "SwitchPower", "Set", "Status", hasUser);
		}

	}// GEN-LAST:event_closeDoorStateChanged

	private void sensorUserCheckboxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_sensorUserCheckboxActionPerformed
		this.hasUser = sensorUserCheckbox.isSelected();
	}// GEN-LAST:event_sensorUserCheckboxActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JRadioButton closeDoor;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JRadioButton openDoor;
	private javax.swing.JCheckBox sensorUserCheckbox;
	// End of variables declaration//GEN-END:variables
}
