<%@ page import="rcadeserver.Connection" %>



<div class="fieldcontain ${hasErrors(bean: connectionInstance, field: 'ipAddress', 'error')} required">
	<label for="ipAddress">
		<g:message code="connection.ipAddress.label" default="Ip Address" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="ipAddress" required="" value="${connectionInstance?.ipAddress}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: connectionInstance, field: 'port', 'error')} required">
	<label for="port">
		<g:message code="connection.port.label" default="Port" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="port" required="" value="${connectionInstance?.port}"/>
</div>

