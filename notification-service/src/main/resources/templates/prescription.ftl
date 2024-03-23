<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<p>Dear ${Prescription.userName},</p>
<p>Please find the below prescription from Dr. ${Prescription.userName} prescribed during your visit on ${Prescription.appointmentDate}
at ${Prescription.appointmentTimeSlot} in BMC Hospital.</p>
<p>Diagnois: ${Prescription.diagnosis}</p>
<table id="timelineTable" class="timeline" border = "4">
	<tr>
		<td>Medicine name</td>
		<td>Dosage</td>
		<td>Frequency</td>
		<td>Remarks</td>
	</tr>	
	<#list Prescription.medicineList as medicines>
    <tr>
    	<td>${medicines.name}</td>
		<td>${medicines.dosage}</td>
		<td>${medicines.frequency}</td>
		<td>${medicines.remarks}</td>
    </tr>
	<#else>
    <td colspan="4">No Medicine Prescribed.</td>
	</#list>
</table>
<p>Regards,</p>
<p>
    <em>BMC</em> <br />
</p>
</body>
</html>