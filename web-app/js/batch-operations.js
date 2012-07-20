function batch(path, controller)
{
	alert("Path: " + path + "\n" + "Controller: " + controller);
	// Get number of checkboxes on page
	var numBoxes = $("[name='batch']:enabled").size();
	if(numBoxes == 0)
	{
		$("[name='batch-all']").prop("disabled", true);
	}
	// Clear all checkboxes on page load and hide the delete button
	$("[type='checkbox']").each(function(i)
	{
		this.checked = false;
		$("button, #delete").hide();
	});
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
				$("[name='batch-all']").prop("checked", false);
			}
			else
			{
				// Do nothing
			}
		});
	});
	// Show the delete button only when there are checkboxes selected
	$("[type='checkbox']").click(function()
	{
		numChecked = $("[name='batch']:enabled:checked").size();
		if(numChecked > 0)
		{
			$("button, #delete").slideDown(300);
			if(numChecked == numBoxes)
			{
				$("[name='batch-all']").prop("checked", true);
			}
			else
			{
				$("[name='batch-all']").prop("checked", false);
			}
		}
		else
		{
			$("button, #delete").slideUp(300);
			$("[name='batch-all']").prop("checked", false);
		}
	});
}