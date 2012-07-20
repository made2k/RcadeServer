function batch(path, controller)
{	
	// Clear all checkboxes on page load
	$("[type='checkbox']").each(function(i)
	{
		this.checked = false;
	});
	
	alert("Loaded batch-operations.js")
	
	// "Select all" checkbox logic
	$("[name='batch-all']").click(function()
	{
		$("[name='batch']").prop("checked", $("[name='batch-all']").prop("checked"));
	});
	// Delete button logic
	// Uses the Apprise alerts by Daniel Raftery, http://thrivingkings.com/apprise/
	$("button, #delete").click(function()
	{
		str = "Delete selected " + controller + "s?";
		apprise(str, {'verify':true, 'animate':true}, function(r)
		{
			if(r)
			{
				$("[name='batch']:checked").parent().parent().fadeOut("slow");
				$("[name='batch']:checked").each(function(i)
				{
					$.ajax({
						url: path + "/rest/" + controller + "/" + this.value,
						type: "DELETE",
						dataType: 'json',
						success: function(data)
						{
							//alert("Data: " + data);
						},
						error: function(request, status, error)
						{
							//alert("Error: " + error);
						}
					});
				});
			}
			else
			{
				// Do nothing
			}
		});
	});
}