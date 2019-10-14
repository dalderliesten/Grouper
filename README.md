# Grouper
Grouper is a simple (and somewhat inefficient Java application) that allows for the creation of groups based on properties that Brightspace itself doesn't support. This then generates a comma separated values file (CSV) which can be imported into Brightspace for automatic groups generation.

## Exporting the Required Files
To use Grouper, please go to the Brightspace course you manage and select the 'grades' area in the header. Then perform the steps below:

* In the grades view, select the export button at the top.
* In the selection screen that appears, under 'key field,' select 'Org Defined ID.'
* Under the 'grade values' option, select 'Points grade.'
* Deselect all values under 'User Details,' meaning nothing is selected.
* Under 'Choose Grades to Export,' select all grades you wish to consider.
* Click the 'Export to CSV' button at the bottom of the page.
