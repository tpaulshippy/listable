# listable
Mobile app to show items from an imported list as persistent interactive notifications.

Initial use case
--------------------
As a user of Out of Milk, I have a list of grocery items I need to purchase. 
I use grocery.walmart.com to order my items and I want a more convenient way to go through the list of items 
without having to switch between the list app and my browser.

Initial implementation idea
-----------------------------
1. The app accepts Out of Milk (and, eventually, other list apps) list data via "Share To" functionality.
2. The app shows each item from the list as a persistent notification that has three options:  
  [ Fill ] - Copies and pastes the text of the item into the active text box (would probably require extra permissions on the device).  
  [ Done ] - Moves to the next item on the list and marks this item completed.  
  [ Skip ] - Moves to the next item on the list and marks this item uncompleted.  
3. After the last item on the list, show the following options:  
  [ View List ] - show a list of the items with some indication of whether items were done or not, to help synchronize completed state with original app.  
  [ Restart Skipped ] - Go back to the first item that was skipped.

